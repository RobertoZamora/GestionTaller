package com.zamora.modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Level;

import com.zamora.BBDD.ConexionBBDD;
import com.zamora.trazas.Trazas;

public class ProductoGenerico_BEAN {

	private int ID_GENERICO;
	private String NOMBRE;
	private String DESCRIPCION;
	private double UNIDADES;
	private double PVP;
	private int DTO;
	private double TOTAL;
	
	public ProductoGenerico_BEAN() {
		super();
	}

	public ProductoGenerico_BEAN(String nOMBRE, String dESCRIPCION, double uNIDADES, double pVP,
			int dTO, double tOTAL) {
		super();
		NOMBRE = nOMBRE;
		DESCRIPCION = dESCRIPCION;
		UNIDADES = uNIDADES;
		PVP = pVP;
		DTO = dTO;
		TOTAL = tOTAL;
	}

	public int getID_GENERICO() {
		return ID_GENERICO;
	}

	public void setID_GENERICO(int iD_GENERICO) {
		ID_GENERICO = iD_GENERICO;
	}

	public String getNOMBRE() {
		return NOMBRE;
	}

	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}

	public String getDESCRIPCION() {
		return DESCRIPCION;
	}

	public void setDESCRIPCION(String dESCRIPCION) {
		DESCRIPCION = dESCRIPCION;
	}

	public double getUNIDADES() {
		return UNIDADES;
	}

	public void setUNIDADES(double uNIDADES) {
		UNIDADES = uNIDADES;
	}

	public double getPVP() {
		return PVP;
	}

	public void setPVP(double pVP) {
		PVP = pVP;
	}

	public int getDTO() {
		return DTO;
	}

	public void setDTO(int dTO) {
		DTO = dTO;
	}

	public double getTOTAL() {
		return TOTAL;
	}

	public void setTOTAL(double tOTAL) {
		TOTAL = tOTAL;
	}
	
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	private final String SQL_TODOS_NOMBRES =
			" SELECT NOMBRE_GENERICO FROM GENERICOS GROUP BY NOMBRE_GENERICO ";
	
	private final String SQL_RECUPERAR_GENERICO = 
			" SELECT ID_GENERICO, NOMBRE_GENERICO, DESCRIPCION, UNIDADES, PVP, DTO, TOTAL FROM GENERICOS WHERE NOMBRE_GENERICO = ? ";
	
	private final String SQL_BORRAR_GENERICO = 
			" DELETE FROM GENERICOS WHERE NOMBRE_GENERICO = ? ";
	
	private final String SQL_INSERTAR_GENERICO = 
			" INSERT INTO GENERICOS (ID_GENERICO, NOMBRE_GENERICO, DESCRIPCION, UNIDADES, PVP, DTO, TOTAL) VALUES ((SELECT COALESCE(MAX(ID_GENERICO), 0) + 1 FROM GENERICOS), ?, ?, ?, ?, ?, ?)";

	public ArrayList<String> recuperarNombresGenericos(ConexionBBDD db, Trazas log)
	{
		ArrayList<String> genericos = new ArrayList<String>();
		try {
			ps = db.prepareStatement(SQL_TODOS_NOMBRES);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_TODOS_NOMBRES);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				genericos.add(rs.getString("NOMBRE_GENERICO"));
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return genericos;
	}
	
	public ArrayList<ProductoGenerico_BEAN> recuperarGenerico(ConexionBBDD db, Trazas log, String nombreGenerico)
	{
		ArrayList<ProductoGenerico_BEAN> generico = new ArrayList<ProductoGenerico_BEAN>();
		try {
			ps = db.prepareStatement(SQL_RECUPERAR_GENERICO);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_GENERICO);
			
			long inicio = System.currentTimeMillis();
			ps.setString(1, nombreGenerico);
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				ProductoGenerico_BEAN aux = new ProductoGenerico_BEAN();
				aux.setID_GENERICO(rs.getInt("ID_GENERICO"));
				aux.setNOMBRE(rs.getString("NOMBRE_GENERICO"));
				aux.setDESCRIPCION(rs.getString("DESCRIPCION"));
				aux.setUNIDADES(rs.getDouble("UNIDADES"));
				aux.setPVP(rs.getDouble("PVP"));
				aux.setDTO(rs.getInt("DTO"));
				aux.setTOTAL(rs.getDouble("TOTAL"));
				generico.add(aux);
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return generico;
	}

	public void borrarGenerico(ConexionBBDD db, Trazas log, String generico)
	{
		try {
			ps = db.prepareStatement(SQL_BORRAR_GENERICO);
			int cont = 0;
			ps.setString(++cont, generico);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_BORRAR_GENERICO);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );

			db.commit();	
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
	}
	
	public void insertarGenerico(ConexionBBDD db, Trazas log)
	{
		try {
			ps = db.prepareStatement(SQL_INSERTAR_GENERICO);
			int cont = 0;
			ps.setString(++cont, getNOMBRE());
			ps.setString(++cont, getDESCRIPCION());
			ps.setDouble(++cont, getUNIDADES());
			ps.setDouble(++cont, getPVP());
			ps.setInt(++cont, getDTO());
			ps.setDouble(++cont, getTOTAL());
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_INSERTAR_GENERICO);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );

			db.commit();	
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
	}

	@Override
	public String toString() {
		return NOMBRE;
	}
}
