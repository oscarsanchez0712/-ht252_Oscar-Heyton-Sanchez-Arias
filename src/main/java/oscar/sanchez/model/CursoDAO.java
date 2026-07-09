package oscar.sanchez.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    public List<Curso> listarCursosActivos() throws SQLException {
        List<Curso> lista = new ArrayList<>();
        String sql = "SELECT * FROM curso WHERE estado = 1 ORDER BY nombre_curso";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void insertar(Curso c) throws SQLException {
        String sql = "INSERT INTO curso (nombre_curso, descripcion, cupo_maximo, cupo_disponible) VALUES (?,?,?,?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombreCurso());
            ps.setString(2, c.getDescripcion());
            ps.setInt(3, c.getCupoMaximo());
            ps.setInt(4, c.getCupoMaximo());
            ps.executeUpdate();
        }
    }

    private Curso mapear(ResultSet rs) throws SQLException {
        Curso c = new Curso();
        c.setIdCurso(rs.getInt("id_curso"));
        c.setNombreCurso(rs.getString("nombre_curso"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setCupoMaximo(rs.getInt("cupo_maximo"));
        c.setCupoDisponible(rs.getInt("cupo_disponible"));
        c.setEstado(rs.getInt("estado"));
        return c;
    }
}
