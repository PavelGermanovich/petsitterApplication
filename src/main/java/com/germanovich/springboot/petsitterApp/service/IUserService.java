package com.germanovich.springboot.petsitterApp.service;

import com.germanovich.springboot.petsitterApp.entity.PetOwner;
import com.germanovich.springboot.petsitterApp.entity.Petsitter;
import com.germanovich.springboot.petsitterApp.entity.User;
import com.germanovich.springboot.petsitterApp.validation.EmailExistException;

public interface IUserService {
    User registerNewUser(User user) throws EmailExistException;
    User updateExistingUser(User user) throws EmailExistException;
    Petsitter registerPetsitter(Petsitter petSitter) throws EmailExistException;
    PetOwner registerPetowner(PetOwner petOwner) throws EmailExistException;

    PetOwner updatePetowner(PetOwner petowner, String oldEmail) throws EmailExistException;
}
