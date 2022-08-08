package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.CityRepository;
import com.germanovich.springboot.petsitterApp.dao.PetRepository;
import com.germanovich.springboot.petsitterApp.dao.PetSizeLimitRepository;
import com.germanovich.springboot.petsitterApp.dao.PetTypeRepository;
import com.germanovich.springboot.petsitterApp.dto.BasicPetsitterSearchDto;
import com.germanovich.springboot.petsitterApp.entity.City;
import com.germanovich.springboot.petsitterApp.entity.PetSizeLimit;
import com.germanovich.springboot.petsitterApp.entity.PetType;
import com.germanovich.springboot.petsitterApp.service.DebugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class InitialContoller {
    @Autowired
    private DebugService debugService;
    @Autowired
    private PetTypeRepository petTypeRepository;
    @Autowired
    private PetSizeLimitRepository petSizeLimitRepository;
    @Autowired
    private PetRepository petRepository;


    @Autowired
    private CityRepository cityRepository;

    private List<PetType> getPetType() {
        return StreamSupport.stream(petTypeRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    private List<City> getCityList() {
        return StreamSupport.stream(cityRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    private List<PetSizeLimit> getPetSizeLimits() {
        return StreamSupport.stream(petSizeLimitRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @RequestMapping("/")
    public String homePage(Model model) {
        model.addAttribute("petTypeList", getPetType());
        model.addAttribute("sizeLimits", getPetSizeLimits());
        model.addAttribute("cityList", getCityList());
        model.addAttribute("searchPetsitter", new BasicPetsitterSearchDto());
        return "home";
    }

}
