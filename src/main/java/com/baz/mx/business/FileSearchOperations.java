/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.business;

import com.baz.mx.beans.Registro;
import static com.baz.mx.business.Validaciones.escribirTextPaneMenosUnaLinea;
import static com.baz.mx.business.Validaciones.getHiloCompletoDeLinea;
import static com.baz.mx.business.Validaciones.getHiloDeLinea;
import static com.baz.mx.business.Validaciones.isEndOperationLine;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author acruzb
 */
public class FileSearchOperations {
    
    private static final Logger LOGGER = Logger.getLogger(FileSearchOperations.class);
    public final static Charset ENCODING = StandardCharsets.UTF_8;
    private static final String NEW_LINE = System.getProperty("line.separator");

    public static final String SINTAXIS_USUARIO = " - Alias: ";
    public static final String SINTAXIS_TR = "Transaccion ";
    public static final String SINTAXIS_HILO = "HTTP-CRED-|Thread-";
    public static final String SINTAXIS_RUTA = "PathInterceptor:2";
    public static final String SINTAXIS_INFO_CLIENTE = "BusquedaPersonaImpl:";
    public static final String SINTAXIS_INFO_CLIENTE_FULL = ".*BusquedaPersonaImpl:\\d+.*";
    public static final String SINTAXIS_HILO_HIJO = ".*PrintThrowThreadConcurrentTaskExecutor:\\d+ - El hilo.*";
    public static final String PATH_OPERACION_STATUS = ".*ObjEncriptacionSeguridad:\\d+ - doObjToStrJSON:.*codigoOperacion.*";
    
    private static final int MAX_LINEAS_POR_HILO = 250;
    private static final int LINEAS_HILO_DIF_MAX = 300;

    private static ArrayList<String> clientesEncontrados;

    public FileSearchOperations() {
        clientesEncontrados = new ArrayList<>();
    }
    
    //SECCION DE CONSULTA DE INFORMACION DE USUARIOS
    public static String procesarDetalleCliente(Path path, String nombre, String apellido){
        clientesEncontrados = new ArrayList<>();
        try{
            LineIterator it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
            try {
                LOGGER.info(String.format("Buscando el usuario %s %s ", nombre, apellido));
                while (it.hasNext()) {
                    String line = it.nextLine();
                    if(line.contains(SINTAXIS_INFO_CLIENTE)){
                        String regex = SINTAXIS_INFO_CLIENTE_FULL;
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(line);
                        if(matcher.find() && isCliente(line, nombre, apellido)){
                            return escribirClienteFormato(line);
                        }
                    }
                }
            } finally {
                LOGGER.info("Termina la busqueda");
                LineIterator.closeQuietly(it);
            }
        }catch(IOException e){
            
        }
        return null;
    }
    
    public static String procesarDetalleICU(Path path, String icu){
        clientesEncontrados = new ArrayList<>();
        try {
            LineIterator it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
            try {
                LOGGER.info(String.format("Buscando el ICU %s ", icu));
                while (it.hasNext()) {
                    String line = it.nextLine();
                    if(line.contains(SINTAXIS_INFO_CLIENTE)){
                        String regex = SINTAXIS_INFO_CLIENTE_FULL;
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(line);
                        if(matcher.find() && isICU(line, icu)){
                            return escribirClienteFormato(line);
                        }
                    }
                }
            } finally {
                LOGGER.info("Termina la busqueda");
                LineIterator.closeQuietly(it);
            }
        }catch(Exception e){
            
        }
        return null;
    }
    
    public static String procesarDetalleUsuario(Path path, String nombreUsuario){
        LineIterator it;
        try {
            it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
            try {
                System.out.println(String.format("Buscando el usuario %s ", nombreUsuario));
                clientesEncontrados = new ArrayList<>();
                LOGGER.info(String.format("Buscando el usuario %s ", nombreUsuario));
                while (it.hasNext()) {
                    String line = it.nextLine();
                    if(line.contains(SINTAXIS_INFO_CLIENTE)){
                        String regex = SINTAXIS_INFO_CLIENTE_FULL;
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(line);
                        if(matcher.find() && isUsario(line, nombreUsuario)){
                            return escribirClienteFormato(line);
                        }
                    }
                }
            } catch (IOException ex) {
                
            } finally {
                LOGGER.info("Termina la busqueda");
                LineIterator.closeQuietly(it);
            }
        } catch (IOException ex) {
            
        }
        return null;
    }
    
