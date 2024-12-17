package com.apexifyconnect.DAO.impl;

import com.apexifyconnect.DAO.interfaces.UserDAO;
import com.apexifyconnect.Model.User;
import com.apexifyconnect.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDAOImpl implements UserDAO {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long creatorId) {
        return userRepository.findById(creatorId);
    }


}