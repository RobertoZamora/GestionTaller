package com.zamora.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Level;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.zamora.Main;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.Albaran_BEAN;
import com.zamora.modelo.Cliente_BEAN;
import com.zamora.modelo.Vehiculo_BEAN;
import com.zamora.trazas.Trazas;
import com.zamora.util.Util;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Albaranes_CON
{

    @FXML
    private JFXButton botonAddAlbaran;

    @FXML
    private JFXButton botonBorrarAlbaran;

    @FXML
    private JFXButton botonImprimirAlbaran;

    @FXML
    private JFXButton botonAjustarImagen;

    @FXML
    private JFXButton botonGirarIzquierda;

    @FXML
    private JFXButton botonGirarDerecha;
    
    @FXML
    private Canvas imgCanvas;

    @FXML
    private JFXSlider scrollZoom;

    @FXML
    private ListView<Albaran_BEAN> listaAlbaranes;

    @FXML
    private ScrollPane scrollPanel;
    
    private Trazas log;
	private Util util;
    private int idCliente;
    private int idVehiculo;
    private String rutaFicheros;
    private String cif;
    private String matricula;
    private final String NO_ALBARAN = "SIN ALBARANES";
    private Image imagen;

    private double anguloRotacion = 0;
    private double escala = 1.0;

    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;

    @FXML
    void initialize()
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();

    	util = new Util();
    	
    	cif = new Cliente_BEAN().recuperarCliente(conexion, log, idCliente).getIDFISCAL();
    	matricula = new Vehiculo_BEAN().recuperarVehiculo(conexion, log, idVehiculo).getMATRICULA();
    	
    	rutaFicheros = Main.rutaDatos + File.separator + "archivos" + File.separator + cif + File.separator + matricula + File.separator;
    	
    	ArrayList<Albaran_BEAN> albaranes = new Albaran_BEAN().recuperarAlbaranes(conexion, log, idCliente, idVehiculo);
    	
    	if(albaranes.size() == 0)
    		listaAlbaranes.getItems().add(new Albaran_BEAN(
					0 ,0 ,0 ,null ,NO_ALBARAN));
        
    	listaAlbaranes.getItems().addAll(albaranes);
    	listaAlbaranes.getSelectionModel().selectFirst();
    	
    	if(!NO_ALBARAN.equals(listaAlbaranes.getSelectionModel().getSelectedItem().getRUTA()))
    	{
    		imagen = new Image("file:" + rutaFicheros + listaAlbaranes.getSelectionModel().getSelectedItem().getRUTA());           
    	}
    	else
    	{
    		imagen = new Image(getClass().getResourceAsStream("/com/zamora/img/fondoAlbaran.png"));
            scrollZoom.setDisable(true);
    	}
        
		// Calcular el factor de escalado inicial
        double widthScale = scrollPanel.getPrefWidth() / imagen.getWidth();
        double heightScale = scrollPanel.getPrefHeight() / imagen.getHeight();
        escala = Math.min(widthScale, heightScale);
        scrollZoom.setValue(escala);
        
        cargarImagen(); 
    	
    	scrollZoom.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            	cambiarZoom();
			}
        });
    	
    	// Define un formateador personalizado para mostrar valores decimales
    	scrollZoom.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                // Formatea el valor como decimal con dos decimales
                return String.format("%.2f", value);
            }

            @Override
            public Double fromString(String string) {
                // Convierte una cadena de texto en un valor Double (no es necesario para la visualización)
                return Double.parseDouble(string);
            }
        });
    	
    	// Configurar un controlador de eventos para el arrastre en el Canvas
    	imgCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            orgSceneX = event.getSceneX();
            orgSceneY = event.getSceneY();
            orgTranslateX = scrollPanel.getHvalue();
            orgTranslateY = scrollPanel.getVvalue();
        });

    	imgCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double offsetX = event.getSceneX() - orgSceneX;
            double offsetY = event.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX - offsetX / imgCanvas.getWidth();
            double newTranslateY = orgTranslateY - offsetY / imgCanvas.getHeight();

            // Asegúrate de que los valores de desplazamiento estén dentro de los límites del ScrollPane
            if (newTranslateX >= 0 && newTranslateX <= 1) {
            	scrollPanel.setHvalue(newTranslateX);
            }
            if (newTranslateY >= 0 && newTranslateY <= 1) {
            	scrollPanel.setVvalue(newTranslateY);
            }
        });
    	conexion.desconectar();
    	
    	Tooltip tooltipAddAlbaran = new Tooltip("Añade un albaran.");
    	Tooltip tooltipBorrarAlbaran = new Tooltip("Borra un albaran (REQUIERE CONTRASEÑA).");
    	Tooltip tooltipImprimirAlbaran = new Tooltip("Imprimir albaran.");
    	Tooltip tooltipAjustarImagen = new Tooltip("Ajusta el albaran al tamaño de la ventana.");
    	Tooltip tooltipGirarIzquierda = new Tooltip("Gira el albaran en sentido contrario a las agujas del reloj.");
    	Tooltip tooltipGirarDerecha = new Tooltip("Gira el albaran en sentido de las agujas del reloj.");

    	botonAddAlbaran.setTooltip(tooltipAddAlbaran);
    	botonBorrarAlbaran.setTooltip(tooltipBorrarAlbaran);
    	botonImprimirAlbaran.setTooltip(tooltipImprimirAlbaran);
    	botonAjustarImagen.setTooltip(tooltipAjustarImagen);
    	botonGirarIzquierda.setTooltip(tooltipGirarIzquierda);
    	botonGirarDerecha.setTooltip(tooltipGirarDerecha);
	}
    
    public void setDato(Trazas log, int idCliente, int idVehiculo)
    {
    	this.log = log;
    	this.idCliente = idCliente;
    	this.idVehiculo = idVehiculo;
    }

    @FXML
    void addAlbaran(MouseEvent event)
    {
    	int iNewAlbaran = listaAlbaranes.getItems().get(listaAlbaranes.getItems().size() - 1).getID_ALBARAN() + 1;
    	log.log(Level.DEBUG, "Nuevo id: " + iNewAlbaran);

    	Date fechaActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formatoFecha.format(fechaActual);
    	
    	// Crea un FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona un archivo"); // Título de la ventana

        FileChooser.ExtensionFilter extFilterImg = new FileChooser.ExtensionFilter("Archivos de Imagen (*.png, *.jpeg, *.jpeg, *.gif, *.bmp)", "*.png", "*.jpeg", "*.jpg", "*.gif", "*.bmp");
        
        // Agrega los filtros al FileChooser
        fileChooser.getExtensionFilters().addAll(extFilterImg);

        // Establece el directorio inicial en la carpeta home del usuario
        fileChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));        

        // Muestra el diálogo de selección de archivos
        File selectedFileAux = fileChooser.showOpenDialog(((Stage) botonAddAlbaran.getScene().getWindow()));
        
        if(selectedFileAux != null)
        {
            log.log(Level.DEBUG, selectedFileAux);
            
            File destino = new File(rutaFicheros);
            if(!destino.exists())
            	destino.mkdirs();
           
            Path origenPath = FileSystems.getDefault().getPath(selectedFileAux.getAbsolutePath());
            Path destinoPath = FileSystems.getDefault().getPath(destino.getAbsolutePath() + File.separator + cif + "_" + matricula + "_" + iNewAlbaran + selectedFileAux.getName().substring(selectedFileAux.getName().lastIndexOf(".")));
            
            try {
    			Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
    		} catch (IOException e) {
    			e.printStackTrace();
				log.log(Level.ERROR, e);
    		}

            if(listaAlbaranes.getItems().size() == 1 && NO_ALBARAN.equals(listaAlbaranes.getSelectionModel().getSelectedItem().getRUTA()))
            	listaAlbaranes.getItems().clear();
            
            Albaran_BEAN aux = new Albaran_BEAN();
            aux.setID_ALBARAN(iNewAlbaran);
            aux.setID_CLIENTE(idCliente);
            aux.setID_VEHICULO(idVehiculo);
            aux.setFECHA(fechaFormateada);
            aux.setRUTA(cif + "_" + matricula + "_" + iNewAlbaran + selectedFileAux.getName().substring(selectedFileAux.getName().lastIndexOf(".")));
            
        	ConexionBBDD conexion = new ConexionBBDD(log);
        	conexion.conectar();
            aux.guardarAlbaran(conexion, log);
        	conexion.desconectar();
        	
        	listaAlbaranes.getItems().add(aux);

        	listaAlbaranes.getSelectionModel().selectLast();
        	imagen = new Image("file:" + rutaFicheros + aux.getRUTA());

        	// Calcular el factor de escalado inicial
            double widthScale = scrollPanel.getPrefWidth() / imagen.getWidth();
            double heightScale = scrollPanel.getPrefHeight() / imagen.getHeight();
            escala = Math.min(widthScale, heightScale);
            scrollZoom.setValue(escala);
            scrollZoom.setDisable(false);

            cargarImagen(); 
        }
    }

    @FXML
    void ajustarImagen(MouseEvent event)
    {

    	if(!NO_ALBARAN.equals(listaAlbaranes.getSelectionModel().getSelectedItem().getRUTA()))
    	{
            double ancho = imgCanvas.getWidth();
            double alto = imgCanvas.getHeight();

            double widthScale = 0.0;
            double heightScale = 0.0;
            if (ancho == Math.max(ancho, alto)) {
                widthScale = scrollPanel.getPrefWidth() / imagen.getWidth();
                heightScale = scrollPanel.getPrefHeight() / imagen.getHeight();
            } else {
                widthScale = scrollPanel.getPrefWidth() / imagen.getHeight();
                heightScale = scrollPanel.getPrefHeight() / imagen.getWidth();
            }
            
            double escalaAux = Math.min(widthScale, heightScale);
            scrollZoom.setValue(escalaAux);
            redibujarImagenRotada(false);
    	}
    }

    @FXML
    void borrarAlbaran(MouseEvent event)
    {
    	try
		{
    		if(!NO_ALBARAN.equals(listaAlbaranes.getSelectionModel().getSelectedItem().getRUTA()))
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
    		    		Albaran_BEAN albaran = listaAlbaranes.getSelectionModel().getSelectedItem();
    		    		albaran.borrarAlbaran(db, log);

    		    		File archivo = new File(rutaFicheros + listaAlbaranes.getSelectionModel().getSelectedItem().getRUTA());
    		    		archivo.delete();
    		    		
    		    		listaAlbaranes.getItems().remove(albaran);
    		    		if(listaAlbaranes.getItems().size() == 0)
    		        		listaAlbaranes.getItems().add(new Albaran_BEAN(
    		    					0 ,0 ,0 ,null ,NO_ALBARAN));
    		    		
    		        	listaAlbaranes.getSelectionModel().selectFirst();
    		        	
    		        	if(!NO_ALBARAN.equals(listaAlbaranes.getSelectionModel().getSelectedItem().getRUTA()))
    		        	{
    		        		imagen = new Image("file:" + rutaFicheros + listaAlbaranes.getSelectionModel().getSelectedItem().getRUTA());           
    		        	}
    		        	else
    		        	{
    		        		imagen = new Image(getClass().getResourceAsStream("/com/zamora/img/fondoAlbaran.png"));
    		                scrollZoom.setDisable(true);
    		        	}
    		        	
    		        	// Calcular el factor de escalado inicial
    		            double widthScale = scrollPanel.getPrefWidth() / imagen.getWidth();
    		            double heightScale = scrollPanel.getPrefHeight() / imagen.getHeight();
    		            escala = Math.min(widthScale, heightScale);
    		            scrollZoom.setValue(escala);

    		            cargarImagen();
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

    void cambiarZoom()
    {
    	if(!NO_ALBARAN.equals(listaAlbaranes.getSelectionModel().getSelectedItem().getRUTA()))
    	{
    		escala = scrollZoom.getValue();
            redibujarImagenRotada(false);
    	}
    }

    @FXML
    void girarDerecha(MouseEvent event)
    {
    	if(!NO_ALBARAN.equals(listaAlbaranes.getSelectionModel().getSelectedItem().getRUTA()))
    	{
    	 	rotar90Grados(90);
    	}
    }

    @FXML
    void girarIzquierda(MouseEvent event)
    {
    	if(!NO_ALBARAN.equals(listaAlbaranes.getSelectionModel().getSelectedItem().getRUTA()))
    	{
    		rotar90Grados(-90);
    	}    	 
    }

    @FXML
    void imprimirAlbaran(MouseEvent event)
    {
    	if(listaAlbaranes.getItems().size() > 0 
    			&& !listaAlbaranes.getSelectionModel().getSelectedItem().getRUTA().equals(NO_ALBARAN))
		{
				imprimir();
		}
    }

	public void imprimir()
	{
		
		// Crear una ventana emergente de diálogo modal con un mensaje de progreso
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		Stage primaryStage = ((Stage) botonImprimirAlbaran.getScene().getWindow());
		dialogStage.initOwner(primaryStage); // Donde primaryStage es tu ventana principal

		// Crea un mensaje de progreso, por ejemplo, una barra de progreso
		ProgressBar progressBar = new ProgressBar();
		progressBar.setProgress(-1.0);
		progressBar.setPrefWidth(200); // Personaliza el tamaño

		// Crea un diseño para la ventana emergente
		VBox dialogVBox = new VBox(20);
		dialogVBox.setAlignment(Pos.CENTER);
		dialogVBox.getChildren().addAll(new Text("Generando impresión..."), progressBar);

		Scene dialogScene = new Scene(dialogVBox, 300, 100);
		dialogStage.setScene(dialogScene);
		
		dialogStage.show();
		
		// Crear un objeto PrinterJob
        PrinterJob printerJob = PrinterJob.createPrinterJob();

        if (printerJob != null) {
            boolean printDialogResult = printerJob.showPrintDialog(null);

            if (printDialogResult) {
                boolean printJobResult = printerJob.printPage(imgCanvas);

                if (printJobResult) {
                    printerJob.endJob();
                }

            } else {
                printerJob.cancelJob(); // Cancelar la impresión si se cancela el diálogo
            }
        }
        
        dialogStage.close();
	}

    @FXML
    void seleccionarAlbaran(MouseEvent event)
    {
    	imagen = new Image("file:" + rutaFicheros + listaAlbaranes.getSelectionModel().getSelectedItem().getRUTA());

    	// Calcular el factor de escalado inicial
        double widthScale = scrollPanel.getPrefWidth() / imagen.getWidth();
        double heightScale = scrollPanel.getPrefHeight() / imagen.getHeight();
        escala = Math.min(widthScale, heightScale);
        scrollZoom.setValue(escala);
    	
        cargarImagen(); 
    }

    private void rotar90Grados(double grados)
    {    	
    	if(grados < 0)
    	{
    		anguloRotacion += grados;
            if (anguloRotacion <= -360) {
                anguloRotacion = 0;
            }
    	}
    	else if(grados > 0)
    	{
            anguloRotacion += grados;
            if (anguloRotacion >= 360) {
                anguloRotacion = 0;
            }
    	}
        redibujarImagenRotada(true);
    }

    private void cargarImagen()
    {
        // Establece el tamaño del Canvas al tamaño de la imagen
    	imgCanvas.setWidth(imagen.getWidth());
    	imgCanvas.setHeight(imagen.getHeight());

        // Dibuja la imagen inicialmente
        redibujarImagenRotada(false);
    }

    private void redibujarImagenRotada(boolean estaRotando)
    {
        // Establece el tamaño del Canvas al tamaño de la imagen, teniendo en cuenta la escala
        double anchoImagen = imagen.getWidth() * escala;
        double altoImagen = imagen.getHeight() * escala;

        double ancho = imgCanvas.getWidth();
        double alto = imgCanvas.getHeight();

        if (estaRotando) {
        	imgCanvas.setWidth(alto);
        	imgCanvas.setHeight(ancho);
        } else {
            if (anchoImagen == Math.max(anchoImagen, altoImagen) && ancho == Math.max(ancho, alto)) {
            	imgCanvas.setWidth(anchoImagen);
            	imgCanvas.setHeight(altoImagen);
            } else {
            	imgCanvas.setWidth(altoImagen);
            	imgCanvas.setHeight(anchoImagen);
            }
        }

        GraphicsContext gc = imgCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, imgCanvas.getWidth(), imgCanvas.getHeight());

        gc.save();
        gc.translate(imgCanvas.getWidth() / 2, imgCanvas.getHeight() / 2);
        gc.rotate(anguloRotacion);

        // Dibuja la imagen con la escala aplicada después de la rotación
        gc.drawImage(imagen, -anchoImagen / 2, -altoImagen / 2, anchoImagen, altoImagen);

        gc.restore();

        // Ajusta el valor de desplazamiento horizontal y vertical para centrar el Canvas
        Bounds canvasBounds = imgCanvas.getBoundsInParent();
        Bounds viewportBounds = scrollPanel.getViewportBounds();
        double hValue = (canvasBounds.getWidth() - viewportBounds.getWidth()) / 2 / canvasBounds.getWidth();
        double vValue = (canvasBounds.getHeight() - viewportBounds.getHeight()) / 2 / canvasBounds.getHeight();
        scrollPanel.setHvalue(hValue);
        scrollPanel.setVvalue(vValue);
    }
}
