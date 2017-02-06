/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

/**
 *
 * @author eduardo
 */
public class Windows extends JFrame {

    private JPopupMenu pop;
    private ImageIcon icono, fondo;
    private TrayIcon trayicon;
    private Windows windows;
    private GetContext contex;
  

    public Windows() throws FileNotFoundException {
        windows = this;
     
        contex = new GetContext();
        if (SystemTray.isSupported()) {

            icono = new ImageIcon(getClass().getResource("../ok.png"));
            fondo = new ImageIcon(getClass().getResource("../fondo.jpg"));
            pop = new JPopupMenu();
            JMenuItem interfaz = new JMenuItem("Interfaz Grafica");
            interfaz.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    windows.setVisible(true);
                    pop.setVisible(false);
                }

            });

            pop.add(interfaz);

            JMenuItem salir = new JMenuItem("Salir");
            salir.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    windows.contex.getProccess().destroyForcibly();
                  

                    System.exit(0);
                }

            });
            pop.addSeparator();
            pop.add(salir);

            trayicon = new TrayIcon(icono.getImage(), "Service Slave", null);
            trayicon.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseReleased(MouseEvent e) {
                    pop.setVisible(true);
                    pop.setLocation(e.getX(), e.getY() - 80);
                }

            });

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

    public void paint(Graphics g) {
        g.drawImage(fondo.getImage(), 0, 15, 500, 400, this);
    }

    public static void main(String arg[]) throws FileNotFoundException {
        Windows windows = new Windows();
        /* Hay que comparar las clases readConf y dejar historico...
         */
        Timer timer = new Timer(900, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    GetContext nvo = new GetContext();
                    if (windows.contex.compareTo(nvo) == 0) 
                    {
                        System.out.println("cambio");
                        
                       windows.contex.setConf(nvo.getConf());      
                    }
                    if (windows.contex.testConextion()) {
                        //p.icono = new ImageIcon(getClass().getResource("../ok.png"));
                        if (!windows.contex.isSlaveRun()) {
                            windows.contex.startSlave();

                        }

                        windows.trayicon.setToolTip("Service Slave: Master Runing");
                        
                    } else {
                        windows.trayicon.setToolTip("Service Slave: Master stop");
                        if (windows.contex.isSlaveRun()) {
                            System.out.println("Entro al master-stop");
                            windows.contex.getProccess().destroy();
                            windows.contex.setProcess(null);
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
        windows.setBounds(0, 0, 500, 400);
        windows.setLocationRelativeTo(null);

    }
}
