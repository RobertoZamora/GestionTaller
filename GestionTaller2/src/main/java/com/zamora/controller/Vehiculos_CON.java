package com.zamora.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.log4j.Level;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.zamora.Main;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.Cliente_BEAN;
import com.zamora.modelo.Vehiculo_BEAN;
import com.zamora.trazas.Trazas;
import com.zamora.util.Util;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Vehiculos_CON
{

    @FXML
    private JFXButton botonAddVehiculo;

    @FXML
    private ImageView imgAddVehiculo;

    @FXML
    private JFXButton botonEditVehiculo;

    @FXML
    private ImageView imgEditVehiculo;

    @FXML
    private JFXButton botonBorrarVehiculo;

    @FXML
    private ImageView imgBorrarVehiculo;

    @FXML
    private JFXButton botonAlbaranes;

    @FXML
    private ImageView imgAlbaran;

    @FXML
    private JFXButton botonAceptar;

    @FXML
    private ImageView imgAceptar;

    @FXML
    private JFXButton botonCancelar;

    @FXML
    private ImageView imgCancelar;

    @FXML
    private JFXTextField textMatricula;

    @FXML
    private JFXComboBox<String> comboMarca;

    @FXML
    private JFXComboBox<String> comboModelo;

    @FXML
    private JFXComboBox<String> comboCombustible;

    @FXML
    private JFXTextField textBastidor;

    @FXML
    private JFXButton botonFichaTecnica;

    @FXML
    private JFXTextField textFichaTecnica;

    @FXML
    private JFXButton botonVerFichaTecnica;

    @FXML
    private JFXButton botonBorrarFichaTecnica;

    @FXML
    private JFXListView<Vehiculo_BEAN> listaVehiculos;

    @FXML
    private JFXButton botonCambiarCliente;
    
    Trazas log;
	Util util;
	private int idCliente = 0;
	boolean editar = false;
	boolean agnadir = false;
	ArrayList<String> marcas;
	ArrayList<String> combustible;
	ArrayList<Vehiculo_BEAN> vehiculosCliente;
	File selectedFile;
	private final String NO_VEHICULO = "SIN VEHICULOS";
	String rutaFichero;

    @FXML
    void initialize()
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	
    	Vehiculo_BEAN vehiculo = new Vehiculo_BEAN();
    	vehiculosCliente = vehiculo.recuperarVehiculosCliente(conexion, log, idCliente);
    	
    	if(vehiculosCliente.size() == 0)
    		listaVehiculos.getItems().add(new Vehiculo_BEAN(
					0,0,NO_VEHICULO,"","","","", ""));
        
    	listaVehiculos.getItems().addAll(vehiculosCliente);
    	listaVehiculos.getSelectionModel().selectFirst();

		String idFiscal = new Cliente_BEAN().recuperarCliente(conexion, log, idCliente).getIDFISCAL();    	
    	Vehiculo_BEAN seleccionado = listaVehiculos.getSelectionModel().getSelectedItem();
    	rutaFichero = Main.rutaDatos + File.separator + "archivos" + File.separator + idFiscal + File.separator;
    	
    	if(!NO_VEHICULO.equals(seleccionado.getMATRICULA()))
    	{
    		textMatricula.setText(seleccionado.getMATRICULA());
    		textBastidor.setText(seleccionado.getBASTIDOR());
    		textFichaTecnica.setText(seleccionado.getFICHATECNICA());
    		
    		if(seleccionado.getFICHATECNICA() != null && seleccionado.getFICHATECNICA().length() > 0)
    			selectedFile = new File(rutaFichero + File.separator + seleccionado.getFICHATECNICA());
    		else
    			selectedFile = null;
    	}
    	
    	util = new Util();
        marcas = new ArrayList<String>();
        marcas.add("Selecciona");
        marcas.addAll(util.recuperarMarcas(conexion, log));
        comboMarca.getItems().addAll(marcas);
        
        int indice = 0;
    	if(!NO_VEHICULO.equals(listaVehiculos.getItems().get(0).getMATRICULA()))
    	{
	        for(int i = 0; i < marcas.size() && indice == 0; i++)
	        {
	        	if(marcas.get(i).equals(vehiculosCliente.get(0).getMARCA()))
	        	{
	        		indice = i;
	        	}
        	}
        }        
        comboMarca.getSelectionModel().select(indice);
    	
        ArrayList<String> modelo = new ArrayList<String>();
        modelo.add("Selecciona");
        modelo.addAll(util.recuperarModelos(conexion, comboMarca.getSelectionModel().getSelectedItem(), log));
        comboModelo.getItems().addAll(modelo);
        
        indice = 0;
    	if(!NO_VEHICULO.equals(listaVehiculos.getItems().get(0).getMATRICULA()))
    	{
	        for(int i = 0; i < modelo.size() && indice == 0; i++)
	        {
	        	if(modelo.get(i).equals(vehiculosCliente.get(0).getMODELO()))
	        	{
	        		indice = i;
	        	}
        	}
        }        
    	comboModelo.getSelectionModel().select(indice);
        
    	combustible = new ArrayList<String>();
    	combustible.add("Selecciona");
    	combustible.addAll(util.recuperarCombustibles(conexion, log));
    	comboCombustible.getItems().addAll(combustible);
        
        indice = 0;
    	if(!NO_VEHICULO.equals(listaVehiculos.getItems().get(0).getMATRICULA()))
    	{
	        for(int i = 0; i < combustible.size() && indice == 0; i++)
	        {
	        	if(combustible.get(i).equals(vehiculosCliente.get(0).getCOMBUSTIBLE()))
	        	{
	        		indice = i;
	        	}
        	}
        }        
    	comboCombustible.getSelectionModel().select(indice);
    	
    	textMatricula.setDisable(true);
    	comboMarca.setDisable(true);
    	comboModelo.setDisable(true);
    	comboCombustible.setDisable(true);
    	textBastidor.setDisable(true);
    	botonFichaTecnica.setDisable(true);
    	textFichaTecnica.setDisable(true);
    	botonBorrarFichaTecnica.setDisable(true);
    	botonAceptar.setDisable(true);
    	botonCancelar.setDisable(true);
    	botonCambiarCliente.setDisable(true);
    	
		conexion.desconectar();
		
		Tooltip tooltipFichaTecnica = new Tooltip("Selecciona una ficha tecnica.");
    	Tooltip tooltipVerFichaTecn = new Tooltip("Muestra la ficha tecnica.");
    	Tooltip tooltipBorrarFichaT = new Tooltip("Borra la ficha tecnica.");
    	Tooltip tooltipAgnadirVehic = new Tooltip("Añadir vehiculo");
    	Tooltip tooltipEditarVehicu = new Tooltip("Editar Vehiculo");
    	Tooltip tooltipBorrarVehicu = new Tooltip("Editar Clientes");
    	Tooltip tooltipBotonAlbaran = new Tooltip("Albaranes del vehiculo");
    	Tooltip tooltipAceptar = new Tooltip("Aceptar");
    	Tooltip tooltipCancelar = new Tooltip("Cancelar");
    	Tooltip tooltipCambioClient = new Tooltip("Cambiar vehiculo de cliente");
    	
    	botonFichaTecnica.setTooltip(tooltipFichaTecnica);
    	botonVerFichaTecnica.setTooltip(tooltipVerFichaTecn);
    	botonBorrarFichaTecnica.setTooltip(tooltipBorrarFichaT);
    	botonAddVehiculo.setTooltip(tooltipAgnadirVehic);
    	botonEditVehiculo.setTooltip(tooltipEditarVehicu);
    	botonBorrarVehiculo.setTooltip(tooltipBorrarVehicu);
    	botonAlbaranes.setTooltip(tooltipBotonAlbaran);
    	botonAceptar.setTooltip(tooltipAceptar);
    	botonCancelar.setTooltip(tooltipCancelar);
    	botonCambiarCliente.setTooltip(tooltipCambioClient);
	}

    @FXML
    void cambiarDatos(MouseEvent event)
    {    	
    	if(!editar)
    	{
        	Vehiculo_BEAN seleccionado = listaVehiculos.getSelectionModel().getSelectedItem();
    		if(!textMatricula.getText().equals(seleccionado.getMATRICULA()))
    		{
        		ConexionBBDD conexion = new ConexionBBDD(log);
            	conexion.conectar();
            
                int indice = 0;
            	if(!NO_VEHICULO.equals(seleccionado.getMATRICULA()))
            	{
            		textMatricula.setText(seleccionado.getMATRICULA());
            		textBastidor.setText(seleccionado.getBASTIDOR());
            		textFichaTecnica.setText(seleccionado.getFICHATECNICA());
            		
            		if(seleccionado.getFICHATECNICA() != null && seleccionado.getFICHATECNICA().length() > 0)
            			selectedFile = new File(rutaFichero + File.separator + seleccionado.getFICHATECNICA());
            		else
            			selectedFile = null;
            	
        	        for(int i = 0; i < marcas.size() && indice == 0; i++)
        	        {
        	        	if(marcas.get(i).equals(seleccionado.getMARCA()))
        	        	{
        	        		indice = i;
        	        	}
                	}
        	        
        	        comboMarca.getSelectionModel().select(indice);
        	        	        
        	        ArrayList<String> modelo = new ArrayList<String>();
        	        modelo.add("Selecciona");
        	        modelo.addAll(util.recuperarModelos(conexion, comboMarca.getSelectionModel().getSelectedItem(), log));
        	        comboModelo.getItems().clear();
        	        comboModelo.getItems().addAll(modelo);
        	        
        	        indice = 0;
        	        for(int i = 0; i < modelo.size() && indice == 0; i++)
        	        {
        	        	if(modelo.get(i).equals(seleccionado.getMODELO()))
        	        	{
        	        		indice = i;
        	        	}
                	}       
        	    	comboModelo.getSelectionModel().select(indice);
        	    	
        	    	combustible = new ArrayList<String>();
        	    	combustible.add("Selecciona");
        	    	combustible.addAll(util.recuperarCombustibles(conexion, log));
        	    	comboCombustible.getItems().addAll(combustible);
        	        
        	        indice = 0;
        	        for(int i = 0; i < combustible.size() && indice == 0; i++)
        	        {
        	        	if(combustible.get(i).equals(seleccionado.getCOMBUSTIBLE()))
        	        	{
        	        		indice = i;
        	        	}
                	}     
        	    	comboCombustible.getSelectionModel().select(indice);
                }
        		conexion.desconectar();
    		}
    	}
    	else
    	{
    		int indice = 0;
    		for(int i = 0; i < listaVehiculos.getItems().size() && indice == 0; i++)
	        {
	        	if(listaVehiculos.getItems().get(i).getMATRICULA().equals(textMatricula.getText()))
	        	{
	        		indice = i;
	        	}
        	}
    		listaVehiculos.getSelectionModel().select(indice);
    	}
    }
    
    public void setDato(Trazas log, int idCliente)
    {
    	this.log = log;
    	this.idCliente = idCliente;
    }

    @FXML
    void addVehiculo(MouseEvent event)
    {
    	agnadir = true;
    	
		ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	
    	textMatricula.setText("");
		textBastidor.setText("");
		textFichaTecnica.setText("");
		selectedFile = null;
		
		comboMarca.getSelectionModel().selectFirst();
        	        
        ArrayList<String> modelo = new ArrayList<String>();
        modelo.add("Selecciona");
        modelo.addAll(util.recuperarModelos(conexion, comboMarca.getSelectionModel().getSelectedItem(), log));
        comboModelo.getItems().clear();
        comboModelo.getItems().addAll(modelo);
        comboModelo.getSelectionModel().selectFirst();
    	
        comboCombustible.getSelectionModel().selectFirst();

		conexion.desconectar();
    	
    	modoEdicion();
    }

    @FXML
    void editarVehiculo(MouseEvent event)
    {
    	if(!NO_VEHICULO.equals(listaVehiculos.getSelectionModel().getSelectedItem().getMATRICULA()))
		{
        	editar = true;
        	modoEdicion();
		}
    	else
    	{
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText("No se puede editar este vehiculo.");
			alert.showAndWait();
    	}
    }

    @FXML
    void borrarVehiculo(MouseEvent event)
    {
    	if(!NO_VEHICULO.equals(listaVehiculos.getSelectionModel().getSelectedItem().getMATRICULA()))
		{
    		try
    		{
    			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/Password_PAN.fxml"));
    			
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

    			Image icon = new Image(Main.class.getResource("/img/car.png").toExternalForm());
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
        	    		Vehiculo_BEAN vehiculo = listaVehiculos.getSelectionModel().getSelectedItem();
        	    		String idFiscal = new Cliente_BEAN().recuperarCliente(db, log, idCliente).getIDFISCAL();    	
        	    		vehiculo.borradoLogicoVehiculo(db, log, idFiscal);
        	    		
        	    		vehiculosCliente = vehiculo.recuperarVehiculosCliente(db, log, idCliente);

        		    	listaVehiculos.getItems().clear();
        		    	if(vehiculosCliente.size() == 0)
        		    		listaVehiculos.getItems().add(new Vehiculo_BEAN(
        							0,0,NO_VEHICULO,"","","","", ""));
        		    	
        		    	listaVehiculos.getItems().addAll(vehiculosCliente);
        		    	
        		    	listaVehiculos.getSelectionModel().selectFirst();
        		    	
        		    	textMatricula.setText("");
        	    		textBastidor.setText("");
        	    		textFichaTecnica.setText("");
                		selectedFile = null;

        	            comboMarca.getSelectionModel().selectFirst();
        		        	        
        		        ArrayList<String> modelo = new ArrayList<String>();
        		        modelo.add("Selecciona");
        		        modelo.addAll(util.recuperarModelos(db, comboMarca.getSelectionModel().getSelectedItem(), log));
        		        comboModelo.getItems().clear();
        		        comboModelo.getItems().addAll(modelo);      
        		    	comboModelo.getSelectionModel().selectFirst();
        		            		           
        		    	comboCombustible.getSelectionModel().selectFirst();
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
				log.log(Level.ERROR, e);
    		}
		}
    	else
    	{
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText("No se puede borrar este vehiculo.");
			alert.showAndWait();
    	}
    }

    @FXML
    void abrirAlbaranes(MouseEvent event)
    {
    	try {
    		
    		if(!NO_VEHICULO.equals(listaVehiculos.getSelectionModel().getSelectedItem().getMATRICULA()))
    		{
    			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/Albaranes_PAN.fxml"));
    			
    			loader.setControllerFactory(controllerClass -> {
                    if (controllerClass == Albaranes_CON.class) {
                    	int idVehiculo = listaVehiculos.getSelectionModel().getSelectedItem().getID_VEHICULO();
                    	Albaranes_CON seleccion = new Albaranes_CON();
                        seleccion.setDato(log, idCliente, idVehiculo);
                        return seleccion;
                    }
                    return null;
                });
    			
    			Parent root = loader.load();

    			Stage vehiculos = new Stage();
    			vehiculos.setTitle("Albaranes");
    			vehiculos.setScene(new Scene(root, 920, 615));

    			Image icon = new Image(Main.class.getResource("/img/albaran.png").toExternalForm());
    			vehiculos.getIcons().add(icon);
    			

    			// Configura la ventana secundaria como modal
    			vehiculos.initModality(Modality.APPLICATION_MODAL);

    			vehiculos.setResizable(false);

    			vehiculos.showAndWait(); // Muestra la ventana y espera hasta que se cierre
    		}
		} catch (Exception e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
    }

    @FXML
    void aceptar(MouseEvent event)
    {
    	
    	ConexionBBDD db = new ConexionBBDD(log);
		db.conectar();
		Vehiculo_BEAN vehiculo = listaVehiculos.getSelectionModel().getSelectedItem();
		String matOriginal = listaVehiculos.getSelectionModel().getSelectedItem().getMATRICULA();
		
		if(!NO_VEHICULO.equals(matOriginal) && agnadir)
		{
			vehiculo = new Vehiculo_BEAN(); 
		}
		
		boolean correcto = true;
		String error = "";
		textMatricula.setText(textMatricula.getText().toUpperCase());
		textBastidor.setText(textBastidor.getText().toUpperCase());
		
		if(!util.checkMatricula(db, log, textMatricula.getText()))
		{
			correcto = false;
			if(textMatricula.getText().length() == 0)
				error += "La matricula es obligatoria.";
			else
				error += "La matricula no tiene un formato correcto.";
		}
		
		if(agnadir && vehiculo.existeMatricula(db, log, textMatricula.getText()))
		{
			correcto = false;
			error += "\nLa matricula ya existe.";
		}
		
		if(comboMarca.getSelectionModel().getSelectedIndex() == 0)
		{
			correcto = false;
			error += "\nDebe seleccionar una marca.";
		}
		
		if(comboModelo.getSelectionModel().getSelectedIndex() == 0)
		{
			correcto = false;
			error += "\nDebe seleccionar un modelo.";
		}
		
		if(comboCombustible.getSelectionModel().getSelectedIndex() == 0)
		{
			correcto = false;
			error += "\nDebe seleccionar un tipo de combustible.";
		}
		
		if(!textBastidor.getText().equals("") && !util.checkBastidor(textBastidor.getText()))
		{
			correcto = false;
			error += "\nEl bastidor se compone de 4 numeros y 3 letras por favor corrijalo.";
		}
		
		if(correcto)
		{
			vehiculo.setID_CLIENTE(idCliente);
			vehiculo.setMATRICULA(textMatricula.getText());
			vehiculo.setMARCA(comboMarca.getSelectionModel().getSelectedItem());
			vehiculo.setMODELO(comboModelo.getSelectionModel().getSelectedItem());
			vehiculo.setCOMBUSTIBLE(comboCombustible.getSelectionModel().getSelectedItem());
			vehiculo.setBASTIDOR(textBastidor.getText());
			
			
			if(textFichaTecnica.getText() != null && !textFichaTecnica.getText().isEmpty())
			{				
				File destino = new File(rutaFichero);
	            if(!destino.exists())
	            	destino.mkdirs();
	           
	            Path origenPath = FileSystems.getDefault().getPath(selectedFile.getAbsolutePath());
	            Path destinoPath = FileSystems.getDefault().getPath(destino.getAbsolutePath() + File.separator + textMatricula.getText() + selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".")));
	            
	            try {
					Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
					log.log(Level.ERROR, e);
				}
				
				vehiculo.setFICHATECNICA(destinoPath.toString().substring(destinoPath.toString().lastIndexOf(File.separator) + 1));
				textFichaTecnica.setText(destinoPath.toString().substring(destinoPath.toString().lastIndexOf(File.separator) + 1));
				
				selectedFile = new File(destinoPath.toString());
			}
			else if(vehiculo.getFICHATECNICA() != null 
					&& !vehiculo.getFICHATECNICA().isEmpty())
			{
				File origen = new File(rutaFichero + File.separator + vehiculo.getFICHATECNICA());
				origen.delete();
				vehiculo.setFICHATECNICA("");
			}
            
			if(agnadir)
			{
				vehiculo.insertarVehiculo(db, log);
			}
			else if (editar)
			{
				vehiculo.updateVehiculo(db, log);
			}
			
	    	vehiculosCliente = vehiculo.recuperarVehiculosCliente(db, log, idCliente);

	    	listaVehiculos.getItems().clear();
	    	if(vehiculosCliente.size() == 0)
	    		listaVehiculos.getItems().add(new Vehiculo_BEAN(
						0,0,NO_VEHICULO,"","","","", ""));
	    	
	    	listaVehiculos.getItems().addAll(vehiculosCliente);
	    	
	    	int indice = 0;
	    	for(int i = 0; i < listaVehiculos.getItems().size(); i++)
	    	{
	    		if(listaVehiculos.getItems().get(i).getMATRICULA().equals(vehiculo.getMATRICULA()))
	    		{
	    			indice = i;
	    		}
	    	}
	    	
	    	listaVehiculos.getSelectionModel().select(indice);
			
	    	modoVisualizar();
		}
		else
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText(error);
			alert.showAndWait();
		}
		db.desconectar();	
    }

    @FXML
    void cancelar(MouseEvent event)
    {
	   	modoVisualizar();
    	Vehiculo_BEAN seleccionado = listaVehiculos.getSelectionModel().getSelectedItem();

		ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    
        int indice = 0;
    	if(!NO_VEHICULO.equals(seleccionado.getMATRICULA()))
    	{
    		textMatricula.setText(seleccionado.getMATRICULA());
    		textBastidor.setText(seleccionado.getBASTIDOR());
    		textFichaTecnica.setText(seleccionado.getFICHATECNICA());
    		
    		if(seleccionado.getFICHATECNICA() != null && seleccionado.getFICHATECNICA().length() > 0)
    			selectedFile = new File(rutaFichero + File.separator + seleccionado.getFICHATECNICA());
    		else
    			selectedFile = null;
    	
	        for(int i = 0; i < marcas.size() && indice == 0; i++)
	        {
	        	if(marcas.get(i).equals(seleccionado.getMARCA()))
	        	{
	        		indice = i;
	        	}
        	}
	        
	        comboMarca.getSelectionModel().select(indice);
	        	        
	        ArrayList<String> modelo = new ArrayList<String>();
	        modelo.add("Selecciona");
	        modelo.addAll(util.recuperarModelos(conexion, comboMarca.getSelectionModel().getSelectedItem(), log));
	        comboModelo.getItems().clear();
	        comboModelo.getItems().addAll(modelo);
	        
	        indice = 0;
	        for(int i = 0; i < modelo.size() && indice == 0; i++)
	        {
	        	if(modelo.get(i).equals(seleccionado.getMODELO()))
	        	{
	        		indice = i;
	        	}
        	}       
	    	comboModelo.getSelectionModel().select(indice);
	    	
	    	combustible = new ArrayList<String>();
	    	combustible.add("Selecciona");
	    	combustible.addAll(util.recuperarCombustibles(conexion, log));
	    	comboCombustible.getItems().addAll(combustible);
	        
	        indice = 0;
	        for(int i = 0; i < combustible.size() && indice == 0; i++)
	        {
	        	if(combustible.get(i).equals(seleccionado.getCOMBUSTIBLE()))
	        	{
	        		indice = i;
	        	}
        	}     
	    	comboCombustible.getSelectionModel().select(indice);
        }
    	else
    	{
    		textMatricula.setText("");
    		textBastidor.setText("");
    		textFichaTecnica.setText("");
    		selectedFile = null;
    	
	        for(int i = 0; i < marcas.size() && indice == 0; i++)
	        {
	        	if(marcas.get(i).equals(seleccionado.getMARCA()))
	        	{
	        		indice = i;
	        	}
        	}
	        
	        comboMarca.getSelectionModel().select(indice);
	        	        
	        ArrayList<String> modelo = new ArrayList<String>();
	        modelo.add("Selecciona");
	        modelo.addAll(util.recuperarModelos(conexion, comboMarca.getSelectionModel().getSelectedItem(), log));
	        comboModelo.getItems().clear();
	        comboModelo.getItems().addAll(modelo);
	        
	        indice = 0;
	        for(int i = 0; i < modelo.size() && indice == 0; i++)
	        {
	        	if(modelo.get(i).equals(seleccionado.getMODELO()))
	        	{
	        		indice = i;
	        	}
        	}       
	    	comboModelo.getSelectionModel().select(indice);
	        
	        indice = 0;
	        for(int i = 0; i < combustible.size() && indice == 0; i++)
	        {
	        	if(combustible.get(i).equals(seleccionado.getCOMBUSTIBLE()))
	        	{
	        		indice = i;
	        	}
        	}     
	    	comboCombustible.getSelectionModel().select(indice);
    	}
		conexion.desconectar();
    }

    @FXML
    void buscarFichaTecnica(MouseEvent event)
    {
    	
    	// Crea un FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona un archivo"); // Título de la ventana

        FileChooser.ExtensionFilter extFilterImg = new FileChooser.ExtensionFilter("Archivos de Imagen (*.png, *.jpeg, *.jpeg, *.gif, *.bmp)", "*.png", "*.jpeg", "*.jpg", "*.gif", "*.bmp");
        
        // Agrega los filtros al FileChooser
        fileChooser.getExtensionFilters().addAll(extFilterImg);

        // Establece el directorio inicial en la carpeta home del usuario
        fileChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));        

        // Muestra el diálogo de selección de archivos
        File selectedFileAux = fileChooser.showOpenDialog(((Stage) botonFichaTecnica.getScene().getWindow()));

        // Verifica si se seleccionó un archivo
        if (selectedFileAux != null) {
        	selectedFile = selectedFileAux;
            String filePath = selectedFile.getAbsolutePath().substring(selectedFile.getAbsolutePath().lastIndexOf(File.separator) + 1);
            textFichaTecnica.setText(filePath);
        }
        else
        {
        	Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText("El tipo de archivo seleccionado no es valido.");
			alert.showAndWait();
        }
    }

    @FXML
    void verFichaTecnica(MouseEvent event)
    {
    	try {
    		if(selectedFile != null)
    		{
    			if(selectedFile.exists())
    			{
    				Runtime.getRuntime().exec( "CMD /C START " + selectedFile.getAbsolutePath());
    			}
    			else
    			{
    				Alert alert = new Alert(Alert.AlertType.ERROR);
    				alert.setHeaderText(null);
    				alert.setTitle("Error");
    				alert.setContentText("El archivo ya no existe.");
    				alert.showAndWait();
    			}
    		}
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
    }

    @FXML
    void borrarFichaTecnica(MouseEvent event)
    {
    	Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmationAlert.setHeaderText(null);
	    confirmationAlert.setTitle("Confirmación");
	    confirmationAlert.setContentText("Se va a proceder a borrar la ficha técnica ¿Esta Seguro?");
	    
	    Optional<ButtonType> result = confirmationAlert.showAndWait();
	    if (result.isPresent() && result.get() == ButtonType.OK)
	    {
	    	selectedFile = null;
	    	textFichaTecnica.setText("");
	    }
    }

    @FXML
    void cambiarMarca(ActionEvent event)
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	
    	ArrayList<String> modelo = new ArrayList<String>();
        modelo.add("Selecciona");
        modelo.addAll(util.recuperarModelos(conexion, comboMarca.getSelectionModel().getSelectedItem(), log));
        comboModelo.getItems().clear();
        comboModelo.getItems().addAll(modelo);
        
        int indice = 0;
             
    	comboModelo.getSelectionModel().select(indice);
    	conexion.desconectar();
    }
    
    public void modoEdicion()
    {
    	listaVehiculos.setDisable(true);
    	textMatricula.setDisable(false);
    	comboMarca.setDisable(false);
    	comboModelo.setDisable(false);
    	comboCombustible.setDisable(false);
    	textBastidor.setDisable(false);
    	botonFichaTecnica.setDisable(false);
    	botonVerFichaTecnica.setDisable(true);
    	botonBorrarFichaTecnica.setDisable(false);
    	botonAddVehiculo.setDisable(true);
    	botonEditVehiculo.setDisable(true);
    	botonBorrarVehiculo.setDisable(true);
    	botonAlbaranes.setDisable(true);
    	botonAceptar.setDisable(false);
    	botonCancelar.setDisable(false);
    	if (editar)
		{
        	botonCambiarCliente.setDisable(false);
		}
    }
    
    public void modoVisualizar()
    {
    	agnadir = false;
    	editar = false;
    	listaVehiculos.setDisable(false);
    	textMatricula.setDisable(true);
    	comboMarca.setDisable(true);
    	comboModelo.setDisable(true);
    	comboCombustible.setDisable(true);
    	textBastidor.setDisable(true);
    	botonFichaTecnica.setDisable(true);
    	botonVerFichaTecnica.setDisable(false);
    	botonBorrarFichaTecnica.setDisable(true);
    	botonAddVehiculo.setDisable(false);
    	botonEditVehiculo.setDisable(false);
    	botonBorrarVehiculo.setDisable(false);
    	botonAlbaranes.setDisable(false);
    	botonAceptar.setDisable(true);
    	botonCancelar.setDisable(true);
    	botonCambiarCliente.setDisable(true);
    }

    @FXML
    void cambiarCliente(MouseEvent event) {
    	
    	if(!NO_VEHICULO.equals(listaVehiculos.getSelectionModel().getSelectedItem().getMATRICULA()))
		{
    		try {
    			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/Password_PAN.fxml"));
    			
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

    			Image icon = new Image(Main.class.getResource("/img/car.png").toExternalForm());
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
				db.desconectar();
    	    	
				if(!"".equals(aux))
    	    	{
    	    		if(pass.equals(aux))
        	    	{
    	    			FXMLLoader loaderBusqueda = new FXMLLoader(Main.class.getResource("/vistas/BusquedaClientes_PAN.fxml"));
    	    			
    	    			loaderBusqueda.setControllerFactory(controllerClass -> {
    	                    if (controllerClass == BusquedaClientes_CON.class) {
    	                    	BusquedaClientes_CON seleccion = new BusquedaClientes_CON();
    	                        seleccion.setDato(log);
    	                        return seleccion;
    	                    }
    	                    return null;
    	                });
    	    			
    	    			Parent rootBusqueda = loaderBusqueda.load();
    	    			
    	    			Stage seleccionableBusqueda = new Stage();
    	    			seleccionableBusqueda.setTitle("Buscar Cliente");

    	    			Image iconBusqueda = new Image(Main.class.getResource("/img/car.png").toExternalForm());
    	    			seleccionableBusqueda.getIcons().add(iconBusqueda);

    	    			seleccionableBusqueda.setScene(new Scene(rootBusqueda));

    	    			// Configura la ventana secundaria como modal
    	    			seleccionableBusqueda.initModality(Modality.APPLICATION_MODAL);

    	    			// Configura el evento para establecer el tamaño mínimo
    	    			seleccionableBusqueda.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
    	                    @Override
    	                    public void handle(WindowEvent event) {
    	                    	seleccionableBusqueda.setMinWidth(seleccionableBusqueda.getWidth());
    	                    	seleccionableBusqueda.setMinHeight(seleccionableBusqueda.getHeight());
    	                    }
    	                });

    	    			seleccionableBusqueda.showAndWait(); // Muestra la ventana y espera hasta que se cierre
    	    			Cliente_BEAN auxBusqueda = ((BusquedaClientes_CON)loaderBusqueda.getController()).getDato();
    	    			
    	    			if(auxBusqueda != null && !"NO CLIENTES".equals(auxBusqueda.getNOMBRE()))
    	    			{
    	    				boolean cambiar = util.confirmar("Se va a cambiar el vehiculo seleccionado al cliente " + auxBusqueda.toString());
    	    				
    	    				if(cambiar)
    	    				{
    	    					db.conectar();
        	    				
        	    				Vehiculo_BEAN vehiculo = listaVehiculos.getSelectionModel().getSelectedItem();
        	    				
        	    				vehiculo.setID_CLIENTE(auxBusqueda.getID_CLIENTE());
        	    				vehiculo.updateVehiculo(db, log);
        	    				
        	    				vehiculosCliente = vehiculo.recuperarVehiculosCliente(db, log, idCliente);

        	    		    	listaVehiculos.getItems().clear();
        	    		    	if(vehiculosCliente.size() == 0)
        	    		    		listaVehiculos.getItems().add(new Vehiculo_BEAN(
        	    							0,0,NO_VEHICULO,"","","","", ""));
        	    		    	
        	    		    	listaVehiculos.getItems().addAll(vehiculosCliente);
        	    		    	
        	    		    	int indice = 0;
        	    		    	for(int i = 0; i < listaVehiculos.getItems().size(); i++)
        	    		    	{
        	    		    		if(listaVehiculos.getItems().get(i).getMATRICULA().equals(vehiculo.getMATRICULA()))
        	    		    		{
        	    		    			indice = i;
        	    		    		}
        	    		    	}
        	    		    	
        	    		    	listaVehiculos.getSelectionModel().select(indice);
        	    				
        	    		    	modoVisualizar();
        	    				db.desconectar();
    	    				}
    	    			}
        	    	}
    	    	}
    	    } catch (Exception e) {
    	    	Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setHeaderText(null);
    			alert.setTitle("Error");
    			alert.setContentText(e.getMessage());
    			alert.showAndWait();
    	        e.printStackTrace();
    			log.log(Level.ERROR, e);
    	    }
		}
    	else
    	{
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText("No se puede cambiar de cliente este vehiculo.");
			alert.showAndWait();
    	}
    }
}
