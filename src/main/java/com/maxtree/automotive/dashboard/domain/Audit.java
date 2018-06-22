package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

public class Audit {

	public Integer getApprovalUniqueId() {
		return approvalUniqueId;
	}

	public void setApprovalUniqueId(Integer approvalUniqueId) {
		this.approvalUniqueId = approvalUniqueId;
	}

	public Integer getTransactionUniqueId() {
		return transactionUniqueId;
	}

	public void setTransactionUniqueId(Integer transactionUniqueId) {
		this.transactionUniqueId = transactionUniqueId;
	}

	public Integer getAuditor() {
		return auditor;
	}

	public void setAuditor(Integer auditor) {
		this.auditor = auditor;
	}

	public String getAuditResults() {
		return auditResults;
	}

	public void setAuditResults(String auditResults) {
		this.auditResults = auditResults;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	
	public String getAuditorFirstName() {
		return auditorFirstName;
	}

	public void setAuditorFirstName(String auditorFirstName) {
		this.auditorFirstName = auditorFirstName;
	}

	public String getAuditorLastName() {
		return auditorLastName;
	}

	public void setAuditorLastName(String auditorLastName) {
		this.auditorLastName = auditorLastName;
	}

	private Integer approvalUniqueId = 0;   //审批ID
	private Integer transactionUniqueId = 0;//事务ID
	private Integer auditor = 0;			//审核人
	private String auditResults;			//审核结果
	private Date auditDate; 				//审核时间
	private String auditorFirstName;	 	// 审核人名
	private String auditorLastName;	 		// 审核人姓
}
