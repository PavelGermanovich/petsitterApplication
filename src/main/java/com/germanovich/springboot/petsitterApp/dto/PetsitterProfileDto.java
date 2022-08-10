package com.germanovich.springboot.petsitterApp.dto;

import com.germanovich.springboot.petsitterApp.entity.Petsitter;
import com.germanovich.springboot.petsitterApp.enums.PETSITTER_SERVICE;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

@Data
public class PetsitterProfileDto {
    private int id;

    @NotEmpty(message = "name should be provided")
    private String nameFirst;
    @NotEmpty(message = "last name should be provided")
    private String nameLast;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    @NotEmpty(message = "Phone is required")
    private String phone;
    @NotNull(message = "City must be specified")
    private int cityId;
    private String email;
    private int fileDbId;
    @NotEmpty(message = "description is required")
    private String description;

    @NotNull(message = "pet size limit should be provided")
    private int petSizeLimitId;

    private Boolean petSittingProvided;
    private Boolean dogWalkingProvided;

    private Boolean dogPetsitted;
    private Boolean catPetsitted;
    private int costInHourDogWalking;
    private int costInDayPetsitting;

    public static PetsitterProfileDto convertPetsitterProfileDtoFromPetsitter(Petsitter petSitter) {
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
            petsitterProfileDto.setPetSittingProvided(true);
            petsitterProfileDto.setCostInDayPetsitting(petSitter.getServiceProvidedWithCost().stream()
                    .filter(x -> x.getService().getServiceName().equals(PETSITTER_SERVICE
                            .SITTING.getRoleName())).findFirst().get().getCostForServicePerUnit());

            if (Optional.ofNullable(petSitter.getPetsittingDetails()).isEmpty()) {
                petsitterProfileDto.setDogPetsitted(false);
                petsitterProfileDto.setCatPetsitted(false);
            } else {
                petsitterProfileDto.setDogPetsitted(petSitter.getPetsittingDetails().getIsDogWanted());
                petsitterProfileDto.setCatPetsitted(petSitter.getPetsittingDetails().getIsCatWanted());
            }
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
