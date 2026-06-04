error id: file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/repository/ParticularMensualRepository.java:com/victuscaf/modules/client/models/EstadoMembresia#
file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/repository/ParticularMensualRepository.java
empty definition using pc, found symbol in pc: com/victuscaf/modules/client/models/EstadoMembresia#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 155
uri: file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/repository/ParticularMensualRepository.java
text:
```scala
package com.victuscaf.modules.client.repository;

import com.victuscaf.modules.client.models.ParticularMensual;
import com.victuscaf.modules.client.models.@@EstadoMembresia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParticularMensualRepository extends JpaRepository<ParticularMensual, Long> {

    Optional<ParticularMensual> findByNumeroDeDocumento(Long numeroDocumento);

    List<ParticularMensual> findByEstadoMembresia(EstadoMembresia estado);

    List<ParticularMensual> findByTieneEntrenadorTrue();
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: com/victuscaf/modules/client/models/EstadoMembresia#