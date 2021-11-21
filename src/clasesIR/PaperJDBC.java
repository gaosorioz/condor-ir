package clasesIR;

/**
* Conexión a la tabla paper de la BD
* @author: Germán A. Osorio-Zuluaga
   */


import condorIR.Condor;
import java.io.File;
import java.util.List;
//import clasesIR.Paper;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaperJDBC {
    
//    private Connection conexionTransaccional;
    
    private static final String SQL_SELECT = "SELECT codPaper, tipoPaper, titPaper, " +
                                                "yearPaper, autorPaper, lengPaper, " +
                                                "abstPaper, keywPaper, pathPaper," +
                                                "fecCrea, fecModi FROM paper";
    private static final String SQL_SELECT_BY_PATH = "SELECT codPaper, tipoPaper, titPaper, " +
                                                "yearPaper, autorPaper, lengPaper, " +
                                                "abstPaper, keywPaper, pathPaper," +
                                                "fecCrea, fecModi FROM paper ORDER BY pathPaper";
    private static final String SQL_SELECT_ORD = "SELECT codPaper, tipoPaper, titPaper, " +
                                                "yearPaper, autorPaper, lengPaper, " +
                                                "abstPaper, keywPaper, pathPaper," +
                                                "fecCrea, fecModi FROM paper ORDER BY ?";
    private static final String SQL_SELECT_LENG = "SELECT codPaper, tipoPaper, titPaper, " +
                                                "yearPaper, autorPaper, lengPaper, " +
                                                "abstPaper, keywPaper, pathPaper," +
                                                "fecCrea, fecModi FROM paper WHERE lengPaper = ?";
//    private static final String SQL_SELECT_WH = "SELECT codPaper, tipoPaper, titPaper, " +
//                                                "yearPaper, autorPaper, lengPaper, " +
//                                                "abstPaper, keywPaper, pathPaper," +
//                                                "fecCrea, fecModi FROM paper WHERE codPaper = ?";
    
    private static final String SQL_INSERT = "INSERT INTO paper(codPaper, tipoPaper, titPaper, " +
                                                "yearPaper, autorPaper, lengPaper, " +
                                                "abstPaper, keywPaper, pathPaper," +
                                                "fecCrea, fecModi) " +
                                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE paper SET tipoPaper=?, titPaper=?, " +
                                                "yearPaper=?, autorPaper=?, lengPaper=?, " +
                                                "abstPaper=?, keywPaper=?, pathPaper=?," +
                                                "fecCrea=?, fecModi=? " +
                                        "WHERE codPaper = ?";
    private static final String SQL_DELETE = "DELETE FROM paper WHERE codPaper = ?";
    

/**
 * Devuelve en una lista, el resultado del comando SELECT a la BD
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/07
 * @return Lista de papers de la BD
   */     
    
    
    public List<Paper> select() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Paper paper;
        List<Paper> papers;
        papers = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareCall(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
            // recupera cada columna de la BD
                String codPaper = rs.getString("codPaper"); 
                String tipoPaper = rs.getString("tipoPaper"); 
                String titPaper = rs.getString("titPaper"); 
                int    yearPaper = rs.getInt("yearPaper"); 
                String autorPaper = rs.getString("autorPaper"); 
                String lengPaper = rs.getString("lengPaper"); 
                String abstPaper = rs.getString("abstPaper"); 
                String keywPaper = rs.getString("keywPaper"); 
                String pathPaper = rs.getString("pathPaper"); 
                String fecCrea = rs.getString("fecCrea"); 
                String fecModi = rs.getString("fecModi"); 
            // crea un objeto de tipo paper    
                paper = new Paper();
            // pasa el valor del registro de la BD al objeto    
                paper.setCodPaper(codPaper);
                paper.setTipoPaper(tipoPaper);
                paper.setTitPaper(titPaper);
                paper.setYearPaper(yearPaper);
                paper.setAutorPaper(autorPaper);
                paper.setLengPaper(lengPaper);
                paper.setAbstPaper(abstPaper);
                paper.setKeywPaper(keywPaper);
                paper.setPathPaper(pathPaper);
                paper.setFecCrea(fecCrea);
                paper.setFecModi(fecModi);
            // Agrega un paper a la lista    
                papers.add(paper);
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return papers;
        
    }
