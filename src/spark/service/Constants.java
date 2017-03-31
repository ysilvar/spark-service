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

    private String COMMENTARY;
    private String SEPARATOR;
    private String OS;
    private final String SPARK_HOME;
    private final String SPARK_CONF_DIR;
    private String MOD;
    private String SPARK_ENV;

    private Constants() {
        SPARK_HOME = System.getenv("SPARK_HOME");
        SPARK_CONF_DIR = System.getenv("SPARK_CONF_DIR");
        if (System.getProperty("os.name").contains("Windows")) {
            SPARK_ENV = SPARK_CONF_DIR == null ? SPARK_HOME.concat("\\conf\\spark-env.cmd") : SPARK_CONF_DIR.concat("\\spark-env.cmd");
            COMMENTARY = "REM ";
            SEPARATOR = "\\";
            OS = "Windows";
            MOD = "set";
        } else if (System.getProperty("os.name").contains("Linux")) {
            SPARK_ENV = SPARK_CONF_DIR == null ? SPARK_HOME.concat("/conf/spark-env.sh") : SPARK_CONF_DIR.concat("/spark-env.sh");
            COMMENTARY = "#";
            SEPARATOR = "/";
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

    public String getSPARK_HOME() {
        return SPARK_HOME;
    }

    public String getMOD() {
        return MOD;
    }

    public String getSPARK_CONF_DIR() {
        return SPARK_ENV;
    }

}
