/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiServerThread extends Thread {
    private Socket socket = null;
    private Protocol protocol;

    public MultiServerThread(Socket socket, Protocol protocol) {
	super("KKMultiServerThread");
	this.socket = socket;
        this.protocol = protocol;
    }

    public void run() {
	try {
	    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	    BufferedReader in = new BufferedReader(
				    new InputStreamReader(
				    socket.getInputStream()));

	    String inputLine, outputLine;
	    
	    outputLine = protocol.processInput("sd");
	    out.println(outputLine);

	    while ((inputLine = in.readLine()) != null) {
		outputLine = protocol.processInput(inputLine);
		out.println(outputLine);
		if (outputLine.equals("stop")){
                    System.out.println("Se paro");
		    break;
                }
	    }
	    out.close();
	    in.close();
	    socket.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}