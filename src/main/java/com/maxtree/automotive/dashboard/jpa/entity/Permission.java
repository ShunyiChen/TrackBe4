package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
public class Permission {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 权限名 */
    private String name;
    /** 权限组 */
    @ManyToOne
    @JoinColumn(name = "group_id")
    private PermissionGroup group;
    /** 角色权限关系映射 */
    @OneToMany(mappedBy = "permission", fetch = FetchType.EAGER)
    protected Set<RolePermissionMapping> rolePermissionMappings = new HashSet<>();

    public Permission() {}

    public Permission(String name, PermissionGroup group, Set<RolePermissionMapping> rolePermissionMappings) {
        this.name = name;
        this.group = group;
        this.rolePermissionMappings = rolePermissionMappings;
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

    public PermissionGroup getGroup() {
        return group;
    }

    public void setGroup(PermissionGroup group) {
        this.group = group;
    }

    public Set<RolePermissionMapping> getRolePermissionMappings() {
        return rolePermissionMappings;
    }

    public void setRolePermissionMappings(Set<RolePermissionMapping> rolePermissionMappings) {
        this.rolePermissionMappings = rolePermissionMappings;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", group=" + group +
                ", rolePermissionMappings=" + rolePermissionMappings +
                '}';
    }
}
