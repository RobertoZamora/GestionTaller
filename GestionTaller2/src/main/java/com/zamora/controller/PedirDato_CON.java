package com.zamora.controller;

import org.apache.log4j.Level;

import com.jfoenix.controls.JFXTextField;
import com.zamora.trazas.Trazas;
import com.zamora.util.Util;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class PedirDato_CON
{
	private Trazas log;
	private Object validar;
	
    @FXML
    private JFXTextField  textPass;

    @FXML
    void aceptar(KeyEvent event)
    {
    	if (event.getCode() == KeyCode.ENTER)
    	{
    		if(validar == null || (validar != null && validar instanceof String))
    		{
    			log.log(Level.DEBUG, "Validamos con String");
    			validar = new String(textPass.getText());
            	Stage stage = (Stage) textPass.getScene().getWindow();
                stage.close();
    		}
    		else if(validar != null && validar instanceof Integer)
    		{
    			log.log(Level.DEBUG, "Validamos con Integer");
    			String str = textPass.getText();
    			if(str.indexOf(",") != -1)
    				str = str.replace(",", ".");
    			boolean valido = Util.isNumeric(str);
    			if(valido)
    			{
        			validar = Integer.parseInt(str);
        			Stage stage = (Stage) textPass.getScene().getWindow();
                    stage.close();
    			}
    			else
    			{
    				Alert alert = new Alert(Alert.AlertType.ERROR);
    				alert.setHeaderText(null);
    				alert.setTitle("Error");
    				alert.setContentText("Debe introducir un numero entero.");
    				alert.showAndWait();
    			}
    		}
    		else if(validar != null && validar instanceof Double)
    		{
    			log.log(Level.DEBUG, "Validamos con double");
    			String str = textPass.getText();
    			if(str.indexOf(",") != -1)
    				str = str.replace(",", ".");
    			boolean valido = Util.isNumeric(str);
    			if(valido)
    			{
        			validar = Double.parseDouble(str);
        			Stage stage = (Stage) textPass.getScene().getWindow();
                    stage.close();
    			}
    			else
    			{
    				Alert alert = new Alert(Alert.AlertType.ERROR);
    				alert.setHeaderText(null);
    				alert.setTitle("Error");
    				alert.setContentText("Debe introducir un numero natural (con decimales).");
    				alert.showAndWait();
    			}
    			
    		}
    	}
    }

    @FXML
    void initialize()
    {
    	if(validar != null)
    	{
    		textPass.setText(validar.toString());
    	}
    		
    }
    
    public void setDato(Trazas log)
    {
    	this.log = log;
    }
    
    public void setDato(Trazas log, Object validar)
    {
    	this.log = log;
    	this.validar = validar;
    }
    
    public Object getDato()
    {
    	return validar;
    }
}