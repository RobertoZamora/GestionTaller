package com.zamora.controller;

import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Level;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.zamora.Main;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.CargoDocumento_BEAN;
import com.zamora.modelo.Cliente_BEAN;
import com.zamora.modelo.Documento_BEAN;
import com.zamora.modelo.Empresa_BEAN;
import com.zamora.modelo.Nota_BEAN;
import com.zamora.modelo.ProductoGenerico_BEAN;
import com.zamora.modelo.Vehiculo_BEAN;
import com.zamora.trazas.Trazas;
import com.zamora.util.NTPClient;
import com.zamora.util.PDF;
import com.zamora.util.Util;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Documentos_CON
{

    @FXML
    private JFXButton btnPrimero;

    @FXML
    private JFXButton btnAnterior;

    @FXML
    private JFXButton btnSiguiente;

    @FXML
    private JFXButton btnUltimo;

    @FXML
    private JFXButton btnAddDocumento;

    @FXML
    private ImageView imgAddDoc;

    @FXML
    private JFXButton btnEditarDocumento;

    @FXML
    private ImageView imgEditDoc;

    @FXML
    private JFXButton btnBorrarDocumento;

    @FXML
    private ImageView imgDelDoc;

    @FXML
    private JFXButton btnBuscarDocumento;

    @FXML
    private ImageView imgFindDoc;

    @FXML
    private JFXButton pdf;

    @FXML
    private JFXButton btnAceptar;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private JFXTextField serie;

    @FXML
    private JFXTextField idDocumento;

    @FXML
    private JFXDatePicker fecha;

    @FXML
    private JFXButton btnBuscarVehiculo;

    @FXML
    private JFXTextField matricula;

    @FXML
    private JFXTextField kilometros;

    @FXML
    private JFXTextField marcaModelo;

    @FXML
    private JFXButton btnBuscarCliente;

    @FXML
    private JFXCheckBox mosKmTotalPres;

    @FXML
    private JFXButton btnGenerarFacturaPresupuesto;

    @FXML
    private JFXTextField impuesto;

    @FXML
    private JFXTextField cliente;

    @FXML
    private JFXComboBox<String> formaDePago;

    @FXML
    private JFXButton btnProdGen;

    @FXML
    private JFXButton btnAddFila;

    @FXML
    private JFXButton btnBorrarFila;

    @FXML
    private Label labelImpuesto;

    @FXML
    private JFXTextField textTotal;

    @FXML
    private ImageView notaImportante;

    @FXML
    private HBox hboxTable;
    private JTable swingTable;
	private DefaultTableModel modeloTabla;
    
    private Trazas log;
    private Util util;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private DecimalFormat formato;
    private String tipoDoc;
    private Documento_BEAN documentoCliente;
    private int idEmpresa;
	private int numDocumentos;
	private int idDocumentoActual;
	private int serieActual;
	private int ultimoDocumento;
	private int UltimaSerie;
	private int primerDocumento;
	private int primeraSerie;
	private boolean tieneNotaImp = false;
	private boolean agnadir = false;
	private boolean editar = false;
	private String annoActual;
    
    @FXML
    void initialize()
    {

    	Tooltip tooltipPrimero = new Tooltip("Primero");
    	Tooltip tooltipAnterior = new Tooltip("Anterior");
    	Tooltip tooltipSiguiente = new Tooltip("Siguiente");
    	Tooltip tooltipUltimo = new Tooltip("Ultimo");
    	Tooltip tooltipAddDocumento = new Tooltip("Añadir Documento");
    	Tooltip tooltipEditarDocumento = new Tooltip("Editar Documento");
    	Tooltip tooltipBorrarDocumento = new Tooltip("Borrar Documento");
    	Tooltip tooltipBuscarDocumento = new Tooltip("Buscar Documento");
    	Tooltip tooltippdf = new Tooltip("Generar PDF");
    	Tooltip tooltipBuscarVehiculo = new Tooltip("Buscar Vehiculo");
    	Tooltip tooltipBuscarCliente = new Tooltip("Buscar Cliente");
    	Tooltip tooltipGenerarFacturaPresupuesto = new Tooltip("Generar Factura Presupuesto");
    	Tooltip tooltipProdGen = new Tooltip("Productos Genericos");
    	Tooltip tooltipAddFila = new Tooltip("Añadir Fila");
    	Tooltip tooltipBorrarFila  = new Tooltip("Borrar Fila");

    	btnPrimero.setTooltip(tooltipPrimero);
    	btnAnterior.setTooltip(tooltipAnterior);
    	btnSiguiente.setTooltip(tooltipSiguiente);
    	btnUltimo.setTooltip(tooltipUltimo);
    	btnAddDocumento.setTooltip(tooltipAddDocumento);
    	btnEditarDocumento.setTooltip(tooltipEditarDocumento);
    	btnBorrarDocumento.setTooltip(tooltipBorrarDocumento);
    	btnBuscarDocumento.setTooltip(tooltipBuscarDocumento);
    	pdf.setTooltip(tooltippdf);
    	btnBuscarVehiculo.setTooltip(tooltipBuscarVehiculo);
    	btnBuscarCliente.setTooltip(tooltipBuscarCliente);
    	btnGenerarFacturaPresupuesto.setTooltip(tooltipGenerarFacturaPresupuesto);
    	btnProdGen.setTooltip(tooltipProdGen);
    	btnAddFila.setTooltip(tooltipAddFila);
    	btnBorrarFila.setTooltip(tooltipBorrarFila);
    	
		ConexionBBDD db = new ConexionBBDD(log);
		util = new Util();
		db.conectar();
		formato = new DecimalFormat("###,##0.00 €");
		if("P".equals(tipoDoc))
		{
	        Image imageAddPresu = new Image(Main.class.getResource("/img/presupuestoPlus.png").toExternalForm());
	        imgAddDoc.setImage(imageAddPresu);
	        Image imageEditDoc = new Image(Main.class.getResource("/img/presupuestoEdit.png").toExternalForm());
	        imgEditDoc.setImage(imageEditDoc);
	        Image imageDelDoc = new Image(Main.class.getResource("/img/presupuestoErase.png").toExternalForm());
	        imgDelDoc.setImage(imageDelDoc);
	        Image imageFindDoc = new Image(Main.class.getResource("/img/presupuestoSearch.png").toExternalForm());
	        imgFindDoc.setImage(imageFindDoc);
	        
	        mosKmTotalPres.setText("Total Presupuesto");
	        btnGenerarFacturaPresupuesto.setVisible(false);
	        labelImpuesto.setVisible(false);
	        impuesto.setVisible(false);
	        formaDePago.setVisible(false);
		}
		
		SwingNode swingNode = new SwingNode();
		createSwingContent(swingNode);
		
		// Agregar el SwingNode al HBox
		hboxTable.getChildren().add(swingNode);
		HBox.setHgrow(swingNode, javafx.scene.layout.Priority.ALWAYS); // Establece el ancho de SwingNode para que se expanda
		

		String sFormasPago =  util.recuperarParametro(db, log, "FORMAS_PAGO");
		ArrayList<String> ALFormasPago = new ArrayList<String>();
		ALFormasPago.add("Seleccione");
		ALFormasPago.addAll(Arrays.asList(sFormasPago.split(";")));
		
		if (ALFormasPago.contains("###PRESUPUESTO###"))
		{
			ALFormasPago.remove("###PRESUPUESTO###");
			formaDePago.setVisible(true);
		}
		
		formaDePago.getItems().addAll(ALFormasPago);  
		
		idEmpresa = new Empresa_BEAN().recuperarDatosEmpresaPredefinida(db, log).getIdEmpresa();
		
		int[] documentoIdSerie = new Documento_BEAN(log).recuperarIdUltimoDocumento(db, log, tipoDoc, idEmpresa);
		
		if(documentoCliente != null)
		{
			idDocumentoActual = documentoCliente.getIdDocumento();
			serieActual = documentoCliente.getSerie();
		}
		else
		{
			idDocumentoActual = documentoIdSerie[0];
			serieActual = documentoIdSerie[1];
		}		
		
		ultimoDocumento = documentoIdSerie[0];
		UltimaSerie = documentoIdSerie[1];
		
		documentoIdSerie = new Documento_BEAN(log).recupererPrimerIdDocumento(db, log, tipoDoc, idEmpresa);
		primerDocumento = documentoIdSerie[0];
		primeraSerie = documentoIdSerie[1];
		
		numDocumentos = new Documento_BEAN(log).recupererNumDocumentos(db, log, tipoDoc, idEmpresa);
		
		documentoCliente = new Documento_BEAN(log).recuperarDocumento(db, log, serieActual, idDocumentoActual, tipoDoc, idEmpresa);
		
		// siempre deshabilitadas
		serie.setEditable(false);
		marcaModelo.setEditable(false);
		
		KeyListener tablaDatosAdapter = new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {}
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(swingTable.isEnabled())
				{
					if(e.getKeyCode() == KeyEvent.VK_TAB)
					{
						
						double total = 0.0;
						for(int i = 0; i < swingTable.getRowCount(); i++)
						{
							if(tipoDoc.equals("F"))
							{

								swingTable.setValueAt(swingTable.getValueAt(i, 0) != null ? swingTable.getValueAt(i, 0).toString().toUpperCase() : "", i, 0);
								double cantidad = swingTable.getValueAt(i, 2) != null?Double.parseDouble(swingTable.getValueAt(i, 2).toString()):0.0;
								double precio = swingTable.getValueAt(i, 3) != null?Double.parseDouble(swingTable.getValueAt(i, 3).toString()):0.0;
								int descuento = swingTable.getValueAt(i, 4) != null?Integer.parseInt(swingTable.getValueAt(i, 4).toString()):0;
								
								double precioTotal = precio * cantidad;
								double descontar = (precioTotal * descuento) / 100;
								double totalProducto = precioTotal - descontar;
								total += totalProducto;
								swingTable.setValueAt(Math.round(totalProducto * 100.0) / 100.0, i, 5);
							}
							else
							{
								swingTable.setValueAt(swingTable.getValueAt(i, 0).toString().toUpperCase(), i, 0);
								double cantidad = swingTable.getValueAt(i, 1) != null && !"".equals(swingTable.getValueAt(i, 1))?Double.parseDouble(swingTable.getValueAt(i, 1).toString()):0.0;
								double precio = swingTable.getValueAt(i, 2) != null && !"".equals(swingTable.getValueAt(i, 2))?Double.parseDouble(swingTable.getValueAt(i, 2).toString()):0.0;
																
								double precioTotal = precio * cantidad;
								total += precioTotal;
								swingTable.setValueAt(Math.round(precioTotal*100.0)/100.0, i, 3);
							}
						}
						total = total + (total * Integer.parseInt(impuesto != null && !impuesto.getText().trim().equals("")?impuesto.getText():"0") / 100);
						textTotal.setText(formato.format(total));
						
						if(tipoDoc.equals("F"))
						{
							if((swingTable.getColumnCount() - 1) == swingTable.getSelectedColumn())
							{
								modeloTabla.addRow(new Object[] {"", "", null, null, null, null});
								swingTable.changeSelection(swingTable.getSelectedRow() + 1, 0, false, false);
							}
						}
						else
						{
							if((swingTable.getColumnCount() - 1) == swingTable.getSelectedColumn())
							{
								modeloTabla.addRow(new Object[] {"", null, null});
								swingTable.changeSelection(swingTable.getSelectedRow() + 1, 0, false, false);
							}
							
						}
					}					
				}
			}
		};

		swingTable.addKeyListener(tablaDatosAdapter);
		
		cargarDocumento(db, null);
		modoVisualizacion();
		activarBotonesNavegacion();
		
		db.desconectar();
    }
    
    public void setDato(Trazas log, String tipoDoc, Documento_BEAN documentoCliente)
    {
    	this.log = log;
    	this.tipoDoc = tipoDoc;
    	this.documentoCliente = documentoCliente;
    }
    
    private void createSwingContent(final SwingNode swingNode)
    {
        swingTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(swingTable);
        swingNode.setContent(scrollPane);
    }

    @FXML
    void primero(MouseEvent event)
    {
		ConexionBBDD db = new ConexionBBDD(log);
		db.conectar();
		
		idDocumentoActual = primerDocumento;
		serieActual = primeraSerie;

		documentoCliente = documentoCliente.recuperarDocumento(db, log, serieActual, idDocumentoActual, tipoDoc, idEmpresa);
		
    	cargarDocumento(db, null);
		activarBotonesNavegacion();
		
    	db.desconectar();
    }

    @FXML
    void anterior(MouseEvent event)
    {
    	ConexionBBDD db = new ConexionBBDD(log);
		db.conectar();
		
		int[] documentoIdSerie = documentoCliente.recupererAnteriorIdDocumento(db, log, idDocumentoActual, tipoDoc, idEmpresa, serieActual);
		
		idDocumentoActual = documentoIdSerie[0];
		serieActual = documentoIdSerie[1];

		documentoCliente = documentoCliente.recuperarDocumento(db, log, serieActual, idDocumentoActual, tipoDoc, idEmpresa);
		
    	cargarDocumento(db, null);
		activarBotonesNavegacion();
		
    	db.desconectar();
    }

    @FXML
    void siguiente(MouseEvent event)
    {
    	ConexionBBDD db = new ConexionBBDD(log);
		db.conectar();
		
		int[] documentoIdSerie = documentoCliente.recupererSiguienteIdDocumento(db, log, idDocumentoActual, tipoDoc, idEmpresa, serieActual);
		
		idDocumentoActual = documentoIdSerie[0];
		serieActual = documentoIdSerie[1];

		documentoCliente = documentoCliente.recuperarDocumento(db, log, serieActual, idDocumentoActual, tipoDoc, idEmpresa);
		
    	cargarDocumento(db, null);
		activarBotonesNavegacion();
		
    	db.desconectar();

    }

    @FXML
    void ultimo(MouseEvent event)
    {
		ConexionBBDD db = new ConexionBBDD(log);
		db.conectar();
		
		idDocumentoActual = ultimoDocumento;
		serieActual = UltimaSerie;

		documentoCliente = documentoCliente.recuperarDocumento(db, log, serieActual, idDocumentoActual, tipoDoc, idEmpresa);
		
    	cargarDocumento(db, null);
		activarBotonesNavegacion();
		
    	db.desconectar();

    }

    @FXML
    void addDocumento(MouseEvent event)
    {
    	agnadir = true;
    	modoEdicion();

		ConexionBBDD db = new ConexionBBDD(log);
		db.conectar();
    	
		NTPClient ntp = new NTPClient(log);
		Date fechaActual = ntp.getTime();
		if(fechaActual == null)
			fechaActual = new Date();
		
		annoActual = new SimpleDateFormat("yy").format(fechaActual);
		
		// Convertimos java.util.Date a Instant
        Instant instant = fechaActual.toInstant();

        // Luego, obtenemos una LocalDate desde el Instant, especificando una zona horaria (ZoneId)
        ZoneId zoneId = ZoneId.systemDefault(); // Puedes cambiar a la zona horaria que necesites
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
		
		serie.setText(new SimpleDateFormat("yy").format(fechaActual));
    	idDocumento.setText(String.valueOf(documentoCliente.recupererNexID(db, log, tipoDoc, idEmpresa, Integer.parseInt(annoActual))));
    	fecha.setValue(localDate);
    	matricula.setText("");
    	marcaModelo.setText("");
    	cliente.setText("");
    	kilometros.setText("");
    	mosKmTotalPres.setSelected(false);
    	
    	Image imageNotaImportante = new Image(Main.class.getResource("/img/verde.png").toExternalForm());
    	notaImportante.setImage(imageNotaImportante);
    	
    	Tooltip tooltip = new Tooltip();
    	Tooltip.uninstall(notaImportante, tooltip);
    	
    	if("F".equals(tipoDoc))
    	{
        	impuesto.setText("21");
        	
        	kilometros.setText("");
        	
            formaDePago.getSelectionModel().select(0);                
            
            modeloTabla = new DefaultTableModel	(new Object[][]{}
			,new String[] {"REFERENCIA", "DESCRIPCION", "UNIDADES", "PVP", "DTO", "TOTAL"}) {
				private static final long serialVersionUID = 3634681328632062693L;
				
				boolean[] columnEditables = new boolean[] {
					true, true, true, true, true, false
				};
				
				@SuppressWarnings("rawtypes")
				Class[] types = new Class[] {java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class};
				
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public Class getColumnClass(int columnIndex)
				{
					return types[columnIndex];
				}
				
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
			swingTable.setModel(modeloTabla);
			swingTable.getColumnModel().getColumn(0).setPreferredWidth(200);
			swingTable.getColumnModel().getColumn(0).setResizable(false);
			swingTable.getColumnModel().getColumn(1).setPreferredWidth(350);
			swingTable.getColumnModel().getColumn(1).setResizable(false);
			swingTable.getColumnModel().getColumn(2).setPreferredWidth(100);
			swingTable.getColumnModel().getColumn(2).setResizable(false);
			swingTable.getColumnModel().getColumn(3).setPreferredWidth(100);
			swingTable.getColumnModel().getColumn(3).setResizable(false);
			swingTable.getColumnModel().getColumn(4).setPreferredWidth(100);
			swingTable.getColumnModel().getColumn(4).setResizable(false);
			swingTable.getColumnModel().getColumn(5).setPreferredWidth(100);
			swingTable.getColumnModel().getColumn(5).setResizable(false);
            
			modeloTabla.getDataVector().removeAllElements();
			modeloTabla.fireTableDataChanged();
			
			double total = 0;
			
			total = total + (total * Integer.parseInt(impuesto != null && !impuesto.getText().trim().equals("")?impuesto.getText():"0") / 100);
            textTotal.setText(formato.format(total));
    	}
    	else
    	{
    		modeloTabla = new DefaultTableModel	(new Object[][]{}
			,new String[] {"DESCRIPCION", "UNIDADES", "PVP", "TOTAL"}) {
				private static final long serialVersionUID = 3634681328632062693L;
				
				boolean[] columnEditables = new boolean[] {
					true, true, true, false
				};
				
				@SuppressWarnings("rawtypes")
				Class[] types = new Class[] {java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class};
				
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public Class getColumnClass(int columnIndex)
				{
					return types[columnIndex];
				}
				
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
			swingTable.setModel(modeloTabla);
			swingTable.getColumnModel().getColumn(0).setPreferredWidth(350);
			swingTable.getColumnModel().getColumn(0).setResizable(false);
			swingTable.getColumnModel().getColumn(1).setPreferredWidth(100);
			swingTable.getColumnModel().getColumn(1).setResizable(false);
			swingTable.getColumnModel().getColumn(2).setPreferredWidth(100);
			swingTable.getColumnModel().getColumn(2).setResizable(false);
			swingTable.getColumnModel().getColumn(3).setPreferredWidth(100);
			swingTable.getColumnModel().getColumn(3).setResizable(false);
			
			modeloTabla.getDataVector().removeAllElements();
			modeloTabla.fireTableDataChanged();
			
			double total = 0;
			                
            textTotal.setText(formato.format(total));
    	}
    	db.desconectar();
    }

    @FXML
    void editDocumento(MouseEvent event)
    {
    	editar = true;
    	modoEdicion();
    	
    	NTPClient ntp = new NTPClient(log);
		Date fechaActual = ntp.getTime();
		if(fechaActual == null)
			fechaActual = new Date();
		
		annoActual = new SimpleDateFormat("yy").format(fechaActual);
    }

    @FXML
    void borrarDocumento(MouseEvent event)
    {
    	try
		{
    		if(util.confirmar("Se va a proceder a borrar este documento ¿esta seguro?"))
    		{
        		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/Password_PAN.fxml"));
    			
    			loader.setControllerFactory(controllerClass -> {
    	            if (controllerClass == Password_CON.class) {
    	            	Password_CON pass = new Password_CON();
    	                pass.setDato(log);
    	                return pass;
    	            }
    	            return null;
    	        });
    			
    			Parent root = loader.load();
    			
    			Stage seleccionable = new Stage();
    			seleccionable.setTitle("Contraseña");

    			Image icon = new Image(Main.class.getResource("/img/car.png").toExternalForm());
    			seleccionable.getIcons().add(icon);

    			seleccionable.setScene(new Scene(root));

    			// Configura la ventana secundaria como modal
    			seleccionable.initModality(Modality.APPLICATION_MODAL);

    			// Configura el evento para establecer el tamaño mínimo
    			seleccionable.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
    	            @Override
    	            public void handle(WindowEvent event) {
    	            	seleccionable.setMinWidth(seleccionable.getWidth());
    	            	seleccionable.setMinHeight(seleccionable.getHeight());
    	            }
    	        });

    			seleccionable.showAndWait(); // Muestra la ventana y espera hasta que se cierre
    			String aux = ((Password_CON)loader.getController()).getDato();

    			ConexionBBDD db = new ConexionBBDD(log);
    	    	db.conectar();
    	    		    	
    	    	String pass = util.recuperarParametro(db, log, "PASSWORD");
    	    	
    	    	if(!"".equals(aux))
    	    	{
    	    		if(pass.equals(aux))
    		    	{
    	    			int idAux = idDocumentoActual;
    					int serieAux = serieActual;
    					
    					int[] anterior = documentoCliente.recupererAnteriorIdDocumento(db, log, idDocumentoActual, tipoDoc, idEmpresa, serieActual);							
    					int idAnterior = anterior[0];
    					int serieAnterior = anterior[1];
    					
    					int[] siguiente = documentoCliente.recupererSiguienteIdDocumento(db, log, idDocumentoActual, tipoDoc, idEmpresa, serieActual);
    					int idSiguiente = siguiente[0];
    					int serieSiguiente = siguiente[1];
    					
    					if(idAnterior != 0)
    					{
    						idDocumentoActual = idAnterior;
    						serieActual = serieAnterior;
    					}
    					else if (idSiguiente != 0)
    					{
    						idDocumentoActual = idSiguiente;
    						serieActual = serieSiguiente;
    					}
    					else
    					{
    						idDocumentoActual = 0;
    						serieActual = 0;
    					}

    					documentoCliente.borrarDocumento(db, log, idAux, tipoDoc, idEmpresa, serieAux);
						
						int[] documentoIdSerie = documentoCliente.recuperarIdUltimoDocumento(db, log, tipoDoc, idEmpresa);
						ultimoDocumento = documentoIdSerie[0];
						UltimaSerie = documentoIdSerie[1];									
						numDocumentos = documentoCliente.recupererNumDocumentos(db, log, tipoDoc, idEmpresa);
						
						documentoCliente = new Documento_BEAN(log).recuperarDocumento(db, log, serieActual, idDocumentoActual, tipoDoc, idEmpresa);
    					
    					if(idDocumentoActual != 0)
    					{
    						cargarDocumento(db, null);
    					}
    					else
    					{
    						serie.setText(new SimpleDateFormat("yy").format(new Date()));
    						idDocumento.setText("");
    						fecha.setValue(null);
    						matricula.setText("");
    						kilometros.setText("");
    						matricula.setText("");
    						cliente.setText("");
    						formaDePago.setValue("EFECTIVO");
    						mosKmTotalPres.setSelected(false);
    						modeloTabla.getDataVector().removeAllElements();
    						modeloTabla.fireTableDataChanged();
    					}
    					
    					activarBotonesNavegacion();
    		    	}
    	    		else
    	    		{
    	    			Alert alert = new Alert(Alert.AlertType.ERROR);
    	    			alert.setHeaderText(null);
    	    			alert.setTitle("Error");
    	    			alert.setContentText("Contraseña incorrecta");
    	    			alert.showAndWait();
    	    		}
    	    	}
    			db.desconectar();
    		}
		}
		catch (IOException e)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			e.printStackTrace();
			log.log(Level.ERROR, e);
		}
    }

    @FXML
    void buscarDocumento(MouseEvent event)
    {
    	try {
    		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/BusquedaDocumentos_PAN.fxml"));
			
			loader.setControllerFactory(controllerClass -> {
                if (controllerClass == BusquedaDocumentos_CON.class) {
                	BusquedaDocumentos_CON seleccion = new BusquedaDocumentos_CON();
                    seleccion.setDato(log, tipoDoc, idEmpresa);
                    return seleccion;
                }
                return null;
            });
			
			Parent root = loader.load();
			
			Stage seleccionable = new Stage();

			Image icon = null;
			
			if("F".equals(tipoDoc))
			{
				icon = new Image(Main.class.getResource("/img/factura.png").toExternalForm());
				seleccionable.setTitle("Buscar Factura");
			}
			else
			{
				icon = new Image(Main.class.getResource("/img/presupuesto.png").toExternalForm());
				seleccionable.setTitle("Buscar Presupuesto");
				
			}
			
			seleccionable.getIcons().add(icon);

			seleccionable.setScene(new Scene(root));

			// Configura la ventana secundaria como modal
			seleccionable.initModality(Modality.APPLICATION_MODAL);

			// Configura el evento para establecer el tamaño mínimo
			seleccionable.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                	seleccionable.setMinWidth(seleccionable.getWidth());
                	seleccionable.setMinHeight(seleccionable.getHeight());
                }
            });

			seleccionable.showAndWait(); // Muestra la ventana y espera hasta que se cierre
			Documento_BEAN aux = ((BusquedaDocumentos_CON)loader.getController()).getDato();
			
			if(aux != null)
			{

		    	ConexionBBDD conexion = new ConexionBBDD(log);
		    	conexion.conectar();
		    	
				documentoCliente = aux;
				serieActual = documentoCliente.getSerie();
				idDocumentoActual = documentoCliente.getIdDocumento();
				cargarDocumento(conexion, null);
				activarBotonesNavegacion();
				
				conexion.desconectar();
				
			}
			
	    } catch (Exception e) {
	    	Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
	        e.printStackTrace();
			log.log(Level.ERROR, e);
	    }
    }

    @FXML
    void pdf(MouseEvent event)
    {
    	ConexionBBDD db = new ConexionBBDD(log);
		db.conectar();

		Empresa_BEAN emp = new Empresa_BEAN().recuperarDatosEmpresa(db, log, idEmpresa);
		if(emp.getEmpresa() != null && !emp.getEmpresa().equals(""))
		{
			Documento_BEAN docu = new Documento_BEAN(log).recuperarDocumento(db, log, serieActual, idDocumentoActual, tipoDoc, idEmpresa);
			Cliente_BEAN cliente = new Cliente_BEAN().recuperarClienteDatosDoc(db, log, docu.getIdCliente());
			Vehiculo_BEAN vehiculo = new Vehiculo_BEAN().recuperarVehiculo(db, log, docu.getIdVehiculo());
			
			// Crea un FileChooser
			DirectoryChooser directoryChooser = new DirectoryChooser();
	        directoryChooser.setTitle("Selecciona una carpeta");
	        
	        File documentsDir = Paths.get(System.getProperty("user.home"), "Documents").toFile();
	        File fallbackDir = new File(System.getProperty("user.home"));
	        
	        // Establece el directorio inicial en la carpeta home del usuario
	        if (documentsDir.exists() && documentsDir.isDirectory()) {
	            directoryChooser.setInitialDirectory(documentsDir);
	        } else {
	            directoryChooser.setInitialDirectory(fallbackDir);
	        }   

	        // Abre el diálogo de selección de carpeta
	        File selectedDirectory = directoryChooser.showDialog(((Stage) pdf.getScene().getWindow()));
			
			if(selectedDirectory != null)
			{
				String directorio = selectedDirectory.getAbsolutePath();
				
				String rutaArchivo = directorio + File.separator + docu.getSerie() + "_" + docu.getIdDocumento() + "_" + cliente.getIDFISCAL() + "_" + "_" + vehiculo.getMATRICULA() + tipoDoc + ".pdf";
				
				PDF pdf = new PDF(cliente, vehiculo, docu, emp, new File(rutaArchivo), log);
				pdf.generarFactura();
				
				//WordToPdfConverter pdf = new WordToPdfConverter();
				
				try {
					File file = new File(rutaArchivo);
					Desktop.getDesktop().open(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}			
		}
		else
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText("Para poder generar documentos debe rellenar los datos de la empresa.");
			alert.showAndWait();	
		}
		db.desconectar();
    }

    @FXML
    void cambiarFecha(ActionEvent event)
    {
    	if((agnadir || editar))
    	{    		
    		String annoIntroducido = DateTimeFormatter.ofPattern("yy").format(fecha.getValue());
    		String annoDocumento = documentoCliente.getFecha().substring(documentoCliente.getFecha().length() - 2);
			
    		if(editar && !annoIntroducido.equals(annoDocumento))
			{				
				fecha.setValue(LocalDate.parse(documentoCliente.getFecha(), formatter));

		    	Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setTitle("Error");
				alert.setContentText("No esta permitido cambiar la serie de un documento ya creado.");
				alert.showAndWait();
			}
    		else if (agnadir)
    		{
    			if(!annoActual.equals(annoIntroducido))
    			{
    				if(editar)
    				{    					
    					fecha.setValue(LocalDate.parse(documentoCliente.getFecha(), formatter));

    			    	Alert alert = new Alert(Alert.AlertType.ERROR);
    					alert.setHeaderText(null);
    					alert.setTitle("Error");
    					alert.setContentText("No esta permitido cambiar la serie de un documento ya creado.");
    					alert.showAndWait();
    				}
    				else
    				{
    					ConexionBBDD db = new ConexionBBDD(log);
    					db.conectar();
    					log.log(Level.DEBUG, "La serie elegida no es la misma a la actual.");
    					serie.setText(annoIntroducido);
    					idDocumento.setText(String.valueOf(documentoCliente.recuperarNextIdSerie(db, log, tipoDoc, idEmpresa, Integer.parseInt(annoIntroducido))));
    					db.desconectar();
    				}
    			}
    			else
    			{
    				ConexionBBDD db = new ConexionBBDD(log);
    				db.conectar();
    				log.log(Level.DEBUG, "La serie elegida no es la misma a la actual.");
    				serie.setText(annoIntroducido);
    				idDocumento.setText(String.valueOf(documentoCliente.recupererNexID(db, log, tipoDoc, idEmpresa, Integer.parseInt(annoIntroducido))));
    				db.desconectar();
    			}
    		}
    	}
    }

    @FXML
    void buscarVehiculo(MouseEvent event)
    {
    	try {
    		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/BusquedaVehiculo_PAN.fxml"));
			
			loader.setControllerFactory(controllerClass -> {
                if (controllerClass == BusquedaVehiculo_CON.class) {
                	BusquedaVehiculo_CON seleccion = new BusquedaVehiculo_CON();
                    seleccion.setDato(log, null);
                    return seleccion;
                }
                return null;
            });
			
			Parent root = loader.load();
			
			Stage seleccionable = new Stage();
			seleccionable.setTitle("Buscar Vehiculo");

			Image icon = new Image(Main.class.getResource("/img/car.png").toExternalForm());
			seleccionable.getIcons().add(icon);

			seleccionable.setScene(new Scene(root));

			// Configura la ventana secundaria como modal
			seleccionable.initModality(Modality.APPLICATION_MODAL);

			// Configura el evento para establecer el tamaño mínimo
			seleccionable.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                	seleccionable.setMinWidth(seleccionable.getWidth());
                	seleccionable.setMinHeight(seleccionable.getHeight());
                }
            });

			seleccionable.showAndWait(); // Muestra la ventana y espera hasta que se cierre
			Vehiculo_BEAN aux = ((BusquedaVehiculo_CON)loader.getController()).getDato();
			
			if(aux != null)
			{
				matricula.setText(aux.getMATRICULA());
				marcaModelo.setText(aux.getMARCA() + "/" + aux.getMODELO());
				ConexionBBDD db = new ConexionBBDD(log);
				db.conectar();
				Cliente_BEAN clie = new Cliente_BEAN();
				cliente.setText(clie.recuperarClienteMatricula(db, log, aux.getMATRICULA()).getIDFISCAL());
				
				double deuda = clie.recuperarDeudaCliente(db, log, aux.getID_CLIENTE());
				
				if(deuda > 0)
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setTitle("Error");
					alert.setContentText("El cliente tiene una deuda " + deuda + "€.");
					alert.showAndWait();
				}
				
				String sNotaImportante = "";
	    		if(documentoCliente != null)
	    		{
	    			sNotaImportante = new Nota_BEAN().clienteConNotaImportante(db, log, aux.getID_CLIENTE());
	    			tieneNotaImp = sNotaImportante.length() > 0;
	    		}
	        	
	        	Image imageNotaImportante;
	        	Tooltip tooltip = new Tooltip();
	        	if(tieneNotaImp)
	        	{
	        		tooltip.setText(sNotaImportante);
	            	Tooltip.install(notaImportante, tooltip);
	        		imageNotaImportante = new Image(Main.class.getResource("/img/rojo.png").toExternalForm());
	        	}
	        	else
	        	{
	            	Tooltip.uninstall(notaImportante, tooltip);
	        		imageNotaImportante = new Image(Main.class.getResource("/img/verde.png").toExternalForm());
	        	}
	        	
	        	notaImportante.setImage(imageNotaImportante);
				
				db.desconectar();
			}
			
	    } catch (Exception e) {
	    	Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
	        e.printStackTrace();
			log.log(Level.ERROR, e);
	    }
    }

    @FXML
    void buscarCliente(MouseEvent event)
    {
    	try {
    		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/BusquedaClientes_PAN.fxml"));
			
			loader.setControllerFactory(controllerClass -> {
                if (controllerClass == BusquedaClientes_CON.class) {
                	BusquedaClientes_CON seleccion = new BusquedaClientes_CON();
                    seleccion.setDato(log);
                    return seleccion;
                }
                return null;
            });
			
			Parent root = loader.load();
			
			Stage seleccionable = new Stage();
			seleccionable.setTitle("Buscar Cliente");

			Image icon = new Image(Main.class.getResource("/img/cliente.png").toExternalForm());
			seleccionable.getIcons().add(icon);

			seleccionable.setScene(new Scene(root));

			// Configura la ventana secundaria como modal
			seleccionable.initModality(Modality.APPLICATION_MODAL);

			// Configura el evento para establecer el tamaño mínimo
			seleccionable.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                	seleccionable.setMinWidth(seleccionable.getWidth());
                	seleccionable.setMinHeight(seleccionable.getHeight());
                }
            });

			seleccionable.showAndWait(); // Muestra la ventana y espera hasta que se cierre
			Cliente_BEAN aux = ((BusquedaClientes_CON)loader.getController()).getDato();
			
			if(aux != null)
			{				
				ConexionBBDD db = new ConexionBBDD(log);
				db.conectar();
				ArrayList<Vehiculo_BEAN> vehiculosCliente = new Vehiculo_BEAN().recuperarVehiculosCliente(db, log, aux.getID_CLIENTE());
				
				if(vehiculosCliente.size() == 0)
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setTitle("Error");
					alert.setContentText("El cliente no tiene vehiculos.");
					alert.showAndWait();
				}
				else if (vehiculosCliente.size() == 1)
				{
					cliente.setText(aux.getIDFISCAL());
					matricula.setText(vehiculosCliente.get(0).getMATRICULA());
				}
				else
				{
					cliente.setText(aux.getIDFISCAL());
					
					FXMLLoader loaderVehiculo = new FXMLLoader(Main.class.getResource("/vistas/BusquedaVehiculo_PAN.fxml"));
					
					loaderVehiculo.setControllerFactory(controllerClass -> {
		                if (controllerClass == BusquedaVehiculo_CON.class) {
		                	BusquedaVehiculo_CON seleccion = new BusquedaVehiculo_CON();
		                    seleccion.setDato(log, vehiculosCliente);
		                    return seleccion;
		                }
		                return null;
		            });
					
					Parent rootVehiculo = loaderVehiculo.load();
					
					Stage seleccionableVehiculo = new Stage();
					seleccionableVehiculo.setTitle("Buscar Vehiculo");

					Image iconVehiculo = new Image(Main.class.getResource("/img/car.png").toExternalForm());
					seleccionableVehiculo.getIcons().add(iconVehiculo);

					seleccionableVehiculo.setScene(new Scene(rootVehiculo));

					// Configura la ventana secundaria como modal
					seleccionableVehiculo.initModality(Modality.APPLICATION_MODAL);

					// Configura el evento para establecer el tamaño mínimo
					seleccionableVehiculo.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
		                @Override
		                public void handle(WindowEvent event) {
		                	seleccionableVehiculo.setMinWidth(seleccionableVehiculo.getWidth());
		                	seleccionableVehiculo.setMinHeight(seleccionableVehiculo.getHeight());
		                }
		            });

					seleccionableVehiculo.showAndWait(); // Muestra la ventana y espera hasta que se cierre
					Vehiculo_BEAN auxVehiculo = ((BusquedaVehiculo_CON)loaderVehiculo.getController()).getDato();
					
					if(auxVehiculo != null)
					{
						matricula.setText(auxVehiculo.getMATRICULA());
						marcaModelo.setText(auxVehiculo.getMARCA() + "/" + auxVehiculo.getMODELO());
					}					
				}
				
				double deuda = aux.recuperarDeudaCliente(db, log, aux.getID_CLIENTE());
				
				if(deuda > 0)
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setTitle("Error");
					alert.setContentText("El cliente tiene una deuda " + deuda + "€.");
					alert.showAndWait();
				}
				
				String sNotaImportante = "";
	    		if(documentoCliente != null)
	    		{
	    			sNotaImportante = new Nota_BEAN().clienteConNotaImportante(db, log, aux.getID_CLIENTE());
	    			tieneNotaImp = sNotaImportante.length() > 0;
	    		}
	        	
	        	Image imageNotaImportante;
	        	Tooltip tooltip = new Tooltip();
	        	if(tieneNotaImp)
	        	{
	        		tooltip.setText(sNotaImportante);
	            	Tooltip.install(notaImportante, tooltip);
	        		imageNotaImportante = new Image(Main.class.getResource("/img/rojo.png").toExternalForm());
	        	}
	        	else
	        	{
	            	Tooltip.uninstall(notaImportante, tooltip);
	        		imageNotaImportante = new Image(Main.class.getResource("/img/verde.png").toExternalForm());
	        	}
	        	
	        	notaImportante.setImage(imageNotaImportante);
				
				db.desconectar();
				
			}
	    } catch (Exception e) {
	    	Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
	        e.printStackTrace();
			log.log(Level.ERROR, e);
	    }
    }

    @FXML
    void generarFactPres(MouseEvent event)
    {
    	try {
	    	FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/BusquedaDocumentos_PAN.fxml"));
			
			loader.setControllerFactory(controllerClass -> {
	            if (controllerClass == BusquedaDocumentos_CON.class) {
	            	BusquedaDocumentos_CON seleccion = new BusquedaDocumentos_CON();
	                seleccion.setDato(log, "P", idEmpresa);
	                return seleccion;
	            }
	            return null;
	        });
			
			Parent root = loader.load();
			
			Stage seleccionable = new Stage();
	
			Image icon = new Image(Main.class.getResource("/img/presupuesto.png").toExternalForm());
			seleccionable.setTitle("Buscar Presupuesto");
			
			seleccionable.getIcons().add(icon);
	
			seleccionable.setScene(new Scene(root));
	
			// Configura la ventana secundaria como modal
			seleccionable.initModality(Modality.APPLICATION_MODAL);
	
			// Configura el evento para establecer el tamaño mínimo
			seleccionable.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent event) {
	            	seleccionable.setMinWidth(seleccionable.getWidth());
	            	seleccionable.setMinHeight(seleccionable.getHeight());
	            }
	        });
	
			seleccionable.showAndWait(); // Muestra la ventana y espera hasta que se cierre
			Documento_BEAN aux = ((BusquedaDocumentos_CON)loader.getController()).getDato();
			
			if(aux != null)
			{

				boolean desglosarIva = util.confirmar("¿Desea desglosar el I.V.A a los productos en el presupuesto?");
				
				int idAux = aux.getIdDocumento();
				if(idAux != -1)
				{	
					Documento_BEAN documentoCarga = new Documento_BEAN(log);
					documentoCarga.setSerie(Integer.parseInt(serie.getText()));
					documentoCarga.setIdDocumento(Integer.parseInt(idDocumento.getText()));		
					documentoCarga.setFecha(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(fecha.getValue()));
					documentoCarga.setIdCliente(aux.getIdCliente());
					documentoCarga.setIdEmpresa(aux.getIdEmpresa());
					documentoCarga.setKilometraje(aux.getKilometraje());
					documentoCarga.setIdVehiculo(aux.getIdVehiculo());
					documentoCarga.setTipo("F");
					documentoCarga.setFormaPago(String.valueOf(formaDePago.getValue()));
					documentoCarga.setImpuesto(Integer.parseInt(impuesto != null && !impuesto.getText().trim().equals("")?impuesto.getText():"0"));
					if(desglosarIva)
					{
						log.log(Level.DEBUG, "desglosamos iva");
						for(int i = 0; i < aux.getCargos().size(); i++)
						{
							double precioTotal = aux.getCargos().get(i).getTotal();
							double precio = aux.getCargos().get(i).getPrecio();
							double cantidad = aux.getCargos().get(i).getUnidades();
							
							precio = precio / (((double)(documentoCarga.getImpuesto()) + 100.0) / 100.0) ;
							precio = Math.round(precio * 100.0) / 100.0;
							precioTotal = precio * cantidad;
							aux.getCargos().get(i).setPrecio(precio);
							aux.getCargos().get(i).setTotal(precioTotal);									
						}
					}
					documentoCarga.setCargos(aux.getCargos());
					
					ConexionBBDD db = new ConexionBBDD(log);
					db.conectar();
					
					String sNotaImportante = new Nota_BEAN().clienteConNotaImportante(db, log, documentoCarga.getIdCliente());
					tieneNotaImp = sNotaImportante.length() > 0;

				    formato.applyPattern("###,###.00 €");
					
				    cargarDocumento(db, documentoCarga);
					
					db.desconectar();
				}			
			}
	    } catch (Exception e) {
	    	Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
	        e.printStackTrace();
			log.log(Level.ERROR, e);
	    }
    }

    @FXML
    void prodGen(MouseEvent event)
    {
    	try {
    		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/vistas/Generico_PAN.fxml"));
			
			loader.setControllerFactory(controllerClass -> {
                if (controllerClass == Generico_CON.class) {
                	Generico_CON seleccion = new Generico_CON();
                    seleccion.setDato(log);
                    return seleccion;
                }
                return null;
            });
			
			Parent root = loader.load();
			
			Stage seleccionable = new Stage();
			seleccionable.setTitle("Producto Generico");

			Image icon = new Image(Main.class.getResource("/img/car.png").toExternalForm());
			seleccionable.getIcons().add(icon);

			seleccionable.setScene(new Scene(root));

			// Configura la ventana secundaria como modal
			seleccionable.initModality(Modality.APPLICATION_MODAL);

			// Configura el evento para establecer el tamaño mínimo
			seleccionable.setOnShown(new javafx.event.EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                	seleccionable.setMinWidth(seleccionable.getWidth());
                	seleccionable.setMinHeight(seleccionable.getHeight());
                }
            });

			seleccionable.showAndWait(); // Muestra la ventana y espera hasta que se cierre
			ArrayList<ProductoGenerico_BEAN> aux = ((Generico_CON)loader.getController()).getDato();
			
			if(aux != null)
			{
				if(swingTable.getRowCount() > 0)
				{
					ArrayList<Integer> filasRemover = new ArrayList<Integer>();
					
					for(int i = 0; i < swingTable.getRowCount(); i++)
					{
						
						String descripcion = "";
						double total;
						if(tipoDoc.equals("F"))
						{
							descripcion = String.valueOf(swingTable.getModel().getValueAt(i, 1));
							total = swingTable.getModel().getValueAt(i, 5) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 5))):0.0;
						}
						else
						{
							descripcion = String.valueOf(swingTable.getModel().getValueAt(i, 0));
							total = swingTable.getModel().getValueAt(i, 3) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 3))):0.0;
						}
								
						if(descripcion.length() == 0 && total == 0)
							filasRemover.add(i);
					}
					
					for(int i = filasRemover.size() - 1; i > -1; i--)
						modeloTabla.removeRow(filasRemover.get(i));
				}
				
				for(int i = 0; i < aux.size(); i++)
					if(tipoDoc.equals("F"))
					{
						String referencia = "";
						String descripcion = aux.get(i).getDESCRIPCION();
						Double unidades = aux.get(i).getUNIDADES() != 0.0?(double)aux.get(i).getUNIDADES():null;
						Double pvp = aux.get(i).getPVP() != 0.0?aux.get(i).getPVP():null;
						Integer dto = aux.get(i).getDTO() != 0.0?aux.get(i).getDTO():null;
						Double total = aux.get(i).getTOTAL() != 0.0?aux.get(i).getTOTAL():null;
						modeloTabla.addRow(new Object[] {referencia, descripcion, unidades, pvp, dto, total});								
					}
					else
					{
						String descripcion = aux.get(i).getDESCRIPCION();
						Double unidades = aux.get(i).getUNIDADES() != 0.0?(double)aux.get(i).getUNIDADES():null;
						Double pvp = aux.get(i).getPVP() != 0.0?aux.get(i).getPVP():null;
						Double total = aux.get(i).getTOTAL() != 0.0?aux.get(i).getTOTAL():null;
						modeloTabla.addRow(new Object[] {descripcion, unidades, pvp, total});	
					}
				
				double total = 0.0;
				for(int i = 0; i < swingTable.getRowCount(); i++)
				{
					if(tipoDoc.equals("F"))
					{
						swingTable.setValueAt(swingTable.getValueAt(i, 0) != null ? swingTable.getValueAt(i, 0).toString().toUpperCase() : "", i, 0);
						double cantidad = (Double) (swingTable.getValueAt(i, 2) != null?swingTable.getValueAt(i, 2):0.0);
						double precio = (Double) (swingTable.getValueAt(i, 3) != null?swingTable.getValueAt(i, 3):0.0);
						int descuento = (Integer) (swingTable.getValueAt(i, 4) != null?swingTable.getValueAt(i, 4):0);
						
						double precioTotal = precio * cantidad;
						double descontar = (precioTotal * descuento) / 100;
						double totalProducto = precioTotal - descontar;
						total += totalProducto;
						swingTable.setValueAt(Math.round(totalProducto * 100.0) / 100.0, i, 5);
					}
					else
					{
						swingTable.setValueAt(swingTable.getValueAt(i, 0).toString().toUpperCase(), i, 0);
						double cantidad = (Double) (swingTable.getValueAt(i, 1) != null?swingTable.getValueAt(i, 1):0.0);
						double precio = (Double) (swingTable.getValueAt(i, 2) != null?swingTable.getValueAt(i, 2):0.0);
														
						double precioTotal = precio * cantidad;
						total += precioTotal;
						swingTable.setValueAt(Math.round(precioTotal*100.0)/100.0, i, 3);
					}
				}
				total = total + (total * Integer.parseInt(impuesto != null && !impuesto.getText().trim().equals("")?impuesto.getText():"0") / 100);
				textTotal.setText(formato.format(total));
			}
			
	    } catch (Exception e) {
	    	Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
	        e.printStackTrace();
			log.log(Level.ERROR, e);
			log.log(Level.ERROR, e);
	    }
    }

    @FXML
    void addFila(MouseEvent event)
    {
    	if(tipoDoc.equals("P"))
		{
			if(swingTable.getSelectedRow() != -1)
			{
				int indice = swingTable.getSelectedRow();
				modeloTabla.insertRow(indice + 1, new Object[] {"", null, null});
			}
			else
			{
				modeloTabla.addRow(new Object[] {"", null, null});
			}
		}
		else
		{
			if(swingTable.getSelectedRow() != -1)
			{
				int indice = swingTable.getSelectedRow();
				modeloTabla.insertRow(indice + 1, new Object[] {"", "", null, null, null, null});
			}
			else
			{
				modeloTabla.addRow(new Object[] {"", "", null, null, null, null});
			}
		}
    }

    @FXML
    void borrarFila(MouseEvent event)
    {
    	if(swingTable.isEditing())
    		swingTable.getCellEditor().stopCellEditing();
		
		if(swingTable.getSelectedRow() != -1)
		{
			modeloTabla.removeRow(swingTable.getSelectedRow());
		}
		else if(swingTable.getRowCount() > 0)
		{
			modeloTabla.removeRow(swingTable.getRowCount() - 1);
		}
    }

    @FXML
    void aceptar(MouseEvent event)
    {
    	
    	if(swingTable.isEnabled())
		{
			double total = 0.0;
			for(int i = 0; i < swingTable.getRowCount(); i++)
			{
				if(tipoDoc.equals("F"))
				{
					swingTable.setValueAt(swingTable.getValueAt(i, 0) != null ? swingTable.getValueAt(i, 0).toString().toUpperCase() : "", i, 0);
					double cantidad = swingTable.getValueAt(i, 2) != null?Double.parseDouble(swingTable.getValueAt(i, 2).toString()):0.0;
					double precio = swingTable.getValueAt(i, 3) != null?Double.parseDouble(swingTable.getValueAt(i, 3).toString()):0.0;
					int descuento = swingTable.getValueAt(i, 4) != null?Integer.parseInt(swingTable.getValueAt(i, 4).toString()):0;
					
					double precioTotal = precio * cantidad;
					double descontar = (precioTotal * descuento) / 100;
					double totalProducto = precioTotal - descontar;
					total += totalProducto;
					swingTable.setValueAt(Math.round(totalProducto * 100.0) / 100.0, i, 5);
				}
				else
				{
					swingTable.setValueAt(swingTable.getValueAt(i, 0).toString().toUpperCase(), i, 0);
					double cantidad = swingTable.getValueAt(i, 1) != null && !"".equals(swingTable.getValueAt(i, 1))?Double.parseDouble(swingTable.getValueAt(i, 1).toString()):0.0;
					double precio = swingTable.getValueAt(i, 2) != null && !"".equals(swingTable.getValueAt(i, 2))?Double.parseDouble(swingTable.getValueAt(i, 2).toString()):0.0;
													
					double precioTotal = precio * cantidad;
					total += precioTotal;
					swingTable.setValueAt(Math.round(precioTotal*100.0)/100.0, i, 3);
				}
			}
			total = total + (total * Integer.parseInt(impuesto != null && !impuesto.getText().trim().equals("")?impuesto.getText():"0") / 100);
			textTotal.setText(formato.format(total));				
		}

		boolean correcto = true;
		String error = "";
		int descripcionesVacias = 0;
		int cargosSinRellenar = 0;
		int cargosRellenos = 0;
		ConexionBBDD db = new ConexionBBDD(log);
		db.conectar();
		Vehiculo_BEAN vehiculoBean = new Vehiculo_BEAN();
		Cliente_BEAN clienteBean = new Cliente_BEAN();
		
		if(!clienteBean.existeIdFiscal(db, log, cliente.getText()))
		{
			correcto = false;
			error += "La identificacion fiscal no existe, si no la conoce por favor use la busqueda.";
		}
		
		if(!vehiculoBean.existeMatricula(db, log, matricula.getText()))
		{
			correcto = false;
			error += "\nLa Matricula no existe, si no la conoce por favor use la busqueda.";
		}
		
		if(!Util.isNumeric(kilometros.getText()))
		{
			correcto = false;
			error += "\nEl kilometraje debe ser numerico.";
		}
		
		if(swingTable.getRowCount() > 0)
		{
			
			ArrayList<Integer> filasRemover = new ArrayList<Integer>();
			boolean eliminar = true;
			for(int i = swingTable.getRowCount() - 1; i >= 0 ; i--)
			{
				
				String descripcion = "";
				double total;
				if(tipoDoc.equals("F"))
				{
					descripcion = String.valueOf(swingTable.getModel().getValueAt(i, 1));
					total = swingTable.getModel().getValueAt(i, 5) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 5))):0.0;					
				}
				else
				{
					descripcion = String.valueOf(swingTable.getModel().getValueAt(i, 0));
					total = swingTable.getModel().getValueAt(i, 3) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 3))):0.0;
				}
						
				if(descripcion.length() == 0 && total != 0)
				{
					correcto = false;
					descripcionesVacias++;
				}
				else if(descripcion.length() == 0 && total == 0 && eliminar)
				{
					filasRemover.add(i);
					cargosSinRellenar++;
				}
				else
				{
					cargosRellenos++;
					eliminar = false;
				}
			}
			
			for(int i = 0; i < filasRemover.size(); i++)
				modeloTabla.removeRow(filasRemover.get(i));
			
			if(cargosRellenos == 0 && cargosSinRellenar == swingTable.getRowCount())
			{
				correcto = false;
				error += "\nNo hay cargo\\s en " + (tipoDoc.equals("F")?"Factura":"Presupuesto") + " por favor rellenelos.";
			}
			
			if(descripcionesVacias > 0)
				error += "\nHay " + descripcionesVacias + " descripcion\\es vacias por favor rellenlas.";
		}
		else
		{
			correcto = false;
			error += "\nNo hay cargo\\s en " + (tipoDoc.equals("F")?"Factura":"Presupuesto") + " por favor rellenelos.";
		}
		
		
		int idCliente = clienteBean.recuperarIdIdFiscal(db, log, cliente.getText());
		int idMatricula = vehiculoBean.recuperarIdMatricula(db, log, matricula.getText());
							
		if(correcto)
		{
			Documento_BEAN doc = new Documento_BEAN(log);
			doc.setIdCliente(idCliente);
			doc.setIdVehiculo(idMatricula);
			doc.setIdDocumento(Integer.parseInt(idDocumento.getText()));
			doc.setIdEmpresa(idEmpresa);
			doc.setTipo(tipoDoc);
			doc.setSerie(Integer.parseInt(serie.getText()));
			doc.setImpuesto(Integer.parseInt(impuesto != null && !impuesto.getText().trim().equals("")?impuesto.getText():"0"));
			doc.setFecha(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(fecha.getValue()));
			doc.setKilometraje(Integer.parseInt(kilometros.getText()));
			doc.setTotalPresupuesto(mosKmTotalPres.isSelected());
			if(formaDePago.isVisible())
				doc.setFormaPago(String.valueOf(formaDePago.getValue()));
			
			ArrayList<CargoDocumento_BEAN> cargos = new ArrayList<CargoDocumento_BEAN>();
			
			for(int i = 0; i < swingTable.getRowCount(); i++)
			{
				CargoDocumento_BEAN cargo = new CargoDocumento_BEAN();
				cargo.setIdDocumento(doc.getIdDocumento());
				cargo.setIdCargo(i);
				
				
				if(tipoDoc.equals("F"))
				{
					cargo.setReferencia(swingTable.getModel().getValueAt(i, 0) != null?String.valueOf(swingTable.getModel().getValueAt(i, 0)).toUpperCase():"");
					cargo.setDescripcion(String.valueOf(swingTable.getModel().getValueAt(i, 1)).toUpperCase());
					cargo.setUnidades(swingTable.getModel().getValueAt(i, 2) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 2))):0.0);
					cargo.setPrecio(swingTable.getModel().getValueAt(i, 3) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 3))):0.0);
					cargo.setDescuento(swingTable.getModel().getValueAt(i, 4) != null?Integer.parseInt(String.valueOf(swingTable.getModel().getValueAt(i, 4))):0);
					cargo.setTotal(swingTable.getModel().getValueAt(i, 5) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 5))):0.0);
					
				}
				else
				{
					cargo.setDescripcion(String.valueOf(swingTable.getModel().getValueAt(i, 0)).toUpperCase());
					cargo.setUnidades(swingTable.getModel().getValueAt(i, 1) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 1))):0.0);
					cargo.setPrecio(swingTable.getModel().getValueAt(i, 2) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 2))):0.0);
					cargo.setTotal(swingTable.getModel().getValueAt(i, 3) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 3))):0.0);
				}
				cargos.add(cargo);
			}
			doc.setCargos(cargos);
			
			String annoIntroducido = DateTimeFormatter.ofPattern("yyyy").format(fecha.getValue());
			NTPClient ntp = new NTPClient(log);
			Date fechaActual = ntp.getTime();
			if(fechaActual == null)
				fechaActual = new Date();
			
			String annoActual = new SimpleDateFormat("yyyy").format(fechaActual);
						
			if(agnadir)
			{
				doc.insertarDocumento(db, log, !annoActual.equals(annoIntroducido));
				cliente.setEditable(false);
			}
			else if (editar)
				doc.actualizarDocumento(db, log);
			
			int[] documentoIdSerie = doc.recuperarIdUltimoDocumento(db, log, tipoDoc, idEmpresa); 
			idDocumentoActual = doc.getIdDocumento();
			serieActual = doc.getSerie();
			
			ultimoDocumento = documentoIdSerie[0];
			UltimaSerie = documentoIdSerie[1];
			
			documentoIdSerie = documentoCliente.recupererPrimerIdDocumento(db, log, tipoDoc, idEmpresa); 
			primerDocumento = documentoIdSerie[0];
			primeraSerie = documentoIdSerie[1];
			
			numDocumentos = documentoCliente.recupererNumDocumentos(db, log, tipoDoc, idEmpresa);
			
			documentoCliente = doc;
			
			agnadir = false;
			editar = false;
			modoVisualizacion();
			activarBotonesNavegacion();
			cargarDocumento(db, null);
		}
		else
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Error");
			alert.setContentText(error);
			alert.showAndWait();
		}
		db.desconectar();
    }

    @FXML
    void cancelar(MouseEvent event)
    {
		ConexionBBDD db = new ConexionBBDD(log);
		db.conectar();
		
    	modoVisualizacion();
    	cargarDocumento(db, null);
		
    	db.desconectar();

    }
    
    public void modoEdicion()
    {
		swingTable.setEnabled(true);
		kilometros.setDisable(false);
		fecha.setDisable(false);
		
		mosKmTotalPres.setDisable(false);
		if(agnadir)
			btnGenerarFacturaPresupuesto.setDisable(false);
		impuesto.setDisable(false);
		formaDePago.setDisable(false);
		btnProdGen.setDisable(false);
		btnAddFila.setDisable(false);
		btnBorrarFila.setDisable(false);
		
		btnBuscarCliente.setDisable(false);
		btnBuscarVehiculo.setDisable(false);
		
		btnAceptar.setDisable(false);
		btnCancelar.setDisable(false);
		
		btnAddDocumento.setDisable(true);
		btnEditarDocumento.setDisable(true);
		btnBorrarDocumento.setDisable(true);
		btnBuscarDocumento.setDisable(true);
		pdf.setDisable(true);
		
		btnPrimero.setDisable(true);
		btnAnterior.setDisable(true);
		btnSiguiente.setDisable(true);
		btnUltimo.setDisable(true);
		
    }

    public void modoVisualizacion()
    {	
		swingTable.setEnabled(false);
		kilometros.setDisable(true);
		fecha.setDisable(true);
		
		mosKmTotalPres.setDisable(true);
		btnGenerarFacturaPresupuesto.setDisable(true);
		impuesto.setDisable(true);
		formaDePago.setDisable(true);
		btnProdGen.setDisable(true);
		btnAddFila.setDisable(true);
		btnBorrarFila.setDisable(true);
		
		btnBuscarCliente.setDisable(true);
		btnBuscarVehiculo.setDisable(true);
		
		btnAceptar.setDisable(true);
		btnCancelar.setDisable(true);    
		
		agnadir = false;
		editar = false;
		
		btnAddDocumento.setDisable(false);
		btnEditarDocumento.setDisable(false);
		btnBorrarDocumento.setDisable(false);
		btnBuscarDocumento.setDisable(false);
		pdf.setDisable(false);
		
		activarBotonesNavegacion();
    }
    
    public void  cargarDocumento(ConexionBBDD db, Documento_BEAN documentoCarga)
    {
    	Documento_BEAN documentoAux = null;
    	if(documentoCliente != null)
    	{
    		if(documentoCarga != null)
    		{
    			documentoAux = documentoCliente;
    			documentoCliente = documentoCarga;
    		}    			
    		
        	Cliente_BEAN clienteBean = new Cliente_BEAN().recuperarClienteDatosDoc(db, log, documentoCliente.getIdCliente());
        	Vehiculo_BEAN vehiculoBean = new Vehiculo_BEAN().recuperarVehiculo(db, log, documentoCliente.getIdVehiculo());
        	
        	serie.setText(String.valueOf(documentoCliente.getSerie()));
        	idDocumento.setText(String.valueOf(documentoCliente.getIdDocumento()));
        	fecha.setValue(LocalDate.parse(documentoCliente.getFecha(), formatter));
        	matricula.setText(vehiculoBean.getMATRICULA());
        	marcaModelo.setText(vehiculoBean.getMARCA() + "/" + vehiculoBean.getMODELO());
        	cliente.setText(clienteBean.getIDFISCAL());
        	mosKmTotalPres.setSelected(documentoCliente.isTotalPresupuesto());
        	kilometros.setText(String.valueOf(documentoCliente.getKilometraje()));
        	if("F".equals(tipoDoc))
        	{
            	impuesto.setText(String.valueOf(documentoCliente.getImpuesto()));
            	            	
            	int indice = 0;
                for(int i = 0; i < formaDePago.getItems().size() && indice == 0; i++)
                {
                	if(formaDePago.getItems().get(i).equals(documentoCliente.getFormaPago()))
                	{
                		indice = i;
                	}
            	}
                formaDePago.getSelectionModel().select(indice);                
                
                modeloTabla = new DefaultTableModel	(new Object[][]{}
    			,new String[] {"REF", "DESCRIPCION", "UNIDADES", "PVP", "DTO", "TOTAL"}) {
    				private static final long serialVersionUID = 3634681328632062693L;
    				
    				boolean[] columnEditables = new boolean[] {
    						true, true, true, true, true, false
    				};
    				
    				@SuppressWarnings("rawtypes")
    				Class[] types = new Class[] {java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class};
    				
    				@SuppressWarnings({ "rawtypes", "unchecked" })
    				public Class getColumnClass(int columnIndex)
    				{
    					return types[columnIndex];
    				}
    				
    				public boolean isCellEditable(int row, int column) {
    					return columnEditables[column];
    				}
    			};
    			swingTable.setModel(modeloTabla);
    			swingTable.getColumnModel().getColumn(0).setPreferredWidth(200);
    			swingTable.getColumnModel().getColumn(0).setResizable(false);
    			swingTable.getColumnModel().getColumn(1).setPreferredWidth(350);
    			swingTable.getColumnModel().getColumn(1).setResizable(false);
    			swingTable.getColumnModel().getColumn(2).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(2).setResizable(false);
    			swingTable.getColumnModel().getColumn(3).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(3).setResizable(false);
    			swingTable.getColumnModel().getColumn(4).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(4).setResizable(false);
    			swingTable.getColumnModel().getColumn(5).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(5).setResizable(false);
                
    			modeloTabla.getDataVector().removeAllElements();
    			modeloTabla.fireTableDataChanged();
    			
    			double total = 0;
    			for(int i = 0; i < documentoCliente.getCargos().size(); i++)
    			{
    				CargoDocumento_BEAN cargoActual = documentoCliente.getCargos().get(i);
					modeloTabla.addRow(new Object[] {
							cargoActual.getReferencia()
							, cargoActual.getDescripcion()
							, cargoActual.getUnidades()
							, cargoActual.getPrecio()
							, cargoActual.getDescuento()
							, cargoActual.getTotal()});
    				
    				total += cargoActual.getTotal();
    			}
    			total = total + (total * Integer.parseInt(impuesto != null && !impuesto.getText().trim().equals("")?impuesto.getText():"0") / 100);
                textTotal.setText(formato.format(total));
        	}
        	else
        	{
        		modeloTabla = new DefaultTableModel	(new Object[][]{}
    			,new String[] {"DESCRIPCION", "UNIDADES", "PVP", "TOTAL"}) {
    				private static final long serialVersionUID = 3634681328632062693L;
    				
    				boolean[] columnEditables = new boolean[] {
    					true, true, true, false
    				};
    				
    				@SuppressWarnings("rawtypes")
    				Class[] types = new Class[] {java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class};
    				
    				@SuppressWarnings({ "rawtypes", "unchecked" })
    				public Class getColumnClass(int columnIndex)
    				{
    					return types[columnIndex];
    				}
    				
    				public boolean isCellEditable(int row, int column) {
    					return columnEditables[column];
    				}
    			};
    			swingTable.setModel(modeloTabla);
    			swingTable.getColumnModel().getColumn(0).setPreferredWidth(350);
    			swingTable.getColumnModel().getColumn(0).setResizable(false);
    			swingTable.getColumnModel().getColumn(1).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(1).setResizable(false);
    			swingTable.getColumnModel().getColumn(2).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(2).setResizable(false);
    			swingTable.getColumnModel().getColumn(3).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(3).setResizable(false);
    			
    			modeloTabla.getDataVector().removeAllElements();
    			modeloTabla.fireTableDataChanged();
    			
    			double total = 0;
    			for(int i = 0; i < documentoCliente.getCargos().size(); i++)
    			{
    				CargoDocumento_BEAN cargoActual = documentoCliente.getCargos().get(i);
					modeloTabla.addRow(new Object[] {cargoActual.getDescripcion()
							, cargoActual.getUnidades()
							, cargoActual.getPrecio()
							, cargoActual.getTotal()});
    				
    				total += cargoActual.getTotal();
    			}
                
                textTotal.setText(formato.format(total));
        	}
        	
        	double deuda = new Cliente_BEAN().recuperarDeudaCliente(db, log, documentoCliente.getIdCliente());
        	String mensajeDeuda = "";
			if(deuda > 0)
			{
				mensajeDeuda = "El cliente tiene una deuda " + deuda + "€.";
			}
        	
        	String sNotaImportante = "";
    		if(documentoCliente != null)
    		{
    			sNotaImportante = new Nota_BEAN().clienteConNotaImportante(db, log, documentoCliente.getIdCliente());
    			tieneNotaImp = sNotaImportante.length() > 0;
    		}
        	
        	Image imageNotaImportante;
        	Tooltip tooltip = new Tooltip();
        	String sTooltip = "";
        	if(tieneNotaImp)
        	{
        		if(mensajeDeuda.length() > 0)
        		{
        			sTooltip = mensajeDeuda + "\n";
        		}
        		sTooltip += sNotaImportante;
        		tooltip.setText(sTooltip);
            	Tooltip.install(notaImportante, tooltip);
        		imageNotaImportante = new Image(Main.class.getResource("/img/rojo.png").toExternalForm());
        	}
        	else
        	{
        		if(mensajeDeuda.length() > 0)
        		{
            		tooltip.setText(mensajeDeuda);
                	Tooltip.install(notaImportante, tooltip);
            		imageNotaImportante = new Image(Main.class.getResource("/img/rojo.png").toExternalForm());
        		}
        		else
        		{
            		imageNotaImportante = new Image(Main.class.getResource("/img/verde.png").toExternalForm());
                	Tooltip.uninstall(notaImportante, tooltip);
        		}
        	}
        	
        	notaImportante.setImage(imageNotaImportante);
        	

    		if(documentoCarga != null)
    		{
    			documentoCarga = documentoCliente;
    			documentoCliente = documentoAux;
    		}
    	}
    	else
    	{
    		documentoCliente = new Documento_BEAN(log);
    		serie.setText("");
        	idDocumento.setText("");
        	fecha.setValue(null);
        	matricula.setText("");
        	marcaModelo.setText("");
        	cliente.setText("");
        	mosKmTotalPres.setSelected(false);
        	
        	Image imageNotaImportante = new Image(Main.class.getResource("/img/verde.png").toExternalForm());
        	notaImportante.setImage(imageNotaImportante);
        	
        	Tooltip tooltip = new Tooltip();
        	Tooltip.uninstall(notaImportante, tooltip);
        	
        	if("F".equals(tipoDoc))
        	{
            	impuesto.setText("");
            	
            	kilometros.setText("");
            	
                formaDePago.getSelectionModel().select(0);                
                
                modeloTabla = new DefaultTableModel	(new Object[][]{}
    			,new String[] {"REF", "DESCRIPCION", "UNIDADES", "PVP", "DTO", "TOTAL"}) {
    				private static final long serialVersionUID = 3634681328632062693L;
    				
    				boolean[] columnEditables = new boolean[] {
    					true, true, true, true, true, false
    				};
    				
    				@SuppressWarnings("rawtypes")
    				Class[] types = new Class[] {java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class};
    				
    				@SuppressWarnings({ "rawtypes", "unchecked" })
    				public Class getColumnClass(int columnIndex)
    				{
    					return types[columnIndex];
    				}
    				
    				public boolean isCellEditable(int row, int column) {
    					return columnEditables[column];
    				}
    			};
    			swingTable.setModel(modeloTabla);
    			swingTable.getColumnModel().getColumn(0).setPreferredWidth(200);
    			swingTable.getColumnModel().getColumn(0).setResizable(false);
    			swingTable.getColumnModel().getColumn(1).setPreferredWidth(350);
    			swingTable.getColumnModel().getColumn(1).setResizable(false);
    			swingTable.getColumnModel().getColumn(2).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(2).setResizable(false);
    			swingTable.getColumnModel().getColumn(3).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(3).setResizable(false);
    			swingTable.getColumnModel().getColumn(4).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(4).setResizable(false);
    			swingTable.getColumnModel().getColumn(5).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(5).setResizable(false);
                
    			modeloTabla.getDataVector().removeAllElements();
    			modeloTabla.fireTableDataChanged();
    			
    			double total = 0;
    			
    			total = total + (total * Integer.parseInt(impuesto != null && !impuesto.getText().trim().equals("")?impuesto.getText():"0") / 100);
                textTotal.setText(formato.format(total));
        	}
        	else
        	{
        		modeloTabla = new DefaultTableModel	(new Object[][]{}
    			,new String[] {"DESCRIPCION", "UNIDADES", "PVP", "TOTAL"}) {
    				private static final long serialVersionUID = 3634681328632062693L;
    				
    				boolean[] columnEditables = new boolean[] {
    					true, true, true, false
    				};
    				
    				@SuppressWarnings("rawtypes")
    				Class[] types = new Class[] {java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class};
    				
    				@SuppressWarnings({ "rawtypes", "unchecked" })
    				public Class getColumnClass(int columnIndex)
    				{
    					return types[columnIndex];
    				}
    				
    				public boolean isCellEditable(int row, int column) {
    					return columnEditables[column];
    				}
    			};
    			swingTable.setModel(modeloTabla);
    			swingTable.getColumnModel().getColumn(0).setPreferredWidth(350);
    			swingTable.getColumnModel().getColumn(0).setResizable(false);
    			swingTable.getColumnModel().getColumn(1).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(1).setResizable(false);
    			swingTable.getColumnModel().getColumn(2).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(2).setResizable(false);
    			swingTable.getColumnModel().getColumn(3).setPreferredWidth(100);
    			swingTable.getColumnModel().getColumn(3).setResizable(false);
    			
    			modeloTabla.getDataVector().removeAllElements();
    			modeloTabla.fireTableDataChanged();
    			
    			double total = 0;
    			                
                textTotal.setText(formato.format(total));
        	}
    		modeloTabla.getDataVector().removeAllElements();
    		modeloTabla.fireTableDataChanged();
    	}
    }
    
    public void activarBotonesNavegacion()
	{
		if((idDocumentoActual == primerDocumento && serieActual == primeraSerie) || numDocumentos == 0)
		{
			btnPrimero.setDisable(true);
			btnAnterior.setDisable(true);
		}
		else
		{
			btnPrimero.setDisable(false);
			btnAnterior.setDisable(false);
		}
		
		if((idDocumentoActual == ultimoDocumento && serieActual == UltimaSerie) || numDocumentos == 0)
		{
			btnSiguiente.setDisable(true);
			btnUltimo.setDisable(true);
		}
		else
		{
			btnSiguiente.setDisable(false);
			btnUltimo.setDisable(false);
		}
	}
}
