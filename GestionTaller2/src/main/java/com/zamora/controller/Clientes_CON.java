package com.zamora.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.zamora.Main;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.Cliente_BEAN;
import com.zamora.modelo.Documento_BEAN;
import com.zamora.modelo.Nota_BEAN;
import com.zamora.trazas.Trazas;
import com.zamora.util.Util;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Clientes_CON
{
	
	// PARAMETROS
	private int idCliente = 0;
	private int idUltimoCliente = 0;
	private int idPrimerCliente = 0;
	private int numClientes = 0;
	
	Cliente_BEAN cli;
	Util util;
	ArrayList<String> municipios;
	ArrayList<String> provincias;
	
	// CAMPOS

    @FXML
    private JFXButton btnPrimero;
    
    @FXML
    private ImageView imgPrimero;

    @FXML
    private JFXButton btnAnterior;

    @FXML
    private ImageView imgAnterior;

    @FXML
    private JFXButton btnSiguiente;

    @FXML
    private ImageView imgSiguiente;

    @FXML
    private JFXButton btnUltimo;

    @FXML
    private ImageView imgUltimo;

    @FXML
    private JFXButton btnAgnadirCliente;

    @FXML
    private ImageView imgAgnadirCliente;

    @FXML
    private JFXButton btnEditarCliente;

    @FXML
    private ImageView imgEditarCliente;

    @FXML
    private JFXButton btnBorrarCliente;

    @FXML
    private ImageView imgBorrarCliente;

    @FXML
    private JFXButton btnBuscarCliente;

    @FXML
    private ImageView imgBuscarCliente;

    @FXML
    private JFXButton btnVehiculos;

    @FXML
    private ImageView imgVehiculo;

    @FXML
    private JFXButton btnNotas;

    @FXML
    private ImageView imgNotas;

    @FXML
    private JFXButton btnBuscarDocumento;

    @FXML
    private ImageView imgBuscarDocumento;

    @FXML
    private ImageView imgImportante;

    @FXML
    private JFXTextField numCliente;

    @FXML
    private JFXTextField idFiscal;

    @FXML
    private JFXTextField nombre;

    @FXML
    private JFXTextField cp;

    @FXML
    private JFXTextField localidad;

    @FXML
    private JFXTextField provincia;

    @FXML
    private JFXTextField direccion;

    @FXML
    private JFXTextField portal;

    @FXML
    private JFXTextField piso;

    @FXML
    private JFXTextField letra;

    @FXML
    private JFXTextField telefono;

    @FXML
    private JFXTextField movil;

    @FXML
    private JFXTextField mail;

    @FXML
    private JFXTextArea notaPrincipal;

    @FXML
    private JFXButton btnAceptar;

    @FXML
    private ImageView imgAceptar;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private ImageView imgCancelar;

    @FXML
    private JFXButton btnDeuda;

    @FXML
    private ImageView deuda;
    
    Trazas log;
    
    @FXML
    void initialize()
    {
    	Tooltip tooltipPrimero = new Tooltip("Primero");
    	Tooltip tooltipAnterior = new Tooltip("Anterior");
    	Tooltip tooltipSiguiente = new Tooltip("Siguiente");
    	Tooltip tooltipUltimo = new Tooltip("Ultimo");
    	Tooltip tooltipAgnadirCliente = new Tooltip("Añadir Cliente");
    	Tooltip tooltipEditarCliente = new Tooltip("Editar Clientes");
    	Tooltip tooltipBorrarCliente = new Tooltip("Borrar Cliente");
    	Tooltip tooltipBuscarCliente = new Tooltip("Buscar Cliente");
    	Tooltip tooltipVehiculos = new Tooltip("Vehiculos");
    	Tooltip tooltipNotas = new Tooltip("Notas");
    	Tooltip tooltipBuscarDocumento = new Tooltip("Buscar Documento");
		
		btnPrimero.setTooltip(tooltipPrimero);
		btnAnterior.setTooltip(tooltipAnterior);
		btnSiguiente.setTooltip(tooltipSiguiente);
		btnUltimo.setTooltip(tooltipUltimo);
		btnAgnadirCliente.setTooltip(tooltipAgnadirCliente);
		btnEditarCliente.setTooltip(tooltipEditarCliente);
		btnBorrarCliente.setTooltip(tooltipBorrarCliente);
		btnBuscarCliente.setTooltip(tooltipBuscarCliente);
		btnVehiculos.setTooltip(tooltipVehiculos);
		btnNotas.setTooltip(tooltipNotas);
		btnBuscarDocumento.setTooltip(tooltipBuscarDocumento);
		    	
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	cli = new Cliente_BEAN();
    	
    	idCliente = cli.recuperarIdUltimoCliente(conexion, log);
		idUltimoCliente = cli.recuperarIdUltimoCliente(conexion, log);
		idPrimerCliente = cli.recupererPrimerIdcliente(conexion, log);
		numClientes = cli.recupererNumclientes(conexion, log);
    	
		cli = cli.recuperarCliente(conexion, log, idCliente);
		
		util = new Util();
		municipios = util.getMunProv(conexion, log, "LOCALIDAD");
		provincias = util.getMunProv(conexion, log, "PROVINCIA");
		
		TextFields.bindAutoCompletion(provincia, 
				provincias);
		
		TextFields.bindAutoCompletion(localidad, 
				municipios);
		
		localidad.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
            	perdidaFoco(localidad);
            }
            else
            {
            	gananciaFoco(localidad);
            }
        });
		
		provincia.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
            	perdidaFoco(provincia);
            }
            else
            {
            	gananciaFoco(provincia);
            }
        });

		pintarDatos(conexion);
		
		modoConsulta();
		
    	conexion.desconectar();
	}
    
    public void setDato(Trazas log)
    {
    	this.log = log;
    }

    @FXML
    void accionCliPrimero(MouseEvent event) {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	
    	idCliente = idPrimerCliente;
    	cli = cli.recuperarCliente(conexion, log, idCliente);
		
		botonesCambioCliente();
		pintarDatos(conexion);

		conexion.desconectar();
    }

    @FXML
    void accionCliAnterior(MouseEvent event)
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	
    	idCliente = cli.recupererAnteriorIdcliente(conexion, log, idCliente);
    	cli = cli.recuperarCliente(conexion, log, idCliente);
		
		botonesCambioCliente();
		pintarDatos(conexion);

		conexion.desconectar();
    }

    @FXML
    void accionCliSiguiente(MouseEvent event)
    {

    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	
    	idCliente = cli.recupererSiguienteIdcliente(conexion, log, idCliente);
    	cli = cli.recuperarCliente(conexion, log, idCliente);
		
		botonesCambioCliente();
		pintarDatos(conexion);

		conexion.desconectar();
    }

    @FXML
    void accionCliUltimo(MouseEvent event)
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	
    	idCliente = idUltimoCliente;
    	cli = cli.recuperarCliente(conexion, log, idCliente);
		
		botonesCambioCliente();
		pintarDatos(conexion);

		conexion.desconectar();
    }

    @FXML
    void accionAddCliente(MouseEvent event)
    {
    	vaciarCampos();
    	modoEdicion();
    }

    @FXML
    void accionEditCliente(MouseEvent event)
    {
    	modoEdicion();
    }
	
    @FXML
    void acccionBorrarCliente(MouseEvent event)
    {		
		try {
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

	    	ConexionBBDD conexion = new ConexionBBDD(log);
	    	conexion.conectar();
	    		    	
	    	String pass = util.recuperarParametro(conexion, log, "PASSWORD");

	    	if(!"".equals(aux))
	    	{
	    		if(pass.equals(aux))
		    	{
		    		log.log(Level.DEBUG, "contraseña correcta");
		    		Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    	    confirmationAlert.setHeaderText(null);
		    	    confirmationAlert.setTitle("Confirmación");
		    	    confirmationAlert.setContentText("Se va a proceder a borrar el cliente ¿Esta Seguro?");
		    	    
		    	    Optional<ButtonType> result = confirmationAlert.showAndWait();
		    	    if (result.isPresent() && result.get() == ButtonType.OK)
		    	    {
		    	    	int numDocumentos = new Documento_BEAN(log).recuperarDocumentosIdCliente(conexion, log, cli.getID_CLIENTE()).size();
		    	    	
		    	    	if(numDocumentos == 0)
		    	    	{
			    	        cli.eliminarCliente(conexion, log);
		    	    	}
		    	    	else
		    	    	{
		    	    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setHeaderText(null);
							alert.setTitle("Informacion");
							alert.setContentText("El cliente tiene documentos por lo que no es posible eliminarlo por completo se procedera a realizar un borrado logico del mismo.");
							alert.showAndWait();
			    	        cli.eliminadoLogicoCliente(conexion, log);
		    	    	}
		    	        
		    	        if(cli.getID_CLIENTE() == idUltimoCliente)
		    	        {
		    	        	idCliente = cli.recuperarIdUltimoCliente(conexion, log);
			    			idUltimoCliente = cli.recuperarIdUltimoCliente(conexion, log);
			    			idPrimerCliente = cli.recupererPrimerIdcliente(conexion, log);
			    			numClientes = cli.recupererNumclientes(conexion, log);
			    			
			    			cli = cli.recuperarCliente(conexion, log, idCliente);
		    	        }
		    	        else if(cli.getID_CLIENTE() == idPrimerCliente)
		    	        {
		    	        	idCliente = cli.recupererPrimerIdcliente(conexion, log);
			    			idUltimoCliente = cli.recuperarIdUltimoCliente(conexion, log);
			    			idPrimerCliente = cli.recupererPrimerIdcliente(conexion, log);
			    			numClientes = cli.recupererNumclientes(conexion, log);
			    			
			    			cli = cli.recuperarCliente(conexion, log, idCliente);
		    	        }
		    	        else
		    	        {
		    	        	idCliente = cli.recupererAnteriorIdcliente(conexion, log, idCliente);
			    			idUltimoCliente = cli.recuperarIdUltimoCliente(conexion, log);
			    			idPrimerCliente = cli.recupererPrimerIdcliente(conexion, log);
			    			numClientes = cli.recupererNumclientes(conexion, log);
			    			
			    	    	cli = cli.recuperarCliente(conexion, log, idCliente);
		    	        }
		    			
		    			botonesCambioCliente();
		    			pintarDatos(conexion);
		    	    }
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

			conexion.desconectar();
			
		} catch (IOException e) {
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
    void accionBuscarCliente(MouseEvent event)
    {
    	try {
    		FXMLLoader loader = new FXMLLoader(Main.class.getResource("vistas/BusquedaClientes_PAN.fxml"));
			
			loader.setControllerFactory(controllerClass -> {
                if (controllerClass == BusquedaClientes_CON.class) {
                	BusquedaClientes_CON seleccion = new BusquedaClientes_CON();
                    seleccion.setDato(log);
                    return seleccion;
                }
                return null;
            });
			
			Parent root = loader.load();
			
			Stage seleccionable = new Stage();
			seleccionable.setTitle("Buscar Cliente");

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
			Cliente_BEAN aux = ((BusquedaClientes_CON)loader.getController()).getDato();
			
			if(aux != null && !"NO CLIENTES".equals(aux.getNOMBRE()))
			{
				idCliente = aux.getID_CLIENTE();
		    	cli = aux;
				
				botonesCambioCliente();
				
				ConexionBBDD conexion = new ConexionBBDD(log);
				conexion.conectar();
				
				pintarDatos(conexion);
				
				conexion.desconectar();
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

    @FXML
    void accionAceptar(MouseEvent event)
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();

		boolean insertar = idCliente != Integer.parseInt(numCliente.getText());
		
    	// validaciones
		boolean correcto = true;
		String error = "";
		
		if(idFiscal.getText().length() == 0 || idFiscal.getText().length() > 9)
		{
			correcto = false;
			error =  "La identificacion fiscal no es correcta";
		}
		
		if(insertar && cli.existeIdFiscal(conexion, log, idFiscal.getText().toUpperCase()))
		{
			correcto = false;
			error =  "Ya hay un cliente con esa identificacion fiscal.";
		}
		
		if(nombre.getText().length() == 0 || nombre.getText().length() > 100)
		{
			correcto = false;
			if(nombre.getText().length() == 0)
				error +=  "\nEl nombre es obligatorio.";
			else
				error +=  "\nEl nombre no puede exceder de 100 caracteres.";
		}
		
		if(cp.getText().length() != 5 || !Util.isNumeric(cp.getText()))
		{
			correcto = false;
			if(cp.getText().length() == 0)
				error +=  "\nEl codigo postal es obligatorio.";
			else if(cp.getText().length() < 5)
				error +=  "\nEl codigo postal se compone de cinco digitos.";
			else if(!Util.isNumeric(cp.getText()))
				error +=  "\nEl codigo postal se debe componer de numeros.";
			
		}
		
		if(municipios.indexOf(localidad.getText()) == -1)
		{
			correcto = false;
			if(localidad.getText().length() > 0)
				error +=  "\nDebe rellenar la localidad con un dato valido.";
			else
				error +=  "\nDebe rellenar la localidad.";
		}
		
		if(provincias.indexOf(provincia.getText()) == -1)
		{
			correcto = false;
			if(provincia.getText().length() > 0)
				error +=  "\nDebe rellenar la localidad con un dato provincia.";
			else
				error +=  "\nDebe rellenar la provincia.";
		}
		
		if(direccion.getText().length() > 100)
		{
			correcto = false;
			error +=  "\nLa direccion no puede exceder de 100 caracteres.";
		}
		
		if(portal.getText().length() > 10)
		{
			correcto = false;
			error +=  "\nEl Portal no puede exceder de 10 caracteres.";
		}
		
		if(!piso.getText().isEmpty() && !Util.isNumeric(piso.getText()))
		{
			correcto = false;
			error +=  "\nEl piso no puede exceder de 10 caracteres.";
		}
		
		if(letra.getText().length() > 10)
		{
			correcto = false;
			error +=  "\nLa letra no puede exceder de 10 caracteres.";
		}
		
		if(!Util.isNumeric(telefono.getText()) || telefono.getText().length() > 9)
		{
			correcto = false;
			if(!Util.isNumeric(telefono.getText()))
				error +=  "\nEl telefono no tiene un formato adecuado.";
			else
				error +=  "\nEl telefono no puede exceder de 9 digitos.";
		}
		
		if(!movil.getText().isEmpty() && (!Util.isNumeric(movil.getText()) || movil.getText().length() > 9))
		{
			correcto = false;
			if(!Util.isNumeric(movil.getText()))
				error +=  "\nEl movil no tiene un formato adecuado.";
			else
				error +=  "\nEl movil no puede exceder de 9 digitos.";
		}
		
		if(mail.getText().length() > 200)
		{
			correcto = false;
			error +=  "\nLa direccion no puede exceder de 200 caracteres.";
		}
		else if(mail.getText().length() > 0 && mail.getText().length() <= 200)
		{
			Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
			String email = mail.getText();
			
			Matcher mather = pattern.matcher(email);
			 
	        if (!mather.find()) {
	        	correcto = false;
				error +=  "\nEl email no es correcto.";
	        }
		}
		
		if(correcto)
		{
	    	cli.setID_CLIENTE(Integer.parseInt(numCliente.getText()));
			cli.setIDFISCAL(idFiscal.getText().toUpperCase());
			cli.setNOMBRE(nombre.getText());
			cli.setDIRECCION(direccion.getText());
			cli.setCODIGOPOSTAL(cp.getText());
			cli.setLOCALIDAD(localidad.getText());
			cli.setPROVINCIA(provincia.getText());
			cli.setPORTAL(portal.getText());
			cli.setPISO(!piso.getText().equals("")?Integer.parseInt(piso.getText()):null );
			cli.setLETRA(letra.getText());
			cli.setTELEFONO(Integer.parseInt(telefono.getText()));
			cli.setMOVIL(!movil.getText().equals("")?Integer.parseInt(movil.getText()):null );
			cli.setEMAIL(mail.getText());
			
			if(insertar)
	    	{
				cli.insertarCliente(conexion, log);
				
				idCliente = cli.getID_CLIENTE();
				numClientes = cli.recupererNumclientes(conexion, log);
				idUltimoCliente = cli.recuperarIdUltimoCliente(conexion, log);
	    	}
	    	else
	    	{
	    		cli.actualizarCliente(conexion,  log);
	    	}
	    	pintarDatos(conexion);
	    	modoConsulta();
		}
		else
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText(error);
			alert.showAndWait();
		}
		
		conexion.desconectar();
    }

    @FXML
    void accionCancelar(MouseEvent event)
    {
		ConexionBBDD conexion = new ConexionBBDD(log);
		conexion.conectar();
		
		pintarDatos(conexion);
    	modoConsulta();
		
		conexion.desconectar();
    }

    @FXML
    void accionCambiarCampo(KeyEvent event)
    {
    	if (event.getCode() == KeyCode.ENTER || (event.getCode() == KeyCode.TAB && !event.isShiftDown()))
    	{
    		event.consume();
            JFXTextField textField = (JFXTextField) event.getTarget();
            String fuente = textField.getId();
            ConexionBBDD conexion;
            
            switch (fuente) {
                case "idFiscal":
                	conexion = new ConexionBBDD(log);;
                	conexion.conectar();
                	boolean insertar = idCliente != Integer.parseInt(numCliente.getText());
					if(insertar && cli.existeIdFiscal(conexion, log, idFiscal.getText().toUpperCase()))
					{
						idFiscal.requestFocus();
						
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setHeaderText(null);
						alert.setTitle("Error");
						alert.setContentText("Ya hay un cliente con esa identificacion fiscal.");
						alert.showAndWait();
					}
					else
					{
						nombre.requestFocus();
					}
					conexion.desconectar();
                    break;
                case "nombre":
                	cp.requestFocus();
                    break;
                case "cp":
                	if(cp.getText().length() > 0)
            		{
                		conexion = new ConexionBBDD(log);
                		conexion.conectar();
                		boolean correcto = true;
                		String error = "";
            			if(cp.getText().length() == 0)
            			{
            				error +=  "\nEl codigo postal es obligatorio.";
            				correcto = false;
            			}
            			else if(cp.getText().length() < 5)
            			{
            				error +=  "\nEl codigo postal se compone de cindo digitos.";
            				correcto = false;
            			}
            			else if(!Util.isNumeric(cp.getText()))
            			{
            				error +=  "\nEl codigo postal se debe componer de numeros.";
            				correcto = false;
            			}
                		
            			if(correcto)
            			{
            				ArrayList<String[]> cpMunProv = util.recuperarMunicipioProvincia(conexion, log, cp.getText());
                    		
                    		if(cpMunProv.size() > 0)
                    		{
                    			localidad.setText(cpMunProv.get(0)[0]);
                    			provincia.setText(cpMunProv.get(0)[1]);
                    			direccion.requestFocus();
                    		}
                    		else
                    		{
                    			Alert alert = new Alert(Alert.AlertType.ERROR);
        						alert.setHeaderText(null);
        						alert.setTitle("Error");
        						alert.setContentText("El codigo postal no es correcto");
        						alert.showAndWait();
        						cp.requestFocus();
                    		}
            			}
            			else
            			{
            				Alert alert = new Alert(Alert.AlertType.ERROR);
    						alert.setHeaderText(null);
    						alert.setTitle("Error");
    						alert.setContentText(error);
    						alert.showAndWait();
    						cp.requestFocus();
            			}
            			conexion.desconectar();
            		}
                	else
                	{
                		Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setHeaderText(null);
						alert.setTitle("Error");
						alert.setContentText("El codigo postal es obligatorio.");
						alert.showAndWait();
                	}
                	
                    break;
                case "localidad":
                	
                	if(localidad.getText().length() > 0)
            		{
                		boolean correcto = true;
            			String error = "";

            			if(municipios.indexOf(localidad.getText()) == -1)
                		{
                			correcto = false;
                			if(localidad.getText().length() > 0)
                				error +=  "\nDebe rellenar la localidad con un dato valido.";
                			else
                				error +=  "\nDebe rellenar la localidad.";
                		}
                		
                		if(correcto)
                		{
                			direccion.requestFocus();                			
                		}
                		else
                		{
                			Alert alert = new Alert(Alert.AlertType.ERROR);
    						alert.setHeaderText(null);
    						alert.setTitle("Error");
    						alert.setContentText(error);
    						alert.showAndWait();
                		}
                		
            		}
                    break;
                case "provincia":
                	if(provincia.getText().length() > 0)
            		{
                		boolean correcto = true;
            			String error = "";

            			if(provincias.indexOf(provincia.getText()) == -1)
                		{
                			correcto = false;
                			if(localidad.getText().length() > 0)
                				error +=  "\nDebe rellenar la provincia con un dato valido.";
                			else
                				error +=  "\nDebe rellenar la provincia.";
                		}
                		
                		if(correcto)
                		{
                			direccion.requestFocus();                			
                		}
                		else
                		{
                			Alert alert = new Alert(Alert.AlertType.ERROR);
    						alert.setHeaderText(null);
    						alert.setTitle("Error");
    						alert.setContentText(error);
    						alert.showAndWait();
                		}
                		
            		}
                    break;
                case "direccion":
                	portal.requestFocus();
                    break;
                case "portal":
                	piso.requestFocus();
                    break;
                case "piso":
                	letra.requestFocus();
                    break;
                case "letra":
                	telefono.requestFocus();
                    break;
                case "telefono":
                	movil.requestFocus();
                    break;
                case "movil":
                	mail.requestFocus();
                    break;
                case "mail":
                	idFiscal.requestFocus();
                    break;
            }
        }
    	else if (event.getCode() == KeyCode.TAB && event.isShiftDown())
    	{
    		event.consume();
            JFXTextField textField = (JFXTextField) event.getTarget();
            String fuente = textField.getId();
            ConexionBBDD conexion;
            
            switch (fuente) {
                case "idFiscal":
                	conexion = new ConexionBBDD(log);;
                	conexion.conectar();
					boolean insertar = idCliente != cli.getID_CLIENTE();
					if(insertar && cli.existeIdFiscal(conexion, log, idFiscal.getText().toUpperCase()))
					{
						idFiscal.requestFocus();
						
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setHeaderText(null);
						alert.setTitle("Error");
						alert.setContentText("Ya hay un cliente con esa identificacion fiscal.");
						alert.showAndWait();
					}
					else
					{
						mail.requestFocus();
					}
					conexion.desconectar();
                    break;
                case "nombre":
                	idFiscal.requestFocus();
                    break;
                case "cp":
                	if(cp.getText().length() > 0)
            		{
                		conexion = new ConexionBBDD(log);
                		conexion.conectar();
                		boolean correcto = true;
                		String error = "";
            			if(cp.getText().length() == 0)
            			{
            				error +=  "\nEl codigo postal es obligatorio.";
            				correcto = false;
            			}
            			else if(cp.getText().length() < 5)
            			{
            				error +=  "\nEl codigo postal se compone de cindo digitos.";
            				correcto = false;
            			}
            			else if(!Util.isNumeric(cp.getText()))
            			{
            				error +=  "\nEl codigo postal se debe componer de numeros.";
            				correcto = false;
            			}
                		
            			if(correcto)
            			{
            				ArrayList<String[]> cpMunProv = util.recuperarMunicipioProvincia(conexion, log, cp.getText());
                    		
                    		if(cpMunProv.size() > 0)
                    		{
                    			localidad.setText(cpMunProv.get(0)[0]);
                    			provincia.setText(cpMunProv.get(0)[1]);
                    			nombre.requestFocus();
                    		}
                    		else
                    		{
                    			Alert alert = new Alert(Alert.AlertType.ERROR);
        						alert.setHeaderText(null);
        						alert.setTitle("Error");
        						alert.setContentText("El codigo postal no es correcto");
        						alert.showAndWait();
        						cp.requestFocus();
                    		}
            			}
            			else
            			{
            				Alert alert = new Alert(Alert.AlertType.ERROR);
    						alert.setHeaderText(null);
    						alert.setTitle("Error");
    						alert.setContentText(error);
    						alert.showAndWait();
    						cp.requestFocus();
            			}
            			conexion.desconectar();
            		}
                	else
                	{
                		Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setHeaderText(null);
						alert.setTitle("Error");
						alert.setContentText("El codigo postal es obligatorio.");
						alert.showAndWait();
                	}
                	
                    break;
                case "localidad":
                	
                	if(localidad.getText().length() > 0)
            		{
                		boolean correcto = true;
            			String error = "";

            			if(municipios.indexOf(localidad.getText()) == -1)
                		{
                			correcto = false;
                			if(localidad.getText().length() > 0)
                				error +=  "\nDebe rellenar la localidad con un dato valido.";
                			else
                				error +=  "\nDebe rellenar la localidad.";
                		}
                		
                		if(correcto)
                		{
                			nombre.requestFocus();                			
                		}
                		else
                		{
                			Alert alert = new Alert(Alert.AlertType.ERROR);
    						alert.setHeaderText(null);
    						alert.setTitle("Error");
    						alert.setContentText(error);
    						alert.showAndWait();
                		}
                		
            		}
                    break;
                case "provincia":
                	if(provincia.getText().length() > 0)
            		{
                		boolean correcto = true;
            			String error = "";

            			if(provincias.indexOf(provincia.getText()) == -1)
                		{
                			correcto = false;
                			if(localidad.getText().length() > 0)
                				error +=  "\nDebe rellenar la provincia con un dato valido.";
                			else
                				error +=  "\nDebe rellenar la provincia.";
                		}
                		
                		if(correcto)
                		{
                			nombre.requestFocus();                			
                		}
                		else
                		{
                			Alert alert = new Alert(Alert.AlertType.ERROR);
    						alert.setHeaderText(null);
    						alert.setTitle("Error");
    						alert.setContentText(error);
    						alert.showAndWait();
                		}
                		
            		}
                    break;
                case "direccion":
                	cp.requestFocus();
                    break;
                case "portal":
                	direccion.requestFocus();
                    break;
                case "piso":
                	portal.requestFocus();
                    break;
                case "letra":
                	piso.requestFocus();
                    break;
                case "telefono":
                	letra.requestFocus();
                    break;
                case "movil":
                	telefono.requestFocus();
                    break;
                case "mail":
                	movil.requestFocus();
                    break;
            }
        
    	}
    }
    
    private String localidadOri = "";
    private String provinciaOri = "";
    
    private void gananciaFoco(JFXTextField textField)
    {
    	if(textField == localidad)
    	{
    		localidadOri = textField.getText();
    	}
    	else if (textField == provincia)
    	{
    		provinciaOri = textField.getText();
    	}
    }

    private void perdidaFoco(JFXTextField textField)
    {
    	if(textField == localidad && !localidadOri.equals(localidad.getText()))
    	{
    		ConexionBBDD conexion = new ConexionBBDD(log);
    		conexion.conectar();
    		
    		ArrayList<String[]> cpMunProv = util.recuperarCpProvincia(conexion, log, localidad.getText());
    		
    		if(cpMunProv.size() == 1)
    		{
    			provincia.setText(cpMunProv.get(0)[1]);
    			cp.setText(cpMunProv.get(0)[2]);
    			direccion.requestFocus();
    		}
    		else if(cpMunProv.size() > 1)
    		{
    			
    			Parent root;
				try {
        			FXMLLoader loader = new FXMLLoader(Main.class.getResource("vistas/Seleccion_PAN.fxml"));
        			
        			loader.setControllerFactory(controllerClass -> {
                        if (controllerClass == Seleccion_CON.class) {
                        	Seleccion_CON seleccion = new Seleccion_CON();
                            seleccion.setDato(log, cpMunProv);
                            return seleccion;
                        }
                        return null;
                    });
        			
					root = loader.load();
					
        			Stage seleccionable = new Stage();
        			seleccionable.setTitle("Selecciona...");

        			Image icon = new Image(Main.class.getResource("img/car.png").toExternalForm());
        			seleccionable.getIcons().add(icon);

//        			seleccionable.setScene(new Scene(root, 300, 400));
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
        			String[] aux = ((Seleccion_CON)loader.getController()).getDato();
        			
        			provincia.setText(aux[1]);
        			cp.setText(aux[2]);
        			direccion.requestFocus();
				} catch (IOException e) {
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
				alert.setContentText("No hay provincias ni codigos postales validos para esta localidad.");
				alert.showAndWait();
    		}
    		
    		conexion.desconectar();
		
    	}
    	else if (textField == provincia && !provinciaOri.equals(provincia.getText()))
    	{
    		ConexionBBDD conexion = new ConexionBBDD(log);
    		conexion.conectar();
    		
    		ArrayList<String[]> cpMunProv = util.recuperarCpLocalidad(conexion, log, provincia.getText());
    		
    		if(cpMunProv.size() == 1)
    		{
    			localidad.setText(cpMunProv.get(0)[0]);
    			cp.setText(cpMunProv.get(0)[2]);
    			direccion.requestFocus();
    		}
    		else if(cpMunProv.size() > 1)
    		{
    			
    			Parent root;
				try {
        			FXMLLoader loader = new FXMLLoader(Main.class.getResource("vistas/Seleccion_PAN.fxml"));
        			
        			loader.setControllerFactory(controllerClass -> {
                        if (controllerClass == Seleccion_CON.class) {
                        	Seleccion_CON seleccion = new Seleccion_CON();
                            seleccion.setDato(log, cpMunProv);
                            return seleccion;
                        }
                        return null;
                    });
        			
					root = loader.load();
					
        			Stage seleccionable = new Stage();
        			seleccionable.setTitle("Selecciona...");

        			Image icon = new Image(Main.class.getResource("img/car.png").toExternalForm());
        			seleccionable.getIcons().add(icon);
        			
//        			seleccionable.setScene(new Scene(root, 300, 400));
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
        			String[] aux = ((Seleccion_CON)loader.getController()).getDato();
        			
        			localidad.setText(aux[0]);
        			cp.setText(aux[2]);
        			direccion.requestFocus();
				} catch (IOException e) {
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
				alert.setContentText("No hay localidades ni codigos postales validos para esta provincia.");
				alert.showAndWait();
    		}
    		
    		conexion.desconectar();
    	}
    	
    }

    @FXML
    void accionNotas(MouseEvent event)
    {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zamora/vistas/Notas_PAN.fxml"));
            
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == Notas_CON.class) {
                	Notas_CON seleccion = new Notas_CON();
                    seleccion.setDato(log, idCliente);
                    return seleccion;
                }
                return null;
            });
            
            Parent root = loader.load();

            Stage ventanaStage = new Stage();
            ventanaStage.setTitle("Notas");
            ventanaStage.setScene(new Scene(root, 841, 439));
            
            Image icon = new Image(getClass().getResourceAsStream("/com/zamora/img/POSTIT.png"));
            ventanaStage.getIcons().add(icon);
            
			// Configura la ventana secundaria como modal
            ventanaStage.initModality(Modality.APPLICATION_MODAL);

            ventanaStage.setResizable(false);

            ventanaStage.showAndWait();
            
            ConexionBBDD conexion = new ConexionBBDD(log);
            conexion.conectar();
            pintarDatos(conexion);
            conexion.desconectar();
            
        } catch (Exception e) {
            e.printStackTrace();
			log.log(Level.ERROR, e);
        }
    }

    @FXML
    void accionVehiculos(MouseEvent event)
    {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zamora/vistas/Vehiculos_PAN.fxml"));
            
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == Vehiculos_CON.class) {
                	Vehiculos_CON seleccion = new Vehiculos_CON();
                    seleccion.setDato(log, idCliente);
                    return seleccion;
                }
                return null;
            });
            
            Parent root = loader.load();

            Stage ventanaStage = new Stage();
            ventanaStage.setTitle("Vehiculos");
            ventanaStage.setScene(new Scene(root, 700, 500));
            
            Image icon = new Image(getClass().getResourceAsStream("/com/zamora/img/car.png"));
            ventanaStage.getIcons().add(icon);

            
			// Configura la ventana secundaria como modal
            ventanaStage.initModality(Modality.APPLICATION_MODAL);

            ventanaStage.setResizable(false);

            ventanaStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
			log.log(Level.ERROR, e);
        }
    }

    @FXML
    void accionBuscarDocumentos(MouseEvent event)
    {
    	try
    	{
    		FXMLLoader loader = new FXMLLoader(Main.class.getResource("vistas/TipoDocumento_PAN.fxml"));
			
			loader.setControllerFactory(controllerClass -> {
                if (controllerClass == TipoDocumento_CON.class) {
                	TipoDocumento_CON seleccion = new TipoDocumento_CON();
                    seleccion.setDato(log, idCliente);
                    return seleccion;
                }
                return null;
            });
			
			Parent root = loader.load();
			
			Stage seleccionable = new Stage();
			seleccionable.setTitle("Tipo Documento");

			Image icon = new Image(Main.class.getResource("img/car.png").toExternalForm());
			seleccionable.getIcons().add(icon);

			seleccionable.setScene(new Scene(root));

			// Configura la ventana secundaria como modal
			seleccionable.initModality(Modality.APPLICATION_MODAL);

			// Configura el evento para establecer el tamaño mínimo
			seleccionable.setResizable(false);
			seleccionable.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                	seleccionable.setMinWidth(seleccionable.getWidth());
                	seleccionable.setMinHeight(seleccionable.getHeight());
                }
            });

			seleccionable.showAndWait();
			Documento_BEAN seleccionado = ((TipoDocumento_CON)loader.getController()).getDato();
			
			if(seleccionado != null)
			{
				FXMLLoader loaderDocumento = new FXMLLoader(getClass().getResource("/com/zamora/vistas/Documentos_PAN.fxml"));
                
				loaderDocumento.setControllerFactory(controllerClass -> {
	                if (controllerClass == Documentos_CON.class) {
	                	Documentos_CON documento = new Documentos_CON();
	                	documento.setDato(log, seleccionado.getTipo(), seleccionado);
	                    return documento;
	                }
	                return null;
	            });

	            Parent rootDocumento = loaderDocumento.load();

	            Stage presupuesto = new Stage();
	            presupuesto.setTitle(seleccionado.getTipo().equals("F")?"Facturas":"Presupuesto");
	            presupuesto.setScene(new Scene(rootDocumento, 967, 540));
	            
	            Image iconDocumento = null;
	            if(seleccionado.getTipo().equals("F"))
	            	iconDocumento = new Image(getClass().getResourceAsStream("/com/zamora/img/factura.png"));
	            else
	            	iconDocumento = new Image(getClass().getResourceAsStream("/com/zamora/img/presupuesto.png"));
	            presupuesto.getIcons().add(iconDocumento);
	            
				// Configura la ventana secundaria como modal
	            presupuesto.initModality(Modality.APPLICATION_MODAL);
	            
	            // Configura el evento para establecer el tamaño mínimo
	            presupuesto.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
	                 @Override
	                 public void handle(WindowEvent event) {
	                	 presupuesto.setMinWidth(presupuesto.getWidth());
	                	 presupuesto.setMinHeight(presupuesto.getHeight());
	                 }
	             });

	            presupuesto.show();
			}
			
        } catch (Exception e) {
            e.printStackTrace();
			log.log(Level.ERROR, e);
        }
    }
    
    public void botonesCambioCliente()
    {
    	if(numClientes == 0 || numClientes == 1)
		{
			btnPrimero.setDisable(true);
			btnAnterior.setDisable(true);
			btnSiguiente.setDisable(true);
			btnUltimo.setDisable(true);
		}
		else if (numClientes > 1)
		{
			if(idCliente == idPrimerCliente)
			{
				btnPrimero.setDisable(true);
				btnAnterior.setDisable(true);
				btnSiguiente.setDisable(false);
				btnUltimo.setDisable(false);
			}
			else if (idCliente == idUltimoCliente)
			{
				btnPrimero.setDisable(false);
				btnAnterior.setDisable(false);
				btnSiguiente.setDisable(true);
				btnUltimo.setDisable(true);
			}
			else
			{
				btnPrimero.setDisable(false);
				btnAnterior.setDisable(false);
				btnSiguiente.setDisable(false);
				btnUltimo.setDisable(false);
			}
		}
    }
    
    public void pintarDatos(ConexionBBDD conexion)
    {
    	numCliente.setText(String.valueOf(cli.getID_CLIENTE()));
    	idFiscal.setText(cli.getIDFISCAL());
    	nombre.setText(cli.getNOMBRE());
    	cp.setText(cli.getCODIGOPOSTAL());
    	localidad.setText(cli.getLOCALIDAD());
    	provincia.setText(cli.getPROVINCIA());
    	direccion.setText(cli.getDIRECCION());
    	portal.setText(cli.getPORTAL());
    	piso.setText(String.valueOf(cli.getPISO() != null?cli.getPISO():""));
    	letra.setText(cli.getLETRA());
    	telefono.setText(String.valueOf( cli.getTELEFONO() != null?cli.getTELEFONO():"" ));
    	movil.setText(String.valueOf(cli.getMOVIL() != null?cli.getMOVIL():""));
    	mail.setText(cli.getEMAIL());
    	
    	String sNotaImportante = "";
    	String sNotaPrincipal = "";
    	boolean tieneDeuda = false;
    	boolean tieneNotaImp = false;
		if(cli != null)
		{
			sNotaImportante = new Nota_BEAN().clienteConNotaImportante(conexion, log, cli.getID_CLIENTE());
			sNotaPrincipal = new Nota_BEAN().clienteConNotaPrincipal(conexion, log, cli.getID_CLIENTE());
			tieneDeuda = cli.recuperarDeudaCliente(conexion, log, cli.getID_CLIENTE()) > 0;
			tieneNotaImp = sNotaImportante.length() > 0;
		}
    	
    	Image imageNotaImportante;
    	Tooltip tooltip = new Tooltip();
    	if(tieneNotaImp)
    	{
    		tooltip.setText(sNotaImportante);
        	Tooltip.install(imgImportante, tooltip);
    		imageNotaImportante = new Image(Main.class.getResource("img/rojo.png").toExternalForm());
    	}
    	else
    	{
        	Tooltip.uninstall(imgImportante, tooltip);
    		imageNotaImportante = new Image(Main.class.getResource("img/verde.png").toExternalForm());
    	}
    	

    	Image imageDeuda;
    	if(tieneDeuda)
    	{
    		imageDeuda = new Image(Main.class.getResource("img/freeikon-43.png").toExternalForm());
    	}
    	else
    	{
    		imageDeuda = new Image(Main.class.getResource("img/freeikon-44.png").toExternalForm());
    	}
    	
    	deuda.setImage(imageDeuda);
    	imgImportante.setImage(imageNotaImportante);
    	notaPrincipal.setText(sNotaPrincipal);
    	
    }
    
    public void vaciarCampos()
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	
    	numCliente.setText(String.valueOf(cli.recuperarIdNextCliente(conexion, log)));
    	idFiscal.setText("");
    	nombre.setText("");
    	cp.setText("");
    	localidad.setText("");
    	provincia.setText("");
    	direccion.setText("");
    	portal.setText("");
    	piso.setText("");
    	letra.setText("");
    	telefono.setText("");
    	movil.setText("");
    	mail.setText("");
    	notaPrincipal.setText("");

		conexion.desconectar();
    }

    @FXML
    void accionDeuda(MouseEvent event) {
		
		try {
			ConexionBBDD db = new ConexionBBDD(log);
			db.conectar();
			double deudaCantidad = cli.recuperarDeudaCliente(db, log, cli.getID_CLIENTE());
			db.desconectar();
	    	FXMLLoader loader = new FXMLLoader(Main.class.getResource("vistas/PedirDato_PAN.fxml"));
			
			loader.setControllerFactory(controllerClass -> {
	            if (controllerClass == PedirDato_CON.class) {
	            	PedirDato_CON pass = new PedirDato_CON();
	                pass.setDato(log, new Double(deudaCantidad));
	                return pass;
	            }
	            return null;
	        });
			
			Parent root = loader.load();
			
			Stage seleccionable = new Stage();
			seleccionable.setTitle("Cantidad de la deuda.");

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
			
			AtomicBoolean cerraronSinInsertar = new AtomicBoolean(false);
			seleccionable.setOnCloseRequest(evento -> {
				cerraronSinInsertar.set(true);
			});

			seleccionable.showAndWait(); // Muestra la ventana y espera hasta que se cierre
			Double aux = (double) ((PedirDato_CON)loader.getController()).getDato();
			
			if(aux != null && !cerraronSinInsertar.get())
			{
				db.conectar();
				cli.actualizarDeudaCliente(db, log, cli.getID_CLIENTE(), aux);
				db.desconectar();
				
				Image imageDeuda;
		    	if(aux > 0)
		    	{
		    		imageDeuda = new Image(Main.class.getResource("img/freeikon-43.png").toExternalForm());
		    	}
		    	else
		    	{
		    		imageDeuda = new Image(Main.class.getResource("img/freeikon-44.png").toExternalForm());
		    	}
		    	
		    	deuda.setImage(imageDeuda);
				
			}			
		} catch (IOException e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
    }
    
    public void modoConsulta()
    {
    	idFiscal.setDisable(true);
    	nombre.setDisable(true);
    	cp.setDisable(true);
    	localidad.setDisable(true);
    	provincia.setDisable(true);
    	direccion.setDisable(true);
    	portal.setDisable(true);
    	piso.setDisable(true);
    	letra.setDisable(true);
    	telefono.setDisable(true);
    	movil.setDisable(true);
    	mail.setDisable(true);
    	btnAceptar.setDisable(true);
    	btnCancelar.setDisable(true);
		
		btnAgnadirCliente.setDisable(false);
		btnEditarCliente.setDisable(false);
		btnBorrarCliente.setDisable(false);
		btnBuscarCliente.setDisable(false);
		
		btnVehiculos.setDisable(false);
		btnNotas.setDisable(false);
		btnBuscarDocumento.setDisable(false);
		
		btnDeuda.setDisable(false);
    	
    	botonesCambioCliente();
    }
    
    public void modoEdicion()
    {
    	idFiscal.setDisable(false);
    	nombre.setDisable(false);
    	cp.setDisable(false);
    	localidad.setDisable(false);
    	provincia.setDisable(false);
    	direccion.setDisable(false);
    	portal.setDisable(false);
    	piso.setDisable(false);
    	letra.setDisable(false);
    	telefono.setDisable(false);
    	movil.setDisable(false);
    	mail.setDisable(false);
    	btnAceptar.setDisable(false);
    	btnCancelar.setDisable(false);
    	
    	btnPrimero.setDisable(true);
		btnAnterior.setDisable(true);
		btnSiguiente.setDisable(true);
		btnUltimo.setDisable(true);
		
		btnAgnadirCliente.setDisable(true);
		btnEditarCliente.setDisable(true);
		btnBorrarCliente.setDisable(true);
		btnBuscarCliente.setDisable(true);
		
		btnVehiculos.setDisable(true);
		btnNotas.setDisable(true);
		btnBuscarDocumento.setDisable(true);
		
		btnDeuda.setDisable(true);
    }
}
