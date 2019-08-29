package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "workflow")
public class Work {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 流程实例ID */
    private String workId;
    /** 审核人 */
    private String auditor;
    /** 审核日期 */
    private Date dateOfAudit = new Date();
    /** 是否审批通过 */
    private Boolean approved;
    /** 批改建议 */
    @OneToMany(cascade= {CascadeType.ALL},fetch=FetchType.LAZY)
    private List<Suggestion> suggestions = new ArrayList<>();

    protected Work() {}

    public Work(String workId, String auditor, Date dateOfAudit, Boolean approved, List<Suggestion> suggestions) {
        this.workId = workId;
        this.auditor = auditor;
        this.dateOfAudit = dateOfAudit;
        this.approved = approved;
        this.suggestions = suggestions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public Date getDateOfAudit() {
        return dateOfAudit;
    }

    public void setDateOfAudit(Date dateOfAudit) {
        this.dateOfAudit = dateOfAudit;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public String toString() {
        return "Work{" +
                "id=" + id +
                ", workId='" + workId + '\'' +
                ", auditor='" + auditor + '\'' +
                ", dateOfAudit=" + dateOfAudit +
                ", approved=" + approved +
                ", suggestions=" + suggestions +
                '}';
    }
}
