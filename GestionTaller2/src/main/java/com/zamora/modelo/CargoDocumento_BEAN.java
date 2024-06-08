package com.zamora.modelo;

public class CargoDocumento_BEAN {

	private int idDocumento;
	private int idEmpresa;
	private int idCargo;
	private String referencia;
	private String descripcion;
	private double unidades;
	private double precio;
	private int descuento;
	private double total;
	
	public CargoDocumento_BEAN() {
		super();
	}

	public CargoDocumento_BEAN(int idDocumento, int idEmpresa, int idCargo, String referencia, String descripcion, int unidades, double precio,
			int descuento, double total) {
		super();
		this.idDocumento = idDocumento;
		this.idEmpresa = idEmpresa;
		this.idCargo = idCargo;
		this.referencia = referencia;
		this.descripcion = descripcion;
		this.unidades = unidades;
		this.precio = precio;
		this.descuento = descuento;
		this.total = total;
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

	public int getIdCargo() {
		return idCargo;
	}

	public void setIdCargo(int idCargo) {
		this.idCargo = idCargo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getUnidades() {
		return unidades;
	}

	public void setUnidades(double unidades) {
		this.unidades = unidades;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getDescuento() {
		return descuento;
	}

	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	@Override
	public String toString() {
		return "CargoDocumento_BEAN [idDocumento=" + idDocumento + ", idEmpresa=" + idEmpresa + ", idCargo=" + idCargo
				+ ", descripcion=" + descripcion + ", unidades=" + unidades + ", precio=" + precio + ", descuento="
				+ descuento + ", total=" + total + "]";
	}
}
