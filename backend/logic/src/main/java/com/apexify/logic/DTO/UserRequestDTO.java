package com.apexify.logic.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String email;
    private String password;
    private String role;

    // Fields specific to ContentCreator
    private String profilePicture;
    private String bio;

    // Fields specific to Company
    private String companyName;
    private String businessLicense;
}
