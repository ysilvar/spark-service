/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiServer extends Thread{

        ServerSocket serverSocket = null;
        int port;
        Protocol protocol;

    public MultiServer(int port, Protocol protocol) {
        this.port = port;
        this.protocol = protocol;
        start();
    }
        
        
    
//    public static void main(String[] args) {
//     
//        MultiServer server = new MultiServer(4444);
//    }

    @Override
    public void run() {
       boolean listening = true;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(-1);
        }

        while (listening){
            try {
                new MultiServerThread(serverSocket.accept(),protocol).start();
            } catch (IOException ex) {
                Logger.getLogger(MultiServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }

        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(MultiServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}