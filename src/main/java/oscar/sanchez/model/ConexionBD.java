package oscar.sanchez.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de la conexión a la base de datos MySQL en AWS RDS.
 * Vive dentro de "model" según el estándar MVC exigido.
 *
 * IMPORTANTE: reemplazar HOST, USUARIO y PASSWORD con los datos
 * reales de tu instancia RDS antes de compilar/ejecutar.
 */
public class ConexionBD {

    private static final String HOST        = "database-1.cqgwkwyy7rng.us-east-1.rds.amazonaws.com ";
    private static final String PUERTO      = "3306";
    private static final String BASE_DATOS  = "hackathon_matricula";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DATOS
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO  = "oscar";
    private static final String PASSWORD = "matricula-0712";

    private static Connection conexion;

    private ConexionBD() {
    }

    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("No se encontró el driver de MySQL (mysql-connector-j)", e);
            }
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
