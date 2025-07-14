package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.LoginDto;
import com.example.demo.model.DepartmentData;
import com.example.demo.model.JobData;
import com.example.demo.model.OrganizationData;
import com.example.demo.service.DepartmentDataService;
import com.example.demo.service.JobDataService;
import com.example.demo.service.OrganizationDataService;

@Controller
@RequestMapping("/departmentJob")
public class DepartmentJobRegisterController {

    private final OrganizationDataService organizationService;
    private final DepartmentDataService departmentService;
    private final JobDataService jobService;

    public DepartmentJobRegisterController(
            OrganizationDataService organizationService,
            DepartmentDataService departmentService,
            JobDataService jobService) {
        this.organizationService = organizationService;
        this.departmentService = departmentService;
        this.jobService = jobService;
    }

    // ✅ 部署・職種登録画面表示
    @GetMapping("/register")
    public String showRegisterPage(HttpSession session, Model model) {
        // 🟢 初回登録チェック
        if (session.getAttribute("tempOrganizationKey") != null) {
            Integer orgKey = (Integer) session.getAttribute("tempOrganizationKey");

            Optional<OrganizationData> orgOpt = organizationService.findByKey(orgKey);
            if (orgOpt.isEmpty()) {
                return "redirect:/login";
            }
            OrganizationData org = orgOpt.get();
            session.setAttribute("organization", org);

            List<DepartmentData> departments = departmentService.findAllByOrganizationKey(orgKey);
            List<JobData> jobs = jobService.findAllByOrganizationKey(orgKey);

            model.addAttribute("organization", org);
            model.addAttribute("departmentList", departments);
            model.addAttribute("jobList", jobs);
            model.addAttribute("isInitialRegistration", true); // 初回登録フラグ

            return "departmentAndJobRegister";
        }

        // 🔒 通常ログインチェック（管理者のみ）
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");
        if (loginUser == null || !Boolean.TRUE.equals(loginUser.getAdminFlag())) {
            return "redirect:/login";
        }

        Integer orgKey = loginUser.getOrganizationKey();
        Optional<OrganizationData> orgOpt = organizationService.findByKey(orgKey);
        if (orgOpt.isEmpty()) {
            return "redirect:/login";
        }
        OrganizationData org = orgOpt.get();
        session.setAttribute("organization", org);

        List<DepartmentData> departments = departmentService.findAllByOrganizationKey(orgKey);
        List<JobData> jobs = jobService.findAllByOrganizationKey(orgKey);

        model.addAttribute("organization", org);
        model.addAttribute("departmentList", departments);
        model.addAttribute("jobList", jobs);
        model.addAttribute("isInitialRegistration", false);
        model.addAttribute("loginUser", loginUser); 

        return "departmentAndJobRegister";
    }


    // ✅ 部署・職種登録処理（モーダル経由）
    @PostMapping("/register")
    public String registerDepartmentAndJob(
            @RequestParam(required = false) String departmentNames,
            @RequestParam(required = false) String jobNames,
            @RequestParam(required = false) String action,
            HttpSession session,
            Model model) {

        // 🟢 初回登録 or 通常登録の orgKey 判定
        Integer orgKey = (Integer) session.getAttribute("organizationKey");
        if (orgKey == null && session.getAttribute("tempOrganizationKey") != null) {
            orgKey = (Integer) session.getAttribute("tempOrganizationKey");
        }

        if (orgKey == null) {
            return "redirect:/login";
        }

        // 🔽 以下はそのままでOK
        if (departmentNames != null && (action.equals("confirmSingleDepartment") || action.equals("bulk"))) {
            List<String> deptList = Arrays.stream(departmentNames.split(","))
                                          .map(String::trim)
                                          .filter(name -> !name.isEmpty())
                                          .collect(Collectors.toList());

            for (String deptName : deptList) {
                DepartmentData dept = new DepartmentData();
                dept.setDepartmentName(deptName);
                dept.setOrganizationKey(orgKey);
                departmentService.insertDepartment(dept);
            }
        }

        if (jobNames != null && (action.equals("confirmSingleJob") || action.equals("bulk"))) {
            List<String> jobList = Arrays.stream(jobNames.split(","))
                                         .map(String::trim)
                                         .filter(name -> !name.isEmpty())
                                         .collect(Collectors.toList());

            for (String jobName : jobList) {
                JobData job = new JobData();
                job.setJobName(jobName);
                job.setOrganizationKey(orgKey);
                jobService.insertJob(job);
            }
        }

        model.addAttribute("message", "部署・職種の登録が完了しました");

        // 団体情報取得
        Optional<OrganizationData> orgOpt = organizationService.findByKey(orgKey);
        OrganizationData org = orgOpt.orElseThrow(() -> new IllegalStateException("団体情報が見つかりません"));

        List<DepartmentData> departments = departmentService.findAllByOrganizationKey(orgKey);
        List<JobData> jobs = jobService.findAllByOrganizationKey(orgKey);

        model.addAttribute("organization", org);
        model.addAttribute("departmentList", departments);
        model.addAttribute("jobList", jobs);
        model.addAttribute("isInitialRegistration", session.getAttribute("tempOrganizationKey") != null);

        return "departmentAndJobRegister";
    }

}
