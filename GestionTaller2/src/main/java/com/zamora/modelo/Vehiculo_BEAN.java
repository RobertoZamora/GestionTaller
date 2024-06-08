package com.zamora.modelo;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Level;

import com.zamora.Main;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.trazas.Trazas;

public class Vehiculo_BEAN {
	private int ID_CLIENTE;
	private int ID_VEHICULO;
	private String MATRICULA;
	private String MARCA;
	private String MODELO;
	private String COMBUSTIBLE;
	private String FICHATECNICA;
	private String BASTIDOR;
	
	public Vehiculo_BEAN() {
		super();
	}

	public Vehiculo_BEAN(int ID_CLIENTE, int ID_VEHICULO, String MATRICULA, String MARCA, String MODELO,
			String COMBUSTIBLE, String FICHATECNICA, String BASTIDOR) {
		super();
		this.ID_CLIENTE = ID_CLIENTE;
		this.ID_VEHICULO = ID_VEHICULO;
		this.MATRICULA = MATRICULA;
		this.MARCA = MARCA;
		this.MODELO = MODELO;
		this.COMBUSTIBLE = COMBUSTIBLE;
		this.FICHATECNICA = FICHATECNICA;
		this.BASTIDOR = BASTIDOR;
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

	public String getMATRICULA() {
		return MATRICULA;
	}

	public void setMATRICULA(String MATRICULA) {
		this.MATRICULA = MATRICULA;
	}

	public String getMARCA() {
		return MARCA;
	}

	public void setMARCA(String MARCA) {
		this.MARCA = MARCA;
	}

	public String getMODELO() {
		return MODELO;
	}

	public void setMODELO(String MODELO) {
		this.MODELO = MODELO;
	}

	public String getCOMBUSTIBLE() {
		return COMBUSTIBLE;
	}

	public void setCOMBUSTIBLE(String COMBUSTIBLE) {
		this.COMBUSTIBLE = COMBUSTIBLE;
	}

	public String getFICHATECNICA() {
		return FICHATECNICA;
	}

	public void setFICHATECNICA(String FICHATECNICA) {
		this.FICHATECNICA = FICHATECNICA;
	}

	public String getBASTIDOR() {
		return BASTIDOR;
	}

	public void setBASTIDOR(String bASTIDOR) {
		BASTIDOR = bASTIDOR;
	}
	
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	private final String SQL_VEHICULO = 
			" SELECT ID_CLIENTE, ID_VEHICULO, MATRICULA, MARCA, MODELO, COMBUSTIBLE, FICHA_TECNICA, BASTIDOR FROM VEHICULOS WHERE ID_VEHICULO = ? ";
		
	private final String SQL_VEHICULOS = 
			" SELECT ID_CLIENTE, ID_VEHICULO, MATRICULA, MARCA, MODELO, COMBUSTIBLE, FICHA_TECNICA, BASTIDOR FROM VEHICULOS WHERE ID_CLIENTE = ? AND ACTIVO = 'S' ";
	
	private final String SQL_EXISTE_MATRICULA =
			" SELECT * FROM VEHICULOS WHERE MATRICULA = ? AND ACTIVO = 'S' ";
	
	private final String NEXT_VAL =
			" SELECT ID_VEHICULO + 1 ID_VEHICULO FROM AUTO_INCREMENT ";
	
	private final String UPDATE_NEXT_VAL =
			" UPDATE AUTO_INCREMENT SET ID_VEHICULO = ID_VEHICULO + 1 ";
	
	private final String INSERT_VEHICULO = 
			" INSERT INTO VEHICULOS (ID_CLIENTE, ID_VEHICULO, MATRICULA, MARCA, MODELO, COMBUSTIBLE, FICHA_TECNICA, BASTIDOR) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
	
	private final String UPDATE_VEHICULO = 
			" UPDATE VEHICULOS SET ID_CLIENTE = ?, MATRICULA = ?, MARCA = ?, MODELO = ?, COMBUSTIBLE = ?, FICHA_TECNICA = ?, BASTIDOR = ? WHERE ID_VEHICULO = ?";

	private final String BORRAR_VEHICULO = 
			" DELETE FROM VEHICULOS WHERE ID_VEHICULO = ?";
	
	private final String BORRAR_LOGICO_VEHICULO = 
			" UPDATE VEHICULOS SET ACTIVO = 'N', FICHA_TECNICA = '' WHERE ID_VEHICULO = ?";
	
	private final String SQL_TODOS_VEHICULOS = 
			" SELECT ID_CLIENTE, ID_VEHICULO, MATRICULA, MARCA, MODELO, COMBUSTIBLE, FICHA_TECNICA FROM VEHICULOS ORDER BY ID_CLIENTE DESC ";
	
	private final String SQL_VEHICULO_MATRICULA = 
			" SELECT ID_CLIENTE, ID_VEHICULO, MATRICULA, MARCA, MODELO, COMBUSTIBLE, FICHA_TECNICA FROM VEHICULOS WHERE MATRICULA = ? ";
	
	private final String SQL_ID_MATRICULA =
			" SELECT ID_VEHICULO FROM VEHICULOS WHERE MATRICULA = ? ";
	
	public Vehiculo_BEAN recuperarVehiculo(ConexionBBDD db, Trazas log, int idVehiculo)
	{
		Vehiculo_BEAN vehiculo = new Vehiculo_BEAN();
		try {
			ps = db.prepareStatement(SQL_VEHICULO);
			ps.setInt(1, idVehiculo);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_VEHICULO);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				vehiculo.setID_CLIENTE(rs.getInt("ID_CLIENTE"));
				vehiculo.setID_VEHICULO(rs.getInt("ID_VEHICULO"));
				vehiculo.setMATRICULA(rs.getString("MATRICULA"));
				vehiculo.setMARCA(rs.getString("MARCA"));
				vehiculo.setMODELO(rs.getString("MODELO"));
				vehiculo.setCOMBUSTIBLE(rs.getString("COMBUSTIBLE"));
				vehiculo.setFICHATECNICA(rs.getString("FICHA_TECNICA"));
				vehiculo.setBASTIDOR(rs.getString("BASTIDOR"));
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return vehiculo;
	}
	
	public ArrayList<Vehiculo_BEAN> recuperarVehiculosCliente(ConexionBBDD db, Trazas log, int idCliente)
	{
		ArrayList<Vehiculo_BEAN> vehiculos = new ArrayList<Vehiculo_BEAN>();
		try {
			ps = db.prepareStatement(SQL_VEHICULOS);
			ps.setInt(1, idCliente);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_VEHICULOS);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				Vehiculo_BEAN aux = new Vehiculo_BEAN();
				aux.setID_CLIENTE(rs.getInt("ID_CLIENTE"));
				aux.setID_VEHICULO(rs.getInt("ID_VEHICULO"));
				aux.setMATRICULA(rs.getString("MATRICULA"));
				aux.setMARCA(rs.getString("MARCA"));
				aux.setMODELO(rs.getString("MODELO"));
				aux.setCOMBUSTIBLE(rs.getString("COMBUSTIBLE"));
				aux.setFICHATECNICA(rs.getString("FICHA_TECNICA"));
				aux.setBASTIDOR(rs.getString("BASTIDOR"));
				vehiculos.add(aux);
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return vehiculos;
	}
	
	public boolean existeMatricula(ConexionBBDD db, Trazas log, String matricula)
	{
		boolean existe = false;
		try {
			ps = db.prepareStatement(SQL_EXISTE_MATRICULA);
			ps.setString(1, matricula);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_EXISTE_MATRICULA);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			existe = rs.next();
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
		return existe;
	}
	
	public int nexVal(ConexionBBDD db, Trazas log)
	{
		int id = 0;
		try {
			ps = db.prepareStatement(NEXT_VAL);
			log.log(Level.DEBUG, "Consulta lanzada: " + NEXT_VAL);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				id = rs.getInt("ID_VEHICULO");
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
		return id;
	}
	
	public void updateNextVal(ConexionBBDD db, Trazas log)
	{
		try {
			ps = db.prepareStatement(UPDATE_NEXT_VAL);
			log.log(Level.DEBUG, "Consulta lanzada: " + UPDATE_NEXT_VAL);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
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
				try {
					db.rollback();
				} catch (SQLException e1) {
					log.log(Level.ERROR, e1.getMessage());	
					log.log(Level.ERROR, e1);
				}
				log.log(Level.ERROR, e.getMessage());	
				log.log(Level.ERROR, e);
			}
		}
	}
	
	public void insertarVehiculo(ConexionBBDD db, Trazas log)
	{
		try {
			setID_VEHICULO(nexVal(db, log));
			ps = db.prepareStatement(INSERT_VEHICULO);
			int cont = 0;
			ps.setInt(++cont, getID_CLIENTE());
			ps.setInt(++cont, getID_VEHICULO());
			ps.setString(++cont, getMATRICULA());
			ps.setString(++cont, getMARCA());
			ps.setString(++cont, getMODELO());
			ps.setString(++cont, getCOMBUSTIBLE());
			ps.setString(++cont, getFICHATECNICA());
			ps.setString(++cont, getBASTIDOR());
			log.log(Level.DEBUG, "Consulta lanzada: " + INSERT_VEHICULO);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			updateNextVal(db, log);
			
			db.commit();			
		} catch (SQLException e) {
			try {
				db.rollback();
			} catch (SQLException e1) {
				log.log(Level.ERROR, e1.getMessage());	
				log.log(Level.ERROR, e1);
			}
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
	}

	public void updateVehiculo(ConexionBBDD db, Trazas log) {
		try {
			ps = db.prepareStatement(UPDATE_VEHICULO);
			int cont = 0;
			ps.setInt(++cont, getID_CLIENTE());
			ps.setString(++cont, getMATRICULA());
			ps.setString(++cont, getMARCA());
			ps.setString(++cont, getMODELO());
			ps.setString(++cont, getCOMBUSTIBLE());
			ps.setString(++cont, getFICHATECNICA());
			ps.setString(++cont, getBASTIDOR());
			ps.setInt(++cont, getID_VEHICULO());
			log.log(Level.DEBUG, "Consulta lanzada: " + UPDATE_VEHICULO);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			db.commit();
		} catch (SQLException e) {
			try {
				db.rollback();
			} catch (SQLException e1) {
				log.log(Level.ERROR, e1.getMessage());	
				log.log(Level.ERROR, e1);
			}
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
	}

	public void borrarVehiculo(ConexionBBDD db, Trazas log, String idFiscal) {
		try {
			ps = db.prepareStatement(BORRAR_VEHICULO);
			int cont = 0;
			ps.setInt(++cont, getID_VEHICULO());
			log.log(Level.DEBUG, "Consulta lanzada: " + BORRAR_VEHICULO);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			db.commit();
			
			if(FICHATECNICA != null && FICHATECNICA.length() > 0)
			{
				File fichaTecnica = new File(Main.rutaDatos + File.separator + "archivos" + File.separator + idFiscal + File.separator + FICHATECNICA);
				if(fichaTecnica.exists())
					fichaTecnica.delete();
			}
			
			ArrayList<Albaran_BEAN> albaranes = new Albaran_BEAN().recuperarAlbaranes(db, log, ID_CLIENTE, ID_VEHICULO);
			
			for(Albaran_BEAN albaran : albaranes)
			{
				File rutaAlbaran = new File(Main.rutaDatos + File.separator + "archivos" + File.separator + idFiscal + File.separator + MATRICULA + File.separator + albaran.getRUTA());
				albaran.borrarAlbaran(db, log);
				if(rutaAlbaran.exists())
					rutaAlbaran.delete();
			}
			
		} catch (SQLException e) {
			try {
				db.rollback();
			} catch (SQLException e1) {
				log.log(Level.ERROR, e1.getMessage());	
				log.log(Level.ERROR, e1);
			}
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
	}

	public void borradoLogicoVehiculo(ConexionBBDD db, Trazas log, String idFiscal) {
		try {
			ps = db.prepareStatement(BORRAR_LOGICO_VEHICULO);
			int cont = 0;
			ps.setInt(++cont, getID_VEHICULO());
			log.log(Level.DEBUG, "Consulta lanzada: " + BORRAR_LOGICO_VEHICULO);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			db.commit();			
		} catch (SQLException e) {
			try {
				db.rollback();
			} catch (SQLException e1) {
				log.log(Level.ERROR, e1.getMessage());	
				log.log(Level.ERROR, e1);
			}
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
	}
	
	public ArrayList<Vehiculo_BEAN> recuperarTodosVehiculos(ConexionBBDD db, Trazas log)
	{
		ArrayList<Vehiculo_BEAN> vehiculos = new ArrayList<Vehiculo_BEAN>();
		try {
			ps = db.prepareStatement(SQL_TODOS_VEHICULOS);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_TODOS_VEHICULOS);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				Vehiculo_BEAN aux = new Vehiculo_BEAN();
				aux.setID_CLIENTE(rs.getInt("ID_CLIENTE"));
				aux.setID_VEHICULO(rs.getInt("ID_VEHICULO"));
				aux.setMATRICULA(rs.getString("MATRICULA"));
				aux.setMARCA(rs.getString("MARCA"));
				aux.setMODELO(rs.getString("MODELO"));
				aux.setCOMBUSTIBLE(rs.getString("COMBUSTIBLE"));
				aux.setFICHATECNICA(rs.getString("FICHA_TECNICA"));
				vehiculos.add(aux);
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return vehiculos;
	}
	
	public Vehiculo_BEAN recuperarMatricula(ConexionBBDD db, Trazas log, String matricula)
	{
		Vehiculo_BEAN vehiculo = new Vehiculo_BEAN();
		try {
			ps = db.prepareStatement(SQL_VEHICULO_MATRICULA);
			ps.setString(1, matricula);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_VEHICULO_MATRICULA);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				vehiculo.setID_CLIENTE(rs.getInt("ID_CLIENTE"));
				vehiculo.setID_VEHICULO(rs.getInt("ID_VEHICULO"));
				vehiculo.setMATRICULA(rs.getString("MATRICULA"));
				vehiculo.setMARCA(rs.getString("MARCA"));
				vehiculo.setMODELO(rs.getString("MODELO"));
				vehiculo.setCOMBUSTIBLE(rs.getString("COMBUSTIBLE"));
				vehiculo.setFICHATECNICA(rs.getString("FICHA_TECNICA"));
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return vehiculo;
	}
	
	public int recuperarIdMatricula(ConexionBBDD db, Trazas log, String matricula)
	{
		int id = 0;
		try {
			ps = db.prepareStatement(SQL_ID_MATRICULA);
			ps.setString(1, matricula);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ID_MATRICULA);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				id = rs.getInt("ID_VEHICULO");
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
		return id;
	}

	@Override
	public String toString() {
		return MATRICULA + (MODELO != null && !MODELO.equals("")?" - ":"") + MODELO;
	}
}
