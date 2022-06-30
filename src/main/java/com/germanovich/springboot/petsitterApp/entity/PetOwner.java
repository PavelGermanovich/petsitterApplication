package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity(name = "petowner")
@Data()
public class PetOwner {
    @Id
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_fk")
    private User user;

    @OneToMany(mappedBy = "petOwner", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Pet> pet;

    @OneToMany(mappedBy = "petOwner")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OrderPlanned> plannedOrders;
}
