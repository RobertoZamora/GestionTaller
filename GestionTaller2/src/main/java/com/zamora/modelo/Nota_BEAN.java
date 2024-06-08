package com.zamora.modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Level;

import com.zamora.BBDD.ConexionBBDD;
import com.zamora.trazas.Trazas;

public class Nota_BEAN {
	int ID_CLIENTE;
	int ID_NOTA;
	String NOTA;
	String NOMBRE;
	boolean IMPORTANTE = false;
	boolean PRINCIPAL = false;
	
	public Nota_BEAN() {
		super();
	}

	public Nota_BEAN(int ID_CLIENTE, int ID_NOTA, String NOTA, String NOMBRE, boolean IMPORTANTE, boolean PRINCIPAL) {
		super();
		this.ID_CLIENTE = ID_CLIENTE;
		this.ID_NOTA = ID_NOTA;
		this.NOTA = NOTA;
		this.NOMBRE = NOMBRE;
		this.IMPORTANTE = IMPORTANTE;
		this.PRINCIPAL = PRINCIPAL;
	}

	public int getID_CLIENTE() {
		return ID_CLIENTE;
	}

	public void setID_CLIENTE(int ID_CLIENTE) {
		this.ID_CLIENTE = ID_CLIENTE;
	}

	public int getID_NOTA() {
		return ID_NOTA;
	}

	public void setID_NOTA(int ID_NOTA) {
		this.ID_NOTA = ID_NOTA;
	}

	public String getNOTA() {
		return NOTA;
	}

	public void setNOTA(String NOTA) {
		this.NOTA = NOTA;
	}

	public String getNOMBRE() {
		return NOMBRE;
	}

	public void setNOMBRE(String NOMBRE) {
		this.NOMBRE = NOMBRE;
	}

	public boolean isIMPORTANTE() {
		return IMPORTANTE;
	}

	public void setIMPORTANTE(boolean IMPORTANTE) {
		this.IMPORTANTE = IMPORTANTE;
	}

	public boolean isPRINCIPAL() {
		return PRINCIPAL;
	}

	public void setPRINCIPAL(boolean PRINCIPAL) {
		this.PRINCIPAL = PRINCIPAL;
	}
	
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	private final String SQL_RECUPERAR_NOTAS = 
			" SELECT ID_NOTA, ID_CLIENTE, NOTA, NOMBRE, IMPORTANTE, PRINCIPAL FROM NOTAS WHERE ID_CLIENTE = ? ORDER BY ID_NOTA ";
	
	private final String SQL_INSERTAR_NOTAS = 
			" INSERT INTO NOTAS (ID_NOTA, ID_CLIENTE, NOTA, NOMBRE, IMPORTANTE, PRINCIPAL) VALUES (?, ?, ?, ?, ?, ?) ";
	
	private final String SQL_ACTUALIZAR_NOTA = 
			" UPDATE NOTAS SET NOTA = ?, IMPORTANTE = ?, PRINCIPAL = ? WHERE ID_NOTA = ? AND ID_CLIENTE = ? ";
	
	private final String SQL_ACTUALIZAR_PRINCIPAL = 
			" UPDATE NOTAS SET PRINCIPAL = false WHERE ID_CLIENTE = ? ";
	
	private final String SQL_BORRAR_NOTAS = 
			" DELETE FROM NOTAS WHERE ID_NOTA = ? AND ID_CLIENTE = ? ";
	
	private final String SQL_TIENE_NOTA_IMPORTANTE =
			" SELECT ID_CLIENTE, ID_NOTA, NOMBRE, NOTA, IMPORTANTE FROM NOTAS WHERE ID_CLIENTE = ? AND IMPORTANTE = true ";
	
	private final String SQL_TIENE_NOTA_PRINCIPAL =
			" SELECT ID_CLIENTE, ID_NOTA, NOMBRE, NOTA, IMPORTANTE FROM NOTAS WHERE ID_CLIENTE = ? AND PRINCIPAL = true ";
	
