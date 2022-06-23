package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_roles")
@Data
public class UserRole {
    @Id
    @Column(name = "role_pk")
    private int id;

    @Column(name = "role_name")
    private String roleName;
}
