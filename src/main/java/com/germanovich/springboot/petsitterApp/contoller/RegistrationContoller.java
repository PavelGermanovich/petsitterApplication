package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.*;
import com.germanovich.springboot.petsitterApp.dao.validation.OnCreate;
import com.germanovich.springboot.petsitterApp.entity.*;
import com.germanovich.springboot.petsitterApp.enums.PETSITTER_SERVICE;
import com.germanovich.springboot.petsitterApp.enums.USER_ROLE;
import com.germanovich.springboot.petsitterApp.service.FileStorageService;
import com.germanovich.springboot.petsitterApp.service.UserService;
import com.germanovich.springboot.petsitterApp.validation.EmailExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class RegistrationContoller {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PetSitterServiceCostRepository petSitterServiceCostRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private FileStorageService storageService;

    @Autowired
    private PetSizeLimitRepository petSizeLimitRepository;

    @ModelAttribute
    private List<City> getCityList() {
        return StreamSupport.stream(cityRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    private List<PetSizeLimit> getPetSizeLimits() {
        return StreamSupport.stream(petSizeLimitRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @GetMapping(value = "/signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("registrationTypeSelect", "user", new User());
    }

    @Validated(OnCreate.class)
    @PostMapping(value = "/petsitter/register")
    public ModelAndView registerPetsitter(@Valid final Petsitter petsitter, final BindingResult bindingResult,
                                          @ModelAttribute("petwalkgingCheckbox") String petwalkgingCheckbox,
                                          @ModelAttribute("petsitCheckbox") String petsitCheckbox,
                                          @RequestParam("file") MultipartFile multipartFile, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("sizeLimits", getPetSizeLimits());
            return new ModelAndView("registerPetsitter", "petsitter", petsitter);
        }

        try {
            petsitter.getUser().setFileDb(storageService.store(multipartFile));
        } catch (Exception e) {
            bindingResult.addError(new FieldError("petsitter", "petSitter.user.fileDb", e.getMessage()));
        }

        petsitter.getUser().setUserRole(userRoleRepository.findByRoleId(USER_ROLE.PET_SITTER));
        Petsitter petSitterRegistered;
        try {
            petSitterRegistered = userService.registerPetsitter(petsitter);
        } catch (EmailExistException e) {
            bindingResult.addError(new FieldError("petsitter", "petSitter.user", e.getMessage()));
            return new ModelAndView("registerPetsitter", "petsitter", petsitter);
        }

        if (!petwalkgingCheckbox.isEmpty()) {
            PetsitterServiceCost petsitterServiceCost = new PetsitterServiceCost();
            petsitterServiceCost.setService(serviceRepository.findServiceByServiceName(PETSITTER_SERVICE.WALKING.getRoleName()));
            petsitterServiceCost.setPetSitter(petSitterRegistered);
            petSitterServiceCostRepository.save(petsitterServiceCost);

        }

        if (!petsitCheckbox.isEmpty()) {
            PetsitterServiceCost petsitterServiceCost = new PetsitterServiceCost();
            petsitterServiceCost.setService(serviceRepository.findServiceByServiceName(PETSITTER_SERVICE.SITTING.getRoleName()));
            petsitterServiceCost.setPetSitter(petSitterRegistered);
            petSitterServiceCostRepository.save(petsitterServiceCost);
        }

        ModelAndView loginPage = new ModelAndView("redirect:/login");
        return loginPage;
    }

    @Validated(OnCreate.class)
    @PostMapping(value = "/petowner/register")
    public ModelAndView registerPetowner(@Valid final PetOwner petOwner, final BindingResult bindingResult,
                                         @RequestParam("file") MultipartFile multipartFile) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("registerOwner", "petOwner", petOwner);
        }

        try {
            petOwner.getUser().setFileDb(storageService.store(multipartFile));
        } catch (Exception e) {
            bindingResult.addError(new FieldError("petOwner", "petOwner.user.fileDb", e.getMessage()));
        }

        petOwner.getUser().setUserRole(userRoleRepository.findByRoleId(USER_ROLE.PET_OWNER));
        try {
            userService.registerPetowner(petOwner);
        } catch (EmailExistException e) {
            bindingResult.addError(new FieldError("petOwner", "petOwner.user", e.getMessage()));
            return new ModelAndView("registerOwner", "petOwner", petOwner);
        }

        return new ModelAndView("redirect:/login");
    }
}
