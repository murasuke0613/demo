package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.OrganizationData;
import com.example.demo.repository.OrganizationDataRepository;

@Service
public class OrganizationDataService {

    private final OrganizationDataRepository organizationRepository;

    public OrganizationDataService(OrganizationDataRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    /**
     * 団体登録処理
     */
    @Transactional
    public void insertOrganization(OrganizationData dto) {
        organizationRepository.save(dto);
    }

    /**
     * 団体キーで団体情報を取得
     */
    public Optional<OrganizationData> findByKey(Integer organizationKey) {
        return organizationRepository.findByOrganizationKey(organizationKey);
    }

    /**
     * 団体IDの存在チェック
     */
    public boolean isOrganizationIdExists(String organizationId) {
        return organizationRepository.existsByOrganizationId(organizationId);
    }
}
