package com.zamora.controller;

import com.jfoenix.controls.JFXPasswordField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class PasswordChange_CON {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXPasswordField textPass1;

    @FXML
    private JFXPasswordField textPass2;

    @FXML
    void aceptar(KeyEvent event)
    {
    	if (event.getCode() == KeyCode.ENTER)
    	{
    		if(textPass1.getText().equals(textPass2.getText()))
    		{
            	Stage stage = (Stage) textPass1.getScene().getWindow();
                stage.close();
    		}
    	}
    }

    @FXML
    void initialize()
    {

    }
    
    public String getDato()
    {
    	return textPass1.getText();
    }
}