package com.apexifyconnect.DAO.interfaces;

import com.apexifyconnect.Model.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO {
    public List<User> findUsersByRole(String role);

}
