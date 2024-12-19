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

    // Constructor with all arguments
    public ContentCreatorRequestDTO(String email, String password, String profilePicture, String bio, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.bio = bio;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String profilePicture;
    private String bio;
}