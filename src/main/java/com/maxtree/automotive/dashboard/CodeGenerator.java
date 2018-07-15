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
	 * @return
	 */
	public String generateStorehouseCode() {
		int count = Integer.parseInt(ui.storehouseService.getMaxCodeOfStorehouse());
		
		count++;
		
		String code = count + "";
		if (code.toCharArray().length == 1) {
			code = "00"+count;
		} else if (code.toCharArray().length == 2) {
			code = "0"+count;
		}
		return code;
	}
	
	/**
	 * 
	 * @param storehouseUniqueId
	 * @return
	 */
	public String generateDenseframeCode(int storehouseUniqueId) {
		String densehouseCode = ui.storehouseService.getMaxCodeOfDenseFrame(storehouseUniqueId);
		
		densehouseCode = densehouseCode.substring(densehouseCode.indexOf("-")+1, densehouseCode.length());
		
		int count = Integer.parseInt(densehouseCode);
		
		count++;
		
		String code = count + "";
		if (code.toCharArray().length == 1) {
			code = "00"+count;
		} else if (code.toCharArray().length == 2) {
			code = "0"+count;
		}
		return code;
	}
	
	public String generateCode(int num) {
		String code = num + "";
		if (code.toCharArray().length == 1) {
			code = "00"+num;
		} else if (code.toCharArray().length == 2) {
			code = "0"+num;
		}
		return code;
	}
	
	/**
	 * 
	 * @return
//	 */
//	public static String generate4BitCode() {
//		Random random = new Random();
//		String[] codes = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","0","1","2","3","4","5","6","7","8","9"};
//		StringBuilder generated = new StringBuilder(4);
//		for(int i = 0; i < 4; i++) {
//			int randomNum = random.nextInt(36);
//			generated.append(codes[randomNum]);
//		}
//		return generated.toString();
//	}
	
	 public static void main( String[] args )
	    {
		 CodeGenerator cg = new CodeGenerator();
//		 for (int i =0;i < 20;i++) {
//			 System.out.println(cg.generate4BitCode());
//		 }
		 
	    }
	
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
