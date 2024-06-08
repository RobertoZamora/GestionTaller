package com.zamora.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
//Import all needed packages
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Level;

import com.zamora.trazas.Trazas;

public class ZipUtils {

    private ArrayList<String> fileList;
    private String OUTPUT = "";
    private String INPUT = "";
    private Trazas log;

    public ZipUtils(Trazas log, String entrada, String salida) {
        fileList = new ArrayList<String> ();
        this.OUTPUT = salida;
        this.INPUT = entrada;
        this.log = log;
    }

    public void zipIt() {
    	generateFileList(new File(INPUT));
    	String zipFile = OUTPUT;
        byte[] buffer = new byte[1024];
        String source = new File(INPUT).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            log.log(Level.DEBUG, "Output to Zip : " + zipFile);
            FileInputStream in = null;

            for (String file: this.fileList) {
            	log.log(Level.DEBUG, "File Added : " + file);
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(INPUT + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();
            log.log(Level.DEBUG, "Folder successfully compressed");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
	public void unZipIt() {
		try {
			File directorio = new File(OUTPUT);
			if (!directorio.exists()) {
				directorio.mkdirs();
			}

			FileInputStream fis = new FileInputStream(INPUT);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));

			ZipEntry entrada;
			while ((entrada = zis.getNextEntry()) != null) {
				String nombreArchivo = entrada.getName();
				File archivo = new File(OUTPUT, nombreArchivo);

				String[] carpetas = entrada.getName().split("\\\\");
				String carpeta = "";
				for (int j = 0; j < carpetas.length - 1; j++) {
					carpeta += File.separator + carpetas[j];
					File carpetaActual = new File(OUTPUT + carpeta);
					if (!carpetaActual.exists())
					{
						log.log(Level.DEBUG, OUTPUT + carpeta);
						carpetaActual.mkdir();	
					}
				}

				// Si la entrada es un archivo, crea un flujo de salida y copia el contenido
				byte[] buffer = new byte[1024];
				FileOutputStream fos = new FileOutputStream(archivo);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				int leido;
				log.log(Level.DEBUG, carpetas[carpetas.length - 1]);
				while ((leido = zis.read(buffer)) != -1) {
					bos.write(buffer, 0, leido);
				}
				bos.close();
			}
			zis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void generateFileList(File node) {
        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename: subNote) {
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file) {
        return file.substring(INPUT.length() + 1, file.length());
    }
}
