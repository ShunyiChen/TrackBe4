package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "departments")
public class Department {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 部门名称 */
    private String name;
    /** 部门类别 */
    private String type;
    /** 单位 */
    @ManyToOne(fetch=FetchType.LAZY)
    private Company company;

    public Department() {
    }

    public Department(String name, String type, Company company) {
        this.name = name;
        this.type = type;
        this.company = company;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", company=" + company +
                '}';
    }
}
