/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.baz.mx.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author acruzb
 */
public class ArchivoFTP implements Comparable<ArchivoFTP>{
    
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
    
    private int id;
    private String nombre;
    @JsonIgnore
    private String rutaCompleta;
    private String fechaCreacion;
    private String tamano;
    @JsonIgnore
    private long tamanoKB;

    public ArchivoFTP() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRutaCompleta() {
        return rutaCompleta;
    }

    public void setRutaCompleta(String rutaCompleta) {
        this.rutaCompleta = rutaCompleta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public long getTamanoKB() {
        return tamanoKB;
    }

    public void setTamanoKB(long tamanoKB) {
        this.tamanoKB = tamanoKB;
    }

    @Override
    public int compareTo(ArchivoFTP o) {
        try {
            Date uno = SDF.parse(o.getFechaCreacion());
            Date dos = SDF.parse(this.getFechaCreacion());
            return uno.compareTo(dos);
        } catch (ParseException ex) {
            System.out.println("Error al parsear fecha comparando.");
        }
        return -1;
    }
    
}
