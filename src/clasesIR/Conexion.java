package clasesIR;

import java.sql.*;

public class Conexion {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/Dataset";
    private static final String JDB_USER = "condor";
    private static final String JDBC_PASS = "condor";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDB_USER, JDBC_PASS);
    }

public static void close(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Falló en el close de Conexion");
            ex.printStackTrace(System.out);
        }
       
    }

public static void close(PreparedStatement stmt) {
        try {
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }

public static void close(Connection conn) {
        try {
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
}
    
}
