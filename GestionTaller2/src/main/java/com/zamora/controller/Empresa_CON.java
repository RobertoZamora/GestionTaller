package com.zamora.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.Empresa_BEAN;
import com.zamora.trazas.Trazas;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Empresa_CON
{

    @FXML
    private JFXComboBox<Empresa_BEAN> cmbEmpresa;

    @FXML
    private JFXTextField textEmpresa;

    @FXML
    private JFXTextField textNombre;

    @FXML
    private JFXTextField textDireccion;

    @FXML
    private JFXTextField textTelefono;

    @FXML
    private JFXTextField textCIF;

    @FXML
    private JFXTextField textCodigoPostal;

    @FXML
    private JFXTextField textLocalidad;

    @FXML
    private JFXTextField textProvincia;

    @FXML
    private JFXCheckBox checkPredefinida;
    
    @FXML
    private JFXButton btnAceptar;
    
    Trazas log;

    @FXML
    void Aceptar(MouseEvent event)
    {
    	Empresa_BEAN aux = cmbEmpresa.getValue();
    	    	
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	if(aux.getEmpresa().equals("NUEVA"))
    	{
    		rellenarCampos(aux);
    		aux.insertarEmpresa(conexion, log);
    	}
    	else
    	{
    		rellenarCampos(aux);
    		aux.actualizarEmpresa(conexion, log);	
    	}
    	conexion.desconectar();
    	
    	// Cerrar la ventana
    	Stage stage = (Stage) ((JFXButton) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    public void rellenarCampos(Empresa_BEAN aux)
    {
    	aux.setEmpresa(textEmpresa.getText());
    	aux.setNombre(textNombre.getText());
    	aux.setDireccion(textDireccion.getText());
    	aux.setTelefono(textTelefono.getText());
    	aux.setCif(textCIF.getText());
    	aux.setCodigoPostal(textCodigoPostal.getText());
    	aux.setMunicipio(textLocalidad.getText());
    	aux.setProvincia(textProvincia.getText());
    	aux.setPredefinida(checkPredefinida.isSelected());
    }

    @FXML
    void cambiarEmpresa(ActionEvent event)
    {
    	Empresa_BEAN aux = cmbEmpresa.getValue();
    	
    	textEmpresa.setText(aux.getEmpresa().equals("NUEVA")?"":aux.getEmpresa());
    	textNombre.setText(aux.getNombre());
    	textDireccion.setText(aux.getDireccion());
    	textTelefono.setText(aux.getTelefono());
    	textCIF.setText(aux.getCif());
    	textCodigoPostal.setText(aux.getCodigoPostal());
    	textLocalidad.setText(aux.getMunicipio());
    	textProvincia.setText(aux.getProvincia());
    	checkPredefinida.setSelected(aux.isPredefinida());
    	
    }

    @FXML
    void initialize()
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	Empresa_BEAN bean = new Empresa_BEAN();
    	
    	// recuperamos las empresas
    	ArrayList<Empresa_BEAN> empresas = bean.getEmrpesas(conexion, log); 
    	conexion.desconectar();
    	bean.setEmpresa("NUEVA");
    	empresas.add(bean);
    	
    	// recuperamos que indice debe estar seleccionado
    	int indice = 0;
    	for(int i = 0; i < empresas.size(); i++)
    		if(empresas.get(i).isPredefinida())
    			indice = i;
    	
    	cmbEmpresa.getItems().addAll(empresas);
    	cmbEmpresa.setValue(cmbEmpresa.getItems().get(indice));
    	
    	textEmpresa.setText(empresas.get(indice).getEmpresa());
    	textNombre.setText(empresas.get(indice).getNombre());
    	textDireccion.setText(empresas.get(indice).getDireccion());
    	textTelefono.setText(empresas.get(indice).getTelefono());
    	textCIF.setText(empresas.get(indice).getCif());
    	textCodigoPostal.setText(empresas.get(indice).getCodigoPostal());
    	textLocalidad.setText(empresas.get(indice).getMunicipio());
    	textProvincia.setText(empresas.get(indice).getProvincia());
    	checkPredefinida.setSelected(empresas.get(indice).isPredefinida());
    	
    }
    
    public void setDato(Trazas log)
    {
    	this.log = log;
    }
}
