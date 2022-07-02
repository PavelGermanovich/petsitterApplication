package com.germanovich.springboot.petsitterApp.dao;

import com.germanovich.springboot.petsitterApp.entity.UserRoleEntity;
import com.germanovich.springboot.petsitterApp.enums.USER_ROLE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRoleEntity, Integer> {
    UserRoleEntity findByRoleId(USER_ROLE roleName);
}
