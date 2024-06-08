package com.zamora.BBDD;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Level;

import com.zamora.Main;
import com.zamora.trazas.Trazas;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

public class ConexionBBDD {

	public Connection connection = null;
	private String driver = "";
	private String dbName = "";
	private String dbParam = "";
	private String connectionURL = "";
	private Trazas log;
	
	public ConexionBBDD(Trazas log)
	{
		super();
		this.log = log;
		
		driver = "org.apache.derby.jdbc.EmbeddedDriver";
		dbName = Main.rutaDatos + File.separator + "configuraciones/AMJ";
		dbParam = "create=true"; // la base de datos se creará si no existe todavía
		connectionURL = "jdbc:derby:" + dbName + ";" + dbParam;
		
		String nuevaRutaLogsErrores = Main.rutaDatos + File.separator + "logs/derby.log";
				
		System.setProperty("derby.stream.error.file", nuevaRutaLogsErrores);
	}
	
	public void cargaInicial(ProgressBar progressBar, Text texto, int lineas)
	{
		File BBDD = new File(Main.rutaDatos + File.separator + "configuraciones/AMJ");
		log.log(Level.DEBUG, "RUTA BBDD: " + BBDD.getAbsolutePath());
		if(!BBDD.exists())
		{
			int i = 0;
			log.log(Level.DEBUG, "La base de datos no existe procedemos a crearla.");
			connection = null;

			try {
				Class.forName(driver);
				connection = DriverManager.getConnection(connectionURL);
								
				InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("datos/DDL.sql");
				BufferedReader reader = null;
				
				InputStream inputStreamCP = Main.class.getClassLoader().getResourceAsStream("datos/CP.sql");
				BufferedReader readerCP = null;
				
				InputStream inputStreamMARCA = Main.class.getClassLoader().getResourceAsStream("datos/MARCA.sql");
				BufferedReader readerMARCA = null;
				
				InputStream inputStreamMODELO = Main.class.getClassLoader().getResourceAsStream("datos/MODELO.sql");
				BufferedReader readerMODELO = null;
				
				InputStream inputStreamPARAMETROS = Main.class.getClassLoader().getResourceAsStream("datos/PARAMETROS.sql");
				BufferedReader readerPARAMETROS = null;
				
				InputStream inputStreamCOMBUSTIBLE = Main.class.getClassLoader().getResourceAsStream("datos/COMBUSTIBLE.sql");
				BufferedReader readerCOMBUSTIBLE = null;
			        
		        try {
	            	Platform.runLater(() -> texto.setText("Creamos las tablas."));
		        	reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		            
		            String linea;
		            while((linea = reader.readLine())!=null)
		            {
			            log.log(Level.DEBUG, linea);
		            	String consulta = linea;
		            	actualizacion(consulta);
		            	final double progress = ((double) (i + 1) / lineas); // Incrementa la barra de progreso
	                    Platform.runLater(() -> progressBar.setProgress(progress));
		            	i++;
		            }

	            	Platform.runLater(() -> texto.setText("Cargando los codigos postales."));
		            readerCP = new BufferedReader(new InputStreamReader(inputStreamCP, "UTF-8"));
		            
		            String lineaCP;
		            while((lineaCP = readerCP.readLine())!=null)
		            {
			            log.log(Level.DEBUG, lineaCP);
		            	String consulta = lineaCP;
		            	actualizacion(consulta);
		            	final double progress = ((double) (i + 1) / lineas); // Incrementa la barra de progreso
	                    Platform.runLater(() -> progressBar.setProgress(progress));
		            	i++;
		            }

	            	Platform.runLater(() -> texto.setText("Cargando marcas de vehiculos."));
		            readerMARCA = new BufferedReader(new InputStreamReader(inputStreamMARCA, "UTF-8"));
				    
				    String lineaMARCA;
				    while((lineaMARCA = readerMARCA.readLine())!=null)
				    {
				        log.log(Level.DEBUG, lineaMARCA);
				    	String consulta = lineaMARCA;
				    	actualizacion(consulta);
		            	final double progress = ((double) (i + 1) / lineas); // Incrementa la barra de progreso
	                    Platform.runLater(() -> progressBar.setProgress(progress));
		            	i++;
				    }

	            	Platform.runLater(() -> texto.setText("Cargando los modelos de los vehiculos."));
				    readerMODELO = new BufferedReader(new InputStreamReader(inputStreamMODELO, "UTF-8"));
		            
		            String lineaMODELO;
		            while((lineaMODELO = readerMODELO.readLine())!=null)
		            {
			            log.log(Level.DEBUG, lineaMODELO);
		            	String consulta = lineaMODELO;
		            	actualizacion(consulta);
		            	final double progress = ((double) (i + 1) / lineas); // Incrementa la barra de progreso
	                    Platform.runLater(() -> progressBar.setProgress(progress));
		            	i++;
		            }

	            	Platform.runLater(() -> texto.setText("Cargando parametrizaciones."));		            
		            readerPARAMETROS = new BufferedReader(new InputStreamReader(inputStreamPARAMETROS, "UTF-8"));
		            
		            String lineaPARAMETROS;
		            while((lineaPARAMETROS = readerPARAMETROS.readLine())!=null)
		            {
			            log.log(Level.DEBUG, lineaPARAMETROS);
		            	String consulta = lineaPARAMETROS;
		            	actualizacion(consulta);
		            	final double progress = ((double) (i + 1) / lineas); // Incrementa la barra de progreso
	                    Platform.runLater(() -> progressBar.setProgress(progress));
		            	i++;
		            }


	            	Platform.runLater(() -> texto.setText("Cargando tipos de combustibles."));
		            readerCOMBUSTIBLE = new BufferedReader(new InputStreamReader(inputStreamCOMBUSTIBLE, "UTF-8"));
		            
		            String lineaCOMBUSTIBLE;
		            while((lineaCOMBUSTIBLE = readerCOMBUSTIBLE.readLine())!=null)
		            {
			            log.log(Level.DEBUG, lineaCOMBUSTIBLE);
		            	String consulta = lineaCOMBUSTIBLE;
		            	actualizacion(consulta);
		            	final double progress = ((double) (i + 1) / lineas); // Incrementa la barra de progreso
	                    Platform.runLater(() -> progressBar.setProgress(progress));
		            	i++;
		            }
		            
		        } catch (NullPointerException e) {
		        	log.log(Level.ERROR, "No se ha seleccionado ningún fichero");
		        } catch (Exception e) {
		            log.log(Level.ERROR, e.getMessage());
		        }
		        finally
		        {
		        	try{                    
		                if( null != reader )
		                	reader.close();
		                
		             }catch (Exception e2){ 
		                e2.printStackTrace();
		             }
		        }
			} catch (java.lang.ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				log.log(Level.ERROR, e);
			} finally {
				try {
					connection.close();
				} catch (Throwable t) {
				}
			}
		}
	}
	
