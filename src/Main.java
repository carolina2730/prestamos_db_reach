import static spark.Spark.*;
import com.google.gson.Gson;
import java.sql.*;
import java.util.*;

public class Main {

    static Gson gson = new Gson();

    public static void main(String[] args) {

        port(8081);

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type");
        });

        options("/*", (req, res) -> "OK");

        // GUARDAR PRÉSTAMo
        post("/prestamos", (req, res) -> {
            System.out.println("peticion recibida: " + req.body());

            res.type("application/json");

            try {

                Prestamo p = gson.fromJson(req.body(), Prestamo.class);

                Connection conn = Conexion.getConnection();

                String sql = "INSERT INTO prestamos(nombre,monto,plazo_meses,tasa_interes,estado, fecha) VALUES(?,?,?,?,?,?)";

                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, p.getNombre());
                ps.setDouble(2, p.getMonto());
                ps.setInt(3, p.getPlazoMeses());
                ps.setDouble(4, p.getTasaInteres());
                ps.setString(5, p.getEstado());
                ps.setDate(6, new 
                java.sql.Date(System.currentTimeMillis()));
                ps.executeUpdate();

                conn.close();

                res.status(201);

                return gson.toJson(Map.of("mensaje", "Préstamo guardado"));

            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return gson.toJson(Map.of("error", e.getMessage()));
            }

        });

        // LISTAR PRÉSTAMOS
        get("/prestamos", (req, res) -> {

            res.type("application/json");

            List<Prestamo> lista = new ArrayList<>();

            try {

                Connection conn = Conexion.getConnection();

                String sql = "SELECT * FROM prestamos";

                PreparedStatement ps = conn.prepareStatement(sql);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    Prestamo p = new Prestamo();

                    p.setId(rs.getInt("id"));
                    p.setNombre(rs.getString("nombre"));
                    p.setMonto(rs.getDouble("monto"));
                    p.setPlazoMeses(rs.getInt("plazo_meses"));
                    p.setTasaInteres(rs.getDouble("tasa_interes"));
                    p.setEstado(rs.getString("estado"));

                    lista.add(p);
                }

                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return gson.toJson(lista);

        });

    }
}