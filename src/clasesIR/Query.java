package clasesIR;

/**
 * Define los atributos de una consulta (query)
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/11
  */


public class Query {
// Atributos
        private int codQuery;        
        private String lengQuery;
        private String cadQuery;
        private String investigador;
        private String topicQuery;
        private String fecCrea;
        private String fecModi;
        private int numPalQuery;        
        private int numDocPer;        


    public int getNumPalQuery() {        
        return numPalQuery;
    }

    public void setNumPalQuery(int numPalQuery) {
        this.numPalQuery = numPalQuery;
    }

    public int getNumDocPer() {
        return numDocPer;
    }

// Métodos
    public void setNumDocPer(int numDocPer) {
        this.numDocPer = numDocPer;
    }

    public Query(int codQuery, String lengQuery, String cadQuery, String investigador, String topicQuery, String fecCrea, 
                String fecModi, int numPalQuery, int numDocPer) {
        this.codQuery = codQuery;
        this.lengQuery = lengQuery;
        this.cadQuery = cadQuery;
        this.investigador = investigador;
        this.topicQuery = topicQuery;
        this.fecCrea = fecCrea;
        this.fecModi = fecModi;
        this.numPalQuery = numPalQuery;        
        this.numDocPer = numDocPer;        
        
        
    }

    public Query() {

    }

    public int getCodQuery() {
        return codQuery;
    }

    public void setCodQuery(int codQuery) {
        this.codQuery = codQuery;
    }

    public String getLengQuery() {
        return lengQuery;
    }

    public void setLengQuery(String lengQuery) {
        this.lengQuery = lengQuery;
    }

    public String getCadQuery() {
        return cadQuery;
    }

    public void setCadQuery(String cadQuery) {
        this.cadQuery = cadQuery;
    }

    public String getInvestigador() {
        return investigador;
    }

    public void setInvestigador(String investigador) {
        this.investigador = investigador;
    }

    public String getTopicQuery() {
        return topicQuery;
    }

    public void setTopicQuery(String topicQuery) {
        this.topicQuery = topicQuery;
    }

    public String getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(String fecCrea) {
        this.fecCrea = fecCrea;
    }

    public String getFecModi() {
        return fecModi;
    }

    public void setFecModi(String fecModi) {
        this.fecModi = fecModi;
    }

    
    
    
    @Override
    public String toString() {
        return "Query{" + "codQuery=" + codQuery + ", lengQuery=" + lengQuery + ","
                + "cadQuery=" + cadQuery + ", investigador=" + investigador 
                + ", topicQuery=" + topicQuery + ", fecCrea=" + fecCrea + ", fecModi=" + fecModi
                + ", numPalQry=" + numPalQuery + ", numDocPer=" + numDocPer   + '}';
    }



        
        
        
    
}