/**
 * Devuelve en una lista, el resultado del comando SELECT a la BD
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/07
 * @return Lista de papers de la BD
   */     
    
    
    public List<Paper> selectOrd() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Paper paper;
        List<Paper> papers;
        papers = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareCall(SQL_SELECT_BY_PATH);
            rs = stmt.executeQuery();
            while (rs.next()) {
            // recupera cada columna de la BD
                String codPaper = rs.getString("codPaper"); 
                String tipoPaper = rs.getString("tipoPaper"); 
                String titPaper = rs.getString("titPaper"); 
                int    yearPaper = rs.getInt("yearPaper"); 
                String autorPaper = rs.getString("autorPaper"); 
                String lengPaper = rs.getString("lengPaper"); 
                String abstPaper = rs.getString("abstPaper"); 
                String keywPaper = rs.getString("keywPaper"); 
                String pathPaper = rs.getString("pathPaper"); 
                String fecCrea = rs.getString("fecCrea"); 
                String fecModi = rs.getString("fecModi"); 
            // crea un objeto de tipo paper    
                paper = new Paper();
            // pasa el valor del registro de la BD al objeto    
                paper.setCodPaper(codPaper);
                paper.setTipoPaper(tipoPaper);
                paper.setTitPaper(titPaper);
                paper.setYearPaper(yearPaper);
                paper.setAutorPaper(autorPaper);
                paper.setLengPaper(lengPaper);
                paper.setAbstPaper(abstPaper);
                paper.setKeywPaper(keywPaper);
                paper.setPathPaper(pathPaper);
                paper.setFecCrea(fecCrea);
                paper.setFecModi(fecModi);
            // Agrega un paper a la lista    
                papers.add(paper);
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return papers;
        
    }
