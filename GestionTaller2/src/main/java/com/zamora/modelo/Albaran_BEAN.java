package com.zamora.modelo;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Level;

import com.zamora.BBDD.ConexionBBDD;
import com.zamora.trazas.Trazas;

public class Albaran_BEAN {

	private int ID_ALBARAN;
	private int ID_CLIENTE;
	private int ID_VEHICULO;
	private String FECHA;
	private String RUTA;
	
	public Albaran_BEAN() {
		super();
	}

	public Albaran_BEAN(int ID_ALBARAN, int ID_CLIENTE, int ID_VEHICULO, String FECHA, String RUTA) {
		super();
		this.ID_ALBARAN = ID_ALBARAN;
		this.ID_CLIENTE = ID_CLIENTE;
		this.ID_VEHICULO = ID_VEHICULO;
		this.FECHA = FECHA;
		this.RUTA = RUTA;
	}

	public int getID_ALBARAN() {
		return ID_ALBARAN;
	}

	public void setID_ALBARAN(int ID_ALBARAN) {
		this.ID_ALBARAN = ID_ALBARAN;
	}

	public int getID_CLIENTE() {
		return ID_CLIENTE;
	}

	public void setID_CLIENTE(int ID_CLIENTE) {
		this.ID_CLIENTE = ID_CLIENTE;
	}

	public int getID_VEHICULO() {
		return ID_VEHICULO;
	}

	public void setID_VEHICULO(int ID_VEHICULO) {
		this.ID_VEHICULO = ID_VEHICULO;
	}

	public String getFECHA() {
		return FECHA;
	}

	public void setFECHA(String FECHA) {
		this.FECHA = FECHA;
	}

	public String getRUTA() {
		return RUTA;
	}

	public void setRUTA(String RUTA) {
		this.RUTA = RUTA;
	}
	
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	private final String SQL_ALBARANES =
			" SELECT ID_ALBARAN, FECHA, RUTA FROM ALBARANES WHERE ID_CLIENTE = ? AND ID_VEHICULO = ? ORDER BY ID_ALBARAN ";
	
	private final String SQL_INSERT_ALBARANES =
			" INSERT INTO ALBARANES(ID_ALBARAN, ID_CLIENTE, ID_VEHICULO, FECHA, RUTA) VALUES (?, ?, ?, ?, ?) ";
	
	private final String SQL_BORRAR_ALBARAN =
			" DELETE FROM ALBARANES WHERE ID_ALBARAN = ? AND ID_CLIENTE = ? AND ID_VEHICULO = ? ";

	public ArrayList<Albaran_BEAN> recuperarAlbaranes(ConexionBBDD db, Trazas log, int idCliente, int idVehiculo)
	{
		ArrayList<Albaran_BEAN> albaranes = new ArrayList<Albaran_BEAN>();
		try {
			ps = db.prepareStatement(SQL_ALBARANES);
			ps.setInt(1, idCliente);
			ps.setInt(2, idVehiculo);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ALBARANES);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				Albaran_BEAN aux = new Albaran_BEAN();
				aux.setID_ALBARAN(rs.getInt("ID_ALBARAN"));
				aux.setID_CLIENTE(idCliente);
				aux.setID_VEHICULO(idVehiculo);
				aux.setFECHA(rs.getString("FECHA"));
				aux.setRUTA(rs.getString("RUTA"));
				albaranes.add(aux);
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return albaranes;
	}

	public void guardarAlbaran(ConexionBBDD db, Trazas log)
	{
		try {
			ps = db.prepareStatement(SQL_INSERT_ALBARANES);
			ps.setInt(1, getID_ALBARAN());
			ps.setInt(2, getID_CLIENTE());
			ps.setInt(3, getID_VEHICULO());
			ps.setString(4, getFECHA());
			ps.setString(5, getRUTA());
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_INSERT_ALBARANES);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
	}

	public void borrarAlbaran(ConexionBBDD db, Trazas log)
	{
		try {
			ps = db.prepareStatement(SQL_BORRAR_ALBARAN);
			ps.setInt(1, getID_ALBARAN());
			ps.setInt(2, getID_CLIENTE());
			ps.setInt(3, getID_VEHICULO());
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_BORRAR_ALBARAN);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
	}
	
	@Override
	public String toString() {
		return RUTA.substring(RUTA.lastIndexOf(File.separator) + 1);
	}
	
}
