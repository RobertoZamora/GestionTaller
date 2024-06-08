package com.zamora.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.CargoDocumento_BEAN;
import com.zamora.modelo.Documento_BEAN;
import com.zamora.modelo.Empresa_BEAN;
import com.zamora.trazas.Trazas;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.log4j.Level;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DocumentosCliente_CON {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnAceptar;

    @FXML
    private JFXListView<Documento_BEAN> listaDocumentos;
    private Trazas log;
    private String tipoDoc;
    private int idEmpresa;
    private int idCliente;
    private Documento_BEAN seleccionado;
    private ArrayList<Documento_BEAN> documentos;
    
    

    @FXML
    void aceptar(MouseEvent event)
    {
    	if(listaDocumentos.getSelectionModel().getSelectedItem() != null
    			&& listaDocumentos.getSelectionModel().getSelectedItem().getIdDocumento() != 0)
		{
        	seleccionado = listaDocumentos.getSelectionModel().getSelectedItem();
        	Stage stage = (Stage) btnAceptar.getScene().getWindow();
            stage.close();    		
		}
    	else
    	{
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText("No hay ningun documento valido seleccionado.");
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
        ConexionBBDD db = new ConexionBBDD(log);
        db.conectar();
        
        idEmpresa = new Empresa_BEAN().recuperarDatosEmpresaPredefinida(db, log).getIdEmpresa();
        documentos = new Documento_BEAN(log).recuperarDocumentoIdCliente(db, log, idCliente, tipoDoc, idEmpresa);
		
		if(documentos.size() > 0)
			listaDocumentos.getItems().addAll(documentos);
		else
		{
			btnAceptar.setDisable(true);
			listaDocumentos.getItems().add(new Documento_BEAN(0, 0, 0, 0, 0, "", 0, "", "",	0, false, new ArrayList<CargoDocumento_BEAN>(), log));
		}
        
        db.desconectar();
    }
    
    public void setDato(Trazas log, String tipoDoc, int idCliente)
    {
    	this.log = log;
    	this.tipoDoc = tipoDoc;
    	this.idCliente = idCliente;
    	log.log(Level.DEBUG, tipoDoc);
    	log.log(Level.DEBUG, idCliente);
    }
    
    public Documento_BEAN getDato()
    {
    	return seleccionado;
    }
    
    
}