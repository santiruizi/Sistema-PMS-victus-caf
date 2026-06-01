package com.victuscaf.modules.client.service;

import com.victuscaf.modules.client.models.*;
import com.victuscaf.modules.client.repository.*;
import com.victuscaf.modules.client.repository.*;
import com.victuscaf.modules.client.repository.BitacoraAccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final ContratoRepository contratoRepository;
    private final RemisionRepository remisionRepository;
    private final FlujoDeCajaRepository flujoDeCajaRepository;
    private final BitacoraAccionRepository bitacoraRepository;
    private final BeneficiarioEpsRepository beneficiarioEpsRepository;

    public List<Contrato> consultarContratosVencidos() {
        return contratoRepository.findByFechaVencimientoBefore(LocalDate.now());
    }

    public List<BeneficiarioEps> consultarBeneficiariosEPS() {
        return beneficiarioEpsRepository.findAll();
    }

    public FlujoDeCaja consultarFlujoCaja(LocalDate fecha) {
        return flujoDeCajaRepository.findByFecha(fecha)
                .orElseThrow(() -> new RuntimeException("No hay flujo de caja para esa fecha"));
    }

    public List<BitacoraAccion> consultarBitacora(LocalDate desde, LocalDate hasta) {
        return bitacoraRepository.findByFechaBetweenOrderByFechaDescHoraDesc(desde, hasta);
    }
}