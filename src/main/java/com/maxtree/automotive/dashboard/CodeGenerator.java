package com.maxtree.automotive.dashboard;

import java.util.Random;

import com.vaadin.ui.UI;

/**
 * 
 * @author chens
 *
 */
public class CodeGenerator {

	/**
	 * 
	 * @param num
	 * @return
	 */
	public String generateCode(int num) {
		String code = num + "";
		if (code.toCharArray().length == 1) {
			code = "00"+num;
		} else if (code.toCharArray().length == 2) {
			code = "0"+num;
		}
		return code;
	}
	
	
	 public static void main( String[] args )
	    {
		 CodeGenerator cg = new CodeGenerator();
//		 for (int i =0;i < 20;i++) {
//			 System.out.println(cg.generate4BitCode());
//		 }
		 
	    }
	
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
