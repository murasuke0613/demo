package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.JobData;
import com.example.demo.repository.JobDataRepository;
import com.example.demo.repository.UserInfoRepository;

@Service
public class JobDataService {

    private final JobDataRepository jobRepository;
    private final UserInfoRepository userRepository;

    public JobDataService(JobDataRepository jobRepository,
                          UserInfoRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    /**
     * 職種を登録する
     */
    @Transactional
    public void insertJob(JobData jobDto) {
        jobRepository.save(jobDto);
    }

    /**
     * 指定された団体キーの職種一覧を取得
     */
    public List<JobData> findAllByOrganizationKey(Integer organizationKey) {
        return jobRepository.findByOrganizationKeyOrderByJobId(organizationKey);
    }

    /**
     * 職種一覧＋所属ユーザー数を取得
     */
    public List<JobData> findAllWithUserCount(Integer organizationKey) {
        List<JobData> jobList = jobRepository.findByOrganizationKeyOrderByJobId(organizationKey);
        for (JobData job : jobList) {
            int userCount = userRepository.countByJobId(job.getJobId());
            job.setUserCount(userCount); // JobData に userCount フィールドが必要
        }
        return jobList;
    }

    /**
     * 職種を削除（所属ユーザーがいる場合は削除しない）
     */
    @Transactional
    public boolean deleteIfNoUsers(Integer jobId) {
        int userCount = userRepository.countByJobId(jobId);
        if (userCount > 0) {
            return false; // 削除不可
        }
        jobRepository.deleteById(jobId);
        return true; // 削除成功
    }
}
