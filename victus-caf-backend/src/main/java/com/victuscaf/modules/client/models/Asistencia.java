package com.victuscaf.modules.client.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asistencia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaIngreso;
    private LocalTime horaIngreso;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}