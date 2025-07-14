package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "department_data") // DBテーブル名に合わせる
public class DepartmentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id") // DBカラム名に合わせる
    private Integer departmentId;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "organization_key")
    private Integer organizationKey;
    
    @Transient // DBカラムとしては持たない
    private int userCount;

    //----------getter/setter----------

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getOrganizationKey() {
        return organizationKey;
    }

    public void setOrganizationKey(Integer organizationKey) {
        this.organizationKey = organizationKey;
    }
    
    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}
