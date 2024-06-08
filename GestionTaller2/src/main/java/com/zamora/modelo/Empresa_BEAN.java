package com.zamora.modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Level;

import com.zamora.BBDD.ConexionBBDD;
import com.zamora.trazas.Trazas;

public class Empresa_BEAN {

	private int idEmpresa;
	private String empresa;
	private String nombre;
	private String direccion;
	private String telefono;
	private String cif;
	private String codigoPostal;
	private String municipio;
	private String provincia;
	private boolean predefinida;
	
	public Empresa_BEAN() {
		super();
	}

	public Empresa_BEAN(int idEmpresa, String empresa, String nombre, String direccion, String telefono, String cif,
			String codigoPostal, String municipio, String provincia, boolean predefinida) {
		super();
		this.idEmpresa = idEmpresa;
		this.empresa = empresa;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		this.cif = cif;
		this.codigoPostal = codigoPostal;
		this.municipio = municipio;
		this.provincia = provincia;
		this.predefinida = predefinida;
	}

	public int getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public boolean isPredefinida() {
		return predefinida;
	}

	public void setPredefinida(boolean predefinida) {
		this.predefinida = predefinida;
	}
	
	@Override
	public String toString() {
		return empresa;
	}

	PreparedStatement ps = null;
	ResultSet rs = null;
	
	private String SQL_RECUPERAR_DATOS_EMPRESAS =
			" SELECT ID_EMPRESA, EMPRESA, NOMBRE, DIRECCION, TELEFONO, CIF, CODIGOPOSTA, MUNICIPIO, PROVINCIA, PREDEFINIDA FROM DATOSEMPRESA ";
	
	private String SQL_MAX_ID_EMPRESA = 
			" SELECT (MAX(ID_EMPRESA) + 1) MAXIMO FROM DATOSEMPRESA ";
	
	private String INSERT_DATOS_EMPRESA = 
			" INSERT INTO DATOSEMPRESA (ID_EMPRESA, EMPRESA, NOMBRE, DIRECCION, TELEFONO, CIF, CODIGOPOSTA, MUNICIPIO, PROVINCIA, ID_FACTURA, PREDEFINIDA) VALUES ((SELECT (COALESCE(MAX(ID_EMPRESA), 0) + 1) FROM DATOSEMPRESA), ?, ?, ?, ?, ?, ?, ?, ?, 0, ?) ";
	
	private String UPDATE_RESET_PREDEFINIDA = 
			" UPDATE DATOSEMPRESA SET PREDEFINIDA = FALSE ";
	
	private String UPDATE_DATOS_EMPRESA = 
			" UPDATE DATOSEMPRESA SET EMPRESA = ?, NOMBRE = ?, DIRECCION = ?, TELEFONO = ?, CIF = ?, CODIGOPOSTA = ?, MUNICIPIO = ?, PROVINCIA = ?, PREDEFINIDA = ? WHERE ID_EMPRESA = ? ";
	
	private String SQL_EMPRESA_PREDEFINIDA = 
			" SELECT ID_EMPRESA, EMPRESA, NOMBRE, DIRECCION, TELEFONO, CIF, CODIGOPOSTA, MUNICIPIO, PROVINCIA, PREDEFINIDA FROM DATOSEMPRESA WHERE PREDEFINIDA = TRUE ";

	private String SQL_RECUPERAR_DATOS_EMPRESA =
			" SELECT ID_EMPRESA, EMPRESA, NOMBRE, DIRECCION, TELEFONO, CIF, CODIGOPOSTA, MUNICIPIO, PROVINCIA, PREDEFINIDA FROM DATOSEMPRESA WHERE ID_EMPRESA = ? ";
	
