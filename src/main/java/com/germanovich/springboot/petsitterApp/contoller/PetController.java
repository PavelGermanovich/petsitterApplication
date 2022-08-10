package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.PetOwnerRepository;
import com.germanovich.springboot.petsitterApp.dao.PetRepository;
import com.germanovich.springboot.petsitterApp.dao.PetTypeRepository;
import com.germanovich.springboot.petsitterApp.entity.FileDb;
import com.germanovich.springboot.petsitterApp.entity.Pet;
import com.germanovich.springboot.petsitterApp.entity.PetOwner;
import com.germanovich.springboot.petsitterApp.entity.PetType;
import com.germanovich.springboot.petsitterApp.service.FileStorageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/pet")
public class PetController {
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PetOwnerRepository petOwnerRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private PetTypeRepository petTypeRepository;

    private List<PetType> getPetType() {
        return StreamSupport.stream(petTypeRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @PostMapping("/createPet")
    public String createPet(@Valid Pet pet, Principal principal, Model model,
                            @RequestParam("petImage") MultipartFile multipartFile, RedirectAttributes redirectAttrs) {
        PetOwner petOwner = petOwnerRepository.findPetOwnerByUserEmail(principal.getName());
        try {
            if (!multipartFile.isEmpty()) {
                pet.setFileDb(fileStorageService.store(multipartFile));
            }
            pet.setPetOwner(petOwner);
            petOwner.getPetList().add(pet);
            petRepository.save(pet);
        } catch (Exception e) {
            //ToDo add exceptions here
            redirectAttrs.addFlashAttribute("messagePetCreated", "Failed");
            return "redirect:/user";
        }
        //ToDo add validation

        redirectAttrs.addFlashAttribute("messagePetCreated", "Success");
        return "redirect:/user";
    }

    @GetMapping("/image/{id}")
    public void showPetImage(@PathVariable int id, HttpServletResponse response) throws IOException {
        InputStream in;
        if (id == -1) {
            in = new BufferedInputStream(new FileInputStream("src/main/resources/static/images/pet.png"));
            response.setContentType("image/png");
        } else {
            FileDb fileDb = fileStorageService.getFile(id);
            in = new ByteArrayInputStream(fileDb.getData());
            response.setContentType(fileDb.getType());
        }

        IOUtils.copy(in, response.getOutputStream());
    }

    @PostMapping("/image/{id}")
    public String updatePetImage(@PathVariable int id, Model model, @RequestParam("file") MultipartFile petImageNew) throws IOException {
        Pet pet = petRepository.findById(id).get();
        if (!petImageNew.isEmpty()) {
            try {
                pet.setFileDb(fileStorageService.updateImage(id, petImageNew));
                pet = petRepository.save(pet);
            } catch (Exception e) {
                //ToDo return errors here
            }
        }
        return "redirect:/pet?id=" + pet.getId();
    }

    @GetMapping
    public String showPetInfo(@RequestParam("id") int id, Model model) {
        Pet pet = petRepository.findById(id).get();
        model.addAttribute("pet", pet);
        model.addAttribute("petTypeList", getPetType());

        return "petInfomation";
    }

    @PostMapping
    public String updatePet(Pet pet, Model model) {
        Pet existingPet = petRepository.findById(pet.getId()).get();
        pet.setFileDb(existingPet.getFileDb());
        pet.setPetOwner(existingPet.getPetOwner());
        petRepository.save(pet);
        return "redirect:/pet?id=" + pet.getId();
    }

    @PostMapping("/remove")
    public String deletePet(@RequestParam int id, Model model) throws IOException {
        petRepository.deleteById(id);
        return "redirect:/user";
    }
}
