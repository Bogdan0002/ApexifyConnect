package com.apexifyconnect.DAO.interfaces;

import com.apexifyconnect.Model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDAO {
    List<User> findUsersByRole(String role);
    Optional<User> findByEmail(String email);
    User save(User user);
}