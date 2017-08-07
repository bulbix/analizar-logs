/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.controller;

import com.baz.mx.ArchivoNoSeleccionadoException;
import com.baz.mx.beans.ArchivoFTP;
import com.baz.mx.beans.InformacionSession;
import com.baz.mx.business.FTPUtils;
import com.baz.mx.business.FileSearchOperations;
import com.baz.mx.business.ListarArchivos;
import com.baz.mx.dto.BusquedaGeneralDTO;
import com.baz.mx.dto.UsuarioDTO;
import com.baz.mx.request.BusquedaLineaRequest;
import com.baz.mx.request.ObtenerArchivosFTPRequest;
import com.baz.mx.response.BusquedaGeneralResponse;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
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
    
    @Autowired
    private FTPUtils ftpUtils;
    
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
    
    @PostMapping(value= "busqueda/general", consumes = "application/json", produces = {"application/json"})
    @ResponseBody
    public BusquedaGeneralResponse getGeneralInformation(@RequestBody BusquedaGeneralDTO infoBusqueda) throws ArchivoNoSeleccionadoException{
        LOGGER.info("json recibido: " + infoBusqueda);
        String path = sessionData.getRutaArchivoId(sessionData.getIdArchivo());
        LOGGER.info("path a consultar: " + path);
        validaPathSeleccionado(path);
        String respuesta = null;
        //Solo usuario
        if (infoBusqueda.getUsuario().isVisible() && !infoBusqueda.getTrAlnova().isVisible() && !infoBusqueda.getTextoLibre().isVisible() && !infoBusqueda.getRuta().isVisible()) {//Solo usuario
            respuesta = FileSearchOperations.procesarSoloUsuario(Paths.get(path), infoBusqueda.getUsuario().getTexto(), infoBusqueda.getRangoTiempo().isVisible(), infoBusqueda.getRangoTiempo().isVisible() ? infoBusqueda.getRangoTiempo().getTexto().split("-"): null);
        }
        //solo tr
        else if(!infoBusqueda.getUsuario().isVisible() && infoBusqueda.getTrAlnova().isVisible() && !infoBusqueda.getTextoLibre().isVisible() && !infoBusqueda.getRuta().isVisible()){
            respuesta = FileSearchOperations.procesarSoloTR(Paths.get(path), infoBusqueda.getTrAlnova().getTexto(), infoBusqueda.getRangoTiempo().isVisible(), infoBusqueda.getRangoTiempo().isVisible() ? infoBusqueda.getRangoTiempo().getTexto().split("-"): null);
        }
        //solo ruta
        else if(!infoBusqueda.getUsuario().isVisible() && !infoBusqueda.getTrAlnova().isVisible() && !infoBusqueda.getTextoLibre().isVisible() && infoBusqueda.getRuta().isVisible()){
            respuesta = FileSearchOperations.procesarSoloRuta(Paths.get(path), infoBusqueda.getRuta().getTexto(), infoBusqueda.getRangoTiempo().isVisible(), infoBusqueda.getRangoTiempo().isVisible() ? infoBusqueda.getRangoTiempo().getTexto().split("-"): null);
        }
        //solo libre
        else if(!infoBusqueda.getUsuario().isVisible() && !infoBusqueda.getTrAlnova().isVisible() && infoBusqueda.getTextoLibre().isVisible() && !infoBusqueda.getRuta().isVisible()){
            LOGGER.info("Se procesa solo libre.");
            respuesta = FileSearchOperations.procesarSoloLibre(Paths.get(path), infoBusqueda.getTextoLibre().getTexto(), infoBusqueda.getRangoTiempo().isVisible(), infoBusqueda.getRangoTiempo().isVisible() ? infoBusqueda.getRangoTiempo().getTexto().split("-"): null);
        }
        //usuario y tr
        else if(infoBusqueda.getUsuario().isVisible() && infoBusqueda.getTrAlnova().isVisible() && !infoBusqueda.getTextoLibre().isVisible() && !infoBusqueda.getRuta().isVisible()){
            LOGGER.info("Se procesa solo usuario y tr.");
            respuesta = FileSearchOperations.procesarUsuarioTR(Paths.get(path), infoBusqueda.getUsuario().getTexto(), infoBusqueda.getTrAlnova().getTexto(), infoBusqueda.getRangoTiempo().isVisible(), infoBusqueda.getRangoTiempo().isVisible() ? infoBusqueda.getRangoTiempo().getTexto().split("-"): null);
        }
        //usuario y ruta
        else if(infoBusqueda.getUsuario().isVisible() && !infoBusqueda.getTrAlnova().isVisible() && !infoBusqueda.getTextoLibre().isVisible() && infoBusqueda.getRuta().isVisible()){
            LOGGER.info("Se procesa solo usuario y ruta.");
            respuesta = FileSearchOperations.procesarUsuarioRuta(Paths.get(path), infoBusqueda.getUsuario().getTexto(), infoBusqueda.getRuta().getTexto(), infoBusqueda.getRangoTiempo().isVisible(), infoBusqueda.getRangoTiempo().isVisible() ? infoBusqueda.getRangoTiempo().getTexto().split("-"): null);
        }
        return new BusquedaGeneralResponse(respuesta);
    }
    
    @PostMapping(value= "busqueda/linea", consumes = "application/json", produces = {"application/json"})
    @ResponseBody
    public BusquedaGeneralResponse getLineaInformation(@RequestBody BusquedaLineaRequest infoBusqueda) throws ArchivoNoSeleccionadoException{
        LOGGER.info("json recibido: " + infoBusqueda);
        String path = sessionData.getRutaArchivoId(sessionData.getIdArchivo());
        LOGGER.info("path a consultar: " + path);
        validaPathSeleccionado(path);
        return new BusquedaGeneralResponse(FileSearchOperations.procesarLibreDetalle(Paths.get(path), infoBusqueda.getLinea()));
    }
    
    @PostMapping(value= "obtener/archivos/ftp", consumes = "application/json", produces = {"application/json"})
    @ResponseBody
    public BusquedaGeneralResponse getArchivosFTP(@RequestBody ObtenerArchivosFTPRequest infoFTP) throws ArchivoNoSeleccionadoException{
        LOGGER.info("json recibido: " + infoFTP);
        ftpUtils.obtenerArchivosBD_JVC(infoFTP.getIp(), infoFTP.isCore());
//        return new BusquedaGeneralResponse(FileSearchOperations.procesarLibreDetalle(Paths.get(path), infoBusqueda.getLinea()));
return null;
    }
    
    
    private void validaPathSeleccionado(String path) throws ArchivoNoSeleccionadoException {
        if (null == path) {
            throw new ArchivoNoSeleccionadoException("No se ha definido un archivo a procesar.");
        }
    }
    
}