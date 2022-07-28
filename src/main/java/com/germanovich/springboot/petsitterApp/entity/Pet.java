package com.germanovich.springboot.petsitterApp.entity;

import com.germanovich.springboot.petsitterApp.enums.PET_SEX;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Data
public class Pet {
    @Id
    @Column(name = "pet_pk")
    @GenericGenerator(name = "auto", strategy = "increment")
    @GeneratedValue(generator = "auto")
    private int id;
    @Column(name = "petname")
    private String petName;

    @ManyToOne
    @JoinColumn(name = "pettype")
    private PetType petType;
    private int size;
    private int age;
    private String breed;
    private String color;
    @Enumerated(EnumType.STRING)
    @Column(name = "sex_fk")
    private PET_SEX petSex;

    @Column(name = "has_veterinar_passport", nullable = false, columnDefinition = "BIT")
    @Type(type = "numeric_boolean")
    private boolean hasVeterinarPassport;

    @ManyToOne
    @JoinColumn(name = "owner_fk")
    private PetOwner petOwner;

    @Column(columnDefinition = "LONGTEXT", name = "description")
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_fk")
    private FileDb fileDb;
}
