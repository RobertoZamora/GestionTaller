package com.zamora.controller;

import java.util.ArrayList;

import org.apache.log4j.Level;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.zamora.trazas.Trazas;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Seleccion_CON
{

	// datos
	ArrayList<String[]> elementos;
	Trazas log;
	
    @FXML
    private JFXListView<String> listado;

    @FXML
    private JFXButton btnAceptar;

    @FXML
    void accionAceptar(MouseEvent event)
    {
    	Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void dobleClick(MouseEvent event)
    {
    	if (event.getClickCount() == 2) { // Verificar si fue un doble clic
            String selectedItem = listado.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
            	Stage stage = (Stage) listado.getScene().getWindow();
                stage.close();
            }
        }
    }

    @FXML
    void initialize()
    {
    	String[] aux = new String[elementos.size()];
        for(int i = 0; i < elementos.size(); i++)
        {
        	aux[i] = elementos.get(i)[0] + " - " + elementos.get(i)[1] + " - " + elementos.get(i)[2];
        	log.log(Level.DEBUG, aux[i].toString());
        }
        
        listado.getItems().addAll(aux);
        listado.getSelectionModel().selectFirst();
    }
    
    public void setDato(Trazas log, ArrayList<String[]> cpMunProv)
    {
    	this.log = log;
    	this.elementos = cpMunProv;
    }
    
    public String[] getDato()
    {
    	int selectedIndex = listado.getSelectionModel().getSelectedIndex();
    	return elementos.get(selectedIndex);
    }
}