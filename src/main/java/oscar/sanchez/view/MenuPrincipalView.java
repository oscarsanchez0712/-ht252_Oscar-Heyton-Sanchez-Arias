package oscar.sanchez.view;

import oscar.sanchez.model.Usuario;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class MenuPrincipalView extends JFrame {

    public MenuPrincipalView(Usuario usuario) {
        setTitle("Sistema de Matrícula - Menú Principal");
        setSize(500, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblBienvenida = new JLabel("Bienvenido, " + usuario.getNombreCompleto(), SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(lblBienvenida, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 10, 10));

        JButton btnMatriculas = new JButton("Gestión de Matrículas");
        btnMatriculas.addActionListener(e -> new MatriculaView().setVisible(true));

        JButton btnSalir = new JButton("Cerrar Sesión");
        btnSalir.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
        });

        panelBotones.add(btnMatriculas);
        panelBotones.add(btnSalir);
        panel.add(panelBotones, BorderLayout.CENTER);

        add(panel);
    }
}
