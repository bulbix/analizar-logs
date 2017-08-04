/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.response;

/**
 *
 * @author acruzb
 */
public class BusquedaGeneralResponse {
    
    public String informacion;

    public BusquedaGeneralResponse() {
    }

    public BusquedaGeneralResponse(String informacion) {
        this.informacion = informacion;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }
    
}
