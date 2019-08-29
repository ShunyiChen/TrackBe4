package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Logging {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Date dateCreated;
    private String userName;
    private String ipAddress;
    private String actionName;// 登录/退出/修改密码/创建用户/提交录入数据/退回提交
    private String details;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor
     */
    public Logging() {}

    /**
     * Constructor
     *
     * @param dateCreated
     * @param userName
     * @param ipAddress
     * @param actionName
     * @param details
     */
    public Logging(Date dateCreated, String userName, String ipAddress, String actionName, String details) {
        this.dateCreated = dateCreated;
        this.userName = userName;
        this.ipAddress = ipAddress;
        this.actionName = actionName;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getFormattedDatetime() {
        return formatter.format(dateCreated);
    }

    @Override
    public String toString() {
        return String.format(
                "Logging[id=%d, dateCreated='%s', userName='%s', ipAddress='%s', actionName='%s', details='%s']",
                id, dateCreated, userName, ipAddress, actionName, details);
    }
}
