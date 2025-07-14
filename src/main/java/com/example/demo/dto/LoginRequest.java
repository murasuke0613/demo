package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotBlank(message = "ユーザーIDは必須です")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "ユーザーIDは半角英数字のみです")
    @Size(min = 4, max = 20, message = "ユーザーIDは4〜20文字で入力してください")
    private String userId;

    @NotBlank(message = "パスワードは必須です")
    @Size(min = 6, message = "パスワードは6文字以上で入力してください")
    private String password;

    @NotBlank(message = "団体IDは必須です")
    @Pattern(regexp = "^[a-zA-Z0-9\\-]+$", message = "団体IDは半角英数字とハイフンのみです")
    @Size(min = 3, max = 30, message = "団体IDは3〜30文字で入力してください")
    private String organizationId;

    // getter/setter
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getOrganizationId() { return organizationId; }
    public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }
}
