package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginDto;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @GetMapping
    public Map<String, Object> getCurrentUser(HttpSession session) {
        LoginDto loginUser = (LoginDto) session.getAttribute("LOGIN_INFO");
        Map<String, Object> response = new HashMap<>();
        if (loginUser != null) {
            response.put("userName", loginUser.getUserName());
            response.put("userId", loginUser.getUserId());
        } else {
            response.put("userName", "ゲスト");
            response.put("userId", "N/A");
        }
        return response;
    }
}
