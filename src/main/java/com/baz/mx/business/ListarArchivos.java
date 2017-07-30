/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.business;

import com.baz.mx.beans.ArchivoFTP;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tools.ant.DirectoryScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author acruzb
 */

@Component
public class ListarArchivos {
    
    private final String DATE_FORMATTER = "dd/MM/yyyy hh:mm:ss a";
    private final DateFormat dateFormater;
    
    private final String ruta;

    @Autowired
    public ListarArchivos(@Value("${ruta.local.logs}") String ruta ) {
        dateFormater = new SimpleDateFormat(DATE_FORMATTER);
        this.ruta = ruta;
    }

    public List<ArchivoFTP> obtenerLista(){
        System.out.println("Se obtienen los archivos de la ruta: " + ruta);
        List<ArchivoFTP> lista = new ArrayList<>();
        Path p1 = Paths.get(ruta);
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/*"});
        scanner.setBasedir(p1.toString());
        scanner.setCaseSensitive(false);
        scanner.scan();
        String[] files = scanner.getIncludedFiles();
        int id = 1;
        for (String file : files) {
            try {
                Path path = p1.resolve(file);
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                if(! attr.isDirectory()){
                    ArchivoFTP archivo = new ArchivoFTP();
                    archivo.setId(id++);
                    archivo.setFechaCreacion(dateFormater.format(attr.creationTime().toMillis()));
                    archivo.setNombre(file);
                    archivo.setTamano(formatFileSize(attr.size()));
                    archivo.setTamanoKB(attr.size());
                    archivo.setRutaCompleta(path.toString());
                    lista.add(archivo);
                }
            } catch (IOException ex) {
                Logger.getLogger(ListarArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(lista);
        return lista;
    }
    
    private String formatFileSize(long size) {
        String hrSize;
        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }
        return hrSize;
    }
    
}
