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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import spark.Caca;

/**
 *
 * @author Yorlay Silva Rodriguez
 */
public class Windows {

    private PopupMenu pop;
    private ImageIcon icono;
    private TrayIcon trayicon;
    private Windows windows;
    private GetContext contex;
    private ReadConfig readConfig;
    private boolean manualStop;
    private InterfazGrafica interfaz;

    public Windows() throws FileNotFoundException, IOException {
        windows = this;
        contex = new GetContext();
        readConfig = new ReadConfig();
        
        System.out.println(readConfig.getConfig().length);
        manualStop = false;
        interfaz = new InterfazGrafica();

        if (SystemTray.isSupported()) {

            icono = new ImageIcon(getClass().getResource("../ok.png"));
           // fondo = new ImageIcon(getClass().getResource("../fondo.jpg"));
            pop = new PopupMenu();
            MenuItem btnInterfaz = new MenuItem("Configure Slave");
            btnInterfaz.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    interfaz.setVisible(true);
                }

            });

            MenuItem webItem = new MenuItem("Show master in browser");
            webItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Desktop.getDesktop().browse(URI.create("http://"
                                + readConfig.getSystemVariable("SPARK_MASTER_IP")
                                + ":" + readConfig.getSystemVariable("SPARK_MASTER_WEBUI_PORT")));
                    } catch (IOException e1) {
                        e1.printStackTrace();
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
                        e1.printStackTrace();
                    }
                }
            });

            MenuItem action = new MenuItem(action());
            action.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    try {
                        if (!manualStop) {
                            contex.stopSlave();
                            System.out.println("stop------slave");
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Windows.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    manualStop = !manualStop;
                    MenuItem action = (MenuItem) e.getSource();
                    action.setLabel(action());
                }
            });

            pop.add(btnInterfaz);
            pop.add(webItem);
            pop.add(webClient);
            pop.add(action);

            MenuItem salir = new MenuItem("Salir");
            salir.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        contex.stopSlave();
                    } catch (IOException ex) {
                        Logger.getLogger(Windows.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.exit(0);
                }

            });
            pop.addSeparator();
            pop.add(salir);

            trayicon = new TrayIcon(icono.getImage(), "Service Slave", pop);

            try {
                SystemTray.getSystemTray().add(trayicon);
            } catch (AWTException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(null, "El Sistema Operativo no soporta minimizar en la barra de tareas.");
        }

    }

   

    private String action() {

        return !contex.isSlaveRun() ? "Stop Slave" : "Start Slave";
    }

    public static void main(String arg[]) throws FileNotFoundException, IOException {
        Windows windows = new Windows();
        
        Timer timer = new Timer(900, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ReadConfig nvo = new ReadConfig();
                    if (windows.readConfig.compareTo(nvo) == 0) {
                       
                        windows.readConfig = nvo;
                    }
                    if (windows.contex.testConextion()) {
                        //p.icono = new ImageIcon(getClass().getResource("../ok.png"));
                        if (!windows.contex.isSlaveRun() && !windows.manualStop) {
                            windows.contex.startSlave();
                            windows.pop.getItem(3).setLabel(windows.action());

                        }

                        windows.trayicon.setToolTip("Service Slave: Master Runing");

                    } else {
                        windows.trayicon.setToolTip("Service Slave: Master stop");
                        if (windows.contex.isSlaveRun()) {
                            System.out.println("Entro al master-stop");
                            windows.contex.stopSlave();
                            windows.pop.getItem(3).setLabel(windows.action());

                        }

                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Windows.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Windows.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        timer.start();
        

    }

}
