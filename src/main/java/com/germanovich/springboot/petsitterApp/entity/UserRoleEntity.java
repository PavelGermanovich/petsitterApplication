package com.germanovich.springboot.petsitterApp.entity;

import com.germanovich.springboot.petsitterApp.enums.USER_ROLE;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_roles")
@Data
public class UserRoleEntity {
    @Id
    @Column(name = "role_pk")
    private int id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_id")
    @Enumerated(EnumType.STRING)
    private USER_ROLE roleId;
}
