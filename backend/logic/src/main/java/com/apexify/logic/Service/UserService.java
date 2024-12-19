package com.apexify.logic.Service;

import com.apexify.logic.DTO.*;
import com.apexify.logic.util.JwtUtil;
import com.apexifyconnect.DAO.interfaces.UserDAO;
import com.apexifyconnect.Model.Company;
import com.apexifyconnect.Model.ContentCreator;
import com.apexifyconnect.Model.User;
import com.apexifyconnect.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO registerContentCreator(ContentCreatorRequestDTO requestDTO) {
        Optional<User> existingUser = userDAO.findByEmail(requestDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        ContentCreator contentCreator = new ContentCreator();
        contentCreator.setFirstName(requestDTO.getFirstName());
        contentCreator.setLastName(requestDTO.getLastName());
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
        company.setCompanyName(requestDTO.getCompanyName());
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

        if (!(user.get() instanceof Company)) {
            throw new RuntimeException("Invalid role for this email");
        }

        return new UserResponseDTO(user.get().getEmail(), "Company");
    }

    public UserResponseDTO loginContentCreator(LoginRequestDTO requestDTO) {
        Optional<User> user = userDAO.findByEmail(requestDTO.getEmail());
        if (user.isEmpty() || !passwordEncoder.matches(requestDTO.getPassword(), user.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!(user.get() instanceof ContentCreator)) {
            throw new RuntimeException("Invalid role for this email");
        }

        return new UserResponseDTO(user.get().getEmail(), "Content Creator");
    }

//    public String getProfilePicture(String email) {
//        Optional<User> user = userDAO.findByEmail(email);
//        if (user.isEmpty() || !(user.get() instanceof ContentCreator)) {
//            throw new RuntimeException("User not found or not a content creator");
//        }
//        String profilePictureUrl = ((ContentCreator) user.get()).getProfilePicture();
//        // Ensure the URL is absolute if necessary
//        if (!profilePictureUrl.startsWith("http")) {
//            profilePictureUrl = "http://localhost:8080" + profilePictureUrl;
//        }
//        return profilePictureUrl;
//    }
//

    public String getProfilePicture(String email) {
        Optional<User> user = userDAO.findByEmail(email);
        if (user.isEmpty() || !(user.get() instanceof ContentCreator)) {
            throw new RuntimeException("User not found or not a content creator");
        }

        String storedFileName = ((ContentCreator) user.get()).getProfilePicture();
        // Using the exact case-sensitive path that matches our configuration
        return "http://localhost:8080/uploads/" + storedFileName;
    }



    public void updateProfilePicture(String email, String profilePictureUrl) {
        Optional<User> user = userDAO.findByEmail(email);
        if (user.isEmpty() || !(user.get() instanceof ContentCreator)) {
            throw new RuntimeException("User not found or not a content creator");
        }
        ContentCreator contentCreator = (ContentCreator) user.get();
        contentCreator.setProfilePicture(profilePictureUrl);
        userDAO.save(contentCreator);
    }

    public UserResponseDTO getUserProfile(String token) {
        // Remove "Bearer " prefix from the token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Parse the token
        Claims claims = Jwts.parser()
                .setSigningKey(jwtUtil.getSecretKey()) // Replace with your actual secret key
                .parseClaimsJws(token)
                .getBody();

        // Fetch user details from the database using claims
        String email = claims.getSubject();
        Optional<User> userOptional = userDAO.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        // Set user details in UserResponseDTO
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRole(user.getRole()); // Fetch the actual role from the user

        return userResponseDTO;
    }

    public ContentCreatorResponseDTO getCreatorProfile (String token) {
        // Remove "Bearer " prefix from the token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Parse the token
        Claims claims = Jwts.parser()
                .setSigningKey(jwtUtil.getSecretKey()) // Replace with your actual secret key
                .parseClaimsJws(token)
                .getBody();

        // Extract email from claims
        String email = claims.getSubject();

        // Fetch the ContentCreator user from the database
        Optional<User> userOptional = userDAO.findByEmail(email);
        ContentCreatorResponseDTO responseDTO = getContentCreatorResponseDTO(userOptional);

        return responseDTO;
    }

    private static ContentCreatorResponseDTO getContentCreatorResponseDTO(Optional<User> userOptional) {
        if (userOptional.isEmpty() || !(userOptional.get() instanceof ContentCreator)) {
            throw new RuntimeException("User not found or not a content creator");
        }

        ContentCreator contentCreator = (ContentCreator) userOptional.get();

        // Map the ContentCreator to ContentCreatorResponseDTO
        ContentCreatorResponseDTO responseDTO = new ContentCreatorResponseDTO();
        responseDTO.setEmail(contentCreator.getEmail());
        responseDTO.setFirstName(contentCreator.getFirstName());
        responseDTO.setLastName(contentCreator.getLastName());
        responseDTO.setProfilePicture(contentCreator.getProfilePicture());
        responseDTO.setBio(contentCreator.getBio());
        responseDTO.setRole(contentCreator.getRole());
        return responseDTO;
    }

    public Company getCompanyByToken(String token) {
        // Remove "Bearer " prefix from the token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Parse the token
        Claims claims = Jwts.parser()
                .setSigningKey(jwtUtil.getSecretKey()) // Replace with your actual secret key
                .parseClaimsJws(token)
                .getBody();

        // Extract email from claims
        String email = claims.getSubject();

        // Fetch the Company user from the database
        Optional<User> userOptional = userDAO.findByEmail(email);
        if (userOptional.isEmpty() || !(userOptional.get() instanceof Company)) {
            throw new RuntimeException("User not found or not a company");
        }

        return (Company) userOptional.get();
    }


}

