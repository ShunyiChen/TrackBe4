package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "quality")
public class Quality {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 流程实例ID */
    private String workId;

    protected Quality() {}

    public Quality(String workId) {
        this.workId = workId;
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

    @Override
    public String toString() {
        return "Quality{" +
                "id=" + id +
                ", workId='" + workId + '\'' +
                '}';
    }
}
