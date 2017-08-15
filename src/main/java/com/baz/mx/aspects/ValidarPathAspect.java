
package com.baz.mx.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import com.baz.mx.annotations.ValidarPath;
import com.baz.mx.beans.InformacionSession;
import com.baz.mx.exceptions.ArchivoNoSeleccionadoException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author acruzb
 */
@Aspect
@Component
public class ValidarPathAspect {
    
    private static final Logger LOGGER = Logger.getLogger(ValidarPathAspect.class);
    
    @Autowired
    private InformacionSession sessionData;
    
    @Before("@annotation(validarPath)")
    public void validarPathSeleccionado(ValidarPath validarPath) throws ArchivoNoSeleccionadoException{
        validaPathSeleccionado(sessionData.getRutaArchivoId(sessionData.getIdArchivo()));
    }
    
    private void validaPathSeleccionado(String path) throws ArchivoNoSeleccionadoException {
        if (null == path) {
            LOGGER.info("No se ha definido un archivo a procesar.");
            throw new ArchivoNoSeleccionadoException("No se ha definido un archivo a procesar.");
        }
        LOGGER.info("El archivo a procesar es válido: " + path);
    }
    
}
