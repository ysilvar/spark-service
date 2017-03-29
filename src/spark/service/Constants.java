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
public final class Constants {
    private  String COMMENTARY;
    private  String SEPARATOR;
    private  String OS; 
    private final  String SPARK_HOME;
    private final  String SPARK_CONF_DIR;
    private  String MOD;
    
    private Constants() {
            SPARK_CONF_DIR = System.getenv("SPARK_CONF_DIR");
            SPARK_HOME = System.getenv("SPARK_HOME");
            
        if (System.getProperty("os.name").contains("Windows")) {
            COMMENTARY = "REM ";
            SEPARATOR  = "\\";
            OS = "Windows";
            MOD = "set";
        }else if(System.getProperty("os.name").contains("Linux")){
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
