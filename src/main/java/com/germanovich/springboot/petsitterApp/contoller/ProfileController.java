package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.entity.User;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/user")
    public String userProfile(@AuthenticationPrincipal User user) {
        return "profile";
    }
}
