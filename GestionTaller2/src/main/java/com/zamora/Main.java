package com.zamora;

import java.io.File;

import org.apache.log4j.Level;

import com.zamora.BBDD.ConexionBBDD;
import com.zamora.controller.Principal_CON;
import com.zamora.trazas.Trazas;
import com.zamora.util.Util;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Main extends Application {
	
	public static String rutaDatos = "";
	private Trazas log;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Util.generarCarpetaDatos();
			log = new Trazas();
			log.log(Level.DEBUG, "ruta de datos --> " + rutaDatos);
			
			File BBDD = new File(Main.rutaDatos + File.separator + "configuraciones/AMJ");
			log.log(Level.DEBUG, "RUTA BBDD: " + BBDD.getAbsolutePath());
			if(!BBDD.exists())
			{
				// Crear una nueva ventana para mostrar la barra de progreso
		        Stage progressStage = new Stage();
		        progressStage.initModality(Modality.APPLICATION_MODAL);
		        progressStage.initStyle(StageStyle.UNDECORATED);

		        ProgressBar progressBar = new ProgressBar();
		        progressBar.setProgress(0);
		        progressBar.setPrefWidth(200); // Personaliza el tamaño
		        
		        Text texto = new Text("Iniciando...");

		        VBox vbox = new VBox();
		        vbox.setAlignment(Pos.CENTER);
		        vbox.getChildren().addAll(new Text("Cargando..."), texto, progressBar);

		        Scene progressScene = new Scene(vbox, 300, 100);
		        progressStage.setScene(progressScene);
		        progressStage.show();	        

		        // Simular un proceso que tarda 10 segundos en completarse
		        Task<Void> task = new Task<Void>() {
		            @Override
		            protected Void call() throws Exception
		            {
		            	int lineas = 0;
		            	lineas += Util.contarLineasArchivoEnJar("datos/DDL.sql");
		            	lineas += Util.contarLineasArchivoEnJar("datos/CP.sql");
		            	lineas += Util.contarLineasArchivoEnJar("datos/MARCA.sql");
		            	lineas += Util.contarLineasArchivoEnJar("datos/MODELO.sql");
		            	lineas += Util.contarLineasArchivoEnJar("datos/PARAMETROS.sql");
		            	lineas += Util.contarLineasArchivoEnJar("datos/COMBUSTIBLE.sql");
			            
		            	ConexionBBDD db = new ConexionBBDD(log);
		            	db.cargaInicial(progressBar, texto, lineas);
		                return null;
		            }
		        };

		        task.setOnSucceeded(e -> {
		            progressStage.close(); // Cierra la ventana de progreso al completar la tarea
		            mostrarVentanaPrincipal(primaryStage); // Muestra la ventana principal
		        });

		        new Thread(task).start(); // Inicia la tarea en un hilo separado
			}
			else
			{
				mostrarVentanaPrincipal(primaryStage); // Muestra la ventana principal
			}
			
			
	    } catch (Exception e) {
	        e.printStackTrace();
	        log.log(Level.ERROR, e);
	    }
	}
	
	// Método para mostrar la ventana principal
	private void mostrarVentanaPrincipal(Stage primaryStage) {
	    try {
	    	FXMLLoader loader =  new FXMLLoader(getClass().getResource("/vistas/Principal_PAN.fxml"));
			Pane ventana = (Pane) loader.load();

			Principal_CON principal = loader.getController();
			principal.setDato(log);
			
			Scene scene = new Scene(ventana, 860, 480);
			
			primaryStage.setTitle("Gestion Taller");

			Image icon = new Image(Main.class.getResourceAsStream("/img/car.png"));
			primaryStage.getIcons().add(icon);

			primaryStage.setScene(scene);

			// Configura el evento para establecer el tamaño mínimo
			primaryStage.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					primaryStage.setMinWidth(primaryStage.getWidth());
					primaryStage.setMinHeight(primaryStage.getHeight());
				}
			});
			
			primaryStage.setOnCloseRequest(event -> {
				System.exit(0);
			});

			primaryStage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	        log.log(Level.ERROR, e);
	    }
	}

	public static void main(String[] args) {
		launch(args);
	}
}
