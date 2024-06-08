package com.zamora.controller;

import org.apache.log4j.Level;

import com.jfoenix.controls.JFXPasswordField;
import com.zamora.trazas.Trazas;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Password_CON
{
	private Trazas log;
	private String dato;
	
    @FXML
    private JFXPasswordField textPass;

    @FXML
    void aceptar(KeyEvent event)
    {
    	if (event.getCode() == KeyCode.ENTER)
    	{
    		log.log(Level.DEBUG, "contraseña introducida.");
    		dato = textPass.getText();
        	Stage stage = (Stage) textPass.getScene().getWindow();
            stage.close();
    	}
    }

    @FXML
    void initialize()
    {
        
    }
    
    public void setDato(Trazas log)
    {
    	this.log = log;
    }
    
    public String getDato()
    {
    	return dato;
    }
}