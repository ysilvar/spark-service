/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Yorlay Silva Rodriguez
 */
public class VisualHandler implements Runnable {

    private PopupMenu popupMenu;
    private ImageIcon icono;
    private TrayIcon trayicon;

    private ConectionHandler conectionHandler;
    private ReadConfig readConfig;
    private TableHandler tableHandler;
    private MultiServer server;
    private MenuItem manualStopStart;
    private boolean visual;

    public VisualHandler() throws IOException {
        this(true);

    }

    public VisualHandler(boolean visual) throws IOException {

        conectionHandler = new ConectionHandler();

        server = new MultiServer(4444, new Protocol(this));

        readConfig = new ReadConfig();

        this.visual = visual;
        if (visual) {
            tableHandler = new TableHandler(readConfig);
            try {
                tableHandler.addData(readConfig.tableData());
            } catch (Exception ex) {

            }

            if (SystemTray.isSupported()) {

                icono = new ImageIcon(getClass().getResource("icon.png"));
                popupMenu = new PopupMenu();
                trayicon = new TrayIcon(icono.getImage(), statusMaster(), popupMenu);

                if (visual) {
                    ActionListener configSlave = (ActionEvent e) -> {
                        tableHandler.setVisible(true);
                    };
                    MenuItem btnInterfaz = new MenuItem("Configure Slave");
                    btnInterfaz.addActionListener(configSlave);
                    popupMenu.add(btnInterfaz);
                    trayicon.addActionListener(configSlave);

                }

                MenuItem webItem = new MenuItem("Show master in browser");
                webItem.addActionListener((ActionEvent e) -> {
                    try {
                        Desktop.getDesktop().browse(URI.create("http://"
                                + readConfig.getSystemVariable("SPARK_MASTER_IP")
                                + ":" + readConfig.getSystemVariable("SPARK_MASTER_WEBUI_PORT")));
                    } catch (IOException e1) {

                    }
                });
                MenuItem webClient = new MenuItem("Show slave in browser");
                webClient.addActionListener((ActionEvent e) -> {
                    try {
                        Desktop.getDesktop().browse(URI.create("http://127.0.0.1:8081"));
                    } catch (IOException e1) {

                    }
                });

                manualStopStart = new MenuItem(manualSlaveStopStart());
                manualStopStart.addActionListener((ActionEvent e) -> {
                    if (!conectionHandler.isManualStop()) {
                        conectionHandler.manualStopSlave();
                    } else {

                        conectionHandler.manualStartSlave();
                    }
                    MenuItem action1 = (MenuItem) e.getSource();
                    action1.setLabel(manualSlaveStopStart());
                });

                popupMenu.add(webItem);
                popupMenu.add(webClient);
                popupMenu.add(manualStopStart);

                MenuItem salir = new MenuItem("Salir");
                salir.addActionListener((ActionEvent e) -> {
                    close();
                });
                popupMenu.addSeparator();
                popupMenu.add(salir);

                try {
                    SystemTray.getSystemTray().add(trayicon);
                } catch (AWTException e1) {

                }

            } else {
                if (visual) {
                    JOptionPane.showMessageDialog(null, "El Sistema Operativo no soporta minimizar en la barra de tareas.");
                }
            }
        }
    }

    public void close() {
        try {
            conectionHandler.stopSlave();
            conectionHandler.stopThread();
            server.stopThead();
        } catch (IOException ex) {
            Logger.getLogger(VisualHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);

    }

    private String statusMaster() {
        return conectionHandler.isMasterRun() ? "Slave Service:Master Running "
                : "Service Slave:Master Stop";
    }

    private String manualSlaveStopStart() {

        return conectionHandler.isManualStop() ? "Start Manual Slave"
                : "Stop Manual Slave";
    }

    public ConectionHandler getContex() {
        return conectionHandler;
    }

    public void setContex(ConectionHandler contex) {
        this.conectionHandler = contex;
    }

    public PopupMenu getPop() {
        return popupMenu;
    }

    public void setPop(PopupMenu pop) {
        this.popupMenu = pop;
    }

    public ConectionHandler getConectionHandler() {
        return conectionHandler;
    }

    @Override
    public void run() {
        while (true) {
            if (visual) {
                trayicon.setToolTip(statusMaster());
                manualStopStart.setLabel(manualSlaveStopStart());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(VisualHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
