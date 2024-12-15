package com.apexifyconnect.DAO.impl;

import com.apexifyconnect.DAO.interfaces.UserDAO;
import com.apexifyconnect.Model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findUsersByRole(String role) {
        String query;

        if (role.equalsIgnoreCase("ContentCreator")) {
            query = "SELECT u FROM ContentCreator u";
        } else if (role.equalsIgnoreCase("Company")) {
            query = "SELECT u FROM Company u";
        } else {
            throw new IllegalArgumentException("Invalid role type: " + role);
        }

        return entityManager.createQuery(query, User.class).getResultList();
    }
}
