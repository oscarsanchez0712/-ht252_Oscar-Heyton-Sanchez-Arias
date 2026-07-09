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
