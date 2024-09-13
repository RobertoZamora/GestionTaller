package com.zamora.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.log4j.Level;

import com.zamora.Main;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.Empresa_BEAN;
import com.zamora.trazas.Trazas;
import com.zamora.util.Util;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Principal_CON
{
	@FXML
	private Button btnClientes;
	@FXML
	private ImageView imgCliente;
	@FXML
	private Button btnPresupuesto;
	@FXML
	private ImageView imgPresupuesto;
	@FXML
	private Button btnFacturas;
	@FXML
	private ImageView imgFactura;
	@FXML
	private Button btnEmpresas;
	@FXML
	private ImageView imgEmpresa;
	@FXML
	private Button btnConfiguracion;
	@FXML
	private ImageView imgConfig;
	@FXML
	private ImageView fondoCar;
	
	private Trazas log;
	private Util util;

	@FXML
    void initialize()
	{
		Tooltip tooltipClientes = new Tooltip("Clientes");
		Tooltip tooltipPresupue = new Tooltip("Presupuestos");
		Tooltip tooltipFacturas = new Tooltip("Facturas");
		Tooltip tooltipEmpresas = new Tooltip("Empresas");
		Tooltip tooltipConfigur = new Tooltip("Configuración");
		btnClientes.setTooltip(tooltipClientes);
		btnPresupuesto.setTooltip(tooltipPresupue);
		btnFacturas.setTooltip(tooltipFacturas);
		btnEmpresas.setTooltip(tooltipEmpresas);
		btnConfiguracion.setTooltip(tooltipConfigur);
		

		util = new Util();
		try {
			util.eliminarDirectorio(Paths.get(Main.rutaDatos + File.separator + "tmp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    public void setDato(Trazas log)
    {
    	this.log = log;
    }
	
    @FXML
	public void abrirClientes(MouseEvent event)
    {
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Clientes_PAN.fxml"));
                        
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == Clientes_CON.class) {
                    Clientes_CON cliente = new Clientes_CON();
                    cliente.setDato(log);
                    return cliente;
                }
                return null;
            });

            Parent root = loader.load();

            Stage clientes = new Stage();
            clientes.setTitle("Clientes");
            clientes.setScene(new Scene(root, 967, 540));
            
            Image icon = new Image(getClass().getResourceAsStream("/img/cliente.png"));
            clientes.getIcons().add(icon);
            
			// Configura la ventana secundaria como modal
            clientes.initModality(Modality.APPLICATION_MODAL);
            
            // Configura el evento para establecer el tamaño mínimo
            clientes.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
                 @Override
                 public void handle(WindowEvent event) {
                	 clientes.setMinWidth(clientes.getWidth());
                	 clientes.setMinHeight(clientes.getHeight());
                 }
             });

            clientes.show();
        } catch (Exception e) {
            e.printStackTrace();
			log.log(Level.ERROR, e);
        }

	}

	@FXML
	public void abrirPresupuestos(MouseEvent event)
	{
		try 
		{		
			ConexionBBDD db = new ConexionBBDD(log);
			db.conectar();
			Empresa_BEAN emrpesa = new Empresa_BEAN().recuperarDatosEmpresaPredefinida(db, log);
			db.desconectar();
			
			if(emrpesa != null)
			{
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Documentos_PAN.fxml"));
                
	            loader.setControllerFactory(controllerClass -> {
	                if (controllerClass == Documentos_CON.class) {
	                	Documentos_CON documento = new Documentos_CON();
	                	documento.setDato(log, "P", null);
	                    return documento;
	                }
	                return null;
	            });

	            Parent root = loader.load();

	            Stage presupuesto = new Stage();
	            presupuesto.setTitle("Presupuestos");
	            presupuesto.setScene(new Scene(root, 967, 540));
	            
	            Image icon = new Image(getClass().getResourceAsStream("/img/presupuesto.png"));
	            presupuesto.getIcons().add(icon);
	            
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
			else
			{
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setHeaderText(null);
    			alert.setTitle("Error");
    			alert.setContentText("No hay empresa configurada.");
    			alert.showAndWait();
			}
        } catch (Exception e) {
            e.printStackTrace();
			log.log(Level.ERROR, e);
        }
	}
	
	@FXML
	public void abrirFacturas(MouseEvent event)
	{
		try {
			ConexionBBDD db = new ConexionBBDD(log);
			db.conectar();
			Empresa_BEAN emrpesa = new Empresa_BEAN().recuperarDatosEmpresaPredefinida(db, log);
			db.desconectar();
			
			if(emrpesa != null)
			{
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Documentos_PAN.fxml"));
                
	            loader.setControllerFactory(controllerClass -> {
	                if (controllerClass == Documentos_CON.class) {
	                	Documentos_CON documento = new Documentos_CON();
	                	documento.setDato(log, "F", null);
	                    return documento;
	                }
	                return null;
	            });

	            Parent root = loader.load();

	            Stage facturas = new Stage();
	            facturas.setTitle("Facturas");
	            facturas.setScene(new Scene(root, 967, 540));
	            
	            Image icon = new Image(getClass().getResourceAsStream("/img/factura.png"));
	            facturas.getIcons().add(icon);
	            
				// Configura la ventana secundaria como modal
	            facturas.initModality(Modality.APPLICATION_MODAL);
	            
	            // Configura el evento para establecer el tamaño mínimo
	            facturas.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
	                 @Override
	                 public void handle(WindowEvent event) {
	                	 facturas.setMinWidth(facturas.getWidth());
	                	 facturas.setMinHeight(facturas.getHeight());
	                 }
	             });

	            facturas.show();
			}
			else
			{
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setHeaderText(null);
    			alert.setTitle("Error");
    			alert.setContentText("No hay empresa configurada.");
    			alert.showAndWait();
			}
			
        } catch (Exception e) {
            e.printStackTrace();
			log.log(Level.ERROR, e);
        }
	}
	
	@FXML
	public void abrirEmpresas(MouseEvent event)
	{
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/Empresa_PAN.fxml"));
			
			loader.setControllerFactory(controllerClass -> {
                if (controllerClass == Empresa_CON.class) {
                	Empresa_CON cliente = new Empresa_CON();
                    cliente.setDato(log);
                    return cliente;
                }
                return null;
            });
			
			Parent root = loader.load();

			Stage Empresas = new Stage();
			Empresas.setTitle("Empresa");

			Image icon = new Image(Main.class.getResource("/img/car.png").toExternalForm());
			Empresas.getIcons().add(icon);
			
			Empresas.setScene(new Scene(root, 382, 483));

			// Configura la ventana secundaria como modal
			Empresas.initModality(Modality.APPLICATION_MODAL);

			// Configura el evento para establecer el tamaño mínimo
			Empresas.setResizable(false);
			Empresas.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                	Empresas.setMinWidth(Empresas.getWidth());
                	Empresas.setMinHeight(Empresas.getHeight());
                }
            });

			Empresas.showAndWait(); // Muestra la ventana y espera hasta que se cierre
		} catch (Exception e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
	}
	
	@FXML
	public void abrirConfiguracion(MouseEvent event)
	{
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/Configuracion_PAN.fxml"));
			
			loader.setControllerFactory(controllerClass -> {
                if (controllerClass == Configuracion_CON.class) {
                	Configuracion_CON cliente = new Configuracion_CON();
                    cliente.setDato(log);
                    return cliente;
                }
                return null;
            });
			
			Parent root = loader.load();

			Stage configuracion = new Stage();
			configuracion.setTitle("Configuracion");

			Image icon = new Image(Main.class.getResource("/img/engranage.png").toExternalForm());
			configuracion.getIcons().add(icon);
			
			configuracion.setScene(new Scene(root));

			// Configura la ventana secundaria como modal
			configuracion.initModality(Modality.APPLICATION_MODAL);

			// Configura el evento para establecer el tamaño mínimo
			configuracion.setResizable(false);
			configuracion.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                	configuracion.setMinWidth(configuracion.getWidth());
                	configuracion.setMinHeight(configuracion.getHeight());
                }
            });

			configuracion.showAndWait(); // Muestra la ventana y espera hasta que se cierre
		} catch (Exception e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
	}
}
