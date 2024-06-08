package com.zamora.modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

import com.zamora.BBDD.ConexionBBDD;
import com.zamora.trazas.Trazas;

public class Cliente_BEAN {
	private int ID_CLIENTE;
	private String IDFISCAL;
	private	String NOMBRE;
	private String CODIGOPOSTAL;
	private String LOCALIDAD;
	private String PROVINCIA;
	private String DIRECCION;
	private String PORTAL;
	private Integer PISO;
	private String LETRA;
	private Integer TELEFONO;
	private Integer MOVIL;
	private String EMAIL;
	private List<Vehiculo_BEAN> VEHICULOS;
	
	public Cliente_BEAN()
	{
		
	}
	
	public Cliente_BEAN(int ID_CLIENTE, String IDFISCAL, String NOMBRE, String CODIGOPOSTAL, String LOCALIDAD, String PROVINCIA,
			String DIRECCION, String PORTAL, int PISO, String LETRA, int TELEFONO, int MOVIL, List<Vehiculo_BEAN> VEHICULOS,
			String EMAIL) {
		super();
		this.ID_CLIENTE = ID_CLIENTE;
		this.IDFISCAL = IDFISCAL;
		this.NOMBRE = NOMBRE;
		this.CODIGOPOSTAL = CODIGOPOSTAL;
		this.LOCALIDAD = LOCALIDAD;
		this.PROVINCIA = PROVINCIA;
		this.PORTAL = PORTAL;
		this.PISO = PISO;
		this.LETRA = LETRA;
		this.DIRECCION = DIRECCION;
		this.TELEFONO = TELEFONO;
		this.MOVIL = MOVIL;
		this.VEHICULOS = VEHICULOS;
		this.EMAIL = EMAIL;
	}
	
	public int getID_CLIENTE() {
		return ID_CLIENTE;
	}
	
	public void setID_CLIENTE(int ID_CLIENTE) {
		this.ID_CLIENTE = ID_CLIENTE;
	}
	
	public String getIDFISCAL() {
		return IDFISCAL;
	}
	
	public void setIDFISCAL(String IDFISCAL) {
		this.IDFISCAL = IDFISCAL;
	}
	
	public String getNOMBRE() {
		return NOMBRE;
	}
	
	public void setNOMBRE(String NOMBRE) {
		this.NOMBRE = NOMBRE;
	}
	
	public String getDIRECCION() {
		return DIRECCION;
	}
	
	public void setDIRECCION(String DIRECCION) {
		this.DIRECCION = DIRECCION;
	}
	
	public String getLOCALIDAD() {
		return LOCALIDAD;
	}
	
	public void setLOCALIDAD(String LOCALIDAD) {
		this.LOCALIDAD = LOCALIDAD;
	}
	
	public String getPROVINCIA() {
		return PROVINCIA;
	}
	
	public void setPROVINCIA(String PROVINCIA) {
		this.PROVINCIA = PROVINCIA;
	}
	
	public Integer getTELEFONO() {
		return TELEFONO;
	}
	
	public void setTELEFONO(Integer TELEFONO) {
		this.TELEFONO = TELEFONO;
	}
	
	public Integer getMOVIL() {
		if(MOVIL != null && MOVIL == 0)
			return null;
		else
			return MOVIL;
	}
	
	public void setMOVIL(Integer MOVIL) {
		this.MOVIL = MOVIL;
	}
	
	public List<Vehiculo_BEAN> getVEHICULOS() {
		return VEHICULOS;
	}
	
	public void setVEHICULOS(List<Vehiculo_BEAN> VEHICULOS) {
		this.VEHICULOS = VEHICULOS;
	}
		
	public String getCODIGOPOSTAL() {
		return CODIGOPOSTAL;
	}

	public void setCODIGOPOSTAL(String CODIGOPOSTAL) {
		this.CODIGOPOSTAL = CODIGOPOSTAL;
	}

	public String getPORTAL() {
		return PORTAL;
	}

	public void setPORTAL(String PORTAL) {
		this.PORTAL = PORTAL;
	}

