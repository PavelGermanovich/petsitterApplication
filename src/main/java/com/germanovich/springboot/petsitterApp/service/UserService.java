package com.germanovich.springboot.petsitterApp.service;

import com.germanovich.springboot.petsitterApp.dao.PetOwnerRepository;
import com.germanovich.springboot.petsitterApp.dao.PetsitterRepository;
import com.germanovich.springboot.petsitterApp.dao.UserRepository;
import com.germanovich.springboot.petsitterApp.entity.PetOwner;
import com.germanovich.springboot.petsitterApp.entity.Petsitter;
import com.germanovich.springboot.petsitterApp.validation.EmailExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PetsitterRepository petsitterRepository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    public boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public Petsitter registerPetsitter(Petsitter petSitter) throws EmailExistException {
        if (emailExist(petSitter.getUser().getEmail())) {
            throw new EmailExistException("Such email already registered!");
        }
        petSitter.getUser().setPassword(passwordEncoder.encode(petSitter.getUser().getPassword()));
        return petsitterRepository.save(petSitter);
    }

    @Override
    public PetOwner registerPetowner(PetOwner petOwner) throws EmailExistException {
        if (emailExist(petOwner.getUser().getEmail())) {
            throw new EmailExistException("Such email already registered!");
        }
        petOwner.getUser().setPassword(passwordEncoder.encode(petOwner.getUser().getPassword()));
        return petOwnerRepository.save(petOwner);
    }

    @Override
    public PetOwner updatePetowner(PetOwner petowner) throws EmailExistException {
        return petOwnerRepository.save(petowner);
    }
}
