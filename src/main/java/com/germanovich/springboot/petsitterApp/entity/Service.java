package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "service")
@Data
public class Service {
    @Id
    @Column(name = "service_fk")
    private int id;
    @Column(name = "service_name")
    private String serviceName;

    @ManyToOne
    @JoinColumn(name = "unit_fk")
    private Unit unit;

    @ManyToMany(mappedBy = "serviceAvailable")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PetType> petForWhichServiceAvailable;

    @OneToMany(mappedBy = "service")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PetsitterServiceCost> petsitterProvideServiceWithCost;

    @OneToMany(mappedBy = "service")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OrderSubmitted> plannedService;
}
