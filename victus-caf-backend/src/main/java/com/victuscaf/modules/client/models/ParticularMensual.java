package com.victuscaf.modules.client.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)

public class ParticularMensual extends Usuario {
    private Boolean tieneEntrenador;
    private EstadoMembresia estadoMembresia;
    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
    private Contrato contrato;
}
