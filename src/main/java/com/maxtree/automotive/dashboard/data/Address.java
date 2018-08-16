package com.maxtree.automotive.dashboard.data;

public class Address {

	public String[] getProvince() {
		return province;
	}

	public void setProvince(String[] province) {
		this.province = province;
	}

	public String[] getCity() {
		return city;
	}

	public void setCity(String[] city) {
		this.city = city;
	}

	public String[] getDistrict() {
		return district;
	}

	public void setDistrict(String[] district) {
		this.district = district;
	}

	public String getLicenseplate() {
		return licenseplate;
	}

	public void setLicenseplate(String licenseplate) {
		this.licenseplate = licenseplate;
	}

	private String[] province;  //省份
	private String[] city;		//地级市
	private String[] district;	//市、县级市
	private String licenseplate;//辽B
	
}
