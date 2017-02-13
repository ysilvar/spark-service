/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

/**
 *
 * @author Yorlay Silva Rodriguez
 */
public class Constants {
    private String COMMENTARY;
    private String SEPARATOR;
    private Constants() {
        if (System.getenv("OS").equals("Windows_NT")) {
            COMMENTARY = "REM";
            SEPARATOR  = "\\";
        }else{
          COMMENTARY = "#";
          SEPARATOR  = "/";
        }
    }
    
    public static Constants getInstance() {
        return ConstantsHolder.INSTANCE;
    }
    
    private static class ConstantsHolder {

        private static final Constants INSTANCE = new Constants();
    }

    public String getCOMMENTARY() {
        return COMMENTARY;
    }

    public String getSEPARATOR() {
        return SEPARATOR;
    }
    
}
