/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.beans;

/**
 *
 * @author acruzb
 */
/**
 * 
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

//import com.bancoazteca.bdm.digitalizacion.entity.enums.Product;
//import com.bancoazteca.bdm.encryptsecure.entity.beans.internal.RequestControlerInternalTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Miguel Angel Garcia Labastida 187047
 *
 */
public class GenerateDocumentsInternalRequest implements Map<String, Object> {
    
        private static String INMUTABLES = "|Documentos|NumContrato|ICU|Pais|Canal|Sucursal|Folio|CanalDig|SucDig|IdProducto|NumEmpleado|descProd|origen|llaveProd|Workstation|noPedido|descSubProd|";
        private static String TABULADOR_OBJECT = "_jsonObject";
    
	private static final Logger LOGGER = Logger.getLogger(GenerateDocumentsInternalRequest.class);

	@JsonIgnore
	private HashMap<String, Object> data;
	
	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 */
	public GenerateDocumentsInternalRequest() {
		data = new HashMap<>();
	}

	/**
	 * @return
	 * @see java.util.HashMap#size()
	 */
	@Override
	public int size() {
		return data.size();
	}

	/**
	 * @return
	 * @see java.util.HashMap#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	/**
	 * @param paramObject
	 * @return
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public Object get(Object paramObject) {
		return data.get(paramObject);
	}

	/**
	 * @param paramObject
	 * @return
	 * @see java.util.HashMap#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object paramObject) {
		return data.containsKey(paramObject);
	}

	/**
	 * @param paramK
	 * @param paramV
	 * @return
	 * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object put(String paramK, Object paramV) {
		if ("passUser".equals(paramK)) {
//			super.setPassUser(paramV.toString());
			return null;
		}
		if ("ipUser".equals(paramK)) {
//			super.setIpUser(paramV.toString());
			return null;
		}
		if ("nombreUser".equals(paramK)) {
//			super.setNombreUser(paramV.toString());
			return null;
		}
		if ("descProd".equals(paramK)) {
//			data.put("IdProducto", Product.valueOf(paramV.toString()).getId());
		}
		
		if("MeOpongo".equals(paramK)){
			if("x".equalsIgnoreCase(paramV.toString())){
				data.put("Aut01", "No Autorizo");
			}
			if("".equals(paramV.toString())){
				data.put("Aut01", "Autorizo");
			}
		}
		
		if("NoConsiento".equals(paramK)){
			if("x".equalsIgnoreCase(paramV.toString())){
				data.put("Aut02", "No Autorizo");
			}
			if("".equalsIgnoreCase(paramV.toString())){
				data.put("Aut02", "Autorizo");
			}
		}
		return data.put(paramK, paramV);
	}

	/**
	 * @param paramMap
	 * @see java.util.HashMap#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends String, ? extends Object> paramMap) {
		data.putAll(paramMap);
	}

	/**
	 * @param paramObject
	 * @return
	 * @see java.util.HashMap#remove(java.lang.Object)
	 */
	@Override
	public Object remove(Object paramObject) {
		return data.remove(paramObject);
	}

	/**
	 * 
	 * @see java.util.HashMap#clear()
	 */
	@Override
	public void clear() {
		data.clear();
	}

	/**
	 * @param paramObject
	 * @return
	 * @see java.util.HashMap#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object paramObject) {
		return data.containsValue(paramObject);
	}

	/**
	 * @return
	 * @see java.util.HashMap#clone()
	 */
	@Override
	public Object clone() {
		return data.clone();
	}

	/**
	 * @return
	 * @see java.util.HashMap#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		Set<java.util.Map.Entry<String, Object>> copy = new HashSet<>();
		validateDate();
		for (final Entry<String, Object> e : data.entrySet()) {
			MyEntry myEntry = new MyEntry();
			myEntry.setKey(e.getKey());
			if(e.getKey().matches(TABULADOR_OBJECT + ".*")){//Se realiza la extraccion del objecto json a primera posicion
				try {
					MyEntry dos = (MyEntry)e.getValue();
					JsonNode node = mapper.convertValue(dos, JsonNode.class);
					Iterator<String> names = node.getFieldNames();
					if(names.hasNext()){
						String first = names.next();//se toma la primera clave del json
						myEntry.setKey(first);
						myEntry.setValue(node.get(first));
					}
				} catch (Exception e1) {
					LOGGER.info("Incidencia con el objeto recibido: " + e.getValue(), e1);
				}
			}
			else if (!INMUTABLES.matches(".*\\|" + e.getKey() + "\\|.*")) {
				myEntry.setValue(getMutableObject(e.getKey(), e.getValue()));
			} else {
				myEntry.setValue(e.getValue());
			}
			copy.add(myEntry);
		}
		return copy;
	}

	private Object getMutableObject(String key, final Object value) {
		if (key.toLowerCase().matches(".*firma.*")) {
			return new Object() {
				public final Object img = value;
			};
		} else {
			return new Object() {
				public final Object texto = value;
			};
		}
	}

	/**
	 * Valida si envian los siguientes campos de lo contrario los agrega
	 * fd01 a fd13
	 * @param set
	 */
	private void validateDate() {
		if (data.containsKey("NumContrato") && data.get("NumContrato").toString().trim().equals("26")) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
			String date = dateFormat.format(new Date());
			for (int i = 1; i <= 14; ++i) {
				String fd = String.format("Fd%02d", i);
				if (!data.containsKey(fd)) {
					LOGGER.info("Incidencia en el request recibido falta que informen el campo:" + fd);
					put(fd, date);
				}
			}
			dateFormat = new SimpleDateFormat("dd 'de' MMMM 'de' YYYY");

			if (!data.containsKey("FechaAviso")) {
				LOGGER.info("Incidencia en el request recibido falta que informen el campo FechaAviso");
				put("FechaAviso", dateFormat.format(new Date()));
			}
			if (!data.containsKey("MeOpongo")) {
				LOGGER.info("Incidencia en el request recibido falta que informen el campo MeOpongo");
				put("MeOpongo", "");
			}
			if (!data.containsKey("NoConsiento")) {
				LOGGER.info("Incidencia en el request recibido falta que informen el campo NoConsiento");
				put("NoConsiento", "");
			}
		}
	}

	/**
	 * @param paramObject
	 * @return
	 * @see java.util.AbstractMap#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object paramObject) {
		return data.equals(paramObject);
	}

	/**
	 * @return
	 * @see java.util.AbstractMap#hashCode()
	 */
	@Override
	public int hashCode() {
		return data.hashCode();
	}

	/**
	 * @return
	 * @see java.util.HashMap#keySet()
	 */
	@Override
	public Set<String> keySet() {
		return data.keySet();
	}

	/**
	 * @return
	 * @see java.util.AbstractMap#toString()
	 */
	@Override
	public String toString() {
		return data.toString();
	}

	/**
	 * @return
	 * @see java.util.HashMap#values()
	 */
	@Override
	public Collection<Object> values() {
		return data.values();
	}

	private class MyEntry implements Map.Entry<String, Object> {

		private String key;
		private Object value;

		@Override
		public String getKey() {
			return key;
		}

		public String setKey(String arg1) {
			key = arg1;
			return key;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Map.Entry#getValue()
		 */
		@Override
		public Object getValue() {
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Map.Entry#setValue(java.lang.Object)
		 */
		@Override
		public Object setValue(Object arg0) {
			value = arg0;
			return value;
		}

	}

}
