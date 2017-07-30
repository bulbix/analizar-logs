/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.beans;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author acruzb
 */
public class ListMigracionData {
    
    private ArrayList<MigracionData> lista;

    public ListMigracionData() {
        lista = new ArrayList<>();
    }

    public ArrayList<MigracionData> getLista() {
        return lista;
    }

    public void setLista(ArrayList<MigracionData> lista) {
        this.lista = lista;
    }
    
    public void addAll(ArrayList<MigracionData> lista){
        this.lista.addAll(lista);
    }
    
    public void sort(){
        Collections.sort(lista);
    }
    
    public int size(){
        return this.lista.size();
    }
    
    public void addDatosAdicionales(ArrayList<MigracionDataAdicional> adicionales){
        for (MigracionDataAdicional obj : adicionales) {
            String icu = obj.getIcu();
            MigracionData md = findObj(icu);
            if(null != md && md.getTermino().equalsIgnoreCase(MigracionData.TERMINO_SUCCESS)){
                md.setEmpleado(obj.getEmpleado());
                md.setSucursal(obj.getSucursal());
            }
        }
    }
    
    private MigracionData findObj(String icu){
        for (MigracionData migracionData : lista) {
            if(migracionData.getIcu().equalsIgnoreCase(icu)){
                return migracionData;
            }
        }
        return null;
    }
    
}
