error id: file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/models/BeneficiarioEps.java:com/fasterxml/jackson/annotation/JsonManagedReference#
file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/models/BeneficiarioEps.java
empty definition using pc, found symbol in pc: com/fasterxml/jackson/annotation/JsonManagedReference#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 86
uri: file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/models/BeneficiarioEps.java
text:
```scala
package com.victuscaf.modules.client.models;

import com.fasterxml.jackson.annotation.@@JsonManagedReference;
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
    private EstadoContratoEps estadoContrato;
    @JsonManagedReference
    @OneToOne(mappedBy = "beneficiario", cascade = CascadeType.ALL)
    private Remision remision;
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: com/fasterxml/jackson/annotation/JsonManagedReference#