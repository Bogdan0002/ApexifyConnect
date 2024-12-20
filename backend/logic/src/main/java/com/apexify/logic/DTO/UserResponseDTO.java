package com.apexify.logic.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object representing user response information.
 * Used for transferring user data between layers while excluding sensitive information like passwords.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UserResponseDTO {
    private Long id;
    private String email;
    private String role;

    public UserResponseDTO(String email, Long id, String contentCreator) {
        this.email = email;
        this.id = id;
        this.role = contentCreator;
    }


    // Exclude password
}