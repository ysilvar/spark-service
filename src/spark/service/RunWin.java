/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yorlay Silva Rodriguez
 */
public class RunWin extends Thread {

    private Windows windows;

    public RunWin(Windows windows) {
        this.windows = windows;
    }

    @Override
    public void run() {
        while (true) {

            try {
                ReadConfig nvo = new ReadConfig();
                if (windows.getReadConfig().compareTo(nvo) == 0) {

                    windows.setReadConfig(nvo);
                }

                if (windows.getContex().testConextion()) {
                    //p.icono = new ImageIcon(getClass().getResource("../ok.png"));
                    if (!windows.getContex().isSlaveRun() && !windows.isManualStop()) {
                        windows.getContex().startSlave();
                        windows.getPop().getItem(3).setLabel(windows.action());

                    }

                    windows.getTrayicon().setToolTip("Service Slave: Master Runing");

                } else {
                    windows.getTrayicon().setToolTip("Service Slave: Master stop");
                    if (windows.getContex().isSlaveRun()) {
                        System.out.println("Entro al master-stop");
                        windows.getContex().stopSlave();
                        windows.getPop().getItem(3).setLabel(windows.action());

                    }

                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Windows.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Windows.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
