package com.apexify.logic.Service;

import com.apexify.logic.DTO.*;
import com.apexify.logic.util.JwtUtil;
import com.apexifyconnect.DAO.interfaces.JobPostDAO;
import com.apexifyconnect.DAO.interfaces.UserDAO;
import com.apexifyconnect.Model.*;
import com.apexifyconnect.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class handling user management operations in the ApexifyConnect platform.
 * Manages user registration, authentication, and profile management for both Content Creators and Companies.
 */

@Service @NoArgsConstructor @AllArgsConstructor
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private JobPostDAO jobPostDAO;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Registers a new Content Creator user
     *
     * @param requestDTO DTO containing content creator registration details
     * @return UserResponseDTO with registered user information
     * @throws RuntimeException if email is already in use
     */

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

        return new UserResponseDTO(savedCreator.getEmail(), savedCreator.getId(),"Content Creator");
    }
    /**
     * Registers a new Company user
     *
     * @param requestDTO DTO containing company registration details
     * @return UserResponseDTO with registered company information
     * @throws RuntimeException if email is already in use
     */

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

        return new UserResponseDTO(savedCompany.getEmail(), savedCompany.getId(), "Company");
    }

    public JobPostResponseDTO completeJob(Long jobPostId) {
        JobPost jobPost = jobPostDAO.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("Job post not found"));

        jobPost.setStatus(JobStatus.COMPLETED);
        JobPost updatedJobPost = jobPostDAO.save(jobPost);

        return new JobPostResponseDTO(
                updatedJobPost.getId(),
                updatedJobPost.getTitle(),
                updatedJobPost.getDescription(),
                updatedJobPost.getBudget(),
                updatedJobPost.getCompany().getCompanyName(),
                updatedJobPost.getCreatedAt(),
                updatedJobPost.getDeadline()
        );
    }

    /**
     * Authenticates a company login attempt
     *
     * @param requestDTO DTO containing login credentials
     * @return UserResponseDTO with authenticated company information
     * @throws RuntimeException if credentials are invalid or role mismatch
     */

    public UserResponseDTO loginCompany(LoginRequestDTO requestDTO) {
        Optional<User> user = userDAO.findByEmail(requestDTO.getEmail());
        if (user.isEmpty() || !passwordEncoder.matches(requestDTO.getPassword(), user.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!(user.get() instanceof Company)) {
            throw new RuntimeException("Invalid role for this email");
        }

        return new UserResponseDTO(user.get().getEmail(), user.get().getId(), "Company");
    }

    /**
     * Authenticates a content creator login attempt
     *
     * @param requestDTO DTO containing login credentials
     * @return UserResponseDTO with authenticated content creator information
     * @throws RuntimeException if credentials are invalid or role mismatch
     */

    public UserResponseDTO loginContentCreator(LoginRequestDTO requestDTO) {
        Optional<User> user = userDAO.findByEmail(requestDTO.getEmail());
        if (user.isEmpty() || !passwordEncoder.matches(requestDTO.getPassword(), user.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!(user.get() instanceof ContentCreator)) {
            throw new RuntimeException("Invalid role for this email");
        }

        return new UserResponseDTO(user.get().getEmail(), user.get().getId(),"Content Creator");
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

    /**
     * Retrieves the profile picture URL for a content creator
     *
     * @param email Email of the content creator
     * @return URL of the profile picture
     * @throws RuntimeException if user not found or not a content creator
     */

    public String getProfilePicture(String email) {
        Optional<User> user = userDAO.findByEmail(email);
        if (user.isEmpty() || !(user.get() instanceof ContentCreator)) {
            throw new RuntimeException("User not found or not a content creator");
        }

        String storedFileName = ((ContentCreator) user.get()).getProfilePicture();
        // Using the exact case-sensitive path that matches our configuration
        return "http://localhost:8080/uploads/" + storedFileName;
    }

    public List<JobPostResponseDTO> getCompanyCollaborations(String token) {
        Company company = getCompanyByToken(token);
        List<JobPost> collaborations = jobPostDAO.findByCompanyIdAndStatus(company.getId(), JobStatus.CLOSED);
        return collaborations.stream().map(jobPost -> {
            JobPostResponseDTO dto = new JobPostResponseDTO();
            dto.setId(jobPost.getId());
            dto.setTitle(jobPost.getTitle());
            dto.setDescription(jobPost.getDescription());
            dto.setBudget(jobPost.getBudget());
            dto.setCompanyName(company.getCompanyName());
            dto.setCreatedAt(jobPost.getCreatedAt());
            dto.setDeadline(jobPost.getDeadline());
            dto.setStatus(String.valueOf(jobPost.getStatus()));
            return dto;
        }).collect(Collectors.toList());
    }

    public List<JobPostResponseDTO> getCompanyProjects(String token) {
        Company company = getCompanyByToken(token);
        List<JobPost> projects = jobPostDAO.findByCompanyIdAndStatus(company.getId(), JobStatus.COMPLETED);
        return projects.stream().map(jobPost -> {
            JobPostResponseDTO dto = new JobPostResponseDTO();
            dto.setId(jobPost.getId());
            dto.setTitle(jobPost.getTitle());
            dto.setDescription(jobPost.getDescription());
            dto.setBudget(jobPost.getBudget());
            dto.setCompanyName(company.getCompanyName());
            dto.setCreatedAt(jobPost.getCreatedAt());
            dto.setDeadline(jobPost.getDeadline());
            dto.setStatus(String.valueOf(jobPost.getStatus()));
            return dto;
        }).collect(Collectors.toList());
    }


    /**
     * Updates the profile picture URL for a content creator
     *
     * @param email Email of the content creator
     * @param profilePictureUrl New profile picture URL
     * @throws RuntimeException if user not found or not a content creator
     */

    public void updateProfilePicture(String email, String profilePictureUrl) {
        Optional<User> user = userDAO.findByEmail(email);
        if (user.isEmpty() || !(user.get() instanceof ContentCreator)) {
            throw new RuntimeException("User not found or not a content creator");
        }
        ContentCreator contentCreator = (ContentCreator) user.get();
        contentCreator.setProfilePicture(profilePictureUrl);
        userDAO.save(contentCreator);
    }

    /**
     * Retrieves user profile information from JWT token
     *
     * @param token JWT authentication token
     * @return UserResponseDTO containing user profile information
     * @throws RuntimeException if user not found
     */

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

    /**
     * Retrieves content creator profile information from JWT token
     *
     * @param token JWT authentication token
     * @return ContentCreatorResponseDTO containing profile information
     * @throws RuntimeException if user not found or not a content creator
     */

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

    /**
     * Helper method to convert User entity to ContentCreatorResponseDTO
     *
     * @param userOptional Optional containing User entity
     * @return ContentCreatorResponseDTO with mapped information
     * @throws RuntimeException if user not found or not a content creator
     */

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

    /**
     * Retrieves company information from JWT token
     *
     * @param token JWT authentication token
     * @return Company entity
     * @throws RuntimeException if user not found or not a company
     */

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



    public CompanyResponseDTO getCompanyProfile(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parser()
                .setSigningKey(jwtUtil.getSecretKey())
                .parseClaimsJws(token)
                .getBody();

        String email = claims.getSubject();
        Optional<User> userOptional = userDAO.findByEmail(email);

        if (userOptional.isEmpty() || !(userOptional.get() instanceof Company)) {
            throw new RuntimeException("User not found or not a company");
        }

        Company company = (Company) userOptional.get();
        return CompanyResponseDTO.builder()
                .email(company.getEmail())
                .companyName(company.getCompanyName())
                .businessLicense(company.getBusinessLicense())
                .role(company.getRole())
                .build();
    }

    public List<JobPostResponseDTO> getCompanyJobPosts(String token) {
        Company company = getCompanyByToken(token);
        List<JobPost> jobPosts = jobPostDAO.findByCompanyId(company.getId());
        return jobPosts.stream().map(jobPost -> {
            JobPostResponseDTO dto = new JobPostResponseDTO();
            dto.setId(jobPost.getId());
            dto.setTitle(jobPost.getTitle());
            dto.setDescription(jobPost.getDescription());
            dto.setBudget(jobPost.getBudget());
            dto.setCompanyName(company.getCompanyName());
            dto.setCreatedAt(jobPost.getCreatedAt());
            dto.setDeadline(jobPost.getDeadline());
            return dto;
        }).collect(Collectors.toList());
    }



}

