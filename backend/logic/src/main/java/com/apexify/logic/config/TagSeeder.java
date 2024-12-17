package com.apexify.logic.config;

import com.apexify.logic.DTO.TagRequestDTO;
import com.apexify.logic.Service.TagService;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Component
public class TagSeeder {

    private final TagService tagService;

    public TagSeeder(TagService tagService) {
        this.tagService = tagService;
    }

    @PostConstruct
    public void seedTags() {
        // List of predefined tags
        List<String> predefinedTags = List.of(
                "Fitness", "Tech", "Beauty", "Travel", "Food", "Lifestyle", "Gaming", "Education", "Health"
        );

        // Seed tags into the database
        predefinedTags.forEach(tagName -> {
            // Convert each String to a TagRequestDTO
            TagRequestDTO tagRequestDTO = new TagRequestDTO(tagName);

            // Use TagService to create/find tags
            tagService.findOrCreateTag(tagRequestDTO);
        });
    }
}