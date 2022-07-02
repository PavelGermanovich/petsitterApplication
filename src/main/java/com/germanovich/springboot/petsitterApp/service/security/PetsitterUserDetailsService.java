package com.germanovich.springboot.petsitterApp.service.security;

import com.germanovich.springboot.petsitterApp.dao.UserRepository;
import com.germanovich.springboot.petsitterApp.entity.User;
import com.germanovich.springboot.petsitterApp.enums.USER_ROLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Service
public class PetsitterUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with the email " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), true, true, true, true, getAthorities(user.getUserRole().getRoleId()));
    }

    private Collection<? extends GrantedAuthority> getAthorities(USER_ROLE role_user) {
        return Arrays.asList(new SimpleGrantedAuthority(role_user.toString()));
    }
}
