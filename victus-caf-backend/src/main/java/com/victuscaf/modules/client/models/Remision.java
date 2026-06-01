package com.victuscaf.modules.client.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Remision {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int sesionesAutorizadas;
    private int sesionesAsistidas;
    private String medicoRemitente;
    private String entidadEps;
    private String diagnostico;
    private String zonaCuerpoTratar;
    private String motivoCancelacion;

    @Enumerated(EnumType.STRING)
    private EstadoRemision estado;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiario_id")
    private BeneficiarioEps beneficiario;
}