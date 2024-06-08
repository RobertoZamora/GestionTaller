package com.zamora.trazas;

import java.util.UUID;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.zamora.Main;

public class Trazas
{	
	private static Logger logger = Logger.getLogger(Trazas.class) ;
	private UUID uuid = UUID.randomUUID();
	
	public Trazas()  {
		configure();
	}
	
    public void configure() {
        // Configurar el nivel de registro
        logger.setLevel(Level.ALL);

        // Crear un patrón de diseño
        PatternLayout layout = new PatternLayout("[%d{yyyy-MM-dd HH:mm:ss}] [%-5p] [%c{1}:%L] - %m%n");

        // Configurar el archivo de registro
        RollingFileAppender fileAppender = new RollingFileAppender();
        fileAppender.setLayout(layout);
        fileAppender.setFile(Main.rutaDatos + "/logs/gestiontaller.log");
        fileAppender.setAppend(true);
        fileAppender.setMaxFileSize("10MB");
        fileAppender.setMaxBackupIndex(2);
        fileAppender.activateOptions();

        // Configurar la salida de consola
        ConsoleAppender consoleAppender = new ConsoleAppender(layout);
        consoleAppender.setTarget("System.out");

        // Agregar los appenders al logger
        logger.addAppender(fileAppender);
        logger.addAppender(consoleAppender);
        
     // Configurar un nuevo Appender específico para com.healthmarketscience.jackcess.impl.DatabaseImpl
        RollingFileAppender jackcessFileAppender = new RollingFileAppender();
        jackcessFileAppender.setLayout(layout);
        jackcessFileAppender.setFile(Main.rutaDatos + "/logs/gestiontaller.log"); // Ruta del archivo para jackcess
        jackcessFileAppender.setAppend(true);
        jackcessFileAppender.setMaxFileSize("10MB");
        jackcessFileAppender.setMaxBackupIndex(2);
        jackcessFileAppender.activateOptions();

        // Crear un nuevo Logger para jackcess y configurar su nivel de registro
        Logger jackcessLogger = Logger.getLogger("com.healthmarketscience.jackcess.impl.DatabaseImpl");
        jackcessLogger.setLevel(Level.ALL);
        jackcessLogger.addAppender(jackcessFileAppender);
    }
	
	public Logger getLogger()
	{
		return logger;
	}
	
	public void log (Level error, Object mensaje) {
		//ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF
		try
		{
			// AÑADIMOS UUID a la traza para identificarlas al buscarlas
			mensaje = "[" + uuid.toString() + "] - " + mensaje;
			
			if (error == Level.TRACE)
				logger.trace(mensaje);

			if (error == Level.DEBUG)
				logger.debug(mensaje);
			
			if (error == Level.INFO)
				logger.info(mensaje);
			
			if (error == Level.ERROR)
				logger.error(mensaje);

			if (error == Level.WARN)
				logger.warn(mensaje);
			
			if (error == Level.FATAL)
				logger.fatal(mensaje);
		}
		catch (Exception ex)
		{
			logger.error("ERROR EN LOG", ex);
		}
	}
}
