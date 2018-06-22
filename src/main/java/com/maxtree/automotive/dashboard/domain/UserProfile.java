package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

/**
 * 用户个人设置信息
 * 
 * @author chens
 *
 */
public class UserProfile {

	public Integer getUserProfileUniqueId() {
		return userProfileUniqueId;
	}

	public void setUserProfileUniqueId(Integer userProfileUniqueId) {
		this.userProfileUniqueId = userProfileUniqueId;
	}

	public Integer getUserUniqueId() {
		return userUniqueId;
	}

	public void setUserUniqueId(Integer userUniqueId) {
		this.userUniqueId = userUniqueId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getManagerUniqueId() {
		return managerUniqueId;
	}

	public void setManagerUniqueId(Integer managerUniqueId) {
		this.managerUniqueId = managerUniqueId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Integer getCreatorUniqueId() {
		return creatorUniqueId;
	}

	public void setCreatorUniqueId(Integer creatorUniqueId) {
		this.creatorUniqueId = creatorUniqueId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	public Date getLastLogon() {
		return lastLogon;
	}

	public void setLastLogon(Date lastLogon) {
		this.lastLogon = lastLogon;
	}
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return String.format(
				"UserProfile[userProfileUniqueId=%d, userUniqueId=%d, title='%s',picture='%s', managerUniqueId=%d, createdBy='%s',"+
				" sex='%s',email='%s', location='%s', phone='%s',firstName='%s',lastName='%s'",
				userProfileUniqueId, userUniqueId, title, picture, managerUniqueId, createdBy, sex, email, location, phone,firstName,lastName);
	}

	private Integer userProfileUniqueId = 0; 	// ID
	private Integer userUniqueId = 0; 			// 用户ID
	private String title; 						// 职称
	private String picture; 					// 头像路径
	private Integer managerUniqueId = 0; 		// 经理ID
	private String createdBy; 					// 管理员用户名
	private Integer creatorUniqueId = 0;    	// 管理员ID
	private Date dateCreated; 					// 创建日期
	private Date dateLastModified; 				// 最后修改日期
	private Date lastLogon; 					// 最后登录时间
	private String sex = "";		// 性别    (因为TextField组件不能设置null值)
	private String email = "";   // 邮箱
	private String location = "";// 办公地址
	private String phone = "";   // 电话
	private String firstName = "";    // 名
	private String lastName = ""; 	 // 姓
}
