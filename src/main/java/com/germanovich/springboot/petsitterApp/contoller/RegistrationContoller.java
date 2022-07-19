package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.CityRepository;
import com.germanovich.springboot.petsitterApp.dao.PetSitterServiceCostRepository;
import com.germanovich.springboot.petsitterApp.dao.ServiceRepository;
import com.germanovich.springboot.petsitterApp.dao.UserRoleRepository;
import com.germanovich.springboot.petsitterApp.dao.validation.OnCreate;
import com.germanovich.springboot.petsitterApp.entity.*;
import com.germanovich.springboot.petsitterApp.enums.PETSITTER_SERVICE;
import com.germanovich.springboot.petsitterApp.enums.USER_ROLE;
import com.germanovich.springboot.petsitterApp.service.FileStorageService;
import com.germanovich.springboot.petsitterApp.service.UserService;
import com.germanovich.springboot.petsitterApp.validation.EmailExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @ModelAttribute
    private List<City> getCityList() {
        return StreamSupport.stream(cityRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("registrationTypeSelect", "user", new User());
    }

    @PostMapping(value = "/petsitter/register")
    public ModelAndView registerPetsitter(@Valid PetSitter petSitter,
                                          @ModelAttribute("petwalkgingCheckbox") String petwalkgingCheckbox,
                                          @ModelAttribute("petsitCheckbox") String petsitCheckbox, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("registerPetsitter", "petSitter", petSitter);
        }

        petSitter.getUser().setUserRole(userRoleRepository.findByRoleId(USER_ROLE.PET_SITTER));
        try {
            petSitter = userService.registerPetsitter(petSitter);
        } catch (EmailExistException e) {
            bindingResult.addError(new FieldError("petSitter", "petSitter.user", e.getMessage()));
            return new ModelAndView("registerPetsitter", "petSitter", petSitter);
        }

        if (!petwalkgingCheckbox.isEmpty()) {
            PetsitterServiceCost petsitterServiceCost = new PetsitterServiceCost();
            petsitterServiceCost.setService(serviceRepository.findServiceByServiceName(PETSITTER_SERVICE.WALKING.getRoleName()));
            petsitterServiceCost.setPetSitter(petSitter);
            petSitterServiceCostRepository.save(petsitterServiceCost);

        }

        if (!petsitCheckbox.isEmpty()) {
            PetsitterServiceCost petsitterServiceCost = new PetsitterServiceCost();
            petsitterServiceCost.setService(serviceRepository.findServiceByServiceName(PETSITTER_SERVICE.SITTING.getRoleName()));
            petsitterServiceCost.setPetSitter(petSitter);
            petSitterServiceCostRepository.save(petsitterServiceCost);
        }

        return new ModelAndView("redirect:/login");
    }

    @Validated(OnCreate.class)
    @PostMapping(value = "/petowner/register")
    public ModelAndView registerPetowner(@Valid final PetOwner petOwner, final BindingResult bindingResult,
                                         @RequestParam("file") MultipartFile multipartFile) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("registerOwnerBootstrap", "petOwner", petOwner);
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
            return new ModelAndView("registerOwnerBootstrap", "petOwner", petOwner);
        }

        return new ModelAndView("redirect:/login");
    }
}
