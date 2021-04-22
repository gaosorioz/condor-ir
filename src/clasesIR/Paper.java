package clasesIR;

/**
 * En esta clase de definen los distintos atributos de un paper
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/11
  */


public class Paper {
// Atributos
        private String codPaper;        
        private String tipoPaper;
        private String titPaper;
        private int yearPaper;
        private String autorPaper;
        private String lengPaper;
        private String abstPaper;
        private String keywPaper;
        private String pathPaper;
        private String fecCrea;
        private String fecModi;

    
// Métodos

    public Paper(String codPaper, String tipoPaper, String titPaper,
                int yearPaper, String autorPaper, String lengPaper,
                String abstPaper, String keywPaper, String pathPaper,
                String fecCrea, String fecModi) {
        this.codPaper = codPaper;
        this.tipoPaper = tipoPaper;
        this.titPaper = titPaper;
        this.yearPaper = yearPaper;
        this.autorPaper = autorPaper;
        this.lengPaper = lengPaper;
        this.abstPaper = abstPaper;
        this.keywPaper = keywPaper;
        this.pathPaper = pathPaper;
        this.fecCrea = fecCrea;
        this.fecModi = fecModi;
    }


    
    public Paper(){
        
    }

    public String getCodPaper() {
        return codPaper;
    }

    public void setCodPaper(String codPaper) {
        this.codPaper = codPaper;
    }

    public String getTitPaper() {
        return titPaper;
    }

    public void setTitPaper(String titPaper) {
        this.titPaper = titPaper;
    }

    public int getYearPaper() {
        return yearPaper;
    }

    public void setYearPaper(int yearPaper) {
        this.yearPaper = yearPaper;
    }

    public String getAbstPaper() {
        return abstPaper;
    }

    public void setAbstPaper(String abstPaper) {
        this.abstPaper = abstPaper;
    }

    public String getKeywPaper() {
        return keywPaper;
    }

    public void setKeywPaper(String keywPaper) {
        this.keywPaper = keywPaper;
    }

    public String getTipoPaper() {
        return tipoPaper;
    }

    public void setTipoPaper(String tipoPaper) {
        this.tipoPaper = tipoPaper;
    }

    public String getAutorPaper() {
        return autorPaper;
    }

    public void setAutorPaper(String autorPaper) {
        this.autorPaper = autorPaper;
    }

    public String getPathPaper() {
        return pathPaper;
    }

    public void setPathPaper(String pathPaper) {
        this.pathPaper = pathPaper;
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

    public String getLengPaper() {
        return lengPaper;
    }

    public void setLengPaper(String lengPaper) {
        this.lengPaper = lengPaper;
    }

    @Override
    public String toString() {
        return "Paper{\n" + "\ncodPaper=" + codPaper + "\ntipoPaper=" + tipoPaper + "\ntitPaper=" + titPaper
                + "\nyearPaper=" + yearPaper + "\nautorPaper=" + autorPaper + "\nlengPaper="
                + lengPaper + "\nabstPaper=" + abstPaper + "\nkeywPaper=" + keywPaper 
                + "\npathPaper=" + pathPaper + "\nfecCrea=" + fecCrea + "\nfecModi=" + fecModi + '}';
    }

 
    
}
