package com.apexify.logic.DTO;

/**
 * Data Transfer Object representing tag information in responses.
 * Used for transferring tag data when displaying or listing tags in the system.
 */

public class TagResponseDTO {

    private Long id;
    private String name;

    // Constructor
    public TagResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}