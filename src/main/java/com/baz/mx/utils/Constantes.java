/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.utils;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author acruzb
 */
@Component
public class Constantes {
    
    public static String FTP_SERVERS;
    public static int FTP_PORT;
    public static String FTP_USER;
    public static String FTP_PASS;
    public static String FTP_BAZ;
    public static String FTP_JVC;
    
    public static String RUTA_LOGS;
    public static String RUTA_LOGS_BAZ;
    public static String RUTA_LOGS_JVC;
           
    @Autowired
    public Constantes(@Value("${ftp.servers}") String FTP_SERVERS,
            @Value("${ftp.port}") String FTP_PORT,
            @Value("${ftp.user}") String FTP_USER,
            @Value("${ftp.pass}") String FTP_PASS,
            @Value("${ftp.path.bancadigital}") String FTP_BAZ,
            @Value("${ftp.path.jvc}") String FTP_JVC,
            @Value("${ruta.local.logs}") String RUTA_LOGS) {
        this.FTP_SERVERS = FTP_SERVERS;
        this.FTP_PORT = Integer.parseInt(FTP_PORT);
        this.FTP_USER = FTP_USER;
        this.FTP_PASS = FTP_PASS;
        this.FTP_BAZ = FTP_BAZ;
        this.FTP_JVC = FTP_JVC;
        this.RUTA_LOGS = RUTA_LOGS;
        this.RUTA_LOGS_BAZ = RUTA_LOGS.concat(File.separator).concat(FTP_BAZ);
        this.RUTA_LOGS_JVC = RUTA_LOGS.concat(File.separator).concat(FTP_JVC);
    }
}
