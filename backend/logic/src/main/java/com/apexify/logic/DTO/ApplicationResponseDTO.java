package com.apexify.logic.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApplicationResponseDTO {
    private Long id;
    private Long jobPostId;
    private String jobTitle;
    private String contentCreatorName;
    private String coverLetter;
    private String status;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
