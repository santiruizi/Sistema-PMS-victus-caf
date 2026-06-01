package com.victuscaf.modules.client.service;

import com.victuscaf.modules.client.models.*;
import com.victuscaf.modules.client.repository.*;
import com.victuscaf.modules.pago.dto.*;
import com.victuscaf.modules.client.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final UsuarioRepository usuarioRepository;
    private final ParticularMensualRepository particularMensualRepository;
    private final PagoRepository pagoRepository;
    private final TarifaRepository tarifaRepository;
    private final FlujoDeCajaRepository flujoDeCajaRepository;
    private final GastoOperativoRepository gastoOperativoRepository;
    private final FacturaEpsRepository facturaEpsRepository;
    private final BitacoraAccionRepository bitacoraRepository;

    // ========== REGISTRO DE PAGOS ==========

    @Transactional
    public Pago registrarPagoMensualidad(Long numeroDocumento, MetodoDePago metodo, double valor) {
        ParticularMensual cliente = particularMensualRepository.findByNumeroDeDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Tarifa tarifa = tarifaRepository.findByTipoDeCliente(TipoDeCliente.PARTICULAR_MENSUAL)
                .orElseThrow(() -> new RuntimeException("Tarifa no configurada"));
        if (valor != tarifa.getValorMensual()) {
            throw new RuntimeException("El valor debe ser igual a la mensualidad vigente: " + tarifa.getValorMensual());
        }
        Pago pago = crearPago(cliente, valor, metodo, TipoDePago.MEMBRESIA, EstadoPago.EXITOSO);
        // Reactivar membresía si estaba vencida/bloqueada
        cliente.setEstadoMembresia(EstadoMembresia.ACTIVO);
        // Renovar contrato (extender fecha de vencimiento)
        Contrato contrato = cliente.getContrato();
        contrato.setFechaVencimiento(contrato.getFechaVencimiento().plusMonths(1));
        particularMensualRepository.save(cliente);
        return pago;
    }

    @Transactional
    public Pago registrarPagoDiario(Long numeroDocumento, MetodoDePago metodo, double valor) {
        ParticularDiario cliente = particularDiarioRepository.findByNumeroDeDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Tarifa tarifa = tarifaRepository.findByTipoDeCliente(TipoDeCliente.PARTICULAR_DIARIO)
                .orElseThrow(() -> new RuntimeException("Tarifa no configurada"));
        if (valor != tarifa.getValorDiario()) {
            throw new RuntimeException("Valor incorrecto. Debe pagar: " + tarifa.getValorDiario());
        }
        Pago pago = crearPago(cliente, valor, metodo, TipoDePago.DIARIO, EstadoPago.EXITOSO);
        // Actualizar acceso diario: nueva fecha y hora, expiración en 2 horas
        cliente.setFechaDeIngreso(LocalDate.now());
        cliente.setHoraIngreso(java.time.LocalTime.now());
        cliente.setHoraExpiracion(java.time.LocalTime.now().plusHours(2));
        particularDiarioRepository.save(cliente);
        return pago;
    }

    @Transactional
    public Pago registrarCopagoEPS(Long numeroDocumento, MetodoDePago metodo, double valor) {
        BeneficiarioEps cliente = beneficiarioEpsRepository.findByNumeroDeDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Beneficiario no encontrado"));
        Tarifa tarifa = tarifaRepository.findByTipoDeCliente(TipoDeCliente.PARTICULAR_MENSUAL)
                .orElseThrow(() -> new RuntimeException("Tarifa no configurada"));
        double copagoEsperado = tarifa.getValorMensual() * 0.03;
        if (Math.abs(valor - copagoEsperado) > 0.01) {
            throw new RuntimeException("Valor de copago incorrecto. Debe ser: " + copagoEsperado);
        }
        Pago pago = crearPago(cliente, valor, metodo, TipoDePago.COPAGO, EstadoPago.EXITOSO);
        // Si estaba bloqueado por copago, se desbloquea automáticamente (quitar bloqueo)
        if (cliente.getEstadoContrato() == EstadoContratoEps.BLOQUEADO) {
            cliente.setEstadoContrato(EstadoContratoEps.ACTIVO);
            beneficiarioEpsRepository.save(cliente);
        }
        return pago;
    }

    private Pago crearPago(Usuario usuario, double valor, MetodoDePago metodo, TipoDePago tipo, EstadoPago estado) {
        Pago pago = new Pago();
        pago.setUsuario(usuario);
        pago.setValor(valor);
        pago.setMetodoPago(metodo);
        pago.setTipoPago(tipo);
        pago.setEstadoPago(estado);
        pago.setFechaPago(LocalDate.now());
        return pagoRepository.save(pago);
    }

    // ========== GASTOS OPERATIVOS ==========

    @Transactional
    public GastoOperativo registrarPagoNomina(Long numeroEntrenador, double valor) {
        Entrenador entrenador = entrenadorRepository.findByNumeroDeDocumento(numeroEntrenador)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));
        GastoOperativo gasto = new GastoOperativo();
        gasto.setFecha(LocalDate.now());
        gasto.setDescripcion("Pago de nómina a " + entrenador.getNombreCompleto());
        gasto.setValor(valor);
        gasto.setTipoGasto(TipoGasto.NOMINA);
        // Asociar al flujo de caja del día (se hará al cierre)
        return gastoOperativoRepository.save(gasto);
    }

    @Transactional
    public GastoOperativo registrarPagoArriendo(double valor, String descripcion) {
        GastoOperativo gasto = new GastoOperativo();
        gasto.setFecha(LocalDate.now());
        gasto.setDescripcion(descripcion);
        gasto.setValor(valor);
        gasto.setTipoGasto(TipoGasto.ARRIENDO);
        return gastoOperativoRepository.save(gasto);
    }

    @Transactional
    public GastoOperativo registrarPagoMantenimiento(Long idEquipo, double valor, String descripcion) {
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
        GastoOperativo gasto = new GastoOperativo();
        gasto.setFecha(LocalDate.now());
        gasto.setDescripcion(descripcion + " - Equipo: " + equipo.getNombre());
        gasto.setValor(valor);
        gasto.setTipoGasto(TipoGasto.MANTENIMIENTO);
        return gastoOperativoRepository.save(gasto);
    }

    // ========== FLUJO DE CAJA ==========

    @Transactional
    public FlujoDeCaja generarFlujoCajaDiario(LocalDate fecha) {
        if (flujoDeCajaRepository.findByFecha(fecha).isPresent()) {
            throw new RuntimeException("El flujo de caja para esta fecha ya fue generado.");
        }
        List<Pago> pagos = pagoRepository.findByFechaPago(fecha);
        List<GastoOperativo> gastos = gastoOperativoRepository.findByFecha(fecha);

        double totalIngresos = pagos.stream().mapToDouble(Pago::getValor).sum();
        double totalIngresosParticular = pagos.stream().filter(p -> p.getTipoPago() == TipoDePago.MEMBRESIA).mapToDouble(Pago::getValor).sum();
        double totalIngresosDiario = pagos.stream().filter(p -> p.getTipoPago() == TipoDePago.DIARIO).mapToDouble(Pago::getValor).sum();
        double totalIngresosCopago = pagos.stream().filter(p -> p.getTipoPago() == TipoDePago.COPAGO).mapToDouble(Pago::getValor).sum();
        double totalIngresosPlus = pagos.stream().filter(p -> p.getTipoPago() == TipoDePago.PLUS).mapToDouble(Pago::getValor).sum();

        double totalEgresos = gastos.stream().mapToDouble(GastoOperativo::getValor).sum();
        double egresosMantenimiento = gastos.stream().filter(g -> g.getTipoGasto() == TipoGasto.MANTENIMIENTO).mapToDouble(GastoOperativo::getValor).sum();
        double egresosNomina = gastos.stream().filter(g -> g.getTipoGasto() == TipoGasto.NOMINA).mapToDouble(GastoOperativo::getValor).sum();
        double egresosArriendo = gastos.stream().filter(g -> g.getTipoGasto() == TipoGasto.ARRIENDO).mapToDouble(GastoOperativo::getValor).sum();

        FlujoDeCaja flujo = new FlujoDeCaja();
        flujo.setFecha(fecha);
        flujo.setTotalIngresoParticular(totalIngresosParticular);
        flujo.setTotalIngresoParticularDiario(totalIngresosDiario);
        flujo.setTotalIngresoCopagoEps(totalIngresosCopago);
        flujo.setTotalIngresoPlus(totalIngresosPlus);
        flujo.setTotalEgresosMantenimiento(egresosMantenimiento);
        flujo.setTotalEgresosNomina(egresosNomina);
        flujo.setTotalEgresosArriendo(egresosArriendo);
        flujo.setUtilidadNeta(totalIngresos - totalEgresos);
        flujo.setGastos(gastos);

        // Asociar gastos al flujo de caja
        gastos.forEach(g -> g.setFlujoDeCaja(flujo));
        gastoOperativoRepository.saveAll(gastos);

        return flujoDeCajaRepository.save(flujo);
    }

    // ========== FACTURACIÓN EPS ==========

    @Transactional
    public FacturaEps generarFacturaEPS(String periodo) {
        if (facturaEpsRepository.findByPeriodoConsolidado(periodo).isPresent()) {
            throw new RuntimeException("La factura para este período ya existe.");
        }
        // Calcular total a partir de las asistencias de beneficiarios EPS en el período
        // (Simplificado: aquí deberías sumar las sesiones y aplicar tarifa por sesión)
        FacturaEps factura = new FacturaEps();
        factura.setPeriodoConsolidado(periodo);
        factura.setFechaGeneracion(LocalDate.now());
        factura.setEstado(EstadoFacturaEps.PENDIENTE);
        // valorTotal calculado
        factura.setValorTotal(0.0); // placeholder
        return facturaEpsRepository.save(factura);
    }

    // ========== TARIFAS ==========

    @Transactional
    public Tarifa actualizarTarifa(TipoDeCliente tipo, double nuevoValorMensual, double nuevoValorDiario, double nuevoValorPlus, Long idAdmin) {
        Tarifa tarifa = tarifaRepository.findByTipoDeCliente(tipo)
                .orElse(new Tarifa());
        tarifa.setTipoDeCliente(tipo);
        tarifa.setValorMensual(nuevoValorMensual);
        tarifa.setValorDiario(nuevoValorDiario);
        tarifa.setValorPlus(nuevoValorPlus);
        tarifa.setFechaUltimaModificacion(LocalDate.now());
        tarifa.setIdAdminModificador(idAdmin);
        // Registrar en bitácora (lo hará el servicio de seguridad)
        return tarifaRepository.save(tarifa);
    }
}