package com.zamora.modelo;

public class Marca_BEAN {

	private int idMarca;
	private String nombreMarca;
	
	public Marca_BEAN() {
		super();
	}

	public Marca_BEAN(int idMarca, String nombreMarca) {
		super();
		this.idMarca = idMarca;
		this.nombreMarca = nombreMarca;
	}

	public int getIdMarca() {
		return idMarca;
	}

	public void setIdMarca(int idMarca) {
		this.idMarca = idMarca;
	}

	public String getNombreMarca() {
		return nombreMarca;
	}

	public void setNombreMarca(String nombreMarca) {
		this.nombreMarca = nombreMarca;
	}

	@Override
	public String toString() {
		return nombreMarca;
	}
}
