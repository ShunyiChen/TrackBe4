package com.maxtree.automotive.dashboard.data;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.maxtree.automotive.dashboard.UploadParameters;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.server.VaadinSession;

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
	
	/**
	 * 更新用户操作行为
	 * 
	 * @param params
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void updateUploadParameters(UploadParameters params) throws JsonGenerationException, JsonMappingException, IOException {
		File dumpFile = new File("devices/"+params.getUserUniqueId()+"/parameters.yaml");
		if (!dumpFile.exists()) {
			dumpFile.getParentFile().mkdirs();
		}
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.writeValue(dumpFile, params);
	}
	
	/**
	 * 读取用户操作行为
	 * 
	 * @param userUniqueId
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static UploadParameters readUploadParameters(int userUniqueId) throws JsonGenerationException, JsonMappingException, IOException {
		UploadParameters ub = null;
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		try {
			ub = mapper.readValue(new File("devices/"+userUniqueId+"/parameters.yaml"), UploadParameters.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return ub;
	}
	
	/**
	 * 删除行为文件
	 * 
	 * @param userUniqueId
	 * @throws IOException
	 */
	public static void deleteUploadParameters(int userUniqueId) throws IOException {
		FileUtils.deleteQuietly(new File("devices/parameters/"+userUniqueId+".yaml"));
	}
	
//	public static void main(String[] args) {
//		SystemConfiguration sd = readSystemConfiguration();
//		System.out.println(sd.getDateformat());
//	}
}
