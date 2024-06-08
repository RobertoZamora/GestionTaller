package com.zamora.util;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.log4j.Level;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.CargoDocumento_BEAN;
import com.zamora.modelo.Cliente_BEAN;
import com.zamora.modelo.Documento_BEAN;
import com.zamora.modelo.Empresa_BEAN;
import com.zamora.modelo.Vehiculo_BEAN;
import com.zamora.trazas.Trazas;

public class PDF {
	private Trazas log;
	private Util util;
 
	private static final Font paragraphLegalFont = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL);
    private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL);
    private static final Font paragraphFontBold = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
    
    NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
    DecimalFormat df = (DecimalFormat)nf;

    NumberFormat nfe = NumberFormat.getNumberInstance(Locale.GERMANY);
    DecimalFormat dfe = (DecimalFormat)nfe;

    NumberFormat nfk = NumberFormat.getNumberInstance(Locale.GERMANY);
    DecimalFormat dfk = (DecimalFormat)nfk;
    
    Cliente_BEAN cliente = null;
    Vehiculo_BEAN vehiculo = null;
    Documento_BEAN documento = null;
    Empresa_BEAN empresa = null;
    File pdfNewFile;
    
    double totalDocumento = 0.0;
    double totalImpuesto = 0.0;
    double baseImponibleDoc = 0.0;
    boolean tieneReferencias = false;
    
    
    public PDF(Cliente_BEAN cliente, Vehiculo_BEAN vehiculo, Documento_BEAN documento, Empresa_BEAN empresa, File fichero, Trazas log)
    {
    	this.log = log;
    	this.cliente = cliente;
    	this.vehiculo = vehiculo;
    	this.documento = documento;
    	this.empresa = empresa;
    	this.pdfNewFile = fichero;
    	this.util = new Util();

    	df.applyPattern("###,###.00");
    	dfe.applyPattern("###,###.00 €");
    	dfk.applyPattern("###,###");
    }
    
	public void generarFactura() {
		
		try {
    	    Document document = new Document();
    	    try {
    	        PdfWriter.getInstance(document, new FileOutputStream(pdfNewFile));
    	    } catch (FileNotFoundException fileNotFoundException) {
    	        log.log(Level.DEBUG, "No such file was found to generate the PDF "
    	                + "(No se encontró el fichero para generar el pdf)" + fileNotFoundException);
    	    }
    	    document.setMargins(20, 20, 20, 20);
    	    document.open();
    	    
	    	// PRODUCTOS
	    	Integer numColumns = 0;
	    	numColumns = 4;
	    		
	    	Integer numRows = documento.getCargos().size();
	    	
	    	double maxRowsPerPage = 35;
	    	
	    	Integer numDocu = 1;
	    	
    	    generarCabecera(document, numDocu, (int)Math.ceil(numRows/maxRowsPerPage));
	    	
	    	
	    	for(int i = 0; i < documento.getCargos().size(); i++)
	    	{
	    		CargoDocumento_BEAN cargo = documento.getCargos().get(i);
	    		
	    		double precioTotal = cargo.getTotal();
				double aumentar = ((precioTotal) * documento.getImpuesto()) / 100;
    	           	        
    	        totalDocumento += cargo.getTotal() + aumentar;
    	        totalImpuesto += aumentar;  
    	        baseImponibleDoc += precioTotal;
    	        
    	        if(cargo.getReferencia() != null && !"".equals(cargo.getReferencia()))
    	        {
    	        	tieneReferencias = true;
    	        	numColumns = 5;
    	        }
	    	}
	    	
	    	PdfPTable productos = generarTabla(numColumns);
	    	
	    	// Fill table rows (rellenamos las filas de la tabla).                
	    	for (int row = 0; row < numRows; row++) {
	    		if(row != 0 && row % maxRowsPerPage == 0)
	    		{
	    			document.add(productos);
	    			productos = generarTabla(numColumns);
	    	    	
	    	    	generarPie(document);
	        	    generarCabecera(document, ++numDocu, (int)Math.ceil(numRows/maxRowsPerPage));
	    		}
	    		
	    		CargoDocumento_BEAN cargo = documento.getCargos().get(row);
	    			    		
	    		if(tieneReferencias)
	    		{
		    		String refCargo = cargo.getReferencia();
		    		
		    		PdfPCell referencia =  new PdfPCell(new Phrase(refCargo, paragraphFont));
		    		referencia.setHorizontalAlignment(Element.ALIGN_LEFT);
		    		referencia.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);
	    	        productos.addCell(referencia);
	    		}
	    		
	    		String descripcionCargo = cargo.getDescripcion().length() == 0?"   ":cargo.getDescripcion();
	    		
	    		AffineTransform affinetransform = new AffineTransform();
	    		FontRenderContext frc = new FontRenderContext(affinetransform,true,true); 
	    		java.awt.Font font = new java.awt.Font("HELVETICA", Font.NORMAL, 10);
	    		int textwidth = (int)(font.getStringBounds(descripcionCargo, frc).getWidth());
	    		
	    		while(textwidth >= 408)
	    		{
	    			descripcionCargo = descripcionCargo.substring(0, descripcionCargo.length() - 1);
	    			textwidth = (int)(font.getStringBounds(descripcionCargo, frc).getWidth());
	    		}
	    		
	    		PdfPCell descripcion =  new PdfPCell(new Phrase(descripcionCargo, paragraphFont));
	    		descripcion.setHorizontalAlignment(Element.ALIGN_LEFT);
	    		descripcion.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);
    	        productos.addCell(descripcion);
    	        
    	        String unidades = "";
    	        
    	        try
    	        {
    	        	unidades = String.valueOf(cargo.getUnidades());
        	        String entero = unidades.replace(".", ",");
        	        String decimales = entero.substring(entero.lastIndexOf(",") + 1);
    	        	if(Integer.parseInt(decimales) == 0)
    	        		unidades = entero.substring(0, entero.lastIndexOf(","));
    	        	else
    	        		unidades = entero;
    	        }
    	        catch (NumberFormatException e){}
    	        catch (StringIndexOutOfBoundsException e){}
	    		
	    		PdfPCell cantidad =  new PdfPCell(new Phrase((unidades.equals("0")?"":unidades), paragraphFont));
	    		cantidad.setHorizontalAlignment(Element.ALIGN_CENTER);
	    		cantidad.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);
    	        productos.addCell(cantidad);
	    		
    	        double precio = cargo.getPrecio();
    	        double descuento = cargo.getDescuento();
    	        double precioDescontado = precio - ((precio * descuento) / 100.0);
    	        
    	        String sPvp = cargo.getPrecio() == 0?"":dfe.format(precioDescontado);
    	        sPvp = sPvp.startsWith(",")?"0"+sPvp:sPvp;
	    		PdfPCell pvp =  new PdfPCell(new Phrase(sPvp, paragraphFont));
	    		pvp.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    		pvp.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);
    	        productos.addCell(pvp);

    	        String sTotal = cargo.getTotal() == 0?"":dfe.format(cargo.getTotal());
    	        sTotal = sTotal.startsWith(",")?"0"+sTotal:sTotal;
	    		PdfPCell total =  new PdfPCell(new Phrase(sTotal, paragraphFont));
	    		total.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    		total.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);
    	        productos.addCell(total);
    	        
	    		if(row == numRows - 1)
	    		{
	    			if(row < maxRowsPerPage)
	    			{
	    				int filasVacias = (int)(maxRowsPerPage) - numRows;
	    				PdfPCell celda =  new PdfPCell(new Phrase(" ", paragraphFont));
		    	    	celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		    	    	celda.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);
	    				for(int i = 0; i < filasVacias; i++)
	    					for(int j = 0; j < numColumns; j++)
	    						productos.addCell(celda);
	    			}
	    			else if(row < maxRowsPerPage * numDocu)
	    			{
	    				int filasVacias = (int)(maxRowsPerPage * numDocu) - row - 1;
	    				PdfPCell celda =  new PdfPCell(new Phrase(" ", paragraphFont));
		    	    	celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		    	    	celda.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);
	    				for(int i = 0; i < filasVacias; i++)
	    					for(int j = 0; j < numColumns; j++)
	    						productos.addCell(celda);
	    			}
	    		}
	    	}
	    	document.add(productos);
	    	
	    	generarPie(document);
	    	
	    	
    	    document.close();
    	    log.log(Level.DEBUG,"Your PDF file has been generated!(¡Se ha generado tu hoja PDF!");
		} catch (DocumentException documentException) {
			log.log(Level.DEBUG,"The file not exists (Se ha producido un error al generar un documento): " + documentException);
    	}
	}
	
	public void generarCabecera(Document document, Integer numDocu, Integer maxDocu) throws DocumentException
	{
		// DATOS
    	
    	Integer numColumns = 3;
    	// We create the table (Creamos la tabla).
    	PdfPTable datos = new PdfPTable(numColumns);
    	datos.setWidthPercentage(100);

    	if(documento.getTipo().equals("F"))
    	{
    		datos.setWidths(new int[]{160,40,160});
        	datos.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        	PdfPCell nombreEmrpesa =  new PdfPCell(new Phrase(empresa.getEmpresa(), paragraphFontBold));
        	nombreEmrpesa.setHorizontalAlignment(Element.ALIGN_LEFT);
        	nombreEmrpesa.setBorder(PdfPCell.NO_BORDER);
        	datos.addCell(nombreEmrpesa);

        	PdfPCell hueco11 =  new PdfPCell(new Phrase(""));
        	hueco11.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco11.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco11);

        	PdfPCell matricula =  new PdfPCell(new Phrase("MATRICULA " + vehiculo.getMATRICULA(), paragraphFontBold));
        	matricula.setHorizontalAlignment(Element.ALIGN_CENTER);
        	matricula.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.TOP); 
        	datos.addCell(matricula);
        	
        	PdfPCell nombre =  new PdfPCell(new Phrase(empresa.getNombre(), paragraphFont));
        	nombre.setHorizontalAlignment(Element.ALIGN_LEFT);
        	nombre.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(nombre);

        	PdfPCell hueco21 =  new PdfPCell(new Phrase(""));
        	hueco21.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco21.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco21);

        	PdfPCell vehiculoCliente =  new PdfPCell(new Phrase("VEHICULO:" + vehiculo.getMARCA() + " // " + vehiculo.getMODELO(), paragraphFont));
        	vehiculoCliente.setHorizontalAlignment(Element.ALIGN_LEFT);
        	vehiculoCliente.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT); 
        	datos.addCell(vehiculoCliente);
        	
        	PdfPCell cifEmpresa =  new PdfPCell(new Phrase("C.I.F.: " + empresa.getCif(), paragraphFont));
        	cifEmpresa.setHorizontalAlignment(Element.ALIGN_LEFT);
        	cifEmpresa.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(cifEmpresa);

        	PdfPCell hueco31 =  new PdfPCell(new Phrase(""));
        	hueco31.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco31.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco31);

        	PdfPCell motorVehiculo =  new PdfPCell(new Phrase("MOTOR: " + vehiculo.getCOMBUSTIBLE(), paragraphFont));
        	motorVehiculo.setHorizontalAlignment(Element.ALIGN_LEFT);
        	motorVehiculo.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT); 
        	datos.addCell(motorVehiculo);

        	PdfPCell calleEmpresa =  new PdfPCell(new Phrase(empresa.getDireccion(), paragraphFont));
        	calleEmpresa.setHorizontalAlignment(Element.ALIGN_LEFT);
        	calleEmpresa.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(calleEmpresa);

        	PdfPCell hueco41 =  new PdfPCell(new Phrase(""));
        	hueco41.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco41.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco41);
        	
    		if(documento.isTotalPresupuesto())
    		{
    			PdfPCell kilometrosVehiculo =  new PdfPCell(new Phrase("KM: " + dfk.format(documento.getKilometraje()), paragraphFont));
            	kilometrosVehiculo.setHorizontalAlignment(Element.ALIGN_LEFT);
            	kilometrosVehiculo.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.BOTTOM); 
            	datos.addCell(kilometrosVehiculo);
    		}
    		else
    		{
    			PdfPCell kilometrosVehiculo =  new PdfPCell(new Phrase(" ", paragraphFont));
            	kilometrosVehiculo.setHorizontalAlignment(Element.ALIGN_LEFT);
            	kilometrosVehiculo.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.BOTTOM); 
            	datos.addCell(kilometrosVehiculo);
    		}
        	
        	PdfPCell calleEmpresa2 =  new PdfPCell(new Phrase(empresa.getCodigoPostal() + " - " + empresa.getMunicipio() + " - " + empresa.getProvincia() , paragraphFont));
        	calleEmpresa2.setHorizontalAlignment(Element.ALIGN_LEFT);
        	calleEmpresa2.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(calleEmpresa2);

        	PdfPCell hueco51 =  new PdfPCell(new Phrase(""));
        	hueco51.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco51.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco51);

        	PdfPCell hueco52 =  new PdfPCell(new Phrase("", paragraphFont));
        	hueco52.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco52.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco52);
    	}
    	else
    	{
    		datos.setWidths(new int[]{160,40,160});
        	datos.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        	PdfPCell nombreEmrpesa =  new PdfPCell(new Phrase(empresa.getEmpresa(), paragraphFontBold));
        	nombreEmrpesa.setHorizontalAlignment(Element.ALIGN_LEFT);
        	nombreEmrpesa.setBorder(PdfPCell.NO_BORDER);
        	datos.addCell(nombreEmrpesa);

        	PdfPCell hueco11 =  new PdfPCell(new Phrase(""));
        	hueco11.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco11.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco11);

        	PdfPCell matricula =  new PdfPCell(new Phrase("MATRICULA " + vehiculo.getMATRICULA(), paragraphFontBold));
        	matricula.setHorizontalAlignment(Element.ALIGN_CENTER);
        	matricula.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.TOP); 
        	datos.addCell(matricula);

        	PdfPCell calleEmpresa =  new PdfPCell(new Phrase(empresa.getDireccion(), paragraphFont));
        	calleEmpresa.setHorizontalAlignment(Element.ALIGN_LEFT);
        	calleEmpresa.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(calleEmpresa);

        	PdfPCell hueco21 =  new PdfPCell(new Phrase(""));
        	hueco21.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco21.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco21);

        	PdfPCell vehiculoCliente =  new PdfPCell(new Phrase("VEHICULO:" + vehiculo.getMARCA() + " // " + vehiculo.getMODELO(), paragraphFont));
        	vehiculoCliente.setHorizontalAlignment(Element.ALIGN_LEFT);
        	vehiculoCliente.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT); 
        	datos.addCell(vehiculoCliente);
        	
        	PdfPCell calleEmpresa2 =  new PdfPCell(new Phrase(empresa.getCodigoPostal() + " - " + empresa.getMunicipio() + " - " + empresa.getProvincia() , paragraphFont));
        	calleEmpresa2.setHorizontalAlignment(Element.ALIGN_LEFT);
        	calleEmpresa2.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(calleEmpresa2);

        	PdfPCell hueco31 =  new PdfPCell(new Phrase(""));
        	hueco31.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco31.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco31);

        	PdfPCell motorVehiculo =  new PdfPCell(new Phrase("MOTOR: " + vehiculo.getCOMBUSTIBLE(), paragraphFont));
        	motorVehiculo.setHorizontalAlignment(Element.ALIGN_LEFT);
        	motorVehiculo.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT); 
        	datos.addCell(motorVehiculo);

        	PdfPCell telefonoEmpresa =  new PdfPCell(new Phrase("TELEFONO: " + empresa.getTelefono(), paragraphFont));
        	telefonoEmpresa.setHorizontalAlignment(Element.ALIGN_LEFT);
        	telefonoEmpresa.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(telefonoEmpresa);

        	PdfPCell hueco41 =  new PdfPCell(new Phrase(""));
        	hueco41.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco41.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco41);
        	
        	PdfPCell kilometrosVehiculo =  new PdfPCell(new Phrase("KM: " + dfk.format(documento.getKilometraje()), paragraphFont));
        	kilometrosVehiculo.setHorizontalAlignment(Element.ALIGN_LEFT);
        	kilometrosVehiculo.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.BOTTOM); 
        	datos.addCell(kilometrosVehiculo);
        	
        	PdfPCell vacio1 =  new PdfPCell(new Phrase("" , paragraphFont));
        	vacio1.setHorizontalAlignment(Element.ALIGN_LEFT);
        	vacio1.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(vacio1);

        	PdfPCell hueco51 =  new PdfPCell(new Phrase(""));
        	hueco51.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco51.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco51);

        	PdfPCell hueco52 =  new PdfPCell(new Phrase("", paragraphFont));
        	hueco52.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco52.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco52);

        	PdfPCell vacio2 =  new PdfPCell(new Phrase("", paragraphFont));
        	vacio2.setHorizontalAlignment(Element.ALIGN_LEFT);
        	vacio2.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(vacio2);

        	PdfPCell hueco61 =  new PdfPCell(new Phrase(""));
        	hueco61.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco61.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco61);

        	PdfPCell hueco62 =  new PdfPCell(new Phrase("", paragraphFont));
        	hueco62.setHorizontalAlignment(Element.ALIGN_LEFT);
        	hueco62.setBorder(PdfPCell.NO_BORDER);  
        	datos.addCell(hueco62);
    	}
    	
    	
    	
    	document.add(datos);
    	document.add(new Paragraph(" "));
    	
    	numColumns = 4;
    	// We create the table (Creamos la tabla).
    	PdfPTable datos2 = new PdfPTable(numColumns);
    	datos2.setWidthPercentage(100);

    	datos2.setWidths(new int[]{80,80,40,160});
    	datos2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

    	PdfPCell documentoTipo = new PdfPCell(new Phrase(documento.getTipo().equals("P")?(documento.isTotalPresupuesto()?"Presupuesto nº":""):"Factura nº", paragraphFont));
    	documentoTipo.setHorizontalAlignment(Element.ALIGN_LEFT);
    	documentoTipo.setBorder(PdfPCell.LEFT | PdfPCell.BOTTOM | PdfPCell.TOP);  
    	datos2.addCell(documentoTipo);

    	PdfPCell documentoId = new PdfPCell(new Phrase(documento.getSerie() + "/" + documento.getIdDocumento() , paragraphFontBold));
    	documentoId.setHorizontalAlignment(Element.ALIGN_CENTER);
    	documentoId.setBorder(PdfPCell.RIGHT | PdfPCell.BOTTOM | PdfPCell.TOP);  
    	datos2.addCell(documentoId);

    	PdfPCell hueco71 =  new PdfPCell(new Phrase("", paragraphFont));
    	hueco71.setHorizontalAlignment(Element.ALIGN_LEFT);
    	hueco71.setBorder(PdfPCell.NO_BORDER);  
    	datos2.addCell(hueco71);
    	
    	PdfPCell nombreCliente =  new PdfPCell(new Phrase(cliente.getNOMBRE(), paragraphFont));
    	nombreCliente.setHorizontalAlignment(Element.ALIGN_LEFT);
    	nombreCliente.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.TOP);
    	datos2.addCell(nombreCliente);

    	PdfPCell LabelFecha = new PdfPCell(new Phrase("FECHA", paragraphFont));
    	LabelFecha.setHorizontalAlignment(Element.ALIGN_LEFT);
    	LabelFecha.setBorder(PdfPCell.LEFT | PdfPCell.BOTTOM | PdfPCell.TOP);  
    	datos2.addCell(LabelFecha);

    	PdfPCell fecha = new PdfPCell(new Phrase(documento.getFecha(), paragraphFont));
    	fecha.setHorizontalAlignment(Element.ALIGN_CENTER);
    	fecha.setBorder(PdfPCell.RIGHT | PdfPCell.BOTTOM | PdfPCell.TOP);  
    	datos2.addCell(fecha);

    	PdfPCell hueco81 =  new PdfPCell(new Phrase("", paragraphFont));
    	hueco81.setHorizontalAlignment(Element.ALIGN_LEFT);
    	hueco81.setBorder(PdfPCell.NO_BORDER);  
    	datos2.addCell(hueco81);

    	PdfPCell idFiscal =  new PdfPCell(new Phrase(cliente.getIDFISCAL(), paragraphFont));
    	idFiscal.setHorizontalAlignment(Element.ALIGN_LEFT);
    	idFiscal.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);  
    	datos2.addCell(idFiscal);

    	PdfPCell LabelFormaPago = new PdfPCell(new Phrase("", paragraphFont));
    	LabelFormaPago.setHorizontalAlignment(Element.ALIGN_LEFT);
    	LabelFormaPago.setBorder(PdfPCell.NO_BORDER);  
    	datos2.addCell(LabelFormaPago);

    	PdfPCell formaPago = new PdfPCell(new Phrase("", paragraphFont));
    	formaPago.setHorizontalAlignment(Element.ALIGN_CENTER);
    	formaPago.setBorder(PdfPCell.NO_BORDER);  
    	datos2.addCell(formaPago);

    	PdfPCell hueco91 =  new PdfPCell(new Phrase("", paragraphFont));
    	hueco91.setHorizontalAlignment(Element.ALIGN_LEFT);
    	hueco91.setBorder(PdfPCell.NO_BORDER);  
    	datos2.addCell(hueco91);

    	PdfPCell direccionCliente1 =  new PdfPCell(new Phrase(cliente.getDIRECCION() + " " + cliente.getPORTAL() + " " + ((cliente.getPISO() == null)?"":cliente.getPISO()) + " " + cliente.getLETRA(), paragraphFont));
    	direccionCliente1.setHorizontalAlignment(Element.ALIGN_LEFT);
    	direccionCliente1.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);  
    	datos2.addCell(direccionCliente1);

    	if(maxDocu > 1)
    	{
        	PdfPCell numDocus = new PdfPCell(new Phrase("Nº: " + numDocu + "/" + maxDocu, paragraphFont));
        	numDocus.setHorizontalAlignment(Element.ALIGN_LEFT);
        	numDocus.setBorder(PdfPCell.LEFT | PdfPCell.BOTTOM | PdfPCell.TOP);  
        	datos2.addCell(numDocus);

        	PdfPCell hueco92 = new PdfPCell(new Phrase("", paragraphFont));
        	hueco92.setHorizontalAlignment(Element.ALIGN_CENTER);
        	hueco92.setBorder(PdfPCell.RIGHT | PdfPCell.BOTTOM | PdfPCell.TOP);  
        	datos2.addCell(hueco92);
    	}
    	else
    	{
        	PdfPCell numDocus = new PdfPCell(new Phrase("", paragraphFont));
        	numDocus.setHorizontalAlignment(Element.ALIGN_LEFT);
        	numDocus.setBorder(PdfPCell.NO_BORDER);  
        	datos2.addCell(numDocus);

        	PdfPCell hueco92 = new PdfPCell(new Phrase("", paragraphFont));
        	hueco92.setHorizontalAlignment(Element.ALIGN_CENTER);
        	hueco92.setBorder(PdfPCell.NO_BORDER);  
        	datos2.addCell(hueco92);
    	}

    	PdfPCell hueco93 =  new PdfPCell(new Phrase("", paragraphFont));
    	hueco93.setHorizontalAlignment(Element.ALIGN_LEFT);
    	hueco93.setBorder(PdfPCell.NO_BORDER);  
    	datos2.addCell(hueco93);

    	PdfPCell direccionCliente2 =  new PdfPCell(new Phrase(cliente.getCODIGOPOSTAL() + " - " + cliente.getLOCALIDAD(), paragraphFont));
    	direccionCliente2.setHorizontalAlignment(Element.ALIGN_LEFT);
    	direccionCliente2.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);  
    	datos2.addCell(direccionCliente2);

    	PdfPCell hueco101 = new PdfPCell(new Phrase("", paragraphFont));
    	hueco101.setHorizontalAlignment(Element.ALIGN_LEFT);
    	hueco101.setBorder(PdfPCell.NO_BORDER);   
    	datos2.addCell(hueco101);

    	PdfPCell hueco102 = new PdfPCell(new Phrase("", paragraphFont));
    	hueco102.setHorizontalAlignment(Element.ALIGN_CENTER);
    	hueco102.setBorder(PdfPCell.NO_BORDER);  
    	datos2.addCell(hueco102);

    	PdfPCell hueco103 =  new PdfPCell(new Phrase("", paragraphFont));
    	hueco103.setHorizontalAlignment(Element.ALIGN_LEFT);
    	hueco103.setBorder(PdfPCell.NO_BORDER);  
    	datos2.addCell(hueco103);

    	PdfPCell direccionCliente3 =  new PdfPCell(new Phrase(cliente.getPROVINCIA(), paragraphFont));
    	direccionCliente3.setHorizontalAlignment(Element.ALIGN_LEFT);
    	if(documento.getTipo().equals("P"))
        	direccionCliente3.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT); 
    	else
        	direccionCliente3.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.BOTTOM);
    	datos2.addCell(direccionCliente3);

    	PdfPCell hueco111 = new PdfPCell(new Phrase("", paragraphFont));
    	hueco111.setHorizontalAlignment(Element.ALIGN_LEFT);
    	hueco111.setBorder(PdfPCell.NO_BORDER);   
    	datos2.addCell(hueco111);

    	PdfPCell hueco112 = new PdfPCell(new Phrase("", paragraphFont));
    	hueco112.setHorizontalAlignment(Element.ALIGN_CENTER);
    	hueco112.setBorder(PdfPCell.NO_BORDER);  
    	datos2.addCell(hueco112);

    	PdfPCell hueco113 =  new PdfPCell(new Phrase("", paragraphFont));
    	hueco113.setHorizontalAlignment(Element.ALIGN_LEFT);
    	hueco113.setBorder(PdfPCell.NO_BORDER);  
    	datos2.addCell(hueco113);

    	if(documento.getTipo().equals("P"))
    	{
        	PdfPCell telefonoCliente =  new PdfPCell( new Phrase( String.valueOf((cliente.getTELEFONO() != 0?cliente.getTELEFONO():cliente.getMOVIL())), paragraphFont ) );
        	telefonoCliente.setHorizontalAlignment(Element.ALIGN_LEFT);
        	telefonoCliente.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.BOTTOM);  
        	datos2.addCell(telefonoCliente);
    	}

    	document.add(datos2);
    	document.add(new Paragraph(" "));
	}
	
	public void generarPie(Document document) throws DocumentException
	{
		// TOTALES
    	Integer numColumns = 4;
    	PdfPTable totales = new PdfPTable(numColumns); 
    	totales.setWidthPercentage(100);

    	totales.setWidths(new int[]{120,40,80,80});
    	totales.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
    	
    	String firmaCliente = "Firma Cliente:";
    	if(documento.getTipo().equals("F") || (documento.getTipo().equals("P") && !documento.isTotalPresupuesto()))
    			firmaCliente = "";
    	PdfPCell huecoTotal1 = new PdfPCell(new Phrase(firmaCliente));	 
    	huecoTotal1.setBorder(PdfPCell.LEFT | PdfPCell.TOP);
    	totales.addCell(huecoTotal1);
    	PdfPCell huecoTotal11 = new PdfPCell(new Phrase(" "));	
    	huecoTotal11.setBorder(PdfPCell.TOP);
    	totales.addCell(huecoTotal11);
    	
    	PdfPCell labelBaseImponible = null;
    	if(documento.getTipo().equals("F"))
    		labelBaseImponible =  new PdfPCell(new Phrase("Base imponible", paragraphFont));
    	else
    		labelBaseImponible =  new PdfPCell(new Phrase(" ", paragraphFont));
    	labelBaseImponible.setHorizontalAlignment(Element.ALIGN_LEFT);
    	labelBaseImponible.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.BOTTOM | PdfPCell.TOP);
    	totales.addCell(labelBaseImponible);
    	
    	PdfPCell baseImponible =  null;
    	if(documento.getTipo().equals("F"))
    		baseImponible =  new PdfPCell(new Phrase(dfe.format(baseImponibleDoc), paragraphFont));
    	else
    		baseImponible =  new PdfPCell(new Phrase(" ", paragraphFont));
    	baseImponible.setHorizontalAlignment(Element.ALIGN_RIGHT);
    	baseImponible.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.BOTTOM | PdfPCell.TOP);
    	totales.addCell(baseImponible);
    	
    	PdfPCell huecoTotal2 = new PdfPCell(new Phrase(""));		 
    	huecoTotal2.setBorder(PdfPCell.LEFT);
    	totales.addCell(huecoTotal2);
    	huecoTotal2.setBorder(PdfPCell.NO_BORDER);
    	totales.addCell(huecoTotal2);
    	
    	PdfPCell labelTotalImp = null;
    	if(documento.getTipo().equals("F"))
    		labelTotalImp =  new PdfPCell(new Phrase("Total Imp (" + documento.getImpuesto() + "%)", paragraphFont));
    	else
    		labelTotalImp =  new PdfPCell(new Phrase(" ", paragraphFont));
    	labelTotalImp.setHorizontalAlignment(Element.ALIGN_LEFT);
    	labelTotalImp.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.BOTTOM);
    	totales.addCell(labelTotalImp);
    	
    	PdfPCell totalImp = null;
    	if(documento.getTipo().equals("F"))
    		totalImp =  new PdfPCell(new Phrase(dfe.format(totalImpuesto), paragraphFont));
    	else
    		totalImp =  new PdfPCell(new Phrase(" ", paragraphFont));
    	totalImp.setHorizontalAlignment(Element.ALIGN_RIGHT);
    	totalImp.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.BOTTOM);
    	totales.addCell(totalImp);
    	
    	PdfPCell huecoTotal3 = new PdfPCell(new Phrase(""));		 
    	huecoTotal3.setBorder(PdfPCell.LEFT | PdfPCell.BOTTOM);
    	totales.addCell(huecoTotal3);	 
    	huecoTotal3.setBorder(PdfPCell.BOTTOM);
    	totales.addCell(huecoTotal3);
    	
    	String literalTotal = " Factura";
    	if(documento.getTipo().equals("P"))
    		if(documento.isTotalPresupuesto())
    			literalTotal = " Presupuesto";
    		else
    			literalTotal = "";
    	PdfPCell labelTotal =  new PdfPCell(new Phrase("Total" + literalTotal, paragraphFont));
    	labelTotal.setHorizontalAlignment(Element.ALIGN_LEFT);
    	labelTotal.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.BOTTOM);
    	totales.addCell(labelTotal);
    	
    	PdfPCell total =  new PdfPCell(new Phrase(dfe.format(totalDocumento), paragraphFont));
    	total.setHorizontalAlignment(Element.ALIGN_RIGHT);
    	total.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.BOTTOM);
    	totales.addCell(total);
    	
    	PdfPCell hueco1 =  new PdfPCell(new Phrase(" "));
    	hueco1.setHorizontalAlignment(Element.ALIGN_LEFT);
    	hueco1.setBorder(PdfPCell.NO_BORDER);  
    	totales.addCell(hueco1);
    	
    	PdfPCell hueco2 =  new PdfPCell(new Phrase(" "));
    	hueco2.setHorizontalAlignment(Element.ALIGN_LEFT);
    	hueco2.setBorder(PdfPCell.NO_BORDER);  
    	totales.addCell(hueco2);
    	
    	PdfPCell hueco3 =  new PdfPCell(new Phrase(" "));
    	hueco3.setHorizontalAlignment(Element.ALIGN_LEFT);
    	hueco3.setBorder(PdfPCell.NO_BORDER);  
    	totales.addCell(hueco3);
    	
    	PdfPCell hueco4 =  new PdfPCell(new Phrase(" "));
    	hueco4.setHorizontalAlignment(Element.ALIGN_LEFT);
    	hueco4.setBorder(PdfPCell.NO_BORDER);  
    	totales.addCell(hueco4);
	    
    	document.add(totales);
    	
    	Integer numColumnsLegal = 1;
    	PdfPTable textoLegal = new PdfPTable(numColumnsLegal); 
    	textoLegal.setWidthPercentage(100);

    	textoLegal.setWidths(new int[]{100});
    	textoLegal.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
    	
    	String parametro = "LEGAL_FACTURA";
    	if(documento.getTipo().equals("P"))
    		parametro = "LEGAL_PRESUPUESTO";
    	
    	ConexionBBDD db = new ConexionBBDD(log);
		db.conectar();
		final String sTextoLegal = util.recuperarParametro(db, log, parametro);
		db.desconectar();
    	
    	PdfPCell legal1 =  new PdfPCell(new Phrase(sTextoLegal, paragraphLegalFont));
    	legal1.setHorizontalAlignment(Element.ALIGN_LEFT);
    	legal1.setBorder(PdfPCell.NO_BORDER);
    	textoLegal.addCell(legal1);
	    
    	document.add(textoLegal);
    	document.newPage();
	}
	
	public PdfPTable generarTabla(Integer numColumns) throws DocumentException
	{
    	String[] cabeceras;
		cabeceras = new String[numColumns];
		
    	PdfPTable productos = new PdfPTable(numColumns); 
    	productos.setWidthPercentage(100);
    	
		if(tieneReferencias)
		{
			cabeceras[0] = "Ref.";
			cabeceras[1] = "Descripcion";
			cabeceras[2] = "Cta";
			cabeceras[3] = "PVP";
			cabeceras[4] = "Total";

	   		productos.setWidths(new int[]{30,160,10,20,20});
		}
		else
		{
			cabeceras[0] = "Descripcion";
			cabeceras[1] = "Cta";
			cabeceras[2] = "PVP";
			cabeceras[3] = "Total";

	   		productos.setWidths(new int[]{160,10,20,20});
		}

   		productos.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
    	PdfPCell cabecera;               
    	for (int column = 0; column < cabeceras.length; column++) {
    	    cabecera = new PdfPCell(new Phrase(cabeceras[column], paragraphFontBold));
    	    cabecera.setHorizontalAlignment(Element.ALIGN_CENTER);
    	    
    	    if(column == 0)
    	    	cabecera.setBorder(PdfPCell.LEFT | PdfPCell.TOP | PdfPCell.BOTTOM);	    	    	
    	    else if(column == cabeceras.length - 1)
    	    	cabecera.setBorder(PdfPCell.RIGHT | PdfPCell.TOP | PdfPCell.BOTTOM);
    	    else
    	    	cabecera.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM);
    	    
    	    productos.addCell(cabecera);
    	}
    	productos.setHeaderRows(1);
    	return productos;
	}
	
	/*public static void main(String args[]) throws IOException {
		PDF pdf = new PDF();
        File prueba = new File("Pruebas.pdf");
        pdf.generarFactura(prueba);
    }*/
}
