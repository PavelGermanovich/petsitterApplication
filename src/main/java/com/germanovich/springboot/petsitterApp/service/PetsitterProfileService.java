package com.germanovich.springboot.petsitterApp.service;

import com.germanovich.springboot.petsitterApp.dao.*;
import com.germanovich.springboot.petsitterApp.dto.PetsitterProfileDto;
import com.germanovich.springboot.petsitterApp.entity.PetSitter;
import com.germanovich.springboot.petsitterApp.entity.PetsitterServiceCost;
import com.germanovich.springboot.petsitterApp.entity.PetsittingDetails;
import com.germanovich.springboot.petsitterApp.entity.key.PetsitterServiceKey;
import com.germanovich.springboot.petsitterApp.enums.PETSITTER_SERVICE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.validation.Validator;

@Service
public class PetsitterProfileService {
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
    private FileStorageService fileStorageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetSizeLimitRepository petSizeLimitRepository;

    @Autowired
    private PetSitterServiceCostRepository petSitterServiceCostRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public PetSitter updatePetsitterProfile(PetsitterProfileDto petsitterProfileDto) {
        PetSitter petSitter = convertPetsitterProfileDtoToPetsitter(petsitterProfileDto);
        return petsitterRepository.save(petSitter);
    }

    public PetSitter convertPetsitterProfileDtoToPetsitter(PetsitterProfileDto petsitterProfileDto) {
        PetSitter oldPetsitter = petsitterRepository.findById(petsitterProfileDto.getId()).get();

        oldPetsitter.getUser().setNameFirst(petsitterProfileDto.getNameFirst());
        oldPetsitter.getUser().setNameLast(petsitterProfileDto.getNameLast());
        oldPetsitter.getUser().setBirthdate(petsitterProfileDto.getBirthdate());
        oldPetsitter.getUser().setPhone(petsitterProfileDto.getPhone());
        oldPetsitter.getUser().setCity(cityRepository.findById(petsitterProfileDto.getCityId()).get());
        oldPetsitter.setDescription(petsitterProfileDto.getDescription());
        oldPetsitter.setPetSizeLimtis(petSizeLimitRepository.findById(petsitterProfileDto.getPetSizeLimitId()).get());

        boolean wasPetsittingProvided = oldPetsitter.getServiceProvidedWithCost().stream()
                .anyMatch(x -> x.getService().getServiceName().equals(PETSITTER_SERVICE.SITTING.getRoleName()));
        PetsitterServiceKey petsitterServiceKey = new PetsitterServiceKey();
        petsitterServiceKey.setPetsitterFk(oldPetsitter.getId());
        petsitterServiceKey.setServiceFk(serviceRepository
                .findServiceByServiceName(PETSITTER_SERVICE.SITTING.getRoleName()).getId());

        if (wasPetsittingProvided != petsitterProfileDto.getPetSittingProvided()) {
            if (petsitterProfileDto.getPetSittingProvided()) {
                PetsitterServiceCost petsitterServiceCost = new PetsitterServiceCost();
                petsitterServiceCost.setService(serviceRepository
                        .findServiceByServiceName(PETSITTER_SERVICE.SITTING.getRoleName()));
                petsitterServiceCost.setPetSitter(oldPetsitter);
                petsitterServiceCost.setCostForServicePerUnit(petsitterProfileDto.getCostInDayPetsitting());
                oldPetsitter.getServiceProvidedWithCost().add(petsitterServiceCost);
            } else {
                oldPetsitter.getServiceProvidedWithCost()
                        .remove(petSitterServiceCostRepository.findById(petsitterServiceKey).get());
            }
        } else if (petsitterProfileDto.getPetSittingProvided()) {
            oldPetsitter.getServiceProvidedWithCost().stream().filter(x -> x.getId().equals(petsitterServiceKey))
                    .findFirst().get().setCostForServicePerUnit(petsitterProfileDto.getCostInDayPetsitting());
        }

        boolean wasWalkingProvided = oldPetsitter.getServiceProvidedWithCost().stream()
                .anyMatch(x -> x.getService().getServiceName().equals(PETSITTER_SERVICE.WALKING.getRoleName()));
        PetsitterServiceKey petsitterServiceKeyWalking = new PetsitterServiceKey();
        petsitterServiceKeyWalking.setPetsitterFk(oldPetsitter.getId());
        petsitterServiceKeyWalking.setServiceFk(serviceRepository
                .findServiceByServiceName(PETSITTER_SERVICE.WALKING.getRoleName()).getId());


        if (wasWalkingProvided != petsitterProfileDto.getDogWalkingProvided()) {
            if (petsitterProfileDto.getDogWalkingProvided()) {
                PetsitterServiceCost petsitterServiceCost = new PetsitterServiceCost();
                petsitterServiceCost.setService(serviceRepository
                        .findServiceByServiceName(PETSITTER_SERVICE.WALKING.getRoleName()));
                petsitterServiceCost.setPetSitter(oldPetsitter);
                petsitterServiceCost.setCostForServicePerUnit(petsitterProfileDto.getCostInHourDogWalking());
                oldPetsitter.getServiceProvidedWithCost().add(petsitterServiceCost);
            } else {
                oldPetsitter.getServiceProvidedWithCost()
                        .remove(petSitterServiceCostRepository.findById(petsitterServiceKeyWalking).get());
            }
        } else if (petsitterProfileDto.getDogWalkingProvided()) {
            oldPetsitter.getServiceProvidedWithCost().stream().filter(x -> x.getId().equals(petsitterServiceKeyWalking))
                    .findFirst().get().setCostForServicePerUnit(petsitterProfileDto.getCostInHourDogWalking());
        }

        PetsittingDetails petsittingDetails = new PetsittingDetails();
        petsittingDetails.setIsCatWanted(petsitterProfileDto.getCatPetsitted());
        petsittingDetails.setIsDogWanted(petsitterProfileDto.getDogPetsitted());
        petsittingDetails.setPetSitter(oldPetsitter);
        oldPetsitter.setPetsittingDetails(petsittingDetails);
        return oldPetsitter;
    }
}