	public ArrayList<Empresa_BEAN> getEmrpesas(ConexionBBDD db, Trazas log)
	{
		ArrayList<Empresa_BEAN> empresas = new ArrayList<Empresa_BEAN>();
		try {
			ps = db.prepareStatement(SQL_RECUPERAR_DATOS_EMPRESAS);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_DATOS_EMPRESAS);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				Empresa_BEAN emp = new Empresa_BEAN();
				emp.setIdEmpresa(rs.getInt("ID_EMPRESA"));
				emp.setEmpresa(rs.getString("EMPRESA"));
				emp.setNombre(rs.getString("NOMBRE"));
				emp.setDireccion(rs.getString("DIRECCION"));
				emp.setTelefono(rs.getString("TELEFONO"));
				emp.setCif(rs.getString("CIF"));
				emp.setCodigoPostal(rs.getString("CODIGOPOSTA"));
				emp.setMunicipio(rs.getString("MUNICIPIO"));
				emp.setProvincia(rs.getString("PROVINCIA"));
				emp.setPredefinida(rs.getBoolean("PREDEFINIDA"));
				empresas.add(emp);
			}
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (empresas): " + empresas.size());
		return empresas;	
	}
	
	public int maxIdEmpresas(ConexionBBDD db, Trazas log)
	{
		int maxIdEmpresa = 0;
		try {
			ps = db.prepareStatement(SQL_MAX_ID_EMPRESA);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_MAX_ID_EMPRESA);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				maxIdEmpresa = rs.getInt("MAXIMO");
			}
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (maxIdEmpresas): " + maxIdEmpresa);
		return maxIdEmpresa;	
	}
	
	public void insertarEmpresa(ConexionBBDD db, Trazas log)
	{
		try {
			ps = db.prepareStatement(INSERT_DATOS_EMPRESA);
			int cont = 0;
			ps.setString(++cont, getEmpresa());
			ps.setString(++cont, getNombre());
			ps.setString(++cont, getDireccion());
			ps.setString(++cont, getTelefono());
			ps.setString(++cont, getCif());
			ps.setString(++cont, getCodigoPostal());
			ps.setString(++cont, getMunicipio());
			ps.setString(++cont, getProvincia());
			ps.setBoolean(++cont, isPredefinida());
			log.log(Level.DEBUG, "Consulta lanzada: " + INSERT_DATOS_EMPRESA);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			
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
	}
	
	public void actualizarEmpresa(ConexionBBDD db, Trazas log)
	{
		try {

			long inicio = 0;
			long fin = 0;
			if(isPredefinida())
			{
				ps = db.prepareStatement(UPDATE_RESET_PREDEFINIDA);
				log.log(Level.DEBUG, "Consulta lanzada: " + UPDATE_DATOS_EMPRESA);

				inicio = System.currentTimeMillis();
				ps.executeUpdate();
				fin = System.currentTimeMillis();
				log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
				ps.close();
			}
			
			ps = db.prepareStatement(UPDATE_DATOS_EMPRESA);
			int cont = 0;
			ps.setString(++cont, getEmpresa());
			ps.setString(++cont, getNombre());
			ps.setString(++cont, getDireccion());
			ps.setString(++cont, getTelefono());
			ps.setString(++cont, getCif());
			ps.setString(++cont, getCodigoPostal());
			ps.setString(++cont, getMunicipio());
			ps.setString(++cont, getProvincia());
			ps.setBoolean(++cont, isPredefinida());
			ps.setInt(++cont, getIdEmpresa());
			log.log(Level.DEBUG, "Consulta lanzada: " + UPDATE_DATOS_EMPRESA);
			
			inicio = System.currentTimeMillis();
			ps.executeUpdate();
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			
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
	}
	

	
	public Empresa_BEAN recuperarDatosEmpresaPredefinida(ConexionBBDD db, Trazas log)
	{
		Empresa_BEAN emp= null;
		try {
			ps = db.prepareStatement(SQL_EMPRESA_PREDEFINIDA);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_EMPRESA_PREDEFINIDA);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				emp = new Empresa_BEAN();
				emp.setIdEmpresa(rs.getInt("ID_EMPRESA"));
				emp.setEmpresa(rs.getString("EMPRESA"));
				emp.setNombre(rs.getString("NOMBRE"));
				emp.setDireccion(rs.getString("DIRECCION"));
				emp.setTelefono(rs.getString("TELEFONO"));
				emp.setCif(rs.getString("CIF"));
				emp.setCodigoPostal(rs.getString("CODIGOPOSTA"));
				emp.setMunicipio(rs.getString("MUNICIPIO"));
				emp.setProvincia(rs.getString("PROVINCIA"));
				emp.setPredefinida(rs.getBoolean("PREDEFINIDA"));
			}
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (emp): " + emp);
		return emp;
	}
	
	public Empresa_BEAN recuperarDatosEmpresa(ConexionBBDD db, Trazas log, int idEmpresa)
	{
		Empresa_BEAN emp= null;
		try {
			ps = db.prepareStatement(SQL_RECUPERAR_DATOS_EMPRESA);
			ps.setInt(1, idEmpresa);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_DATOS_EMPRESA);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				emp = new Empresa_BEAN();
				emp.setIdEmpresa(rs.getInt("ID_EMPRESA"));
				emp.setEmpresa(rs.getString("EMPRESA"));
				emp.setNombre(rs.getString("NOMBRE"));
				emp.setDireccion(rs.getString("DIRECCION"));
				emp.setTelefono(rs.getString("TELEFONO"));
				emp.setCif(rs.getString("CIF"));
				emp.setCodigoPostal(rs.getString("CODIGOPOSTA"));
				emp.setMunicipio(rs.getString("MUNICIPIO"));
				emp.setProvincia(rs.getString("PROVINCIA"));
				emp.setPredefinida(rs.getBoolean("PREDEFINIDA"));
			}
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (emp): " + emp);
		return emp;
	}
}
