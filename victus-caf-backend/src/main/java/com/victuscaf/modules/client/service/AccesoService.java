package com.victuscaf.modules.client.service;

import com.victuscaf.modules.client.models.*;
import com.victuscaf.modules.client.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccesoService {

    private final UsuarioRepository usuarioRepository;
    private final BeneficiarioEpsRepository beneficiarioEpsRepository;
    private final ParticularMensualRepository particularMensualRepository;
    private final ParticularDiarioRepository particularDiarioRepository;
    private final RemisionRepository remisionRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final PagoRepository pagoRepository;
    private final FacturaEpsRepository facturaEpsRepository;
    private final PagoService pagoService;  // para lógica de pagos si es necesario, aunque aquí no se usa directamente

    // ==================== REGISTRO DE INGRESO PRINCIPAL ====================

    @Transactional
    public Asistencia registrarIngreso(Long numeroDocumento) {
        Usuario usuario = usuarioRepository.findByNumeroDeDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!usuario.getEstado()) {
            throw new RuntimeException("Cliente inactivo. No puede ingresar.");
        }

        return switch (usuario.getTipoDeCliente()) {
            case PARTICULAR_MENSUAL -> registrarIngresoMensual((ParticularMensual) usuario);
            case PARTICULAR_DIARIO -> registrarIngresoDiario((ParticularDiario) usuario);
            case BENEFICIARIO_EPS -> registrarIngresoEPS((BeneficiarioEps) usuario);
        };
    }

    // ==================== INGRESO POR TIPO DE CLIENTE ====================

    private Asistencia registrarIngresoMensual(ParticularMensual mensual) {
        if (mensual.getEstadoMembresia() != EstadoMembresia.ACTIVO) {
            throw new RuntimeException("Membresía no activa. No puede ingresar.");
        }
        // Verificar si tiene pagos pendientes (pagos no realizados en la fecha de vencimiento)
        if (tieneSaldoPendiente(mensual)) {
            throw new RuntimeException("Pago pendiente. No puede ingresar.");
        }
        return guardarAsistencia(mensual);
    }

    private Asistencia registrarIngresoDiario(ParticularDiario diario) {
        // Verificar si pagó hoy (buscando un pago de tipo DIARIO en el repositorio)
        boolean pagoHoy = pagoRepository.findByUsuario(diario).stream()
                .anyMatch(p -> p.getTipoPago() == TipoDePago.DIARIO &&
                        p.getFechaPago().equals(LocalDate.now()) &&
                        p.getEstadoPago() == EstadoPago.EXITOSO);

        if (!pagoHoy) {
            throw new RuntimeException("No ha pagado el acceso diario para hoy.");
        }

        // Verificar si el acceso aún no ha expirado
        if (diario.getHoraExpiracion() != null && LocalTime.now().isAfter(diario.getHoraExpiracion())) {
            throw new RuntimeException("Acceso diario expirado (2 horas).");
        }
        return guardarAsistencia(diario);
    }

    private Asistencia registrarIngresoEPS(BeneficiarioEps eps) {
        // Verificar estado del contrato EPS
        if (eps.getEstadoContrato() != EstadoContratoEps.ACTIVO) {
            throw new RuntimeException("Contrato EPS no activo o bloqueado. Verifique su estado.");
        }

        // Verificar si tiene copago pendiente (último mes sin pago)
        if (tieneCopagoPendiente(eps)) {
            // Opcional: notificar a la EPS (podría ser un método en NotificacionService)
            throw new RuntimeException("Copago pendiente. No puede ingresar hasta pagarlo.");
        }

        Remision remision = eps.getRemision();
        if (remision == null || remision.getEstado() != EstadoRemision.ACTIVO) {
            throw new RuntimeException("No tiene una remisión activa.");
        }

        int sesionesRestantes = remision.calcularSesionesRestantes();
        if (sesionesRestantes <= 0) {
            // Finalizar remisión y contrato
            remision.setEstado(EstadoRemision.FINALIZADO);
            remisionRepository.save(remision);
            eps.setEstadoContrato(EstadoContratoEps.FINALIZADO);
            beneficiarioEpsRepository.save(eps);
            throw new RuntimeException("Sesiones agotadas. Remisión finalizada.");
        }

        // Registrar asistencia y descontar sesión
        Asistencia asistencia = guardarAsistencia(eps);
        remision.setSesionesAsistidas(remision.getSesionesAsistidas() + 1);
        if (remision.calcularSesionesRestantes() == 0) {
            remision.setEstado(EstadoRemision.FINALIZADO);
            eps.setEstadoContrato(EstadoContratoEps.FINALIZADO);
        }
        remisionRepository.save(remision);
        beneficiarioEpsRepository.save(eps);

        // Asociar la asistencia a la factura EPS del mes actual
        asociarAsistenciaAFactura(eps, asistencia);

        return asistencia;
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private Asistencia guardarAsistencia(Usuario usuario) {
        Asistencia a = new Asistencia();
        a.setUsuario(usuario);
        a.setFechaIngreso(LocalDate.now());
        a.setHoraIngreso(LocalTime.now());
        return asistenciaRepository.save(a);
    }

    private void asociarAsistenciaAFactura(BeneficiarioEps eps, Asistencia asistencia) {
        String periodo = LocalDate.now().withDayOfMonth(1).toString().substring(0, 7); // YYYY-MM
        FacturaEps factura = facturaEpsRepository.findByPeriodoConsolidado(periodo)
                .orElseGet(() -> {
                    FacturaEps f = new FacturaEps();
                    f.setPeriodoConsolidado(periodo);
                    f.setEntidadEps(eps.getRemision().getEntidadEps());
                    f.setFechaGeneracion(LocalDate.now());
                    f.setEstado(EstadoFacturaEps.PENDIENTE);
                    return facturaEpsRepository.save(f);
                });
        factura.getAsistencias().add(asistencia);
        // Recalcular valor total (debería ser un método aparte en FacturaEpsService)
        facturaEpsRepository.save(factura);
    }

    /**
     * Verifica si un cliente particular mensual tiene saldo pendiente.
     * Se considera saldo pendiente si no ha pagado la mensualidad del mes actual.
     * (Simplificado: busca pagos de tipo MEMBRESIA en los últimos 30 días)
     */
    private boolean tieneSaldoPendiente(ParticularMensual mensual) {
        LocalDate haceUnMes = LocalDate.now().minusDays(30);
        return pagoRepository.findByUsuarioAndTipoPago(mensual, TipoDePago.MEMBRESIA).stream()
                .noneMatch(p -> p.getFechaPago().isAfter(haceUnMes) && p.getEstadoPago() == EstadoPago.EXITOSO);
    }

    /**
     * Verifica si un beneficiario EPS tiene copago pendiente.
     * Se considera pendiente si no ha pagado el copago del mes actual.
     */
    private boolean tieneCopagoPendiente(BeneficiarioEps eps) {
        LocalDate haceUnMes = LocalDate.now().minusDays(30);
        return pagoRepository.findByUsuarioAndTipoPago(eps, TipoDePago.COPAGO).stream()
                .noneMatch(p -> p.getFechaPago().isAfter(haceUnMes) && p.getEstadoPago() == EstadoPago.EXITOSO);
    }
}