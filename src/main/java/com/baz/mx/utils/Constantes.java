/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author acruzb
 */
@Component
public class Constantes {
    
    public static String FTP_IP_64;
    public static String FTP_IP_115;
    public static int FTP_PORT;
    public static String FTP_USER;
    public static String FTP_PASS;
    public static String FTP_BAZ;
    public static String FTP_JVC;
           
    @Autowired
    public Constantes(@Value("${ftp.server.64}") String FTP_IP_64,
            @Value("${ftp.server.115}") String FTP_IP_115,
            @Value("${ftp.port}") String FTP_PORT,
            @Value("${ftp.user}") String FTP_USER,
            @Value("${ftp.pass}") String FTP_PASS,
            @Value("${ftp.path.bancadigital}") String FTP_BAZ,
            @Value("${ftp.path.jvc }") String FTP_JVC) {
        this.FTP_IP_64 = FTP_IP_64;
        this.FTP_IP_115 = FTP_IP_115;
        this.FTP_PORT = Integer.parseInt(FTP_PORT);
        this.FTP_USER = FTP_USER;
        this.FTP_PASS = FTP_PASS;
        this.FTP_BAZ = FTP_BAZ;
        this.FTP_JVC = FTP_JVC;
    }
}
