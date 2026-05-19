 # KinalApp 
 Este proyecto es una aplicación web desarrollada con Spring Boot que nos ayuda a llevar un control sobre ventas, clientes, productos y usuarios. 
 Proporciona un CRUD completo para cada entidad con una interfaz amigable usando Thymeleaf. 

## Tecnologias Utilizadas
* **Java 21**
* **Spring Boot 4.0.2**
* **Maven (Gestor De Dependencias)**
* **MySQL (Sistema Gestor De Bases De Datos)**
* **SpringSecurity (Autenticación y Autorización de datos)**
* **ByCript(Encriptación de Contraseñas)**

## Funcionalidades

* Gestion de Clientes (CRUD)
* Gestion de Productos (CRUD)
* Gestion de Ventas (CRUD)
* Gestion de DetalleVentas (CRUD)
* Gestion de Usuarios (CRUD)
* Interfaz web con diseño responsive
* API Rest
* Autenticación de usuarios con Spring Security

## Seguridad

La aplicación implementa Spring Security 6 para proteger los recursos y controlar el acceso según el rol del usuario.

## Roles
| Rol     | Acceso                                                                             |
|---------|------------------------------------------------------------------------------------|
| `ADMIN` | Acceso completo: gestión de usuarios, eliminar, configurar (aun sin aplicamiento)  |
| `USER`  | Acceso limitado: consulta y operaciones básicas            (aun sin aplicamiento)  |

### Rutas públicas (sin autenticación)
| URL         | Descripción              |
|-------------|--------------------------|
| `/login`    | Página de inicio de sesión |
| `/registro` | Página de registro de usuario |
| `/css/**`   | Archivos de estilos      |
| `/js/**`    | Archivos JavaScript      |
| `/img/**`   | Imágenes estáticas       |

### Flujo de autenticación
  * Usuario ingresa credenciales 
  * ↓
  * Spring Security intercepta POST /login
  * ↓
  * CustomUserDetailsService busca el usuario en la BD
  * ↓
  * BCryptPasswordEncoder compara la contraseña
  * ↓
  * ¿Correcto? → Redirige a /Inicio
  * ¿Incorrecto? → Redirige a /login?error=true

## Requisitos Previos
Antes del proyecto es importante tener:
* JDK 17 o superior instalado
* Maven instalado
* Una instancia activa de MySQL

## Como Se Instala Y Ejecuta
# Primero se necesitaria descargar del repositorio en Git Hub:

```bash
git clone https://github.com/tu-usuario/KinalApp1.git
cd KinalApp1
```
# luego se configura applications.properties
```bash
    Spring.datasource.url=jdbc:mysql://Localhost:3306/dbKinal_App_in5av?createDatabaseIfNotExist=true
    spring.datasource.username=root
    spring.datasource.password=tu_contraseña
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
```

# Acceder a la aplicación

Abre tu navegador y ve a: http://localhost:8081/login

# Estructura del proyecto

```bash
KinalApp1/
    ├── src/main/java/com/pablocos/KinalApp1/
    │   ├── controller/     # Controladores MVC y REST
    │   ├── entity/         # Entidades JPA
    │   ├── repository/     # Repositorios JPA
    │   ├── security/
    │   │   └── config/         # Configuración de Spring Security
    │   │     └── SecurityConfig.java
    │   └── service/        # Servicios (lógica de negocio)
    ├── src/main/resources/
    │   ├── static/css/     # Archivos CSS
    │   └── templates/      # Plantillas Thymeleaf
    │       ├── clientes/
    │       ├── productos/
    │       ├── venta/
    │       ├── DetalleVenta/
    │       └── users/
    └── pom.xml             # Dependencias del proyecto
```
## Endpoints Principales

| Módulo         | URL                           | Descripción                   |
|----------------|-------------------------------|-------------------------------|
| Clientes       | `/clientes/lista`             | Listar clientes               |
| Productos      | `/productos/lista`            | Listar productos              |
| Ventas         | `/venta/lista`                | Listar ventas                 |
| Detalle Venta  | `/DetalleVenta/lista`         | Listar detalles de venta      |
| Usuarios       | `/users/lista`                | Listar usuarios               |

## Autor

* Pablo José Cos Taracena

## Licencia 

Proyecto Academico

