package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.LoginDto;
import com.example.demo.model.DepartmentData;
import com.example.demo.model.WritingData;
import com.example.demo.service.DepartmentDataService;
import com.example.demo.service.WritingDataService;

@Controller
public class DepartmentHistoryController {

    private final DepartmentDataService departmentService;
    private final WritingDataService writingService;

    // ✅ コンストラクタDI
    public DepartmentHistoryController(DepartmentDataService departmentService, WritingDataService writingService) {
        this.departmentService = departmentService;
        this.writingService = writingService;
    }

    // ✅ 履歴ページ表示
    @GetMapping("/departmentHistory/{id}")
    public String showDepartmentHistory(@PathVariable("id") Integer departmentId, HttpSession session, Model model) {
        // ログインチェック
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 部署情報取得
        DepartmentData department = departmentService.findById(departmentId);
        if (department == null) {
            model.addAttribute("errorMessage", "部署が見つかりませんでした。");
            return "error"; // カスタムエラーページへ
        }

        // 組織権限チェック
        if (!department.getOrganizationKey().equals(loginUser.getOrganizationKey())) {
            model.addAttribute("errorMessage", "この部署へのアクセス権がありません。");
            return "error";
        }

        // 部署IDに紐づく投稿リストを取得
        List<WritingData> writings = writingService.findByDepartmentId(departmentId);

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("department", department);
        model.addAttribute("writingList", writings);

        return "departmentHistory"; // templates/departmentHistory.html
    }
}
