package com.germanovich.springboot.petsitterApp.service;

import com.germanovich.springboot.petsitterApp.dao.PetSitterServiceCostRepository;
import com.germanovich.springboot.petsitterApp.dao.PetsitterRepository;
import com.germanovich.springboot.petsitterApp.dto.BasicPetsitterSearchDto;
import com.germanovich.springboot.petsitterApp.dto.PetsitterSearchResultDto;
import com.germanovich.springboot.petsitterApp.entity.PetsitterServiceCost;
import com.germanovich.springboot.petsitterApp.enums.ORDER_STATUS_ENUM;
import com.germanovich.springboot.petsitterApp.enums.PETSITTER_SERVICE;
import com.germanovich.springboot.petsitterApp.enums.PET_TYPE_Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    private PetsitterRepository petsitterRepository;

    @Autowired
    private PetSitterServiceCostRepository petSitterServiceCostRepository;

    public List<PetsitterSearchResultDto> getPetsittersBasedOnSearch(BasicPetsitterSearchDto basicPetsitterSearchDto) {
        if (basicPetsitterSearchDto.getPetsitterService().equals(PETSITTER_SERVICE.SITTING)) {
            return getPetsitterForSitting(basicPetsitterSearchDto);
        } else {
            return getPetsitterForWalking(basicPetsitterSearchDto);
        }
    }

    public List<PetsitterSearchResultDto> getPetsitterForSitting(BasicPetsitterSearchDto basicPetsitterSearchDto) {

        List<PetsitterServiceCost> petsittersWithCost = petSitterServiceCostRepository
                .findPetsitterByServiceName(basicPetsitterSearchDto.getPetsitterService().getRoleName());

        petsittersWithCost = petsittersWithCost.stream().filter(x -> x.getPetSitter().getUser().getCity().getId() == (basicPetsitterSearchDto.getCity().getId())).
                filter(x -> x.getPetSitter().getPetSizeLimtis().getSizeMax() >= basicPetsitterSearchDto.getPetSize())
                .collect(Collectors.toList());

        petsittersWithCost = filterOrderPlannedForSitting(basicPetsitterSearchDto, petsittersWithCost);
        petsittersWithCost = filterOrderApprovedForSitting(basicPetsitterSearchDto, petsittersWithCost);

        if (basicPetsitterSearchDto.getPetsitterService().getRoleName().equals(PETSITTER_SERVICE.SITTING.getRoleName())) {
            if (basicPetsitterSearchDto.getPetType().getName().equals(PET_TYPE_Enum.CAT.getName())) {
                petsittersWithCost = petsittersWithCost.stream().filter(pS -> pS.getPetSitter().getPetsittingDetails().getIsCatWanted()).collect(Collectors.toList());
            } else {
                petsittersWithCost = petsittersWithCost.stream().filter(pS -> pS.getPetSitter().getPetsittingDetails().getIsDogWanted()).collect(Collectors.toList());
            }
        }

        return petsittersWithCost.stream()
                .map(SearchService::convertPetsitterWithServiceCostToPetsitterSearchRst).collect(Collectors.toList());
    }

    private List<PetsitterServiceCost> filterOrderPlannedForSitting(BasicPetsitterSearchDto basicPetsitterSearchDto,
                                                                    List<PetsitterServiceCost> petsittersWithCost) {
        petsittersWithCost = petsittersWithCost.stream().filter(x -> x.getPetSitter().getPlannedOrders().stream()
                .filter(order -> order.getService().getServiceName().equals(basicPetsitterSearchDto
                        .getPetsitterService().getRoleName()) && order.getOrderStatus().getStatusName()
                        .equals(ORDER_STATUS_ENUM.SUBMITTED.getName()))
                .noneMatch(order -> order.getStartDate().isBefore(basicPetsitterSearchDto.getEndDate()) &&
                        order.getEndDate().isAfter(basicPetsitterSearchDto.getStartDate()))).collect(Collectors.toList());
        return petsittersWithCost;
    }

    private List<PetsitterServiceCost> filterOrderApprovedForSitting(BasicPetsitterSearchDto basicPetsitterSearchDto,
                                                                    List<PetsitterServiceCost> petsittersWithCost) {
        petsittersWithCost = petsittersWithCost.stream().filter(x -> x.getPetSitter().getApprovedOrders().stream()
                .filter(order -> order.getService().getServiceName().equals(basicPetsitterSearchDto
                        .getPetsitterService().getRoleName()) && order.getStatus().getStatusName()
                        .equals(ORDER_STATUS_ENUM.APPROVED.getName()))
                .noneMatch(order -> order.getStartTime().isBefore(basicPetsitterSearchDto.getEndDate()) &&
                        order.getEndTime().isAfter(basicPetsitterSearchDto.getStartDate()))).collect(Collectors.toList());
        return petsittersWithCost;
    }

    public List<PetsitterSearchResultDto> getPetsitterForWalking(BasicPetsitterSearchDto basicPetsitterSearchDto) {
        List<PetsitterServiceCost> petsittersWithCost = petSitterServiceCostRepository
                .findPetsitterByServiceName(basicPetsitterSearchDto.getPetsitterService().getRoleName());

        petsittersWithCost = petsittersWithCost.stream().filter(x -> x.getPetSitter().getUser().getCity().getId() == (basicPetsitterSearchDto.getCity().getId())).
                filter(x -> x.getPetSitter().getPetSizeLimtis().getSizeMax() >= basicPetsitterSearchDto.getPetSize())
                .collect(Collectors.toList());

        petsittersWithCost = petsittersWithCost.stream().filter(x -> x.getPetSitter().getPlannedOrders().stream()
                .noneMatch(order -> order.getStartDate().isEqual(basicPetsitterSearchDto.getStartDate()))).collect(Collectors.toList());

        return petsittersWithCost.stream()
                .map(SearchService::convertPetsitterWithServiceCostToPetsitterSearchRst).collect(Collectors.toList());
    }

    public static PetsitterSearchResultDto convertPetsitterWithServiceCostToPetsitterSearchRst(PetsitterServiceCost petsitterServiceCost) {
        PetsitterSearchResultDto petsitterSearchResultDto = new PetsitterSearchResultDto();
        petsitterSearchResultDto.setPetsitterId(petsitterServiceCost.getPetSitter().getId());
        petsitterSearchResultDto.setPetsitterName(petsitterServiceCost.getPetSitter().getUser().getNameFirst());
        petsitterSearchResultDto.setCostForServicePerUnit(petsitterServiceCost.getCostForServicePerUnit());
        petsitterSearchResultDto.setCity(petsitterServiceCost.getPetSitter().getUser().getCity().getName());
        petsitterSearchResultDto.setFileDbId(petsitterServiceCost.getPetSitter().getUser().getFileDb().getId());

        return petsitterSearchResultDto;
    }
}
