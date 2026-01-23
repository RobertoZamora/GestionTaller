package com.zamora.controller;

import com.jfoenix.controls.JFXButton;
import com.zamora.Main;
import com.zamora.BBDD.ConexionAccess;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.trazas.Trazas;
import com.zamora.util.Util;
import com.zamora.util.ZipUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Configuracion_CON {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnAddMarca;

    @FXML
    private JFXButton btnAddModelo;

    @FXML
    private JFXButton btnGenerarBackUp;

    @FXML
    private JFXButton btnCargarBackUp;

    @FXML
    private JFXButton btnParametros;
    
    private Trazas log;
    private Util util;

    @FXML
    void initialize()
    {
    	Tooltip tooltipAddMarca = new Tooltip("Agregar marcas");
		Tooltip tooltipAddModelo = new Tooltip("Agregar modelos");
		Tooltip tooltipGenerarBackUp = new Tooltip("Genera un backup");
		Tooltip tooltipCargarBackUp = new Tooltip("Carga un backup\nOJO SE BORRA LO QUE HUBIERA");
		Tooltip tooltipParametros = new Tooltip("Parametros necesarios para la aplicación.\nREQUIERE CONTRASEÑA.");
		btnAddMarca.setTooltip(tooltipAddMarca);
		btnAddModelo.setTooltip(tooltipAddModelo);
		btnGenerarBackUp.setTooltip(tooltipGenerarBackUp);
		btnCargarBackUp.setTooltip(tooltipCargarBackUp);
		btnParametros.setTooltip(tooltipParametros);
		
		util = new Util();
    }
    
    public void setDato(Trazas log)
    {
    	this.log = log;
    }

    @FXML
    void addMarca(MouseEvent event)
    {
    	try
		{
    		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/PedirDato_PAN.fxml"));
    		
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
    		seleccionable.setTitle("Nombre de la Marca");

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
    		String aux = (String) ((PedirDato_CON)loader.getController()).getDato();
    		
			if(!"".equals(aux) && aux != null)
			{
				ConexionBBDD db = new ConexionBBDD(log);
				db.conectar();
				
				if(!util.existeMarca(db, log, aux))
				{
					util.insertarMarca(db, log, aux);
				}
				else
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setTitle("Error");
					alert.setContentText("Esa marca ya existe.");
					alert.showAndWait();
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

    @FXML
    void addModelo(MouseEvent event)
    {
    	try
		{
    		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/Modelo_PAN.fxml"));
    		
    		loader.setControllerFactory(controllerClass -> {
                if (controllerClass == Modelo_CON.class) {
                	Modelo_CON pass = new Modelo_CON();
                    pass.setDato(log);
                    return pass;
                }
                return null;
            });
    		
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
    		String[] aux = ((Modelo_CON)loader.getController()).getDatos();
    		
			if(aux != null && aux.length > 0)
			{
				ConexionBBDD db = new ConexionBBDD(log);
				db.conectar();
				
				util.insertarModelo(db, log, Integer.parseInt(aux[0]),aux[1]);
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

    @FXML
    void cargarBackUp(MouseEvent event)
    {
    	// Crea un FileChooser
		DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecciona una carpeta");
        
        // Establece el directorio inicial en la carpeta home del usuario
        directoryChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));   

        // Abre el diálogo de selección de carpeta
        File selectedDirectory = directoryChooser.showDialog(((Stage) btnGenerarBackUp.getScene().getWindow()));
		
		if(selectedDirectory != null)
		{
			// Crear una ventana emergente de diálogo modal con un mensaje de progreso
			Stage dialogStage = new Stage();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initStyle(StageStyle.UNDECORATED);
			Stage primaryStage = ((Stage) btnGenerarBackUp.getScene().getWindow());
			dialogStage.initOwner(primaryStage); // Donde primaryStage es tu ventana principal

			// Crea un mensaje de progreso, por ejemplo, una barra de progreso
			ProgressBar progressBar = new ProgressBar();
			progressBar.setProgress(-1.0);
			progressBar.setPrefWidth(200); // Personaliza el tamaño

			// Crea un diseño para la ventana emergente
			VBox dialogVBox = new VBox(20);
			dialogVBox.setAlignment(Pos.CENTER);
			dialogVBox.getChildren().addAll(new Text("Cargando copia de seguridad..."), progressBar);

			Scene dialogScene = new Scene(dialogVBox, 300, 100);
			dialogStage.setScene(dialogScene);

			Task<String> task = new Task<String>()
			{
				@Override
				protected String call() throws Exception
				{
					String archivosEncontrados = "NO";
					String directorioBackUp = selectedDirectory.getAbsolutePath();
					if(selectedDirectory.exists() && selectedDirectory.isDirectory())
					{
						String files[] = selectedDirectory.list();
						for(int i = 0; i < files.length; i++)
						{
							
							if(files[i].equals("001.zip"))
							{
								archivosEncontrados = "SI";
								log.log(Level.DEBUG, "Archivos de clientes");
								
								File archivos = new File(Main.rutaDatos + File.separator + "archivos");
								if(archivos.exists())
									FileUtils.deleteDirectory(archivos);
								
								ZipUtils zip = new ZipUtils(log, directorioBackUp + File.separator + files[i], Main.rutaDatos);
								zip.unZipIt();								
							}
							else if(files[i].equals("002.zip"))
							{
								File fichero = new File(selectedDirectory.getAbsolutePath() + File.separator + files[i]);
								log.log(Level.DEBUG, fichero.getAbsolutePath());
								
								String origen = fichero.getAbsolutePath();
								String destino = Main.rutaDatos + File.separator + "tmp";
								String rutaDB = destino + File.separator + "configuraciones" + File.separator + "taller.db";
								String tempSql = destino + File.separator + "temp.sql";
								
								ZipUtils zip = new ZipUtils(log, origen, destino);
								zip.unZipIt();
								
								ConexionAccess dbAcces = new ConexionAccess(log, rutaDB);
								dbAcces.conectar();
								
								util.backUpBBDDAccess(dbAcces, log, tempSql);
								dbAcces.desconectar();
								
								FileReader fr = null;
						        BufferedReader br = null;
						        
						        try {

									File ficheroTMP = new File(tempSql);
									FileInputStream fis = new FileInputStream(ficheroTMP);
									InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
						            br = new BufferedReader(isr);
						            
						            String linea;
					            	
					            	ConexionBBDD db = new ConexionBBDD(log);
									db.conectar();
									log.log(Level.DEBUG, "Iniciamos la carga de la BBDD");
									
									long inicio = System.currentTimeMillis();
						            while((linea = br.readLine()) != null)
						            {
						            	log.log(Level.DEBUG, linea);
										db.actualizacion(linea);	
						            }
									db.desconectar();
									long fin = System.currentTimeMillis();
									log.log(Level.DEBUG, "Tiempo en cargar el BackUp de BBDD: " + (double) (fin - inicio) / 1000 + " segundos." );
									
						        } catch (NullPointerException e) {
						        	log.log(Level.ERROR, "No se ha seleccionado ningún fichero");
						        } catch (Exception e) {
						        	archivosEncontrados = e.getMessage();
						        	log.log(Level.ERROR, e.getMessage());
						        }
						        finally
						        {
						        	try{                    
						                if( null != fr )   
						                   fr.close();
						                   
						                if( null != br )
							                   br.close();
						                
						             }catch (Exception e2){ 
						                e2.printStackTrace();
						             }
						        }
							}
							else if(files[i].equals("DDBB.sql"))
							{
								archivosEncontrados = "SI";
								log.log(Level.DEBUG, "Datos del aplicativo");
								
								FileReader fr = null;
						        BufferedReader br = null;
						        
						        try {

									File fichero = new File(selectedDirectory.getAbsolutePath() + File.separator + files[i]);
									FileInputStream fis = new FileInputStream(fichero);
									InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
									br = new BufferedReader(isr);
						            
						            String linea;
					            	
					            	ConexionBBDD db = new ConexionBBDD(log);
									db.conectar();
									log.log(Level.DEBUG, "Iniciamos la carga de la BBDD");
									
									long inicio = System.currentTimeMillis();
						            while((linea = br.readLine()) != null)
						            {
						            	log.log(Level.DEBUG, linea);
										db.actualizacion(linea);	
						            }
									db.desconectar();
									long fin = System.currentTimeMillis();
									log.log(Level.DEBUG, "Tiempo en cargar el BackUp de BBDD: " + (double) (fin - inicio) / 1000 + " segundos." );
						            
						        } catch (NullPointerException e) {
						        	log.log(Level.ERROR, "No se ha seleccionado ningún fichero");
						        } catch (Exception e) {
						        	archivosEncontrados = e.getMessage();
						        	log.log(Level.ERROR, e.getMessage());
						        }
						        finally
						        {
						        	try{                    
						                if( null != fr )   
						                   fr.close();
						                   
						                if( null != br )
							                   br.close();
						                
						             }catch (Exception e2){ 
						                e2.printStackTrace();
						             }
						        }
							}
						}
					}					
					return archivosEncontrados;
				}
			};

	        task.setOnRunning((WorkerStateEvent e) -> {
	            dialogStage.show();
	        });

	        task.setOnSucceeded((WorkerStateEvent e) -> {
	        	if(!String.valueOf(e.getSource().getValue()).equals("SI"))
	        	{
		        	Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setTitle("Error");
					alert.setContentText("No se encontraron ficheros para cargar la copia de seguridad.");
					alert.showAndWait();
					log.log(Level.ERROR, "No se encontraron ficheros para cargar la copia de seguridad.");
	        	}
	            dialogStage.close();
	        });

	        Thread thread = new Thread(task);
	        thread.start();
		}
	}

    @FXML
    void generarBackUp(MouseEvent event)
    {
    	// Crea un FileChooser
		DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecciona una carpeta");
        
        // Establece el directorio inicial en la carpeta home del usuario
        directoryChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));   

        // Abre el diálogo de selección de carpeta
        File selectedDirectory = directoryChooser.showDialog(((Stage) btnGenerarBackUp.getScene().getWindow()));
		
		if(selectedDirectory != null)
		{
			// Crear una ventana emergente de diálogo modal con un mensaje de progreso
			Stage dialogStage = new Stage();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initStyle(StageStyle.UNDECORATED);
			Stage primaryStage = ((Stage) btnGenerarBackUp.getScene().getWindow());
			dialogStage.initOwner(primaryStage); // Donde primaryStage es tu ventana principal

			// Crea un mensaje de progreso, por ejemplo, una barra de progreso
			ProgressBar progressBar = new ProgressBar();
			progressBar.setProgress(-1.0);
			progressBar.setPrefWidth(200); // Personaliza el tamaño

			// Crea un diseño para la ventana emergente
			VBox dialogVBox = new VBox(20);
			dialogVBox.setAlignment(Pos.CENTER);
			dialogVBox.getChildren().addAll(new Text("Generando copia de seguridad..."), progressBar);

			Scene dialogScene = new Scene(dialogVBox, 300, 100);
			dialogStage.setScene(dialogScene);

			Task<Void> task = new Task<Void>()
			{
				@Override
				protected Void call() throws Exception
				{
					// BACKUP FICHEROS
					File rutaFicheros = new File(Main.rutaDatos + "/archivos");
					
					SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
					String rutaSalida = selectedDirectory.getAbsolutePath() + File.separator + "BACKUP_" + sdf.format(new Date());
					File carpetaSalida = new File(rutaSalida);
					if(!carpetaSalida.exists())
						carpetaSalida.mkdir();
					String salidaArchivos = rutaSalida + File.separator + "001.zip";
					
					ZipUtils zip = new ZipUtils(log, rutaFicheros.getAbsolutePath(), salidaArchivos);
					zip.zipIt();
					
					// BACKUP BBDD
					ConexionBBDD db = new ConexionBBDD(log);
					db.conectar();
					String salidaConfiguracion = rutaSalida + File.separator + "DDBB.sql";
					util.backUpBBDD(db, log, salidaConfiguracion);
					db.desconectar();
					
					return null;
				}
			};

	        task.setOnRunning((WorkerStateEvent e) -> {
	            dialogStage.show();
	        });

	        task.setOnSucceeded((WorkerStateEvent e) -> {
	            dialogStage.close();
	        });

	        Thread thread = new Thread(task);
	        thread.start();
		}
    }

    @FXML
    void parametros(MouseEvent event)
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
	    	
	    	db.desconectar();
	    	
	    	if(!"".equals(aux) && pass.equals(aux))
	    	{
	    		FXMLLoader loaderParametros = new FXMLLoader(Main.class.getResource("/vistas/Parametros_PAN.fxml"));
	    		
	    		loaderParametros.setControllerFactory(controllerClass -> {
	                if (controllerClass == Parametros_CON.class) {
	                	Parametros_CON param = new Parametros_CON();
	                	param.setDato(log);
	                    return param;
	                }
	                return null;
	            });
	    		
	    		Parent rootParametros = loaderParametros.load();
	    		
	    		Stage seleccionableParametros = new Stage();
	    		seleccionableParametros.setTitle("Parametrizaciones");

	    		Image iconParametros = new Image(Main.class.getResource("/img/engranage.png").toExternalForm());
	    		seleccionableParametros.getIcons().add(iconParametros);

	    		seleccionableParametros.setScene(new Scene(rootParametros));

	    		// Configura la ventana secundaria como modal
	    		seleccionableParametros.initModality(Modality.APPLICATION_MODAL);

	    		// Configura el evento para establecer el tamaño mínimo
	    		seleccionableParametros.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
	                @Override
	                public void handle(WindowEvent event) {
	                	seleccionableParametros.setMinWidth(seleccionableParametros.getWidth());
	                	seleccionableParametros.setMinHeight(seleccionableParametros.getHeight());
	                }
	            });

	    		seleccionableParametros.showAndWait(); // Muestra la ventana y espera hasta que se cierre
	    	}
	    	else if(aux != null)
	    	{
	    		Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setTitle("Error");
				alert.setContentText("Contraseña incorrecta.");
				alert.showAndWait();
				log.log(Level.ERROR, "Contraseña incorrecta");
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
}