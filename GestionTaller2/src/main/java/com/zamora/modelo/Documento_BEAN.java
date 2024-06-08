package com.zamora.modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Level;

import com.zamora.BBDD.ConexionBBDD;
import com.zamora.trazas.Trazas;

public class Documento_BEAN {

	private int idCliente;
	private int idVehiculo;
	private int idDocumento;
	private int idEmpresa;
	private int serie;
	private String tipo;
	private Integer impuesto;
	private String fecha;
	private String formaPago;
	private int kilometraje;
	private boolean totalPresupuesto;
	private ArrayList<CargoDocumento_BEAN> cargos;
	private Trazas log;
	
	public Documento_BEAN(Trazas log) {
		super();
		this.log = log;
	}

	public Documento_BEAN(int idCliente, int idVehiculo, int idDocumento, int idEmpresa, int serie, String tipo, Integer impuesto
			, String fecha, String formaPago, int kilometraje, boolean totalPresupuesto, ArrayList<CargoDocumento_BEAN> cargos, Trazas log) {
		super();
		this.log = log;
		this.idCliente = idCliente;
		this.idVehiculo = idVehiculo;
		this.idDocumento = idDocumento;
		this.idEmpresa = idEmpresa;
		this.serie = serie;
		this.tipo = tipo;
		this.impuesto = impuesto;
		this.fecha = fecha;
		this.formaPago = formaPago;
		this.kilometraje = kilometraje;
		this.totalPresupuesto = totalPresupuesto;
		this.cargos = cargos;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getIdVehiculo() {
		return idVehiculo;
	}

	public void setIdVehiculo(int idVehiculo) {
		this.idVehiculo = idVehiculo;
	}

	public int getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}

	public int getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public int getSerie() {
		return serie;
	}

