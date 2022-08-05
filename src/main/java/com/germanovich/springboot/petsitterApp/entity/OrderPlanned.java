package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_time_planned")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_time_planned")
    private LocalDate endDate;

    private int units;



    /**
     * toDo add NF to add details to a booking order
     */
//    @Column(columnDefinition = "LONGTEXT", name = "notes")
//    private String notes;
}
