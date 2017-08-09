/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.request;

import com.baz.mx.enums.CIPHER_MODE;
import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author acruzb
 */
public class CifradoCadenaRequest {
    
    private String cadena;
    private boolean cifrar;
    private CIPHER_MODE sistema;

    public CifradoCadenaRequest() {
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public boolean isCifrar() {
        return cifrar;
    }

    public void setCifrar(boolean cifrar) {
        this.cifrar = cifrar;
    }

    public CIPHER_MODE getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        if(null != sistema){
            if (sistema.toUpperCase().equals(CIPHER_MODE.ALNOVA.toString())) {
                this.sistema = CIPHER_MODE.ALNOVA;
            } else {
                this.sistema = CIPHER_MODE.ACLARACIONES;
            }
        }
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
