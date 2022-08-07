package com.germanovich.springboot.petsitterApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "order_sent")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderApproved {
    @Id
    @GenericGenerator(name = "auto", strategy = "increment")
    @GeneratedValue(generator = "auto")
    private int id;

    @ManyToOne()
    @JoinColumn(name = "petsitter_fk")
    private PetSitter petsitterFk;

    @ManyToOne()
    @JoinColumn(name = "petowner_fk")
    private PetOwner petownerFk;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_time")
    private LocalDate startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_time")
    private LocalDate endTime;

    @Column(name = "price")
    private Integer price;

    @Column(name = "units")
    private Integer unitsNumber;

    @ManyToOne()
    @JoinColumn(name = "pet_type_fk")
    private PetType petType;

//    @Lob
//    @Column(name = "notes")
//    private String notes;

    @ManyToOne()
    @JoinColumn(name = "status_fk")
    private OrderStatus statusFk;

    @ManyToOne()
    @JoinColumn(name = "service_fk")
    private Service serviceFk;
}