	public ArrayList<Nota_BEAN> recuperarNotasCliente(ConexionBBDD db, Trazas log, int idCliente)
	{
		ArrayList<Nota_BEAN> notas = new ArrayList<Nota_BEAN>();
		try {
			ps = db.prepareStatement(SQL_RECUPERAR_NOTAS);
			ps.setInt(1, idCliente);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_NOTAS);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				Nota_BEAN aux = new Nota_BEAN();
				aux.setID_NOTA(rs.getInt("ID_NOTA"));
				aux.setID_CLIENTE(rs.getInt("ID_CLIENTE"));
				aux.setNOTA(rs.getString("NOTA"));
				aux.setNOMBRE(rs.getString("NOMBRE"));
				aux.setIMPORTANTE(rs.getBoolean("IMPORTANTE"));
				aux.setPRINCIPAL(rs.getBoolean("PRINCIPAL"));
				notas.add(aux);
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return notas;
	}
	
	public void insertarNotaCliente(ConexionBBDD db, Trazas log)
	{
		try {
			ps = db.prepareStatement(SQL_INSERTAR_NOTAS);
			ps.setInt(1, getID_NOTA());
			ps.setInt(2, getID_CLIENTE());
			ps.setString(3, getNOTA());
			ps.setString(4, getNOMBRE());
			ps.setBoolean(5, isIMPORTANTE());
			ps.setBoolean(6, isPRINCIPAL());
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_INSERTAR_NOTAS);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
	}
	
	public void actualizarNotaCliente(ConexionBBDD db, Trazas log)
	{
		try {
			if(isPRINCIPAL())
				actualizarPrincipalCliente(db, log);
			
			ps = db.prepareStatement(SQL_ACTUALIZAR_NOTA);
			ps.setString(1, getNOTA());
			ps.setBoolean(2, isIMPORTANTE());
			ps.setBoolean(3, isPRINCIPAL());
			ps.setInt(4, getID_NOTA());
			ps.setInt(5, getID_CLIENTE());
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ACTUALIZAR_NOTA);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
	}
	
	public void actualizarPrincipalCliente(ConexionBBDD db, Trazas log)
	{
		try {
			ps = db.prepareStatement(SQL_ACTUALIZAR_PRINCIPAL);
			ps.setInt(1, getID_CLIENTE());
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ACTUALIZAR_PRINCIPAL);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
	}
	
	public void borrarNotaCliente(ConexionBBDD db, Trazas log)
	{
		try {
			ps = db.prepareStatement(SQL_BORRAR_NOTAS);
			ps.setInt(1, getID_NOTA());
			ps.setInt(2, getID_CLIENTE());
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_BORRAR_NOTAS);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
	}
	
	public String clienteConNotaImportante(ConexionBBDD db, Trazas log, int idCliente)
	{
		String notas = "";
		try {
			ps = db.prepareStatement(SQL_TIENE_NOTA_IMPORTANTE);
			ps.setInt(1, idCliente);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_TIENE_NOTA_IMPORTANTE);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				notas = rs.getString("NOTA");
			}
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		finally
		{
			try {
				if(ps != null)
					ps.close();
				if(rs != null)
					rs.close();
			} catch (SQLException e) {
				log.log(Level.ERROR, e.getMessage());	
				log.log(Level.ERROR, e);
			}
		}
		log.log(Level.DEBUG, "¿Tiene nota importante? " + (notas.length() > 0?"SI":"NO"));
		return notas;
	}
	
	public String clienteConNotaPrincipal(ConexionBBDD db, Trazas log, int idCliente)
	{
		String notas = "";
		try {
			ps = db.prepareStatement(SQL_TIENE_NOTA_PRINCIPAL);
			ps.setInt(1, idCliente);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_TIENE_NOTA_PRINCIPAL);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				notas = rs.getString("NOTA");
			}
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		finally
		{
			try {
				if(ps != null)
					ps.close();
				if(rs != null)
					rs.close();
			} catch (SQLException e) {
				log.log(Level.ERROR, e.getMessage());	
				log.log(Level.ERROR, e);
			}
		}
		log.log(Level.DEBUG, "¿Tiene nota principal? " + (notas.length() > 0?"SI":"NO"));
		return notas;
	}

	@Override
	public String toString() {
		return NOMBRE;
	}
}
