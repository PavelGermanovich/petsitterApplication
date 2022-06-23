package com.germanovich.springboot.petsitterApp.entity;

import com.germanovich.springboot.petsitterApp.entity.key.PetsitterServiceKey;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "petsitter_service_provide")
@Data
public class PetsitterServiceCost {
    @EmbeddedId
    private PetsitterServiceKey id;

    @ManyToOne
    @MapsId("petsitterFk")
    @JoinColumn(name = "petsitter_fk")
    private PetSitter petSitter;

    @ManyToOne
    @MapsId("serviceFk")
    @JoinColumn(name = "service_fk")
    private Service service;

    @Column(name = "cost_per_unit")
    private int costForServicePerUnit;
}
