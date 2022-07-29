package com.germanovich.springboot.petsitterApp.service;

import com.germanovich.springboot.petsitterApp.dao.*;
import com.germanovich.springboot.petsitterApp.dto.PetsitterProfileDto;
import com.germanovich.springboot.petsitterApp.entity.PetSitter;
import com.germanovich.springboot.petsitterApp.entity.PetsitterServiceCost;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void updatePetsitterProfile(PetsitterProfileDto petsitterProfileDto) {

        convertPetsitterProfileDtoToPetsitter(petsitterProfileDto);

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

        if (petsitterProfileDto.getPetSittingProvided()) {
            //todo add logic here
        }

        return null;
    }
}
