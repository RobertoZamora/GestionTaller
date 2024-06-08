package com.zamora.BBDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Level;

import com.zamora.trazas.Trazas;

public class ConexionAccess {
	private Connection conexion;
	private Trazas log;
	private String connectionURL = "";

	public ConexionAccess(Trazas log, String rutaBaseDatos) throws SQLException
	{
		this.log = log;
		connectionURL = "jdbc:ucanaccess://" + rutaBaseDatos;
		
		try {
            // Establece el controlador JDBC de UCanAccess (net.ucanaccess.jdbc.UcanaccessDriver)
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de excepciones aquí
        }
	}
	
	public Connection obtenerConexion() {
		return conexion;
    }
	
	public void conectar() {
		try {
			conexion = DriverManager.getConnection(connectionURL);
			if (conexion == null)
				throw new Exception("Error en la conexion a la BBDD");
			log.log(Level.DEBUG, "Conexión exitosa!");
			conexion.setAutoCommit(false);
		} catch (Exception ex) {
			log.log(Level.ERROR, ex.getClass().getName() + ": " + ex.getMessage());
			log.log(Level.ERROR, "Error en la conexión");
		}
	}
	
	public void desconectar()
	{
		try {
			conexion.commit();
			conexion.close();
        } catch (SQLException ex) {
        	log.log(Level.ERROR, ex.getClass().getName() + ": " + ex.getMessage());
        	log.log(Level.ERROR, "Error en la conexión");
        }
	}
	
	public PreparedStatement prepareStatement(String sentencia) throws SQLException
	{
		return conexion.prepareStatement(sentencia);
	}
	
	public void rollback() throws SQLException
	{
		conexion.rollback();
	}
	
	public void commit() throws SQLException
	{
		conexion.commit();
	}
	
}
