package com.victuscaf.modules.service;

import com.victuscaf.modules.acceso.dto.RegistroIngresoDTO;
import com.victuscaf.modules.client.model.*;
import com.victuscaf.modules.client.repository.*;
import com.victuscaf.modules.client.repository.PagoRepository;
import com.victuscaf.modules.notificacion.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;

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
    private final NotificacionService notificacionService;
    private final FacturaEpsRepository facturaEpsRepository;

    /**
     * Registra el ingreso de un cliente.
     * Flujo completo: verifica estado, membresía/contrato, pago pendiente, sesiones EPS, etc.
     */
    @Transactional
    public Asistencia registrarIngreso(Long numeroDocumento) {
        Usuario usuario = usuarioRepository.findByNumeroDeDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!usuario.getEstado()) {
            throw new RuntimeException("Cliente inactivo. No puede ingresar.");
        }

        // Flujo según tipo de cliente
        if (usuario.getTipoDeCliente() == TipoDeCliente.PARTICULAR_MENSUAL) {
            return registrarIngresoMensual((ParticularMensual) usuario);
        } else if (usuario.getTipoDeCliente() == TipoDeCliente.PARTICULAR_DIARIO) {
            return registrarIngresoDiario((ParticularDiario) usuario);
        } else if (usuario.getTipoDeCliente() == TipoDeCliente.BENEFICIARIO_EPS) {
            return registrarIngresoEPS((BeneficiarioEps) usuario);
        } else {
            throw new RuntimeException("Tipo de cliente no válido");
        }
    }

    private Asistencia registrarIngresoMensual(ParticularMensual mensual) {
        if (mensual.getEstadoMembresia() != EstadoMembresia.ACTIVO) {
            throw new RuntimeException("Membresía no activa. No puede ingresar.");
        }
        // Verificar pago pendiente (simulación)
        if (tieneSaldoPendiente(mensual)) {
            throw new RuntimeException("Pago pendiente. No puede ingresar.");
        }
        return guardarAsistencia(mensual);
    }

    private Asistencia registrarIngresoDiario(ParticularDiario diario) {
        // Para diario, debe haber pagado hoy y no tener acceso expirado
        boolean pagoHoy = pagoRepository.existsByUsuarioAndFechaPago(diario, LocalDate.now());
        if (!pagoHoy) {
            throw new RuntimeException("No ha pagado el acceso diario.");
        }
        if (diario.getHoraExpiracion() != null && LocalTime.now().isAfter(diario.getHoraExpiracion())) {
            throw new RuntimeException("Acceso diario expirado (2 horas).");
        }
        return guardarAsistencia(diario);
    }

    private Asistencia registrarIngresoEPS(BeneficiarioEps eps) {
        if (eps.getEstadoContrato() != EstadoContratoEps.ACTIVO) {
            throw new RuntimeException("Contrato EPS no activo.");
        }
        // Verificar copago pendiente
        if (tieneCopagoPendiente(eps)) {
            notificacionService.generarReporteCopagoPendiente(eps);
            throw new RuntimeException("Copago pendiente. No puede ingresar hasta pagarlo.");
        }

        Remision remision = eps.getRemision();
        if (remision == null || remision.getEstado() != EstadoRemision.ACTIVO) {
            throw new RuntimeException("No tiene una remisión activa.");
        }
        if (remision.calcularSesionesRestantes() <= 0) {
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

        // Asociar asistencia a factura EPS del mes actual
        asociarAsistenciaAFactura(eps, asistencia);

        return asistencia;
    }

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
        // Recalcular valor total (servicio aparte)
        facturaEpsRepository.save(factura);
    }

    private boolean tieneSaldoPendiente(Usuario usuario) {
        // Lógica real: consultar pagos no completados o facturas vencidas
        return false; // placeholder
    }

    private boolean tieneCopagoPendiente(BeneficiarioEps eps) {
        // Lógica real: verificar si existe un pago de tipo COPAGO no completado
        return false; // placeholder
    }
}