package com.maxtree.automotive.dashboard.data;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * 
 * @author Chen
 *
 */
public class Yaml {

	/**
	 * 
	 * @return
	 */
	public static SystemConfiguration readSystemConfiguration() {
		SystemConfiguration sc = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
        	sc = mapper.readValue(new File("configuration/SystemConfiguration.yaml"), SystemConfiguration.class);
//            System.out.println(ReflectionToStringBuilder.toString(user,ToStringStyle.MULTI_LINE_STYLE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sc;
    }
	
 
}
