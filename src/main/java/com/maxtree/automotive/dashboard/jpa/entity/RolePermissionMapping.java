package com.maxtree.automotive.dashboard.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="role_permission_mapping")
@org.hibernate.annotations.Immutable
public class RolePermissionMapping {

    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "role_id")
        protected Long roleId;

        @Column(name = "permission_id")
        protected Long permissionId;

        public Id() {
        }

        public Id(Long roleId, Long permissionId) {
            this.roleId = roleId;
            this.permissionId = permissionId;
        }

        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }

        public Long getPermissionId() {
            return permissionId;
        }

        public void setPermissionId(Long permissionId) {
            this.permissionId = permissionId;
        }
    }

    @EmbeddedId
    protected EmployeeRoleMapping.Id id = new EmployeeRoleMapping.Id();

    @ManyToOne
    @JoinColumn(
            name = "role_id",
            insertable = false, updatable = false
    )
    @JsonIgnore
    protected Role role;

    @ManyToOne
    @JoinColumn(
            name = "permission_id",
            insertable = false, updatable = false
    )
    @JsonIgnore
    protected Permission permission;

    public EmployeeRoleMapping.Id getId() {
        return id;
    }

    public void setId(EmployeeRoleMapping.Id id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "RolePermissionMapping{" +
                "id=" + id +
                ", role=" + role +
                ", permission=" + permission +
                '}';
    }
}
