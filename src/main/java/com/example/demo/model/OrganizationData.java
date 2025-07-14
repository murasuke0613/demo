package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "organization_data") // DBテーブル名に合わせる
public class OrganizationData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "organization_key")
	private Integer organizationKey;

    @Column(name = "organization_id", nullable = false, unique = true)
    private String organizationId;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "organization_password", nullable = false)
    private String organizationPassword;

    // ---------- getter/setter ----------

    public Integer getOrganizationKey() {
        return organizationKey;
    }

    public void setOrganizationKey(Integer organizationKey) {
        this.organizationKey = organizationKey;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationPassword() {
        return organizationPassword;
    }

    public void setOrganizationPassword(String organizationPassword) {
        this.organizationPassword = organizationPassword;
    }
}
