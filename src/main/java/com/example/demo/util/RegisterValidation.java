package com.example.demo.util;

import org.springframework.stereotype.Component;

@Component
public class RegisterValidation {

    public boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public boolean validateUserId(String userId) {
        return isNotEmpty(userId)
                && userId.matches("^[a-zA-Z0-9]+$")
                && userId.length() >= 4
                && userId.length() <= 20;
    }

    public boolean validatePassword(String password) {
        return isNotEmpty(password) && password.length() >= 6;
    }

    public boolean validateOrganizationId(String orgId) {
        return isNotEmpty(orgId)
                && orgId.matches("^[a-zA-Z0-9\\-]+$")
                && orgId.length() >= 3
                && orgId.length() <= 30;
    }

    public boolean validateOrganizationName(String orgName) {
        return isNotEmpty(orgName) && orgName.length() <= 50;
    }

    public boolean validateDepartmentOrJobName(String name) {
        return isNotEmpty(name) && name.length() <= 50;
    }
}
