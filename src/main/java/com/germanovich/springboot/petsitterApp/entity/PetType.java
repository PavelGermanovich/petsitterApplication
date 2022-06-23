package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "pet_type")
@Data
public class PetType {
    @Id
    @Column(name = "pet_type_pk")
    private int id;
    private String name;

    @ManyToMany
    @JoinTable(name = "service_available_pettype", joinColumns = @JoinColumn(name = "pettype_fk"),
            inverseJoinColumns = @JoinColumn(name = "service_fk"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Service> serviceAvailable;
}
