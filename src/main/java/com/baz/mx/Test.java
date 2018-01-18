/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx;

import com.baz.mx.beans.Registro;
import static com.baz.mx.business.FileSearchOperations.ENCODING;
import static com.baz.mx.business.FileSearchOperations.NEW_LINE;
import static com.baz.mx.business.Validaciones.escribirTextPaneMenosUnaLinea;
import static com.baz.mx.business.Validaciones.getHiloCompletoDeLinea;
import static com.baz.mx.business.Validaciones.isEndOperationLine;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

/**
 *
 * @author acruzb
 */
public class Test {
    
    public static String procesarLibreDetalle(Path path, String texto) {
        StringBuilder salida = new StringBuilder();
        LineIterator it = null;
        boolean lineasSig = false;//Lineas despues de encontrar el response
        Registro registro = new Registro();
        int numLinSig = 0;
        String hilo = null;
        try {
            it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
            System.out.println("Buscando el texto: " + texto);
            texto = texto.trim();
            while (it.hasNext()) {
                String line = it.nextLine();
                registro.add(line);//Se agrega la linea al registro
                if (line.contains(texto)) {
                    hilo = getHiloCompletoDeLinea(line);
                    escribirTextPaneMenosUnaLinea(salida, registro.getRegistro(), hilo);//Antes
                    salida.append("<b>").append(line).append("</b>").append(NEW_LINE);
                    if (isEndOperationLine(line, hilo)) {//Fin de la operacion
                        break;
                    }else{
                        lineasSig = true;
                    }
                } else if (lineasSig) {
                    if (numLinSig < 5000) {
                        if (line.contains(hilo)) {
                            salida.append(line).append(NEW_LINE);
                        } else {
                            numLinSig++;
                        }
                    } else {
                        break;
                    }
                    if (isEndOperationLine(line, hilo)) {//Fin de la operacion
                        break;
                    }
                }
            }
        } catch (IOException ex) {
        } finally {
            if (null != it) {
                LineIterator.closeQuietly(it);
            }
            System.out.println("Termina la busqueda de linea: " + salida.toString().length());
        }
        return salida.toString();
    }
    
    public static void main(String[] args) {
        System.out.println(procesarLibreDetalle(Paths.get("E:\\AnalisisLogs\\LogsBazDigital\\server158.log.2018-01-17.1"), "[#| 2018-01-17 11:10:53,152 INFO  (HTTP-CRED-644) ObjEncriptacionSeguridad:256 - doObjToStrJSON:{\"codigoOperacion\":\"-7407\",\"descripcion\":\"No se pudo obtener la información, por favor intenta más tarde.\",\"folio\":\"1582018117111053151\",\"tokenCode\":\"2705e5c8c1c8489e6fd27d5bbfd4e80c\",\"tarjetas\":[],\"domicilio\":{\"tipo\":\"BDM:PRINCIPAL\",\"calle\":\"9 Andador De Mariquita   Sanchez  65\",\"colonia\":\"Culhuacan Ctm Obrero Seccion 6\",\"codigo_postal\":\"04480\",\"poblacion\":\"Coyoacan\",\"estado\":\"Ciudad De Mexico\",\"pais\":\"1\",\"latitud\":19.326237,\"longitud\":-99.122091}} infoHash: C3B1CFC67D6844FCBA2B71F671FDAF35 |#]"));
    }
    
}
