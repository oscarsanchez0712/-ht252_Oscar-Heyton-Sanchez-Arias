# Sistema de Matrícula de Cursos Extracurriculares
Instituto Tecnológico "Valle Grande" — Java Swing + MySQL (AWS RDS)

## Estructura MVC (según lo pedido)

```
src/main/java/oscar/sanchez/
├── Main.java                     # Punto de entrada, abre el login
├── model/                        # Conexión, DAOs y entidades
│   ├── ConexionBD.java           # Conexión JDBC a MySQL RDS
│   ├── Curso.java                # Entidad tabla maestra
│   ├── Matricula.java            # Entidad tabla transaccional
│   ├── Usuario.java              # Entidad para login
│   ├── CursoDAO.java
│   ├── MatriculaDAO.java         # CRUD + eliminado lógico
│   ├── UsuarioDAO.java
│   └── PDFExporter.java          # Exportación de reporte a PDF (iText)
├── view/
│   ├── LoginView.java            # Pantalla de inicio (login)
│   ├── MenuPrincipalView.java    # Menú principal tras iniciar sesión
│   └── MatriculaView.java        # CRUD de matrículas
└── controller/
    ├── LoginController.java
    └── MatriculaController.java  # Validaciones y lógica de negocio
```

## 1. Base de datos

1. Crea tu instancia MySQL en AWS RDS (puerto 3306) y configura el
   Security Group para permitir tu IP.
2. Ejecuta el script `sql/script.sql` completo en tu cliente MySQL
   (Workbench, DBeaver, o `mysql -h <endpoint> -u <user> -p < script.sql`).
   Este script:
   - Crea la tabla maestra `curso` con 4 cursos de ejemplo.
   - Crea la tabla `usuario` con un usuario `admin / admin123`.
   - Crea la tabla transaccional `matricula` con:
     - CHECK de DNI (8 dígitos numéricos)
     - FK obligatoria hacia `curso` (no admite curso inexistente)
     - CHECK beca ↔ monto_pago = 0.00
     - ENUM de turno
   - Crea triggers que controlan el cupo disponible (no permiten
     matricular si no hay cupo) y que liberan el cupo al anular
     (eliminado lógico) una matrícula.

## 2. Configurar la conexión

Edita `src/main/java/oscar/sanchez/model/ConexionBD.java` y reemplaza:

```java
private static final String HOST     = "TU_ENDPOINT_RDS.rds.amazonaws.com";
private static final String USUARIO  = "admin";
private static final String PASSWORD = "TU_PASSWORD";
```

con los datos reales de tu instancia RDS.

## 3. Compilar y ejecutar

Con Maven instalado:

```bash
mvn clean package
java -jar target/hackathon-matricula.jar
```

También puedes abrir la carpeta como proyecto Maven en NetBeans o
IntelliJ y ejecutar `Main.java` directamente.

## 4. Uso de la aplicación

1. **Inicio de sesión**: usuario `admin`, contraseña `admin123`.
2. **Menú principal** → "Gestión de Matrículas".
3. Formulario CRUD:
   - DNI validado (8 dígitos).
   - Curso: `JComboBox` cargado desde la tabla maestra `curso`.
   - Turno: `JRadioButton` (Mañana / Tarde / Noche).
   - Beca: `JCheckBox`; al marcarla, el monto se fija en 0.00.
   - Botones: Nuevo, Guardar, Modificar, Eliminar (anular = eliminado
     lógico, nunca DELETE físico) y **Exportar PDF**.
4. **Exportar PDF**: genera un reporte con todas las matrículas
   activas usando `PDFExporter` (iText).

## Notas para la sustentación

- El "Eliminar" nunca borra físicamente: solo cambia `estado = 0`.
  El trigger `trg_after_update_matricula` libera automáticamente el
  cupo del curso.
- Los triggers `trg_before_insert_matricula` y
  `trg_after_insert_matricula` controlan que no se pueda matricular
  en un curso sin cupo o inexistente.
- El PDF se genera bajo demanda desde la vista de matrículas y se
  guarda donde el usuario elija (`JFileChooser`).
