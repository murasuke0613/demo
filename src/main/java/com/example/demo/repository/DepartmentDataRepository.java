package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.DepartmentData;

@Repository
public interface DepartmentDataRepository extends JpaRepository<DepartmentData, Integer> {

    // 指定された団体IDの部署一覧を取得
    List<DepartmentData> findByOrganizationKeyOrderByDepartmentId(Integer organizationKey);
}
