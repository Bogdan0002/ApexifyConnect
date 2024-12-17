package com.apexify.logic.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobPostResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Double budget;
    private String companyName;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
}
