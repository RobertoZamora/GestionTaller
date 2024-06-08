package com.zamora.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.CargoDocumento_BEAN;
import com.zamora.modelo.Cliente_BEAN;
import com.zamora.modelo.Documento_BEAN;
import com.zamora.modelo.Vehiculo_BEAN;
import com.zamora.trazas.Trazas;
import com.zamora.util.Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BusquedaDocumentos_CON
{	
	private Trazas log;
	private Util util;
	private String tipoDoc;
	private int idEmpresa;
	private Documento_BEAN seleccionado;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXListView<Documento_BEAN> listaDocumentos;

    @FXML
    private JFXComboBox<String> tipoBusqueda;

    @FXML
    private JFXTextField busqueda;

    @FXML
    private JFXButton buscar;

    @FXML
    private JFXButton reset;

    @FXML
    private JFXButton aceptar;

    @FXML
    void aceptar(MouseEvent event)
    {
    	if(listaDocumentos.getSelectionModel().getSelectedItem() != null
    			&& listaDocumentos.getSelectionModel().getSelectedItem().getIdDocumento() != 0)
		{
        	seleccionado = listaDocumentos.getSelectionModel().getSelectedItem();
        	Stage stage = (Stage) aceptar.getScene().getWindow();
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
    void buscar(MouseEvent event)
    {
    	ConexionBBDD db = new ConexionBBDD(log);
		db.conectar();
		
		String tipo = tipoBusqueda.getSelectionModel().getSelectedItem();
		String textoBusqueda = busqueda.getText().toUpperCase();
		busqueda.setText(textoBusqueda);
		
		switch(tipo)
		{
		case "Numero de Documento":
			if(textoBusqueda != null && !textoBusqueda.isEmpty())
			{
				if(util.isSerieDoc(textoBusqueda))
				{
					
					String[] datos = textoBusqueda.split("/");
					Documento_BEAN documento = new Documento_BEAN(log);
					documento = documento.recuperarDocumento(db, log, Integer.parseInt(datos[1]), tipoDoc, idEmpresa, Integer.parseInt(datos[0]));
					
					listaDocumentos.getItems().clear();
					if(documento != null && documento.getIdDocumento() != 0)
						listaDocumentos.getItems().add(documento);
					else
						listaDocumentos.getItems().add(new Documento_BEAN(0, 0, 0, 0, 0, "", 0, "", "",
								0, false, new ArrayList<CargoDocumento_BEAN>(), log));
				}
				else
				{
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText(null);
					alert.setTitle("Error");
					alert.setContentText("Al seleccionar busqueda por ID CLIENTE debe rellenar el campo con numeros.");
					alert.showAndWait();
				}
			}
			else
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setTitle("Error");
				alert.setContentText("Debe rellenar el campo de busqueda.");
				alert.showAndWait();
			}
			break;
			case "Identificación fiscal":
				if(textoBusqueda.length() == 9)
				{
					textoBusqueda = textoBusqueda.toUpperCase();
					
					Cliente_BEAN cliente = new Cliente_BEAN();
					cliente = cliente.recuperarClienteIdFiscal(db, log, textoBusqueda);
					
					ArrayList<Documento_BEAN> documentos = new Documento_BEAN(log).recuperarDocumentoIdCliente(db, log, cliente.getID_CLIENTE(), tipoDoc, idEmpresa);
					
					listaDocumentos.getItems().clear();
					if(documentos.size() > 0)						
						listaDocumentos.getItems().addAll(documentos);
					else
						listaDocumentos.getItems().add(new Documento_BEAN(0, 0, 0, 0, 0, "", 0, "", "",
								0, false, new ArrayList<CargoDocumento_BEAN>(), log));
					
				}
				else
				{
					if(textoBusqueda.length() == 0)
					{
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setTitle("Error");
						alert.setContentText("Debe rellenar el campo de busqueda.");
						alert.showAndWait();
					}
					else if (textoBusqueda.length() != 9)
					{
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setTitle("Error");
						alert.setContentText("La identificacion fiscal no es correcta(9 caracteres incluida la letra)");
						alert.showAndWait();
					}
				}
				break;
			case "Matricula":
				textoBusqueda = textoBusqueda.toUpperCase();
				
				Vehiculo_BEAN vehiculo = new Vehiculo_BEAN();
				vehiculo = vehiculo.recuperarMatricula(db, log, textoBusqueda);
				
				ArrayList<Documento_BEAN> documentos = new Documento_BEAN(log).recuperarDocumentoIdVehiculo(db, log, vehiculo.getID_VEHICULO(), tipoDoc, idEmpresa);

				listaDocumentos.getItems().clear();
				if(documentos.size() > 0)
					listaDocumentos.getItems().addAll(documentos);
				else
					listaDocumentos.getItems().add(new Documento_BEAN(0, 0, 0, 0, 0, "", 0, "", "",
							0, false, new ArrayList<CargoDocumento_BEAN>(), log));
				break;
			default:
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setTitle("Error");
				alert.setContentText("Debe seleccionar un tipo de busqueda.");
				alert.showAndWait();
		}
		db.desconectar();
	}

    @FXML
    void doubleClick(MouseEvent event)
    {
    	if (event.getClickCount() == 2) { // Verificar si fue un doble clic
            aceptar(event);
        }
    }

    @FXML
    void reset(MouseEvent event)
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	
    	tipoBusqueda.getSelectionModel().selectFirst();
    	busqueda.setText("");
    	listaDocumentos.getSelectionModel().select(-1);
    	
    	List<Documento_BEAN> documentos = new Documento_BEAN(log).recuperarDocumentos(conexion, log, tipoDoc, idEmpresa);
		
		if(documentos.size() > 0)
			listaDocumentos.getItems().addAll(documentos);
		else
		{
			aceptar.setDisable(true);
			listaDocumentos.getItems().add(new Documento_BEAN(0, 0, 0, 0, 0, "", 0, "", "",	0, false, new ArrayList<CargoDocumento_BEAN>(), log));
		}
		conexion.desconectar();
    }

    @FXML
    void initialize()
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();

    	ArrayList<String> busquedas = new ArrayList<>(Arrays.asList(new String[] {"Seleccione un tipo de busqueda","Numero de Documento", "Identificación fiscal", "Matricula"}));
    	tipoBusqueda.getItems().addAll(busquedas);
    	tipoBusqueda.getSelectionModel().selectFirst();
    	
    	
    	List<Documento_BEAN> documentos = new Documento_BEAN(log).recuperarDocumentos(conexion, log, tipoDoc, idEmpresa);
		
		if(documentos.size() > 0)
			listaDocumentos.getItems().addAll(documentos);
		else
		{
			aceptar.setDisable(true);
			listaDocumentos.getItems().add(new Documento_BEAN(0, 0, 0, 0, 0, "", 0, "", "",	0, false, new ArrayList<CargoDocumento_BEAN>(), log));
		}
    	
		conexion.desconectar();
    }
    
    public void setDato(Trazas log, String tipoDoc,int idEmpresa)
    {
    	this.log = log;
    	this.tipoDoc = tipoDoc;
    	this.idEmpresa = idEmpresa;
    	this.util = new Util();
    }
    
    public Documento_BEAN getDato()
    {
    	return this.seleccionado;
    }
}
