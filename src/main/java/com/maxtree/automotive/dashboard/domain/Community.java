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

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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
//		return String.format(
//				"Community[communityUniqueId=%d,  communityName='%s', communityDescription='%s',groupId=%d, level=%d]",
//				communityUniqueId, communityName, communityDescription, groupId, level);
		return communityName;
	}

	
	private Integer communityUniqueId = 0; // 社区ID
	private String communityName;	//社区名称
	private String communityDescription;//社区描述
	private Integer groupId = 0;//组ID
	private Integer level = 0;//级别
	private List<Company> companies;//已分配的机构
	private List<Community> communities;//下属社区（非数据库字段）
}
