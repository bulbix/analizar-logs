/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.business;

import com.baz.mx.beans.Registro;
import static com.baz.mx.business.Validaciones.escribirTextPaneMenosUnaLinea;
import static com.baz.mx.business.Validaciones.getHiloCompletoDeLinea;
import static com.baz.mx.business.Validaciones.getRutaDeLinea;
import static com.baz.mx.business.Validaciones.getTRDeLinea;
import static com.baz.mx.business.Validaciones.isEndOperationLine;
import static com.baz.mx.business.Validaciones.validarRangoDeTiempo;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
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
    public static final String NEW_LINE = System.getProperty("line.separator");

    public static final String SINTAXIS_USUARIO = " - Alias: ";
    public static final String SINTAXIS_TR = "Transaccion ";
    public static final String SINTAXIS_HILO = "HTTP-CRED-|Thread-";
    public static final String SINTAXIS_RUTA = ".*PathInterceptor:\\d+\\s-\\s/\\w+/.*";
    public static final String SINTAXIS_INFO_CLIENTE = "BusquedaPersonaImpl:";
    public static final String SINTAXIS_INFO_CLIENTE_FULL = ".*BusquedaPersonaImpl:\\d+.*";
    public static final String SINTAXIS_HILO_HIJO = ".*PrintThrowThreadConcurrentTaskExecutor:\\d+ - El hilo.*";
    public static final String PATH_OPERACION_STATUS = ".*%s.*ObjEncriptacionSeguridad:\\d+ - doObjToStrJSON:.*codigoOperacion.*";

    private static final int MAX_LINEAS_POR_HILO = 5000;
    private static final int LINEAS_HILO_DIF_MAX = 500;

    private static ArrayList<String> clientesEncontrados;

    public FileSearchOperations() {
        clientesEncontrados = new ArrayList<>();
    }
    
    //SECCION DE CONSULTA DE INFORMACION DE USUARIOS
    public static String procesarDetalleCliente(Path path, String nombre, String apellido) {
        clientesEncontrados = new ArrayList<>();
        try {
            LineIterator it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
            try {
                LOGGER.info(String.format("Buscando el usuario %s %s ", nombre, apellido));
                while (it.hasNext()) {
                    String line = it.nextLine();
                    if (line.contains(SINTAXIS_INFO_CLIENTE)) {
                        String regex = SINTAXIS_INFO_CLIENTE_FULL;
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find() && isCliente(line, nombre, apellido)) {
                            return escribirClienteFormato(line);
                        }
                    }
                }
            } finally {
                LOGGER.info("Termina la busqueda");
                LineIterator.closeQuietly(it);
            }
        } catch (IOException e) {

        }
        return null;
    }

    public static String procesarDetalleICU(Path path, String icu) {
        clientesEncontrados = new ArrayList<>();
        try {
            LineIterator it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
            try {
                LOGGER.info(String.format("Buscando el ICU %s ", icu));
                while (it.hasNext()) {
                    String line = it.nextLine();
                    if (line.contains(SINTAXIS_INFO_CLIENTE)) {
                        String regex = SINTAXIS_INFO_CLIENTE_FULL;
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find() && isICU(line, icu)) {
                            return escribirClienteFormato(line);
                        }
                    }
                }
            } finally {
                LOGGER.info("Termina la busqueda");
                LineIterator.closeQuietly(it);
            }
        } catch (Exception e) {

        }
        return null;
    }

    public static String procesarDetalleUsuario(Path path, String nombreUsuario) {
        LineIterator it;
        try {
            it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
            try {
                System.out.println(String.format("Buscando el usuario %s ", nombreUsuario));
                clientesEncontrados = new ArrayList<>();
                LOGGER.info(String.format("Buscando el usuario %s ", nombreUsuario));
                while (it.hasNext()) {
                    String line = it.nextLine();
                    if (line.contains(SINTAXIS_INFO_CLIENTE)) {
                        String regex = SINTAXIS_INFO_CLIENTE_FULL;
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find() && isUsario(line, nombreUsuario)) {
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

    private static boolean isUsario(String line, String nombreUsuario) {
        String regex = ".*\"alias\":(.+),\"nivel\".*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String nomUsuarioLocal = matcher.group(1);
            nomUsuarioLocal = nomUsuarioLocal.replaceAll("[\"\\[\\]]", "").replace(",", " ");
            if (nomUsuarioLocal.toLowerCase().contains(nombreUsuario.toLowerCase())) {
                LOGGER.info("Usuario encontrado: " + nomUsuarioLocal);
                for (String cliente : clientesEncontrados) {
                    if (cliente.equals(nomUsuarioLocal)) {
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

    private static boolean isCliente(String line, String nombre, String apellido) {
        String regex = ".*nombre\":\"([A-Z-a-zñÑ ]+)...apellidos\":(.+)..genero_cu.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String nom = matcher.group(1);
            String apellidos = matcher.group(2);
            apellidos = apellidos.replaceAll("[\"\\[\\]]", "").replace(",", " ");
            if (nom.toLowerCase().contains(nombre.toLowerCase()) && apellidos.toLowerCase().contains(apellido.toLowerCase())) {
                String nombreCompleto = nom + " " + apellidos;
                LOGGER.info("Cliente encontrado: " + nombreCompleto);
                for (String cliente : clientesEncontrados) {
                    if (cliente.equals(nombreCompleto)) {
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

    private static boolean isICU(String line, String icu) {
        String regex = ".*\"icu\":(.+),\"fecha_registro\".*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String icuLocal = matcher.group(1);
            icuLocal = icuLocal.replaceAll("[\"\\[\\]]", "").replace(",", " ");
            if (icuLocal.toLowerCase().contains(icu.toLowerCase())) {
                LOGGER.info("ICU encontrado: " + icuLocal);
                for (String cliente : clientesEncontrados) {
                    if (cliente.equals(icuLocal)) {
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

    private static String escribirClienteFormato(String line) throws IOException {
        String regex = ".*Se ha encontrado al menos un registro para la busqueda.(.+).+\\|.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(matcher.group(1), Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        }
        return null;
    }

    //SECCION DE BUSQUEDA DE INFORMACION GENERAL POR CRITERIO
    public static String procesarSoloUsuario(Path path, String usuarioField, boolean intervaloHora, String... rangoTiempo) {
        boolean encontrado = false;
        int lineasPorHilo = 0;
        String hilo = null;
        int lineaHiloDif = 0;
        LOGGER.info("Iniciando la busqueda del usuario: " + usuarioField);
        usuarioField = usuarioField.toLowerCase();
        LineIterator it = null;
        StringBuilder salida = new StringBuilder();
        try {
            it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
            Registro registro = new Registro();
            while (it.hasNext()) {
                String line = it.nextLine();
                registro.add(line);
                if (!encontrado && line.contains(SINTAXIS_USUARIO) && line.toLowerCase().contains(usuarioField)) {
                    boolean isRango = true;
                    if (intervaloHora) {//VERIFICACION DE INTERVALO DE TIEMPO
                        isRango = validarRangoDeTiempo(line, rangoTiempo[0], rangoTiempo[1]);
                    }
                    if (isRango) {
                        hilo = getHiloCompletoDeLinea(line);
                        lineaHiloDif = 0;
                        encontrado = true;
                        lineasPorHilo = 0;
                        LOGGER.info("procesando hilo " + hilo);
                        escribirTextPaneMenosUnaLinea(salida, registro.getRegistro(), hilo);
                    }
                }
                if (encontrado) {
                    if (line.contains(hilo) && lineasPorHilo++ <= MAX_LINEAS_POR_HILO) {
                        salida.append(line).append(NEW_LINE);
                    } else {
                        lineaHiloDif++;
                    }
                    if (lineaHiloDif >= LINEAS_HILO_DIF_MAX) {
                        encontrado = false;
                        System.out.println("SE ALCANZÓ EL MAXIMO " + lineaHiloDif);
                    }
                    if (isEndOperationLine(line, hilo)) {//Fin de la operacion
                        salida.append(NEW_LINE);
                        encontrado = false;
                    }
                }
            }
        } catch (IOException ex) {

        } finally {
            if (null != it) {
                LineIterator.closeQuietly(it);
            }
            LOGGER.info("Termina la busqueda solo usuario");
        }
        return salida.toString();
    }

    public static String procesarSoloTR(Path path, String fieldTR, boolean intervaloHora, String... rangoTiempo) {
        StringBuilder salida = new StringBuilder();
        if (null != fieldTR && !"".equals(fieldTR)) {
            fieldTR = fieldTR.trim().toUpperCase();
            LineIterator it = null;
            try {
                it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
                LOGGER.info("Buscando el TR: " + fieldTR);
                while (it.hasNext()) {
                    String line = it.nextLine();
                    if (line.contains(SINTAXIS_TR)) {
                        String tr = getTRDeLinea(line);
                        if (fieldTR.equalsIgnoreCase(tr)) {
                            boolean isRango = true;
                            if (intervaloHora) {
                                isRango = validarRangoDeTiempo(line, rangoTiempo[0], rangoTiempo[1]);
                            }
                            if (isRango) {
                                if (line.contains("Alnova Request")) {
                                    salida.append("Entrada:").append(NEW_LINE);
                                    salida.append(line).append(NEW_LINE).append(NEW_LINE);
                                } else {
                                    salida.append("Salida:").append(NEW_LINE);
                                    salida.append(line).append(NEW_LINE).append(NEW_LINE).append(NEW_LINE).append(NEW_LINE);
                                }
                            }
                        }
                    }
                }
            } catch (IOException ex) {
            } finally {
                if (null != it) {
                    LineIterator.closeQuietly(it);
                }
                LOGGER.info("Termina la busqueda solo tr");
            }
        }
        return salida.toString();
    }

    public static String procesarSoloRuta(Path path, String fieldRuta, boolean intervaloHora, String... rangoTiempo) {
        boolean encontrado = false;
        String hilo = "";
        String rutaCompleta;
        int lineaHiloDif = 0;
        StringBuilder salida = new StringBuilder();
        if (null != fieldRuta && !"".equals(fieldRuta) && (fieldRuta.contains("/") || fieldRuta.length() > 15)) {
            fieldRuta = fieldRuta.trim();
            LineIterator it = null;
            try {
                it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
                LOGGER.info("Buscando la ruta: " + fieldRuta);
                while (it.hasNext()) {
                    String line = it.nextLine();
                    if (line.matches(SINTAXIS_RUTA)) {
                        rutaCompleta = getRutaDeLinea(line);
                        if (null != rutaCompleta && rutaCompleta.contains(fieldRuta)) {
                            boolean isRango = true;
                            if (intervaloHora) {
                                isRango = validarRangoDeTiempo(line, rangoTiempo[0], rangoTiempo[1]);
                            }
                            if (isRango) {
                                hilo = getHiloCompletoDeLinea(line);
                                encontrado = true;
                                lineaHiloDif = 0;
                            }
                        }
                    }
                    if (encontrado) {
                        String hiloLocal = getHiloCompletoDeLinea(line);
                        if (hilo.equals(hiloLocal)) {
                            salida.append(line).append(NEW_LINE);
                        } else {
                            lineaHiloDif++;
                        }
                        if (lineaHiloDif >= LINEAS_HILO_DIF_MAX) {
                            encontrado = false;
                        }
                        if (isEndOperationLine(line, hilo)) {//Fin de la operacion
                            encontrado = false;
                            salida.append(NEW_LINE).append(NEW_LINE);
                        }
                    }
                }
            } catch (IOException ex) {
            } finally {
                if (null != it) {
                    LineIterator.closeQuietly(it);
                }
                LOGGER.info("Termina la busqueda de solo path");
            }
        }
        return salida.toString();
    }

    public static String procesarSoloLibre(Path path, String fieldLibre, boolean intervaloHora, String... rangoTiempo) {
        StringBuilder salida = new StringBuilder();
        if (null != fieldLibre && !"".equals(fieldLibre)) {
            LineIterator it = null;
            try {
                it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
                LOGGER.info("Buscando el texto: " + fieldLibre);
                while (it.hasNext()) {
                    String line = it.nextLine();
                    if (line.contains(fieldLibre)) {
                        boolean isRango = true;
                        if (intervaloHora) {
                            isRango = validarRangoDeTiempo(line, rangoTiempo[0], rangoTiempo[1]);
                        }
                        if (isRango) {
                            salida.append(line).append(NEW_LINE);
                        }
                    }
                }
            } catch (IOException ex) {
            } finally {
                if (null != it) {
                    LineIterator.closeQuietly(it);
                }
                LOGGER.info("Termina la busqueda de texto libre");
            }
        }
        return salida.toString();
    }

    public static String procesarUsuarioRuta(Path path, String usuarioField, String rutaField, boolean intervaloHora, String... rangoTiempo) {
        StringBuilder salida = new StringBuilder();
        String hilo = null;
        usuarioField = usuarioField.toLowerCase();
        boolean lineasSig = false;//Lineas despues de encontrar el response
        int numLinSig = 0;
        LOGGER.info("Iniciando la busqueda del usuario " + usuarioField + " y una ruta " + rutaField);
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
            Registro registro = new Registro();
            while (it.hasNext()) {
                String line = it.nextLine();
                registro.add(line);
                if (line.contains(SINTAXIS_USUARIO) && line.toLowerCase().contains(usuarioField)) {
                    hilo = getHiloCompletoDeLinea(line);
                    if (registro.buscarRutaHilo(rutaField, hilo)) {
                        boolean isRango = true;
                        if (intervaloHora) {
                            isRango = validarRangoDeTiempo(line, rangoTiempo[0], rangoTiempo[1]);
                        }
                        if (isRango) {
                            if (escribirTextPaneMenosUnaLinea(salida, registro.getRegistro(), hilo, rutaField)) {
                                salida.append(line).append(NEW_LINE);
                                lineasSig = true;
                                numLinSig = 0;
                            }
                        }
                    }
                } else if (lineasSig) {
                    if (numLinSig++ < MAX_LINEAS_POR_HILO) {
                        String hiloLinea = getHiloCompletoDeLinea(line);
                        if (null != hilo && hilo.equals(hiloLinea)) {
                            salida.append(line).append(NEW_LINE);
                        }
                    } else {
                        lineasSig = false;
                    }
                    if (isEndOperationLine(line, hilo)) {//Fin de la operacion
                        lineasSig = false;
                    }
                }
            }
        } catch (IOException ex) {
        } finally {
            if (null != it) {
                LineIterator.closeQuietly(it);
            }
            LOGGER.info("Termina la busqueda usuario mas ruta");
        }
        return salida.toString();
    }

    public static String procesarUsuarioTR(Path path, String usuarioField, String fieldTR, boolean intervaloHora, String... rangoTiempo) {
        StringBuilder salida = new StringBuilder();
        String hilo = null;
        boolean lineasSig = false;//Lineas despues de encontrar el response
        Registro registro = new Registro();
        usuarioField = usuarioField.toLowerCase();
        LOGGER.info("Iniciando la busqueda del usuario: " + usuarioField + " ya tr: " + fieldTR);
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
            int numLinSig = 0;
            while (it.hasNext()) {
                String line = it.nextLine();
                registro.add(line);//Se agrega la linea al registro
                if (line.contains(SINTAXIS_TR)) {
                    String tr = getTRDeLinea(line);
                    if (fieldTR.equalsIgnoreCase(tr)) {
                        hilo = getHiloCompletoDeLinea(line);
                        if (registro.buscarUsuarioHilo(usuarioField, hilo)) {
                            boolean isRango = true;
                            if (intervaloHora) {
                                isRango = validarRangoDeTiempo(line, rangoTiempo[0], rangoTiempo[1]);
                            }
                            if (isRango) {
                                LOGGER.info("Se encuentra la TR " + tr + " para el usuario: " + usuarioField + " -- " + ((line.contains("Alnova Request")) ? "REQUEST" : "RESPONSE"));
                                if (line.contains("Alnova Request")) {
                                    escribirTextPaneMenosUnaLinea(salida, registro.getRegistro(), hilo);
                                    salida.append(NEW_LINE).append("Entrada:").append(NEW_LINE);
                                    salida.append(line).append(NEW_LINE);
                                } else {
                                    salida.append(NEW_LINE).append("Salida:").append(NEW_LINE);
                                    salida.append(line).append(NEW_LINE).append(NEW_LINE);
                                    lineasSig = true;
                                    numLinSig = 0;
                                }
                            }
                        }
                    }
                } else if (lineasSig) {
                    if (numLinSig++ < MAX_LINEAS_POR_HILO) {
                        String hiloLinea = getHiloCompletoDeLinea(line);
                        if (null != hilo && hilo.equals(hiloLinea)) {
                            salida.append(line).append(NEW_LINE);
                        }
                    } else {
                        lineasSig = false;
                    }
                    if (isEndOperationLine(line, hilo)) {//Fin de la operacion
                        lineasSig = false;
                    }
                }
            }
        } catch (IOException ex) {
        } finally {
            LineIterator.closeQuietly(it);
            LOGGER.info("Termina la busqueda de usuario y tr.");
        }
        return salida.toString();
    }

    public static String procesarLibreDetalle(Path path, String texto) {
        StringBuilder salida = new StringBuilder();
        LineIterator it = null;
        boolean lineasSig = false;//Lineas despues de encontrar el response
        Registro registro = new Registro();
        int numLinSig = 0;
        String hilo = null;
        try {
            it = FileUtils.lineIterator(path.toFile(), ENCODING.name());
            texto = texto.trim();
            LOGGER.info("Buscando el texto: " + texto);
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
                    if (numLinSig < MAX_LINEAS_POR_HILO) {
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
            LOGGER.info("Termina la busqueda de linea: " + salida.toString().length());
        }
        return salida.toString();
    }

}
