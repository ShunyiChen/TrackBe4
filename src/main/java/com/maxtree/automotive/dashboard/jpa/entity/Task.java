package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tasks")
public class Task {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 任务标题 */
    private String title;
    /** 创建日期 */
    private Date dateCreated;
    /** 任务图标 */
    private String iconUrl;
    /** 员工 */
    @ManyToOne(fetch=FetchType.LAZY)
    private Employee employee;

    public Task() {}

    public Task(String title, Date dateCreated, String iconUrl, Employee employee) {
        this.title = title;
        this.dateCreated = dateCreated;
        this.iconUrl = iconUrl;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dateCreated=" + dateCreated +
                ", iconUrl='" + iconUrl + '\'' +
                ", employee=" + employee +
                '}';
    }
}
