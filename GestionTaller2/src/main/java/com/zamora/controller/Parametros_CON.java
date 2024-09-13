package com.zamora.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.zamora.Main;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.trazas.Trazas;
import com.zamora.util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Level;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Parametros_CON {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextArea matriculas;

    @FXML
    private JFXTextArea pieFactura;

    @FXML
    private JFXTextArea piePresupuesto;

    @FXML
    private JFXTextArea servidoresFecha;

    @FXML
    private JFXTextArea formasPago;

    @FXML
    private JFXButton btnPass;

    @FXML
    private JFXButton btnGuardar;

    private Trazas log;
    private Util util;
    private String sMatriculas;
    private String sPieFactura;
    private String sPiePresupuesto;
    private String sServidoresFecha;
    private String sFormasPago;
    
    @FXML
    void guardar(MouseEvent event)
    {
    	ConexionBBDD db = new ConexionBBDD(log);;
		db.conectar();
		
		if(!matriculas.getText().equals(sMatriculas))
		{
			util.actualizarParametro(db, log, "EXPRESIONES_MATRICULAS", matriculas.getText());
		}
		
		if(!pieFactura.getText().equals(sPieFactura))
		{
			util.actualizarParametro(db, log, "LEGAL_FACTURA", pieFactura.getText());
		}
		
		if(!piePresupuesto.getText().equals(sPiePresupuesto))
		{
			util.actualizarParametro(db, log, "LEGAL_PRESUPUESTO", piePresupuesto.getText());
		}
		
		if(!servidoresFecha.getText().equals(sServidoresFecha))
		{
			util.actualizarParametro(db, log, "SERVER_DATE", servidoresFecha.getText());
		}
		
		if(!formasPago.getText().equals(sFormasPago))
		{
			util.actualizarParametro(db, log, "FORMAS_PAGO", formasPago.getText());
		}
		
		db.desconectar();
		
		Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void pass(MouseEvent event)
    {
    	try
		{
    		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/PasswordChange_PAN.fxml"));
    		
    		Parent root = loader.load();
    		
    		Stage seleccionable = new Stage();
    		seleccionable.setTitle("Modelo");

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
    		String aux = ((PasswordChange_CON)loader.getController()).getDato();
    		
			if(aux != null && aux.length() > 0)
			{
				ConexionBBDD db = new ConexionBBDD(log);
				db.conectar();
				
				util.actualizarParametro(db, log, "PASSWORD", aux);
				db.desconectar();
			}
			else
			{
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setTitle("Error");
				alert.setContentText("La contraseña no debe estar vacia.");
				alert.showAndWait();
				log.log(Level.ERROR, "La contraseña no debe estar vacia.");
			}
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
    void initialize()
    {
    	util = new Util();
        ConexionBBDD db = new ConexionBBDD(log);
        db.conectar();
        
        sMatriculas = util.recuperarParametro(db, log, "EXPRESIONES_MATRICULAS");
        sPieFactura = util.recuperarParametro(db, log, "LEGAL_FACTURA");
		sPiePresupuesto =  util.recuperarParametro(db, log, "LEGAL_PRESUPUESTO");
		sServidoresFecha =  util.recuperarParametro(db, log, "SERVER_DATE");
		sFormasPago =  util.recuperarParametro(db, log, "FORMAS_PAGO");
		
		matriculas.setText(sMatriculas);
		pieFactura.setText(sPieFactura);
		piePresupuesto.setText(sPiePresupuesto);
		servidoresFecha.setText(sServidoresFecha);
		formasPago.setText(sFormasPago);
        
        db.desconectar();
    }
    
    public void setDato(Trazas log)
    {
    	this.log = log;
    }
}