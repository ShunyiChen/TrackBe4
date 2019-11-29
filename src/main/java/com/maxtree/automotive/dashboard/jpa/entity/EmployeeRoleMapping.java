package com.maxtree.automotive.dashboard.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="employee_role_mapping")
@org.hibernate.annotations.Immutable
public class EmployeeRoleMapping {
    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "employee_id")
        protected Long employeeId;

        @Column(name = "role_id")
        protected Long roleId;

        public Id() {
        }

        public Id(Long employeeId, Long roleId) {
            this.employeeId = employeeId;
            this.roleId = roleId;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }

        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }
    }

    @EmbeddedId
    protected Id id = new Id();

    @ManyToOne
    @JoinColumn(
            name = "employee_id",
            insertable = false, updatable = false
    )
    @JsonIgnore
    protected Employee employee;

    @ManyToOne
    @JoinColumn(
            name = "role_id",
            insertable = false, updatable = false
    )
    @JsonIgnore
    protected Role role;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "EmployeeRoleMapping{" +
                "id=" + id +
                ", employee=" + employee +
                ", role=" + role +
                '}';
    }
}
