package com.germanovich.springboot.petsitterApp.dao;

import com.germanovich.springboot.petsitterApp.entity.Petsitter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetsitterRepository extends CrudRepository<Petsitter, Integer> {
    Petsitter findPetSitterByUserEmail(String email);
}
