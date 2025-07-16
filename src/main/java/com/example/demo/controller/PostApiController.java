package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.LoginDto;
import com.example.demo.model.WritingData;
import com.example.demo.service.WritingDataService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/posts")
public class PostApiController {

    private final WritingDataService writingDataService;

    @Autowired
    public PostApiController(WritingDataService writingDataService) {
        this.writingDataService = writingDataService;
    }

    @GetMapping
    public List<WritingData> getAllPosts(HttpSession session) {
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ログインしてください");
        }
        Integer organizationKey = loginUser.getOrganizationKey();
        return writingDataService.findByOrganizationKey(organizationKey);
    }
}
