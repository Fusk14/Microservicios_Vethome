# 📚 Documentación Completa de Endpoints - Microservicios VetHome

Esta documentación describe todos los endpoints disponibles en los microservicios de VetHome con sus códigos de respuesta HTTP correctos.

---

## 🔐 Microservicio de Usuarios (Puerto 8081)

### Base URL
```
https://rvhcfwb0-8081.brs.devtunnels.ms/
```

### Endpoints de Autenticación (`/api/auth`)

#### 1. Login de Usuario
- **Método:** `POST`
- **Ruta:** `/api/auth/login`
- **Descripción:** Autentica un usuario con correo y contraseña
- **Request Body:**
  ```json
  {
    "correo": "usuario@ejemplo.com",
    "contrasena": "password123"
  }
  ```
- **Response:** `String` (mensaje de éxito)
- **Códigos de Respuesta:**
  - `200 OK`: Login exitoso
  - `401 Unauthorized`: Credenciales inválidas

#### 2. Registro de Usuario
- **Método:** `POST`
- **Ruta:** `/api/auth/register`
- **Descripción:** Registra un nuevo usuario en el sistema
- **Request Body:**
  ```json
  {
    "rut": "12345678-9",
    "nombre": "Juan",
    "apellido": "Pérez",
    "correo": "juan@ejemplo.com",
    "telefono": "+56912345678",
    "contrasena": "password123",
    "rolNombre": "CLIENTE"
  }
  ```
- **Response:** `Usuario` (con ID generado)
- **Códigos de Respuesta:**
  - `201 Created`: Usuario registrado exitosamente
  - `400 Bad Request`: Datos inválidos
  - `409 Conflict`: RUT o correo ya registrado

#### 3. Recuperar Contraseña
- **Método:** `POST`
- **Ruta:** `/api/auth/forgot-password`
- **Descripción:** Envía instrucciones para recuperar la contraseña
- **Request Body:**
  ```json
  {
    "correo": "usuario@ejemplo.com"
  }
  ```
- **Response:** `String` (mensaje de confirmación)
- **Códigos de Respuesta:**
  - `200 OK`: Instrucciones enviadas exitosamente
  - `400 Bad Request`: Correo requerido
  - `404 Not Found`: Usuario no encontrado

### Endpoints de Usuarios (`/api/usuarios`)

#### 4. Crear Usuario
- **Método:** `POST`
- **Ruta:** `/api/usuarios`
- **Descripción:** Crea un nuevo usuario (contraseña será encriptada)
- **Request Body:** `Usuario`
- **Response:** `Usuario` (con ID generado)
- **Códigos de Respuesta:**
  - `201 Created`: Usuario creado exitosamente
  - `400 Bad Request`: Datos inválidos

#### 5. Listar Todos los Usuarios
- **Método:** `GET`
- **Ruta:** `/api/usuarios`
- **Descripción:** Obtiene la lista de todos los usuarios
- **Response:** `List<Usuario>`
- **Códigos de Respuesta:**
  - `200 OK`: Lista de usuarios

#### 6. Obtener Usuario por ID
- **Método:** `GET`
- **Ruta:** `/api/usuarios/{id}`
- **Parámetros:** `id` (Path): ID del usuario
- **Response:** `Usuario`
- **Códigos de Respuesta:**
  - `200 OK`: Usuario encontrado
  - `404 Not Found`: Usuario no encontrado

#### 7. Buscar Usuario por RUT
- **Método:** `GET`
- **Ruta:** `/api/usuarios/rut/{rut}`
- **Parámetros:** `rut` (Path): RUT del usuario
- **Response:** `Usuario`
- **Códigos de Respuesta:**
  - `200 OK`: Usuario encontrado
  - `404 Not Found`: Usuario no encontrado

#### 8. Buscar Usuario por Correo
- **Método:** `GET`
- **Ruta:** `/api/usuarios/correo/{correo}`
- **Parámetros:** `correo` (Path): Correo electrónico
- **Response:** `Usuario`
- **Códigos de Respuesta:**
  - `200 OK`: Usuario encontrado
  - `404 Not Found`: Usuario no encontrado

#### 9. Actualizar Información del Usuario
- **Método:** `PUT`
- **Ruta:** `/api/usuarios/{id}`
- **Parámetros:** `id` (Path): ID del usuario
- **Request Body:**
  ```json
  {
    "nombre": "Juan",
    "apellido": "Pérez",
    "telefono": "+56912345678"
  }
  ```
