package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.LoginDto;
import com.example.demo.model.WritingData;
import com.example.demo.service.WritingDataService;

@Controller
public class PostWritingController {

    private final WritingDataService writingDataService;

    public PostWritingController(WritingDataService writingDataService) {
        this.writingDataService = writingDataService;
    }

    /**
     * ✅ 新規投稿処理
     */
    @PostMapping("/postWriting")
    public String postWriting(
            @RequestParam String message,
            @RequestParam(required = false) String pin,
            @RequestParam(required = false) MultipartFile pdfFile,
            HttpSession session,
            Model model
    ) {
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");
        if (loginUser == null) {
            return "redirect:/login";
        }

        try {
            if (pin != null && pin.trim().isEmpty()) {
                pin = null;
            }

            WritingData writingData = new WritingData();
            writingData.setOrganizationKey(loginUser.getOrganizationKey());
            writingData.setDepartmentId(loginUser.getDepartmentId());
            writingData.setDepartmentNameStamp(loginUser.getDepartmentName());
            writingData.setUserKey(loginUser.getUserKey());
            writingData.setUserNameStamp(loginUser.getUserName());
            writingData.setJobId(loginUser.getJobId());
            writingData.setJobNameStamp(loginUser.getJobName());
            writingData.setMessage(message);
            writingData.setPin(pin);

            if (pdfFile != null && !pdfFile.isEmpty()) {
                writingData.setPdfMovie(pdfFile.getBytes());
            }

            writingDataService.saveWriting(writingData);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "投稿に失敗しました。");
            return "redirect:/latestPosts";
        }

        // ✅ 投稿後は最新投稿一覧へリダイレクト
        return "redirect:/latestPosts";
    }

    /**
     * ✅ 投稿編集処理
     */
    @PostMapping("/editWriting")
    public String editWriting(
            @RequestParam Integer writingId,
            @RequestParam String message,
            @RequestParam(required = false) String pin,
            @RequestParam(required = false) MultipartFile pdfFile,
            HttpSession session,
            Model model
    ) {
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");
        if (loginUser == null) {
            return "redirect:/login";
        }

        byte[] pdfBytes = null;
        try {
            if (pdfFile != null && !pdfFile.isEmpty()) {
                pdfBytes = pdfFile.getBytes();
            }

            if (pin != null && pin.trim().isEmpty()) {
                pin = null;
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "PDFの読み込みに失敗しました。");
            return "redirect:/latestPosts";
        }

        boolean updated = writingDataService.updateWriting(writingId, message, pin, pdfBytes);

        if (!updated) {
            model.addAttribute("errorMessage", "PINが一致しないため更新できませんでした。");
        }

        // ✅ 編集後は元の部署履歴に戻る
        WritingData editedWriting = writingDataService.findById(writingId).orElse(null);
        if (editedWriting != null) {
            return "redirect:/departmentHistory/" + editedWriting.getDepartmentId();
        } else {
            return "redirect:/latestPosts";
        }
    }

    /**
     * ✅ 投稿削除処理
     */
    @PostMapping("/deleteWriting")
    public String deleteWriting(
            @RequestParam Integer writingId,
            @RequestParam(required = false) String pin,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");
        if (loginUser == null) {
            return "redirect:/login";
        }

        if (pin != null && pin.trim().isEmpty()) {
            pin = null;
        }

        // ✅ 削除前に部署IDを取得
        WritingData targetWriting = writingDataService.findById(writingId).orElse(null);
        Integer deptId = (targetWriting != null) ? targetWriting.getDepartmentId() : null;

        boolean deleted = writingDataService.deleteWriting(writingId, pin);

        if (!deleted) {
            redirectAttributes.addFlashAttribute("errorMessage", "PINが一致しないため削除できませんでした。");
        }

        if (deptId != null) {
            return "redirect:/departmentHistory/" + deptId;
        } else {
            return "redirect:/latestPosts";
        }
    }
}
