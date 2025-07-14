package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.LoginDto;
import com.example.demo.model.OrganizationData;
import com.example.demo.service.DepartmentDataService;
import com.example.demo.service.JobDataService;
import com.example.demo.service.UserInfoService;

@Controller
@RequestMapping("/user")
public class UserRegisterController {

    private final DepartmentDataService departmentService;
    private final JobDataService jobService;
    private final UserInfoService userInfoService;

    public UserRegisterController(
            DepartmentDataService departmentService,
            JobDataService jobService,
            UserInfoService userInfoService) {
        this.departmentService = departmentService;
        this.jobService = jobService;
        this.userInfoService = userInfoService;
    }

    @GetMapping("/register")
    public String showUserRegisterPage(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) Boolean complete, // ✅ 完了フラグ
            @RequestParam(required = false) Boolean clear,    // ✅ 入力初期化フラグ
            HttpSession session,
            Model model) {

        Integer orgKey = (Integer) session.getAttribute("organizationKey");
        if (orgKey == null) {
            orgKey = (Integer) session.getAttribute("tempOrganizationKey");
            if (orgKey == null) {
                return "redirect:/login";
            }
            model.addAttribute("isInitialRegistration", true);
        } else {
            model.addAttribute("isInitialRegistration", false);
        }

        // ✅ clear=true のときは初回登録モード解除
        if (clear != null && clear) {
            model.addAttribute("userId", "");
            model.addAttribute("userName", "");
            model.addAttribute("password", "");
            model.addAttribute("isAdmin", false);
            model.addAttribute("isInitialRegistration", false);
        }

        // 🟢 ログインユーザー情報
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");
        model.addAttribute("loginUser", loginUser);

        OrganizationData organization = (OrganizationData) session.getAttribute("organization");
        model.addAttribute("organization", organization);

        model.addAttribute("departmentList", departmentService.findAllByOrganizationKey(orgKey));
        model.addAttribute("jobList", jobService.findAllByOrganizationKey(orgKey));

        // ✅ 完了フラグ
        model.addAttribute("registerComplete", complete != null && complete);

        // 遷移元
        model.addAttribute("fromPage", from != null ? from : "home");

        return "userRegister";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String userId,
            @RequestParam String userName,
            @RequestParam Integer departmentId,
            @RequestParam Integer jobId,
            @RequestParam String password,
            @RequestParam(defaultValue = "false") Boolean isAdmin,
            HttpSession session,
            Model model) {

        Integer orgKey = (Integer) session.getAttribute("organizationKey");
        if (orgKey == null) {
            orgKey = (Integer) session.getAttribute("tempOrganizationKey");
            if (orgKey == null) {
                return "redirect:/login";
            }
        }

        try {
            userInfoService.registerUser(userId, userName, departmentId, jobId, password, isAdmin, orgKey);
            // ✅ クエリパラメータに完了フラグを付ける
            return "redirect:/user/register?complete=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "登録エラー: " + e.getMessage());
            return "userRegister";
        }
    }
}
