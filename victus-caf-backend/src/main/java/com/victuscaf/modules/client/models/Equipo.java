package com.victuscaf.modules.client.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode
@ToString
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idEquipo;

    private String nombre;
    private String descripcion;
    private LocalDate fechaAdquision;
    private EstadoEquipo estadoEquipo;
    private String proximoMantenimiento;

}
