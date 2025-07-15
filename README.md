# To-Do List Full-Stack Application

Este repositorio contiene el código para una aplicación de lista de tareas (To-Do list) completa, desarrollada como parte de la prueba técnica para LG CNS. La solución incluye un backend RESTful construido con Java y Spring Boot, y un frontend moderno e interactivo construido con React.

---

## Requisitos Previos

Antes de comenzar, debe de tener instalado el siguiente software en tu sistema:

* **JDK 17** o superior.
* **Apache Maven** 3.8 o superior.
* **Node.js** v18 o superior.
* **npm** (generalmente se instala con Node.js).

---

## Instalación y Ejecución

Sigue estos pasos para clonar, instalar las dependencias y ejecutar la aplicación completa en tu entorno local.Las instrucciones de como usar la aplicacion funcionalmente estan en la wiki. El frontEnd de la aplicacion sale por defecto en el puerto 5173, y el backend sale por defecto en el puerto 8080. 

```bash
git clone https://github.com/juanjice29/to-do-app.git
cd to-do-app

# Navega a la carpeta del backend
cd to-do-backend

# Maven descargará las dependencias y compilará el proyecto
mvn clean install

# Ejecuta la aplicación de Spring Boot
mvn spring-boot:run

# Navega a la carpeta del frontend desde la raíz del proyecto
cd to-do-frontend

# Instala todas las dependencias de Node.js
npm install

# Inicia el servidor de desarrollo de Vite
npm run dev

# Si desea ejecutar las pruebas del backend desde la carpeta /to-do-backend
mvn test
```
---
## Api Documentacion

---
## Características Principales

* **Gestión de Tareas (CRUD):** Crear, leer, actualizar y eliminar tareas.
* **Estados de Tarea:** Marcar tareas como completadas o pendientes.
* **Filtrado Dinámico:** Visualizar tareas filtrando por "Todas", "Pendientes" y "Completadas".
* **Reordenamiento Interactivo:** Arrastrar y soltar (Drag and Drop) para cambiar el orden de las tareas.
* **Vista de Detalles:** Consultar la descripción, fecha de creación y última actualización de cada tarea en un modal.
* **Edición en Sitio:** Modificar el título y la descripción de las tareas existentes.
* **Diseño Responsivo:** Interfaz adaptable a dispositivos móviles y de escritorio.

---

##  Stack Tecnológico

### Backend
* **Java 17**
* **Spring Boot 3**
* **Spring Data JPA**
* **Maven**
* **H2 Database** (para un despliegue y pruebas sencillas)

### Frontend
* **React (Vite)**
* **TypeScript**
* **Tailwind CSS**
* **Context API** (para el manejo de estado global)
* **Axios** (para las peticiones a la API)
* **@hello-pangea/dnd** (para la funcionalidad de arrastrar y soltar)

### Futuras implementaciones
* ** Dockerizar todo el proyecto para que sea facilmente replicable como una imagen.
* ** Implementar la funcionalidad de modo oscuro y claro con tailwind
* ** Crear un plan de pruebas mas elaborado(actualmente solo hay pruebas de backend) , monkey ripets, pruebas e2e,prueba ui/ux.

