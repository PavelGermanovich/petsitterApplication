package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Set;

@Entity(name = "petsitter")
@Data
public class PetSitter {
    @Id
    @Column(name = "petsitter_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_fk")
    @Valid
    private User user;

    private String description;

    @OneToMany(mappedBy = "petSitter", cascade=CascadeType.ALL, orphanRemoval=true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PetsitterServiceCost> serviceProvidedWithCost;

    @OneToMany(mappedBy = "petSitter", cascade=CascadeType.ALL, orphanRemoval=true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OrderPlanned> plannedOrders;

    @ManyToOne
    @JoinColumn(name = "pet_size_limit_fk")
    private PetSizeLimit petSizeLimtis;

    @OneToOne(mappedBy = "petSitter", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    PetsittingDetails petsittingDetails;
}