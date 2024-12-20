package com.apexify.logic.Service;

import com.apexifyconnect.Model.User;
import com.apexifyconnect.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * Handles user authentication by loading user details from the database.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repository for accessing user data
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Loads a user's details by their email address for authentication purposes.
     * Implements the UserDetailsService interface method.
     *
     * @param email The email address of the user to load
     * @return UserDetails object containing the user's authentication information
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        User user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