- **Response:** `Usuario` (actualizado)
- **Códigos de Respuesta:**
  - `200 OK`: Información actualizada exitosamente
  - `400 Bad Request`: Datos inválidos (nombre o teléfono requeridos)
  - `404 Not Found`: Usuario no encontrado
  - `500 Internal Server Error`: Error interno

#### 10. Cambiar Contraseña
- **Método:** `PUT`
- **Ruta:** `/api/usuarios/{id}/contrasena`
- **Parámetros:** `id` (Path): ID del usuario
- **Request Body:**
  ```json
  {
    "contrasenaActual": "password123",
    "nuevaContrasena": "newPassword456"
  }
  ```
- **Response:** `String` (mensaje de confirmación)
- **Códigos de Respuesta:**
  - `200 OK`: Contraseña actualizada exitosamente
  - `400 Bad Request`: Contraseña actual incorrecta o datos inválidos
  - `404 Not Found`: Usuario no encontrado
  - `500 Internal Server Error`: Error interno

---

## 🐾 Microservicio de Mascotas (Puerto 8090)

### Base URL
```
https://rvhcfwb0-8090.brs.devtunnels.ms/
```

### Endpoints (`/api/mascotas`)

#### 1. Crear Mascota
- **Método:** `POST`
- **Ruta:** `/api/mascotas`
- **Descripción:** Registra una nueva mascota en la base de datos
- **Request Body:** `Mascota`
  ```json
  {
    "idCliente": 1,
    "nombre": "Max",
    "especie": "Perro",
    "raza": "Labrador",
    "edad": 3
  }
  ```
- **Response:** `Mascota` (con ID generado)
- **Códigos de Respuesta:**
  - `201 Created`: Mascota creada exitosamente
  - `400 Bad Request`: Datos inválidos o error de negocio
  - `404 Not Found`: Cliente no encontrado o no tiene rol CLIENTE

#### 2. Listar Todas las Mascotas
- **Método:** `GET`
- **Ruta:** `/api/mascotas`
- **Descripción:** Obtiene una lista de todas las mascotas registradas
- **Response:** `List<Mascota>`
- **Códigos de Respuesta:**
  - `200 OK`: Lista de mascotas

#### 3. Obtener Mascota por ID
- **Método:** `GET`
- **Ruta:** `/api/mascotas/{id}`
- **Parámetros:** `id` (Path): ID de la mascota
- **Response:** `Mascota`
- **Códigos de Respuesta:**
  - `200 OK`: Mascota encontrada
  - `404 Not Found`: Mascota no encontrada

#### 4. Eliminar Mascota
- **Método:** `DELETE`
- **Ruta:** `/api/mascotas/{id}`
- **Parámetros:** `id` (Path): ID de la mascota
- **Response:** Sin contenido
- **Códigos de Respuesta:**
  - `204 No Content`: Mascota eliminada exitosamente
  - `404 Not Found`: Mascota no encontrada

#### 5. Subir Foto de Mascota
- **Método:** `POST`
- **Ruta:** `/api/mascotas/{id}/foto`
- **Parámetros:** 
  - `id` (Path): ID de la mascota
  - `foto` (Form Data): Archivo de imagen (MultipartFile)
- **Response:** `String` (mensaje de confirmación)
- **Códigos de Respuesta:**
  - `200 OK`: Foto subida exitosamente
  - `400 Bad Request`: Archivo inválido, vacío o no es una imagen
  - `404 Not Found`: Mascota no encontrada
  - `500 Internal Server Error`: Error al procesar la imagen

#### 6. Obtener Foto de Mascota
- **Método:** `GET`
- **Ruta:** `/api/mascotas/{id}/foto`
- **Parámetros:** `id` (Path): ID de la mascota
- **Response:** `byte[]` (imagen JPEG)
- **Headers de Respuesta:**
  - `Content-Type: image/jpeg`
  - `Cache-Control: public, max-age=3600`
  - `Access-Control-Allow-Origin: *`
- **Códigos de Respuesta:**
  - `200 OK`: Foto obtenida exitosamente
  - `404 Not Found`: Mascota no encontrada o sin foto
  - `500 Internal Server Error`: Error interno

---

## 🏥 Microservicio de Consultas (Puerto 8091)

### Base URL
```
https://rvhcfwb0-8091.brs.devtunnels.ms/
```

### Endpoints (`/api/consultas`)

#### 1. Crear Consulta
- **Método:** `POST`
- **Ruta:** `/api/consultas`
- **Descripción:** Registra una nueva consulta en la base de datos
- **Request Body:** `Consulta`
  ```json
  {
    "idMascota": 1,
    "idVeterinario": 2,
    "idCliente": 1,
    "fecha": "2024-01-15",
    "motivo": "Control anual"
  }
  ```
