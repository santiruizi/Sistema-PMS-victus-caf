package com.victuscaf.modules.client.service;

import com.victuscaf.modules.client.models.*;
import com.victuscaf.modules.client.repository.*;
import com.victuscaf.modules.entrenador.dto.*;
import com.victuscaf.modules.client.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntrenadorService {

    private final EntrenadorRepository entrenadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final RutinaRepository rutinaRepository;
    private final HorarioEntrenadorRepository horarioRepository;
    private final MensajeRepository mensajeRepository;

    // CRUD Entrenador (solo admin)
    @Transactional
    public Entrenador crearEntrenador(EntrenadorDTO dto) {
        if (entrenadorRepository.findByNumeroDeDocumento(dto.getNumeroDeDocumento()).isPresent()) {
            throw new RuntimeException("Ya existe un entrenador con ese documento");
        }
        Entrenador e = new Entrenador();
        e.setNumeroDeDocumento(dto.getNumeroDeDocumento());
        e.setNombreCompleto(dto.getNombreCompleto());
        e.setCorreoElectronico(dto.getCorreoElectronico());
        e.setContrasena(dto.getContrasena());
        e.setEspecialidad(dto.getEspecialidad());
        e.setSalario(dto.getSalario());
        e.setCantidadClientesActivos(0);
        e.setEstado(true);
        e.setRol(Rol.ENTRENADOR);
        return entrenadorRepository.save(e);
    }

    // Horarios
    @Transactional
    public HorarioEntrenador crearHorario(Long idEntrenador, HorarioDTO dto) {
        Entrenador entrenador = entrenadorRepository.findById(idEntrenador)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));
        HorarioEntrenador horario = new HorarioEntrenador();
        horario.setEntrenador(entrenador);
        horario.setFechaDisponible(dto.getFechaDisponible());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());
        horario.setDisponible(true);
        return horarioRepository.save(horario);
    }

    @Transactional
    public void asignarHorarioAClientePlus(Long idHorario, Long numeroDocumento) {
        HorarioEntrenador horario = horarioRepository.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        if (!horario.isDisponible()) {
            throw new RuntimeException("Horario no disponible");
        }
        Usuario cliente = usuarioRepository.findByNumeroDeDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        horario.setIdClienteAsignado(cliente.getId());
        horario.setDisponible(false);
        horarioRepository.save(horario);
    }

    // Rutinas
    @Transactional
    public Rutina crearRutina(Long idEntrenador, Long numeroDocumento, RutinaDTO dto) {
        Entrenador entrenador = entrenadorRepository.findById(idEntrenador)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));
        Usuario cliente = usuarioRepository.findByNumeroDeDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Rutina rutina = new Rutina();
        rutina.setEntrenador(entrenador);
        rutina.setUsuario(cliente);
        rutina.setNombre(dto.getNombre());
        rutina.setDescripcion(dto.getDescripcion());
        rutina.setFechaCreacion(java.time.LocalDate.now());
        rutina.setFechaVencimiento(dto.getFechaVencimiento());
        rutina.setActiva(true);
        return rutinaRepository.save(rutina);
    }

    // Mensajes
    @Transactional
    public Mensaje enviarMensaje(Long idEntrenador, Long numeroDocumento, String contenido) {
        Entrenador entrenador = entrenadorRepository.findById(idEntrenador)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));
        Usuario cliente = usuarioRepository.findByNumeroDeDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Mensaje msg = new Mensaje();
        msg.setEntrenador(entrenador);
        msg.setCliente(cliente);
        msg.setContenido(contenido);
        msg.setFechaEnvio(java.time.LocalDate.now());
        msg.setHoraEnvio(java.time.LocalTime.now());
        msg.setLeido(false);
        return mensajeRepository.save(msg);
    }
}