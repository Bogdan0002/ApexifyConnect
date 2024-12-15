package com.apexify.logic.DTO;

import lombok.Data;

@Data
public class ContentCreatorRequestDTO {
    private String email;
    private String password;
    private String profilePicture;
    private String bio;
}