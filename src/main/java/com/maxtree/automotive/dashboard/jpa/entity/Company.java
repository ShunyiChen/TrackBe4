package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "companies")
public class Company {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 单位名称 */
    private String name;
    /** 单位类别 */
    private String type;
    /** 社区 */
    @ManyToOne(fetch=FetchType.LAZY)
    private Community community;

    public Company() {}

    public Company(String name, String type, Community community) {
        this.name = name;
        this.type = type;
        this.community = community;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", community=" + community +
                '}';
    }
}
