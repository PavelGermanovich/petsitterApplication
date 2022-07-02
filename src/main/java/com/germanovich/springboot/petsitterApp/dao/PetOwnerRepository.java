package com.germanovich.springboot.petsitterApp.dao;

import com.germanovich.springboot.petsitterApp.entity.PetOwner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetOwnerRepository extends CrudRepository<PetOwner, Integer> {
    PetOwner findPetOwnerByUserEmail(String email);
}
