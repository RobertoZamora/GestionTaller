package com.zamora.controller;

import com.jfoenix.controls.JFXButton;
import com.zamora.Main;
import com.zamora.modelo.Documento_BEAN;
import com.zamora.trazas.Trazas;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Level;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TipoDocumento_CON
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnFacturas;

    @FXML
    private JFXButton btnPresupuestos;
    private Trazas log;
    private int idCliente;
    private Documento_BEAN seleccionado;

    @FXML
    void facturas(MouseEvent event)
    {

    	try
    	{
    		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/DocumentosCliente_PAN.fxml"));
			
			loader.setControllerFactory(controllerClass -> {
                if (controllerClass == DocumentosCliente_CON.class) {
                	DocumentosCliente_CON seleccion = new DocumentosCliente_CON();
                    seleccion.setDato(log, "F", idCliente);
                    return seleccion;
                }
                return null;
            });
			
			Parent root = loader.load();
			
			Stage seleccionable = new Stage();
			seleccionable.setTitle("Facturas");

			Image icon = new Image(Main.class.getResource("/img/factura.png").toExternalForm());
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

			seleccionable.showAndWait();
			seleccionado = ((DocumentosCliente_CON)loader.getController()).getDato();
			
			Stage stage = (Stage) btnFacturas.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
			log.log(Level.ERROR, e);
        }
    }

    @FXML
    void presupuestos(MouseEvent event)
    {
    	try
    	{
    		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/DocumentosCliente_PAN.fxml"));
			
			loader.setControllerFactory(controllerClass -> {
                if (controllerClass == DocumentosCliente_CON.class) {
                	DocumentosCliente_CON seleccion = new DocumentosCliente_CON();
                    seleccion.setDato(log, "P", idCliente);
                    return seleccion;
                }
                return null;
            });
			
			Parent root = loader.load();
			
			Stage seleccionable = new Stage();
			seleccionable.setTitle("Presupuestos");

			Image icon = new Image(Main.class.getResource("/img/presupuesto.png").toExternalForm());
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

			seleccionable.showAndWait();
			seleccionado = ((DocumentosCliente_CON)loader.getController()).getDato();
			
			Stage stage = (Stage) btnPresupuestos.getScene().getWindow();
            stage.close();
			
        } catch (Exception e) {
            e.printStackTrace();
			log.log(Level.ERROR, e);
        }
    }

    @FXML 
    void initialize()
    {

    }
    
    public void setDato(Trazas log, int idCliente)
    {
    	this.log = log;
    	this.idCliente = idCliente;
    }
    
    public Documento_BEAN getDato()
    {
    	return seleccionado;
    }
}