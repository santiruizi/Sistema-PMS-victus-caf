package com.victuscaf.modules.client.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Contrato {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    private TipoDeMembresia tipoMembresia;

    @Enumerated(EnumType.STRING)
    private EstadoContratoParticular estado;

    @OneToOne
    @JoinColumn(name = "cliente_id")
    private ParticularMensual cliente;
}