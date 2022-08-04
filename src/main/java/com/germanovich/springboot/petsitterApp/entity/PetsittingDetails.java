package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity(name = "petsit_service_details")
@Data
public class PetsittingDetails {
    @Id
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "petsitter_fk")
    private PetSitter petSitter;

    @Column(name = "dog_wanted", nullable = false, columnDefinition = "BIT")
    @Type(type = "numeric_boolean")
    private Boolean isDogWanted;

    @Column(name = "cat_wanted", nullable = false, columnDefinition = "BIT")
    @Type(type = "numeric_boolean")
    private Boolean isCatWanted;
}
