package oscar.sanchez.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MatriculaDAO {

    public List<Matricula> listarMatriculasActivas() throws SQLException {
        List<Matricula> lista = new ArrayList<>();
        String sql = "SELECT m.*, c.nombre_curso FROM matricula m "
                + "INNER JOIN curso c ON m.id_curso = c.id_curso "
                + "WHERE m.estado = 1 ORDER BY m.id_matricula DESC";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void insertar(Matricula m) throws SQLException {
        String sql = "INSERT INTO matricula (dni, nombres, apellidos, id_curso, turno, beca, monto_pago) "
                + "VALUES (?,?,?,?,?,?,?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getDni());
            ps.setString(2, m.getNombres());
            ps.setString(3, m.getApellidos());
            ps.setInt(4, m.getIdCurso());
            ps.setString(5, m.getTurno());
            ps.setBoolean(6, m.isBeca());
            ps.setDouble(7, m.getMontoPago());
            ps.executeUpdate();
        }
    }

    public void modificar(Matricula m) throws SQLException {
        String sql = "UPDATE matricula SET dni=?, nombres=?, apellidos=?, id_curso=?, turno=?, beca=?, monto_pago=? "
                + "WHERE id_matricula=? AND estado=1";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getDni());
            ps.setString(2, m.getNombres());
            ps.setString(3, m.getApellidos());
            ps.setInt(4, m.getIdCurso());
            ps.setString(5, m.getTurno());
            ps.setBoolean(6, m.isBeca());
            ps.setDouble(7, m.getMontoPago());
            ps.setInt(8, m.getIdMatricula());
            ps.executeUpdate();
        }
    }

    /**
     * Eliminado lógico: nunca se hace DELETE físico.
     * Cambia estado a 0 y el trigger de BD libera el cupo automáticamente.
     */
    public void eliminarLogico(int idMatricula) throws SQLException {
        String sql = "UPDATE matricula SET estado = 0 WHERE id_matricula = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMatricula);
            ps.executeUpdate();
        }
    }

    private Matricula mapear(ResultSet rs) throws SQLException {
        Matricula m = new Matricula();
        m.setIdMatricula(rs.getInt("id_matricula"));
        m.setDni(rs.getString("dni"));
        m.setNombres(rs.getString("nombres"));
        m.setApellidos(rs.getString("apellidos"));
        m.setIdCurso(rs.getInt("id_curso"));
        m.setNombreCurso(rs.getString("nombre_curso"));
        m.setTurno(rs.getString("turno"));
        m.setBeca(rs.getBoolean("beca"));
        m.setMontoPago(rs.getDouble("monto_pago"));
        Timestamp ts = rs.getTimestamp("fecha_matricula");
        if (ts != null) {
            m.setFechaMatricula(ts.toLocalDateTime());
        }
        m.setEstado(rs.getInt("estado"));
        return m;
    }
}
