package com.germanovich.springboot.petsitterApp.dao;

import com.germanovich.springboot.petsitterApp.entity.PetsitterServiceCost;
import com.germanovich.springboot.petsitterApp.entity.key.PetsitterServiceKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetSitterServiceCostRepository extends CrudRepository<PetsitterServiceCost, PetsitterServiceKey> {
    @Query("from petsitter_service_provide where service.serviceName = ?1")
    List<PetsitterServiceCost> findPetsitterByServiceName(String name);
}
