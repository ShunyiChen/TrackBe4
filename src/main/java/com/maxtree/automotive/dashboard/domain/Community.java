package com.maxtree.automotive.dashboard.domain;

import java.util.List;

/**
 * Community is the group of companies and locations. It is owned by a company
 * called Originator Company and other companies join a community called
 * responder companies.
 * 
 * 社区
 * 
 * @author chens
 *
 */
public class Community {

	public Integer getCommunityUniqueId() {
		return communityUniqueId;
	}
	public void setCommunityUniqueId(Integer communityUniqueId) {
		this.communityUniqueId = communityUniqueId;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getCommunityDescription() {
		return communityDescription;
	}
	public void setCommunityDescription(String communityDescription) {
		this.communityDescription = communityDescription;
	}
	public List<Company> getCompanies() {
		return companies;
	}
	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}
	public List<Community> getCommunities() {
		return communities;
	}
	public void setCommunities(List<Community> communities) {
		this.communities = communities;
	}
	@Override
	public String toString() {
		return communityName;
	}
	
	private Integer communityUniqueId = 0; // 社区ID
	private String communityName;	//社区名称
	private String communityDescription;//社区描述
	private List<Company> companies;//已分配的机构
	private List<Community> communities;//下属社区（非数据库字段）
}
