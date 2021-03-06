/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.baz.mx.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.map.ObjectMapper;

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
    private long tamanoKB;
    private boolean core;

    public ArchivoFTP() {
    }

    public ArchivoFTP(int id, String nombre, String rutaCompleta, String fechaCreacion, String tamano, long tamanoKB, boolean core) {
        this.id = id;
        this.nombre = nombre;
        this.rutaCompleta = rutaCompleta;
        this.fechaCreacion = fechaCreacion;
        this.tamano = tamano;
        this.tamanoKB = tamanoKB;
        this.core = core;
    }

    public int getId() {
        return id;
    }

    public boolean isCore() {
        return core;
    }

    public void setCore(boolean core) {
        this.core = core;
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
    
    @Override
    public String toString(){
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        }catch (IOException e) {}
        return null;
    }
    
}
