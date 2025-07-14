package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.LoginDto;
import com.example.demo.service.DepartmentDataService;
import com.example.demo.service.JobDataService;
import com.example.demo.service.UserInfoService;

@Controller
@RequestMapping("/admin") // 管理者専用URL
public class AdminController {

    private final UserInfoService userInfoService;
    private final DepartmentDataService departmentDataService;
    private final JobDataService jobDataService;

    public AdminController(UserInfoService userInfoService,
                           DepartmentDataService departmentDataService,
                           JobDataService jobDataService) {
        this.userInfoService = userInfoService;
        this.departmentDataService = departmentDataService;
        this.jobDataService = jobDataService;
    }

    /**
     * 管理者削除ページ表示
     */
    @GetMapping("/delete")
    public String showDeletePage(HttpSession session, Model model) {
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");

        if (loginUser == null || !Boolean.TRUE.equals(loginUser.getAdminFlag())) {
            LoginDto dummy = new LoginDto();
            dummy.setAdminFlag(Boolean.FALSE); // null防止
            model.addAttribute("loginUser", dummy);
            return "admin_delete";
        }

        model.addAttribute("loginUser", loginUser);

        Integer organizationKey = loginUser.getOrganizationKey();

        // 所属人数をセットして返すバージョン
        model.addAttribute("userList", userInfoService.findAllByOrganization(organizationKey));
        model.addAttribute("departmentList", departmentDataService.findAllWithUserCount(organizationKey));
        model.addAttribute("jobList", jobDataService.findAllWithUserCount(organizationKey));

        return "admin_delete";
    }

    /**
     * 🧑‍💼 ユーザー削除
     */
    @PostMapping("/delete/user/{key}")
    public String deleteUser(@PathVariable("key") Integer userKey, HttpSession session, RedirectAttributes redirectAttrs) {
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");
        if (loginUser == null || !Boolean.TRUE.equals(loginUser.getAdminFlag())) {
            return "redirect:/home";
        }

        // 自分自身は削除不可
        if (loginUser.getUserKey().equals(userKey)) {
            redirectAttrs.addFlashAttribute("errorMessage", "自身のアカウントは削除できません。");
            return "redirect:/admin/delete";
        }

        userInfoService.deleteById(userKey); // 👈 KEYで削除
        return "redirect:/admin/delete";
    }
    /**
     * 🏢 部署削除
     */
    @PostMapping("/delete/department/{id}")
    public String deleteDepartment(@PathVariable("id") Integer departmentId, HttpSession session, RedirectAttributes redirectAttrs) {
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");
        if (loginUser == null || !Boolean.TRUE.equals(loginUser.getAdminFlag())) {
            return "redirect:/home";
        }

        boolean deleted = departmentDataService.deleteIfNoUsers(departmentId);
        if (!deleted) {
            redirectAttrs.addFlashAttribute("errorMessage", "この部署には所属ユーザーがいるため削除できません。");
        }

        return "redirect:/admin/delete";
    }

    /**
     * 🛠 職種削除
     */
    @PostMapping("/delete/job/{id}")
    public String deleteJob(@PathVariable("id") Integer jobId, HttpSession session, RedirectAttributes redirectAttrs) {
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");
        if (loginUser == null || !Boolean.TRUE.equals(loginUser.getAdminFlag())) {
            return "redirect:/home";
        }

        boolean deleted = jobDataService.deleteIfNoUsers(jobId);
        if (!deleted) {
            redirectAttrs.addFlashAttribute("errorMessage", "この職種には所属ユーザーがいるため削除できません。");
        }

        return "redirect:/admin/delete";
    }
}
