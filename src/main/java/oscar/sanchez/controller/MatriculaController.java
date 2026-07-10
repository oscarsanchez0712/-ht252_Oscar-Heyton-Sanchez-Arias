package oscar.sanchez.controller;

import oscar.sanchez.model.Curso;
import oscar.sanchez.model.CursoDAO;
import oscar.sanchez.model.Matricula;
import oscar.sanchez.model.MatriculaDAO;
import oscar.sanchez.model.PDFExporter;
import oscar.sanchez.view.MatriculaView;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class MatriculaController {

    private final MatriculaView vista;
    private final MatriculaDAO matriculaDAO;
    private final CursoDAO cursoDAO;
    private static final Pattern PATRON_DNI = Pattern.compile("^\\d{8}$");
    private static final Pattern PATRON_NOMBRE = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");

    public MatriculaController(MatriculaView vista) {
        this.vista = vista;
        this.matriculaDAO = new MatriculaDAO();
        this.cursoDAO = new CursoDAO();
    }

    public void cargarCursos() {
        try {
            List<Curso> cursos = cursoDAO.listarCursosActivos();
            vista.cargarCursosEnCombo(cursos);
        } catch (SQLException e) {
            vista.mostrarError("Error al cargar cursos: " + e.getMessage());
        }
    }

    public void cargarMatriculas() {
        try {
            List<Matricula> matriculas = matriculaDAO.listarMatriculasActivas();
            vista.cargarTabla(matriculas);
        } catch (SQLException e) {
            vista.mostrarError("Error al cargar matrículas: " + e.getMessage());
        }
    }

    private Matricula construirMatriculaDesdeFormulario() {
        String dni = vista.getDni();
        if (!PATRON_DNI.matcher(dni).matches()) {
            vista.mostrarError("El DNI debe tener exactamente 8 dígitos numéricos");
            return null;
        }

        String nombres = vista.getNombres();
        String apellidos = vista.getApellidos();

        if (nombres.isEmpty() || apellidos.isEmpty()) {
            vista.mostrarError("Nombres y apellidos son obligatorios");
            return null;
        }

        // Validar que solo contengan letras y espacios
        if (!PATRON_NOMBRE.matcher(nombres).matches()) {
            vista.mostrarError("El campo Nombres solo debe contener letras");
            return null;
        }
        if (!PATRON_NOMBRE.matcher(apellidos).matches()) {
            vista.mostrarError("El campo Apellidos solo debe contener letras");
            return null;
        }

        // Validar longitud máxima (ajusta el número según tu columna en BD)
        if (nombres.length() > 50 || apellidos.length() > 50) {
            vista.mostrarError("Nombres y apellidos no deben superar los 50 caracteres");
            return null;
        }

        Curso curso = vista.getCursoSeleccionado();
        if (curso == null) {
            vista.mostrarError("Debe seleccionar un curso");
            return null;
        }
        String turno = vista.getTurnoSeleccionado();
        if (turno == null) {
            vista.mostrarError("Debe seleccionar un turno");
            return null;
        }
        boolean beca = vista.isBecaSeleccionada();

        // Si no tiene beca, el monto ya no puede quedar vacío en silencio
        if (!beca && vista.getMonto().isEmpty()) {
            vista.mostrarError("Debe ingresar el monto de pago (o marcar Beca completa)");
            return null;
        }

        double monto;
        try {
            monto = vista.getMonto().isEmpty() ? 0.0 : Double.parseDouble(vista.getMonto());
        } catch (NumberFormatException e) {
            vista.mostrarError("El monto de pago debe ser numérico");
            return null;
        }

        // No permitir montos negativos
        if (monto < 0) {
            vista.mostrarError("El monto de pago no puede ser negativo");
            return null;
        }

        // No permitir un monto absurdamente alto (evita errores de tipeo)
        if (monto > 10000) {
            vista.mostrarError("El monto de pago parece incorrecto (máximo S/. 10,000)");
            return null;
        }

        if (beca) {
            monto = 0.00;
        }

        Matricula m = new Matricula();
        m.setDni(dni);
        m.setNombres(nombres);
        m.setApellidos(apellidos);
        m.setIdCurso(curso.getIdCurso());
        m.setTurno(turno);
        m.setBeca(beca);
        m.setMontoPago(monto);
        return m;
    }

    public void guardarMatricula() {
        Matricula m = construirMatriculaDesdeFormulario();
        if (m == null) {
            return;
        }
        try {
            matriculaDAO.insertar(m);
            vista.mostrarMensaje("Matrícula registrada correctamente");
            vista.limpiarFormulario();
            cargarMatriculas();
            cargarCursos();
        } catch (SQLException e) {
            // Aquí llegan los mensajes de los triggers (cupo lleno, curso inexistente, etc.)
            vista.mostrarError("No se pudo registrar la matrícula: " + e.getMessage());
        }
    }

    public void modificarMatricula() {
        int id = vista.getIdSeleccionado();
        if (id == -1) {
            vista.mostrarError("Seleccione una matrícula de la tabla");
            return;
        }
        Matricula m = construirMatriculaDesdeFormulario();
        if (m == null) {
            return;
        }
        m.setIdMatricula(id);
        try {
            matriculaDAO.modificar(m);
            vista.mostrarMensaje("Matrícula actualizada correctamente");
            vista.limpiarFormulario();
            cargarMatriculas();
            cargarCursos();
        } catch (SQLException e) {
            vista.mostrarError("No se pudo actualizar la matrícula: " + e.getMessage());
        }
    }

    public void eliminarMatricula() {
        int id = vista.getIdSeleccionado();
        if (id == -1) {
            vista.mostrarError("Seleccione una matrícula de la tabla");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(vista.getFrame(),
                "¿Desea anular esta matrícula? El registro se mantendrá en el historial (eliminado lógico).",
                "Confirmar anulación", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            matriculaDAO.eliminarLogico(id);
            vista.mostrarMensaje("Matrícula anulada correctamente");
            vista.limpiarFormulario();
            cargarMatriculas();
            cargarCursos();
        } catch (SQLException e) {
            vista.mostrarError("No se pudo anular la matrícula: " + e.getMessage());
        }
    }

    public void exportarPDF() {
        try {
            List<Matricula> matriculas = matriculaDAO.listarMatriculasActivas();
            if (matriculas.isEmpty()) {
                vista.mostrarError("No hay matrículas activas para exportar");
                return;
            }
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("reporte_matriculas.pdf"));
            int resultado = chooser.showSaveDialog(vista.getFrame());
            if (resultado == JFileChooser.APPROVE_OPTION) {
                String ruta = chooser.getSelectedFile().getAbsolutePath();
                if (!ruta.toLowerCase().endsWith(".pdf")) {
                    ruta += ".pdf";
                }
                PDFExporter.exportarMatriculas(matriculas, ruta);
                vista.mostrarMensaje("Reporte PDF generado en:\n" + ruta);
            }
        } catch (Exception e) {
            vista.mostrarError("Error al generar el PDF: " + e.getMessage());
        }
    }
}