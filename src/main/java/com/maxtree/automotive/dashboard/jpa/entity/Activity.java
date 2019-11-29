package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "activities")
public class Activity {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 流程实例ID */
    private String workId;
    /** 审核人 */
    private String inspector;
    /** 审核日期 */
    private Date inspectDate = new Date();
    /** 是否审批通过 */
    private Boolean approved;
    /** 批改建议 */
    @OneToMany(cascade= {CascadeType.ALL},fetch=FetchType.LAZY)
    private List<Suggestion> suggestions = new ArrayList<>();

    protected Activity() {}

    public Activity(String workId, String inspector, Date inspectDate, Boolean approved, List<Suggestion> suggestions) {
        this.workId = workId;
        this.inspector = inspector;
        this.inspectDate = inspectDate;
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

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public Date getInspectDate() {
        return inspectDate;
    }

    public void setInspectDate(Date inspectDate) {
        this.inspectDate = inspectDate;
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
        return "Activity{" +
                "id=" + id +
                ", workId='" + workId + '\'' +
                ", inspector='" + inspector + '\'' +
                ", inspectDate=" + inspectDate +
                ", approved=" + approved +
                ", suggestions=" + suggestions +
                '}';
    }
}
