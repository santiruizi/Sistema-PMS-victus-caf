error id: file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/repository/ContratoRepository.java:com/victuscaf/modules/client/models/ParticularMensual#
file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/repository/ContratoRepository.java
empty definition using pc, found symbol in pc: com/victuscaf/modules/client/models/ParticularMensual#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 146
uri: file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/repository/ContratoRepository.java
text:
```scala
package com.victuscaf.modules.client.repository;

import com.victuscaf.modules.client.models.Contrato;
import com.victuscaf.modules.client.models.@@ParticularMensual;
import com.victuscaf.modules.client.models.EstadoContratoParticular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    Optional<Contrato> findByCliente(ParticularMensual cliente);

    List<Contrato> findByEstado(EstadoContratoParticular estado);

    List<Contrato> findByFechaVencimientoBefore(LocalDate fecha);
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: com/victuscaf/modules/client/models/ParticularMensual#