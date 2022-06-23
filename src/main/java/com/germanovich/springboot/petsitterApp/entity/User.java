package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class User {
    @Id
    @Column(name = "id_user")
    private int id;

    private String login;
    private String password;
    @Column(name = "namefirst")
    private String nameFirst;
    @Column(name = "namelast")
    private String nameLast;
    private LocalDate birthdate;
    @Column(name = "created_date")
    private LocalDate createdDate;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "city_fk")
    private City city;

    private String email;

    @ManyToOne
    @JoinColumn(name = "user_role_fk")
    private UserRole userRole;
}
