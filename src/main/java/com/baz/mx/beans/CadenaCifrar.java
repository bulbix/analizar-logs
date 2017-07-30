/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.baz.mx.beans;

/**
 *
 * @author Temporal
 */
public class CadenaCifrar {
    
    private String original;
    private String procesada;
    private String tipo;

    public CadenaCifrar(String original, String procesada, String tipo) {
        this.original = original;
        this.procesada = procesada;
        this.tipo = tipo;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getProcesada() {
        return procesada;
    }

    public void setProcesada(String procesada) {
        this.procesada = procesada;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
