package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_info") // 実際のDBテーブル名
public class UserInfo {

    // ---------- 基本フィールド ----------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_key")
    private Integer userKey;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "admin_flag")
    private Boolean adminFlag;

    @Column(name = "organization_key")
    private Integer organizationKey;

    @Column(name = "password", nullable = false)
    private String password;

    // ---------- リレーション ----------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_key", insertable = false, updatable = false)
    private OrganizationData organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private DepartmentData department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", insertable = false, updatable = false)
    private JobData job;

    // ---------- 基本フィールドのGetter/Setter ----------
    public Integer getUserKey() {
        return userKey;
    }

    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Boolean getAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(Boolean adminFlag) {
        this.adminFlag = adminFlag;
    }

    public Integer getOrganizationKey() {
        return organizationKey;
    }

    public void setOrganizationKey(Integer organizationKey) {
        this.organizationKey = organizationKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ---------- リレーションのGetter/Setter ----------
    public OrganizationData getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationData organization) {
        this.organization = organization;
    }

    public DepartmentData getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentData department) {
        this.department = department;
    }

    public JobData getJob() {
        return job;
    }

    public void setJob(JobData job) {
        this.job = job;
    }
}
