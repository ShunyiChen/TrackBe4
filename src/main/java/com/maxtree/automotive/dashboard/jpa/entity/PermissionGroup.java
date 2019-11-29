package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "permission_groups")
public class PermissionGroup {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 权限组名 */
    private String name;

    public PermissionGroup() {}

    public PermissionGroup(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "PermissionGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
