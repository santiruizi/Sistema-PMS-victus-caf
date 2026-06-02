package com.victuscaf.modules.client.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaEps {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaGeneracion;
    private LocalDate fechaRadicacion;
    private LocalDate fechaPago;
    private double valorTotal;
    private String entidadEps;
    private String periodoConsolidado;

    @Enumerated(EnumType.STRING)
    private EstadoFacturaEps estado;

    @OneToMany(mappedBy = "facturaEps", cascade = CascadeType.ALL)
    private List<Asistencia> asistencias = new ArrayList<>();
}