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
 * @author Yorlay Silva Rodriguez
 */
public class ReadConfig implements Comparable<ReadConfig> {

    private HashMap<String, String> systemaVarible;
    private String[] config;

    public ReadConfig() throws FileNotFoundException, IOException {
        systemaVarible = new HashMap<>();
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

        FileReader fil = new FileReader(file);
        BufferedReader read = new BufferedReader(fil);

        String line = read.readLine();
        String result = "";
        while (line != null) {
            Pattern pat = Pattern.compile("[^a-zA-Z_:.=0-9/$}{)(%-]+");
            Matcher mat;
            String[] items = pat.split(line);
            String tem = "";
            for (String s : items) {
                pat = Pattern.compile("[a-zA-Z_:.=0-9/$}{)(%-]+");
                mat = pat.matcher(s);

                if (mat.matches()) {
                    tem += s + " ";
                }

            }
            String label = Constants.getInstance().getOS().equals("Windows") ? "^set.+" : "^.+";
            pat = Pattern.compile(label);
            mat = pat.matcher(tem);
            if (mat.matches()) {
                pat = Pattern.compile("[^A-Z0-9._]+");
                items = pat.split(tem);
                systemaVarible.put(items[1], items[2]);
            }
            result += tem + "\n";
            line = read.readLine();
        }
        config = result.split("\n");
    }

    public ArrayList<TableConten> tableData() {
        String[] temp = config;
        ArrayList<TableConten> tem = new ArrayList<TableConten>();
        Pattern pattern;
        Matcher matcher;
        if (Constants.getInstance().getOS().equals("Windows")) {
            for (String tem1 : temp) {

                pattern = Pattern.compile("^REM.+");
                matcher = pattern.matcher(tem1);
                if (matcher.matches()) {
                    pattern = Pattern.compile("^REM[^A-Z=_0-9]+");
                     String[] items = pattern.split(tem1);
                     for (String item : items) {
                        
                    }
                }
            }

        }

        return tem;
    }

    public static void main(String[] args) throws IOException {
        ReadConfig x = new ReadConfig();

        x.tableData();
    }
}
