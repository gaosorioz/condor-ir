/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package condorIR;

/**
 * 
 * @author gaoz
 */
public class CarHashTex {
    public int [] vCodHash;
    public String [] vCarEq;


    public CarHashTex(int[] codHash, String[] carEq) {
        this.vCodHash = codHash;
        this.vCarEq = carEq;
    }

    public int[] getCodHash() {
        return vCodHash;
    }

    public void setCodHash(int[] codHash) {
        this.vCodHash = codHash;
    }

    public String[] getCarEq() {
        return vCarEq;
    }

    public void setCarEq(String[] carEq) {
        this.vCarEq = carEq;
    }

    
    
}
