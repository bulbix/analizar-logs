/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.business;

import com.baz.mx.beans.ArchivoFTP;
import static com.baz.mx.business.FTPUtils.formatFileSize;
import com.baz.mx.utils.Constantes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tools.ant.DirectoryScanner;
import org.springframework.stereotype.Component;

/**
 *
 * @author acruzb
 */

@Component
public class ListarArchivos {
    
    private final String DATE_FORMATTER = "dd/MM/yyyy hh:mm:ss a";
    private final DateFormat dateFormater;

    public ListarArchivos() {
        dateFormater = new SimpleDateFormat(DATE_FORMATTER);
    }

    public List<ArchivoFTP> obtenerLista(boolean isCore){
        List<ArchivoFTP> lista = new ArrayList<>();
        Path p1;
        if(isCore){
            p1 = Paths.get(Constantes.RUTA_LOGS_BAZ);
        }
        else{
            p1 = Paths.get(Constantes.RUTA_LOGS_JVC);
        }
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
    
    public boolean eliminarArchivoHD(String path){
        return Paths.get(path).toFile().delete();
    }
    
}
