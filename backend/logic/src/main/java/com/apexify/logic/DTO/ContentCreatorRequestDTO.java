package com.apexify.logic.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter @Setter
public class ContentCreatorRequestDTO {
    // No-argument constructor
    public ContentCreatorRequestDTO() {}

    // Constructor with arguments
    public ContentCreatorRequestDTO(String email, String password, String profilePicture, String bio) {
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.bio = bio;
    }
    private String email;
    private String password;
    private String profilePicture;
    private String bio;
}