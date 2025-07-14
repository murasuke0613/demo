package com.example.demo.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.LoginDto;
import com.example.demo.model.OrganizationData;
import com.example.demo.service.DepartmentDataService;
import com.example.demo.service.LoginService;
import com.example.demo.service.OrganizationDataService;
import com.example.demo.service.WritingDataService;

@Controller
public class LoginController {

    private final LoginService loginService;
    private final DepartmentDataService departmentService;
    private final WritingDataService writingDataService;
    private final OrganizationDataService organizationService;

    // ✅ コンストラクタで全サービスをDI
    public LoginController(
            LoginService loginService,
            DepartmentDataService departmentService,
            WritingDataService writingDataService,
            OrganizationDataService organizationService) {
        this.loginService = loginService;
        this.departmentService = departmentService;
        this.writingDataService = writingDataService;
        this.organizationService = organizationService;
    }

    @Controller
    public class RootRedirectController {
        @GetMapping("/")
        public String redirectToLogin() {
            return "redirect:/login";
        }
    }
    
    // ✅ ログイン画面表示
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // templates/login.html を表示
    }

    // ✅ ログイン処理
    @PostMapping("/login")
    public String doLogin(
            @RequestParam String organizationId,
            @RequestParam String userId,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        Optional<LoginDto> login = loginService.login(organizationId, userId, password);

        if (login.isPresent()) {
            LoginDto user = login.get();

            // organizationKey をセッションに格納
            session.setAttribute("organizationKey", user.getOrganizationKey());

            // organization をセッションに格納
            OrganizationData org = organizationService.findByKey(user.getOrganizationKey()).orElse(null);
            if (org != null) {
                session.setAttribute("organization", org);
                user.setOrganizationName(org.getOrganizationName());
            }

            session.setAttribute("LOGIN_INFO", user);
            return "redirect:/home";
        } else {
            // 認証失敗時のエラーメッセージ
            model.addAttribute("errorMessage", "ログインに失敗しました。");
            return "login";
        }
    }


    // ✅ ログアウト処理
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
