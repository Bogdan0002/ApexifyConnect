package com.apexify.logic.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobPostRequestDTO {
    private String title;
    private String description;
    private Double budget;
    private String contentType; // Optional content type (e.g., Blog, Video)
    private String deadline; // Deadline in ISO 8601 format, parsed later
    private List<String> tagNames; // List of tag names
}