    private static boolean isUsario(String line, String nombreUsuario){
        String regex = ".*\"alias\":(.+),\"nivel\".*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String nomUsuarioLocal = matcher.group(1);
            nomUsuarioLocal = nomUsuarioLocal.replaceAll("[\"\\[\\]]", "").replace(",", " ");
            if(nomUsuarioLocal.toLowerCase().contains(nombreUsuario.toLowerCase())){
                LOGGER.info("Usuario encontrado: " + nomUsuarioLocal);
                for (String cliente : clientesEncontrados) {
                    if(cliente.equals(nomUsuarioLocal)){
                        return false;
                    }
                }
                clientesEncontrados.add(nomUsuarioLocal);
                LOGGER.info("Usuarios lista:");
                LOGGER.info(clientesEncontrados);
                return true;
            }
        }
        return false;
    }
    
    private static boolean isCliente(String line, String nombre, String apellido){
        String regex = ".*nombre\":\"([A-Z-a-zñÑ ]+)...apellidos\":(.+)..genero_cu.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String nom = matcher.group(1);
            String apellidos = matcher.group(2);
            apellidos = apellidos.replaceAll("[\"\\[\\]]", "").replace(",", " ");
            if(nom.toLowerCase().contains(nombre.toLowerCase()) && apellidos.toLowerCase().contains(apellido.toLowerCase())){
                String nombreCompleto = nom + " " + apellidos;
                LOGGER.info("Cliente encontrado: " + nombreCompleto);
                for (String cliente : clientesEncontrados) {
                    if(cliente.equals(nombreCompleto)){
                        return false;
                    }
                }
                clientesEncontrados.add(nombreCompleto);
                LOGGER.info("Clientes lista:");
                LOGGER.info(clientesEncontrados);
                return true;
            }
        }
        return false;
    }
   
    private static boolean isICU(String line, String icu){
        String regex = ".*\"icu\":(.+),\"fecha_registro\".*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String icuLocal = matcher.group(1);
            icuLocal = icuLocal.replaceAll("[\"\\[\\]]", "").replace(",", " ");
            if(icuLocal.toLowerCase().contains(icu.toLowerCase())){
                LOGGER.info("ICU encontrado: " + icuLocal);
                for (String cliente : clientesEncontrados) {
                    if(cliente.equals(icuLocal)){
                        return false;
                    }
                }
                clientesEncontrados.add(icuLocal);
                LOGGER.info("ICU lista:");
                LOGGER.info(clientesEncontrados);
                return true;
            }
        }
        return false;
    }
    
    private static String escribirClienteFormato(String line) throws IOException{
        String regex = ".*Se ha encontrado al menos un registro para la busqueda.(.+).+\\|.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(matcher.group(1), Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        }
        return null;
    }
    
    //SECCION DE BUSQUEDA DE INFORMACION GENERAL POR CRITERIO
    public static String procesarSoloUsuario(Path path, String usuarioField, boolean intervaloHora){
        boolean encontrado = false;
        int lineasPorHilo = 0;
        String hilo = null;
        int lineaHiloDif = 0;
        LOGGER.info("Iniciando la busqueda del usuario: " + usuarioField);
        LOGGER.info(usuarioField);
        usuarioField = usuarioField.toLowerCase();
        LineIterator it = null;
        StringBuilder salida = new StringBuilder();
        try {
            it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
            Registro registro = new Registro();
            while (it.hasNext()) {
                String line = it.nextLine();
                registro.add(line);
                if (line.contains(SINTAXIS_USUARIO) && line.toLowerCase().contains(usuarioField)) {
                    //CHECAR LAS FORMA...
                    boolean isRango = true;
                    if(intervaloHora){
//                        isRango = validarRangoDeTiempo(line);
                    }
                    if(isRango){
                        hilo = getHiloCompletoDeLinea(line);
                        lineaHiloDif = 0;
                        encontrado = true;
                        lineasPorHilo = 0;
                        LOGGER.info("procesando hilo " + hilo);
                        salida = escribirTextPaneMenosUnaLinea(registro.getRegistro(), hilo);
//                        escribirTextPaneMenosUnaLinea(registro.getRegistro(), null, hilo);
//                        escribirTextPane(line, keyWordStyleAzul);
                    }
                }
                if (encontrado) {
                    if (line.contains(hilo) && lineasPorHilo++ <= MAX_LINEAS_POR_HILO) {
                        salida.append(line).append(NEW_LINE);
//                        escribirTextPane(line, null);
                    } else {
                        lineaHiloDif++;
                    }
                    if(lineaHiloDif >= LINEAS_HILO_DIF_MAX){
                        encontrado = false;
                    }
                    if(isEndOperationLine(line)){//Fin de la operacion
                        encontrado = false;
                    }
                }
            }
        } catch (IOException ex) {
            
        } finally {
            LineIterator.closeQuietly(it);
            if(encontrado){
            }
            LOGGER.info("Termina la busqueda");
        }
        return salida.toString();
    }
}
