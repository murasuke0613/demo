package com.example.demo.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginDto;
import com.example.demo.model.UserInfo;
import com.example.demo.repository.UserInfoRepository;

@Service
public class LoginService {

    private final UserInfoRepository userInfoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Optional<LoginDto> login(String orgId, String userId, String rawPassword) {
        Optional<UserInfo> optionalUser = userInfoRepository.findByOrganizationIdAndUserId(orgId, userId);

        if (optionalUser.isPresent()) {
            UserInfo user = optionalUser.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return Optional.of(toLoginDto(user));
            }
        }

        return Optional.empty();
    }

    private LoginDto toLoginDto(UserInfo user) {
        LoginDto dto = new LoginDto();
        dto.setUserKey(user.getUserKey());
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setOrganizationKey(user.getOrganization().getOrganizationKey());
        dto.setOrganizationId(user.getOrganization().getOrganizationId());
        dto.setOrganizationName(user.getOrganization().getOrganizationName());
        dto.setDepartmentId(user.getDepartment().getDepartmentId());
        dto.setDepartmentName(user.getDepartment().getDepartmentName());
        dto.setJobId(user.getJob().getJobId());
        dto.setJobName(user.getJob().getJobName());
        dto.setAdminFlag(user.getAdminFlag());
        return dto;
    }
}
