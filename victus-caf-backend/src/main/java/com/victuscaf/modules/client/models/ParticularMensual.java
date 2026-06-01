package com.victuscaf.modules.client.models;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@ToString(callSuper = true)

public class ParticularMensual extends Usuario {
    private Boolean tieneEntrenador;
    private EstadoMembresia estadoMembresia;
}
