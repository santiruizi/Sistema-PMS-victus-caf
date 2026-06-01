package com.victuscaf.modules.client.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BeneficiarioEps extends Usuario{
    private Boolean tieneEntrenadorPermanente;
    private EstadoContratoParticular estadoContrato;
    @OneToOne(mappedBy = "beneficiario", cascade = CascadeType.ALL)
    private Remision remision;
}
