/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

public class Protocol {
private ConectionHandler conectionHandler;

    public Protocol(ConectionHandler conectionHandler) {
        this.conectionHandler = conectionHandler;
    }

    public String processInput(String theInput) {

        switch (theInput) {
            case "StopSlave":
                conectionHandler.setManualStopOrStart();
                return theInput;

            case "StartSlave":
               conectionHandler.setManualStopOrStart();

                return theInput;
        }

        return "sto";
    }
}
