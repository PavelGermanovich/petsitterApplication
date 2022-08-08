package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "order_planned")
@Data
public class OrderSubmitted {

    @Id
    @Column(name = "order_pk")
    @GenericGenerator(name = "auto", strategy = "increment")
    @GeneratedValue(generator = "auto")
    private int id;

    @ManyToOne
    @JoinColumn(name = "service_fk")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "status_fk")
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "petsitter_fk")
    private PetSitter petSitter;

    @ManyToOne
    @JoinColumn(name = "petowner_fk")
    private PetOwner petOwner;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_time_planned")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_time_planned")
    private LocalDate endDate;

    private int units;

    @Column(name = "planned_price")
    private int plannedPrice;

    @ManyToOne()
    @JoinColumn(name = "pet_type_fk")
    private PetType petType;

    /**
     * toDo add NF to add details to a booking order
     */
//    @Column(columnDefinition = "LONGTEXT", name = "notes")
//    private String notes;
}