	public void setSerie(int serie) {
		this.serie = serie;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(Integer impuesto) {
		this.impuesto = impuesto;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public int getKilometraje() {
		return kilometraje;
	}

	public void setKilometraje(int kilometraje) {
		this.kilometraje = kilometraje;
	}

	public boolean isTotalPresupuesto() {
		return totalPresupuesto;
	}

	public void setTotalPresupuesto(boolean totalPresupuesto) {
		this.totalPresupuesto = totalPresupuesto;
	}

	public ArrayList<CargoDocumento_BEAN> getCargos() {
		return cargos;
	}

	public void setCargos(ArrayList<CargoDocumento_BEAN> cargos) {
		this.cargos = cargos;
	}
	
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	private final String SQL_PRIMER_ID_DOCUMENTO = 
			" SELECT ID_DOCUMENTO, SERIE " + 
			" FROM DOCUMENTO " + 
			" WHERE TIPO = ? " + 
				" AND ID_EMPRESA = ? " +
			" ORDER BY SERIE ASC, ID_DOCUMENTO ASC ";
	
	private final String SQL_ULTIMO_ID_DOCUMENTO = 
			" SELECT ID_DOCUMENTO, SERIE FROM DOCUMENTO WHERE TIPO = ? AND ID_EMPRESA = ? ORDER BY SERIE DESC, ID_DOCUMENTO DESC  ";
	
	private final String SQL_NUM_DOCUMENTOS = 
			" SELECT COUNT(*) TOTAL FROM DOCUMENTO WHERE ID_EMPRESA = ? AND TIPO = ? ";
	
	private final String SQL_RECUPERAR_DOCUMENTO = 
			" SELECT ID_CLIENTE, ID_VEHICULO, ID_DOCUMENTO, ID_EMPRESA, SERIE, TIPO, IMPUESTO, FECHA, KM_VEHICULO, TOTA_PRESUPUESTO, FORMAPAGO " + 
			" FROM DOCUMENTO " + 
			" WHERE ID_DOCUMENTO = ? " +
				" AND TIPO = ? " + 
				" AND ID_EMPRESA = ? " +
				" AND SERIE = ? ";
	
	private final String SQL_RECUPERAR_CARGOS = 
			" SELECT REFERENCIA, ID_DOCUMENTO, ID_CARGO, ID_EMPRESA, TIPO, DESCRIPCION, UNIDAD, PVP, DTO, TOTAL " + 
			" FROM CARGO_DOCUMENTO " + 
			" WHERE ID_DOCUMENTO = ? " +
				" AND TIPO = ? " +
				" AND ID_EMPRESA = ? " +
				" AND SERIE = ? ";
	
	private final String SQL_ANTERIOR_ID =
			" SELECT ID_DOCUMENTO, SERIE FROM DOCUMENTO WHERE TIPO = ? AND ID_EMPRESA = ? ORDER BY SERIE ASC, ID_DOCUMENTO ASC";

	private final String SQL_SIGUIENTE_ID =
			" SELECT ID_DOCUMENTO, SERIE FROM DOCUMENTO WHERE TIPO = ? AND ID_EMPRESA = ? ORDER BY SERIE DESC, ID_DOCUMENTO DESC ";

	private final String SQL_ID_FACTURA_AGNO = 
			" SELECT COUNT(*) FROM DOCUMENTO WHERE ID_EMPRESA = ? AND SERIE = ? AND TIPO = ? ";
	
	private final String SQL_NEXT_ID_PRESUPUESTO = 
			" SELECT (ID_PRESUPUESTO + 1) ID_DOCUMENTO FROM AUTO_INCREMENT ";
	
	private final String SQL_NEXT_ID_FACTURA = 
			" SELECT (ID_FACTURA + 1) ID_DOCUMENTO FROM DATOSEMPRESA WHERE ID_EMPRESA = X ";
	
	private final String SQL_RECUPERAR_DOCUMENTOS = 
			" SELECT ID_CLIENTE, ID_VEHICULO, ID_DOCUMENTO, ID_EMPRESA, SERIE, TIPO, IMPUESTO, FECHA, KM_VEHICULO, TOTA_PRESUPUESTO, FORMAPAGO " + 
			" FROM DOCUMENTO " + 
			" WHERE TIPO = ? " +
				" AND ID_EMPRESA = ? ";
	
	private final String SQL_RECUPERAR_DOCUMENTO_ID_CLIENTE = 
			" SELECT ID_CLIENTE, ID_VEHICULO, ID_DOCUMENTO, SERIE, TIPO, IMPUESTO, FECHA, KM_VEHICULO, TOTA_PRESUPUESTO, FORMAPAGO " + 
			" FROM DOCUMENTO " + 
			" WHERE ID_CLIENTE = ? " +
				" AND TIPO = ? " +
				" AND ID_EMPRESA = ? ";
	
	private final String SQL_RECUPERAR_DOCUMENTOS_ID_CLIENTE = 
			" SELECT ID_CLIENTE, ID_VEHICULO, ID_DOCUMENTO, SERIE, TIPO, IMPUESTO, FECHA, KM_VEHICULO, TOTA_PRESUPUESTO, FORMAPAGO " + 
			" FROM DOCUMENTO " + 
			" WHERE ID_CLIENTE = ? ";
	
	private final String SQL_RECUPERAR_DOCUMENTO_ID_VEHICULO = 
			" SELECT ID_CLIENTE, ID_VEHICULO, ID_DOCUMENTO, SERIE, TIPO, IMPUESTO, FECHA, KM_VEHICULO, TOTA_PRESUPUESTO, FORMAPAGO " + 
			" FROM DOCUMENTO " + 
			" WHERE ID_VEHICULO = ? " +
				" AND TIPO = ? " +
				" AND ID_EMPRESA = ? ";
	
	private final String INSERT_DOCUMENTO =
			" INSERT INTO DOCUMENTO (ID_CLIENTE, ID_VEHICULO, ID_DOCUMENTO, ID_EMPRESA, TIPO, IMPUESTO, SERIE, FECHA, KM_VEHICULO, TOTA_PRESUPUESTO, FORMAPAGO) " + 
			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	private final String INSERT_CARGO_DOCUMENTO = 
			" INSERT INTO CARGO_DOCUMENTO (ID_DOCUMENTO, ID_CARGO, ID_EMPRESA, TIPO, SERIE, REFERENCIA, DESCRIPCION, UNIDAD, PVP, DTO, TOTAL) " + 
			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	private final String SQL_UPDATE_ID_PRESUPUESTO =
			" UPDATE AUTO_INCREMENT SET ID_PRESUPUESTO = ? ";
	
	private final String SQL_UPDATE_ID_FACTURA =
			" UPDATE DATOSEMPRESA SET ID_FACTURA = ? WHERE ID_EMPRESA = X ";
	
	private final String SQL_UPDATE_DOCUMENTO = 
			" UPDATE DOCUMENTO " + 
			" SET IMPUESTO = ? " + 
				", FECHA = ? " + 
				", KM_VEHICULO = ? " + 
				", TOTA_PRESUPUESTO = ? " + 
				", FORMAPAGO = ? " + 
				", ID_CLIENTE = ? " + 
				", ID_VEHICULO = ? " + 
				" WHERE ID_DOCUMENTO = ? " + 
				" AND TIPO = ? " +
				" AND ID_EMPRESA = ? " + 
				" AND SERIE = ? ";
	
	private final String SQL_RESETEAR_CARGOS = 
			" DELETE FROM CARGO_DOCUMENTO " + 
			" WHERE ID_DOCUMENTO = ? " +
				" AND TIPO = ? " +
				" AND ID_EMPRESA = ? " +
				" AND SERIE = ? ";
	
	private final String SQL_ULTIMO_NEXT_ID_SERIE = 
			" SELECT MAX(ID_DOCUMENTO) + 1 ID_DOCUMENTO FROM DOCUMENTO WHERE TIPO = ? AND ID_EMPRESA = ? AND SERIE = ? ";
	
	private final String DELETE_DOCUMENTO =
			" DELETE FROM DOCUMENTO WHERE ID_DOCUMENTO = ? AND TIPO = ? AND ID_EMPRESA = ? AND SERIE = ? ";
	
	private final String DELTE_CARGO = 
			" DELETE FROM CARGO_DOCUMENTO WHERE ID_DOCUMENTO = ? AND TIPO = ? AND ID_EMPRESA = ? AND SERIE = ? ";
	
	public int[] recupererPrimerIdDocumento(ConexionBBDD db, Trazas log, String tipo, int idEmpresa)
	{
		int[] primerIdCliente = {0 , 0};
		try {
			ps = db.prepareStatement(SQL_PRIMER_ID_DOCUMENTO);
			ps.setString(1, tipo);
			ps.setInt(2, idEmpresa);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_PRIMER_ID_DOCUMENTO);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			
			if(rs.next())
			{
				primerIdCliente[0] = rs.getInt("ID_DOCUMENTO");
				primerIdCliente[1] = rs.getInt("SERIE");
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (primerIdCliente): " + primerIdCliente);	
		return primerIdCliente;
	}
	
	public int[] recuperarIdUltimoDocumento(ConexionBBDD db, Trazas log, String tipo, int idEmpresa)
	{
		int[] ultimoId = {0,0};
		try {
			ps = db.prepareStatement(SQL_ULTIMO_ID_DOCUMENTO);
			ps.setString(1, tipo);
			ps.setInt(2, idEmpresa);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ULTIMO_ID_DOCUMENTO);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			
			if(rs.next())
			{
				ultimoId[0] = rs.getInt("ID_DOCUMENTO");
				ultimoId[1] = rs.getInt("SERIE");
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (ultimoId): " + ultimoId[0] + "/" + ultimoId[1]);	
		return ultimoId;
	}
	
	public int recupererNumDocumentos(ConexionBBDD db, Trazas log, String tipo, int idEmpresa)
	{
		int numclientes = 0;
		try {
			ps = db.prepareStatement(SQL_NUM_DOCUMENTOS);
			ps.setInt(1, idEmpresa);
			ps.setString(2, tipo);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_NUM_DOCUMENTOS);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				numclientes = rs.getInt("TOTAL");
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (numclientes): " + numclientes);	
		return numclientes;
	}
	
	public Documento_BEAN recuperarDocumento(ConexionBBDD db, Trazas log, int serie, int idDocumento, String tipo, int idEmpresa)
	{
		Documento_BEAN documento = null;
		try {
			ps = db.prepareStatement(SQL_RECUPERAR_DOCUMENTO);
			ps.setInt(1, idDocumento);
			ps.setString(2, tipo);
			ps.setInt(3, idEmpresa);
			ps.setInt(4, serie);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_DOCUMENTO);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			
			if(rs.next())
			{
				documento = new Documento_BEAN(log);
				documento.setIdCliente(rs.getInt("ID_CLIENTE"));
				documento.setIdVehiculo(rs.getInt("ID_VEHICULO"));
				documento.setIdDocumento(rs.getInt("ID_DOCUMENTO"));
				documento.setIdEmpresa(rs.getInt("ID_EMPRESA"));
				documento.setSerie(rs.getInt("SERIE"));
				documento.setImpuesto(rs.getInt("IMPUESTO"));
				documento.setTipo(rs.getString("TIPO"));
				documento.setFecha(rs.getString("FECHA"));
				documento.setKilometraje(rs.getInt("KM_VEHICULO"));
				documento.setTotalPresupuesto(rs.getString("TOTA_PRESUPUESTO").equals("S"));
				documento.setFormaPago(rs.getString("FORMAPAGO"));
			}
			rs.close();
			ps.close();
			
			if(documento != null)
			{
				ps = db.prepareStatement(SQL_RECUPERAR_CARGOS);
				ps.setInt(1, idDocumento);
				ps.setString(2, tipo);
				ps.setInt(3, idEmpresa);
				ps.setInt(4, serie);

				log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_CARGOS);
				
				inicio = System.currentTimeMillis();
				rs = ps.executeQuery();
				fin = System.currentTimeMillis();
				log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
				
				
				ArrayList<CargoDocumento_BEAN> cargos = new ArrayList<CargoDocumento_BEAN>();
				
				while(rs.next())
				{
					CargoDocumento_BEAN aux = new CargoDocumento_BEAN();
					aux.setIdDocumento(rs.getInt("ID_DOCUMENTO"));
					aux.setIdCargo(rs.getInt("ID_CARGO"));
					aux.setReferencia(rs.getString("REFERENCIA"));
					aux.setDescripcion(rs.getString("DESCRIPCION"));
					aux.setUnidades(rs.getDouble("UNIDAD"));
					aux.setPrecio(rs.getDouble("PVP"));
					aux.setDescuento(rs.getInt("DTO"));
					aux.setTotal(rs.getDouble("TOTAL"));
					cargos.add(aux);
				}
				documento.setCargos(cargos);
				rs.close();
				ps.close();
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (documento): " + documento);	
		return documento;
	}
	
	public int[] recupererAnteriorIdDocumento(ConexionBBDD db, Trazas log, int id, String tipo, int idEmpresa, int serie)
	{
		int[] anteriorId = {0,0};
		try {
			ps = db.prepareStatement(SQL_ANTERIOR_ID);
			ps.setString(1, tipo);
			ps.setInt(2, idEmpresa);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ANTERIOR_ID);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			
			while(rs.next())
			{
				if(rs.getInt("ID_DOCUMENTO") == id && rs.getInt("SERIE") == serie)
				{
					break;
				}
				else
				{
					anteriorId[0] = rs.getInt("ID_DOCUMENTO");
					anteriorId[1] = rs.getInt("SERIE");
				}
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (anteriorId): " + anteriorId);	
		return anteriorId;
	}
	
	public int[] recupererSiguienteIdDocumento(ConexionBBDD db, Trazas log, int id, String tipo, int idEmpresa, int serie)
	{
		int[] siguienteId = {0,0};
		try {
			ps = db.prepareStatement(SQL_SIGUIENTE_ID);
			ps.setString(1, tipo);
			ps.setInt(2, idEmpresa);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_SIGUIENTE_ID);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			
			while(rs.next())
			{
				if(rs.getInt("ID_DOCUMENTO") == id && rs.getInt("SERIE") == serie)
				{
					break;
				}
				else
				{
					siguienteId[0] = rs.getInt("ID_DOCUMENTO");
					siguienteId[1] = rs.getInt("SERIE");
				}
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (anteriorId): " + siguienteId);	
		return siguienteId;
	}
	
	public int recupererNexID(ConexionBBDD db, Trazas log, String tipo, int idEmpresa, int serie)
	{
		int nexIdDocumento = 0;
		boolean hayFacturasAgno = true;
		try {
				
			ps = db.prepareStatement(SQL_ID_FACTURA_AGNO);
			ps.setInt(1, idEmpresa);
			ps.setInt(2, serie);
			ps.setString(3, tipo);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ID_FACTURA_AGNO);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				hayFacturasAgno = rs.getInt(1) > 0;
			}
			
			ps.close();
			rs.close();
			
			if(hayFacturasAgno)
			{
				String query = "";
				if(tipo.equals("P"))
					query = SQL_NEXT_ID_PRESUPUESTO;
				else
					query = SQL_NEXT_ID_FACTURA.replace("X", String.valueOf(idEmpresa));
				ps = db.prepareStatement(query);
				log.log(Level.DEBUG, "Consulta lanzada: " + query);			
				
				inicio = System.currentTimeMillis();
				rs = ps.executeQuery();
				fin = System.currentTimeMillis();
				log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
							
				if(rs.next())
				{
					nexIdDocumento = rs.getInt("ID_DOCUMENTO");
				}	
			}
			else
			{
				nexIdDocumento = 1;
			}
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (nexIdDocumento): " + nexIdDocumento);
		return nexIdDocumento;
	}
	
	public ArrayList<Documento_BEAN> recuperarDocumentos(ConexionBBDD db, Trazas log, String tipo, int idEmpresa)
	{
		ArrayList<Documento_BEAN> documentos = new ArrayList<Documento_BEAN>();
		try {
			ps = db.prepareStatement(SQL_RECUPERAR_DOCUMENTOS);
			ps.setString(1, tipo);
			ps.setInt(2, idEmpresa);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_DOCUMENTOS);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			
			while(rs.next())
			{
				Documento_BEAN documento = new Documento_BEAN(log);
				documento.setIdCliente(rs.getInt("ID_CLIENTE"));
				documento.setIdVehiculo(rs.getInt("ID_VEHICULO"));
				documento.setIdDocumento(rs.getInt("ID_DOCUMENTO"));
				documento.setSerie(rs.getInt("SERIE"));
				documento.setImpuesto(rs.getInt("IMPUESTO"));
				documento.setTipo(rs.getString("TIPO"));
				documento.setFecha(rs.getString("FECHA"));
				documento.setKilometraje(rs.getInt("KM_VEHICULO"));
				documento.setTotalPresupuesto(rs.getString("TOTA_PRESUPUESTO").equals("S"));
				documento.setFormaPago(rs.getString("FORMAPAGO"));
				
				PreparedStatement ps2 = db.prepareStatement(SQL_RECUPERAR_CARGOS);
				ps2.setInt(1, documento.getIdDocumento());
				ps2.setString(2, tipo);
				ps2.setInt(3, idEmpresa);
				ps2.setInt(4, documento.getSerie());
				log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_CARGOS);
				
				inicio = System.currentTimeMillis();
				ResultSet rs2 = ps2.executeQuery();
				fin = System.currentTimeMillis();
				log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
				
				
				ArrayList<CargoDocumento_BEAN> cargos = new ArrayList<CargoDocumento_BEAN>();
				
				while(rs2.next())
				{
					CargoDocumento_BEAN aux = new CargoDocumento_BEAN();
					aux.setIdDocumento(rs2.getInt("ID_DOCUMENTO"));
					aux.setIdCargo(rs2.getInt("ID_CARGO"));
					aux.setDescripcion(rs2.getString("DESCRIPCION"));
					aux.setUnidades(rs2.getDouble("UNIDAD"));
					aux.setPrecio(rs2.getDouble("PVP"));
					aux.setDescuento(rs2.getInt("DTO"));
					aux.setTotal(rs2.getDouble("TOTAL"));
					cargos.add(aux);
				}
				documento.setCargos(cargos);
				rs2.close();
				ps2.close();
				
				documentos.add(documento);
			}
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (documentos): " + documentos.size());	
		return documentos;
	}
	
	public Documento_BEAN recuperarDocumento(ConexionBBDD db, Trazas log, int idDocumento, String tipo, int idEmpresa, int serie)
	{
		Documento_BEAN documento = null;
		try {
			ps = db.prepareStatement(SQL_RECUPERAR_DOCUMENTO);
			ps.setInt(1, idDocumento);
			ps.setString(2, tipo);
			ps.setInt(3, idEmpresa);
			ps.setInt(4, serie);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_DOCUMENTO);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			
			if(rs.next())
			{
				documento = new Documento_BEAN(log);
				documento.setIdCliente(rs.getInt("ID_CLIENTE"));
				documento.setIdVehiculo(rs.getInt("ID_VEHICULO"));
				documento.setIdDocumento(rs.getInt("ID_DOCUMENTO"));
				documento.setIdEmpresa(rs.getInt("ID_EMPRESA"));
				documento.setSerie(rs.getInt("SERIE"));
				documento.setImpuesto(rs.getInt("IMPUESTO"));
				documento.setTipo(rs.getString("TIPO"));
				documento.setFecha(rs.getString("FECHA"));
				documento.setKilometraje(rs.getInt("KM_VEHICULO"));
				documento.setTotalPresupuesto(rs.getString("TOTA_PRESUPUESTO").equals("S"));
				documento.setFormaPago(rs.getString("FORMAPAGO"));
			}
			rs.close();
			ps.close();
			
			if(documento != null)
			{
				ps = db.prepareStatement(SQL_RECUPERAR_CARGOS);
				ps.setInt(1, idDocumento);
				ps.setString(2, tipo);
				ps.setInt(3, idEmpresa);
				ps.setInt(4, serie);

				log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_CARGOS);
				
				inicio = System.currentTimeMillis();
				rs = ps.executeQuery();
				fin = System.currentTimeMillis();
				log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
				
				
				ArrayList<CargoDocumento_BEAN> cargos = new ArrayList<CargoDocumento_BEAN>();
				
				while(rs.next())
				{
					CargoDocumento_BEAN aux = new CargoDocumento_BEAN();
					aux.setIdDocumento(rs.getInt("ID_DOCUMENTO"));
					aux.setIdCargo(rs.getInt("ID_CARGO"));
					aux.setDescripcion(rs.getString("DESCRIPCION"));
					aux.setUnidades(rs.getDouble("UNIDAD"));
					aux.setPrecio(rs.getDouble("PVP"));
					aux.setDescuento(rs.getInt("DTO"));
					aux.setTotal(rs.getDouble("TOTAL"));
					cargos.add(aux);
				}
				documento.setCargos(cargos);
				rs.close();
				ps.close();
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (documento): " + documento);	
		return documento;
	}
	
	public ArrayList<Documento_BEAN> recuperarDocumentoIdCliente(ConexionBBDD db, Trazas log, int idCliente, String tipo, int idEmpresa)
	{
		ArrayList<Documento_BEAN> documentos = new ArrayList<Documento_BEAN>();
		try {
			ps = db.prepareStatement(SQL_RECUPERAR_DOCUMENTO_ID_CLIENTE);
			ps.setInt(1, idCliente);
			ps.setString(2, tipo);
			ps.setInt(3, idEmpresa);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_DOCUMENTO_ID_CLIENTE);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			
			while(rs.next())
			{
				Documento_BEAN documento = new Documento_BEAN(log);
				documento.setIdCliente(rs.getInt("ID_CLIENTE"));
				documento.setIdVehiculo(rs.getInt("ID_VEHICULO"));
				documento.setIdDocumento(rs.getInt("ID_DOCUMENTO"));
				documento.setSerie(rs.getInt("SERIE"));
				documento.setImpuesto(rs.getInt("IMPUESTO"));
				documento.setTipo(rs.getString("TIPO"));
				documento.setFecha(rs.getString("FECHA"));
				documento.setKilometraje(rs.getInt("KM_VEHICULO"));
				documento.setTotalPresupuesto(rs.getString("TOTA_PRESUPUESTO").equals("S"));
				documento.setFormaPago(rs.getString("FORMAPAGO"));
				
				PreparedStatement ps2 = db.prepareStatement(SQL_RECUPERAR_CARGOS);
				ps2.setInt(1, documento.getIdDocumento());
				ps2.setString(2, tipo);
				ps2.setInt(3, idEmpresa);
				ps2.setInt(4, documento.getSerie());
				log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_CARGOS);
				
				inicio = System.currentTimeMillis();
				ResultSet rs2 = ps2.executeQuery();
				fin = System.currentTimeMillis();
				log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
				
				
				ArrayList<CargoDocumento_BEAN> cargos = new ArrayList<CargoDocumento_BEAN>();
				
				while(rs2.next())
				{
					CargoDocumento_BEAN aux = new CargoDocumento_BEAN();
					aux.setIdDocumento(rs2.getInt("ID_DOCUMENTO"));
					aux.setIdCargo(rs2.getInt("ID_CARGO"));
					aux.setDescripcion(rs2.getString("DESCRIPCION"));
					aux.setUnidades(rs2.getDouble("UNIDAD"));
					aux.setPrecio(rs2.getDouble("PVP"));
					aux.setDescuento(rs2.getInt("DTO"));
					aux.setTotal(rs2.getDouble("TOTAL"));
					cargos.add(aux);
				}
				documento.setCargos(cargos);
				rs2.close();
				ps2.close();
				
				documentos.add(documento);
			}
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (documentos): " + documentos.size());	
		return documentos;
	}
	
	public ArrayList<Documento_BEAN> recuperarDocumentosIdCliente(ConexionBBDD db, Trazas log, int idCliente)
	{
		ArrayList<Documento_BEAN> documentos = new ArrayList<Documento_BEAN>();
		try {
			ps = db.prepareStatement(SQL_RECUPERAR_DOCUMENTOS_ID_CLIENTE);
			ps.setInt(1, idCliente);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_DOCUMENTOS_ID_CLIENTE);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			
			while(rs.next())
			{
				Documento_BEAN documento = new Documento_BEAN(log);
				documento.setIdCliente(rs.getInt("ID_CLIENTE"));
				documento.setIdVehiculo(rs.getInt("ID_VEHICULO"));
				documento.setIdDocumento(rs.getInt("ID_DOCUMENTO"));
				documento.setSerie(rs.getInt("SERIE"));
				documento.setImpuesto(rs.getInt("IMPUESTO"));
				documento.setTipo(rs.getString("TIPO"));
				documento.setFecha(rs.getString("FECHA"));
				documento.setKilometraje(rs.getInt("KM_VEHICULO"));
				documento.setTotalPresupuesto(rs.getString("TOTA_PRESUPUESTO").equals("S"));
				documento.setFormaPago(rs.getString("FORMAPAGO"));
				
				PreparedStatement ps2 = db.prepareStatement(SQL_RECUPERAR_CARGOS);
				ps2.setInt(1, documento.getIdDocumento());
				ps2.setString(2, tipo);
				ps2.setInt(3, idEmpresa);
				ps2.setInt(4, documento.getSerie());
				log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_CARGOS);
				
				inicio = System.currentTimeMillis();
				ResultSet rs2 = ps2.executeQuery();
				fin = System.currentTimeMillis();
				log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
				
				
				ArrayList<CargoDocumento_BEAN> cargos = new ArrayList<CargoDocumento_BEAN>();
				
				while(rs2.next())
				{
					CargoDocumento_BEAN aux = new CargoDocumento_BEAN();
					aux.setIdDocumento(rs2.getInt("ID_DOCUMENTO"));
					aux.setIdCargo(rs2.getInt("ID_CARGO"));
					aux.setDescripcion(rs2.getString("DESCRIPCION"));
					aux.setUnidades(rs2.getDouble("UNIDAD"));
					aux.setPrecio(rs2.getDouble("PVP"));
					aux.setDescuento(rs2.getInt("DTO"));
					aux.setTotal(rs2.getDouble("TOTAL"));
					cargos.add(aux);
				}
				documento.setCargos(cargos);
				rs2.close();
				ps2.close();
				
				documentos.add(documento);
			}
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (documentos): " + documentos.size());	
		return documentos;
	}
	
	public ArrayList<Documento_BEAN> recuperarDocumentoIdVehiculo(ConexionBBDD db, Trazas log, int idVehiculo, String tipo, int idEmpresa)
	{
		ArrayList<Documento_BEAN> documentos = new ArrayList<Documento_BEAN>();
		try {
			ps = db.prepareStatement(SQL_RECUPERAR_DOCUMENTO_ID_VEHICULO);
			ps.setInt(1, idVehiculo);
			ps.setString(2, tipo);
			ps.setInt(3, idEmpresa);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_DOCUMENTO_ID_VEHICULO);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			
			while(rs.next())
			{
				Documento_BEAN documento = new Documento_BEAN(log);
				documento.setIdCliente(rs.getInt("ID_CLIENTE"));
				documento.setIdVehiculo(rs.getInt("ID_VEHICULO"));
				documento.setIdDocumento(rs.getInt("ID_DOCUMENTO"));
				documento.setSerie(rs.getInt("SERIE"));
				documento.setImpuesto(rs.getInt("IMPUESTO"));
				documento.setTipo(rs.getString("TIPO"));
				documento.setFecha(rs.getString("FECHA"));
				documento.setKilometraje(rs.getInt("KM_VEHICULO"));
				documento.setTotalPresupuesto(rs.getString("TOTA_PRESUPUESTO").equals("S"));
				documento.setFormaPago(rs.getString("FORMAPAGO"));
				
				PreparedStatement ps2 = db.prepareStatement(SQL_RECUPERAR_CARGOS);
				ps2.setInt(1, documento.getIdDocumento());
				ps2.setString(2, tipo);
				ps2.setInt(3, idEmpresa);
				ps2.setInt(4, documento.getSerie());
				log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RECUPERAR_CARGOS);
				
				inicio = System.currentTimeMillis();
				ResultSet rs2 = ps2.executeQuery();
				fin = System.currentTimeMillis();
				log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
				
				
				ArrayList<CargoDocumento_BEAN> cargos = new ArrayList<CargoDocumento_BEAN>();
				
				while(rs2.next())
				{
					CargoDocumento_BEAN aux = new CargoDocumento_BEAN();
					aux.setIdDocumento(rs2.getInt("ID_DOCUMENTO"));
					aux.setIdCargo(rs2.getInt("ID_CARGO"));
					aux.setDescripcion(rs2.getString("DESCRIPCION"));
					aux.setUnidades(rs2.getDouble("UNIDAD"));
					aux.setPrecio(rs2.getDouble("PVP"));
					aux.setDescuento(rs2.getInt("DTO"));
					aux.setTotal(rs2.getDouble("TOTAL"));
					cargos.add(aux);
				}
				documento.setCargos(cargos);
				rs2.close();
				ps2.close();
				
				documentos.add(documento);
			}
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (documentos): " + documentos.size());	
		return documentos;
	}
	
	public void insertarDocumento(ConexionBBDD db, Trazas log, boolean serieDistinta)
	{
		try {
			ps = db.prepareStatement(INSERT_DOCUMENTO);
			int cont = 0;
			ps.setInt(++cont, getIdCliente());
			ps.setInt(++cont, getIdVehiculo());
			ps.setInt(++cont, getIdDocumento());
			ps.setInt(++cont, getIdEmpresa());
			ps.setString(++cont, getTipo());
			ps.setInt(++cont, getImpuesto());
			ps.setInt(++cont, getSerie());
			ps.setString(++cont, getFecha());
			ps.setInt(++cont, getKilometraje());
			ps.setString(++cont, isTotalPresupuesto()?"S":"N");
			ps.setString(++cont, getFormaPago());
			log.log(Level.DEBUG, "Consulta lanzada: " + INSERT_DOCUMENTO);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			ps.close();
			
			for(int i = 0; i < getCargos().size(); i++)
			{

				ps = db.prepareStatement(INSERT_CARGO_DOCUMENTO);
				cont = 0;
				ps.setInt(++cont, getCargos().get(i).getIdDocumento());
				ps.setInt(++cont, getCargos().get(i).getIdCargo());
				ps.setInt(++cont, getIdEmpresa());
				ps.setString(++cont, getTipo());
				ps.setInt(++cont, getSerie());
				ps.setString(++cont, getCargos().get(i).getReferencia());
				ps.setString(++cont, getCargos().get(i).getDescripcion());
				ps.setDouble(++cont, getCargos().get(i).getUnidades());
				ps.setDouble(++cont, getCargos().get(i).getPrecio());
				ps.setInt(++cont, getCargos().get(i).getDescuento());
				ps.setDouble(++cont, getCargos().get(i).getTotal());
				log.log(Level.DEBUG, "Consulta lanzada: " + INSERT_CARGO_DOCUMENTO);
				
				inicio = System.currentTimeMillis();
				ps.executeUpdate();
				fin = System.currentTimeMillis();
				log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
				
				ps.close();
			}
			
			// si la serie es distinta a la actual no actualizamos el contador de facturas
			if((!serieDistinta && getTipo().equals("F")) || getTipo().equals("P"))
			{
				String query = "";
				if(getTipo().equals("P"))
					query = SQL_UPDATE_ID_PRESUPUESTO;
				else
					query = SQL_UPDATE_ID_FACTURA.replace("X", String.valueOf(getIdEmpresa()));
				ps = db.prepareStatement(query);
				ps.setInt(1, getIdDocumento());
				log.log(Level.DEBUG, "Consulta lanzada: " + query);
				
				inicio = System.currentTimeMillis();
				ps.executeUpdate();
				fin = System.currentTimeMillis();
				log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
				
				ps.close();
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
	}
	
	public void actualizarDocumento(ConexionBBDD db, Trazas log)
	{
		try {
			ps = db.prepareStatement(SQL_UPDATE_DOCUMENTO);
			int cont = 0;
			ps.setInt(++cont, getImpuesto());
			ps.setString(++cont, getFecha());
			ps.setInt(++cont, getKilometraje());
			ps.setString(++cont, isTotalPresupuesto()?"S":"N");
			ps.setString(++cont, getFormaPago());
			ps.setInt(++cont, getIdCliente());
			ps.setInt(++cont, getIdVehiculo());
			ps.setInt(++cont, getIdDocumento());
			ps.setString(++cont, getTipo());
			ps.setInt(++cont, getIdEmpresa());
			ps.setInt(++cont, getSerie());
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_UPDATE_DOCUMENTO);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			ps.close();
			
			ps = db.prepareStatement(SQL_RESETEAR_CARGOS);
			cont = 0;
			ps.setInt(++cont, getIdDocumento());
			ps.setString(++cont, getTipo());
			ps.setInt(++cont, getIdEmpresa());
			ps.setInt(++cont, getSerie());
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_RESETEAR_CARGOS);
			
			inicio = System.currentTimeMillis();				
			ps.executeUpdate();
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			ps.close();
			
			for(int i = 0; i < getCargos().size(); i++)
			{
				ps = db.prepareStatement(INSERT_CARGO_DOCUMENTO);
				cont = 0;
				ps.setInt(++cont, getCargos().get(i).getIdDocumento());
				ps.setInt(++cont, getCargos().get(i).getIdCargo());
				ps.setInt(++cont, getIdEmpresa());
				ps.setString(++cont, getTipo());
				ps.setInt(++cont, getSerie());
				ps.setString(++cont, getCargos().get(i).getReferencia());
				ps.setString(++cont, getCargos().get(i).getDescripcion());
				ps.setDouble(++cont, getCargos().get(i).getUnidades());
				ps.setDouble(++cont, getCargos().get(i).getPrecio());
				ps.setInt(++cont, getCargos().get(i).getDescuento());
				ps.setDouble(++cont, getCargos().get(i).getTotal());
				log.log(Level.DEBUG, "Consulta lanzada: " + INSERT_CARGO_DOCUMENTO);
				
				inicio = System.currentTimeMillis();
				ps.executeUpdate();
				fin = System.currentTimeMillis();
				log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
				
				ps.close();
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
	}
	
	public int recuperarNextIdSerie(ConexionBBDD db, Trazas log, String tipo, int idEmpresa, int serie)
	{
		int ultimoId = 0;
		try {
			ps = db.prepareStatement(SQL_ULTIMO_NEXT_ID_SERIE);
			ps.setString(1, tipo);
			ps.setInt(2, idEmpresa);
			ps.setInt(3, serie);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_ULTIMO_NEXT_ID_SERIE);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			
			if(rs.next())
			{
				ultimoId = rs.getInt("ID_DOCUMENTO");
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado siguiente id de la serie(" + serie + "): " + ultimoId);	
		return ultimoId;
	}
	
	public void borrarDocumento(ConexionBBDD db, Trazas log, int idDocumento, String tipo, int idEmpresa, int serie)
	{
		try {
			ps = db.prepareStatement(DELETE_DOCUMENTO);
			int cont = 0;
			ps.setInt(++cont, idDocumento);
			ps.setString(++cont, tipo);
			ps.setInt(++cont, idEmpresa);
			ps.setInt(++cont, serie);
			log.log(Level.DEBUG, "Consulta lanzada: " + DELETE_DOCUMENTO);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			ps.close();

			ps = db.prepareStatement(DELTE_CARGO);
			cont = 0;
			ps.setInt(++cont, idDocumento);
			ps.setString(++cont, tipo);
			ps.setInt(++cont, idEmpresa);
			ps.setInt(++cont, serie);
			log.log(Level.DEBUG, "Consulta lanzada: " + DELTE_CARGO);
			
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

	@Override
	public String toString() {
		ConexionBBDD db = new ConexionBBDD(log);
		db.conectar();
		Cliente_BEAN cliente = new Cliente_BEAN();
		Vehiculo_BEAN vehiculo = new Vehiculo_BEAN();
		String salida = serie + "/" + idDocumento + " - " + (idCliente != 0?cliente.recuperarCliente(db, log, idCliente).getNOMBRE():"NO DOCUMENTOS") + " - " + (idVehiculo != 0?vehiculo.recuperarVehiculo(db, log, idVehiculo).getMATRICULA():"");
		
		return salida;
	}
}
