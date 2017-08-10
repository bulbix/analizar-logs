/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.business;

import com.baz.mx.beans.ArchivoPorcentaje;
import com.baz.mx.beans.ArchivosEnDescargaFTP;
import com.baz.mx.utils.Constantes;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author acruzb
 */
@Component
@Scope("singleton")
public class DescargarArchivoFTP {
    
    private static final Logger LOGGER = Logger.getLogger(DescargarArchivoFTP.class);
    
    private FTPClient ftpClient;
    private final ExecutorService pool;
    
    @Autowired
    private ArchivosEnDescargaFTP descargnadoftp;

    public DescargarArchivoFTP() {
        pool = Executors.newCachedThreadPool();
    }
    
    public boolean descargarArchivoFTP(final String serverActivo, final boolean isBD, final String archivo, final long tamKB) {
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(serverActivo, Constantes.FTP_PORT);
            ftpClient.login(Constantes.FTP_USER, Constantes.FTP_PASS);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (isBD) {
                ftpClient.changeWorkingDirectory(Constantes.FTP_BAZ);
            } else {
                ftpClient.changeWorkingDirectory(Constantes.FTP_JVC);
            }
            //Descargando archivo de ftp
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ArchivoPorcentaje archivoDesc = new ArchivoPorcentaje(archivo, serverActivo);
                    LOGGER.info("Inicia la descarga del archivo: " + archivo + " del servidor: " + serverActivo);
                    OutputStream outputStream1 = null;
                    try {
                        String ruta = ((isBD) ? Constantes.RUTA_LOGS_BAZ: Constantes.RUTA_LOGS_JVC).concat(File.separator).concat(archivo);
                        File downloadFile = new File(ruta);
                        outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile));
                        ftpClient.setCopyStreamListener(createListener(tamKB, archivoDesc));
                        boolean success = ftpClient.retrieveFile(archivo, outputStream1);
                        outputStream1.close();
                        if (success) {
                            LOGGER.info("Se ha terminado la descarga del archivo: " + ruta);
                        }
                    } catch (FileNotFoundException ex) {
                        LOGGER.info("Error " + ex.getMessage());
                    } catch (IOException ex) {
                        LOGGER.info("Error " + ex.getMessage());
                    } finally {
                        descargnadoftp.deleteArchivo(archivoDesc);
                        try {
                            if(null != outputStream1){
                            outputStream1.close();}
                        } catch (IOException ex) {
                        }
                        try {
                            if (ftpClient.isConnected()) {
                                ftpClient.logout();
                                ftpClient.disconnect();
                            }
                        } catch (IOException ex) {
                        }
                    }
                }
            };
            pool.execute(runnable);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    private CopyStreamListener createListener(final long tam, final ArchivoPorcentaje archivoP){
        descargnadoftp.addArchivo(archivoP);
        return new CopyStreamListener() {
            
            @Override
            public void bytesTransferred(CopyStreamEvent event) {
                bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
            }

            @Override
            public void bytesTransferred(long totalBytesTransferred,
                    int bytesTransferred, long streamSize) {
                int percent = (int)(totalBytesTransferred * 100 / tam);
                archivoP.incrementar(percent);
            }
        };
    }
    
}

