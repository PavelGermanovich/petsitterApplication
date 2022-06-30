package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.CityRepository;
import com.germanovich.springboot.petsitterApp.entity.City;
import com.germanovich.springboot.petsitterApp.entity.PetOwner;
import com.germanovich.springboot.petsitterApp.entity.PetSitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class PathController {
    @Autowired
    private CityRepository cityRepository;

    private List<City> getCityList() {
        return StreamSupport.stream(cityRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/register-petsitter")
    public String registerPetsitterPage(Model model) {
        model.addAttribute("cityList", getCityList());
        model.addAttribute("petsitter", new PetSitter());
        return "registerPetsitter";
    }

    @GetMapping("/register-owner")
    public ModelAndView registerOwnerPage(Model model) {
        model.addAttribute("cityList", getCityList());
        return new ModelAndView("registerOwner", "petowner", new PetOwner());
    }
}
