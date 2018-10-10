package com.maxtree.automotive.dashboard.domain;

import java.util.List;

import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.cache.DataObject;

/**
 * 用户
 * 
 * @author chens
 *
 */
public class User {
	
	public Integer getUserUniqueId() {
		return userUniqueId;
	}

	public void setUserUniqueId(Integer userUniqueId) {
		this.userUniqueId = userUniqueId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHashed() {
		return hashed;
	}

	public void setHashed(String hashed) {
		this.hashed = hashed;
	}

	public Integer getActivated() {
		return activated;
	}

	public void setActivated(Integer activated) {
		this.activated = activated;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getCompanyUniqueId() {
		return companyUniqueId;
	}

	public void setCompanyUniqueId(Integer companyUniqueId) {
		this.companyUniqueId = companyUniqueId;
	}

	public Integer getCommunityUniqueId() {
		return communityUniqueId;
	}

	public void setCommunityUniqueId(Integer communityUniqueId) {
		this.communityUniqueId = communityUniqueId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	@Override
	public String toString() {
		// return String.format(
		// "Usr[userUniqueId=%d, userName='%s',firstName='%s', lastName='%s',
		// passwordSalt='%s', passwordHash='%s']",
		// userUniqueId, userName, firstName, lastName, passwordSalt, passwordHash);
		return userName;
	}

	public boolean isPermitted(PermissionCodes code) {
		DataObject dataObj = CacheManager.getInstance().getPermissionCache().get(userUniqueId);
		return dataObj.isPermitted(code.code) || "root".equals(userName);
	}
	
	private Integer userUniqueId = 0;	// 用户ID
	private String userName; 			// 用户名
	private String hashed; 				// 密码哈希值
	private Integer activated = 1;		// 1-激活的，0-未激活
	private String companyName; 		// 机构名
	private Integer companyUniqueId = 0;// 机构ID
	private Integer communityUniqueId = 0;// 社区ID
	private String email;				// 电子邮箱地址
	private String phone;				// 电话号
	private String fax;					// 传真
	private List<Role> roles; 			// 角色列表
	private List<Site> sites; 			// 站点列表
	private UserProfile profile;        // 用户个人配置
}
