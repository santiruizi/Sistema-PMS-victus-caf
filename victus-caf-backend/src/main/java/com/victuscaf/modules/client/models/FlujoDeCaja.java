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
public class FlujoDeCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFlujoDeCaja;
    private LocalDate fecha;
    private double totalIngresoParticular;
    private double totalIngresoParticularDiario;
    private double totalIngresoCopagoEps;
    private double totalIngresoPlus;
}
