package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pet_size_limit")
@Data
public class PetSizeLimit {
    @Id
    @Column(name = "pet_size_limit_pk")
    private int id;

    private String name;
    @Column(name = "size_max")
    private int sizeMax;
}
