package oscar.sanchez.controller;

import oscar.sanchez.model.Usuario;
import oscar.sanchez.model.UsuarioDAO;
import oscar.sanchez.view.LoginView;
import oscar.sanchez.view.MenuPrincipalView;

import javax.swing.SwingUtilities;
import java.sql.SQLException;

public class LoginController {

    private final LoginView vista;
    private final UsuarioDAO usuarioDAO;

    public LoginController(LoginView vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDAO();
    }

    public void iniciarSesion() {
        String usuario = vista.getUsuario();
        String password = vista.getPassword();

        if (usuario.isEmpty() || password.isEmpty()) {
            vista.mostrarMensaje("Ingrese usuario y contraseña");
            return;
        }

        try {
            Usuario u = usuarioDAO.validarLogin(usuario, password);
            if (u != null) {
                vista.dispose();
                SwingUtilities.invokeLater(() -> new MenuPrincipalView(u).setVisible(true));
            } else {
                vista.mostrarMensaje("Usuario o contraseña incorrectos");
            }
        } catch (SQLException e) {
            vista.mostrarMensaje("Error de conexión a la base de datos");
            e.printStackTrace();
        }
    }
}