	public Integer getPISO() {
		if(PISO != null && PISO == 0)
			return null;
		else
			return PISO;
	}

	public void setPISO(Integer PISO) {
		this.PISO = PISO;
	}

	public String getLETRA() {
		return LETRA;
	}

	public void setLETRA(String LETRA) {
		this.LETRA = LETRA;
	}

	public String getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(String EMAIL) {
		this.EMAIL = EMAIL;
	}

	@Override
	public String toString() {
		return IDFISCAL + " - " + NOMBRE;
	}
	
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	private final String SQL_ULTIMO_ID_CLIENTE = 
			" SELECT MAX(ID_CLIENTE) ID_CLIENTE FROM CLIENTES WHERE ACTIVO = 'S' ";
	
	private final String SQL_PRIMER_ID = 
			" SELECT MIN(ID_CLIENTE) ID_CLIENTE " + 
			" FROM CLIENTES " + 
			" WHERE ACTIVO = 'S' ";
	
	private final String SQL_NUM_CLIENTES = 
			" SELECT COUNT(*) TOTAL FROM CLIENTES WHERE ACTIVO = 'S' ";
	
	private final String SQL_CLIENTE = 
			" SELECT ID_CLIENTE, IDFISCAL, NOMBRE, CODIGO_POSTAL, LOCALIDAD, PROVINCIA, DIRECCION, PORTAL, PISO, LETRA, TELEFONO, MOVIL, EMAIL " + 
			" FROM CLIENTES " +
			" WHERE ID_CLIENTE = ? " + 
				" AND ACTIVO = 'S' ";
	
	private final String SQL_CLIENTE_DATOS_DOC = 
			" SELECT ID_CLIENTE, IDFISCAL, NOMBRE, CODIGO_POSTAL, LOCALIDAD, PROVINCIA, DIRECCION, PORTAL, PISO, LETRA, TELEFONO, MOVIL, EMAIL " + 
			" FROM CLIENTES " +
			" WHERE ID_CLIENTE = ? ";
	
	private final String SQL_ANTERIOR_ID =
			" SELECT ID_CLIENTE FROM CLIENTES WHERE ACTIVO = 'S' ORDER BY ID_CLIENTE ASC";

	private final String SQL_SIGUIENTE_ID =
			" SELECT ID_CLIENTE FROM CLIENTES WHERE ACTIVO = 'S' ORDER BY ID_CLIENTE DESC";
	
	private final String SQL_NEXT_ID = 
			" SELECT (ID_CLIENTE + 1) ID_CLIENTE FROM AUTO_INCREMENT ";
	
	private final String SQL_EXISTE_ID_FISCAL = 
			" SELECT COUNT(*) TOTAL FROM CLIENTES WHERE IDFISCAL = ? AND ACTIVO = 'S' ";
	
	private final String SQL_UPDATE_ID_CLIENTE =
			" UPDATE AUTO_INCREMENT SET ID_CLIENTE = ? ";
	
	private final String SQL_INSERTAR_CLIENTE =
			"INSERT INTO CLIENTES (ID_CLIENTE, IDFISCAL, NOMBRE, CODIGO_POSTAL, LOCALIDAD, PROVINCIA, DIRECCION, PORTAL, PISO, LETRA, TELEFONO, MOVIL, EMAIL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private final String SQL_ACTUALIZAR_CLIENTE = 
			" UPDATE CLIENTES " + 
			" SET IDFISCAL = ?, " + 
				" NOMBRE = ?, " +
				" CODIGO_POSTAL = ?, " +
				" LOCALIDAD = ?, " +
				" PROVINCIA = ?, " +
				" DIRECCION = ?, " +
				" PORTAL = ?, " +
				" PISO = ?, " +
				" LETRA = ?, " +				
				" TELEFONO = ?, " +
				" MOVIL = ?, " +
				" EMAIL = ? " +
			" WHERE ID_CLIENTE = ? ";
	
	private final String SQL_ELIMINAR_CLIENTE = 
			" DELETE FROM CLIENTES WHERE ID_CLIENTE = ? ";
	
