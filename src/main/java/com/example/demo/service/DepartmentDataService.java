package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.DepartmentData;
import com.example.demo.repository.DepartmentDataRepository;
import com.example.demo.repository.UserInfoRepository;

@Service
public class DepartmentDataService {

    private final DepartmentDataRepository departmentRepository;
    private final UserInfoRepository userRepository;

    public DepartmentDataService(DepartmentDataRepository departmentRepository,
                                 UserInfoRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    /**
     * 指定された団体IDの部署一覧を取得
     */
    public List<DepartmentData> findAllByOrganizationKey(Integer organizationKey) {
        return departmentRepository.findByOrganizationKeyOrderByDepartmentId(organizationKey);
    }

    /**
     * 部署一覧＋所属ユーザー数を取得
     */
    public List<DepartmentData> findAllWithUserCount(Integer organizationKey) {
        List<DepartmentData> departmentList = departmentRepository.findByOrganizationKeyOrderByDepartmentId(organizationKey);
        for (DepartmentData dept : departmentList) {
            int userCount = userRepository.countByDepartmentId(dept.getDepartmentId());
            dept.setUserCount(userCount); // DepartmentDataに userCount フィールドが必要
        }
        return departmentList;
    }

    /**
     * 部署を登録する
     */
    @Transactional
    public void insertDepartment(DepartmentData dto) {
        departmentRepository.save(dto);
    }

    /**
     * 部署IDで部署を取得
     */
    public DepartmentData findById(Integer departmentId) {
        return departmentRepository.findById(departmentId).orElse(null);
    }

    /**
     * 部署を削除（所属ユーザーがいる場合は削除しない）
     */
    @Transactional
    public boolean deleteIfNoUsers(Integer departmentId) {
        int userCount = userRepository.countByDepartmentId(departmentId);
        if (userCount > 0) {
            return false; // 削除不可
        }
        departmentRepository.deleteById(departmentId);
        return true; // 削除成功
    }
}
