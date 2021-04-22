
package clasesIR;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryPaperJDBC {

   private static final String SQL_SELECT_WH = "SELECT codQuery, codPaper, "
                                               + "fecCrea, fecModi FROM queryPaper"
                                               + " WHERE codQuery = ? AND codPaper = ?" ;

   private static final String SQL_SELECT = "SELECT codQuery, codPaper, " +
                                                "fecCrea, fecModi FROM queryPaper";
    private static final String SQL_SELECT_ORD = "SELECT codQuery, codPaper, " +
                                                "fecCrea, fecModi FROM queryPaper ORDER BY ?";
    private static final String SQL_INSERT = "INSERT INTO queryPaper(codQuery, codPaper, " +
                                                "fecCrea, fecModi) " +
                                        "VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE queryPaper SET fecCrea=?, fecModi=? " +
                                        "WHERE codQuery = ? AND codPaper = ?";
    private static final String SQL_DELETE = "DELETE FROM queryPaper WHERE codQuery = ? AND codPaper = ?";
    

/**
 * Devuelve en una lista, el resultado del comando SELECT a la tabla Query
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/09
 * @return Lista de consultas de la BD
   */     
    
    
    public List<QueryPaper> select() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        QueryPaper queryPaper;
        List<QueryPaper> queryPapers;
        queryPapers = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareCall(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
            // recupera cada columna de la BD
                int codQuery = rs.getInt("codQuery");
                String codPaper = rs.getString("codPaper"); 
                String fecCrea = rs.getString("fecCrea"); 
                String fecModi = rs.getString("fecModi"); 
            // crea un objeto de tipo QueryPaper    
                queryPaper = new QueryPaper();
            // pasa el valor del registro de la BD al objeto    
                queryPaper.setCodQuery(codQuery);
                queryPaper.setCodPaper(codPaper);
                queryPaper.setFecCrea(fecCrea);
                queryPaper.setFecModi(fecModi);
            // Agrega un paper a la lista    
                queryPapers.add(queryPaper);
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return queryPapers;
        
    }
    
/**
 * Devuelve en una lista, el resultado del comando SELECT a la tabla Query
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/09
 * @param campo Campo por el cual se ordena la consulta
 * @return Lista de papers de la BD ordenada por campo
   */     
    
    public List<QueryPaper> select(String campo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        QueryPaper queryPaper;
        List<QueryPaper> queryPapers;
        queryPapers = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareCall(SQL_SELECT_WH);
            stmt.setString(1, campo);                        
            rs = stmt.executeQuery();
            while (rs.next()) {
            // recupera cada columna de la BD
                int codQuery = rs.getInt("codQuery");
                String codPaper = rs.getString("codPaper"); 
                String fecCrea = rs.getString("fecCrea"); 
                String fecModi = rs.getString("fecModi"); 
            // crea un objeto de tipo QueryPaper    
                queryPaper = new QueryPaper();
            // pasa el valor del registro de la BD al objeto    
                queryPaper.setCodQuery(codQuery);
                queryPaper.setCodPaper(codPaper);
                queryPaper.setFecCrea(fecCrea);
                queryPaper.setFecModi(fecModi);
            // Agrega un paper a la lista    
                queryPapers.add(queryPaper);
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return queryPapers;
        
    }
    
/**
 * Devuelve en una lista, el resultado del comando SELECT a la tabla Query, dados
 * los códigos de la consulta y del paper
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/09
 * @param codQry Código de la consulta
 * @param codPpr Código del paper
 * 
 * @return El registro para esa clave compuesta
   */     
    
    public List<QueryPaper> select(int codQry, String codPpr) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        QueryPaper queryPaper;
        List<QueryPaper> queryPapers;
        queryPapers = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareCall(SQL_SELECT_WH);
            stmt.setInt(1, codQry);                        
            stmt.setString(2, codPpr);                        
            rs = stmt.executeQuery();
            while (rs.next()) {
            // recupera cada columna de la BD
                int codQuery = rs.getInt("codQuery");
                String codPaper = rs.getString("codPaper"); 
                String fecCrea = rs.getString("fecCrea"); 
                String fecModi = rs.getString("fecModi"); 
            // crea un objeto de tipo QueryPaper    
                queryPaper = new QueryPaper();
            // pasa el valor del registro de la BD al objeto    
                queryPaper.setCodQuery(codQuery);
                queryPaper.setCodPaper(codPaper);
                queryPaper.setFecCrea(fecCrea);
                queryPaper.setFecModi(fecModi);
            // Agrega un paper a la lista    
                queryPapers.add(queryPaper);
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return queryPapers;
        
    }

/**
 * Inserta un registro de la tabla Query a la BD
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/09
 * @param queryPaper Registro a insertar en la tabla
 * @return Inserta el registro y devuelve número de registros adicionados a la tabla
 */ 
    
    public int insert(QueryPaper queryPaper) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            
            stmt.setInt(1, queryPaper.getCodQuery());
            stmt.setString(2, queryPaper.getCodPaper());
            stmt.setString(3, queryPaper.getFecCrea());
            stmt.setString(4, queryPaper.getFecModi());
            
            System.out.println("Ejecutando query " + SQL_INSERT);
            rows = stmt.executeUpdate();
            System.out.print("Registros insertados " + rows + " " + queryPaper.getCodQuery());
            System.out.println("______________________________________________");
            
        } catch (SQLException ex) {
            System.out.println("Error en paper --> " + queryPaper.getCodQuery());
            rows = -1;
            ex.printStackTrace(System.out);
        }
        finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        
        return rows;
    }
 
/**
 * Actualiza un registro de la tabla Query a la BD
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/09
 * @param queryPaper Registro a actualizar en la tabla
 * @return Actualiza el registro y devuelve número de registros actualizados en la tabla
 */     
    
    public static int update(QueryPaper queryPaper) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try {
            conn = Conexion.getConnection();
            System.out.println("Ejecutando query: " + SQL_UPDATE);
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, queryPaper.getFecCrea());
            stmt.setString(2, queryPaper.getFecModi());
            stmt.setInt(3, queryPaper.getCodQuery());
            stmt.setString(4, queryPaper.getCodPaper());
            
            rows = stmt.executeUpdate();
            
            System.out.println("Registros actualizados: " + rows);
            
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }
    
    /**
 * Borra un registro de la tabla QueryPaper a la BD
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/09
 * @param queryPaper Registro a borrar de la tabla
 * @return Borra el registro y devuelve número de registros borrados en la tabla
 */ 
    
    
    public static int delete(QueryPaper queryPaper) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try {
            conn = Conexion.getConnection();
            System.out.println("Ejecutando query " + SQL_DELETE);
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, queryPaper.getCodQuery());
            stmt.setString(2, queryPaper.getCodPaper());
            
            rows = stmt.executeUpdate();
            
            System.out.println("Registros eliminados: " + rows);
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
    return rows;        
    }


    
}
