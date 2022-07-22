package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.PetOwnerRepository;
import com.germanovich.springboot.petsitterApp.dao.PetRepository;
import com.germanovich.springboot.petsitterApp.entity.Pet;
import com.germanovich.springboot.petsitterApp.entity.PetOwner;
import com.germanovich.springboot.petsitterApp.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class PetController {
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PetOwnerRepository petOwnerRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/createPet")
    public String createPet(@Valid Pet pet, Principal principal, Model model,
                            @RequestParam("petImage") MultipartFile multipartFile) {
        PetOwner petOwner = petOwnerRepository.findPetOwnerByUserEmail(principal.getName());
        try {
            //toDo add check if file null
            pet.setFileDb(fileStorageService.store(multipartFile));
        } catch (Exception e) {
            //ToDo add exceptions here
        }
        //ToDo add validation
        pet.setPetOwner(petOwner);
        petOwner.getPetList().add(pet);
        petRepository.save(pet);
        model.addAttribute("messagePetCreated", "Success");
        model.addAttribute("petowner",  petOwner);
        return "petownerProfile";
    }
}
