 # KinalApp 
 Este proyecto es una aplicación web desarrollada con Spring Boot que nos ayuda a llevar un control sobre ventas, clientes, productos y usuarios. 
 Proporciona un CRUD completo para cada entidad con una interfaz amigable usando Thymeleaf. 

## Tecnologias Utilizadas
* **Java 21**
* **Spring Boot 4.0.2**
* **Maven (Gestor De Dependencias)**
* **MySQL (Sistema Gestor De Bases De Datos)**

## Funcionalidades

* Gestion de Clientes (CRUD)
* Gestion de Productos (CRUD)
* Gestion de Ventas (CRUD)
* Gestion de DetalleVentas (CRUD)
* Gestion de Usuarios (CRUD)
* Interfaz web con diseño responsive
* API Rest

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

Abre tu navegador y ve a: http://localhost:8081

