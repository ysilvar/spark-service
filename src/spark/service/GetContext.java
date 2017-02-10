/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author Yorlay Silva Rodriguez
 */
public class GetContext {

    private Process process;
    private String pid;

    public GetContext() throws FileNotFoundException {
        process = null;
    }
    /**
     * 
     * @param request Receive a string that corresponds to the web url of the master.
     * @return true if exist the conection,false in other case.
     */
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
    /**
     * run to slave in the existing configuration in the file "spark-env.cmd".
     * @throws IOException
     * @throws Exception 
     */
    public void startSlave() throws IOException, Exception {
        ReadConfig conf = new ReadConfig();
        if (conexionGET("http://" + conf.getSystemVariable("SPARK_MASTER_IP") + ":"
                + conf.getSystemVariable("SPARK_MASTER_WEBUI_PORT"))) {
            System.out.println("Se conecto correctamente al master");
            String home = System.getenv("SPARK_HOME");
           
               List temp = chechProcess();
                process = Runtime.getRuntime().exec(home + "\\sbin\\start-slave.cmd");
                Thread.sleep(500);
                setPid(temp,chechProcess());

        } else {
            throw new Exception("Error al conectar al master");
        }
    }
    /**
     * 
     * @return true if exist the conection to the master, false in other case. 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public boolean testConextion() throws FileNotFoundException, IOException {
        ReadConfig conf = new ReadConfig();
        return conexionGET("http://" + conf.getSystemVariable("SPARK_MASTER_IP") + ":"
                + conf.getSystemVariable("SPARK_MASTER_WEBUI_PORT"));

    }
    /**
     * 
     * @return true if exist the conection to the slave, false in other case. 
     */
    public boolean isSlaveRun() {

        return conexionGET("http://127.0.0.1:8081");
    }

    public Process getProccess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public String getPid() {
        return pid;
    }

    private List<String> chechProcess() throws IOException {
        List<String> list = new LinkedList<>();
        Process s = Runtime.getRuntime().exec("tasklist");
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String readLine = br.readLine();
        while (readLine != null) {
            String[] tem = readLine.split(" ");
            if (tem[0].equals("java.exe")) {
                for (String tem1 : tem) {
                    try {

                        int x = Integer.parseInt(tem1);
                        list.add(tem1);
                        
                        break;
                    } catch (NumberFormatException e) {

                    }
                }
            }
            readLine = br.readLine();
          
        }
        return list;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        GetContext x = new GetContext();
            x.chechProcess();

    }

    private void setPid(List<String> a,List<String> b) {
         String tem = ManagementFactory.getRuntimeMXBean().getName();
                tem = tem.substring(0,tem.indexOf("@"));
        for (String b1 : b) {
            for (String a1 : a) {
                if (!b1.equals(a1) && !b1.equals(tem)) {
                    pid = b1;
                    
                }
               
            }
        }
        System.out.println("pid "+pid);
    }
    
    public void stopSlave() throws IOException{
      Process tem = Runtime.getRuntime().exec("taskkill /pid " + getPid() + " /f");

    }
}
