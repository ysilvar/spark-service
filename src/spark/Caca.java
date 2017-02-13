/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark;

/**
 *
 * @author Yorlay Silva Rodriguez
 */
public class Caca {
    
    private String comment = "";
    
    private Caca() {
        comment = "REM";
    }
    
    public static Caca getInstance() {
        return CacaHolder.INSTANCE;
    }
    
    private static class CacaHolder {

        private static final Caca INSTANCE = new Caca();
        
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    
   
}
