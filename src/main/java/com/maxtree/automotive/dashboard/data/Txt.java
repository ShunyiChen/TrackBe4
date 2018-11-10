package com.maxtree.automotive.dashboard.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

/**
 * 
 * @author Chen
 *
 */
public class Txt {

	/**
	 * 
	 * @param fileFullPath
	 * @return
	 * @throws IOException
	 */
	public static String read(String fileFullPath) throws IOException {
//		try(FileInputStream inputStream = new FileInputStream(fileFullPath)) {     
//		    return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//		}
		return new String(Files.readAllBytes(Paths.get(fileFullPath)));
	}
}
