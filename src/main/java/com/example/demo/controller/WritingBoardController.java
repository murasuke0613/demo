package com.example.demo.controller;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.LoginDto;
import com.example.demo.model.DepartmentData;
import com.example.demo.model.WritingData;
import com.example.demo.service.DepartmentDataService;
import com.example.demo.service.WritingDataService;

@Controller
public class WritingBoardController {

    private final DepartmentDataService departmentService;
    private final WritingDataService writingService;

    public WritingBoardController(DepartmentDataService departmentService, WritingDataService writingService) {
        this.departmentService = departmentService;
        this.writingService = writingService;
    }

    // ✅ 最新投稿ページ表示
    @GetMapping("/latestPosts")
    public String showLatestPosts(HttpSession session, Model model) {
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");

        if (loginUser == null) {
            return "redirect:/login";
        }

        // 全部署リストを取得
        List<DepartmentData> departments = departmentService.findAllByOrganizationKey(loginUser.getOrganizationKey());

        // 全投稿リストを取得
        List<WritingData> writings = writingService.findByOrganizationKey(loginUser.getOrganizationKey());

        // 部署IDごとに最新投稿をマッピング
        Map<Integer, WritingData> latestPostPerDept = new HashMap<>();

        for (DepartmentData dept : departments) {
            Optional<WritingData> latestPost = writings.stream()
                .filter(w -> w.getDepartmentId().equals(dept.getDepartmentId()))
                .max(Comparator.comparing(WritingData::getWritingTime));

            WritingData postToShow = latestPost.orElseGet(() -> {
                WritingData emptyPost = new WritingData();
                emptyPost.setMessage("この部署にはまだ投稿がありません。");
                emptyPost.setUserNameStamp("システム");
                emptyPost.setWritingTime(null); // 投稿日時はnullで
                return emptyPost;
            });

            latestPostPerDept.put(dept.getDepartmentId(), postToShow);
        }

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("departmentDataList", departments);
        model.addAttribute("latestPostPerDept", latestPostPerDept);
        model.addAttribute("writingList", writings); // 履歴モーダル用

        return "latestPosts";
    }

    // ✅ ホーム画面表示
    @GetMapping("/home")
    public String showHome(HttpSession session, Model model) {
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");

        if (loginUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("loginUser", loginUser);
        return "home"; // templates/home.html
    }
}
