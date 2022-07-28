package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.service.DebugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class InitialContoller {
    @Autowired
    DebugService debugService;

    @RequestMapping("/")
    public String homePage() {
        return "indexFromForm";
    }

}
