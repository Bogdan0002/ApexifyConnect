package com.apexify.logic.Service;

import com.apexify.logic.DTO.TagRequestDTO;
import com.apexify.logic.DTO.TagResponseDTO;
import com.apexifyconnect.Model.Tag;
import com.apexifyconnect.Repository.TagRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Overloaded method: Find or create a tag by name (String).
     */
    public Tag findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setName(tagName);
                    return tagRepository.save(newTag);
                });
    }

    /**
     * Find an existing tag by name or create a new one.
     * Accepts a TagRequestDTO and returns a TagResponseDTO.
     */
    public TagResponseDTO findOrCreateTag(TagRequestDTO tagRequestDTO) {
        // Check if the tag already exists
        Optional<Tag> optionalTag = tagRepository.findByName(tagRequestDTO.getName());

        // If not found, create a new tag
        Tag tag = optionalTag.orElseGet(() -> {
            Tag newTag = new Tag();
            newTag.setName(tagRequestDTO.getName());
            return tagRepository.save(newTag);
        });

        // Map the Tag entity to TagResponseDTO
        return mapToResponseDTO(tag);
    }

    /**
     * Retrieve all tags as a list of TagResponseDTOs.
     * @return List of TagResponseDTO
     */
    public List<TagResponseDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find a tag by its name. Returns an Optional<Tag>.
     */
    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findByName(name);
    }

    // Helper Method: Map Tag entity to TagResponseDTO
    private TagResponseDTO mapToResponseDTO(Tag tag) {
        return new TagResponseDTO(tag.getId(), tag.getName());
    }
}
