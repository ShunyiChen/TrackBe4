package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 角色名称 */
    private String name;
    /** 角色组 */
    @ManyToOne
    @JoinColumn(name = "group_id",
            foreignKey = @ForeignKey(name = "group_id_fk")
    )
    private RoleGroup group;
    /** 员工角色关系映射 */
    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    protected Set<EmployeeRoleMapping> employeeRoleMappings = new HashSet<>();
    /** 角色权限关系映射 */
    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    protected Set<RolePermissionMapping> rolePermissionMappings = new HashSet<>();



    public Role() {}

    public Role(String name, Set<EmployeeRoleMapping> employeeRoleMappings, Set<RolePermissionMapping> rolePermissionMappings, RoleGroup group) {
        this.name = name;
        this.employeeRoleMappings = employeeRoleMappings;
        this.rolePermissionMappings = rolePermissionMappings;
        this.group = group;
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

    public Set<EmployeeRoleMapping> getEmployeeRoleMappings() {
        return employeeRoleMappings;
    }

    public void setEmployeeRoleMappings(Set<EmployeeRoleMapping> employeeRoleMappings) {
        this.employeeRoleMappings = employeeRoleMappings;
    }

    public Set<RolePermissionMapping> getRolePermissionMappings() {
        return rolePermissionMappings;
    }

    public void setRolePermissionMappings(Set<RolePermissionMapping> rolePermissionMappings) {
        this.rolePermissionMappings = rolePermissionMappings;
    }

    public RoleGroup getGroup() {
        return group;
    }

    public void setGroup(RoleGroup group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employeeRoleMappings=" + employeeRoleMappings +
                ", rolePermissionMappings=" + rolePermissionMappings +
                ", group=" + group +
                '}';
    }
}
