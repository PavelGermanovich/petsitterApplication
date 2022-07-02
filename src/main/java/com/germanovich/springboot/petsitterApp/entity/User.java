package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private int id;

    @NotEmpty(message = "Password is required.")
    private String password;

    @Transient
    @NotEmpty(message = "Password confirmation is required.")
    private String passwordConfirmation;
    @Column(name = "namefirst")
    private String nameFirst;
    @Column(name = "namelast")
    private String nameLast;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    @Column(name = "created_date")
    private LocalDate createdDate;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "city_fk")
    private City city;

    @Email
    @NotEmpty(message = "email is required")
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_role_fk")
    private UserRoleEntity userRole;
}
