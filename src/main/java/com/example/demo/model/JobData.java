package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "job_data") // DBのテーブル名に合わせる
public class JobData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id") // DBカラム名
    private Integer jobId;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "organization_key")
    private Integer organizationKey;
    
    @Transient // DBカラムとしては持たない
    private int userCount;

    // ---------- getter/setter ----------

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getOrganizationKey() {
        return organizationKey;
    }

    public void setOrganizationKey(Integer organizationKey) {
        this.organizationKey = organizationKey;
    }
    
    public int getUserCount() { return userCount; }
    public void setUserCount(int userCount) { this.userCount = userCount; }
}
