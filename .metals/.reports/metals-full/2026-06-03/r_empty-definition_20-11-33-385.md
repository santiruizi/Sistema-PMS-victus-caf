error id: file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/models/Contrato.java:com/fasterxml/jackson/annotation/JsonBackReference#
file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/models/Contrato.java
empty definition using pc, found symbol in pc: com/fasterxml/jackson/annotation/JsonBackReference#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 86
uri: file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/models/Contrato.java
text:
```scala
package com.victuscaf.modules.client.models;

import com.fasterxml.jackson.annotation.@@JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
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

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "cliente_id")
    private ParticularMensual cliente;
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: com/fasterxml/jackson/annotation/JsonBackReference#