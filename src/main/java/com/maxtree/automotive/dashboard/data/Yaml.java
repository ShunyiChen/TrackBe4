package com.maxtree.automotive.dashboard.data;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class Yaml {

	public static Area readArea() {
		Area area = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
        	area = mapper.readValue(new File("configuration/Area.yaml"), Area.class);
//            System.out.println(ReflectionToStringBuilder.toString(user,ToStringStyle.MULTI_LINE_STYLE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return area;
    }
	
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
	
	
	public static void main(String[] args) {
		SystemConfiguration sd = readSystemConfiguration();
		System.out.println(sd.getDateformat());
	}
}
