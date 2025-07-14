package com.example.demo.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.UserInfo;
import com.example.demo.repository.UserInfoRepository;

@Service
public class UserInfoService {

    private final UserInfoRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserInfoService(UserInfoRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Spring Security のエンコーダー
    }

    /**
     * 新規ユーザー登録
     */
    @Transactional
    public void registerUser(String userId, String userName, Integer departmentId,
                             Integer jobId, String rawPassword, boolean isAdmin, Integer organizationKey) {

        // DTOを組み立て
        UserInfo user = new UserInfo();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setDepartmentId(departmentId);
        user.setJobId(jobId);
        user.setAdminFlag(isAdmin);
        user.setOrganizationKey(organizationKey);

        // パスワードをハッシュ化
        String hashedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(hashedPassword);

        // 重複チェック
        boolean exists = userRepository.existsByUserIdAndOrganizationKey(userId, organizationKey);
        if (exists) {
            throw new RuntimeException("このユーザーIDは既に登録されています。");
        }

        userRepository.save(user);
    }

    /**
     * 全ユーザー取得
     */
    public List<UserInfo> findAllByOrganization(Integer organizationKey) {
        return userRepository.findByOrganizationKey(organizationKey);
    }

    /**
     * ユーザー削除
     */
    @Transactional
    public void deleteById(Integer userKey) {
        userRepository.deleteById(userKey);
    }
}
