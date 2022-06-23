package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.service.DebugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InitialContoller {
    @Autowired
    DebugService debugService;

    @GetMapping("/")
    public String init() {
        debugService.checkSomeStuff();
        return "good job my friend";
    }
}
