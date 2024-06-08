package com.zamora.util;

import java.io.PrintWriter;

public class FicheroSQL {
	
	private PrintWriter pw = null;
	
	public FicheroSQL (String ruta)
	{		
		try {
			this.pw = new PrintWriter(ruta, "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void escribirTraza(String traza)
	{
		this.pw.println(traza);
		this.pw.flush();
	}
	
	/**
	 * Metodo que libera el objeto PrintWriter.
	 */
	public void liberar()
	{
		if(this.pw != null) 
		{
			this.pw.flush();
			this.pw.close();
		}
	}
}
