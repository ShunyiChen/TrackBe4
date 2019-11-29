package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifications")
public class Notification {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 通知标题 */
    private String title;
    /** 通知内容 */
    private String content;
    /** 通知时间 */
    private Date noticeTime;
    /** 通知图标 */
    private String iconUrl;
    /** 点击事件 */
    private String clickEvent;

    public Notification() {
    }

    public Notification(String title, String content, Date noticeTime, String iconUrl, String clickEvent) {
        this.title = title;
        this.content = content;
        this.noticeTime = noticeTime;
        this.iconUrl = iconUrl;
        this.clickEvent = clickEvent;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(String clickEvent) {
        this.clickEvent = clickEvent;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", noticeTime=" + noticeTime +
                ", iconUrl='" + iconUrl + '\'' +
                ", clickEvent='" + clickEvent + '\'' +
                '}';
    }
}
