package com.zamora.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;

import com.zamora.Main;
import com.zamora.BBDD.ConexionAccess;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.Marca_BEAN;
import com.zamora.trazas.Trazas;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Util {

	PreparedStatement ps = null;
	ResultSet rs = null;
	
	private String SQL_MUN_PROV =
			" SELECT XX FROM CP_MUN_PROV GROUP BY XX ORDER BY XX ";
	
	private final String SQL_CP_LOCALIDAD_PROVINCIA =
			" SELECT CP, LOCALIDAD, PROVINCIA " + 
			" FROM CP_MUN_PROV ";
	
	private final String PARAMETRO = 
			" SELECT NOMBRE_PARAMETRO, VALOR_PARAMETRO " + 
			" FROM PARAMETROS " + 
			" WHERE NOMBRE_PARAMETRO = ? ";

	String UPDATEPARAMETRO = " UPDATE PARAMETROS SET VALOR_PARAMETRO = ? WHERE NOMBRE_PARAMETRO = ? ";

	String INSERTPARAMETRO = " INSERT INTO PARAMETROS (NOMBRE_PARAMETRO, VALOR_PARAMETRO) VALUES (?, ?) ";
	
	private final String SQL_NOMBRES_MODELOS = 
			" SELECT MA.NOMBRE || ' - ' || MO.NOMBRE AS NOMBRE FROM MODELO MO, MARCA MA WHERE MO.ID_MARCA = MA.ID_MARCA ";

	private final String SQL_MARCAS = 
			" SELECT MA.NOMBRE NOMBRE FROM MARCA MA ";

	private final String SQL_MARCAS_BEAN = 
			" SELECT MA.ID_MARCA, MA.NOMBRE NOMBRE FROM MARCA MA ";
	
	private final String SQL_MODELOS = 
			" SELECT MO.NOMBRE FROM MODELO MO, MARCA MA WHERE MO.ID_MARCA = MA.ID_MARCA AND MA.NOMBRE = ? ORDER BY MO.NOMBRE ";
	
	private final String SQL_COMBUSTIBLES = 
			" SELECT TIPO_COMBUSTIBLE FROM COMBUSTIBLES ";
	
	private final String INSERTA_MARCA = 
			" INSERT INTO MARCA (ID_MARCA, NOMBRE) VALUES ((SELECT MAX(ID_MARCA) + 1 FROM MARCA), ?) ";

	private final String INSERTA_MODELO = 
			" INSERT INTO MODELO (ID_MARCA, ID_MODELO, NOMBRE) VALUES (?, (SELECT MAX(ID_MODELO) + 1 FROM MODELO), ?) ";
	
	private final String SQL_TABLAS = 
			" SELECT TABLEID, TABLENAME " + 
			" FROM SYS.SYSTABLES " + 
			" WHERE TABLETYPE = 'T'";
	
	private final String SQL_CAMPOS = 
			" SELECT COLUMNNAME, COLUMNDATATYPE " + 
			" FROM SYS.SYSCOLUMNS " + 
			" WHERE REFERENCEID = ? " + 
			" ORDER BY COLUMNNUMBER";
	
	public void insertarMarca(ConexionBBDD db, Trazas log, String marca)
	{
		try {
			ps = db.prepareStatement(INSERTA_MARCA);
			ps.setString(1, marca);
			log.log(Level.DEBUG, "Consulta lanzada: " + INSERTA_MARCA);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			db.commit();			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
	}
	
	public boolean existeMarca(ConexionBBDD db, Trazas log, String marca)
	{
		boolean existe = false;
		try {
			ps = db.prepareStatement(SQL_MARCAS);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_MARCAS);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next() && !existe)
			{
				existe = marca.equalsIgnoreCase(rs.getString("NOMBRE"));
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return existe;
	}
	
	public void insertarModelo(ConexionBBDD db, Trazas log, int idMarca, String modelo)
	{
		try {
			ps = db.prepareStatement(INSERTA_MODELO);
			ps.setInt(1, idMarca);
			ps.setString(2, modelo);
			log.log(Level.DEBUG, "Consulta lanzada: " + INSERTA_MODELO);
			
			long inicio = System.currentTimeMillis();
			ps.executeUpdate();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			db.commit();			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
	}
	
	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public ArrayList<String> getMunProv(ConexionBBDD db, Trazas log, String munProv)
	{
		ArrayList<String> lMunProv = new ArrayList<String>();
		try {
			ps = db.prepareStatement(SQL_MUN_PROV.replaceAll("XX", munProv));
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_MUN_PROV.replaceAll("XX", munProv));
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				String aux = rs.getString(munProv);
				lMunProv.add(aux);
			}
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (" + munProv + "): " + lMunProv.size());
		return lMunProv;	
	}
	
	public static void generarCarpetaDatos()
	{
        
        // Nombre de la carpeta que deseas crear
        String folderName = ".GestionTaller";
        
        // Ruta completa a la carpeta en diferentes sistemas operativos
        String folderPath = "";
        
        // Verifica el sistema operativo y configura la ruta adecuada
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            // Windows (AppData)
            String appData = System.getenv("APPDATA");
            folderPath = appData + File.separator + folderName;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
            // Linux / Unix / macOS (Directorio del usuario)
    		String userHome = System.getProperty("user.home");
            folderPath = userHome + File.separator + folderName;
        } else {
            return;
        }
        
        // Crear la carpeta
        File folder = new File(folderPath);
        if(!folder.exists())
        	folder.mkdir();
                
        Main.rutaDatos = folderPath;
	}
	
	public ArrayList<String[]> recuperarMunicipioProvincia(ConexionBBDD db, Trazas log, String cp)
	{
		ArrayList<String[]> localidaMunicipio = new ArrayList<String[]>();
		try {
			ps = db.prepareStatement(SQL_CP_LOCALIDAD_PROVINCIA + " WHERE CP = ? ");
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_CP_LOCALIDAD_PROVINCIA + " WHERE CP = ? ");

			long inicio = System.currentTimeMillis();
			ps.setString(1, cp);
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				String[] aux = new String[3];
				aux[0] = rs.getString("LOCALIDAD");
				aux[1] = rs.getString("PROVINCIA");
				aux[2] = rs.getString("CP");
				localidaMunicipio.add(aux);
			}
			log.log(Level.DEBUG, "Dato recuperado (localida, Municipio): " + localidaMunicipio.size());
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
		return localidaMunicipio;
	}
	
	public ArrayList<String[]> recuperarCpProvincia(ConexionBBDD db, Trazas log, String municipio)
	{
		ArrayList<String[]> localidaMunicipio = new ArrayList<String[]>();
		try {
			ps = db.prepareStatement(SQL_CP_LOCALIDAD_PROVINCIA + " WHERE LOCALIDAD = ? ");
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_CP_LOCALIDAD_PROVINCIA + " WHERE LOCALIDAD = ? ");
			ps.setString(1, municipio);

			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				String[] aux = new String[3];
				aux[0] = rs.getString("LOCALIDAD");
				aux[1] = rs.getString("PROVINCIA");
				aux[2] = rs.getString("CP");
				localidaMunicipio.add(aux);
			}
			log.log(Level.DEBUG, "Dato recuperado (cp, provincia): " + localidaMunicipio.size());
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
		return localidaMunicipio;
	}
	
	public ArrayList<String[]> recuperarCpLocalidad(ConexionBBDD db, Trazas log, String provincia)
	{
		ArrayList<String[]> localidaMunicipio = new ArrayList<String[]>();
		try {
			ps = db.prepareStatement(SQL_CP_LOCALIDAD_PROVINCIA + " WHERE PROVINCIA = ? ");
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_CP_LOCALIDAD_PROVINCIA + " WHERE PROVINCIA = ? ");
			ps.setString(1, provincia);

			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				String[] aux = new String[3];
				aux[0] = rs.getString("LOCALIDAD");
				aux[1] = rs.getString("PROVINCIA");
				aux[2] = rs.getString("CP");
				localidaMunicipio.add(aux);
			}
			log.log(Level.DEBUG, "Dato recuperado (cp, localidad): " + localidaMunicipio.size());
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
		return localidaMunicipio;
	}
	
	public String recuperarParametro(ConexionBBDD db, Trazas log, String parametro)
	{
		String parametros = new String();
		try {
			ps = db.prepareStatement(PARAMETRO);
			ps.setString(1, parametro);
			log.log(Level.DEBUG, "Consulta lanzada: " + PARAMETRO.replace("?", "'" + parametro  + "'"));
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				parametros = rs.getString("VALOR_PARAMETRO");
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return parametros;
	}
	
	public String actualizarParametro(ConexionBBDD db, Trazas log, String parametro, String valor)
	{
		String parametros = new String();
		try {
			
			String par = recuperarParametro(db, log, parametro);
			
			if(par.length() > 0)
			{
				ps = db.prepareStatement(UPDATEPARAMETRO);
				ps.setString(1, valor);
				ps.setString(2, parametro);
				log.log(Level.DEBUG, "Consulta lanzada: " + UPDATEPARAMETRO.replace("?", "'" + parametro  + "'"));
				
				long inicio = System.currentTimeMillis();
				ps.executeUpdate();
				long fin = System.currentTimeMillis();
				log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			}
			else
			{
				ps = db.prepareStatement(INSERTPARAMETRO);
				ps.setString(1, parametro);
				ps.setString(2, valor);
				log.log(Level.DEBUG, "Consulta lanzada: " + INSERTPARAMETRO.replace("?", "'" + parametro  + "'"));
				
				long inicio = System.currentTimeMillis();
				ps.executeUpdate();
				long fin = System.currentTimeMillis();
				log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			}
						
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return parametros;
	}
	
	public ArrayList<String> recuperarParametros(ConexionBBDD db, Trazas log,String parametro)
	{
		ArrayList<String> parametros = new ArrayList<String>();
		try {
			ps = db.prepareStatement(PARAMETRO);
			ps.setString(1, parametro);
			log.log(Level.DEBUG, "Consulta lanzada: " + PARAMETRO.replace("?", "'" + parametro  + "'"));
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			if(rs.next())
			{
				String paramAux[] = rs.getString("VALOR_PARAMETRO").split(";");
				for(String param : paramAux)
					parametros.add(param);
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		return parametros;
	}
	
	public ArrayList<String> recuperarNombresModelos(ConexionBBDD db, Trazas log)
	{
		ArrayList<String> modelos = new ArrayList<String>();
		try {
			ps = db.prepareStatement(SQL_NOMBRES_MODELOS);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_NOMBRES_MODELOS);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				modelos.add(rs.getString("NOMBRE"));
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (modelos): " + modelos.size());
		return modelos;
	}
	
	public ArrayList<String> recuperarMarcas(ConexionBBDD db, Trazas log)
	{
		ArrayList<String> marcas = new ArrayList<String>();
		try {
			ps = db.prepareStatement(SQL_MARCAS);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_MARCAS);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				marcas.add(rs.getString("NOMBRE"));
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (marcas): " + marcas.size());
		return marcas;
	}
	
	public ArrayList<Marca_BEAN> recuperarMarcasBean(ConexionBBDD db, Trazas log)
	{
		ArrayList<Marca_BEAN> marcas = new ArrayList<Marca_BEAN>();
		try {
			ps = db.prepareStatement(SQL_MARCAS_BEAN);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_MARCAS_BEAN);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				marcas.add(new Marca_BEAN(rs.getInt("ID_MARCA"), rs.getString("NOMBRE")));
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (marcas): " + marcas.size());
		return marcas;
	}
	
	public ArrayList<String> recuperarModelos(ConexionBBDD db, String marca, Trazas log)
	{
		ArrayList<String> modelos = new ArrayList<String>();
		try {
			ps = db.prepareStatement(SQL_MODELOS);
			ps.setString(1, marca);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_MODELOS);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				modelos.add(rs.getString("NOMBRE"));
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (modelos): " + modelos.size());
		return modelos;
	}
	
	public ArrayList<String> recuperarCombustibles(ConexionBBDD db, Trazas log)
	{
		ArrayList<String> combustibles = new ArrayList<String>();
		try {
			ps = db.prepareStatement(SQL_COMBUSTIBLES);
			log.log(Level.DEBUG, "Consulta lanzada: " + SQL_COMBUSTIBLES);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en ejecutar consulta: " + (double) (fin - inicio) / 1000 + " segundos." );
			
			while(rs.next())
			{
				combustibles.add(rs.getString("TIPO_COMBUSTIBLE"));
			}
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
		log.log(Level.DEBUG, "Dato recuperado (combustibles): " + combustibles.size());
		return combustibles;
	}
	
	public boolean checkMatricula(ConexionBBDD db, Trazas log, String matricula)
	{
		ArrayList<String> expresiones = recuperarParametros(db, log, "EXPRESIONES_MATRICULAS");
		boolean algunaCoincide = false;
        
		matricula = matricula.toUpperCase(); 
        for(int i = 0; i < expresiones.size() && !algunaCoincide; i++)
        {
        	Pattern patternAux = Pattern.compile(expresiones.get(i));
        	if(patternAux.matcher(matricula).matches())
        	{
        		algunaCoincide = true;
        	}
        }
        
        return algunaCoincide;
    }
	
	public boolean checkBastidor(String bastidor){
		Pattern pat = Pattern.compile("[A-Z0-9]{17}");
		Matcher mat = pat.matcher(bastidor);
		if (mat.matches()) {
            return true;
        } else {
            return false;
        }
    }
	
	public String[] serverDate(Trazas log)
	{
		ConexionBBDD db = new ConexionBBDD(log);
		db.conectar();
		String[] servers = recuperarParametros(db, log, "SERVER_DATE").toArray(new String[0]);
		db.desconectar();
		return servers;
	}
	
	public boolean isSerieDoc(String str)
	{
		Pattern pattern = Pattern.compile("[1-9]{2}[/]{1}[0-9]*", Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(str);
	    return matcher.find();
	}
	
	public boolean confirmar(String texto)
	{
		boolean retorno = false;
		Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmationAlert.setHeaderText(null);
	    confirmationAlert.setTitle("Confirmación");
	    confirmationAlert.setContentText(texto);
	    
	    Optional<ButtonType> result = confirmationAlert.showAndWait();
	    
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	    	retorno = true;
	    }
	    
	    return retorno;	    
	}
	
	public void backUpBBDD(ConexionBBDD db, Trazas log, String ruta) 
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs3 = null;
		
		try {
			FicheroSQL escritor = new FicheroSQL(ruta);
			ps = db.prepareStatement(SQL_TABLAS);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				String tabla = rs.getString("TABLENAME");
				ArrayList<String[]> camposMap = new ArrayList<String[]>();
				escritor.escribirTraza("DELETE FROM " + tabla);
				
				ps2 = db.prepareStatement(SQL_CAMPOS);
				ps2.setString(1, rs.getString("TABLEID"));
				
				rs2 = ps2.executeQuery();
				
				String campos = "";
				
				while(rs2.next())
				{
					if(campos.length() > 0)
						campos += ", ";
					campos += rs2.getString("COLUMNNAME");
					
					String[] aux = new String[2];
					aux[0] = rs2.getString("COLUMNNAME");
					aux[1] = rs2.getString("COLUMNDATATYPE");
					camposMap.add(aux);
				}
				
				ps3 = db.prepareStatement(" SELECT " + campos + " FROM " + tabla);
				
				rs3 = ps3.executeQuery();
				
				
				while(rs3.next())
				{
					String values = "";
					for (String[] campo : camposMap) {
					    if(values.length() > 0)
					    	values += ", ";
					    if(campo[1].startsWith("VARCHAR"))
					    {
					    	values += (rs3.getString(campo[0]) != null || rs3.getString(campo[0]).trim().length() != 0 )?"'" + rs3.getString(campo[0]).trim().replaceAll("'", "''").replace("\n", " ") + "'":"null";
					    }
					    else if(campo[1].startsWith("INTEGER") || campo[1].startsWith("SMALLINT") || campo[1].startsWith("BOOLEAN") || campo[1].startsWith("DOUBLE"))
					    {
					    	values += (rs3.getString(campo[0]) != null)?rs3.getString(campo[0]).trim():"null";
					    }
					}
					escritor.escribirTraza("INSERT INTO " + tabla + " (" + campos + ") VALUES (" + values + ")");
				}
			}
			escritor.liberar();
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			
		} catch (SQLException e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
	}
	
	public void backUpBBDDAccess(ConexionAccess db, Trazas log, String ruta) 
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
	
		SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			
			String consultaAlbaranes = " SELECT ID_ALBARAN, ID_CLIENTE, ID_VEHICULO, FECHA, RUTA FROM ALBARANES ";
			
			FicheroSQL escritor = new FicheroSQL(ruta);
			ps = db.prepareStatement(consultaAlbaranes);
			
			long inicio = System.currentTimeMillis();
			rs = ps.executeQuery();

			escritor.escribirTraza("DELETE FROM ALBARANES");
			while(rs.next())
			{
				String insert = "INSERT INTO ALBARANES (ID_ALBARAN, ID_CLIENTE, ID_VEHICULO, FECHA, RUTA) VALUES (";
				insert += rs.getInt("ID_ALBARAN") + ", ";
				insert += rs.getInt("ID_CLIENTE") + ", ";
				insert += rs.getInt("ID_VEHICULO") + ", ";
				insert += "'" + formatoSalida.format(formatoEntrada.parse(rs.getString("FECHA"))) + "', ";
				insert += "'" + rs.getString("RUTA").substring(rs.getString("RUTA").lastIndexOf("\\") + 1) + "'";
				insert += ")"; 
				
				log.log(Level.DEBUG, insert);
				escritor.escribirTraza(insert);
			}
			long fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			rs.close();

			String consultaAutoIncrement = " SELECT ID_CLIENTE, ID_VEHICULO, ID_PRESUPUESTO, ID_CARGO, ID_NOTA FROM AUTO_INCREMENT ";
			ps = db.prepareStatement(consultaAutoIncrement);
			
			inicio = System.currentTimeMillis();
			rs = ps.executeQuery();

			escritor.escribirTraza("DELETE FROM AUTO_INCREMENT");
			while(rs.next())
			{
				String insert = "INSERT INTO AUTO_INCREMENT (ID_CLIENTE, ID_VEHICULO, ID_PRESUPUESTO, ID_CARGO, ID_NOTA) VALUES (";
				insert += rs.getInt("ID_CLIENTE") + ", ";
				insert += rs.getInt("ID_VEHICULO") + ", ";
				insert += rs.getInt("ID_PRESUPUESTO") + ", ";
				insert += rs.getString("ID_CARGO") + ", ";
				insert += rs.getString("ID_NOTA");
				insert += ")"; 
				
				log.log(Level.DEBUG, insert);
				escritor.escribirTraza(insert);
			}
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			rs.close();

			String consultaCargoDocumento = " SELECT ID_DOCUMENTO, ID_CARGO, ID_EMPRESA, TIPO, SERIE, DESCRIPCION, UNIDAD, PVP, DTO, TOTAL FROM CARGO_DOCUMENTO "; 
			ps = db.prepareStatement(consultaCargoDocumento);
			
			inicio = System.currentTimeMillis();
			rs = ps.executeQuery();

			escritor.escribirTraza("DELETE FROM CARGO_DOCUMENTO");
			while(rs.next())
			{
				String insert = "INSERT INTO CARGO_DOCUMENTO (ID_DOCUMENTO, ID_CARGO, ID_EMPRESA, TIPO, SERIE, DESCRIPCION, UNIDAD, PVP, DTO, TOTAL) VALUES (";
				insert += rs.getInt("ID_DOCUMENTO") + ", ";
				insert += rs.getInt("ID_CARGO") + ", ";
				insert += rs.getInt("ID_EMPRESA") + ", ";
				insert += "'" + rs.getString("TIPO") + "', ";
				insert += rs.getInt("SERIE") + ", ";
				insert += "'" + (rs.getString("DESCRIPCION") != null?rs.getString("DESCRIPCION"):"") + "', ";
				insert += rs.getDouble("UNIDAD") + ", ";
				insert += rs.getDouble("PVP") + ", ";
				insert += rs.getDouble("DTO") + ", ";
				insert += rs.getDouble("TOTAL") + "";
				insert += ")"; 
				
				log.log(Level.DEBUG, insert);
				escritor.escribirTraza(insert);
			}
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			rs.close();

			String consultaClientes = " SELECT ID_CLIENTE, IDFISCAL, NOMBRE, DIRECCION, LOCALIDAD, PROVINCIA, TELEFONO, MOVIL, CODIGO_POSTAL, PORTAL, PISO, LETRA, EMAIL FROM CLIENTES "; 
			ps = db.prepareStatement(consultaClientes);
			
			inicio = System.currentTimeMillis();
			rs = ps.executeQuery();

			escritor.escribirTraza("DELETE FROM CLIENTES");
			while(rs.next())
			{
				String insert = "INSERT INTO CLIENTES (ID_CLIENTE, IDFISCAL, NOMBRE, DIRECCION, LOCALIDAD, PROVINCIA, TELEFONO, MOVIL, CODIGO_POSTAL, PORTAL, PISO, LETRA, EMAIL) VALUES (";
				insert += rs.getInt("ID_CLIENTE") + ", ";
				insert += "'" + rs.getString("IDFISCAL") + "', ";
				insert += "'" + rs.getString("NOMBRE") + "', ";
				insert += "'" + rs.getString("DIRECCION").replace("'", "''") + "', ";
				insert += "'" + rs.getString("LOCALIDAD").replace("'", "''") + "', ";
				insert += "'" + rs.getString("PROVINCIA").replace("'", "''") + "', ";
				insert += rs.getInt("TELEFONO") + ", ";
				insert += rs.getInt("MOVIL") + ", ";
				insert += rs.getInt("CODIGO_POSTAL") + ", ";
				insert += "'" + rs.getString("PORTAL") + "', ";
				insert += rs.getInt("PISO") + ", ";
				insert += "'" + rs.getString("LETRA") + "', ";
				insert += "'" + rs.getString("EMAIL") + "'";
				insert += ")"; 
				
				log.log(Level.DEBUG, insert);
				escritor.escribirTraza(insert);
			}
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			rs.close();

			String consultaCombustibles = " SELECT TIPO_COMBUSTIBLE FROM COMBUSTIBLES "; 
			ps = db.prepareStatement(consultaCombustibles);
			
			inicio = System.currentTimeMillis();
			rs = ps.executeQuery();

			escritor.escribirTraza("DELETE FROM COMBUSTIBLES");
			while(rs.next())
			{
				String insert = "INSERT INTO COMBUSTIBLES (TIPO_COMBUSTIBLE) VALUES (";
				insert += "'" + rs.getString("TIPO_COMBUSTIBLE") + "'";
				insert += ")"; 
				
				log.log(Level.DEBUG, insert);
				escritor.escribirTraza(insert);
			}
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			rs.close();

			String consultaDatosEmpresa = " SELECT ID_EMPRESA, EMPRESA, NOMBRE, DIRECCION, TELEFONO, CIF, CODIGOPOSTA, MUNICIPIO, PROVINCIA, ID_FACTURA, PREDEFINIDA FROM DATOSEMPRESA "; 
			ps = db.prepareStatement(consultaDatosEmpresa);
			
			inicio = System.currentTimeMillis();
			rs = ps.executeQuery();

			escritor.escribirTraza("DELETE FROM DATOSEMPRESA");
			while(rs.next())
			{
				String insert = "INSERT INTO DATOSEMPRESA (ID_EMPRESA, EMPRESA, NOMBRE, DIRECCION, TELEFONO, CIF, CODIGOPOSTA, MUNICIPIO, PROVINCIA, ID_FACTURA, PREDEFINIDA) VALUES (";
				insert += rs.getInt("ID_EMPRESA") + ", ";
				insert += "'" + rs.getString("EMPRESA") + "', ";
				insert += "'" + rs.getString("NOMBRE") + "', ";
				insert += "'" + rs.getString("DIRECCION") + "', ";
				insert += "'" + rs.getString("TELEFONO") + "', ";
				insert += "'" + rs.getString("CIF") + "', ";
				insert += "'" + rs.getString("CODIGOPOSTA") + "', ";
				insert += "'" + rs.getString("MUNICIPIO") + "', ";
				insert += "'" + rs.getString("PROVINCIA") + "', ";
				insert += rs.getInt("ID_FACTURA") + ", ";
				insert += rs.getBoolean("PREDEFINIDA");
				insert += ")"; 
				
				log.log(Level.DEBUG, insert);
				escritor.escribirTraza(insert);
			}
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			rs.close();

			String consultaDocumento = " SELECT ID_CLIENTE, ID_VEHICULO, ID_DOCUMENTO, ID_EMPRESA, TIPO, SERIE, IMPUESTO, FECHA, KM_VEHICULO, TOTA_PRESUPUESTO, FORMAPAGO FROM DOCUMENTO "; 
			ps = db.prepareStatement(consultaDocumento);
			
			inicio = System.currentTimeMillis();
			rs = ps.executeQuery();

			escritor.escribirTraza("DELETE FROM DOCUMENTO");
			while(rs.next())
			{
				String insert = "INSERT INTO DOCUMENTO (ID_CLIENTE, ID_VEHICULO, ID_DOCUMENTO, ID_EMPRESA, TIPO, SERIE, IMPUESTO, FECHA, KM_VEHICULO, TOTA_PRESUPUESTO, FORMAPAGO) VALUES (";
				insert += rs.getInt("ID_CLIENTE") + ", ";
				insert += rs.getInt("ID_VEHICULO") + ", ";
				insert += rs.getInt("ID_DOCUMENTO") + ", ";
				insert += rs.getInt("ID_EMPRESA") + ", ";
				insert += "'" + rs.getString("TIPO") + "', ";
				insert += rs.getInt("SERIE") + ", ";
				insert += rs.getInt("IMPUESTO") + ", ";
				insert += "'" + rs.getString("FECHA") + "', ";
				insert += rs.getInt("KM_VEHICULO") + ", ";
				insert += "'" + rs.getString("TOTA_PRESUPUESTO") + "', ";
				insert += "'" + (rs.getString("FORMAPAGO") != null?rs.getString("FORMAPAGO"):"") + "'";
				insert += ")"; 
				
				log.log(Level.DEBUG, insert);
				escritor.escribirTraza(insert);
			}
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			rs.close();

			String consultaGenericos = " SELECT ID_GENERICO, NOMBRE_GENERICO, DESCRIPCION, UNIDADES, PVP, DTO, TOTAL FROM GENERICOS "; 
			ps = db.prepareStatement(consultaGenericos);
			
			inicio = System.currentTimeMillis();
			rs = ps.executeQuery();

			escritor.escribirTraza("DELETE FROM GENERICOS");
			while(rs.next())
			{
				String insert = "INSERT INTO GENERICOS (ID_GENERICO, NOMBRE_GENERICO, DESCRIPCION, UNIDADES, PVP, DTO, TOTAL) VALUES (";
				insert += rs.getInt("ID_GENERICO") + ", ";
				insert += "'" + rs.getString("NOMBRE_GENERICO") + "', ";
				insert += "'" + (rs.getString("DESCRIPCION") != null?rs.getString("DESCRIPCION"):"") + "', ";
				insert += rs.getInt("UNIDADES") + ", ";
				insert += rs.getInt("PVP") + ", ";
				insert += rs.getInt("DTO") + ", ";
				insert += rs.getInt("TOTAL");
				insert += ")"; 
				
				log.log(Level.DEBUG, insert);
				escritor.escribirTraza(insert);
			}
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			rs.close();

			String consultaMarca = " SELECT ID_MARCA, NOMBRE FROM MARCA "; 
			ps = db.prepareStatement(consultaMarca);
			
			inicio = System.currentTimeMillis();
			rs = ps.executeQuery();

			escritor.escribirTraza("DELETE FROM MARCA");
			while(rs.next())
			{
				String insert = "INSERT INTO MARCA (ID_MARCA, NOMBRE) VALUES (";
				insert += rs.getInt("ID_MARCA") + ", ";
				insert += "'" + rs.getString("NOMBRE") + "' ";
				insert += ")"; 
				
				log.log(Level.DEBUG, insert);
				escritor.escribirTraza(insert);
			}
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			rs.close();

			String consultaModelo = " SELECT ID_MARCA, ID_MODELO, NOMBRE FROM MODELO "; 
			ps = db.prepareStatement(consultaModelo);
			
			inicio = System.currentTimeMillis();
			rs = ps.executeQuery();

			escritor.escribirTraza("DELETE FROM MODELO");
			while(rs.next())
			{
				String insert = "INSERT INTO MODELO (ID_MARCA, ID_MODELO, NOMBRE) VALUES (";
				insert += rs.getInt("ID_MARCA") + ", ";
				insert += rs.getInt("ID_MODELO") + ", ";
				insert += "'" + rs.getString("NOMBRE") + "' ";
				insert += ")"; 
				
				log.log(Level.DEBUG, insert);
				escritor.escribirTraza(insert);
			}
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			rs.close();

			String consultaNota = " SELECT ID_CLIENTE, ID_NOTA, NOTA, NOMBRE, IMPORTANTE, PRINCIPAL FROM NOTAS "; 
			ps = db.prepareStatement(consultaNota);
			
			inicio = System.currentTimeMillis();
			rs = ps.executeQuery();

			escritor.escribirTraza("DELETE FROM NOTAS");
			while(rs.next())
			{
				String insert = "INSERT INTO NOTAS (ID_CLIENTE, ID_NOTA, NOTA, NOMBRE, IMPORTANTE, PRINCIPAL) VALUES (";
				insert += rs.getInt("ID_CLIENTE") + ", ";
				insert += rs.getInt("ID_NOTA") + ", ";
				insert += "'" + rs.getString("NOTA").replace("\n", "\\n") + "', ";
				insert += "'" + rs.getString("NOMBRE") + "', ";
				insert += rs.getBoolean("IMPORTANTE") + ", ";
				insert += rs.getBoolean("PRINCIPAL") + " ";
				insert += ")"; 
				
				log.log(Level.DEBUG, insert);
				escritor.escribirTraza(insert);
			}
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			rs.close();

			String consultaParametros = " SELECT NOMBRE_PARAMETRO, VALOR_PARAMETRO FROM PARAMETROS "; 
			ps = db.prepareStatement(consultaParametros);
			
			inicio = System.currentTimeMillis();
			rs = ps.executeQuery();

			escritor.escribirTraza("DELETE FROM PARAMETROS");
			while(rs.next())
			{
				String insert = "INSERT INTO PARAMETROS (NOMBRE_PARAMETRO, VALOR_PARAMETRO) VALUES (";
				insert += "'" + rs.getString("NOMBRE_PARAMETRO").trim() + "', ";
				insert += "'" + (rs.getString("VALOR_PARAMETRO") != null?rs.getString("VALOR_PARAMETRO").replaceAll("\n","\\n"):"") + "' ";
				insert += ")"; 
				
				log.log(Level.DEBUG, insert);
				escritor.escribirTraza(insert);
			}
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			rs.close();

			String consultaVehiculos = " SELECT ID_CLIENTE, ID_VEHICULO, MATRICULA, MARCA, MODELO, COMBUSTIBLE, FICHA_TECNICA, BASTIDOR FROM VEHICULOS "; 
			ps = db.prepareStatement(consultaVehiculos);
			
			inicio = System.currentTimeMillis();
			rs = ps.executeQuery();

			escritor.escribirTraza("DELETE FROM VEHICULOS");
			while(rs.next())
			{
				String insert = "INSERT INTO VEHICULOS (ID_CLIENTE, ID_VEHICULO, MATRICULA, MARCA, MODELO, COMBUSTIBLE, FICHA_TECNICA, BASTIDOR) VALUES (";
				insert += rs.getInt("ID_CLIENTE") + ", ";
				insert += rs.getInt("ID_VEHICULO") + ", ";
				insert += "'" + rs.getString("MATRICULA") + "', ";
				insert += "'" + rs.getString("MARCA") + "', ";
				insert += "'" + rs.getString("MODELO") + "', ";
				insert += "'" + rs.getString("COMBUSTIBLE") + "', ";
				insert += "'" + (rs.getString("FICHA_TECNICA") != null?rs.getString("FICHA_TECNICA").trim().substring(rs.getString("FICHA_TECNICA").lastIndexOf("\\") + 1):"") + "', ";
				insert += "'" + (rs.getString("BASTIDOR") != null?rs.getString("BASTIDOR"):"") + "' ";
				insert += ")"; 
				
				log.log(Level.DEBUG, insert);
				escritor.escribirTraza(insert);
			}
			fin = System.currentTimeMillis();
			log.log(Level.DEBUG, "Tiempo en extraccion del backup tardo: " + (double) (fin - inicio) / 1000 + " segundos." );
			ps.close();
			rs.close();
			
			escritor.liberar();
			
		} catch (Exception e) {
			log.log(Level.ERROR, e.getMessage());	
			log.log(Level.ERROR, e);
		}
	}
	
	public String rellenarIzquierda(String numero, String relleno, int longitudTotal)
	{
		int longitud = numero.length();
		for(int i = longitud; i < longitudTotal; i++)
		{
			numero = relleno + numero;
		}
		return numero;
	}
	
    public void eliminarDirectorio(Path directorio) throws IOException {
        Files.walkFileTree(directorio, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file); // Borra cada archivo
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                // Manejar errores al borrar archivos (si es necesario)
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc == null) {
                    Files.delete(dir); // Borra el directorio después de borrar todos los archivos
                    return FileVisitResult.CONTINUE;
                } else {
                    // Manejar errores al borrar directorios (si es necesario)
                    return FileVisitResult.CONTINUE;
                }
            }
        });
    }
    
    public static int contarLineasArchivoEnJar(String rutaArchivo) {
        int contador = 0;

        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(rutaArchivo);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream != null) {
                while ((br.readLine()) != null) {
                    contador++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contador;
    }
}
