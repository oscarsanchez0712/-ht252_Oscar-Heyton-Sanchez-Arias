package oscar.sanchez.model;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Genera un reporte en PDF con el listado de matrículas activas.
 */
public class PDFExporter {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void exportarMatriculas(List<Matricula> matriculas, String rutaArchivo) throws Exception {
        Document documento = new Document(PageSize.A4.rotate(), 20, 20, 30, 20);
        PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
        documento.open();

        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph titulo = new Paragraph("Reporte de Matrículas - Instituto Tecnológico Valle Grande", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        documento.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(9);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{0.5f, 1, 1.5f, 1.5f, 1.5f, 1, 0.8f, 1, 1.3f});

        String[] encabezados = {"ID", "DNI", "Nombres", "Apellidos", "Curso", "Turno", "Beca", "Monto S/.", "Fecha"};
        Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
        for (String encabezado : encabezados) {
            PdfPCell celda = new PdfPCell(new Phrase(encabezado, fuenteEncabezado));
            celda.setBackgroundColor(new BaseColor(41, 65, 148));
            celda.setPadding(5);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
        }

        Font fuenteCelda = FontFactory.getFont(FontFactory.HELVETICA, 9);
        for (Matricula m : matriculas) {
            agregarCelda(tabla, String.valueOf(m.getIdMatricula()), fuenteCelda);
            agregarCelda(tabla, m.getDni(), fuenteCelda);
            agregarCelda(tabla, m.getNombres(), fuenteCelda);
            agregarCelda(tabla, m.getApellidos(), fuenteCelda);
            agregarCelda(tabla, m.getNombreCurso(), fuenteCelda);
            agregarCelda(tabla, m.getTurno(), fuenteCelda);
            agregarCelda(tabla, m.isBeca() ? "Sí" : "No", fuenteCelda);
            agregarCelda(tabla, String.format("%.2f", m.getMontoPago()), fuenteCelda);
            String fecha = m.getFechaMatricula() != null ? m.getFechaMatricula().format(FORMATO_FECHA) : "-";
            agregarCelda(tabla, fecha, fuenteCelda);
        }

        documento.add(tabla);
        documento.add(new Paragraph(" "));
        Font fuentePie = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8);
        documento.add(new Paragraph("Total de matrículas activas: " + matriculas.size(), fuentePie));

        documento.close();
    }

    private static void agregarCelda(PdfPTable tabla, String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setPadding(4);
        tabla.addCell(celda);
    }
}
