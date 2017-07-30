/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.beans;

/**
 *
 * @author acruzb
 */
public class MigracionDataAdicional {
    
    private String icu;
    private String empleado;
    private String sucursal;

    public MigracionDataAdicional() {
    }

    public MigracionDataAdicional(String icu, String empleado, String sucursal) {
        this.icu = icu;
        this.empleado = empleado;
        this.sucursal = sucursal;
    }
        

    public String getIcu() {
        return icu;
    }

    public void setIcu(String icu) {
        this.icu = icu;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }
}
