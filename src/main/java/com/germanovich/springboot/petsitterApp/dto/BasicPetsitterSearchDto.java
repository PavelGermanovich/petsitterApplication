package com.germanovich.springboot.petsitterApp.dto;

import com.germanovich.springboot.petsitterApp.entity.City;
import com.germanovich.springboot.petsitterApp.entity.PetType;
import com.germanovich.springboot.petsitterApp.enums.PETSITTER_SERVICE;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class BasicPetsitterSearchDto {
    private PetType petType;
    @NotNull(message = "Pet size should be specified")
    @Max(value = 140)
    private Integer petSize;
    private PETSITTER_SERVICE petsitterService;
    @NotNull(message = "Start date should be specified")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @NotNull(message = "City should be specified")
    private City city;
}
