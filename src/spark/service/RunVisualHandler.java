/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author administrador
 */
public class RunVisualHandler {

    public static void main(String arg[]) {

        VisualHandler handler;

        try {
            if (arg.length > 0) {

                 handler = new VisualHandler(false);
                handler.run();
               
            } else {
                handler = new VisualHandler();
                handler.run();

            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Verify that the spark setting is correct..");

            Logger.getLogger(RunVisualHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
