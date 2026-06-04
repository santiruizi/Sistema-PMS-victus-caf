# 🏋️ Victus CAF

## ¿Qué es Victus CAF?
Victus CAF es un sistema de gestión para un Centro de Acondicionamiento Físico (CAF). Está diseñado para administrar clientes, contratos, remisiones médicas, pagos, evaluaciones físicas, control de acceso y los distintos roles que intervienen en el proceso operativo.

El proyecto combina:
- Backend en **Spring Boot 3.2.5** y **Java 21**
- Base de datos **MariaDB / MySQL**
- Frontend en **React 18 + TypeScript** con **Vite**
- Autenticación mediante **JWT** y control de acceso por roles

## Objetivo del sistema
Permitir a un CAF manejar:
- clientes particulares con membresía mensual o acceso diario
- beneficiarios de EPS con remisión médica
- contratos y pagos
- entrenadores y evaluaciones
- roles administrativos y de secretaria
- búsqueda y gestión de clientes desde el panel

## Características principales
- Login con JWT
- Roles: **ADMINISTRADOR**, **SECRETARIA**, **ENTRENADOR**
- CRUD de usuarios del sistema
- Registro de clientes mensuales, diarios y beneficiarios EPS
- Generación automática de contrato para clientes mensuales
- Buscadores por documento
- Rutas protegidas en frontend
- CORS habilitado para `http://localhost:5173`

---

## Estructura del proyecto

### Raíz del workspace
```text
victus-caf/
├── Readme.md
├── HELP.md
├── LICENSE
├── victus-caf-backend/
└── victus-caf-frontend/
```

### Backend
```text
victus-caf-backend/
├── pom.xml
└── src/main/java/com/victuscaf/
    ├── VictusCafApplication.java
    ├── modules/client/
    │   ├── controller/
    │   ├── service/
    │   ├── repository/
    │   ├── models/
    │   └── dto/
    ├── security/
    └── exception/
```

### Frontend
```text
victus-caf-frontend/
├── package.json
├── tsconfig.json
├── vite.config.ts
└── src/
    ├── App.tsx
    ├── main.tsx
    ├── components/
    ├── constants/
    ├── pages/
    ├── services/
    └── utils/
```

---

## Backend: estructura y cómo trabaja

### Paquetes principales
- `com.victuscaf.modules.client.controller`
  - Controladores REST para clientes y operaciones del CAF
- `com.victuscaf.modules.client.service`
  - Lógica de negocio y reglas de negocio
- `com.victuscaf.modules.client.repository`
  - Repositorios JPA para persistencia
- `com.victuscaf.modules.client.models`
  - Entidades JPA: clientes, contratos, remisiones, usuarios
- `com.victuscaf.modules.client.dto`
  - Objetos de transferencia para peticiones y respuestas
- `com.victuscaf.security`
  - Seguridad JWT y configuración de Spring Security

### Modelo de datos ocupado
- `Usuario` (abstracto) → `ParticularMensual`, `ParticularDiario`, `BeneficiarioEps`
- `UsuarioSistema` (abstracto) → `Administrador`, `Entrenador`, `Secretaria`
- `Contrato` ↔ `ParticularMensual`
- `Remision` ↔ `BeneficiarioEps`

### Flujo de trabajo del backend
1. El frontend hace login a `/api/auth/login`
2. `AuthController` autentica con `AuthenticationManager`
3. Se genera un JWT con `JwtUtils`
4. El token se usa en las llamadas al backend
5. `SecurityConfig` valida todas las rutas excepto `/api/auth/**`
6. Los controladores delegan a servicios y repositorios

### Endpoints clave
- `POST /api/auth/login` → Iniciar sesión
- `POST /api/clientes/mensual` → Registrar cliente mensual
- `GET /api/clientes/mensual/{numeroDocumento}` → Consultar cliente mensual
- `GET /api/clientes/mensuales/activos` → Listar clientes mensuales activos
- `GET /api/clientes/mensuales/todos` → Listar todos los clientes mensuales
- `POST /api/clientes/diario` → Registrar cliente diario
- `POST /api/clientes/eps` → Registrar beneficiario EPS

---

## Frontend: estructura y cómo se trabaja

### Carpetas principales
- `src/pages/` → páginas de la aplicación
- `src/components/` → componentes reutilizables
- `src/services/` → llamadas a la API
- `src/utils/` → utilidades generales

### Componentes importantes
- `Login.tsx` → formulario de login, guarda token y datos de usuario en `localStorage`
- `PrivateRoute.tsx` → proteje rutas según rol
- `GestionClientes.tsx` → lista clientes, búsqueda por documento, edición e inactivación
- `RegistrarClienteMensual.tsx` → formulario para registrar clientes mensuales

### Servicios de frontend
- `api.ts` → instancia Axios con interceptores JWT
- `clienteService.ts` → funciones para consumir endpoints de clientes
- `usuarioSistemaService.ts` → funciones para CRUD de usuarios del sistema

### Flujo de trabajo del frontend
1. El usuario ingresa documento y contraseña
2. `Login.tsx` envía datos a `/api/auth/login`
3. Se guarda token en `localStorage`
4. Las llamadas siguientes usan el token en `Authorization`
5. El usuario navega según su rol
6. Las páginas consumen datos con servicios y renderizan tablas

---

## Cómo ejecutar el proyecto localmente

### Backend
```bash
cd victus-caf-backend
../mvnw clean package
../mvnw spring-boot:run
```

Configura `src/main/resources/application.properties` con tu conexión MariaDB/MySQL.

### Frontend
```bash
cd victus-caf-frontend
npm install
npm run dev
```

Asegúrate de tener `VITE_API_URL` apuntando al backend si usas un `.env`.

---

## Notas importantes
- El backend usa JWT y solo permite rutas públicas en `/api/auth/**`
- El frontend asume que `localStorage` contiene `token` y `user`
- El registro de cliente mensual crea contrato automáticamente
- El endpoint de lista de mensuales activos devuelve solo clientes con contrato activo

## Buenas prácticas de desarrollo
- Añade validaciones en DTO cuando crees nuevos endpoints
- No expongas el token en peticiones públicas
- Usa `@JsonIgnore` o referencias de Jackson para evitar recursión infinita en entidades bidireccionales
- Mantén los roles y permisos actualizados en `SecurityConfig`

---

## Qué se puede mejorar
- Añadir documentación de API con Swagger
- Manejar mejor los errores con `ApiResponse` global
- Separar el frontend en módulos más pequeños
- Crear tests automatizados para servicios y controladores

---

## Licencia
Este proyecto se entrega con licencia de uso **estudiantil**. Consulta `LICENSE` para los términos.
