/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.beans;

import java.util.Iterator;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author acruzb
 */
@Component
@Scope("session")
public class InformacionSession {
    
    private int idArchivo;
    private List<ArchivoFTP> lista;

    public InformacionSession() {
        idArchivo = -1;
    }

    public int getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public List<ArchivoFTP> getLista() {
        return lista;
    }

    public void setLista(List<ArchivoFTP> lista) {
        this.lista = lista;
    }
    
    public String getRutaArchivoId(int id){
        if(null != lista){
            for (ArchivoFTP next : lista) {
                if(next.getId() == id){
                    return next.getRutaCompleta();
                }
            }
        }
        return null;
    }
    
    public ArchivoFTP getArchivoId(int id){
        if(null != lista){
            for (ArchivoFTP next : lista) {
                if(next.getId() == id){
                    return next;
                }
            }
        }
        return null;
    }
    
}
