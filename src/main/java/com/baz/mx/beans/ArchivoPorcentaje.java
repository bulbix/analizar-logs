/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.beans;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author acruzb
 */
public class ArchivoPorcentaje {
    
    private String ftp;
    private String nombre;
    private AtomicInteger porcentaje;

    public ArchivoPorcentaje(String nombre, String ftp) {
        this.nombre = nombre;
        this.ftp = ftp;
        this.porcentaje = new AtomicInteger(0);
    }
    
    public void incrementar(int porcentaje){
        this.porcentaje.set(porcentaje);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public AtomicInteger getPorcentaje() {
        return porcentaje;
    }

    public String getFtp() {
        return ftp;
    }

    public void setFtp(String ftp) {
        this.ftp = ftp;
    }
    
}
