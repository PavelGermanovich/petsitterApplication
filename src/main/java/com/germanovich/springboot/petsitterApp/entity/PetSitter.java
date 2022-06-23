package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "petsitter")
@Data
public class PetSitter {
    @Id
    @Column(name = "petsitter_pk")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_fk")
    private User user;

    private String description;

    @OneToMany(mappedBy = "petSitter")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PetsitterServiceCost> serviceProvidedWithCost;

    @OneToMany(mappedBy = "petSitter")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OrderPlanned> plannedOrders;
}