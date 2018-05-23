package com.baz.mx.business;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoazteca.logES.LogService;

/***
 * 
 * @author lpradof
 *
 */
@Service
public class SearchOperations {

	@Autowired LogService logBazES;

	public String procesarSoloLibre(String fieldLibre, Integer numRegistros, String indices) {

		List<Map<String,Object>> documents  = logBazES.searchTerm(fieldLibre, numRegistros, indices);
		
		return documents.stream().map(map -> map.get("message").toString()).collect(Collectors.joining("\n"));
	}
	
	public String procesarLibreDetalle(String linea, String indices) {
		
		return logBazES.getThread(linea, indices).stream().collect(Collectors.joining("\n"));
		
	}
}
