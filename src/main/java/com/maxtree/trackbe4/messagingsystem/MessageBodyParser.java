package com.maxtree.trackbe4.messagingsystem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageBodyParser {

	/**
	 * 
	 * @param map
	 * @return
	 */
	public String map2Json(Map<String, String> map) {
		String json = "";
		try {

			ObjectMapper mapper = new ObjectMapper();

			// convert map to JSON string
			json = mapper.writeValueAsString(map);

//			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
//
//			// pretty print
//			System.out.println(json);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> json2Map(String json) {
		Map<String, String> map = new HashMap<String, String>();
		
		try {

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

			// convert JSON string to Map
			map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return map;
	}
}
