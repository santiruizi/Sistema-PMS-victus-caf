# 🏋️️ Victus CAF - Sistema de Gestión para Centro de Acondicionamiento Físico

## 📖 Introducción

**Victus CAF** es un sistema de información diseñado para administrar de forma integral un Centro de Acondicionamiento Físico (CAF) que opera con dos tipos de clientes:

- Usuarios particulares (membresía mensual o acceso diario).
- Usuarios subsidiados remitidos por Entidades Promotoras de Salud (EPS).

El sistema busca centralizar y automatizar procesos críticos relacionados con la administración del centro deportivo, el seguimiento de usuarios, la gestión financiera y el control operativo.

### Funcionalidades principales

- Registro y clasificación de clientes según su tipo de vinculación.
- Control de contratos de membresía y remisiones médicas (sesiones autorizadas por EPS).
- Gestión de pagos, métodos de pago y flujo de caja diario.
- Control de acceso mediante verificación de estado de pago y vigencia de contrato.
- Asignación de entrenadores y seguimiento de rutinas de entrenamiento.
- Registro de evaluaciones físicas y evolución del cliente.
- Generación de reportes financieros y clínicos para la EPS.
- Auditoría de acciones mediante bitácora y notificaciones automáticas.

El proyecto se desarrolla como una aplicación web completa con **backend en Spring Boot 3.2.5 (Java 21)** y **frontend en React + TypeScript**, siguiendo el patrón **MVC (Modelo-Vista-Controlador)** y exponiendo servicios mediante una API REST protegida con JWT.

---

# 🧱 Arquitectura General

El sistema sigue una arquitectura de tres capas (MVC) tanto en backend como en frontend.

## Backend (Spring Boot)

### Modelo
Entidades JPA que representan la estructura relacional de la base de datos.

### Vista
No aplica en backend. La información es expuesta mediante API REST.

### Controlador
Controladores REST encargados de recibir peticiones HTTP y delegar la lógica de negocio a los servicios.

---

## Frontend (React + TypeScript)

### Modelo
Interfaces y modelos TypeScript utilizados para representar los datos consumidos desde la API.

### Vista
Componentes React organizados por módulos funcionales.

### Controlador
Hooks personalizados y servicios encargados de la interacción con la API y la gestión del estado.

---

## Base de Datos

- Motor: MySQL 8.
- ORM: JPA/Hibernate.
- Estrategia de herencia: `JOINED`.
- Relaciones mediante:
    - `@OneToOne`
    - `@OneToMany`
    - `@ManyToOne`

---

## Comunicación

- API REST.
- Formato JSON.
- Autenticación JWT.
- Control de acceso basado en roles.

### Roles del sistema

- ADMINISTRADOR
- SECRETARIA
- ENTRENADOR
- PARTICULAR_MENSUAL
- PARTICULAR_DIARIO
- BENEFICIARIO_EPS

---

# 🧩 Decisiones Técnicas Clave

| Área | Decisión | Justificación |
|--------|--------|--------|
| Backend | Spring Boot 3.2.5 | Framework robusto y ampliamente utilizado |
| Lenguaje Backend | Java 21 | Versión LTS requerida por Spring Boot 3 |
| Persistencia | JPA/Hibernate JOINED | Base de datos normalizada y extensible |
| Base de Datos | MySQL 8 | Motor relacional ampliamente soportado |
| Frontend | React + Vite | Desarrollo moderno y eficiente |
| Lenguaje Frontend | TypeScript | Tipado estático y mantenibilidad |
| Seguridad | JWT | Arquitectura stateless |
| Metodología | PSP | Seguimiento individual de tiempos y defectos |

---

# 📁 Estructura del Proyecto

```text
victus-caf/
│
├── backend/
│   ├── src/main/java/com/victuscaf/
│   │   ├── VictusCafApplication.java
│   │   │
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   ├── JwtFilter.java
│   │   │   └── CorsConfig.java
│   │   │
│   │   ├── modules/
│   │   │   ├── cliente/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── model/
│   │   │   │   └── dto/
│   │   │   │
│   │   │   ├── pago/
│   │   │   ├── acceso/
│   │   │   ├── evaluacion/
│   │   │   ├── entrenador/
│   │   │   ├── inventario/
│   │   │   └── notificacion/
│   │   │
│   │   ├── security/
│   │   │   ├── JwtUtil.java
│   │   │   └── UserDetailsServiceImpl.java
│   │   │
│   │   └── exception/
│   │       └── GlobalExceptionHandler.java
│   │
│   └── src/main/resources/
│       ├── application.properties
│       └── db/migration/
│
├── frontend/
│   ├── src/
│   │   ├── models/
│   │   ├── views/
│   │   │   ├── common/
│   │   │   ├── layouts/
│   │   │   └── modules/
│   │   │
│   │   ├── controllers/
│   │   ├── services/
│   │   ├── context/
│   │   ├── hooks/
│   │   ├── routes/
│   │   ├── utils/
│   │   └── styles/
│   │
│   ├── public/
│   ├── .env
│   ├── package.json
│   └── vite.config.ts
│
├── docs/
│   ├── requisitos/
│   └── uml/
│
└── README.md
```

---

# 🗃️ Modelo de Datos

## Jerarquía de Clientes (JOINED)

### Usuario (Abstracta)

Atributos comunes:

- id
- numeroDeDocumento
- nombreCompleto
- correoElectronico
- contrasena
- estado
- tipoDeCliente

### ParticularMensual

Atributos:

- tieneEntrenador
- estadoMembresia

