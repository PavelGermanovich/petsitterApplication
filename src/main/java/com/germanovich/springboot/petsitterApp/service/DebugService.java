package com.germanovich.springboot.petsitterApp.service;

import com.germanovich.springboot.petsitterApp.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebugService {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PetTypeRepository petTypeRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PetOwnerRepository petOwnerRepository;
    @Autowired
    private PetsitterRepository petsitterRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private PetSitterServiceCostRepository petSitterServiceCostRepository;
    @Autowired
    private OrderPlannedRepository orderPlannedRepository;

    public void checkSomeStuff() {
        System.out.println("hello here");
    }
}
