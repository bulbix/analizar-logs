package com.baz.mx.business;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	Logger log = LoggerFactory.getLogger(SearchOperations.class);

	public String procesarSoloLibre(String fieldLibre, Integer numRegistros, String[] rangoTiempo, String... indices) {
		List<Map<String,Object>> documents  = logBazES.searchTerm(fieldLibre, numRegistros, rangoTiempo, indices);
		
		StringBuilder result = new StringBuilder();
		
		for(Map<String,Object> doc: documents) {
			DateTime logdate = new DateTime(doc.get("@logdate"));
			String line = String.format("[#| %s %s  %s %s - %s@@@%s", logdate.toString("yyyy-MM-dd HH:mm:ss,SSS"),doc.get("loglevel"),doc.get("thread"),doc.get("classname"),doc.get("msgbody"), doc.get("source"));
			//log.info(line);
			result.append(line + "\n");
		}
		
		return result.toString();
	}
	
	public String procesarLibreDetalle(String linea, String... indices) {
		linea  = linea.split("@@@")[0];
		return logBazES.getThread(linea, indices).stream().collect(Collectors.joining("\n"));
	}
	
	public String[] getIndices() {
		return logBazES.getIndices();
	}
}
