
package clasesIR;


public class QueryPaper {

        private int codQuery;        
        private String codPaper;        
        private String fecCrea;
        private String fecModi;

    public QueryPaper(int codQuery, String codPaper, String fecCrea, String fecModi) {
        this.codQuery = codQuery;
        this.codPaper = codPaper;
        this.fecCrea = fecCrea;
        this.fecModi = fecModi;
    }

    public QueryPaper(){
        
    }
    
    
    public int getCodQuery() {
        return codQuery;
    }

    public void setCodQuery(int codQuery) {
        this.codQuery = codQuery;
    }

    public String getCodPaper() {
        return codPaper;
    }

    public void setCodPaper(String codPaper) {
        this.codPaper = codPaper;
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
        return "QueryPaper{" + "codQuery=" + codQuery + ", codPaper=" + codPaper + ", fecCrea=" + fecCrea + ", fecModi=" + fecModi + '}';
    }


        
        
}
