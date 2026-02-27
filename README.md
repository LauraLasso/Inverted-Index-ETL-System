# Digital Book Management System: Inverted Index Pipeline

Este proyecto es un ecosistema completo para la gestión de libros digitales, que abarca desde la adquisición automatizada de datos hasta su exposición mediante una **API REST**. El sistema utiliza una arquitectura basada en eventos y un flujo de datos estructurado para transformar libros en bruto en un **Índice Invertido** altamente eficiente para búsquedas.

## Arquitectura del Proyecto

El sistema se organiza en cuatro módulos críticos:

### 1. Module 1: Clean-Books (NLP & Procesamiento)
Responsable de normalizar los textos brutos obtenidos de fuentes externas.
* **GutenbergCleaner:** Elimina *stopwords* (en inglés, español y francés), caracteres especiales y normaliza el texto a formato Unicode.
* **GutenbergSplitter:** Divide los archivos en metadatos estructurados (JSON) y contenido textual limpio.
* **WatchService:** Utiliza un sistema de escucha (*listeners*) en tiempo real para detectar nuevos libros en el Datalake y procesarlos automáticamente.

### 2. Module 2: Crawler (Adquisición de Datos)
Automatiza la descarga de libros desde `gutenberg.org`.
* **BatchDownloader:** Gestiona descargas secuenciales por lotes para evitar saturación y cumplir con las restricciones de la fuente.
* **Estructura del Datalake:** Organiza los archivos por fecha (`yyyyMMdd`) y por ID de libro, separando versiones `raw`, `content` y `metadata`.

### 3. Module 3: Datamart-Builder (Indexación)
Construye el Índice Invertido que relaciona cada palabra con los libros donde aparece y su frecuencia.
* **Persistencia Relacional:** Utiliza **SQLite** para el almacenamiento estructurado de palabras, libros y asociaciones.
* **Escalabilidad con MongoDB:** (Implementación opcional/vía Nginx) El sistema está diseñado para migrar o replicar el Datamart en **MongoDB**, permitiendo manejar volúmenes masivos de índices invertidos gracias a su naturaleza NoSQL orientada a documentos.

### 4. Module 4: Book-API (Exposición de Datos)
Servidor web desarrollado con **Spark Java** que permite consultar el sistema mediante una interfaz RESTful.

## Infraestructura y Despliegue

### Nginx: Balanceo y Seguridad
Para asegurar la alta disponibilidad de la API y el manejo eficiente de peticiones, se recomienda el uso de **Nginx** como:
* **Proxy Inverso:** Actúa como puerta de enlace única para los diferentes módulos.
* **Balanceador de Carga:** Distribuye las consultas de búsqueda entre múltiples instancias de la `book-api`.
* **Terminación SSL:** Gestiona la seguridad de las comunicaciones externas.

### MongoDB: Data Lake / Data Mart de Alto Rendimiento
Mientras que SQLite gestiona la consistencia relacional, **MongoDB** se integra para:
* Almacenar el **Datalake de Metadatos**, permitiendo búsquedas por atributos complejos que varían entre libros.
* Soportar un **Datamart NoSQL** para consultas de palabras con latencia ultra baja, ideal para el crecimiento exponencial del índice invertido.

---

## Endpoints de la API REST

* `GET /api/books`: Lista todos los libros indexados.
* `GET /api/search?term=palabra`: Devuelve los libros que contienen el término buscado utilizando el algoritmo de coincidencia de patrones.
* `GET /api/words`: Obtiene el catálogo completo de palabras indexadas.
* `POST /api/books`: Permite la inserción manual de nuevos libros al sistema.

## Tecnologías Clave

* **Java & Spark Framework:** Núcleo del backend y API.
* **SQLite:** Almacenamiento relacional inicial.
* **Nginx:** Gestión de tráfico y proxying.
* **MongoDB:** Almacenamiento NoSQL escalable.
* **Gson:** Serialización y manejo de metadatos JSON.
