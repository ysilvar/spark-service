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
public final class ReadConfig implements Comparable<ReadConfig> {

    private final HashMap<String, String> systemaVarible;
    private String[] config;
    private final Constants constants;

    public ReadConfig() throws IOException {
        constants = Constants.getInstance();
        systemaVarible = new HashMap<>();
        String fileConfig = null;
        switch (constants.getOS()) {
            case "Linux":
                fileConfig = (constants.getSPARK_CONF_DIR() == null)
                        ? constants.getSPARK_HOME() + "\\conf\\spark-env.sh"
                        : constants.getSPARK_CONF_DIR() + "\\spark-env.sh";
                break;
            case "Windows":
                fileConfig = (constants.getSPARK_CONF_DIR() == null)
                        ? constants.getSPARK_HOME() + "\\conf\\spark-env.cmd"
                        : constants.getSPARK_CONF_DIR() + "\\spark-env.cmd";
                break;
        }

        readFile(new File(fileConfig));

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
    public void readFile(File file) throws IOException {

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
            String label = null;
            switch (constants.getOS()) {
                case "Windows":
                    label = "^set.+";
                    break;
                case "Linux":
                    label = "^#.+";
                    break;
            }
            pat = Pattern.compile(label);
            mat = pat.matcher(tem);
            if (mat.matches()) {
                pat = Pattern.compile("[^A-Z0-9._]+");
                items = pat.split(tem);
                try {
                    systemaVarible.put(items[1], items[2]);
                } catch (ArrayIndexOutOfBoundsException e) {

                }

            }
            result += tem + "\n";
            line = read.readLine();
        }
        config = result.split("\n");
    }
// hay que hacer un nuevo metodo
    public ArrayList<TableConten> tableData() throws Exception {
        String[] temp = config;
        ArrayList<TableConten> tem = new ArrayList<TableConten>();
        Pattern pattern;
        Matcher matcher;
        
            for (String tem1 : temp) {

                if (tem1.startsWith(constants.getCOMMENTARY())) {
                    String[] replace = {constants.getCOMMENTARY(), "-", " ",constants.getMOD() };
                    for (String rep : replace) {
                        tem1 = tem1.replace(rep, "");

                    }

                    if (tem1.contains("=") && !tem1.contains(",") && !tem1.contains(")")) {
                        String[] value = tem1.split("=");
                        tem.add(new TableConten(false, value[0], value[1], null));

                    }

                } else if (tem1.startsWith(constants.getMOD())) {
                    tem1 = tem1.replace(constants.getMOD(), "");
                    String[] value = tem1.split("=");

                    tem.add(new TableConten(true, value[0], value[1], null));

                }

            }

        

        return tem;
    }
    
    
}
