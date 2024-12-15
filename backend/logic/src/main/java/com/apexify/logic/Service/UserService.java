package com.apexify.logic.Service;

import com.apexify.logic.DTO.*;
import com.apexifyconnect.Model.Company;
import com.apexifyconnect.Model.ContentCreator;
import com.apexifyconnect.Model.User;
import com.apexifyconnect.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO registerContentCreator(ContentCreatorRequestDTO requestDTO) {
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        ContentCreator creator = new ContentCreator();
        creator.setEmail(requestDTO.getEmail());
        creator.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        creator.setProfilePicture(requestDTO.getProfilePicture());
        creator.setBio(requestDTO.getBio());

        ContentCreator savedCreator = userRepository.save(creator);
        return new UserResponseDTO(savedCreator.getEmail(), "Content Creator");
    }

    @Transactional
    public UserResponseDTO registerCompany(CompanyRequestDTO requestDTO) {
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        Company company = new Company();
        company.setEmail(requestDTO.getEmail());
        company.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        company.setCompanyName(requestDTO.getCompanyName());
        company.setBusinessLicense(requestDTO.getBusinessLicense());

        Company savedCompany = userRepository.save(company);
        return new UserResponseDTO(savedCompany.getEmail(), "Company");
    }

    public UserResponseDTO loginContentCreator(LoginRequestDTO requestDTO) {
        User user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!(user instanceof ContentCreator)) {
            throw new RuntimeException("Invalid user type");
        }

        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return new UserResponseDTO(user.getEmail(), "Content Creator");
    }

    public UserResponseDTO loginCompany(LoginRequestDTO requestDTO) {
        User user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!(user instanceof Company)) {
            throw new RuntimeException("Invalid user type");
        }

        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return new UserResponseDTO(user.getEmail(), "Company");
    }
}