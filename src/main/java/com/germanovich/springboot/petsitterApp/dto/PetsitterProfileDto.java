package com.germanovich.springboot.petsitterApp.dto;

import com.germanovich.springboot.petsitterApp.entity.City;
import com.germanovich.springboot.petsitterApp.entity.PetSitter;
import com.germanovich.springboot.petsitterApp.enums.PETSITTER_SERVICE;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class PetsitterProfileDto {
    private int id;
    private String nameFirst;
    private String nameLast;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    private String phone;
    private int cityId;
    private String email;
    private int fileDbId;
    private String description;
    private int petSizeLimitId;

    private boolean isPetsittingProvided;
    private boolean isDogWalkingProvided;

    private int costInHourDogWalking;
    private int costInDayPetsitting;

    public static PetSitter convertPetsitterProfileDto(PetsitterProfileDto petsitterProfileDto) {
        return null;
    }

    public static PetsitterProfileDto convertPetsitterProfileDto(PetSitter petSitter) {
        PetsitterProfileDto petsitterProfileDto = new PetsitterProfileDto();
        petsitterProfileDto.setId(petSitter.getId());
        petsitterProfileDto.setNameFirst(petSitter.getUser().getNameFirst());
        petsitterProfileDto.setNameLast(petSitter.getUser().getNameLast());
        petsitterProfileDto.setBirthdate(petSitter.getUser().getBirthdate());
        petsitterProfileDto.setPhone(petSitter.getUser().getPhone());
        petsitterProfileDto.setCityId(petSitter.getUser().getCity().getId());
        petsitterProfileDto.setEmail(petSitter.getUser().getEmail());
        petsitterProfileDto.setFileDbId(petSitter.getUser().getFileDb().getId());
        petsitterProfileDto.setDescription(petSitter.getDescription());
        petsitterProfileDto.setPetSizeLimitId(petSitter.getPetSizeLimtis().getId());

        if (petSitter.getServiceProvidedWithCost().stream()
                .anyMatch(x -> x.getService().getServiceName().equals(PETSITTER_SERVICE.SITTING.getRoleName()))) {
            petsitterProfileDto.setPetsittingProvided(true);
            petsitterProfileDto.setCostInDayPetsitting(petSitter.getServiceProvidedWithCost().stream()
                    .filter(x -> x.getService().getServiceName().equals(PETSITTER_SERVICE.SITTING.getRoleName())).findFirst().get().getCostForServicePerUnit());
        }

        if (petSitter.getServiceProvidedWithCost().stream()
                .anyMatch(x -> x.getService().getServiceName().equals(PETSITTER_SERVICE.WALKING.getRoleName()))) {
            petsitterProfileDto.setDogWalkingProvided(true);
            petsitterProfileDto.setCostInHourDogWalking(petSitter.getServiceProvidedWithCost().stream()
                    .filter(x -> x.getService().getServiceName().equals(PETSITTER_SERVICE.WALKING.getRoleName())).findFirst().get().getCostForServicePerUnit());
        }


        return petsitterProfileDto;
    }
}
