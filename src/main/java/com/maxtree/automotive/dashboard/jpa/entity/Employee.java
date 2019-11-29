package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employees")
public class Employee {
    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 登录用户名 */
    private String userName;
    /** 登录密码 */
    private String password;
    /** 账号有效期 */
    private Date expiryDate;
    /** 员工名子 */
    private String firstName;
    /** 员工姓 */
    private String lastName;
    /** 职称 */
    private String title;
    /** 性别 */
    private String gender;
    /** 部门 */
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    /** 员工个人配置 */
    @OneToOne
    private EmployeeProfile profile;
    /** 员工权限关系映射 */
    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    protected Set<EmployeeRoleMapping> employeeRoleMappings = new HashSet<>();

    public Employee() {}

    public Employee(String userName, String password, Date expiryDate, String firstName, String lastName, String title, String gender, Department department, EmployeeProfile profile, Set<EmployeeRoleMapping> employeeRoleMappings) {
        this.userName = userName;
        this.password = password;
        this.expiryDate = expiryDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.gender = gender;
        this.department = department;
        this.profile = profile;
        this.employeeRoleMappings = employeeRoleMappings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public EmployeeProfile getProfile() {
        return profile;
    }

    public void setProfile(EmployeeProfile profile) {
        this.profile = profile;
    }

    public Set<EmployeeRoleMapping> getEmployeeRoleMappings() {
        return employeeRoleMappings;
    }

    public void setEmployeeRoleMappings(Set<EmployeeRoleMapping> employeeRoleMappings) {
        this.employeeRoleMappings = employeeRoleMappings;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", expiryDate=" + expiryDate +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", title='" + title + '\'' +
                ", gender='" + gender + '\'' +
                ", department=" + department +
                ", profile=" + profile +
                ", employeeRoleMappings=" + employeeRoleMappings +
                '}';
    }
}