- **Response:** `Consulta` (con ID generado)
- **Códigos de Respuesta:**
  - `201 Created`: Consulta creada exitosamente
  - `400 Bad Request`: Datos inválidos o error de negocio
  - `404 Not Found`: Mascota, veterinario o cliente no encontrado

#### 2. Listar Todas las Consultas
- **Método:** `GET`
- **Ruta:** `/api/consultas`
- **Descripción:** Obtiene una lista de todas las consultas registradas
- **Response:** `List<Consulta>`
- **Códigos de Respuesta:**
  - `200 OK`: Lista de consultas

#### 3. Obtener Consulta por ID
- **Método:** `GET`
- **Ruta:** `/api/consultas/{id}`
- **Parámetros:** `id` (Path): ID de la consulta
- **Response:** `Consulta`
- **Códigos de Respuesta:**
  - `200 OK`: Consulta encontrada
  - `404 Not Found`: Consulta no encontrada

#### 4. Eliminar Consulta
- **Método:** `DELETE`
- **Ruta:** `/api/consultas/{id}`
- **Parámetros:** `id` (Path): ID de la consulta
- **Response:** Sin contenido
- **Códigos de Respuesta:**
  - `204 No Content`: Consulta eliminada exitosamente
  - `404 Not Found`: Consulta no encontrada

---

## ⭐ Microservicio de Reseñas (Puerto 8086)

### Base URL
```
https://rvhcfwb0-8086.brs.devtunnels.ms/
```

### Endpoints (`/api/resenas`)

#### 1. Crear Reseña
- **Método:** `POST`
- **Ruta:** `/api/resenas`
- **Descripción:** Registra una nueva reseña o calificación de un servicio veterinario
- **Request Body:** `Resena`
  ```json
  {
    "idCliente": 1,
    "idVeterinario": 2,
    "calificacion": 5,
    "comentario": "Excelente atención y profesionalismo"
  }
  ```
- **Response:** `Resena` (con ID generado)
- **Códigos de Respuesta:**
  - `201 Created`: Reseña creada exitosamente
  - `400 Bad Request`: Datos inválidos (calificación fuera de rango, comentario vacío, rol inválido)
  - `404 Not Found`: Cliente o veterinario no encontrado
  - `500 Internal Server Error`: Error interno

#### 2. Listar Todas las Reseñas
- **Método:** `GET`
- **Ruta:** `/api/resenas`
- **Descripción:** Obtiene una lista de todas las reseñas registradas
- **Response:** `List<Resena>`
- **Códigos de Respuesta:**
  - `200 OK`: Lista de reseñas

---

## 📝 Resumen de Códigos HTTP

### Códigos de Éxito
- **200 OK**: Operación exitosa (GET, PUT, DELETE exitosos)
- **201 Created**: Recurso creado exitosamente (POST exitoso)
- **204 No Content**: Recurso eliminado exitosamente (DELETE exitoso)

### Códigos de Error del Cliente
- **400 Bad Request**: Datos inválidos o error de validación
- **401 Unauthorized**: Credenciales inválidas
- **404 Not Found**: Recurso no encontrado
- **409 Conflict**: Conflicto (recurso ya existe)

### Códigos de Error del Servidor
- **500 Internal Server Error**: Error interno del servidor

---

## 🔄 Cambios Realizados

### Documentación
1. ✅ Todos los endpoints POST ahora retornan `201 Created` en lugar de `200 OK`
2. ✅ Documentación completa de códigos HTTP en todos los controladores
3. ✅ Códigos de error 500 agregados donde corresponde
4. ✅ Código 409 Conflict agregado para registros duplicados

### Tests
1. ✅ Test de `MascotaServiceTest` corregido (usando `@ExtendWith(MockitoExtension.class)`)
2. ✅ Test de `MascotaControllerTest` actualizado para esperar `201 Created`
3. ✅ Test de `ConsultaControllerTest` actualizado para esperar `201 Created`
4. ✅ Test de `ResenaControllerTest` actualizado para esperar `201 Created`
5. ✅ Test de `UsuarioControllerTest` actualizado para esperar `201 Created`

---

## 📌 Notas Importantes

1. **CORS**: Todos los endpoints tienen CORS habilitado para `*` (origen permitido)
2. **Validación**: Los endpoints POST y PUT validan los datos usando `@Valid` y `@RequestBody`
3. **Swagger**: Todos los endpoints están documentados con Swagger/OpenAPI
4. **Fotos**: Las fotos de mascotas se almacenan como BLOB en la base de datos
5. **Seguridad**: Las contraseñas se encriptan usando `PasswordEncoder` de Spring Security

