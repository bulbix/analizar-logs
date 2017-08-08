/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.beans;

import java.io.IOException;
import java.util.ArrayList;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author acruzb
 */
@Component
@Scope("singleton")
public class ArchivosEnDescargaFTP {
    
    private final ArrayList<ArchivoPorcentaje> archivos;

    public ArchivosEnDescargaFTP() {
        archivos = new ArrayList<>();
    }
    
    public void addArchivo(ArchivoPorcentaje archivo){
        this.archivos.add(archivo);
    }
    
    public void deleteArchivo(ArchivoPorcentaje archivo){
        this.archivos.remove(archivo);
    }

    public ArrayList<ArchivoPorcentaje> getArchivos() {
        return archivos;
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
