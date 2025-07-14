package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.OrganizationData;

@Repository
public interface OrganizationDataRepository extends JpaRepository<OrganizationData, Integer> {

    // 団体IDの存在チェック
    boolean existsByOrganizationId(String organizationId);

    // 団体キーで団体情報を取得
    Optional<OrganizationData> findByOrganizationKey(Integer organizationKey);
}
