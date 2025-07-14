package com.example.demo.model;

import java.sql.Timestamp;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "writing_data") // DBテーブル名に合わせる
public class WritingData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "writing_id")
    private Integer writingId;

    @Column(name = "user_key")
    private Integer userKey;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "organization_key")
    private Integer organizationKey;

    @Column(name = "user_name_stamp")
    private String userNameStamp;

    @Column(name = "job_name_stamp")
    private String jobNameStamp;

    @Column(name = "department_name_stamp")
    private String departmentNameStamp;

    @Column(name = "pin")
    private String pin;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "pdf_movie")
    @Basic(fetch = FetchType.LAZY) // バイナリとして扱う
    private byte[] pdfMovie;

    @Column(name = "writing_time", updatable = false, insertable = false)
    private Timestamp writingTime;
    
    // ---------- getter/setter ----------

    public Integer getWritingId() {
        return writingId;
    }

    public void setWritingId(Integer writingId) {
        this.writingId = writingId;
    }

    public Integer getUserKey() {
        return userKey;
    }

    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
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

    public Integer getOrganizationKey() {
        return organizationKey;
    }

    public void setOrganizationKey(Integer organizationKey) {
        this.organizationKey = organizationKey;
    }

    public String getUserNameStamp() {
        return userNameStamp;
    }

    public void setUserNameStamp(String userNameStamp) {
        this.userNameStamp = userNameStamp;
    }

    public String getJobNameStamp() {
        return jobNameStamp;
    }

    public void setJobNameStamp(String jobNameStamp) {
        this.jobNameStamp = jobNameStamp;
    }

    public String getDepartmentNameStamp() {
        return departmentNameStamp;
    }

    public void setDepartmentNameStamp(String departmentNameStamp) {
        this.departmentNameStamp = departmentNameStamp;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getPdfMovie() {
        return pdfMovie;
    }

    public void setPdfMovie(byte[] pdfMovie) {
        this.pdfMovie = pdfMovie;
    }

    public Timestamp getWritingTime() {
        return writingTime;
    }

    public void setWritingTime(Timestamp writingTime) {
        this.writingTime = writingTime;
    }
}
