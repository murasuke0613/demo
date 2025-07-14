package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.WritingData;

@Repository
public interface WritingDataRepository extends JpaRepository<WritingData, Integer> {

    // 団体IDで一覧取得
    List<WritingData> findByOrganizationKeyOrderByWritingTimeDesc(Integer organizationKey);

    // 団体ID + 部署IDで一覧取得
    List<WritingData> findByOrganizationKeyAndDepartmentIdOrderByWritingTimeDesc(
            Integer organizationKey, Integer departmentId);

    // ユーザー名スタンプとPINで1件取得
    Optional<WritingData> findByUserNameStampAndPin(String userNameStamp, String pin);
    
    List<WritingData> findByDepartmentIdOrderByWritingTimeDesc(Integer departmentId);

}
