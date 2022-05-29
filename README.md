# ALKEMY CHALLENGE BACKEND

Desarrollo de la solución para el CHALLENGE BACKEND - Java Spring Boot (API) de Alkemy.

Las consignas/especificaciones se encuentran en la carpeta /Doc

## ESPECIFICACIONES

SDK: Java 8 (OpenJDK 1.8.0_312)

Framework: Spring Boot 2.6.7

Pruebas: JUnit 5

Base de datos: PostgreSQL 12.9

Cliente de email: SendGrid

## DOCUMENTACIÓN

La documentación de los endpoints fue realizada con Postman. La misma se encuentra en el siguiente enlace: https://documenter.getpostman.com/view/20666300/Uz5CLdhj

## COMENTARIOS

#### CONFIGURACIÓN

Todas las configuraciones se encuentran en el archivo application.properties (src/main/resources/application.properties).

#### EMAIL

El cliente de email está desactivado por defecto. Para activarlo:

1) Establecer una API Key de SendGrid y la dirección configurada como sender. Para esto, basta con completar los dos campos correspondientes en el archivo application.properties.
2) Descomentar las líneas de código que ponen en marcha la funcionalidad. Este fragmento de código se encuentra en el archivo /src/main/java/com/ivan/alkemybackendchallenge/security/service/AppUserServiceImpl.java, dentro del método saveAppUser().

#### BASE DE DATOS

Para facilitar las pruebas y el análisis de la aplicación, el uso de la base de datos por parte de JPA está configurado en modo create-drop, es decir que las tablas se crean al iniciar la aplicación y se destruyen al finalizar la misma. Para habilitar el comportamiento normal de persistencia, cambiar el modo a "update" en el archivo application.properties.

#### DATOS DE PRUEBA

Algunos datos de prueba son creados en la clase principal de la aplicación (src/main/java/com/ivan/alkemybackendchallenge/AlkemyBackendChallengeApplication.java) utilizando CommandLineRunner. Estos datos de prueba incluyen un usuario admin, un usuario común y algunos personajes, géneros y películas/series. Estos objetos de ejemplo se usaron durante las pruebas y para la documentación con Postman.

