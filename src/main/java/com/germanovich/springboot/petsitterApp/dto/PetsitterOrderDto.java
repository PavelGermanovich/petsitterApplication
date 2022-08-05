package com.germanovich.springboot.petsitterApp.dto;

import com.germanovich.springboot.petsitterApp.enums.PETSITTER_SERVICE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetsitterOrderDto {
    private int petsitterId;
    private int petownerId;
    private PETSITTER_SERVICE service;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}
