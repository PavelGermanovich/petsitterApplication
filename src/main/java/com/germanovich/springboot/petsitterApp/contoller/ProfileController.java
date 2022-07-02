package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.CityRepository;
import com.germanovich.springboot.petsitterApp.dao.PetOwnerRepository;
import com.germanovich.springboot.petsitterApp.dao.PetsitterRepository;
import com.germanovich.springboot.petsitterApp.entity.City;
import com.germanovich.springboot.petsitterApp.entity.PetOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class ProfileController {
    @Autowired
    private PetsitterRepository petsitterRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    private List<City> getCityList() {
        return StreamSupport.stream(cityRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @ModelAttribute
    @PreAuthorize("hasAnyAuthority('PET_OWNER')")
    public void getUserCommonData(Model model, Principal principal) {
        PetOwner petOwner = petOwnerRepository.findPetOwnerByUserEmail(principal.getName());
        model.addAttribute("petowner", petOwner);
        model.addAttribute("cityList", getCityList());
    }

    @GetMapping("/user")
    public String userProfile(Model model) {
        return "profile";
    }
}
