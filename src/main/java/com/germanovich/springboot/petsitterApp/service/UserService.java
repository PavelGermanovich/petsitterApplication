package com.germanovich.springboot.petsitterApp.service;

import com.germanovich.springboot.petsitterApp.dao.PetOwnerRepository;
import com.germanovich.springboot.petsitterApp.dao.PetsitterRepository;
import com.germanovich.springboot.petsitterApp.dao.UserRepository;
import com.germanovich.springboot.petsitterApp.entity.PetOwner;
import com.germanovich.springboot.petsitterApp.entity.PetSitter;
import com.germanovich.springboot.petsitterApp.entity.User;
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

    @Override
    public User registerNewUser(User user) throws EmailExistException {
        if (emailExist(user.getEmail())) {
            throw new EmailExistException("Such email already registered!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User updateExistingUser(User user) throws EmailExistException {
        final Integer id = user.getId();
        final String email = user.getEmail();
        final User emailOwner = userRepository.findByEmail(email);
        if (emailOwner != null && !id.equals(emailOwner.getId())) {
            throw new EmailExistException("Email not available.");
        }
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public PetSitter registerPetsitter(PetSitter petSitter) throws EmailExistException {
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
    public PetOwner updatePetowner(PetOwner petOwner) throws EmailExistException {
        if (emailExist(petOwner.getUser().getEmail())) {
            throw new EmailExistException("Such email already registered!");
        }
        petOwner.setId(userRepository.findByEmail(petOwner.getUser().getEmail()).getId());
        return petOwnerRepository.save(petOwner);
    }
}
