/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.business;

import com.baz.mx.beans.ArchivoFTP;
import com.baz.mx.exceptions.FTPConexionException;
import com.baz.mx.utils.Constantes;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author acruzb
 */
@Component
@Scope("session")
public class FTPUtils {
    
    private static final Logger LOGGER = Logger.getLogger(FTPUtils.class);
    private static final String DATE_FORMATTER = "dd/MM/yyyy hh:mm:ss a";
    
    private FTPClient ftpClient;

    public FTPUtils() {
    }

    public ArrayList<ArchivoFTP> obtenerArchivosBD_JVC(String serverActivo, boolean isBD) throws FTPConexionException{
        ArrayList<ArchivoFTP> archivosFTP = new ArrayList<>();
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(serverActivo, Constantes.FTP_PORT);
            ftpClient.login(Constantes.FTP_USER, Constantes.FTP_PASS);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (isBD) {
                ftpClient.changeWorkingDirectory(Constantes.FTP_BAZ);
            } else {
                ftpClient.changeWorkingDirectory(Constantes.FTP_JVC);
            }
            FTPFile[] files = ftpClient.listFiles();
            DateFormat dateFormater = new SimpleDateFormat(DATE_FORMATTER);
            for (FTPFile file : files) {
                String details = file.getName();
                if (file.isFile()) {
                    details += "\t\t" + file.getSize();
                    details += "\t\t" + formatFileSize(file.getSize());
                    details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());
                    LOGGER.info(details);
                    archivosFTP.add(new ArchivoFTP(0, file.getName(), "", dateFormater.format(file.getTimestamp().getTime()), formatFileSize(file.getSize()), file.getSize()));
                }
            }
        } catch (IOException ex) {
            LOGGER.info("Error: " + ex.getMessage());
            throw new FTPConexionException("No se pudo conectar con el servidor ftp: " + serverActivo);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
            }
        }
        Collections.sort(archivosFTP);
        return archivosFTP;
    }
    
    public static String formatFileSize(long size) {
        String hrSize;

        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }
    
}
