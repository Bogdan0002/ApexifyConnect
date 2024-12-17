package com.apexify.logic.Service;

import com.apexify.logic.DTO.TagRequestDTO;
import com.apexify.logic.DTO.TagResponseDTO;
import com.apexify.logic.DTO.UserRequestDTO;
import com.apexify.logic.DTO.UserResponseDTO;
import com.apexifyconnect.DAO.interfaces.UserDAO;
import com.apexifyconnect.Model.ContentCreator;
import com.apexifyconnect.Model.Tag;
import com.apexifyconnect.Model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentCreatorService {

    private final UserDAO userDAO;
    private final TagService tagService;

    public ContentCreatorService(UserDAO userDAO, TagService tagService) {
        this.userDAO = userDAO;
        this.tagService = tagService;
    }

    // Register a new ContentCreator using UserRequestDTO and return UserResponseDTO
    public UserResponseDTO registerContentCreator(UserRequestDTO requestDTO, List<String> tagNames) {
        // Map UserRequestDTO to ContentCreator entity
        ContentCreator contentCreator = new ContentCreator();
        contentCreator.setEmail(requestDTO.getEmail());
        contentCreator.setPassword(requestDTO.getPassword());
        contentCreator.setRole(requestDTO.getRole());
        contentCreator.setProfilePicture(requestDTO.getProfilePicture());
        contentCreator.setBio(requestDTO.getBio());

        // Process tags and associate them with the content creator
        List<Tag> tags = tagNames.stream()
                .map(tagName -> {
                    // Create TagRequestDTO from tagName
                    TagRequestDTO tagRequestDTO = new TagRequestDTO(tagName);
                    // Fetch or create a tag using tagService
                    TagResponseDTO tagResponseDTO = tagService.findOrCreateTag(tagRequestDTO);
                    // Map TagResponseDTO to Tag entity
                    Tag tag = new Tag();
                    tag.setId(tagResponseDTO.getId());
                    tag.setName(tagResponseDTO.getName());
                    return tag;
                })
                .collect(Collectors.toList());

        contentCreator.setTags(tags);

        // Save the ContentCreator entity
        User savedCreator = userDAO.save(contentCreator);

        // Map the saved ContentCreator to UserResponseDTO
        return mapToResponseDTO(savedCreator);
    }

    // Update tags for an existing ContentCreator and return UserResponseDTO
    public UserResponseDTO updateTags(Long creatorId, List<String> tagNames) {
        // Fetch ContentCreator using UserDAO
        ContentCreator contentCreator = (ContentCreator) userDAO.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        // Process and update tags
        List<Tag> tags = tagNames.stream()
                .map(tagName -> {
                    // Create TagRequestDTO from tagName
                    TagRequestDTO tagRequestDTO = new TagRequestDTO(tagName);
                    // Fetch or create a tag using tagService
                    TagResponseDTO tagResponseDTO = tagService.findOrCreateTag(tagRequestDTO);
                    // Map TagResponseDTO to Tag entity
                    Tag tag = new Tag();
                    tag.setId(tagResponseDTO.getId());
                    tag.setName(tagResponseDTO.getName());
                    return tag;
                })
                .collect(Collectors.toList());

        contentCreator.setTags(tags);

        // Save the updated ContentCreator entity
        User updatedCreator = userDAO.save(contentCreator);

        // Map the updated ContentCreator to UserResponseDTO
        return mapToResponseDTO(updatedCreator);
    }

    // Utility method to map User (or ContentCreator) to UserResponseDTO
    private UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setEmail(user.getEmail());
        responseDTO.setRole(user.getRole());
        return responseDTO;
    }
}