package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.*;
import com.germanovich.springboot.petsitterApp.dto.BasicPetsitterSearchDto;
import com.germanovich.springboot.petsitterApp.dto.PetsitterOrderDto;
import com.germanovich.springboot.petsitterApp.dto.PetsitterSearchResultDto;
import com.germanovich.springboot.petsitterApp.entity.*;
import com.germanovich.springboot.petsitterApp.enums.PETSITTER_SERVICE;
import com.germanovich.springboot.petsitterApp.enums.PET_TYPE_Enum;
import com.germanovich.springboot.petsitterApp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class PetsitterSearchController {
    @Autowired
    private PetsitterRepository petsitterRepository;

    @Autowired
    private PetSitterServiceCostRepository petSitterServiceCostRepository;

    @Autowired
    private SearchService searchService;

    @Autowired
    private PetTypeRepository petTypeRepository;
    @Autowired
    private PetSizeLimitRepository petSizeLimitRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private ServiceRepository serviceRepository;


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

    private List<Service> getServiceList() {
        return StreamSupport.stream(serviceRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    private void addSearchAttr(Model model) {
        model.addAttribute("petTypeList", getPetType());
        model.addAttribute("sizeLimits", getPetSizeLimits());
        model.addAttribute("cityList", getCityList());
        model.addAttribute("serviceType", PETSITTER_SERVICE.values());
    }

    @RequestMapping("/")
    public String homePage(Model model) {
        addSearchAttr(model);
        model.addAttribute("searchPetsitter", new BasicPetsitterSearchDto());
        return "home";
    }

    @GetMapping("/searchPetsitter")
    public String searchPetsitter(@Valid @ModelAttribute("searchPetsitter") final BasicPetsitterSearchDto searchPetsitter,
                                  final BindingResult bindingResult, Model model) {
        addSearchAttr(model);
        if (searchPetsitter.getPetsitterService().equals(PETSITTER_SERVICE.WALKING)) {
            searchPetsitter.setPetType(petTypeRepository.findPetTypeByName(PET_TYPE_Enum.DOG.getName()));
        } else {
            if (searchPetsitter.getEndDate() == null || searchPetsitter.getStartDate().isAfter(searchPetsitter.getEndDate())) {
                bindingResult.addError(new FieldError("searchPetsitter", "endDate",
                        "End date incorrect"));
            }
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("searchPetsitter", searchPetsitter);
            return "home";
        }

        List<PetsitterSearchResultDto> petsitterSearchResultList = searchService.getPetsittersBasedOnSearch(searchPetsitter);
        model.addAttribute("petsittersList", petsitterSearchResultList);
        model.addAttribute("petsitterSearchDto", searchPetsitter);
        return "searchResult";
    }

    @GetMapping(value = "/profile/petsitterInfo/{id}")
    public String showPetsitterInfo(@PathVariable("id") int petsitterId, BasicPetsitterSearchDto basicPetsitterSearchDto, Model model) {
        Petsitter petsitterForBooking = petsitterRepository.findById(petsitterId).get();

        model.addAttribute("petsitterSearchDto", basicPetsitterSearchDto);
        model.addAttribute("petsitter", petsitterForBooking);
        model.addAttribute("petsitterOrder", new PetsitterOrderDto());
        return "petsitterBookingInfo";
    }
}
