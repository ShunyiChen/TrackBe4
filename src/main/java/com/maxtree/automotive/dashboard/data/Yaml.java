package com.maxtree.automotive.dashboard.data;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class Yaml {

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
	 * 获取影像化部门管理员
	 * 
	 * @return
	 */
	public static Administrator readAdministrator() {
		Administrator admin = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
        	admin = mapper.readValue(new File("configuration/Administrator.yaml"), Administrator.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
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
}
