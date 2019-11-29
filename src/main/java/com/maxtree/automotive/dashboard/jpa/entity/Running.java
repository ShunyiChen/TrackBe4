package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "running")
public class Running {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 处理（open,close,execute..） */
    private String handling;
    /** 执行目标（通知ID,事务ID） */
    private String target;
    /** 完成时间 */
    private Date dataFinished;

    public Running() {}

    public Running(String handling, String target, Date dataFinished) {
        this.handling = handling;
        this.target = target;
        this.dataFinished = dataFinished;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHandling() {
        return handling;
    }

    public void setHandling(String handling) {
        this.handling = handling;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Date getDataFinished() {
        return dataFinished;
    }

    public void setDataFinished(Date dataFinished) {
        this.dataFinished = dataFinished;
    }

    @Override
    public String toString() {
        return "Running{" +
                "id=" + id +
                ", handling='" + handling + '\'' +
                ", target='" + target + '\'' +
                ", dataFinished=" + dataFinished +
                '}';
    }
}
