package clasesIR;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class QueryJDBC {


    private static final String SQL_SELECT = "SELECT codQuery, lengQuery, cadQuery, " +
                                                "investigador, topicQuery, " +
                                                "fecCrea, fecModi, numPalQry, numDocPer FROM query";
    private static final String SQL_SELECT_ORD = "SELECT codQuery, lengQuery, cadQuery, " +
                                                "investigador, topicQuery, " +
                                                "fecCrea, fecModi, numpalqry, numdocper FROM query ORDER BY ?";
    private static final String SQL_SELECT_BY_CAD = "SELECT codQuery, lengQuery, cadQuery, " +
                                                "investigador, topicQuery, " +
                                                "fecCrea, fecModi, numpalqry, numdocper FROM query ORDER BY cadQuery";

       
    private static final String SQL_INSERT = "INSERT INTO query(codQuery, lengQuery, cadQuery, " +
                                                "investigador, topicQuery, " +
                                                "fecCrea, fecModi, numpalqry, numdocper) " +
                                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_UPDATE = "UPDATE query SET lenQuery=?, " +
                                                "cadQuery=?, investigador=?, topicQuery=?, " +
                                                "fecCrea=?, fecModi=?, numpalqry=?, numdocper=? " +
                                        "WHERE codQuery = ?";
    private static final String SQL_DELETE = "DELETE FROM query WHERE codQuery = ?";
    

/**
 * Devuelve en una lista, el resultado del comando SELECT a la tabla Query
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/09
 * @return Lista de consultas de la BD
   */     
    
    
    public List<Query> select() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Query query;
        List<Query> queries;
        queries = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareCall(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
            // recupera cada columna de la BD
                int codQuery = rs.getInt("codQuery");
                String lengQuery = rs.getString("lengQuery"); 
                String cadQuery = rs.getString("cadQuery"); 
                String investigador = rs.getString("investigador"); 
                String topicQuery = rs.getString("topicQuery"); 
                String fecCrea = rs.getString("fecCrea"); 
                String fecModi = rs.getString("fecModi"); 
                int numPalQry = rs.getInt("numpalqry");        
                int numDocPer = rs.getInt("numdocper");        

                // crea un objeto de tipo Query    
                query = new Query();
            // pasa el valor del registro de la BD al objeto    
                query.setCodQuery(codQuery);
                query.setLengQuery(lengQuery);
                query.setCadQuery(cadQuery);
                query.setInvestigador(investigador);
                query.setTopicQuery(topicQuery);
                query.setFecCrea(fecCrea);
                query.setFecModi(fecModi);
                query.setNumPalQuery(numPalQry);
                query.setNumDocPer(numDocPer);

            // Agrega un paper a la lista    
                queries.add(query);
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return queries;
        
    }
/**
 * Devuelve en una lista, el resultado del comando SELECT a la tabla Query
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/09
 * @return Lista de queries de la BD
   */     
    
    
    public List<Query> selectOrd() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Query query;
        List<Query> queries;
        queries = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareCall(SQL_SELECT_BY_CAD);
            rs = stmt.executeQuery();
            while (rs.next()) {
            // recupera cada columna de la BD
                int codQuery = rs.getInt("codQuery");
                String lengQuery = rs.getString("lengQuery"); 
                String cadQuery = rs.getString("cadQuery"); 
                String investigador = rs.getString("investigador"); 
                String topicQuery = rs.getString("topicQuery"); 
                String fecCrea = rs.getString("fecCrea"); 
                String fecModi = rs.getString("fecModi");
                int numPalQuery = rs.getInt("numPalQuery");        
                int numDocPer = rs.getInt("numDocPer");        
                
            // crea un objeto de tipo Query    
                query = new Query();
            // pasa el valor del registro de la BD al objeto    
                query.setCodQuery(codQuery);
                query.setLengQuery(lengQuery);
                query.setCadQuery(cadQuery);
                query.setInvestigador(investigador);
                query.setTopicQuery(topicQuery);
                query.setFecCrea(fecCrea);
                query.setFecModi(fecModi);
                query.setNumPalQuery(numPalQuery);
                query.setNumDocPer(numDocPer);

                
// Agrega un paper a la lista    
                queries.add(query);
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return queries;
        
    }
/**
 * Devuelve en una lista, el resultado del comando SELECT a la tabla Query
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/09
 * @param campo Campo por el cual se ordena la consulta
 * @return Lista de papers de la BD ordenada por campo
   */     
    
    
    public List<Query> select(String campo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Query query;
        List<Query> queries;
        queries = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareCall(SQL_SELECT_ORD);
            stmt.setString(1, campo);            

            rs = stmt.executeQuery();
            while (rs.next()) {
            // recupera cada columna de la BD
                int codQuery = rs.getInt("codQuery");
                String lengQuery = rs.getString("lengQuery"); 
                String cadQuery = rs.getString("cadQuery"); 
                String investigador = rs.getString("investigador"); 
                String topicQuery = rs.getString("topicQuery"); 
                String fecCrea = rs.getString("fecCrea"); 
                String fecModi = rs.getString("fecModi"); 
                int numPalQuery = rs.getInt("numPalQuery");        
                int numDocPer = rs.getInt("numDocPer");        
                
            // crea un objeto de tipo paper    
                query = new Query();
            // pasa el valor del registro de la BD al objeto    
                query.setCodQuery(codQuery);
                query.setLengQuery(lengQuery);
                query.setCadQuery(cadQuery);
                query.setInvestigador(investigador);
                query.setTopicQuery(topicQuery);
                query.setFecCrea(fecCrea);
                query.setFecModi(fecModi);
                query.setNumPalQuery(numPalQuery);
                query.setNumDocPer(numDocPer);

                
// Agrega un paper a la lista    
                queries.add(query);
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return queries;
        
    }

/**
 * Inserta un registro de la tabla Query a la BD
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/09
 * @param query Registro a insertar en la tabla
 * @return Inserta el registro y devuelve número de registros adicionados a la tabla
 */ 
    
    
    
    public int insert(Query query) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            
            stmt.setInt(1, query.getCodQuery());
            stmt.setString(2, query.getLengQuery());
            stmt.setString(3, query.getCadQuery());
            stmt.setString(4, query.getInvestigador());
            stmt.setString(5, query.getTopicQuery());
            stmt.setString(6, query.getFecCrea());
            stmt.setString(7, query.getFecModi());
            
            System.out.println("Ejecutando query " + SQL_INSERT);
            rows = stmt.executeUpdate();
            System.out.print("Registros insertados " + rows + " " + query.getCodQuery());
            System.out.println("______________________________________________");
            
        } catch (SQLException ex) {
            System.out.println("Error en paper --> " + query.getCodQuery());
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
 * @param query Registro a actualizar en la tabla
 * @return Actualiza el registro y devuelve número de registros actualizados en la tabla
 */     
    
    public static int update(Query query) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try {
            conn = Conexion.getConnection();
            System.out.println("Ejecutando query: " + SQL_UPDATE);
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, query.getLengQuery());
            stmt.setString(2, query.getCadQuery());
            stmt.setString(3, query.getInvestigador());
            stmt.setString(4, query.getTopicQuery());
            stmt.setString(5, query.getFecCrea());
            stmt.setString(6, query.getFecModi());
            stmt.setInt(7, query.getNumPalQuery());
            stmt.setInt(8, query.getNumDocPer());

            stmt.setInt(9, query.getCodQuery());
            
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
 * Borra un registro de la tabla Query a la BD
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/09
 * @param query Registro a borrar de la tabla
 * @return Borra el registro y devuelve número de registros borrados en la tabla
 */ 
    
    
    public static int delete(Query query) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try {
            conn = Conexion.getConnection();
            System.out.println("Ejecutando query " + SQL_DELETE);
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, query.getCodQuery());
            
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
