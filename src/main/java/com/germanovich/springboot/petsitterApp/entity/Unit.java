package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Unit {
    @Id
    @Column(name = "unit_pk")
    private int id;
    @Column(name = "unit_name")
    private String unitName;
}
