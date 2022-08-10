package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.*;
import com.germanovich.springboot.petsitterApp.dto.BasicPetsitterSearchDto;
import com.germanovich.springboot.petsitterApp.dto.PetsitterOrderDto;
import com.germanovich.springboot.petsitterApp.dto.PetsitterProfileDto;
import com.germanovich.springboot.petsitterApp.entity.*;
import com.germanovich.springboot.petsitterApp.enums.USER_ROLE;
import com.germanovich.springboot.petsitterApp.service.FileStorageService;
import com.germanovich.springboot.petsitterApp.service.PetsitterProfileService;
import com.germanovich.springboot.petsitterApp.service.UserService;
import com.germanovich.springboot.petsitterApp.validation.EmailExistException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.io.*;
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

    @Autowired
    private PetSizeLimitRepository petSizeLimitRepository;

    @Autowired
    private PetsitterProfileService petsitterProfileService;

    @ModelAttribute(name = "cityList")
    public List<City> getCityList() {
        return StreamSupport.stream(cityRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<PetSizeLimit> getPetSizeLimits() {
        return StreamSupport.stream(petSizeLimitRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }


    private String getOwnerCommonData(Principal principal, Model model) {
        PetOwner petOwner = petOwnerRepository.findPetOwnerByUserEmail(principal.getName());
        model.addAttribute("petowner", petOwner);
        return "petownerProfile";

    }

    private String getSitterCommonDataProfile(Principal principal, Model model) {
        Petsitter petSitter = petsitterRepository.findPetSitterByUserEmail(principal.getName());
        PetsitterProfileDto petsitterProfileDto = PetsitterProfileDto.convertPetsitterProfileDtoFromPetsitter(petSitter);
        model.addAttribute("sizeLimits", getPetSizeLimits());
        model.addAttribute("petsitterProfileDto", petsitterProfileDto);
        return "petsitterProfile";

    }

    @GetMapping("/user")
    public String getUserData(Principal principal, Model model) {
        if (((UsernamePasswordAuthenticationToken) principal).getAuthorities().stream()
                .anyMatch(x -> x.getAuthority().equals(USER_ROLE.PET_SITTER.toString()))) {
            return getSitterCommonDataProfile(principal, model);
        } else {
            return getOwnerCommonData(principal, model);
        }
    }

    @GetMapping("/profile/image/{id}")
    public void showProductImage(@PathVariable int id,
                                 HttpServletResponse response) throws IOException {
        InputStream in;
        if (id == -1) {
            in = new BufferedInputStream(new FileInputStream("src/main/resources/static/images/profileImage.jpg"));
            response.setContentType("image/jpg");
        } else {
            FileDb fileDb = fileRepository.getById(id);
            in = new ByteArrayInputStream(fileDb.getData());
            response.setContentType(fileDb.getType());
        }

        IOUtils.copy(in, response.getOutputStream());
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

        Set<ConstraintViolation<PetOwner>> violations = validator.validate(petOwnerExisting);

        if (!violations.isEmpty()) {
            model.addAttribute("message", "Failed");
            return new ModelAndView("redirect:/user");
        }

        try {
            userService.updatePetowner(petOwnerExisting);
        } catch (EmailExistException e) {
            bindingResult.addError(new FieldError("petOwner", "petOwner.user", e.getMessage()));
            model.addAttribute("message", "Failed");
            return new ModelAndView("registerOwner", "petOwner", petownerForm);
        }


        return new ModelAndView("redirect:/user");
    }

    @PostMapping(value = "/updateProfileImage")
    public String updateProfileImage(@RequestParam("file") MultipartFile multipartFile, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        try {
            user.setFileDb(fileStorageService.store(multipartFile));
        } catch (IOException e) {
            //toDo add error here in case failure
            e.printStackTrace();
        }
        userRepository.save(user);
        return "redirect:/user";
    }

    @PostMapping(value = "/changePassword")
    public ModelAndView changePassword() {
        //toDo add change password here
        return new ModelAndView("petownerProfile");
    }

    @PostMapping(value = "/updatePetsitter")
    public ModelAndView updatePetsitter(@Valid PetsitterProfileDto petsitterProfileDto, BindingResult bindingResult,
                                        Model model) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("petsitterProfile", "petsitterProfileDto", petsitterProfileDto);
        }
        Petsitter petSitter = petsitterProfileService.updatePetsitterProfile(petsitterProfileDto);

        return new ModelAndView("redirect:/user");
    }
}
