package com.apexify.logic.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class ApplicationRequestDTO {
    private Long jobPostId;
    private String coverLetter;

    public ApplicationRequestDTO(Long jobPostId, String coverLetter) {
        this.jobPostId = jobPostId;
        this.coverLetter = coverLetter;
    }
}
