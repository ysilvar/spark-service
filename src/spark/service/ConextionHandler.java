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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author Yorlay Silva Rodriguez
 */
public class ConextionHandler extends Thread {

    private Process process;
    private String pid;
    private boolean masterRun;
    private boolean slaveRun;
    private ReadConfig conf;
    private boolean start;
    private boolean manualStop;

    public ConextionHandler() throws FileNotFoundException, IOException {
        conf = new ReadConfig();
        start = true;
        manualStop = false;
        start();
    }

    /**
     *
     * @param request Receive a string that corresponds to the web url of the
     * master.
     * @return true if exist the conection,false in other case.
     */
    private void runningMasterAndSlave() throws InterruptedException, IOException {
        ReadConfig nvo = new ReadConfig();
        if (conf.compareTo(nvo) == 0) {

            conf = nvo;
        }
        String request = "http://" + conf.getSystemVariable("SPARK_MASTER_IP") + ":"
                + conf.getSystemVariable("SPARK_MASTER_WEBUI_PORT");

        setMasterRun(isRunning(request));
        setSlaveRun(isRunning("http://127.0.0.1:8081"));

    }

    private boolean isRunning(String request) throws InterruptedException {

        try {
            URL url = new URL(request);
            if (request.substring(0, request.indexOf(":")).equalsIgnoreCase("https")) {
                HttpsURLConnection conn1 = (HttpsURLConnection) url.openConnection();
                conn1.connect();
            } else {
                URLConnection conn2 = url.openConnection();
                conn2.connect();
            }
            sleep(50);
        } catch (IOException | InterruptedException e) {
            sleep(50);
            return false;
        }
        return true;

    }

    /**
     * run to slave in the existing configuration in the file "spark-env.cmd".
     *
     * @throws IOException
     * @throws Exception
     */
    private void startSlave() throws IOException, Exception {

        if (masterRun) {
            System.out.println("Se conecto correctamente al master");
            String home = Constants.getInstance().getSPARK_HOME();

            List temp = chechProcess();
            startSlave(home, temp);
        } else {
            throw new Exception("Error al conectar al master");
        }
    }

    private void startSlave(String home, List temp) throws IOException, InterruptedException {
        if (pid == null) {
            process = Runtime.getRuntime().exec(home + "\\sbin\\start-slave.cmd");
            Thread.sleep(150);
            setPid(temp, chechProcess());
        }
    }

    /**
     *
     * @return true if exist the conection to the slave, false in other case.
     */
    public boolean isSlaveRun() {

        return slaveRun;
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

    private void setPid(List<String> a, List<String> b) {
        String tem = ManagementFactory.getRuntimeMXBean().getName();
        tem = tem.substring(0, tem.indexOf("@"));
        if (pid == null) {

            for (String b1 : b) {
                for (String a1 : a) {
                    if (!b1.equals(a1) && !b1.equals(tem)) {
                        pid = b1;

                    }

                }
            }
        }
        System.out.println("pid " + pid);
    }

    public void stopSlave() throws IOException {
        Process tem = Runtime.getRuntime().exec("taskkill /pid " + getPid() + " /f");
        pid = null;
    }

    @Override
    public void run() {
        while (start) {
            try {

                try {
                    runningMasterAndSlave();
                    if (!masterRun && slaveRun || manualStop) {
                        stopSlave();
                    }
                    if (masterRun && !slaveRun && manualStop == false) {
                        startSlave();
                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(ConextionHandler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ConextionHandler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(ConextionHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConextionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void stopThread() {
        start = false;
    }

    public void setMasterRun(boolean masterRun) {
        this.masterRun = masterRun;
    }

    public void setSlaveRun(boolean slaveRun) {
        this.slaveRun = slaveRun;
        System.out.println("El esclavo esta " + slaveRun);
    }

    public boolean isManualStop() {
        return manualStop;
    }

    public void setManualStop() {
        this.manualStop = !this.manualStop;
    }

    public boolean isMasterRun() {
        return masterRun;
    }

}
