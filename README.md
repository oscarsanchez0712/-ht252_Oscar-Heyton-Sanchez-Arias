# 🎓 Sistema de Matrícula de Cursos Extracurriculares

<p align="center">
  <b>Instituto de Educación Superior Tecnológico "Valle Grande"</b><br>
  Sistema desarrollado con Java Swing + MySQL (AWS RDS)
</p>

---

## 👨‍💻 Autor

**Oscar Heyton Sanchez Arias**  
Carrera Profesional: **Análisis de Sistemas Empresariales**

---

# 📌 Descripción del Proyecto

El presente proyecto consiste en un **Sistema de Matrícula de Cursos Extracurriculares**, desarrollado para gestionar el registro de estudiantes en diferentes cursos ofrecidos por el Instituto Tecnológico "Valle Grande".

La aplicación permite administrar matrículas mediante operaciones CRUD, control automático de cupos, validaciones de negocio, autenticación de usuarios y generación de reportes en formato PDF.

El sistema utiliza una arquitectura **MVC (Modelo - Vista - Controlador)** para mantener una estructura organizada, escalable y fácil de mantener.

---

# 🛠️ Tecnologías Utilizadas

| Tecnología | Uso |
|---|---|
| ☕ Java | Lenguaje principal de desarrollo |
| 🖥️ Java Swing | Diseño de interfaz gráfica |
| 🗄️ MySQL 8.4 | Motor de base de datos |
| ☁️ AWS RDS | Hospedaje de la base de datos |
| 🔌 JDBC | Conexión entre Java y MySQL |
| 📄 iText | Generación de reportes PDF |
| 📦 Maven | Gestión de dependencias |
| 🏗️ Arquitectura MVC | Organización del proyecto |

---

# 🏛️ Arquitectura del Proyecto (MVC)

```
src/main/java/oscar/sanchez/

├── Main.java
│   └── Punto de entrada del sistema

├── model/
│   ├── ConexionBD.java
│   │   └── Conexión JDBC con MySQL RDS
│   │
│   ├── Curso.java
│   │   └── Entidad de cursos
│   │
│   ├── Matricula.java
│   │   └── Entidad de matrículas
│   │
│   ├── Usuario.java
│   │   └── Entidad de usuarios
│   │
│   ├── CursoDAO.java
│   ├── MatriculaDAO.java
│   ├── UsuarioDAO.java
│   │
│   └── PDFExporter.java
│       └── Exportación de reportes PDF

├── view/
│   ├── LoginView.java
│   │   └── Inicio de sesión
│   │
│   ├── MenuPrincipalView.java
│   │   └── Menú principal
│   │
│   └── MatriculaView.java
│       └── Gestión de matrículas

└── controller/

    ├── LoginController.java
    │   └── Control de autenticación

    └── MatriculaController.java
        └── Validaciones y lógica del negocio
```

---

# 🗄️ Base de Datos

La base de datos está implementada en **MySQL 8.4 alojado en Amazon RDS**.

## Configuración

- Motor: MySQL
- Puerto: 3306
- Servicio Cloud: AWS RDS
- Conexión mediante JDBC

---

## 📚 Tablas principales

### 📘 Curso

Tabla maestra que almacena los cursos extracurriculares disponibles.

Características:

- Nombre del curso.
- Descripción.
- Cupo máximo.
- Cupo disponible.
- Estado activo/inactivo.

Cursos registrados:

- Liderazgo
- Oratoria
- Python Básico
- Cloud Computing


---

### 👤 Usuario

Gestiona el acceso al sistema.

Usuario inicial:

```
Usuario: admin
Password: admin123
```

---

### 📝 Matrícula

Tabla transaccional donde se registran las matrículas.

Incluye:

- DNI del estudiante.
- Nombres y apellidos.
- Curso seleccionado.
- Turno.
- Tipo de beca.
- Monto de pago.
- Estado de matrícula.

---

# ⚙️ Reglas de Negocio Implementadas

✅ Validación de DNI con 8 dígitos.

✅ No permite registrar matrículas en cursos inexistentes.

✅ Control automático de cupos disponibles.

✅ No permite matricular cuando el curso está lleno.

✅ Uso de eliminación lógica:

```
estado = 0
```

en lugar de eliminar registros físicamente.

✅ Liberación automática de cupos al anular una matrícula.

---

# 🔥 Triggers Implementados

## trg_before_insert_matricula

Valida:

- Existencia del curso.
- Estado activo.
- Disponibilidad de cupos.

---

## trg_after_insert_matricula

Actualiza automáticamente:

```
cupo_disponible = cupo_disponible - 1
```

al registrar una matrícula.

---

## trg_after_update_matricula

Cuando una matrícula pasa a estado inactivo:

```
estado 1 → estado 0
```

devuelve el cupo disponible.

---

# 🚀 Instalación y Ejecución

## 1. Clonar repositorio

```bash
git clone https://github.com/oscarsanchez0712/-ht252_Oscar-Heyton-Sanchez-Arias.git
```

---

## 2. Configurar Base de Datos

Ejecutar:

```
sql/script.sql
```

en MySQL Workbench o DBeaver.

---

## 3. Configurar conexión RDS

Editar:

```
src/main/java/oscar/sanchez/model/ConexionBD.java
```

Modificar:

```java
HOST = "TU_ENDPOINT_RDS";
USUARIO = "TU_USUARIO";
PASSWORD = "TU_PASSWORD";
```

con los datos reales de AWS RDS.

---

## 4. Compilar Proyecto

Con Maven:

```bash
mvn clean package
```

Ejecutar:

```bash
java -jar target/hackathon-matricula.jar
```

---

# 🖥️ Funcionalidades del Sistema

## 🔐 Inicio de Sesión

- Validación de usuario.
- Acceso mediante credenciales.


## 📋 Gestión de Matrículas

Permite:

✅ Crear matrícula.

✅ Listar matrículas.

✅ Modificar registros.

✅ Anular matrícula.

✅ Exportar reporte PDF.


## 📄 Reportes PDF

El sistema genera reportes mediante:

```
PDFExporter.java
```

utilizando la librería:

```
iText
```

---

# 📸 Evidencias

- Prototipo UI en Figma.
- Base de datos MySQL RDS.
- Reglas Security Group AWS.
- Ejecución del sistema Java Swing.

---

# 📌 Conclusión

Este proyecto permite automatizar el proceso de matrícula de cursos extracurriculares, reduciendo errores manuales y proporcionando un sistema organizado con control de cupos, seguridad de datos y generación de reportes.

---

<p align="center">
Desarrollado por  
<br>
<b>Oscar Heyton Sanchez Arias</b>
<br>
2026
</p>
