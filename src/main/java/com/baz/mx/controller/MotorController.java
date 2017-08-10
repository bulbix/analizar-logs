/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.controller;

import com.baz.mx.beans.ArchivoFTP;
import com.baz.mx.beans.ArchivosEnDescargaFTP;
import com.baz.mx.beans.InformacionSession;
import com.baz.mx.business.Cifrador;
import com.baz.mx.business.DescargarArchivoFTP;
import com.baz.mx.business.Encryptor;
import com.baz.mx.business.FTPUtils;
import com.baz.mx.business.FileSearchOperations;
import com.baz.mx.business.ListarArchivos;
import com.baz.mx.dto.BusquedaGeneralDTO;
import com.baz.mx.dto.UsuarioDTO;
import com.baz.mx.entity.FileUpload;
import com.baz.mx.exceptions.ArchivoNoSeleccionadoException;
import com.baz.mx.exceptions.FTPConexionException;
import com.baz.mx.request.ActualizarArchivoFTPRequest;
import com.baz.mx.request.BusquedaLineaRequest;
import com.baz.mx.request.CifradoCadenaRequest;
import com.baz.mx.request.ObtenerArchivosFTPRequest;
import com.baz.mx.response.BusquedaGeneralResponse;
import com.baz.mx.response.CifradoCadenaResponse;
import com.baz.mx.utils.Constantes;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
    @Autowired
    private DescargarArchivoFTP descargarFTP;
    @Autowired
    private ArchivosEnDescargaFTP descargnadoftp;
    
    @GetMapping(path = {"/", ""})
    public String inicio(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model){
        LOGGER.info("-----------------------------------------");
        LOGGER.info("Id de session obj session: " + session.getId());
        model.addAttribute("ArchivoFTP", sessionData.getArchivoId(sessionData.getIdArchivo()));
        model.addAttribute("FTPServers", Constantes.FTP_SERVERS);
        return "motor/inicio";
    }
    
    @PostMapping(value= "listar/archivos", produces = "application/json")
    @ResponseBody
    public List<ArchivoFTP> postAllFiles(@RequestBody Map<String, Boolean> request, HttpSession session) {
        LOGGER.info("-----------------------------------------");
        LOGGER.info("Se listan los archivos de la carpeta: " + ((Boolean.valueOf(request.get("core").toString()))? "CORE": "JVC"));
        List<ArchivoFTP> lista = archivos.obtenerLista(Boolean.valueOf(request.get("core").toString()));
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
    
    @PostMapping(value= "eliminar/archivo/servidor", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map<String, Boolean> eliminarArchivo(@RequestBody ArchivoFTP archivo) {
        LOGGER.info("json recibido: " + archivo);
        String path = sessionData.getRutaArchivoId(archivo.getId());
        LOGGER.info("Archivo a eliminar: " + path);
        if(null != path){
            boolean eliminado = archivos.eliminarArchivoHD(path);
            if(eliminado){
                LOGGER.info("Se elimina el archivo: " + archivo.getNombre());
            }else{
                LOGGER.info("No se pudo eliminar el archivo: " + archivo.getNombre());
            }
            return Collections.singletonMap("success", eliminado);
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
    public ArrayList<ArchivoFTP> getArchivosFTP(@RequestBody ObtenerArchivosFTPRequest infoFTP) throws FTPConexionException{
        LOGGER.info("json recibido: " + infoFTP);
        return ftpUtils.obtenerArchivosBD_JVC(infoFTP.getIp(), infoFTP.isCore());
    }
    
    @PostMapping(value= "/actualizar/archivo/ftp", consumes = "application/json", produces = {"application/json"})
    @ResponseBody
    public Map<String, Boolean> actualizaArchivoFTP(@RequestBody ActualizarArchivoFTPRequest infoFTP) throws ArchivoNoSeleccionadoException{
        LOGGER.info("json recibido: " + infoFTP);
        boolean respuesta = descargarFTP.descargarArchivoFTP(infoFTP.getIp(), infoFTP.isCore(), infoFTP.getArchivo().getNombre(), infoFTP.getArchivo().getTamanoKB());
        return Collections.singletonMap("success", respuesta);
    }
    
    @PostMapping(value= "/actualizar/archivo/porcentajes", produces = {"application/json"})
    @ResponseBody
    public ArchivosEnDescargaFTP getPorcentajesArchivoFTP(){
        LOGGER.info("Solicitando datos: " + descargnadoftp);
        return descargnadoftp;
    }
    
    //SECCION DE HERRAMIENTAS
    
    @PostMapping(value= "/cifrado", consumes = "application/json", produces = {"application/json"})
    @ResponseBody
    public CifradoCadenaResponse cifrado(@RequestBody CifradoCadenaRequest request){
        LOGGER.info("json recibido: " + request);
        try {
            String cadenaPro;
            if (request.isCifrar()) {
                cadenaPro = Encryptor.encryptString(request.getCadena(), request.getSistema());
            } else {
                cadenaPro = Encryptor.decryptString(request.getCadena(), request.getSistema());
            }
            return new CifradoCadenaResponse(request.getCadena(), cadenaPro, request.getSistema().toString());
        } catch (Exception e) {
            LOGGER.info("Ocurrió un erro al cifrar/descifrar.", e);
        }
        return null;
    }
    
    @PostMapping(value = "/subir/multiples/archivos/cifrados")
    public ResponseEntity uploadFile(MultipartHttpServletRequest request) {
        LOGGER.info("Se llega el servidor con archivos.");
        try {
            Iterator<String> itr = request.getFileNames();
            while (itr.hasNext()) {
                String uploadedFile = itr.next();
                MultipartFile file = request.getFile(uploadedFile);
                String mimeType = file.getContentType();
                String filename = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                LOGGER.info("Procesando archivo: " + filename);
                FileUpload newFile = new FileUpload(filename, bytes, mimeType);
            }
        }
        catch (IOException e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
    
    @PostMapping(value = "/subir/archivo/cifrado", produces = "application/json")
    @ResponseBody
    public BusquedaGeneralResponse descifrarProperties(@RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            String mimeType = file.getContentType();
            String filename = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            LOGGER.info("Descifrando el archivo: " + filename + " de tipo: " + mimeType);
            return new BusquedaGeneralResponse(Cifrador.decrypt(bytes));
        } catch (Exception ex) {
            LOGGER.info("No se pudo descifrar el archivo.", ex);
            return new BusquedaGeneralResponse();
        }
    }
    
    
    private void validaPathSeleccionado(String path) throws ArchivoNoSeleccionadoException {
        if (null == path) {
            throw new ArchivoNoSeleccionadoException("No se ha definido un archivo a procesar.");
        }
    }
    
}