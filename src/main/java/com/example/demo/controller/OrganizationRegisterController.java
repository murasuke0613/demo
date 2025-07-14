package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.OrganizationData;
import com.example.demo.service.OrganizationDataService;
import com.example.demo.util.SecurityUtil;

@Controller
public class OrganizationRegisterController {

    private final OrganizationDataService organizationService;
    private final SecurityUtil securityUtil;

    public OrganizationRegisterController(OrganizationDataService organizationService,
                                          SecurityUtil securityUtil) {
        this.organizationService = organizationService;
        this.securityUtil = securityUtil;
    }

    // ✅ 団体登録フォーム表示
    @GetMapping("/organization/register")
    public String showRegisterPage(HttpSession session) {
        // 🟢 既存セッションを破棄
        session.invalidate();
        return "organizationRegister"; // templates/organizationRegister.html
    }

    @PostMapping("/organization/register")
    public String registerOrganization(
            @RequestParam String organizationId,
            @RequestParam String organizationName,
            @RequestParam String organizationPassword,
            HttpServletRequest request, // 追加
            RedirectAttributes redirectAttributes
    ) {
        if (organizationService.isOrganizationIdExists(organizationId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "この団体IDは既に使われています。");
            return "redirect:/organization/register";
        }

        // 団体登録処理
        OrganizationData org = new OrganizationData();
        org.setOrganizationId(organizationId);
        org.setOrganizationName(organizationName);
        org.setOrganizationPassword(securityUtil.encodePassword(organizationPassword));
        organizationService.insertOrganization(org);

        // 🟢 セッション再生成
        HttpSession session = request.getSession(false); // 今のセッション取得
        if (session != null) {
            session.invalidate(); // 既存セッション破棄
        }
        session = request.getSession(true); // 新しいセッション作成

        // ✅ 部署登録用の一時キーをセット
        session.setAttribute("tempOrganizationKey", org.getOrganizationKey());

        // ✅ メッセージを部署登録画面に渡す
        redirectAttributes.addFlashAttribute("registerComplete", true);

        return "redirect:/departmentJob/register";
    }


}
