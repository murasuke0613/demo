package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.JobData;

@Repository
public interface JobDataRepository extends JpaRepository<JobData, Integer> {

    // 指定された団体キーの職種一覧を取得
    List<JobData> findByOrganizationKeyOrderByJobId(Integer organizationKey);
}
