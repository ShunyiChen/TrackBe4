package com.maxtree.automotive.dashboard.domain;

import java.util.List;

/**
 * Community is the group of companies and locations. It is owned by a company
 * called Originator Company and other companies join a community called
 * responder companies.
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
	@Override
	public String toString() {
//		return String.format(
//				"Community[communityUniqueId=%d,  communityName='%s', communityDescription='%s',groupId=%d, level=%d]",
//				communityUniqueId, communityName, communityDescription, groupId, level);
		return communityName;
	}

	
	private Integer communityUniqueId = 0;
	private String communityName;
	private String communityDescription;
	private Integer groupId = 0;
	private Integer level = 0;
	private List<Company> companies;
}