	private final String SQL_ELIMINAR_LOGICO_CLIENTE = 
			" UPDATE CLIENTES SET ACTIVO = 'N' WHERE ID_CLIENTE = ? ";
	
	private final String SQL_CLIENTES = 
			" SELECT ID_CLIENTE, IDFISCAL, NOMBRE, CODIGO_POSTAL, LOCALIDAD, PROVINCIA, DIRECCION, PORTAL, PISO, LETRA, TELEFONO, MOVIL, EMAIL " + 
			" FROM CLIENTES WHERE ACTIVO = 'S' ORDER BY NOMBRE";
	
	private final String SQL_CLIENTES_BUSQUEDA = 
			" SELECT ID_CLIENTE, IDFISCAL, NOMBRE, CODIGO_POSTAL, LOCALIDAD, PROVINCIA, DIRECCION, PORTAL, PISO, LETRA, TELEFONO, MOVIL, EMAIL " + 
			" FROM CLIENTES CLI ";	
	
	private final String SQL_CLIENTE_MATRICULA =
			" SELECT CL.ID_CLIENTE, CL.IDFISCAL, CL.NOMBRE, CL.CODIGO_POSTAL, CL.LOCALIDAD, CL.PROVINCIA, CL.DIRECCION, CL.PORTAL, CL.PISO, CL.LETRA, CL.TELEFONO, CL.MOVIL, CL.EMAIL " +
			" FROM CLIENTES CL, VEHICULOS VH " + 
			" WHERE CL.ID_CLIENTE = VH.ID_CLIENTE " + 
				" AND MATRICULA = ? " +
				" AND CL.ACTIVO = 'S' " + 
				" AND VH.ACTIVO = 'S' ";
	
	private final String SQL_CLIENTE_IDFISCAL = 
			" SELECT ID_CLIENTE, IDFISCAL, NOMBRE, CODIGO_POSTAL, LOCALIDAD, PROVINCIA, DIRECCION, PORTAL, PISO, LETRA, TELEFONO, MOVIL, EMAIL " + 
			" FROM CLIENTES " + 
			" WHERE IDFISCAL = ? " + 
				" AND ACTIVO = 'S' ";
	
	private final String SQL_ID_IDFISCAL =
			" SELECT ID_CLIENTE FROM CLIENTES WHERE IDFISCAL = ? AND ACTIVO = 'S' ";
	
	private final String SQL_DEUDA_CLIENTE =
			" SELECT DEUDA FROM CLIENTES WHERE ID_CLIENTE = ? ";
	
	private final String SQL_UPDATE_DEUDA_CLIENTE =
			" UPDATE CLIENTES SET DEUDA = ? WHERE ID_CLIENTE = ? ";
	
