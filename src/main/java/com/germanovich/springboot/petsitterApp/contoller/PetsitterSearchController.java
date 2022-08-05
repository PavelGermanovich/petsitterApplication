package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.PetSitterServiceCostRepository;
import com.germanovich.springboot.petsitterApp.dao.PetsitterRepository;
import com.germanovich.springboot.petsitterApp.dto.BasicPetsitterSearchDto;
import com.germanovich.springboot.petsitterApp.dto.PetsitterSearchResultDto;
import com.germanovich.springboot.petsitterApp.entity.PetSitter;
import com.germanovich.springboot.petsitterApp.entity.PetType;
import com.germanovich.springboot.petsitterApp.entity.PetsitterServiceCost;
import com.germanovich.springboot.petsitterApp.enums.PETSITTER_SERVICE;
import com.germanovich.springboot.petsitterApp.enums.PetTypeEnum;
import com.germanovich.springboot.petsitterApp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/searchPetsitter")
    public String searchPetsitter(BasicPetsitterSearchDto basicPetsitterSearchDto, Model model) {
        List<PetsitterSearchResultDto> petsitterSearchResultList = searchService.getPetsittersBasedOnSearch(basicPetsitterSearchDto);
        //toDo add validation to the search, add specifications for different search options

        model.addAttribute("petsittersList", petsitterSearchResultList);
        model.addAttribute("petsitterSearchDto", basicPetsitterSearchDto);
        return "searchResult";
    }
}
