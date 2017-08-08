/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.request;

import com.baz.mx.beans.ArchivoFTP;
import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author acruzb
 */
public class ActualizarArchivoFTPRequest {
    
    private String ip;
    private boolean core;
    private ArchivoFTP archivo;

    public ActualizarArchivoFTPRequest() {
    }
    
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isCore() {
        return core;
    }

    public void setCore(boolean core) {
        this.core = core;
    }

    public ArchivoFTP getArchivo() {
        return archivo;
    }

    public void setArchivo(ArchivoFTP archivo) {
        this.archivo = archivo;
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
