package com.victuscaf.modules.client.service;

import com.victuscaf.modules.inventario.dto.*;
import com.victuscaf.modules.client.repository.*;
import com.victuscaf.modules.client.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final EquipoRepository equipoRepository;
    private final RegistroMantenimientoRepository mantenimientoRepository;

    @Transactional
    public Equipo crearEquipo(EquipoDTO dto) {
        Equipo equipo = new Equipo();
        equipo.setNombre(dto.getNombre());
        equipo.setDescripcion(dto.getDescripcion());
        equipo.setFechaAdquisicion(dto.getFechaAdquisicion());
        equipo.setEstadoEquipo(dto.getEstadoEquipo());
        equipo.setProximoMantenimiento(dto.getProximoMantenimiento());
        return equipoRepository.save(equipo);
    }

    @Transactional
    public RegistroMantenimiento registrarMantenimiento(Long idEquipo, MantenimientoDTO dto) {
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
        RegistroMantenimiento rm = new RegistroMantenimiento();
        rm.setEquipo(equipo);
        rm.setFechaMantenimiento(dto.getFechaMantenimiento());
        rm.setDescripcion(dto.getDescripcionTrabajo());
        rm.setCosto(dto.getCosto());
        rm.setTecnicoResponsable(dto.getTecnicoResponsable());
        // Actualizar próximo mantenimiento si se indica
        if (dto.getProximoMantenimiento() != null) {
            equipo.setProximoMantenimiento(dto.getProximoMantenimiento());
            equipoRepository.save(equipo);
        }
        return mantenimientoRepository.save(rm);
    }

    public List<Equipo> equiposPorMantenimientoProximo() {
        return equipoRepository.findByProximoMantenimientoBefore(LocalDate.now().plusDays(5));
    }
}