/**
 * Devuelve en una lista, el resultado del comando SELECT a la BD
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/07
 * @param campo Campo por el cual se ordena la consulta
 * @return Lista de papers de la BD ordenada por campo
   */     
    
    
    public List<Paper> select(String campo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Paper paper;
        List<Paper> papers;
        papers = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareCall(SQL_SELECT_ORD);
            stmt.setString(1, campo);            

            rs = stmt.executeQuery();
            while (rs.next()) {
            // recupera cada columna de la BD
                String codPaper = rs.getString("codPaper"); 
                String tipoPaper = rs.getString("tipoPaper"); 
                String titPaper = rs.getString("titPaper"); 
                int    yearPaper = rs.getInt("yearPaper"); 
                String autorPaper = rs.getString("autorPaper"); 
                String lengPaper = rs.getString("lengPaper"); 
                String abstPaper = rs.getString("abstPaper"); 
                String keywPaper = rs.getString("keywPaper"); 
                String pathPaper = rs.getString("pathPaper"); 
                String fecCrea = rs.getString("fecCrea"); 
                String fecModi = rs.getString("fecModi"); 
            // crea un objeto de tipo paper    
                paper = new Paper();
            // pasa el valor del registro de la BD al objeto    
                paper.setCodPaper(codPaper);
                paper.setTipoPaper(tipoPaper);
                paper.setTitPaper(titPaper);
                paper.setYearPaper(yearPaper);
                paper.setAutorPaper(autorPaper);
                paper.setLengPaper(lengPaper);
                paper.setAbstPaper(abstPaper);
                paper.setKeywPaper(keywPaper);
                paper.setPathPaper(pathPaper);
                paper.setFecCrea(fecCrea);
                paper.setFecModi(fecModi);
            // Agrega un paper a la lista    
                papers.add(paper);
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return papers;
        
    }
/**
 * Devuelve en una lista, el resultado del comando SELECT a la BD
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/07
 * @param campo Campo por el cual se ordena la consulta
 * @return Lista de papers de la BD ordenada por campo
   */     
    
    
    public List<Paper> selectLeng(String campo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Paper paper;
        List<Paper> papers;
        papers = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareCall(SQL_SELECT_LENG);
            stmt.setString(1, campo);            

            rs = stmt.executeQuery();
            while (rs.next()) {
            // recupera cada columna de la BD
                String codPaper = rs.getString("codPaper"); 
                String tipoPaper = rs.getString("tipoPaper"); 
                String titPaper = rs.getString("titPaper"); 
                int    yearPaper = rs.getInt("yearPaper"); 
                String autorPaper = rs.getString("autorPaper"); 
                String lengPaper = rs.getString("lengPaper"); 
                String abstPaper = rs.getString("abstPaper"); 
                String keywPaper = rs.getString("keywPaper"); 
                String pathPaper = rs.getString("pathPaper"); 
                String fecCrea = rs.getString("fecCrea"); 
                String fecModi = rs.getString("fecModi"); 
            // crea un objeto de tipo paper    
                paper = new Paper();
            // pasa el valor del registro de la BD al objeto    
                paper.setCodPaper(codPaper);
                paper.setTipoPaper(tipoPaper);
                paper.setTitPaper(titPaper);
                paper.setYearPaper(yearPaper);
                paper.setAutorPaper(autorPaper);
                paper.setLengPaper(lengPaper);
                paper.setAbstPaper(abstPaper);
                paper.setKeywPaper(keywPaper);
                paper.setPathPaper(pathPaper);
                paper.setFecCrea(fecCrea);
                paper.setFecModi(fecModi);
            // Agrega un paper a la lista    
                papers.add(paper);
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return papers;
        
    }

/**
 * Inserta un registro de la tabla Paper a la BD
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/07
 * @param paper Registro a insertar en la tabla
 * @return Inserta el registro y devuelve número de registros adicionados a la tabla
 */ 
    
    
    
    public int insert(Paper paper) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            
            stmt.setString(1, paper.getCodPaper());
            stmt.setString(2, paper.getTipoPaper());
            stmt.setString(3, paper.getTitPaper());
            stmt.setInt(4, paper.getYearPaper());
            stmt.setString(5, paper.getAutorPaper());
            stmt.setString(6, paper.getLengPaper());
            stmt.setString(7, paper.getAbstPaper());
            stmt.setString(8, paper.getKeywPaper());
            stmt.setString(9, paper.getPathPaper());
            stmt.setString(10, paper.getFecCrea());
            stmt.setString(11, paper.getFecModi());
            
            System.out.println("Ejecutando query " + SQL_INSERT);
            rows = stmt.executeUpdate();
            System.out.print("Registros insertados " + rows + " " + paper.getCodPaper());
            System.out.println("______________________________________________");
            
        } catch (SQLException ex) {
            System.out.println("Error en paper --> " + paper.getCodPaper());
            System.out.println("Longitud del título: --> " + paper.getTitPaper().length());
            System.out.println("Título: --> " + paper.getTitPaper());
            System.out.println("Longitud del keywords: --> " + paper.getKeywPaper().length());
            System.out.println("Keywords: --> " + paper.getKeywPaper());
            System.out.println("Longitud del Abstract: --> " + paper.getAbstPaper().length());
            System.out.println("Abstract: --> " + paper.getAbstPaper());
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
 * Actualiza un registro de la tabla Paper a la BD
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/07
 * @param paper Registro a actualizar en la tabla
 * @return Actualiza el registro y devuelve número de registros actualizados en la tabla
 */     
    
    public static int update(Paper paper) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try {
            conn = Conexion.getConnection();
            System.out.println("Ejecutando query: " + SQL_UPDATE);
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, paper.getTipoPaper());
            stmt.setString(2, paper.getTitPaper());
            stmt.setInt(3, paper.getYearPaper());
            stmt.setString(4, paper.getAutorPaper());
            stmt.setString(5, paper.getLengPaper());
            stmt.setString(6, paper.getAbstPaper());
            stmt.setString(7, paper.getKeywPaper());
            stmt.setString(8, paper.getPathPaper());
            stmt.setString(9, paper.getFecCrea());
            stmt.setString(10, paper.getFecModi());

            stmt.setString(11, paper.getCodPaper());
            
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
 * Borra un registro de la tabla Paper a la BD
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/07
 * @param paper Registro a borrar de la tabla
 * @return Borra el registro y devuelve número de registros borrados en la tabla
 */ 
    
    
    public static int delete(Paper paper) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try {
            conn = Conexion.getConnection();
            System.out.println("Ejecutando query " + SQL_DELETE);
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setString(1, paper.getCodPaper());
            
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
