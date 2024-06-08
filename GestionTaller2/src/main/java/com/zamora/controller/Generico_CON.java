package com.zamora.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.zamora.BBDD.ConexionBBDD;
import com.zamora.modelo.ProductoGenerico_BEAN;
import com.zamora.trazas.Trazas;
import com.zamora.util.Util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Generico_CON
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<String> comboGenericos;

    @FXML
    private JFXButton guardar;

    @FXML
    private JFXButton aceptar;

    @FXML
    private JFXButton cancelar;

    @FXML
    private JFXTextField idNombre;

    @FXML
    private JFXButton addFila;

    @FXML
    private JFXButton borrarFila;

    @FXML
    private HBox hBoxTabla;
    
	private KeyListener tablaDatosAdapter;
    
    private Trazas log;
    private Util util;
    ArrayList<ProductoGenerico_BEAN> generico;
    ArrayList<ProductoGenerico_BEAN> retornoGenerico;
    private JTable swingTable;
	private DefaultTableModel modeloTabla;
	private String NUEVO = "Nuevo...";

    @FXML
    void cambiarGenerico(ActionEvent event)
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	
    	generico.removeAll(generico);
		
		modeloTabla.getDataVector().removeAllElements();
		modeloTabla.fireTableDataChanged();
		
    	if(!comboGenericos.getSelectionModel().getSelectedItem().equals(NUEVO))
    	{
    		if(comboGenericos.getItems().size() > 0)
    		{
    			generico = new ProductoGenerico_BEAN().recuperarGenerico(conexion, log, comboGenericos.getSelectionModel().getSelectedItem());
    		}
        	
        	for(int i = 0; i < generico.size(); i++)
    		{
    			modeloTabla.addRow(new Object[] {
    					generico.get(i).getDESCRIPCION()
    						, generico.get(i).getUNIDADES()
    						, generico.get(i).getPVP()
    						, generico.get(i).getDTO()
    						, generico.get(i).getTOTAL()
    			});
    		}
        	
        	idNombre.setText(comboGenericos.getSelectionModel().getSelectedItem());
    	}
    	else
    	{
        	idNombre.setText("");
    	}
    	
		conexion.desconectar();
    }

    @FXML
    void guardar(MouseEvent event)
    {

		boolean correcto = true;
		boolean insertar = true;
		String error = "";
		int descripcionesVacias = 0;
		int cargosSinRellenar = 0;
		int cargosRellenos = 0;
		
		if(!idNombre.getText().isEmpty())
		{						

			if(comboGenericos.getItems().indexOf(idNombre.getText()) != -1
					&& comboGenericos.getSelectionModel().getSelectedItem().toString().equals(NUEVO))
			{
				if(util.confirmar("¿El nombre ya existe si continua sobreescribira la configuracion de productos?"))
				{
					correcto = true;
					insertar = false;
				}
				else
					correcto = false;
			}
			else if(comboGenericos.getItems().indexOf(idNombre.getText()) != -1
					&& !comboGenericos.getSelectionModel().getSelectedItem().toString().equals(NUEVO)
					&& !comboGenericos.getSelectionModel().getSelectedItem().toString().equals(idNombre.getText()))
			{
				if(util.confirmar("¿El nombre ya existe si continua sobreescribira la configuracion de productos?"))
				{
					correcto = true;
					insertar = false;
				}
				else
					correcto = false;
			}
			else if(comboGenericos.getItems().indexOf(idNombre.getText()) != -1
					&& !comboGenericos.getSelectionModel().getSelectedItem().toString().equals(NUEVO)
					&& comboGenericos.getSelectionModel().getSelectedItem().toString().equals(idNombre.getText()))
			{
				insertar = false;
			}
		}
		else
		{
			correcto = false;
			error += "El nombre debe estar relleno.";
		}
		
		if(swingTable.getRowCount() > 0)
		{
			for(int i = 0; i < swingTable.getRowCount(); i++)
			{
				
				String descripcion = String.valueOf(swingTable.getModel().getValueAt(i, 0));
				double total;
				total = swingTable.getModel().getValueAt(i, 4) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 4))):0.0;
						
				if(descripcion.length() == 0 && total != 0)
				{
					correcto = false;
					descripcionesVacias++;
				}
				else if(descripcion.length() == 0 && total == 0)
				{
					modeloTabla.removeRow(i);
					cargosSinRellenar++;
				}
				else
				{
					cargosRellenos++;
				}
			}
			
			if(cargosRellenos == 0 && cargosSinRellenar == swingTable.getRowCount())
			{
				correcto = false;
				error += "\nNo hay cargo\\s por favor rellenelos.";
			}
			
			if(descripcionesVacias > 0)
				error += "\nHay " + descripcionesVacias + " descripcion\\es vacias por favor rellenlas.";
		}
		else
		{
			correcto = false;
			error += "\nNo hay cargo\\s por favor rellenelos.";
		}
							
		if(correcto)
		{
			ConexionBBDD db = new ConexionBBDD(log);
			db.conectar();
			
			if(!insertar)
			{
				if(!idNombre.getText().equals(comboGenericos.getSelectionModel().getSelectedItem().toString()) 
						&& !comboGenericos.getSelectionModel().getSelectedItem().toString().equals(NUEVO))
					new ProductoGenerico_BEAN().borrarGenerico(db, log, comboGenericos.getSelectionModel().getSelectedItem().toString());
				new ProductoGenerico_BEAN().borrarGenerico(db, log, idNombre.getText());
			}
			
			generico.removeAll(generico);
			for(int i = 0; i < swingTable.getRowCount(); i++)
			{
				String nombreAux = idNombre.getText();
				String descripcionAux = String.valueOf(swingTable.getModel().getValueAt(i, 0)).toUpperCase();
				double unidadesAux = swingTable.getModel().getValueAt(i, 1) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 1))):0.0;
				double precioAux = swingTable.getModel().getValueAt(i, 2) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 2))):0.0;
				int descuentoAux = swingTable.getModel().getValueAt(i, 3) != null?Integer.parseInt(String.valueOf(swingTable.getModel().getValueAt(i, 3))):0;
				double totalAux = swingTable.getModel().getValueAt(i, 4) != null?Double.parseDouble(String.valueOf(swingTable.getModel().getValueAt(i, 4))):0.0;

				ProductoGenerico_BEAN genericoInsertar = new ProductoGenerico_BEAN(nombreAux, descripcionAux, unidadesAux, precioAux, descuentoAux, totalAux);
				
				genericoInsertar.insertarGenerico(db, log);
				generico.add(genericoInsertar);
			}
			aceptar(event);
			db.desconectar();
		}
		else
		{
			if(!error.isEmpty())
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setTitle("Error");
				alert.setContentText(error);
				alert.showAndWait();
			}
		}
    }

    @FXML
    void aceptar(MouseEvent event)
    {
    	retornoGenerico = generico;
    	Stage stage = (Stage) aceptar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void cancelar(MouseEvent event)
    {	    	
    	modeloTabla.getDataVector().removeAllElements();
		modeloTabla.fireTableDataChanged();
		
    	if(!comboGenericos.getSelectionModel().getSelectedItem().equals(NUEVO))
    	{        	
        	for(int i = 0; i < generico.size(); i++)
    		{
    			modeloTabla.addRow(new Object[] {
    					generico.get(i).getDESCRIPCION()
    						, generico.get(i).getUNIDADES()
    						, generico.get(i).getPVP()
    						, generico.get(i).getDTO()
    						, generico.get(i).getTOTAL()
    			});
    		}
        	
        	idNombre.setText(comboGenericos.getSelectionModel().getSelectedItem());
    	}
    	else
    	{
        	generico.removeAll(generico);
    		
    	}
    }

    @FXML
    void addFila(MouseEvent event)
    {

		if(swingTable.getSelectedRow() != -1)
		{
			int indice = swingTable.getSelectedRow();
			modeloTabla.insertRow(indice + 1, new Object[] {"", null, null, null, null});
		}
		else
		{
			modeloTabla.addRow(new Object[] {"", null, null, null, null});
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
    void initialize()
    {
    	ConexionBBDD conexion = new ConexionBBDD(log);
    	conexion.conectar();
    	
    	util = new Util();
    	
    	SwingNode swingNode = new SwingNode();
		createSwingContent(swingNode);
		
		// Agregar el SwingNode al HBox
		hBoxTabla.getChildren().add(swingNode);
		HBox.setHgrow(swingNode, javafx.scene.layout.Priority.ALWAYS); // Establece el ancho de SwingNode para que se expanda
		
		modeloTabla = new DefaultTableModel	(new Object[][]{}
		,new String[] {"DESCRIPCION", "UNIDADES", "PVP", "DTO", "TOTAL"}) {
			private static final long serialVersionUID = 3634681328632062693L;
			
			boolean[] columnEditables = new boolean[] {
				true, true, true, true, false
			};
			
			@SuppressWarnings("rawtypes")
			Class[] types = new Class[] {java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class};
			
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
		swingTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		swingTable.getColumnModel().getColumn(4).setResizable(false);
        
		modeloTabla.getDataVector().removeAllElements();
		modeloTabla.fireTableDataChanged();    	
    	
    	ArrayList<String> nombresGenericos = new ProductoGenerico_BEAN().recuperarNombresGenericos(conexion, log);
    	nombresGenericos.add(NUEVO);
    	comboGenericos.getItems().addAll(nombresGenericos);
    	comboGenericos.getSelectionModel().selectFirst();
    	
		generico = new ArrayList<ProductoGenerico_BEAN>();
    	
    	if(!comboGenericos.getSelectionModel().getSelectedItem().equals(NUEVO))
    	{
    		if(comboGenericos.getItems().size() > 0)
    		{
    			generico = new ProductoGenerico_BEAN().recuperarGenerico(conexion, log, comboGenericos.getItems().get(0));
    		}
        	
        	for(int i = 0; i < generico.size(); i++)
    		{
    			modeloTabla.addRow(new Object[] {
    					generico.get(i).getDESCRIPCION()
    						, generico.get(i).getUNIDADES()
    						, generico.get(i).getPVP()
    						, generico.get(i).getDTO()
    						, generico.get(i).getTOTAL()
    			});
    			
    		}
        	idNombre.setText(comboGenericos.getItems().get(0));
    	}
    	    	
    	tablaDatosAdapter = new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {}
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_TAB)
				{
					if((swingTable.getColumnCount() - 1) == swingTable.getSelectedColumn())
					{
						modeloTabla.addRow(new Object[] {"", null, null, null, null});
						swingTable.changeSelection(swingTable.getSelectedRow() + 1, 0, false, false);
					}
					
					for(int i = 0; i < swingTable.getRowCount(); i++)
					{
						swingTable.setValueAt(swingTable.getValueAt(i, 0).toString().toUpperCase(), i, 0);
						double cantidad = (Double) (swingTable.getValueAt(i, 1) != null?swingTable.getValueAt(i, 1):0.0);
						double precio = (Double) (swingTable.getValueAt(i, 2) != null?swingTable.getValueAt(i, 2):0.0);
						int descuento = (Integer) (swingTable.getValueAt(i, 3) != null?swingTable.getValueAt(i, 3):0);
						
						double precioTotal = precio * cantidad;
						double descontar = (precioTotal * descuento) / 100;
						double totalProducto = precioTotal - descontar;
						swingTable.setValueAt(Math.round(totalProducto * 100.0) / 100.0, i, 4);
					}
				}
			}
		};
		

    	swingTable.addKeyListener(tablaDatosAdapter);
    	
		conexion.desconectar();
    }
    
    private void createSwingContent(final SwingNode swingNode)
    {
        swingTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(swingTable);
        swingNode.setContent(scrollPane);
    }
    
    public void setDato(Trazas log)
    {
    	this.log = log;
    }
    
    public ArrayList<ProductoGenerico_BEAN> getDato()
    {
    	return retornoGenerico;
    }
}