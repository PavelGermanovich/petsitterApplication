package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.PetsitterRepository;
import com.germanovich.springboot.petsitterApp.dto.BasicPetsitterSearchDto;
import com.germanovich.springboot.petsitterApp.entity.PetSitter;
import com.germanovich.springboot.petsitterApp.entity.PetType;
import com.germanovich.springboot.petsitterApp.enums.PETSITTER_SERVICE;
import com.germanovich.springboot.petsitterApp.enums.PetTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class PetsitterSearchController {
    @Autowired
    private PetsitterRepository petsitterRepository;

    @GetMapping("/searchPetsitter")
    public String searchPetsitter(BasicPetsitterSearchDto basicPetsitterSearchDto, Model model) {
        List<PetSitter> petsitterListResult;
        List<PetSitter> petsitters =  StreamSupport.stream(petsitterRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        petsitters = petsitters.stream().filter(x -> x.getUser().getCity().getId() == (basicPetsitterSearchDto.getCity().getId())).
                filter(x -> x.getPetSizeLimtis().getId() == basicPetsitterSearchDto.getPetSizeLimit())
                .filter(x -> x.getServiceProvidedWithCost().stream().anyMatch(service -> service.getService()
                        .getServiceName().equals(basicPetsitterSearchDto.getPetsitterService()))).collect(Collectors.toList());

        //toDo скорее всего тут даты не включительно проверяются, нужно будет проверить
        petsitters =  petsitters.stream().filter(x -> x.getPlannedOrders().stream()
                .noneMatch(order -> order.getStartDate().isBefore(basicPetsitterSearchDto.getEndDate()) &&
                        order.getEndDate().isAfter(basicPetsitterSearchDto.getStartDate()))).collect(Collectors.toList());

        if (basicPetsitterSearchDto.getPetsitterService().getRoleName().equals(PETSITTER_SERVICE.SITTING.getRoleName())) {
            if (basicPetsitterSearchDto.getPetType().getName().equals(PetTypeEnum.CAT.getName())) {
                petsitters = petsitters.stream().filter(pS -> pS.getPetsittingDetails().getIsCatWanted()).collect(Collectors.toList());
            } else {
                petsitters = petsitters.stream().filter(pS -> pS.getPetsittingDetails().getIsDogWanted()).collect(Collectors.toList());
            }
        }

        model.addAttribute("petsittersList", petsitters);
        model.addAttribute("petsitterSearchDto", basicPetsitterSearchDto);
        return "searchResult";
    }
}
