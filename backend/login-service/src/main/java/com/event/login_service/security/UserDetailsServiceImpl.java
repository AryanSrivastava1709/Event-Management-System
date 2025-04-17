package com.event.login_service.security;

import com.event.login_service.model.User;
import com.event.login_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/*
 * This class is used to load the user details from the database
 * It implements the UserDetailsService interface and overrides the loadUserByUsername method
 * It uses the UserRepository to get the user details from the database
 * It returns the UserDetails object which contains the user details
 * It uses the User class to get the user details from the database
 * It uses the GrantedAuthority interface to get the authorities of the user
 * It uses the SimpleGrantedAuthority class to get the authorities of the user
 */


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}