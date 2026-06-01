package com.victuscaf.modules.client.models;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode
@ToString
public class RegistroMantenimiento {
    private Long idMantenimiento;
    private LocalDate fechaMantenimiento;
    private String descripcion;
    private double costo;
    private String tecnicoResponsable;
}
