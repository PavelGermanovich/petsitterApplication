package com.germanovich.springboot.petsitterApp.dto;

import com.germanovich.springboot.petsitterApp.entity.City;
import com.germanovich.springboot.petsitterApp.entity.PetType;
import com.germanovich.springboot.petsitterApp.enums.PETSITTER_SERVICE;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class BasicPetsitterSearchDto {
    private PetType petType;
    private int petSizeLimit;
    private PETSITTER_SERVICE petsitterService;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private City city;
}
