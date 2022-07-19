package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Pet {
    @Id
    @Column(name = "pet_pk")
    private int id;
    @Column(name = "petname")
    private String petName;

    @ManyToOne
    @JoinColumn(name = "pettype")
    private PetType petType;
    private int size;
    private int age;

    @ManyToOne
    @JoinColumn(name = "owner_fk")
    private PetOwner petOwner;
    @Column(columnDefinition = "LONGTEXT", name = "description")
    private String description;
}
