/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.dto;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author acruzb
 */
public class BusquedaGeneralDTO {
    
    private GeneralData usuario;
    private GeneralData trAlnova;
    private GeneralData ruta;
    private GeneralData textoLibre;
    private GeneralData rangoTiempo;

    public BusquedaGeneralDTO() {
    }

    public GeneralData getUsuario() {
        return usuario;
    }

    public void setUsuario(GeneralData usuario) {
        this.usuario = usuario;
    }

    public GeneralData getTrAlnova() {
        return trAlnova;
    }

    public void setTrAlnova(GeneralData trAlnova) {
        this.trAlnova = trAlnova;
    }

    public GeneralData getRuta() {
        return ruta;
    }

    public void setRuta(GeneralData ruta) {
        this.ruta = ruta;
    }

    public GeneralData getTextoLibre() {
        return textoLibre;
    }

    public void setTextoLibre(GeneralData textoLibre) {
        this.textoLibre = textoLibre;
    }

    public GeneralData getRangoTiempo() {
        return rangoTiempo;
    }

    public void setRangoTiempo(GeneralData rangoTiempo) {
        this.rangoTiempo = rangoTiempo;
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