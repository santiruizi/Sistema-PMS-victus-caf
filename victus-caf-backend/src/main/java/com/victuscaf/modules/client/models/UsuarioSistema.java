package com.victuscaf.modules.client.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.JOINED)
public abstract  class UsuarioSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuarioSistema;

    private String tipoDeDocumento;
    private Long numeroDeDocumento;
    private String nombreCompleto;
    private Long telefono;
    private String correoElectronico;
    private String contrasena;
    private Boolean estado;
    private Rol rol;
}
