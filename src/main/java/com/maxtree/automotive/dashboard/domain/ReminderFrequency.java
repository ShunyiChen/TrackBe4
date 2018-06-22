package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

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

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
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

	public Date getNextReminderTime() {
		return nextReminderTime;
	}

	public void setNextReminderTime(Date nextReminderTime) {
		this.nextReminderTime = nextReminderTime;
	}

	@Override
	public String toString() {
		return name;
	}

	private Integer frequencyUniqueId = 0;
	private String name; // 计划名称
	private Integer frequency = 0; // 1-每天 7-每周
	private Integer active = 0; // 0未激活，1已激活
	private Date startingTime;// 其实时间
	private Date endingTime;// 结束时间
	private Date nextReminderTime; // 下次提醒时间

}
