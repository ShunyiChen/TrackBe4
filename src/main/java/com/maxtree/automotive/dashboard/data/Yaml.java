package com.maxtree.automotive.dashboard.data;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class Yaml {

	/**
	 * 
	 * @return
	 */
	public static Address readAddress() {
		Address area = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
        	area = mapper.readValue(new File("configuration/Address.yaml"), Address.class);
//            System.out.println(ReflectionToStringBuilder.toString(user,ToStringStyle.MULTI_LINE_STYLE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return area;
    }
	
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
	
	/**
	 * 
	 * @return
	 */
	public static Comment readComments() {
		Comment comment = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
        	comment = mapper.readValue(new File("configuration/Comments.yaml"), Comment.class);
//            System.out.println(ReflectionToStringBuilder.toString(user,ToStringStyle.MULTI_LINE_STYLE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comment;
    }
}
