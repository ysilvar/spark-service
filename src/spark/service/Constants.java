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
    private final String COMMENTARY;
    private final String SEPARATOR;
    private final String OS; 
    private Constants() {
        if (System.getenv("OS").equals("Windows_NT")) {
            COMMENTARY = "REM";
            SEPARATOR  = "\\";
            OS = "Windows";
        }else{
          COMMENTARY = "#";
          SEPARATOR  = "/";
          OS = "Linux";
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

    public String getOS() {
        return OS;
    }
    
}
