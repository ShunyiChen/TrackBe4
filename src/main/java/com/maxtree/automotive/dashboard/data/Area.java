package com.maxtree.automotive.dashboard.data;

public class Area {

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

	public String[] getPrefecture() {
		return prefecture;
	}

	public void setPrefecture(String[] prefecture) {
		this.prefecture = prefecture;
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

	private String[] province;
	private String[] city;
	private String[] prefecture;
	private String[] district;
	private String licenseplate;
}
