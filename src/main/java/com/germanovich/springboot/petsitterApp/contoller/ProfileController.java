package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.PetOwnerRepository;
import com.germanovich.springboot.petsitterApp.dao.PetsitterRepository;
import com.germanovich.springboot.petsitterApp.entity.PetOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class ProfileController {
    @Autowired
    private PetsitterRepository petsitterRepository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    @ModelAttribute
    @PreAuthorize("hasAnyAuthority('PET_OWNER')")
    public void getUserCommonData(Model model, Principal principal) {
        PetOwner petOwner = petOwnerRepository.findPetOwnerByUserEmail(principal.getName());
        model.addAttribute("petowner", petOwner);
    }

    @GetMapping("/user")
    public String userProfile(Model model) {
        return "profile";
    }
}
