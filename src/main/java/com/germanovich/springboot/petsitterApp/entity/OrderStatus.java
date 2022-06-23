package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "order_status")
@Data
public class OrderStatus {
    @Id
    @Column(name = "status_pk")
    private int id;
    @Column(name = "status_name")
    private String statusName;
}
