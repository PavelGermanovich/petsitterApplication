package com.germanovich.springboot.petsitterApp.entity.key;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class PetsitterServiceKey implements Serializable {
    @Column(name = "petsitter_fk")
    private int petsitterFk;

    @Column(name = "service_fk")
    private int serviceFk;
}
