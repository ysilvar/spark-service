/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

/**
 *
 * @author eduardo
 */
public class TableConten {
    private boolean status;
    private String nameVariable;
    private String valueVariable;
    private String commentary;

    public TableConten(boolean status, String nameVariable, String valueVariable, String commentary) {
        this.status = status;
        this.nameVariable = nameVariable;
        this.valueVariable = valueVariable;
        this.commentary = commentary;
    }

    public TableConten() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getNameVariable() {
        return nameVariable;
    }

    public void setNameVariable(String nameVariable) {
        this.nameVariable = nameVariable;
    }

    public String getValueVariable() {
        return valueVariable;
    }

    public void setValueVariable(String valueVariable) {
        this.valueVariable = valueVariable;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
    
    
}
