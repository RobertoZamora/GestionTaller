package com.zamora.controller;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.Vehiculo_BEAN;
import com.zamora.trazas.Trazas;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BusquedaVehiculo_CON
{
    
    private Trazas log;
    private ArrayList<Vehiculo_BEAN> vehiculos;
    private Vehiculo_BEAN seleccionado;
	private final String NO_VEHICULO = "SIN VEHICULOS";

    @FXML
    private JFXButton btnAceptar;

    @FXML
    private JFXListView<Vehiculo_BEAN> listaVehiculos;

    @FXML
    void aceptar(MouseEvent event) {
    	if(!NO_VEHICULO.equals(listaVehiculos.getSelectionModel().getSelectedItem().getMATRICULA()))
		{
        	seleccionado = listaVehiculos.getSelectionModel().getSelectedItem();
        	Stage stage = (Stage) btnAceptar.getScene().getWindow();
            stage.close();    		
		}
    	else
    	{
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText("No hay un vehiculo valido seleccionado.");
			alert.showAndWait();
    	}
    }

    @FXML
    void dobleClick(MouseEvent event)
    {
    	if (event.getClickCount() == 2) { // Verificar si fue un doble clic
            aceptar(event);
        }
    }

    @FXML
    void initialize()
    {
    	if(vehiculos == null)
    	{
        	ConexionBBDD conexion = new ConexionBBDD(log);
        	conexion.conectar();
        	
        	Vehiculo_BEAN vehiculo = new Vehiculo_BEAN();
        	vehiculos = vehiculo.recuperarTodosVehiculos(conexion, log);
    		conexion.desconectar();
    		        	
        	if(vehiculos.size() == 0)
        		listaVehiculos.getItems().add(new Vehiculo_BEAN(
    					0,0,NO_VEHICULO,"","","","", ""));
    	}
        
    	listaVehiculos.getItems().addAll(vehiculos);
    	listaVehiculos.getSelectionModel().selectFirst();

    }
    
    public void setDato(Trazas log, ArrayList<Vehiculo_BEAN> vehiculos)
    {
    	this.log = log;
    	this.vehiculos = vehiculos;
    }
    
    public Vehiculo_BEAN getDato()
    {
    	return seleccionado;
    }

}