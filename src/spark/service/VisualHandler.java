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
import java.awt.event.MouseAdapter;
import java.io.FileNotFoundException;
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
public class VisualHandler extends Thread{

    private PopupMenu popupMenu;
    private ImageIcon icono;
    private TrayIcon trayicon;

    private ConectionHandler conectionHandler;
    private ReadConfig readConfig;
    private TableHandler tableHandler;

    public VisualHandler() throws IOException {
        conectionHandler = new ConectionHandler();
        readConfig = new ReadConfig();
        tableHandler = new TableHandler(readConfig);
        try {
            tableHandler.addData(readConfig.tableData());
        } catch (Exception ex) {

        }

        if (SystemTray.isSupported()) {

            icono = new ImageIcon(getClass().getResource("icon.png"));
            popupMenu = new PopupMenu();

            ActionListener configSlave = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    tableHandler.setVisible(true);
                }

            };
            MenuItem btnInterfaz = new MenuItem("Configure Slave");
            btnInterfaz.addActionListener(configSlave);

            MenuItem webItem = new MenuItem("Show master in browser");
            webItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Desktop.getDesktop().browse(URI.create("http://"
                                + readConfig.getSystemVariable("SPARK_MASTER_IP")
                                + ":" + readConfig.getSystemVariable("SPARK_MASTER_WEBUI_PORT")));
                    } catch (IOException e1) {

                    }
                }
            });
            MenuItem webClient = new MenuItem("Show slave in browser");
            webClient.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Desktop.getDesktop().browse(URI.create("http://127.0.0.1:8081"));
                    } catch (IOException e1) {

                    }
                }
            });

            MenuItem action = new MenuItem(conectionHandler.isManualStop()
                    ? "Start Manual Slave" : "Stop Manual Slave");
            action.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    conectionHandler.setManualStop();

                    MenuItem action = (MenuItem) e.getSource();
                    action.setLabel(conectionHandler.isManualStop()
                            ? "Start Manual Slave" : "Stop Manual Slave");
                }
            });

            popupMenu.add(btnInterfaz);
            popupMenu.add(webItem);
            popupMenu.add(webClient);
            popupMenu.add(action);

            MenuItem salir = new MenuItem("Salir");
            salir.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        conectionHandler.stopSlave();
                        conectionHandler.stopThread();
                    } catch (IOException ex) {
                        Logger.getLogger(VisualHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.exit(0);
                }

            });
            popupMenu.addSeparator();
            popupMenu.add(salir);

            trayicon = new TrayIcon(icono.getImage(), conectionHandler.isMasterRun()
                    ? "Slave Service:Master Running " : "Service Slave:Master Stop", popupMenu);
            trayicon.addActionListener(configSlave);
            trayicon.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    System.out.println("sdsadhsa");
                }
            });
            try {
                SystemTray.getSystemTray().add(trayicon);
            } catch (AWTException e1) {

            }

        } else {
            JOptionPane.showMessageDialog(null, "El Sistema Operativo no soporta minimizar en la barra de tareas.");
        }

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

    @Override
    public void run() {
        while(true){
            trayicon.setToolTip(conectionHandler.isMasterRun()
                    ? "Slave Service:Master Running " : "Service Slave:Master Stop");
            System.out.println("sdjfjhdfs");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(VisualHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
}
