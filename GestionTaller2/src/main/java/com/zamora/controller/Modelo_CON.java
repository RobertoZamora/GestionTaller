package com.zamora.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.Marca_BEAN;
import com.zamora.trazas.Trazas;
import com.zamora.util.Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Modelo_CON {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<Marca_BEAN> listaMarca;

    @FXML
    private JFXTextField nombreModelo;
    private Trazas log;
    private Util util;
    private String[] datos;
    private ArrayList<Marca_BEAN> listadoMarcas;

    @FXML
    void dobleClick(KeyEvent event)
    {
    	if (event.getCode() == KeyCode.ENTER && !event.isShiftDown())
    	{
        	if(nombreModelo.getText() != null && nombreModelo.getText().length() > 0)
        	{
            	datos = new String[2];
            	datos[0] = String.valueOf(listaMarca.getSelectionModel().getSelectedItem().getIdMarca());
            	datos[1] = nombreModelo.getText();
            	
            	Stage stage = (Stage) nombreModelo.getScene().getWindow();
                stage.close();
        	}
        	else
        	{
        		Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setHeaderText(null);
    			alert.setTitle("Error");
    			alert.setContentText("Debe escribir un nombre valido.");
    			alert.showAndWait();
        	}
    	}
    }

    @FXML
    void initialize()
    {
    	ConexionBBDD db = new ConexionBBDD(log);
    	db.conectar();
    	util = new Util();
    	listadoMarcas = util.recuperarMarcasBean(db, log);
    	db.desconectar();
    	listaMarca.getItems().addAll(listadoMarcas);
        listaMarca.getSelectionModel().selectFirst();
    }
    
    public void setDato(Trazas log)
    {
    	this.log = log;
    }
    
    public String[] getDatos()
    {
    	return datos;
    }
}