/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.business;

import static com.baz.mx.business.FileSearchOperations.NEW_LINE;
import static com.baz.mx.business.FileSearchOperations.PATH_OPERACION_STATUS;
import static com.baz.mx.business.FileSearchOperations.SINTAXIS_HILO;
import static com.baz.mx.business.FileSearchOperations.SINTAXIS_RUTA;
import static com.baz.mx.business.FileSearchOperations.SINTAXIS_TR;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author acruzb
 */
public class Validaciones {
    
    private static final Logger LOGGER = Logger.getLogger(Validaciones.class);

    public static synchronized String getRutaDeLinea(String line) {
        String regex = ".*(PathInterceptor:2. - )([A-Za-z0-9\\/-]+).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }

    public static synchronized String getHiloCompletoDeLinea(String line) {
        String regex = ".*(" + SINTAXIS_HILO + ")(\\d+).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1) + matcher.group(2);
        }
        return null;
    }

    public static synchronized String getFechaDeLinea(String line) {
        String regex = ".*(\\d{4}-\\d{2}-\\d{2})\\s(\\d{2}:\\d{2}:\\d{2}).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }

    public static synchronized String getFechaCompleta(String line) {
        String regex = ".*(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2},\\d+).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static synchronized String[] getHoraCalculada(String horaActual) {
        String horaInicial;
        String horaFinal;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendarNow = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaActual.split(":")[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(horaActual.split(":")[1]));
        calendar.set(Calendar.SECOND, Integer.parseInt(horaActual.split(":")[2]));
        calendar.add(Calendar.SECOND, -2);
        if (calendar.get(Calendar.DAY_OF_MONTH) < calendarNow.get(Calendar.DAY_OF_MONTH)) {
            horaInicial = "00:00:00";
        } else {
            horaInicial = dateFormat.format(calendar.getTime());
        }
        calendar.add(Calendar.MINUTE, 3);
        if (calendar.get(Calendar.DAY_OF_MONTH) > calendarNow.get(Calendar.DAY_OF_MONTH)) {
            horaFinal = "23:59:59";
        } else {
            horaFinal = dateFormat.format(calendar.getTime());
        }
        return new String[]{horaInicial, horaFinal};
    }

    public static synchronized String getHiloPadreHiloDeLinea(String line) {
        String regex = ".*PrintThrowThreadConcurrentTaskExecutor:\\d+ - El hilo (.*) esta lanzando a (.*)...\\|.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1) + ":" + matcher.group(2);
        }
        return null;
    }

    public static synchronized String getTRDeLinea(String line) {
        String regex = ".*(" + SINTAXIS_TR + ")([A-Z0-9]+) .*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }

    public static synchronized String getTRRequestResponseDeLinea(String line, boolean request) {
        String regexRequest = ".*(::: Alnova Request\\] )(.*)\\[\\/alnova\\].*";
        String regexResponse = "^.*(::: Alnova Response\\] )(.*)";
        Pattern pattern = Pattern.compile(regexRequest);
        if (!request) {
            if (line.contains("[/alnova]")) {
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

    public static synchronized boolean isEndOperationLine(String line, String hilo) {
        return line.matches(String.format(PATH_OPERACION_STATUS, hilo));
    }

    public static synchronized boolean validarRangoDeTiempo(String linea, String horaInicio, String horaFin){
        String regex = ".*(\\d{4}-\\d{2}-\\d{2})\\s(\\d{2}:\\d{2}:\\d{2}).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(linea);
        if(matcher.find()){
            String horaLocal = matcher.group(2);
            LOGGER.info("Se verifica que " + horaLocal + " este entre:" + horaInicio + " y " + horaFin);
            return horaInicio.compareTo(horaLocal) <= 0 && horaFin.compareTo(horaLocal) >= 0;
        }
        return false;
    }
    
    public static boolean escribirTextPaneMenosUnaLinea(StringBuilder sb, ArrayList<String> lista, String hilo, String ... ruta){
        ArrayList<String> listaAux = new ArrayList<>();
        ArrayList<Integer> pos = new ArrayList<>();
        for (String line : lista) {
            try {
                String hiloLinea = getHiloCompletoDeLinea(line);
                if(hilo.equals(hiloLinea)){
                    listaAux.add(line);
                    if(line.matches(SINTAXIS_RUTA)){
                        pos.add(listaAux.size() - 1);
                    }
                }
            } catch (Exception ex) {
                LOGGER.info("No se pudo guardar: " + ex.getMessage());
            }
        }
        if(ruta.length > 0){//Caso de busqueda de usuario con ruta
            if(!listaAux.get(pos.get(pos.size() - 1 )).matches(".*" + ruta[0]+ ".*")){
                return false;
            }
        }
        int posAMostrar = pos.size() - 1 ;
        LOGGER.info("Se imprime la ruta inicial: " + posAMostrar  );
        if(posAMostrar > -1 ){
            LOGGER.info("Se imprime la ruta inicial: " + listaAux.get(pos.get(posAMostrar)) );
            for (int i = pos.get(posAMostrar); i < listaAux.size() - 1; i++) {
                String line = listaAux.get(i);
                sb.append(line).append(NEW_LINE);
            }
        }
        return true;
    }
    
}
