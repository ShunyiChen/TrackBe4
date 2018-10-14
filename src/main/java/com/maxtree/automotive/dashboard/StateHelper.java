package com.maxtree.automotive.dashboard;

import java.util.List;

import com.maxtree.automotive.dashboard.domain.BusinessState;
import com.vaadin.ui.UI;

/**
 * 业务状态
 * 
 * @author Chen
 *
 */
public class StateHelper {
	
	public StateHelper() {
		list = ui.businessStateService.findAll();
	}
	
	/**
	 * 根据业务状态CODE取NAME
	 * 
	 * @param code
	 */
	public String getName(String code) {
		for(BusinessState state : list) {
			if(state.getCode().equals(code)) {
				return state.getName();
			}
		}
		return "";
	}
	
	private List<BusinessState> list;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
