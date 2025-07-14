package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

    @Query("""
        SELECT u
        FROM UserInfo u
        JOIN FETCH u.organization o
        JOIN FETCH u.job j
        JOIN FETCH u.department d
        WHERE o.organizationId = :orgId
          AND u.userId = :userId
    """)
    Optional<UserInfo> findByOrganizationIdAndUserId(
        @Param("orgId") String organizationId,
        @Param("userId") String userId
    );

    boolean existsByUserIdAndOrganizationKey(String userId, Integer organizationKey);
    
    List<UserInfo> findByOrganizationKey(Integer organizationKey);
    
    int countByDepartmentId(Integer departmentId);
    
    // ✅ 職種IDで所属ユーザー数を数える
    int countByJobId(Integer jobId);

    
}
