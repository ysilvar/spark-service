/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author eduardo
 */
public class GetContext implements Comparable<GetContext> {

    private ReadConfig conf;
    private Process process;

    public GetContext() throws FileNotFoundException {
        conf = new ReadConfig();
    }

    private boolean conexionGET(String request) {

        try {

            URL url = new URL(request);

            if (request.substring(0, request.indexOf(":")).equalsIgnoreCase("https")) {

                HttpsURLConnection conn1 = (HttpsURLConnection) url.openConnection();
                conn1.connect();
            } else {

                URLConnection conn2 = url.openConnection();
                conn2.connect();

            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void startSlave() throws IOException, Exception {
        if (conexionGET("http://" + conf.getSystemVariable("SPARK_MASTER_IP") + ":"
                + conf.getSystemVariable("SPARK_MASTER_WEBUI_PORT"))) {
            System.out.println("Se conecto correctamente al master");
            String home = System.getenv("SPARK_HOME");
            process = Runtime.getRuntime().exec(home + "\\sbin\\start-slave.cmd");
//            Field declaredField = process.getClass().getDeclaredField("");
            Field[] declaredFields = process.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
            System.out.println(declaredField.get(process));
//                System.out.println(declaredField.getName());
            }
//            declaredField.setAccessible(true);
//            System.out.println(process.getClass().getDeclaredField("pid").toString());
        } else {
            throw new Exception("Error al conectar al master");
        }
    }

    public boolean testConextion() {

        return conexionGET("http://" + conf.getSystemVariable("SPARK_MASTER_IP") + ":"
                + conf.getSystemVariable("SPARK_MASTER_WEBUI_PORT"));

    }

    public boolean isSlaveRun() {
        System.out.println(conexionGET("http://127.0.0.1:8081"));
        return conexionGET("http://127.0.0.1:8081");
    }

    public static void main(String[] args) throws FileNotFoundException {
        GetContext g = new GetContext();
        System.out.println(g.isSlaveRun());
    }

    public Process getProccess() {
        return process;
    }

    public void setConf(ReadConfig conf) {
        this.conf = conf;
    }

    public ReadConfig getConf() {
        return conf;
    }

    @Override
    public int compareTo(GetContext nvo) {
        for (int i = 0; i < conf.getConfig().size(); i++) {
            if (!conf.getConfig().get(i).equals(nvo.conf.getConfig().get(i))) {
                return 0;
            }
        }
        return 1;
    }
}
