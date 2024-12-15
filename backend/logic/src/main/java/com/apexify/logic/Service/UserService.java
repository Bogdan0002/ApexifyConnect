package com.apexify.logic.Service;

import com.apexify.logic.DTO.*;
import com.apexifyconnect.DAO.interfaces.UserDAO;
import com.apexifyconnect.Model.Company;
import com.apexifyconnect.Model.ContentCreator;
import com.apexifyconnect.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO registerContentCreator(ContentCreatorRequestDTO requestDTO) {
        Optional<User> existingUser = userDAO.findByEmail(requestDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        ContentCreator contentCreator = new ContentCreator();
        contentCreator.setEmail(requestDTO.getEmail());
        contentCreator.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        contentCreator.setProfilePicture(requestDTO.getProfilePicture());
        contentCreator.setBio(requestDTO.getBio());

        ContentCreator savedCreator = (ContentCreator) userDAO.save(contentCreator);

        return new UserResponseDTO(savedCreator.getEmail(), "Content Creator");
    }

    public UserResponseDTO registerCompany(CompanyRequestDTO requestDTO) {
        Optional<User> existingUser = userDAO.findByEmail(requestDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        Company company = new Company();
        company.setEmail(requestDTO.getEmail());
        company.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        company.setCompanyName(requestDTO.getCompanyName());
        company.setBusinessLicense(requestDTO.getBusinessLicense());

        Company savedCompany = (Company) userDAO.save(company);

        return new UserResponseDTO(savedCompany.getEmail(), "Company");
    }

    public UserResponseDTO loginCompany(LoginRequestDTO requestDTO) {
        Optional<User> user = userDAO.findByEmail(requestDTO.getEmail());
        if (user.isEmpty() || !passwordEncoder.matches(requestDTO.getPassword(), user.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return new UserResponseDTO(user.get().getEmail(), "Company");
    }

    public UserResponseDTO loginContentCreator(LoginRequestDTO requestDTO) {
        Optional<User> user = userDAO.findByEmail(requestDTO.getEmail());
        if (user.isEmpty() || !passwordEncoder.matches(requestDTO.getPassword(), user.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return new UserResponseDTO(user.get().getEmail(), "Content Creator");
    }
}