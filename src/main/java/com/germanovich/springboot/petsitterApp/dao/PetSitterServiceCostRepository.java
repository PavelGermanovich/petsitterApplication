package com.germanovich.springboot.petsitterApp.dao;

import com.germanovich.springboot.petsitterApp.entity.PetsitterServiceCost;
import com.germanovich.springboot.petsitterApp.entity.key.PetsitterServiceKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetSitterServiceCostRepository extends CrudRepository<PetsitterServiceCost, PetsitterServiceKey> {
}
