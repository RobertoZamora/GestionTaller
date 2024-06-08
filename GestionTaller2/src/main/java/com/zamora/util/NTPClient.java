package com.zamora.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.log4j.Level;

import com.zamora.trazas.Trazas;

public class NTPClient {
	
	private Trazas log;
	private Util util;

    private NTPUDPClient ntpudpClient = null;
    private InetAddress inetAddress = null;
    private boolean isInit = false;
    String[] hosts;
    
    public NTPClient(Trazas log)
    {
    	this.log = log;
    	util = new Util();
    	hosts = util.serverDate(log);
    }
    
    public void initNTPClient(String url, int timeout) {
       this.ntpudpClient = new NTPUDPClient();
       // Establecer el tiempo de espera de la conexión
       this.ntpudpClient.setDefaultTimeout(timeout);
        try {
            this.inetAddress = InetAddress.getByName(url);
            this.isInit = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
			log.log(Level.ERROR, e);
        }
    }
    
    private Date getDateTime() {
        if (this.isInit) {
            try {
                TimeInfo timeInfo = this.ntpudpClient.getTime(this.inetAddress);
                return new Date(timeInfo.getMessage().getTransmitTimeStamp().getTime());
            } catch (IOException e) {
                e.printStackTrace();
				log.log(Level.ERROR, e);
            }
        }
        return null;
    }

    public void stopNTPClient() {
        this.isInit = false;
        if (null != this.ntpudpClient)
            this.ntpudpClient.close();
    }
    
    public Date getTime()
    {
    	Date fecha = null;
    	for(int i = 0; i < hosts.length && fecha == null; i++)
    	{
            initNTPClient(hosts[i], 5000);
        	fecha = getDateTime();
        	if(fecha != null)
        	{
            	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy MM/dd HH:mm:ss");
                log.log(Level.DEBUG, "Hora recuperada del host[" + i + "]: " + hosts[i]);
                log.log(Level.DEBUG, simpleDateFormat.format(fecha));
        	}
    	}
        stopNTPClient();
        
        return fecha;
    }
}
