package com.zamora.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.Cliente_BEAN;
import com.zamora.modelo.Vehiculo_BEAN;
import com.zamora.trazas.Trazas;
import com.zamora.util.Util;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BusquedaClientes_CON
{
	
	Trazas log;
	Util util;
	ArrayList<Cliente_BEAN> elementos;

    @FXML
    private JFXListView<Cliente_BEAN> listaClientes;

    @FXML
    private JFXTextField idCliente, idFiscal, matricula, nombre, telefono;

    @FXML
    private JFXCheckBox notas;

    @FXML
    private JFXCheckBox deuda;

    @FXML
    private JFXComboBox<String> vehiculo;

    @FXML
    private JFXButton btnAceptar;

    @FXML
    void dobleClick(MouseEvent event)
    {
    	if (event.getClickCount() == 2) { // Verificar si fue un doble clic
            aceptar(event);
        }
    }

    @FXML
    void aceptar(MouseEvent event)
    {
    	if(listaClientes.getSelectionModel().getSelectedItem() != null && !"NO CLIENTES".equals(listaClientes.getSelectionModel().getSelectedItem().getNOMBRE()))
		{
        	Stage stage = (Stage) btnAceptar.getScene().getWindow();
            stage.close();    		
		}
    	else
    	{
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText("No hay un cliente valido seleccionado.");
			alert.showAndWait();
    	}
    }

    @FXML
    void buscar(MouseEvent event)
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
		
		String mensaje = "";
		
		if(idCliente.getText().trim().length() > 0 || idFiscal.getText().trim().length() > 0
				|| nombre.getText().trim().length() > 0 || telefono.getText().trim().length() > 0
				|| matricula.getText().trim().length() > 0 || !vehiculo.getSelectionModel().getSelectedItem().equals("Selecciona")
				|| notas.isSelected() || deuda.isSelected())
		{
			if(idFiscal.getText().trim().length() > 0 && idFiscal.getText().trim().length() < 3)
				mensaje += "La identificacion fiscal debe ser de al menos 3 caracteres.\n";

			if(nombre.getText().trim().length() > 0 && nombre.getText().trim().length() < 3)
				mensaje += "La nombre debe ser de al menos 3 caracteres.\n";

			if(telefono.getText().trim().length() > 0 && telefono.getText().trim().length() < 3)
				mensaje += "El telefono debe ser de al menos 3 caracteres.\n";

			if(matricula.getText().trim().length() > 0 && matricula.getText().trim().length() < 3)
				mensaje += "La matricula debe ser de al menos 3 caracteres.\n";
		}
		else
		{
			mensaje = "Debe rellenar alguno de los campos.";
		}
		
		if(mensaje.length() > 0)
		{
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setTitle("Información");
			alert.setContentText(mensaje);
			alert.showAndWait();
		}
		else
		{
			
			Cliente_BEAN busCliente = new Cliente_BEAN();
			if(Util.isNumeric(idCliente.getText()))
				busCliente.setID_CLIENTE(Integer.parseInt(idCliente.getText()));
			busCliente.setIDFISCAL((idFiscal.getText().trim().length() > 0?idFiscal.getText().trim().toUpperCase():null));
			busCliente.setNOMBRE((nombre.getText().trim().length() > 0?nombre.getText().trim().toUpperCase():null));
			if(Util.isNumeric(telefono.getText()))
				busCliente.setTELEFONO(Integer.parseInt(telefono.getText()));
			if(Util.isNumeric(telefono.getText()))
				busCliente.setMOVIL(Integer.parseInt(telefono.getText()));
			ArrayList<Cliente_BEAN> busqueda;
			
			if(!vehiculo.getSelectionModel().getSelectedItem().equals("Selecciona"))
			{
				String[] vehiculoSearch = vehiculo.getSelectionModel().getSelectedItem().toString().split(" - ");
				busqueda = busCliente.recuperarClientes(conexion, log, busCliente, matricula.getText(), vehiculoSearch[0], vehiculoSearch[1], notas.isSelected(), deuda.isSelected());
			}
			else
			{
				busqueda = busCliente.recuperarClientes(conexion, log, busCliente, matricula.getText(), "", "", notas.isSelected(), deuda.isSelected());					
			}
			

			Vehiculo_BEAN daoVe = new Vehiculo_BEAN();
			for(Cliente_BEAN client : busqueda)
				client.setVEHICULOS(daoVe.recuperarVehiculosCliente(conexion, log, client.getID_CLIENTE()));
			
			if(busqueda.size() == 0)
				busqueda.add(new Cliente_BEAN(
						0,"","NO CLIENTES","","","","", "", 0,"", 0,0, new ArrayList<Vehiculo_BEAN>(), ""));
			
			listaClientes.getItems().removeAll(listaClientes.getItems());
			listaClientes.getItems().addAll(busqueda);
	        listaClientes.getSelectionModel().selectFirst();				
		}
		conexion.desconectar();
    }

    @FXML
    void cancelar(MouseEvent event)
    {
    	idCliente.setText("");
    	idFiscal.setText("");
    	matricula.setText("");
    	nombre.setText("");
    	telefono.setText("");
    	notas.setSelected(false);

		listaClientes.getItems().removeAll(listaClientes.getItems());
		listaClientes.getItems().addAll(elementos);
        listaClientes.getSelectionModel().selectFirst();	
    	
        listaClientes.getSelectionModel().selectFirst();
        vehiculo.getSelectionModel().selectFirst();
    }

    @FXML
    void initialize()
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	
    	Cliente_BEAN cli = new Cliente_BEAN();
    	elementos = (ArrayList<Cliente_BEAN>) cli.recuperarClientes(conexion, log);
    	
    	if(elementos.size() == 0)
    		listaClientes.getItems().add(new Cliente_BEAN(
					0,"","NO CLIENTES","","","","", "", 0,"", 0,0, new ArrayList<Vehiculo_BEAN>(), ""));
        
        listaClientes.getItems().addAll(elementos);
        //listaClientes.getSelectionModel().selectFirst();
        
        util = new Util();
        ArrayList<String> vehiculos = util.recuperarNombresModelos(conexion, log);
        vehiculo.getItems().add("Selecciona");
        vehiculo.getItems().addAll(vehiculos);
        vehiculo.getSelectionModel().selectFirst();
        
		conexion.desconectar();
    }
    
    public void setDato(Trazas log)
    {
    	this.log = log;
    }
    
    public Cliente_BEAN getDato()
    {
    	return listaClientes.getSelectionModel().getSelectedItem();
    }
}