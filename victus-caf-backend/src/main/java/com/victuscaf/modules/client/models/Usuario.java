package com.victuscaf.modules.client.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@ToString
@EqualsAndHashCode
public abstract class  Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipoDeDocumento;
    private Long numeroDeDocumento;
    private String nombreCompleto;
    private LocalDate fechaDeNacimiento;
    private Long telefono;
    private String contrasena;
    private Boolean estado;
    @Enumerated(EnumType.STRING)
    private TipoDeCliente tipoDeCliente;

}
