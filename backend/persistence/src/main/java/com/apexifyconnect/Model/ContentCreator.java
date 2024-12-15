package com.apexifyconnect.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("CONTENT_CREATOR")
@Getter @Setter
public class ContentCreator extends User {
    private String profilePicture;
    private String bio;
    // Other fields specific to creators
}
