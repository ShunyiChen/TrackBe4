package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

/**
 * 提醒频率
 * 
 * @author chens
 *
 */
public class ReminderFrequency {

	public Integer getFrequencyUniqueId() {
		return frequencyUniqueId;
	}

	public void setFrequencyUniqueId(Integer frequencyUniqueId) {
		this.frequencyUniqueId = frequencyUniqueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Date getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(Date startingTime) {
		this.startingTime = startingTime;
	}

	public Date getEndingTime() {
		return endingTime;
	}

	public void setEndingTime(Date endingTime) {
		this.endingTime = endingTime;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return name;
	}

	private Integer frequencyUniqueId = 0;
	private String name; // 计划名称
	private Integer frequency = 0; // 1-每天 7-每周
	private Integer enabled = 1; // 1-启用 0-禁用
	private Date startingTime;// 其实时间
	private Date endingTime;// 结束时间

}
