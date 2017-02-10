/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author eduardo
 */
public class ReadConfig implements Comparable<ReadConfig> {

    private HashMap<String, String> systemaVarible;
    private String[] config;
    private String os;
    private final String OS_WIN = "Windows_NT";

    public ReadConfig() throws FileNotFoundException, IOException {
        systemaVarible = new HashMap<>();
        os = System.getenv("OS");
        String config = (System.getenv("SPARK_CONF_DIR") == null)
                ? System.getenv("SPARK_HOME") + "\\conf\\spark-env.cmd" : System.getenv("SPARK_CONF_DIR") + "\\spark-env.cmd";

        readFile(new File(config));
        
    }

    public String[] getConfig() {
        return config;
    }
    /**
     * 
     * @param str.Spark variable name
     * @return spark variable value if exist, null in other case.
     */
    public String getSystemVariable(String str) {

        return systemaVarible.get(str);
    }

    @Override
    public int compareTo(ReadConfig o) {
        for (int i = 0; i < o.config.length; i++) {
            if (!o.config[i].equals(config[i])) {
                return 0;
            }
        }
        return 1;
    }
    /**
     * 
     * @param file.Configuration file "spark-env.cmd"
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void readFile(File file) throws FileNotFoundException, IOException {
        
        FileReader fl = new FileReader(file);
        BufferedReader br = new BufferedReader(fl);
        

        String line = br.readLine();
        String result = "";
        while (line != null) {
            Pattern pat = Pattern.compile("[^a-zA-Z_:.=0-9/$}{)(%-]+");
            Matcher mat;
            String[] items = pat.split(line);
            String tem = "";
            for (String s : items) {
                pat = Pattern.compile("[a-zA-Z_:.=0-9/$}{)(%-]+");
                mat = pat.matcher(s);

                if (mat.matches()) 
                    tem += s + " ";
                
            }
            String label = os.equals(OS_WIN) ? "^set.+" : "^.+";
            pat = Pattern.compile(label);
            mat = pat.matcher(tem);
            if (mat.matches()) {
                pat = Pattern.compile("[^A-Z0-9._]+");
                items = pat.split(tem);
                systemaVarible.put(items[1], items[2]);
            }
            result +=  tem + "\n";
            line = br.readLine();
        }
        config = result.split("\n");
    }
    private Object [][] tableData(){
        String [] temp = config;
        ArrayList<TableConten> tem = new ArrayList<TableConten>();  
        if (os.equals(OS_WIN) ) {
             Pattern pat = Pattern.compile("^[]+");
               Matcher mat ;
            for (String temp1 : temp) {
                mat = pat.matcher(temp1);
                if (temp1.startsWith("REM - ")) {
               
                 
                }
            }
        }
    
    return new String [][]{};
    }
    public static void main(String[] args) throws IOException {
        String config = (System.getenv("SPARK_CONF_DIR") == null)
                ? System.getenv("SPARK_HOME") + "\\conf\\spark-env.cmd"
                : System.getenv("SPARK_CONF_DIR") + "\\spark-env.cmd";
        new ReadConfig().readFile(new File(config));
    }
}
