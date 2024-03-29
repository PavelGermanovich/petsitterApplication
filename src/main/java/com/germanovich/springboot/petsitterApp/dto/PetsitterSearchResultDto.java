package com.germanovich.springboot.petsitterApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetsitterSearchResultDto {
    private int petsitterId;
    private String petsitterName;
    private int costForServicePerUnit;
    private String city;
    private int fileDbId;
}
