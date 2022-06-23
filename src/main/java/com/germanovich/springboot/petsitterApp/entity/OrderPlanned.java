package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "order_planned")
@Data
public class OrderPlanned {
    @Id
    @Column(name = "order_pk")
    private int id;

    @ManyToOne
    @JoinColumn(name = "service_fk")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "petsitter_fk")
    private PetSitter petSitter;

    @ManyToOne
    @JoinColumn(name = "petowner_fk")
    private PetOwner petOwner;

    private int units;

    @Column(columnDefinition = "LONGTEXT", name = "notes")
    private String notes;
}
