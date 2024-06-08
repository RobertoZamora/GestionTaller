package com.zamora.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Level;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.zamora.Main;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.Nota_BEAN;
import com.zamora.trazas.Trazas;
import com.zamora.util.Util;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Notas_CON
{

    @FXML
    private JFXTextArea areaTextoNota;

    @FXML
    private JFXButton btnAddNota;

    @FXML
    private JFXButton btnEditNota;

    @FXML
    private JFXButton btnBorrarNota;

    @FXML
    private JFXButton btnAceptar;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private JFXListView<Nota_BEAN> listNotas;

    @FXML
    private JFXCheckBox chkImportante;

    @FXML
    private JFXCheckBox chkPrincipal;
    
    private Trazas log;
	Util util;
    private int idCliente;
    private ArrayList<Nota_BEAN> notas;
    private final String NO_NOTAS = "SIN NOTAS";
    
    private boolean insertar = false;
    private boolean actualizar = false;
    
    

    @FXML
    void initialize()
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	util = new Util();
    	
    	notas = new Nota_BEAN().recuperarNotasCliente(conexion, log, idCliente);
    	
    	if(notas.size() == 0)
    	{
    		listNotas.getItems().add(new Nota_BEAN(
					0,0,"",NO_NOTAS,false,false));
    	}
    	
    	listNotas.getItems().addAll(notas);
    	listNotas.getSelectionModel().selectFirst();
    	
    	cargarSeleccionado();
    	
    	modoVisualizacion();
    	
		conexion.desconectar();
		
		Tooltip tooltipAddNota = new Tooltip("Añade nota.");
		Tooltip tooltipEditNota = new Tooltip("Edita nota.");
		Tooltip tooltipBorrarNota = new Tooltip("Borra nota.");
		Tooltip tooltipAceptar = new Tooltip("Aceptar.");
		Tooltip tooltipCancelar = new Tooltip("Cancelar.");

		btnAddNota.setTooltip(tooltipAddNota);
		btnEditNota.setTooltip(tooltipEditNota);
		btnBorrarNota.setTooltip(tooltipBorrarNota);
		btnAceptar.setTooltip(tooltipAceptar);
		btnCancelar.setTooltip(tooltipCancelar);
	}
    
    public void setDato(Trazas log, int idCliente)
    {
    	this.log = log;
    	this.idCliente = idCliente;
    }

    @FXML
    void seleccionarNota(MouseEvent event)
    {
    	cargarSeleccionado();
    }

    @FXML
    void addNota(MouseEvent event)
    {
    	insertar = true;
    	modoEdicion();
    	chkPrincipal.setSelected(false);
    	chkImportante.setSelected(false);
    	areaTextoNota.setText(""); 
    }

    @FXML
    void editNota(MouseEvent event)
    {
    	if(!NO_NOTAS.equals(listNotas.getSelectionModel().getSelectedItem().getNOMBRE()))
    	{
        	actualizar = true;
        	modoEdicion();
    	}
    }

    @FXML
    void borrarNota(MouseEvent event)
    {
    	if(!NO_NOTAS.equals(listNotas.getSelectionModel().getSelectedItem().getNOMBRE()))
    	{
    		try
    		{
    			FXMLLoader loader = new FXMLLoader(Main.class.getResource("vistas/Password_PAN.fxml"));
    			
    			loader.setControllerFactory(controllerClass -> {
    	            if (controllerClass == Password_CON.class) {
    	            	Password_CON pass = new Password_CON();
    	                pass.setDato(log);
    	                return pass;
    	            }
    	            return null;
    	        });
    			
    			Parent root = loader.load();
    			
    			Stage seleccionable = new Stage();
    			seleccionable.setTitle("Contraseña");

    			Image icon = new Image(Main.class.getResource("img/car.png").toExternalForm());
    			seleccionable.getIcons().add(icon);

    			seleccionable.setScene(new Scene(root));

    			// Configura la ventana secundaria como modal
    			seleccionable.initModality(Modality.APPLICATION_MODAL);

    			// Configura el evento para establecer el tamaño mínimo
    			seleccionable.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
    	            @Override
    	            public void handle(WindowEvent event) {
    	            	seleccionable.setMinWidth(seleccionable.getWidth());
    	            	seleccionable.setMinHeight(seleccionable.getHeight());
    	            }
    	        });

    			seleccionable.showAndWait(); // Muestra la ventana y espera hasta que se cierre
    			String aux = ((Password_CON)loader.getController()).getDato();

    	    	ConexionBBDD db = new ConexionBBDD(log);
    	    	db.conectar();
    	    		    	
    	    	String pass = util.recuperarParametro(db, log, "PASSWORD");
    	    	if(!"".equals(aux))
    	    	{
    	    		if(pass.equals(aux))
        	    	{
    	    			Nota_BEAN seleccionado = listNotas.getSelectionModel().getSelectedItem();
    	    			seleccionado.borrarNotaCliente(db, log);
    	    			listNotas.getItems().remove(seleccionado);

    	    			listNotas.getItems().clear();
    	            	if(listNotas.getItems().size() == 0)
    	            	{
    	            		listNotas.getItems().add(new Nota_BEAN(
    	        					0,0,"",NO_NOTAS,false,false));
    	            	}
    	            	
    	            	listNotas.getSelectionModel().selectFirst();
    	            	
    	            	cargarSeleccionado();
        	    	}
            		else
        	    	{
        	    		Alert alert = new Alert(Alert.AlertType.ERROR);
        				alert.setHeaderText(null);
        				alert.setTitle("Error");
        				alert.setContentText("Contraseña incorrecta.");
        				alert.showAndWait();
        	    	}
    	    	}    	    	
    	    	
    	    	db.desconectar();
    			
    		}
    		catch (IOException e)
    		{
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setHeaderText(null);
    			alert.setTitle("Error");
    			alert.setContentText(e.getMessage());
    			alert.showAndWait();
    			e.printStackTrace();
    		}
    	}
    }

    @FXML
    void aceptar(MouseEvent event)
    {
    	try
		{

        	ConexionBBDD conexion = new ConexionBBDD(log);
        	conexion.conectar();
    		if(insertar)
        	{
        		FXMLLoader loader = new FXMLLoader(Main.class.getResource("vistas/PedirDato_PAN.fxml"));
    			
    			loader.setControllerFactory(controllerClass -> {
    	            if (controllerClass == PedirDato_CON.class) {
    	            	PedirDato_CON pass = new PedirDato_CON();
    	                pass.setDato(log);
    	                return pass;
    	            }
    	            return null;
    	        });
    			
    			Parent root = loader.load();
    			
    			Stage seleccionable = new Stage();
    			seleccionable.setTitle("Nombre de la nota.");

    			Image icon = new Image(Main.class.getResource("img/car.png").toExternalForm());
    			seleccionable.getIcons().add(icon);

    			seleccionable.setScene(new Scene(root));

    			// Configura la ventana secundaria como modal
    			seleccionable.initModality(Modality.APPLICATION_MODAL);

    			// Configura el evento para establecer el tamaño mínimo
    			seleccionable.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
    	            @Override
    	            public void handle(WindowEvent event) {
    	            	seleccionable.setMinWidth(seleccionable.getWidth());
    	            	seleccionable.setMinHeight(seleccionable.getHeight());
    	            }
    	        });

    			seleccionable.showAndWait(); // Muestra la ventana y espera hasta que se cierre
    			String aux = (String) ((PedirDato_CON)loader.getController()).getDato();
        		
    			if(!"".equals(aux))
    			{
    				Nota_BEAN newNota = null;
            		int newId = listNotas.getSelectionModel().getSelectedItem().getID_NOTA() + 1;
            		if(!NO_NOTAS.equals(listNotas.getSelectionModel().getSelectedItem().getNOMBRE()))
            			newNota = new Nota_BEAN();
            		else
            			newNota = listNotas.getSelectionModel().getSelectedItem();
            		
            		newNota.setID_NOTA(newId);
            		newNota.setID_CLIENTE(idCliente);
            		newNota.setNOMBRE(aux);
            		newNota.setPRINCIPAL(chkPrincipal.isSelected());
            		newNota.setIMPORTANTE(chkImportante.isSelected());
            		newNota.setNOTA(areaTextoNota.getText());
            		
            		newNota.insertarNotaCliente(conexion, log);
            		
            		if(listNotas.getItems().size() == 1 && newNota.equals(listNotas.getSelectionModel().getSelectedItem()))
            			listNotas.getItems().clear();
        			listNotas.getItems().add(newNota);
        	    	listNotas.getSelectionModel().selectLast();
                	cargarSeleccionado();
    			}
    			else
    			{

    				Alert alert = new Alert(Alert.AlertType.ERROR);
    				alert.setHeaderText(null);
    				alert.setTitle("Error");
    				alert.setContentText("Debe darle un nombre a la nota");
    				alert.showAndWait();
    			}
    			
        	}
        	else if (actualizar)
        	{
        		Nota_BEAN seleccionada = listNotas.getSelectionModel().getSelectedItem();
        		seleccionada.setPRINCIPAL(chkPrincipal.isSelected());
        		seleccionada.setIMPORTANTE(chkImportante.isSelected());
        		seleccionada.setNOTA(areaTextoNota.getText());
        		
        		seleccionada.actualizarNotaCliente(conexion, log);
        	}
    		conexion.desconectar();
        	
        	modoVisualizacion();
        	actualizar = false;
        	insertar = false;

		}
		catch (IOException e)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}	
    }

    @FXML
    void cancelar(MouseEvent event)
    {
    	cargarSeleccionado();
    	modoVisualizacion();
    	actualizar = false;
    	insertar = false;
    }
    
    public void modoEdicion()
    {
    	btnAddNota.setDisable(true); 
    	btnBorrarNota.setDisable(true); 
    	btnEditNota.setDisable(true); 
    	chkPrincipal.setDisable(false); 
    	chkImportante.setDisable(false); 
    	areaTextoNota.setEditable(true);
    	btnAceptar.setDisable(false); 
    	btnCancelar.setDisable(false);
    	listNotas.setDisable(true);
    }
    
    public void modoVisualizacion()
    {
    	btnAddNota.setDisable(false); 
    	btnBorrarNota.setDisable(false); 
    	btnEditNota.setDisable(false); 
    	chkPrincipal.setDisable(true); 
    	chkImportante.setDisable(true); 
    	areaTextoNota.setEditable(false);
    	btnAceptar.setDisable(true); 
    	btnCancelar.setDisable(true);
    	listNotas.setDisable(false);
    }
    
    public void cargarSeleccionado()
    {

    	Nota_BEAN seleccionado = listNotas.getSelectionModel().getSelectedItem();
    	chkPrincipal.setSelected(seleccionado.isPRINCIPAL());
    	chkImportante.setSelected(seleccionado.isIMPORTANTE());
    	areaTextoNota.setText(seleccionado.getNOTA()); 
    }

}