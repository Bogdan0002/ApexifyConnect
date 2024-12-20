package com.apexify.logic.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobPostResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Double budget;
    private String companyName;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private String status;

    public JobPostResponseDTO(Long id, String title, String description, Double budget, String companyName, LocalDateTime createdAt, LocalDateTime deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.companyName = companyName;
        this.createdAt = createdAt;
        this.deadline = deadline;
    }

    public String getDisplayStatus() {
        if ("CLOSED".equals(status)) {
            return "Active Collaboration";
        } else if ("COMPLETED".equals(status)) {
            return "Completed Projects";
        }
        return status;
    }
}
