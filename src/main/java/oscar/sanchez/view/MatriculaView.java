package oscar.sanchez.view;

import oscar.sanchez.controller.MatriculaController;
import oscar.sanchez.model.Curso;
import oscar.sanchez.model.Matricula;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

public class MatriculaView extends JFrame {

    private JTextField txtDni, txtNombres, txtApellidos, txtMonto;
    private JComboBox<Curso> cbxCurso;
    private JRadioButton rbManana, rbTarde, rbNoche;
    private ButtonGroup grupoTurno;
    private JCheckBox chkBeca;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevo, btnGuardar, btnModificar, btnEliminar, btnExportarPDF;
    private int idSeleccionado = -1;

    private final MatriculaController controller;

    public MatriculaView() {
        setTitle("Gestión de Matrículas - Cursos Extracurriculares");
        setSize(980, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        controller = new MatriculaController(this);

        initComponents();
        controller.cargarCursos();
        controller.cargarMatriculas();
    }

    private void initComponents() {
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de Matrícula"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("DNI:"), gbc);
        txtDni = new JTextField(10);
        gbc.gridx = 1;
        panelForm.add(txtDni, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Nombres:"), gbc);
        txtNombres = new JTextField(15);
        gbc.gridx = 3;
        panelForm.add(txtNombres, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Apellidos:"), gbc);
        txtApellidos = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtApellidos, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Curso:"), gbc);
        cbxCurso = new JComboBox<>();
        gbc.gridx = 3;
        panelForm.add(cbxCurso, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("Turno:"), gbc);
        JPanel panelTurno = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rbManana = new JRadioButton("Mañana");
        rbTarde = new JRadioButton("Tarde");
        rbNoche = new JRadioButton("Noche");
        grupoTurno = new ButtonGroup();
        grupoTurno.add(rbManana);
        grupoTurno.add(rbTarde);
        grupoTurno.add(rbNoche);
        panelTurno.add(rbManana);
        panelTurno.add(rbTarde);
        panelTurno.add(rbNoche);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panelForm.add(panelTurno, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelForm.add(new JLabel("Beca completa:"), gbc);
        chkBeca = new JCheckBox();
        gbc.gridx = 1;
        panelForm.add(chkBeca, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Monto Pago (S/.):"), gbc);
        txtMonto = new JTextField(10);
        gbc.gridx = 3;
        panelForm.add(txtMonto, gbc);

        chkBeca.addActionListener(e -> {
            if (chkBeca.isSelected()) {
                txtMonto.setText("0.00");
                txtMonto.setEnabled(false);
            } else {
                txtMonto.setEnabled(true);
                txtMonto.setText("");
            }
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnNuevo = new JButton("Nuevo");
        btnGuardar = new JButton("Guardar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar (Anular)");
        btnExportarPDF = new JButton("Exportar PDF");

        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnExportarPDF);

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> controller.guardarMatricula());
        btnModificar.addActionListener(e -> controller.modificarMatricula());
        btnEliminar.addActionListener(e -> controller.eliminarMatricula());
        btnExportarPDF.addActionListener(e -> controller.exportarPDF());

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "DNI", "Nombres", "Apellidos", "Curso", "Turno", "Beca", "Monto"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                cargarSeleccionEnFormulario();
            }
        });
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.add(panelForm, BorderLayout.NORTH);
        panelPrincipal.add(scroll, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        txtDni.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtNombres.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtApellidos.setText(modeloTabla.getValueAt(fila, 3).toString());

        String nombreCurso = modeloTabla.getValueAt(fila, 4).toString();
        for (int i = 0; i < cbxCurso.getItemCount(); i++) {
            if (cbxCurso.getItemAt(i).getNombreCurso().equals(nombreCurso)) {
                cbxCurso.setSelectedIndex(i);
                break;
            }
        }

        String turno = modeloTabla.getValueAt(fila, 5).toString();
        switch (turno) {
            case "MAÑANA":
                rbManana.setSelected(true);
                break;
            case "TARDE":
                rbTarde.setSelected(true);
                break;
            case "NOCHE":
                rbNoche.setSelected(true);
                break;
            default:
                grupoTurno.clearSelection();
        }

        boolean beca = "Sí".equals(modeloTabla.getValueAt(fila, 6).toString());
        chkBeca.setSelected(beca);
        txtMonto.setEnabled(!beca);
        txtMonto.setText(modeloTabla.getValueAt(fila, 7).toString());
    }

    public void limpiarFormulario() {
        idSeleccionado = -1;
        txtDni.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        grupoTurno.clearSelection();
        chkBeca.setSelected(false);
        txtMonto.setEnabled(true);
        txtMonto.setText("");
        tabla.clearSelection();
    }

    public void cargarCursosEnCombo(List<Curso> cursos) {
        cbxCurso.removeAllItems();
        for (Curso c : cursos) {
            cbxCurso.addItem(c);
        }
    }

    public void cargarTabla(List<Matricula> matriculas) {
        modeloTabla.setRowCount(0);
        for (Matricula m : matriculas) {
            modeloTabla.addRow(new Object[]{
                    m.getIdMatricula(), m.getDni(), m.getNombres(), m.getApellidos(),
                    m.getNombreCurso(), m.getTurno(), m.isBeca() ? "Sí" : "No",
                    String.format("%.2f", m.getMontoPago())
            });
        }
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public String getDni() {
        return txtDni.getText().trim();
    }

    public String getNombres() {
        return txtNombres.getText().trim();
    }

    public String getApellidos() {
        return txtApellidos.getText().trim();
    }

    public Curso getCursoSeleccionado() {
        return (Curso) cbxCurso.getSelectedItem();
    }

    public String getTurnoSeleccionado() {
        if (rbManana.isSelected()) return "MAÑANA";
        if (rbTarde.isSelected()) return "TARDE";
        if (rbNoche.isSelected()) return "NOCHE";
        return null;
    }

    public boolean isBecaSeleccionada() {
        return chkBeca.isSelected();
    }

    public String getMonto() {
        return txtMonto.getText().trim();
    }

    public int getIdSeleccionado() {
        return idSeleccionado;
    }

    public JFrame getFrame() {
        return this;
    }
}
