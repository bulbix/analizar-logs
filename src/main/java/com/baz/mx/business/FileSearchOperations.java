/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.business;

import com.baz.mx.beans.Registro;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
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

//    private static StyledDocument contenidoDoc;
    public final static Charset ENCODING = StandardCharsets.UTF_8;
    private final static String MIDDLELINE = "                                                                                                                           ";
    private final static String STARTLINE  = "                                                           INICIO                                                          ";
    private final static String ENDLINE    = "                                                            FIN                                                            ";
    private final static String ENTER = "\n";
    private final static String FONT_FAMILY = "Monospaced";

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
    //            insertaLeyenda();
                LineIterator.closeQuietly(it);
            }
        }catch(Exception e){
            
        }
        return null;
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
    
    public static void main(String[] args) throws IOException {
        System.out.println(escribirClienteFormato("2017-07-27 00:00:15,466 INFO  [stdout] (HTTP-CRED-6) [#| 2017-07-27 00:00:15 INFO  BusquedaPersonaImpl:1061 - Se ha encontrado al menos un registro para la busqueda.{\"cliente\":{\"nombre\":\"CLIENTE MONITOREO\",\"apellidos\":[\"MONITOREO\",\"MONITOREO\"],\"genero_cu\":\"M\",\"nacionalidad_cu\":449,\"ocupacion_cu\":1,\"estado_nacimiento_cu\":1,\"pais_nacimiento_cu\":449,\"alias\":[\"usuarioprueba1\"],\"nivel\":\"N2\",\"marcas\":{\"compartir_datos\":false,\"aceptar_publicidad\":false},\"fecha_nacimiento\":\"1983-11-29\",\"ids_vinculados\":[{\"origen\":\"CU\",\"id\":{\"pais\":\"1\",\"canal\":\"33\",\"sucursal\":\"9546\",\"folio\":\"6\"},\"primario\":false},{\"origen\":\"ALNOVA\",\"id\":{\"cliente_alnova\":\"57648553\"},\"primario\":false},{\"origen\":\"CLIENTE-TIENDA\",\"id\":{\"negocio\":\"1\",\"tienda\":\"9546\",\"cliente\":\"303874\",\"digito_verificador\":\"5\"},\"primario\":false}],\"documentos_identidad\":[{\"tipo_cu\":8,\"folio\":\"FEGF831129JV3\",\"tipo\":\"RFC\"},{\"tipo_cu\":0,\"folio\":\"FEGF831129HVZRML09\",\"tipo\":\"CURP\"},{\"tipo_cu\":0,\"folio\":\"\",\"tipo\":\"NUMERO SEGURO SOCIAL\"},{\"tipo_cu\":1,\"folio\":\"1321568765458132\",\"fecha_emision\":\"2007-01-01\",\"fecha_expiracion\":\"2020-12-31\",\"estado\":\"30\",\"pais\":\"1\",\"tipo\":\"PASAPORTE\"}],\"domicilios\":[{\"calle\":\"INSURGENTES SUR 3579\",\"colonia\":\"TLALPAN LA JOYA\",\"codigo_postal\":\"14000\",\"poblacion\":\"CDMX\",\"estado\":\"DF\",\"pais\":\"1\",\"latitud\":19.294532,\"longitud\":-99.181636,\"tipo\":\"CU:PRINCIPAL\"}],\"correos_electronicos\":[],\"telefonos\":[{\"codigo_pais\":\"52\",\"telefono\":\"7225500437\",\"tipo\":\"CU:MOVIL\"},{\"codigo_pais\":\"52\",\"telefono\":\"5555555555\",\"tipo\":\"BDM:PRINCIPAL\"}],\"icu\":\"c80f25c544d14a65b8ba95fcae012da7\",\"fecha_registro\":\"2017-01-02T20:04:21.891Z\",\"fecha_modificacion\":\"2017-06-21T23:07:49.604Z\"},\"ext\":{\"bdm\":{\"telefono_asociado\":\"5555555555\",\"dispositivo\":{\"id\":\"725ded35fade903a8a6f4c7b11af05c365dcade9e3d5b80492d388c8bcbefbb7\",\"tipo\":\"IOS\",\"info_hash\":\"EBA806C83AB44319B642B78DD725D712\"},\"icu\":\"c80f25c544d14a65b8ba95fcae012da7\",\"fecha_registro\":\"2017-01-02T20:04:21.910Z\",\"fecha_modificacion\":\"2017-06-21T23:06:56.388Z\"}}}   |#]"));
    }
    
    private void procesarSoloUsuario(Path path, String usuario) throws IOException {
        boolean encontrado = false;
        int lineasPorHilo = 0;
        String hilo = null;
        int lineaHiloDif = 0;
        String usuarioField = usuario;
        if(null == usuarioField || "".equals(usuarioField)){
            System.out.println("No se ha ingresado un usuario.");
            return;
        }
        LOGGER.info("Iniciando la busqueda del usuario");
        LOGGER.info(usuarioField);
        usuarioField = usuarioField.toLowerCase();
        LineIterator it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
        try {
            Registro registro = new Registro();
            while (it.hasNext()) {
                String line = it.nextLine();
                registro.add(line);
                if (line.contains(SINTAXIS_USUARIO) && line.toLowerCase().contains(usuarioField)) {
                    //CHECAR LAS FORMA...
                    boolean isRango = true;
//                    if(vista.isCheckedIntervalo()){
//                        isRango = validarRangoDeTiempo(line);
//                    }
                    if(isRango){
                        hilo = getHiloDeLinea(line);
//                        insertInitFind();
                        lineaHiloDif = 0;
                        encontrado = true;
                        lineasPorHilo = 0;
                        LOGGER.info("procesando hilo " + hilo);
//                        escribirTextPaneMenosUnaLinea(registro.getRegistro(), null, hilo);
//                        escribirTextPane(line, keyWordStyleAzul);
                    }
                }
                if (encontrado) {
                    if (line.contains(hilo) && lineasPorHilo++ <= MAX_LINEAS_POR_HILO) {
//                        escribirTextPane(line, null);
                    } else {
                        lineaHiloDif++;
                    }
                    if(lineaHiloDif >= LINEAS_HILO_DIF_MAX){
                        encontrado = false;
//                        insertEndFind();
                    }
                    if(isEndOperationLine(line)){//Fin de la operacion
//                        insertEndFind();
                        encontrado = false;
                    }
                }
            }
        } finally {
            LineIterator.closeQuietly(it);
            if(encontrado){
//                insertEndFind();
            }
            LOGGER.info("Termina la busqueda");
//            insertaLeyenda();
        }
    }
    
    
    
    private static boolean isEndOperationLine(String line){
        return line.matches(PATH_OPERACION_STATUS);
    }
    
    public static synchronized String getRutaDeLinea(String line){
        String regex = ".*(PathInterceptor:2. - )([A-Za-z0-9\\/-]+).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }
    
    public static synchronized String getHiloDeLinea(String line){
        String regex = ".*("+ SINTAXIS_HILO +")(\\d+).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }
    
    public static synchronized String getHiloCompletoDeLinea(String line){
        String regex = ".*("+ SINTAXIS_HILO +")(\\d+).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1) + matcher.group(2);
        }
        return null;
    }
    
    public static synchronized String getFechaDeLinea(String line){
        String regex = ".*(\\d{4}-\\d{2}-\\d{2})\\s(\\d{2}:\\d{2}:\\d{2}).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }
    
    public static synchronized String getFechaCompleta(String line){
        String regex = ".*(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2},\\d+).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    public static synchronized String[] getHoraCalculada(String horaActual){
        String horaInicial;
        String horaFinal;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendarNow = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaActual.split(":")[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(horaActual.split(":")[1]));	
        calendar.set(Calendar.SECOND, Integer.parseInt(horaActual.split(":")[2])); 
        calendar.add(Calendar.SECOND, -2);
        if(calendar.get(Calendar.DAY_OF_MONTH) < calendarNow.get(Calendar.DAY_OF_MONTH)){
            horaInicial = "00:00:00";
        }
        else{
            horaInicial = dateFormat.format(calendar.getTime());
        }
        calendar.add(Calendar.MINUTE, 3);
        if(calendar.get(Calendar.DAY_OF_MONTH) > calendarNow.get(Calendar.DAY_OF_MONTH)){
            horaFinal = "23:59:59";
        }
        else{
            horaFinal = dateFormat.format(calendar.getTime());
        }
        return new String[]{horaInicial, horaFinal};
    }
    
    public static synchronized String getHiloPadreHiloDeLinea(String line){
        String regex = ".*PrintThrowThreadConcurrentTaskExecutor:\\d+ - El hilo (.*) esta lanzando a (.*)...\\|.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1) + ":" + matcher.group(2);
        }
        return null;
    }
    
    private String getTRDeLinea(String line){
        String regex = ".*("+ SINTAXIS_TR +")([A-Z0-9]+) .*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }
    
    private String getTRRequestResponseDeLinea(String line, boolean request){
        String regexRequest  = ".*(::: Alnova Request\\] )(.*)\\[\\/alnova\\].*";
        String regexResponse = "^.*(::: Alnova Response\\] )(.*)";
        Pattern pattern = Pattern.compile(regexRequest);
        if(!request){
            if(line.contains("[/alnova]")){
                regexResponse = "^.*(::: Alnova Response\\] )(.*)\\[\\/alnova\\].*";
            }
            pattern = Pattern.compile(regexResponse);
        }
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return "";
    }

}
