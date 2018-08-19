package com.maxtree.automotive.dashboard;

import java.util.HashMap;
import java.util.Map;

public class BusinessCode {

	private static Map<String, String> BUSINESS_CODES = new HashMap<String, String>();
	
	static {
		BUSINESS_CODES.put("0001", "注册登记");
		BUSINESS_CODES.put("0003", "转移登记");
		BUSINESS_CODES.put("0004", "抵押登记");
		BUSINESS_CODES.put("0005", "解除抵押登记");
		BUSINESS_CODES.put("0006", "质押备案");
		BUSINESS_CODES.put("0007", "解除质押备案");
		BUSINESS_CODES.put("0008", "补领登记证书");
		BUSINESS_CODES.put("0009", "换领登记证书");
		BUSINESS_CODES.put("0010", "申领登记证书");
		BUSINESS_CODES.put("0011", "登记事项更正");
		BUSINESS_CODES.put("0012", "补建机动车档案");
		BUSINESS_CODES.put("0013", "转入机动车");
		BUSINESS_CODES.put("0014", "影像化档案信息更正");
		BUSINESS_CODES.put("0021", "变更机动车所有人姓名/名称");
		BUSINESS_CODES.put("0022", "变更共同所有人姓名/名称");
		BUSINESS_CODES.put("0023", "变更使用性质");
		BUSINESS_CODES.put("0024", "更换发动机");
		BUSINESS_CODES.put("0025", "更换车身/车架");
		BUSINESS_CODES.put("0026", "变更车身颜色");
		BUSINESS_CODES.put("0027", "更换整车");
		BUSINESS_CODES.put("0028", "住所迁出车管所管辖区域");
		BUSINESS_CODES.put("0029", "重新打刻发动机号码");
		BUSINESS_CODES.put("0031", "转出/注销恢复");
		BUSINESS_CODES.put("0210", "重新打刻车辆识别");
		BUSINESS_CODES.put("0211", "变更身份证明名称/号码");
		BUSINESS_CODES.put("0214", "加装残疾人操纵辅助装备");
		BUSINESS_CODES.put("0215", "拆除残疾人操纵辅助装置");
		BUSINESS_CODES.put("0216", "变更登记");
	}
	
	
	public static String get(String businessCode) {
		return BUSINESS_CODES.get(businessCode);
	}
}