Relaciones:

- OneToOne → Contrato

### ParticularDiario

Atributos:

- fechaDeIngreso
- horaIngreso
- horaExpiracion

### BeneficiarioEps

Atributos:

- tieneEntrenadorPermanente
- estadoContrato

Relaciones:

- OneToOne → Remision

---

## Jerarquía de Usuarios del Sistema

### UsuarioSistema (Abstracta)

Atributos comunes:

- idUsuarioSistema
- numeroDeDocumento
- nombreCompleto
- correoElectronico
- rol
- estado

### Administrador

Sin atributos adicionales.

### Entrenador

Atributos:

- especialidad
- cantidadClientesActivos
- salario

### Secretaria

Atributos:

- turno

---

## Entidades Principales

| Entidad | Relación | Descripción |
|----------|----------|----------|
| Remision | 1-1 BeneficiarioEps | Información médica y sesiones autorizadas |
| Contrato | 1-1 ParticularMensual | Gestión de membresías |
| Asistencia | N-1 Usuario | Registro de ingresos |
| EvaluacionFisica | N-1 Usuario y Entrenador | Historial físico |
| MetaFisica | 1-1 Usuario | Objetivos del usuario |
| Rutina | N-1 Usuario y Entrenador | Planes de entrenamiento |
| HorarioEntrenador | N-1 Entrenador | Disponibilidad |
| Mensaje | N-1 Entrenador y Usuario | Comunicación interna |
| Pago | N-1 Usuario y Contrato | Gestión financiera |
| FacturaEps | 1-N Asistencia | Consolidado de facturación |
| FlujoDeCaja | 1-N Pago y GastoOperativo | Control financiero |
| GastoOperativo | N-1 FlujoDeCaja | Gastos administrativos |
| Equipo | 1-N RegistroMantenimiento | Inventario |
| Tarifa | - | Tarifas vigentes |
| BitacoraAccion | - | Auditoría |
| Notificacion | N-1 UsuarioSistema | Alertas internas |

---

## Enums Principales

- TipoDeCliente
- EstadoMembresia
- EstadoContratoEps
- EstadoRemision
- EstadoFacturaEps
- MetodoDePago
- TipoDePago
- EstadoPago
- Rol
- Turno
- TipoGasto
- TipoAccion
- EstadoEquipo

---

# 🔄 Flujo de Negocio Ejemplo

## Registro de Asistencia EPS

1. La secretaria ingresa el documento del beneficiario.
2. El sistema valida que exista.
3. Verifica que el estado sea activo.
4. Verifica que el contrato EPS esté activo.
5. Comprueba copagos pendientes.
6. Consulta la remisión asociada.
7. Calcula sesiones disponibles.
8. Registra la asistencia.
9. Actualiza sesiones utilizadas.
10. Si se completan las sesiones:
    - Finaliza la remisión.
    - Finaliza el contrato EPS.
11. Asocia la asistencia a la factura mensual EPS.
12. Devuelve respuesta de éxito o error.

Este flujo corresponde a los RF-63, RF-RE-05, RF-RE-08 y RF-PA-12.

---

# 🛠️ Tecnologías

| Componente | Tecnología | Versión |
|------------|------------|----------|
| Backend | Spring Boot | 3.2.5 |
| Java | OpenJDK | 21 |
| Base de Datos | MySQL | 8.0 |
| ORM | Hibernate (JPA) | 6.3+ |
| Seguridad | Spring Security + JWT | 6.2+ |
| Frontend | React + Vite | 18.2 + 5.0 |
| Lenguaje Frontend | TypeScript | 5.0 |
| Cliente HTTP | Axios | 1.6 |
| Estilos | Tailwind CSS | 3.4 |
| Build | Maven / npm | 3.9+ / 10+ |

---

# 📈 Estado Actual del Desarrollo

## ✅ Completado

- 164 Requerimientos Funcionales.
- 78 Historias de Usuario.
- Diagrama de clases UML completo.
- Diagrama de secuencia de asistencia EPS.
- Estructura de carpetas definida.
- Entidades JPA completas.
- Enums completos.
- Repositorios JPA implementados.

## 🚧 En Progreso

- Servicios de negocio.
- Controladores REST.
- Seguridad JWT.
- Integración frontend-backend.
- Pruebas de integración.

## 🔮 Escalabilidad Futura

- Soporte para múltiples sedes.
- Integración con pasarelas de pago.
- WhatsApp Business API.
- Aplicación móvil.
- Dashboard analítico.

---

# ▶️ Ejecución Local

## Requisitos Previos

- Java 21
- Maven 3.9+
- MySQL 8
- Node.js 18+
- npm 10+

---

## Backend

```bash
cd backend
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

Disponible en:

```text
http://localhost:8080
```

---

## Frontend

```bash
cd frontend
npm install
npm run dev
```

Disponible en:

```text
http://localhost:5173
```

---

## Base de Datos

Crear esquema:

```sql
CREATE DATABASE victus_caf CHARACTER SET utf8mb4;
```

Configurar:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/victus_caf?useSSL=false&serverTimezone=America/Bogota
spring.datasource.username=root
spring.datasource.password=tu_contraseña

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Hibernate generará automáticamente las tablas al iniciar la aplicación.

---

# 📚 Documentación

- Requerimientos Funcionales
- Historias de Usuario
- Diagrama de Clases UML
- Diagrama de Secuencia (Asistencia EPS)

---

# 👥 Equipo de Desarrollo

- Autor: Santiago Ruiz Gallego
- Proyecto académico bajo metodología PSP

---
