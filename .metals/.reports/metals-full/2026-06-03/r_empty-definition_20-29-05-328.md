error id: file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/controller/AccesoController.java:com/victuscaf/modules/client/dto/RegistroIngresoDTO#
file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/controller/AccesoController.java
empty definition using pc, found symbol in pc: com/victuscaf/modules/client/dto/RegistroIngresoDTO#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 90
uri: file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/modules/client/controller/AccesoController.java
text:
```scala
package com.victuscaf.modules.client.controller;

import com.victuscaf.modules.client.dto.@@RegistroIngresoDTO;
import com.victuscaf.modules.client.models.Asistencia;
import com.victuscaf.modules.client.service.AccesoService;
import com.victuscaf.modules.client.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/acceso")
@RequiredArgsConstructor
public class AccesoController {

    private final AccesoService accesoService;

    @PostMapping("/registrar-ingreso")
    @PreAuthorize("hasRole('SECRETARIA') or hasRole('ADMINISTRADOR')")
    public ApiResponse<Asistencia> registrarIngreso(@Valid @RequestBody RegistroIngresoDTO dto) {
        return ApiResponse.success("Ingreso registrado correctamente",
                accesoService.registrarIngreso(dto.numeroDocumento()));
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: com/victuscaf/modules/client/dto/RegistroIngresoDTO#