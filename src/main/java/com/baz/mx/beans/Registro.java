/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.baz.mx.beans;

import static com.baz.mx.business.FileSearchOperations.SINTAXIS_RUTA;
import static com.baz.mx.business.FileSearchOperations.SINTAXIS_USUARIO;
import static com.baz.mx.business.Validaciones.getHiloCompletoDeLinea;
import static com.baz.mx.business.Validaciones.getRutaDeLinea;
import java.util.ArrayList;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author acruzb
 */
public class Registro {
    
    public static int SIZE;
    private final ArrayList<String> registro;

    public Registro() {
        SIZE = 5000;
        registro = new ArrayList<>(SIZE);
    }
    
    public void add(String elem){
        if(registro.size() > SIZE - 1){
            registro.remove(0);
        }
        registro.add(elem);
    }
    
    public ArrayList getRegistro(){
        return this.registro;
    }
    
    public boolean buscarRutaHilo(String ruta, String hilo){
        for (int i = registro.size() -1 ; i > -1; i--) {
            String line = registro.get(i);
            if (line.matches(".*"+SINTAXIS_RUTA+".*") && line.contains(hilo)) {
                String hiloLinea = getHiloCompletoDeLinea(line);
                if(hilo.equals(hiloLinea)){
                    if(getRutaDeLinea(line).contains(ruta)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean buscarUsuarioHilo(String usuario, String hilo){
        for (String line : registro) {
            if (line.contains(SINTAXIS_USUARIO) && line.toLowerCase().contains(usuario)) {
                System.out.println(line);
                String hiloLinea = getHiloCompletoDeLinea(line);
                if(hilo.equals(hiloLinea)){
                    return true;
                }
            }
        }
        return false;
    }
    
    public void escribirTextPane(StyledDocument contenidoDoc, AttributeSet style, String hilo){
        for (String line : registro) {
            try {
                contenidoDoc.insertString(contenidoDoc.getLength(), line + "\n", style);
            } catch (BadLocationException ex) {
                System.out.println("No se pudo escribir: " + ex.getMessage());
            }
        }
    }
    
}