	public int recuperarIdUltimoCliente(ConexionBBDD db, Trazas log)
	{
		int ultimoId = 0;
		try {
			ps = db.prepareStatement(SQL_ULTIMO_ID_CLIENTE);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ULTIMO_ID_CLIENTE);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				ultimoId = rs.getInt("ID_CLIENTE");
			}
			log.log(Level.DEBUG, "Dato recuperado (ultimoId): " + ultimoId);			
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return ultimoId;
	}
	
	public int recupererPrimerIdcliente(ConexionBBDD db, Trazas log)
	{
		int primerIdCliente = -1;
		try {
			ps = db.prepareStatement(SQL_PRIMER_ID);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_PRIMER_ID);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				primerIdCliente = rs.getInt("ID_CLIENTE");
			}
			log.log(Level.DEBUG, "Dato recuperado (primerIdCliente): " + primerIdCliente);
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return primerIdCliente;
	}
	
	public int recupererNumclientes(ConexionBBDD db, Trazas log)
	{
		int numclientes = 0;
		try {
			ps = db.prepareStatement(SQL_NUM_CLIENTES);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_NUM_CLIENTES);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				numclientes = rs.getInt("TOTAL");
			}
			log.log(Level.DEBUG, "Dato recuperado (numclientes): " + numclientes);	
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return numclientes;
	}
	
	public Cliente_BEAN recuperarCliente(ConexionBBDD db, Trazas log, int idCliente)
	{
		Cliente_BEAN cliente = new Cliente_BEAN();
		
		try {
			ps = db.prepareStatement(SQL_CLIENTE);
			ps.setInt(1, idCliente);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_CLIENTE);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				cliente = new Cliente_BEAN();
				cliente.setID_CLIENTE(rs.getInt("ID_CLIENTE"));
				cliente.setIDFISCAL(rs.getString("IDFISCAL"));
				cliente.setNOMBRE(rs.getString("NOMBRE"));
				cliente.setDIRECCION(rs.getString("DIRECCION"));
				cliente.setLOCALIDAD(rs.getString("LOCALIDAD"));
				cliente.setPROVINCIA(rs.getString("PROVINCIA"));
				cliente.setTELEFONO(rs.getInt("TELEFONO"));
				cliente.setMOVIL(rs.getInt("MOVIL"));
				cliente.setCODIGOPOSTAL(rs.getString("CODIGO_POSTAL"));
				cliente.setPORTAL(rs.getString("PORTAL"));
				cliente.setLETRA(rs.getString("LETRA"));
				cliente.setPISO(rs.getInt("PISO"));
				cliente.setEMAIL(rs.getString("EMAIL"));
			}
			log.log(Level.DEBUG, "Dato recuperado (cliente): " + cliente);	
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
		
		return cliente;
	}
	
	public Cliente_BEAN recuperarClienteDatosDoc(ConexionBBDD db, Trazas log, int idCliente)
	{
		Cliente_BEAN cliente = new Cliente_BEAN();
		
		try {
			ps = db.prepareStatement(SQL_CLIENTE_DATOS_DOC);
			ps.setInt(1, idCliente);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_CLIENTE_DATOS_DOC);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				cliente = new Cliente_BEAN();
				cliente.setID_CLIENTE(rs.getInt("ID_CLIENTE"));
				cliente.setIDFISCAL(rs.getString("IDFISCAL"));
				cliente.setNOMBRE(rs.getString("NOMBRE"));
				cliente.setDIRECCION(rs.getString("DIRECCION"));
				cliente.setLOCALIDAD(rs.getString("LOCALIDAD"));
				cliente.setPROVINCIA(rs.getString("PROVINCIA"));
				cliente.setTELEFONO(rs.getInt("TELEFONO"));
				cliente.setMOVIL(rs.getInt("MOVIL"));
				cliente.setCODIGOPOSTAL(rs.getString("CODIGO_POSTAL"));
				cliente.setPORTAL(rs.getString("PORTAL"));
				cliente.setLETRA(rs.getString("LETRA"));
				cliente.setPISO(rs.getInt("PISO"));
				cliente.setEMAIL(rs.getString("EMAIL"));
			}
			log.log(Level.DEBUG, "Dato recuperado (cliente): " + cliente);	
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
		
		return cliente;
	}
	
	public int recupererAnteriorIdcliente(ConexionBBDD db, Trazas log, int id)
	{
		int anteriorId = 0;
		try {
			ps = db.prepareStatement(SQL_ANTERIOR_ID);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ULTIMO_ID_CLIENTE);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				if(rs.getInt("ID_CLIENTE") == id)
				{
					break;
				}
				else
				{
					anteriorId = rs.getInt("ID_CLIENTE");
				}
			}
			log.log(Level.DEBUG, "Dato recuperado (anteriorId): " + anteriorId);	
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return anteriorId;
	}
	
	public int recupererSiguienteIdcliente(ConexionBBDD db, Trazas log, int id)
	{
		int anteriorId = 0;
		try {
			ps = db.prepareStatement(SQL_SIGUIENTE_ID);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_SIGUIENTE_ID);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				if(rs.getInt("ID_CLIENTE") == id)
				{
					break;
				}
				else
				{
					anteriorId = rs.getInt("ID_CLIENTE");
				}
			}
			log.log(Level.DEBUG, "Dato recuperado (anteriorId): " + anteriorId);	
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return anteriorId;
	}
	
	public int recuperarIdNextCliente(ConexionBBDD db, Trazas log)
	{
		int ultimoId = 0;
		try {
			ps = db.prepareStatement(SQL_NEXT_ID);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_NEXT_ID);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				ultimoId = rs.getInt("ID_CLIENTE");
			}
			log.log(Level.DEBUG, "Dato recuperado (nextId): " + ultimoId);
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return ultimoId;
	}
	
	public boolean existeIdFiscal(ConexionBBDD db, Trazas log, String idFiscal)
	{
		boolean existeIdFiscal = false;
		try {
			ps = db.prepareStatement(SQL_EXISTE_ID_FISCAL);
			ps.setString(1, idFiscal);
			rs = ps.executeQuery();
			
			if(rs.next() && rs.getInt("TOTAL") > 0)
			{
				existeIdFiscal = true;
			}
			ps.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
		return existeIdFiscal;
	}
	
	public void insertarCliente(ConexionBBDD db, Trazas log)
	{
		try {
			ps = db.prepareStatement(SQL_INSERTAR_CLIENTE);
			int cont = 0;
			ps.setInt(++cont, getID_CLIENTE());
			ps.setString(++cont, getIDFISCAL());
			ps.setString(++cont, getNOMBRE());
			ps.setString(++cont, getCODIGOPOSTAL());
			ps.setString(++cont, getLOCALIDAD());
			ps.setString(++cont, getPROVINCIA());
			ps.setString(++cont, getDIRECCION());
			ps.setString(++cont, getPORTAL());
			ps.setInt(++cont, (getPISO() == null?0:getPISO()));
			ps.setString(++cont, getLETRA());
			ps.setInt(++cont, getTELEFONO());
			ps.setInt(++cont, (getMOVIL() == null?0:getMOVIL()));
			ps.setString(++cont, getEMAIL());
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_INSERTAR_CLIENTE);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			
			ps = db.prepareStatement(SQL_UPDATE_ID_CLIENTE);
			ps.setInt(1, getID_CLIENTE());
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ULTIMO_ID_CLIENTE);
			
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
	
	public void actualizarCliente(ConexionBBDD db, Trazas log)
	{
		try {
			ps = db.prepareStatement(SQL_ACTUALIZAR_CLIENTE);
			int cont = 0;
			ps.setString(++cont, getIDFISCAL());
			ps.setString(++cont, getNOMBRE());
			ps.setString(++cont, getCODIGOPOSTAL());
			ps.setString(++cont, getLOCALIDAD());
			ps.setString(++cont, getPROVINCIA());
			ps.setString(++cont, getDIRECCION());
			ps.setString(++cont, getPORTAL());
			ps.setInt(++cont, getPISO() == null?0:getPISO());
			ps.setString(++cont, getLETRA());
			ps.setInt(++cont, getTELEFONO());
			ps.setInt(++cont, getMOVIL() == null?0:getMOVIL());
			ps.setString(++cont, getEMAIL());
			ps.setInt(++cont, getID_CLIENTE());
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ACTUALIZAR_CLIENTE);
			
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
	
	public void eliminarCliente(ConexionBBDD db, Trazas log)
	{
		try {
			ps = db.prepareStatement(SQL_ELIMINAR_CLIENTE);
			ps.setInt(1, ID_CLIENTE);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ELIMINAR_CLIENTE);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );

			ArrayList<Vehiculo_BEAN> vehiculos = new Vehiculo_BEAN().recuperarVehiculosCliente(db, log, ID_CLIENTE);
			
			for(Vehiculo_BEAN vehiculo : vehiculos)
			{
				vehiculo.borrarVehiculo(db, log, IDFISCAL);
			}
			
			ArrayList<Nota_BEAN> notas = new Nota_BEAN().recuperarNotasCliente(db, log, ID_CLIENTE);
			
			for(Nota_BEAN nota : notas)
			{
				nota.borrarNotaCliente(db, log);
			}
			
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
	
	public void eliminadoLogicoCliente(ConexionBBDD db, Trazas log)
	{
		try {
			ps = db.prepareStatement(SQL_ELIMINAR_LOGICO_CLIENTE);
			ps.setInt(1, ID_CLIENTE);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ELIMINAR_LOGICO_CLIENTE);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			ArrayList<Vehiculo_BEAN> vehiculos = new Vehiculo_BEAN().recuperarVehiculosCliente(db, log, ID_CLIENTE);
			
			for(Vehiculo_BEAN vehiculo : vehiculos)
			{
				vehiculo.borradoLogicoVehiculo(db, log, IDFISCAL);
			}						
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
	
	public List<Cliente_BEAN> recuperarClientes(ConexionBBDD db, Trazas log)
	{
		List<Cliente_BEAN> clientes = new ArrayList<Cliente_BEAN>();
		
		try {
			ps = db.prepareStatement(SQL_CLIENTES);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				Cliente_BEAN aux = new Cliente_BEAN();
				aux.setID_CLIENTE(rs.getInt("ID_CLIENTE"));
				aux.setIDFISCAL(rs.getString("IDFISCAL"));
				aux.setNOMBRE(rs.getString("NOMBRE"));
				aux.setDIRECCION(rs.getString("DIRECCION"));
				aux.setLOCALIDAD(rs.getString("LOCALIDAD"));
				aux.setPROVINCIA(rs.getString("PROVINCIA"));
				aux.setTELEFONO(rs.getInt("TELEFONO"));
				aux.setMOVIL(rs.getInt("MOVIL"));
				aux.setCODIGOPOSTAL(rs.getString("CODIGO_POSTAL"));
				aux.setPORTAL(rs.getString("PORTAL"));
				aux.setLETRA(rs.getString("LETRA"));
				aux.setPISO(rs.getInt("PISO"));
				aux.setEMAIL(rs.getString("EMAIL"));
				clientes.add(aux);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
		
		return clientes;
	}
	
	public Cliente_BEAN recuperarClienteMatricula(ConexionBBDD db, Trazas log, String matricula)
	{
		Cliente_BEAN cliente = new Cliente_BEAN();
		
		try {
			ps = db.prepareStatement(SQL_CLIENTE_MATRICULA);
			ps.setString(1, matricula);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				cliente = new Cliente_BEAN();
				cliente.setID_CLIENTE(rs.getInt("ID_CLIENTE"));
				cliente.setIDFISCAL(rs.getString("IDFISCAL"));
				cliente.setNOMBRE(rs.getString("NOMBRE"));
				cliente.setDIRECCION(rs.getString("DIRECCION"));
				cliente.setLOCALIDAD(rs.getString("LOCALIDAD"));
				cliente.setPROVINCIA(rs.getString("PROVINCIA"));
				cliente.setTELEFONO(rs.getInt("TELEFONO"));
				cliente.setMOVIL(rs.getInt("MOVIL"));
				cliente.setCODIGOPOSTAL(rs.getString("CODIGO_POSTAL"));
				cliente.setPORTAL(rs.getString("PORTAL"));
				cliente.setLETRA(rs.getString("LETRA"));
				cliente.setPISO(rs.getInt("PISO"));
				cliente.setEMAIL(rs.getString("EMAIL"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
		
		return cliente;
	}
	
	public ArrayList<Cliente_BEAN> recuperarClientes(ConexionBBDD db, Trazas log, Cliente_BEAN cliente, String matricula, String marca, String modelo, boolean conNota, boolean conDeuda)
	{
		ArrayList<Cliente_BEAN> clientes = new ArrayList<Cliente_BEAN>();
		
		try {
			ps = db.prepareStatement(SQL_CLIENTES_BUSQUEDA + componerWhere(cliente, matricula, marca, modelo, conNota, conDeuda) + " ORDER BY NOMBRE ");
			log.log(Level.DEBUG, "QUERY A LANZAR: " + SQL_CLIENTES_BUSQUEDA + componerWhere(cliente, matricula, marca, modelo, conNota, conDeuda) + " ORDER BY NOMBRE " + ";");
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				Cliente_BEAN aux = new Cliente_BEAN();
				aux.setID_CLIENTE(rs.getInt("ID_CLIENTE"));
				aux.setIDFISCAL(rs.getString("IDFISCAL"));
				aux.setNOMBRE(rs.getString("NOMBRE"));
				aux.setDIRECCION(rs.getString("DIRECCION"));
				aux.setLOCALIDAD(rs.getString("LOCALIDAD"));
				aux.setPROVINCIA(rs.getString("PROVINCIA"));
				aux.setTELEFONO(rs.getInt("TELEFONO"));
				aux.setMOVIL(rs.getInt("MOVIL"));
				aux.setCODIGOPOSTAL(rs.getString("CODIGO_POSTAL"));
				aux.setPORTAL(rs.getString("PORTAL"));
				aux.setLETRA(rs.getString("LETRA"));
				aux.setPISO(rs.getInt("PISO"));
				aux.setEMAIL(rs.getString("EMAIL"));
				clientes.add(aux);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
		
		return clientes;
	}
	
	private String componerWhere(Cliente_BEAN cliente, String matricula, String marca, String modelo, boolean conNotas, boolean conDeuda)
	{
		String where = "";
		
		if(cliente.getID_CLIENTE() != 0)
		{
			where += " WHERE ID_CLIENTE = " + cliente.getID_CLIENTE() + " ";
		}
		
		if(cliente.getIDFISCAL() != null && cliente.getIDFISCAL().trim().length() > 0)
		{
	
			if(where.length() == 0)
			{
				where += " WHERE IDFISCAL LIKE '%" + cliente.getIDFISCAL().trim() + "%' ";
			}
			else
			{
				where += " AND IDFISCAL LIKE '%" + cliente.getIDFISCAL().trim() + "%' ";
			}
		}
		
		if(cliente.getNOMBRE() != null && cliente.getNOMBRE().trim().length() > 0)
		{
	
			if(where.length() == 0)
			{
				where += " WHERE NOMBRE LIKE '%" + cliente.getNOMBRE().trim() + "%' ";
			}
			else
			{
				where += " AND NOMBRE LIKE '%" + cliente.getNOMBRE().trim() + "%' ";
			}
		}
		
		if(cliente.getTELEFONO() != null && cliente.getTELEFONO() != 0)
		{
			if(where.length() == 0)
			{
				where += " WHERE (TELEFONO BETWEEN " + rellenarDerecha(String.valueOf(cliente.getTELEFONO()), "0") + " AND " + rellenarDerecha(String.valueOf(cliente.getTELEFONO()), "9") + " ";
			}
			else
			{
				where += " AND (TELEFONO BETWEEN " + rellenarDerecha(String.valueOf(cliente.getTELEFONO()), "0") + " AND " + rellenarDerecha(String.valueOf(cliente.getTELEFONO()), "0") + " ";
			}
			
			if(cliente.getMOVIL() != null && cliente.getMOVIL() != 0)
			{
				where += " OR MOVIL BETWEEN " + rellenarDerecha(String.valueOf(cliente.getMOVIL()), "0") + " AND " + rellenarDerecha(String.valueOf(cliente.getMOVIL()), "0") + ") ";
			}
			else
			{
				where += " ) ";
			}
		}
		
		if(matricula != null && matricula.trim().length() > 0)
		{
			if(where.length() == 0)
			{
				where += " WHERE ID_CLIENTE IN (SELECT ID_CLIENTE FROM VEHICULOS WHERE MATRICULA LIKE '%" + matricula.trim() + "%') ";
			}
			else
			{
				where += " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM VEHICULOS WHERE MATRICULA LIKE '%" + matricula.trim() + "%') ";
			}
		}
		
		if(marca.trim().length() > 0 && modelo.trim().length() > 0)
		{
			if(where.length() == 0)
			{
				where += " WHERE ID_CLIENTE IN (SELECT ID_CLIENTE FROM VEHICULOS WHERE MODELO = '" + modelo.trim() + "' AND MARCA = '" + marca.trim() + "') ";
			}
			else
			{
				where += " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM VEHICULOS WHERE MODELO = '" + modelo.trim() + "' AND MARCA = '" + marca.trim() + "') ";
			}
		}
		
		if(conNotas)
		{
			if(where.length() == 0)
			{
				where += " WHERE ID_CLIENTE IN (SELECT ID_CLIENTE FROM NOTAS WHERE ID_CLIENTE = CLI.ID_CLIENTE) ";
			}
			else
			{
				where += " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM NOTAS WHERE ID_CLIENTE =  CLI.ID_CLIENTE) ";
			}
		}
		
		if(where.length() == 0)
		{
			where += " WHERE ACTIVO = 'S' ";
		}
		else
		{
			where += " AND ACTIVO = 'S' ";
		}
		
		if(conDeuda)
		{
			if(where.length() == 0)
			{
				where += " WHERE DEUDA > 0 ";
			}
			else
			{
				where += " AND DEUDA > 0 ";
			}	
		}
		
		return where;
	}
	
	public Cliente_BEAN recuperarClienteIdFiscal(ConexionBBDD db, Trazas log, String idFiscal)
	{
		Cliente_BEAN cliente = new Cliente_BEAN();
		
		try {
			ps = db.prepareStatement(SQL_CLIENTE_IDFISCAL);
			ps.setString(1, idFiscal);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				cliente = new Cliente_BEAN();
				cliente.setID_CLIENTE(rs.getInt("ID_CLIENTE"));
				cliente.setIDFISCAL(rs.getString("IDFISCAL"));
				cliente.setNOMBRE(rs.getString("NOMBRE"));
				cliente.setDIRECCION(rs.getString("DIRECCION"));
				cliente.setLOCALIDAD(rs.getString("LOCALIDAD"));
				cliente.setPROVINCIA(rs.getString("PROVINCIA"));
				cliente.setTELEFONO(rs.getInt("TELEFONO"));
				cliente.setMOVIL(rs.getInt("MOVIL"));
				cliente.setCODIGOPOSTAL(rs.getString("CODIGO_POSTAL"));
				cliente.setPORTAL(rs.getString("PORTAL"));
				cliente.setLETRA(rs.getString("LETRA"));
				cliente.setPISO(rs.getInt("PISO"));
				cliente.setEMAIL(rs.getString("EMAIL"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
		
		return cliente;
	}
	
	public int recuperarIdIdFiscal(ConexionBBDD db, Trazas log, String idFiscal)
	{
		int id = 0;
		try {
			ps = db.prepareStatement(SQL_ID_IDFISCAL);
			ps.setString(1, idFiscal);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				id = rs.getInt("ID_CLIENTE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
				e.printStackTrace();
				log.log(Level.ERROR, e);
			}
		}
		return id;
	}
	
	public double recuperarDeudaCliente(ConexionBBDD db, Trazas log, int idCliente)
	{
		double deuda = 0;
		try {
			ps = db.prepareStatement(SQL_DEUDA_CLIENTE);
			ps.setInt(1, idCliente);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_DEUDA_CLIENTE);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				deuda = rs.getDouble("DEUDA");
			}
			log.log(Level.DEBUG, "Dato recuperado (ultimoId): " + deuda);			
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return deuda;
	}
	
	public void actualizarDeudaCliente(ConexionBBDD db, Trazas log, int idCliente, double deuda)
	{
		try {
			ps = db.prepareStatement(SQL_UPDATE_DEUDA_CLIENTE);
			int cont = 0;
			ps.setDouble(++cont, deuda);
			ps.setInt(++cont, idCliente);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_UPDATE_DEUDA_CLIENTE);
			
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
	
	private String rellenarDerecha(String numero, String relleno)
	{
		int longitud = numero.length();
		for(int i = longitud; i < 9; i++)
		{
			numero += relleno;
		}
		return numero;
	}
}
