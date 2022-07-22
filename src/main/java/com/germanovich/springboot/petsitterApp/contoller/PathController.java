package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.CityRepository;
import com.germanovich.springboot.petsitterApp.dao.PetRepository;
import com.germanovich.springboot.petsitterApp.dao.PetTypeRepository;
import com.germanovich.springboot.petsitterApp.entity.*;
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
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PetTypeRepository petTypeRepository;

    private List<City> getCityList() {
        return StreamSupport.stream(cityRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    private List<PetType> getPetType() {
        return StreamSupport.stream(petTypeRepository.findAll().spliterator(), false)
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
        return new ModelAndView("registerOwnerBootstrap", "petOwner", new PetOwner());
    }

    @GetMapping("/addPet")
    public String addingPetPage(Model model) {
        model.addAttribute("pet", new Pet());
        model.addAttribute("petTypeList", getPetType());
        return "addPet";
    }
}
