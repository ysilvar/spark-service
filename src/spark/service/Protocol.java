/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

public class Protocol {
private VisualHandler visual;

    public Protocol(VisualHandler visual) {
        this.visual = visual;
    }

    public String processInput(String theInput) {

        switch (theInput) {
            case "stop-slave":
                visual.getConectionHandler().manualStopSlave();
                return "Sucess"+theInput;

            case "start-slave":
               visual.getConectionHandler().manualStartSlave();

                return "Sucess"+theInput;
            case "close":
                visual.close();
                return "close";
        }

        return "df";
    }
}
