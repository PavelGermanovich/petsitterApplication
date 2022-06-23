package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class City {
    @Id
    @Column(name = "id_city")
    private int id;
    private String name;

}
