package com.maxtree.automotive.dashboard.view.admin.storehouse;

import com.maxtree.automotive.dashboard.DashboardUI;
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
	 */
	public String generatePortfolioCode() {
		
		return "";
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