	public void conectar() {
		try {
			connection = DriverManager.getConnection(connectionURL);
			if (connection == null)
				throw new Exception("Error en la conexion a la BBDD");
			log.log(Level.DEBUG, "Conexión exitosa!");
			connection.setAutoCommit(false);
		} catch (Exception ex) {
			log.log(Level.ERROR, ex.getClass().getName() + ": " + ex.getMessage());
			log.log(Level.ERROR, "Error en la conexión");
		}
	}
	
	public void desconectar()
	{
		try {
			connection.commit();
			connection.close();
        } catch (SQLException ex) {
        	log.log(Level.ERROR, ex.getClass().getName() + ": " + ex.getMessage());
        	log.log(Level.ERROR, "Error en la conexión");
        }
	}
	
	public void actualizacion(String consulta) throws Exception
	{
		PreparedStatement ps = null;
		
		try {
			ps = prepareStatement(consulta);
			ps.execute();
			
			commit();			
		} catch (SQLException e) {
			try {
				rollback();
			} catch (SQLException e1) {
				log.log(Level.ERROR, e1.getMessage());	
				log.log(Level.ERROR, e1);
			}
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
			throw new Exception("Hubo un error durante la carga de la BBDD");
		}
		finally
		{
			try {
				if(ps != null)
					ps.close();
			} catch (SQLException e) {
				log.log(Level.ERROR, e.getMessage());	
				log.log(Level.ERROR, e);
			}
		}
	}
	
	public PreparedStatement prepareStatement(String sentencia) throws SQLException
	{
		return connection.prepareStatement(sentencia);
	}
	
	public void rollback() throws SQLException
	{
		connection.rollback();
	}
	
	public void commit() throws SQLException
	{
		connection.commit();
	}
}
