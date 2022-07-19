package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.*;
import com.germanovich.springboot.petsitterApp.entity.City;
import com.germanovich.springboot.petsitterApp.entity.FileDb;
import com.germanovich.springboot.petsitterApp.entity.PetOwner;
import com.germanovich.springboot.petsitterApp.service.FileStorageService;
import com.germanovich.springboot.petsitterApp.service.UserService;
import com.germanovich.springboot.petsitterApp.validation.EmailExistException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Set;
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

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserRepository userRepository;

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
    public String userProfile() {
        return "profile";
    }

    @GetMapping("/product/image/{id}")
    public void showProductImage(@PathVariable int id,
                                 HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg"); // Or whatever format you wanna use

        FileDb fileDb = fileRepository.getById(id);

        InputStream is = new ByteArrayInputStream(fileDb.getData());
        response.setContentType(fileDb.getType());
        IOUtils.copy(is, response.getOutputStream());
    }

    @PostMapping(value = "/updatePetowner")
    public ModelAndView updatePetowner(@Valid final PetOwner petownerForm, final BindingResult bindingResult, Principal principal,
                                       Model model) {
        PetOwner petOwnerExisting = petOwnerRepository.findPetOwnerByUserEmail(principal.getName());

        if (!petOwnerExisting.getOwnerDescription().equals(petownerForm.getOwnerDescription())) {
            petOwnerExisting.setOwnerDescription(petownerForm.getOwnerDescription());
        } else if (!petOwnerExisting.getUser().getNameFirst().equals(petownerForm.getUser().getNameFirst())) {
            petOwnerExisting.getUser().setNameFirst(petownerForm.getUser().getNameFirst());
        } else if (!petOwnerExisting.getUser().getNameLast().equals(petownerForm.getUser().getNameLast())) {
            petOwnerExisting.getUser().setNameLast(petownerForm.getUser().getNameLast());
        } else if (!petOwnerExisting.getUser().getBirthdate().equals(petownerForm.getUser().getBirthdate())) {
            petOwnerExisting.getUser().setBirthdate(petownerForm.getUser().getBirthdate());
        } else if (!petOwnerExisting.getUser().getPhone().equals(petownerForm.getUser().getPhone())) {
            petOwnerExisting.getUser().setPhone(petownerForm.getUser().getPhone());
        } else if (!petOwnerExisting.getUser().getCity().equals(petownerForm.getUser().getCity())) {
            petOwnerExisting.getUser().setCity(petownerForm.getUser().getCity());
        } else if (!petOwnerExisting.getUser().getEmail().equals(petownerForm.getUser().getEmail())) {
            petOwnerExisting.getUser().setEmail(petownerForm.getUser().getEmail());
        }

        Set<ConstraintViolation<PetOwner>> violations = validator.validate(petownerForm);

        if (!violations.isEmpty()) {
            model.addAttribute("message", "Failed");
            return new ModelAndView("profile", "petOwner", petownerForm);
        }

        try {
            userService.updatePetowner(petOwnerExisting, petownerForm.getUser().getEmail());
        } catch (EmailExistException e) {
            bindingResult.addError(new FieldError("petOwner", "petOwner.user", e.getMessage()));
            model.addAttribute("message", "Failed");
            return new ModelAndView("registerOwner", "petOwner", petownerForm);
        }

        model.addAttribute("message", "Success");

        return new ModelAndView("profile");
    }

    @PostMapping(value = "/updateProfileImage")
    public ModelAndView updateProfileImage(@RequestParam("file") MultipartFile multipartFile, Principal principal) {
        PetOwner petOwnerExisting = petOwnerRepository.findPetOwnerByUserEmail(principal.getName());
        try {
            petOwnerExisting.getUser().setFileDb(fileStorageService.store(multipartFile));
        } catch (IOException e) {
            //toDo add error here in case failure
            e.printStackTrace();
        }
        userRepository.save(petOwnerExisting.getUser());

        return new ModelAndView("profile","petOwner", petOwnerExisting);
    }
}
