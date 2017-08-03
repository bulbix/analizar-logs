/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.controller;

import com.baz.mx.ArchivoNoSeleccionadoException;
import com.baz.mx.beans.ArchivoFTP;
import com.baz.mx.beans.InformacionSession;
import com.baz.mx.business.FileSearchOperations;
import com.baz.mx.business.ListarArchivos;
import com.baz.mx.dto.BusquedaGeneralDTO;
import com.baz.mx.dto.UsuarioDTO;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author acruzb
 */
@Controller
@Scope("session")
@RequestMapping(path = "/motor")
public class MotorController {
    
    private static final Logger LOGGER = Logger.getLogger(MotorController.class);
            
    @Autowired
    private ListarArchivos archivos;
    
    @Autowired
    private InformacionSession sessionData;
    
    @GetMapping(path = {"/", ""})
    public String inicio(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model){
        LOGGER.info("-----------------------------------------");
        LOGGER.info("Id de session obj session: " + session.getId());
        LOGGER.info("Id del obj data: " + sessionData);
        model.addAttribute("ArchivoFTP", sessionData.getArchivoId(sessionData.getIdArchivo()));
        return "motor/inicio";
    }
    
    @PostMapping(value= "listar/archivos", produces = "application/json")
    @ResponseBody
    public List<ArchivoFTP> postAllFiles(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        LOGGER.info("-----------------------------------------");
        LOGGER.info("Id de session obj session: " + session.getId());
        LOGGER.info("Id del obj data: " + sessionData);
        LOGGER.info("Lista de cookies post");
        for (Cookie c : request.getCookies()) {
            LOGGER.info(c.getValue());
        }
        List<ArchivoFTP> lista = archivos.obtenerLista();
        sessionData.setLista(lista);
        return lista;
    }
    
    @PostMapping(value= "establecer/archivo", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map<String, Boolean> establecerArchivo(@RequestBody ArchivoFTP archivo) {
        if(null != archivo){
            sessionData.setIdArchivo(archivo.getId());
            LOGGER.info("ArchvioFTP: " + archivo.getId());
            LOGGER.info("ArchvioFTP: " + archivo.getNombre());
            return Collections.singletonMap("success", true);
        }
        return Collections.singletonMap("success", false);
    }
    
    @PostMapping(value= "obtener/informacion/usuario", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String getUserInformation(@RequestBody UsuarioDTO usuario) throws ArchivoNoSeleccionadoException{
        LOGGER.info("json recibido: " + usuario);
        String path = sessionData.getRutaArchivoId(sessionData.getIdArchivo());
        LOGGER.info("path a consultar: " + path);
        validaPathSeleccionado(path);
        switch(usuario.getTipo()){
            case "usuario":
                return FileSearchOperations.procesarDetalleUsuario(Paths.get(path), usuario.getNombreUsuario());
            case "nombre":
                return FileSearchOperations.procesarDetalleCliente(Paths.get(path), usuario.getNombre(), usuario.getApellido());
            case "icu":
                return FileSearchOperations.procesarDetalleICU(Paths.get(path), usuario.getIcu());
            default:
                return null;
        }
    }
    
    @PostMapping(value= "busqueda/general", consumes = "application/json", produces = "text/plain")
    @ResponseBody
    public String getGeneralInformation(@RequestBody BusquedaGeneralDTO infoBusqueda) throws ArchivoNoSeleccionadoException{
        LOGGER.info("json recibido: " + infoBusqueda);
        String path = sessionData.getRutaArchivoId(sessionData.getIdArchivo());
        LOGGER.info("path a consultar: " + path);
        validaPathSeleccionado(path);
        String respuesta = null;
        //Solo usuario
        if (infoBusqueda.getUsuario().isVisible() && !infoBusqueda.getTrAlnova().isVisible() && !infoBusqueda.getTextoLibre().isVisible() && !infoBusqueda.getRuta().isVisible()) {//Solo usuario
            respuesta = FileSearchOperations.procesarSoloUsuario(Paths.get(path), infoBusqueda.getUsuario().getTexto(), false);
        }
        //solo tr
        else if(!infoBusqueda.getUsuario().isVisible() && infoBusqueda.getTrAlnova().isVisible() && !infoBusqueda.getTextoLibre().isVisible() && !infoBusqueda.getRuta().isVisible()){
            LOGGER.info("Se procesa solo tr.");
//            procesarSoloTR(path);
        }
        //solo ruta
        else if(!infoBusqueda.getUsuario().isVisible() && !infoBusqueda.getTrAlnova().isVisible() && !infoBusqueda.getTextoLibre().isVisible() && infoBusqueda.getRuta().isVisible()){
            LOGGER.info("Se procesa solo ruta.");
//            procesarSoloRuta(path);
        }
        //solo libre
        else if(!infoBusqueda.getUsuario().isVisible() && !infoBusqueda.getTrAlnova().isVisible() && infoBusqueda.getTextoLibre().isVisible() && !infoBusqueda.getRuta().isVisible()){
            LOGGER.info("Se procesa solo libre.");
//            procesarSoloLibre(path);
        }
        //usuario y tr
        else if(infoBusqueda.getUsuario().isVisible() && infoBusqueda.getTrAlnova().isVisible() && !infoBusqueda.getTextoLibre().isVisible() && !infoBusqueda.getRuta().isVisible()){
            LOGGER.info("Se procesa solo usuario y tr.");
//            procesarUsuarioTR(path);
        }
        //usuario y ruta
        else if(infoBusqueda.getUsuario().isVisible() && !infoBusqueda.getTrAlnova().isVisible() && !infoBusqueda.getTextoLibre().isVisible() && infoBusqueda.getRuta().isVisible()){
            LOGGER.info("Se procesa solo usuario y ruta.");
//            procesarUsuarioRuta(path);
        }
        return respuesta;
    }
    
    private void validaPathSeleccionado(String path) throws ArchivoNoSeleccionadoException {
        if (null == path) {
            throw new ArchivoNoSeleccionadoException("No se ha definido un archivo a procesar.");
        }
    }
    
}