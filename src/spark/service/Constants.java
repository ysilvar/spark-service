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
    private final String SPARK_HOME;
    private final String SPARK_CONF_DIR;
    private final String MOD;
    
    private Constants() {
            SPARK_CONF_DIR = System.getenv("SPARK_CONF_DIR");
            SPARK_HOME = System.getenv("SPARK_HOME");
        if (System.getenv("OS").equals("Windows_NT")) {
            COMMENTARY = "REM ";
            SEPARATOR  = "\\";
            OS = "Windows";
            MOD = "set";
        }else{
          COMMENTARY = "#";
          SEPARATOR  = "/";
          OS = "Linux";
          MOD = "";
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
    public String getSPARK_HOME(){
        return SPARK_HOME;
    }

    public String getMOD() {
        return MOD;
    }
   
    public String getSPARK_CONF_DIR() {
        return SPARK_CONF_DIR;
    }
    
}
