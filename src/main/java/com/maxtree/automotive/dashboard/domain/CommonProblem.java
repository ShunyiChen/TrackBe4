package com.maxtree.automotive.dashboard.domain;

public class CommonProblem {

	public Integer getProblemUniqueId() {
		return problemUniqueId;
	}

	public void setProblemUniqueId(Integer problemUniqueId) {
		this.problemUniqueId = problemUniqueId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	@Override
	public String toString() {
		return String.format("CommonProblem[problemUniqueId=%d, userName='%s', problem='%s'， frequency=%d]", problemUniqueId,
				userName, problem);
	}

	private Integer problemUniqueId = 0; // UniqueID
	private String userName;//用户名
	private String problem;//常见问题
	private Integer frequency = 0;//使用频率
}
