package com.apexifyconnect.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@DiscriminatorValue("CONTENT_CREATOR")
@Getter
@Setter
public class ContentCreator extends User {
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String bio;
    private String skills;
    private String preferredContentType;
    private Double minBudget;
    private Double maxBudget;

    @ManyToMany
    @JoinTable(
            name = "creator_tags",
            joinColumns = @JoinColumn(name = "creator_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @JsonManagedReference
    @OneToMany(mappedBy = "contentCreator", cascade = CascadeType.ALL)
    private List<Application> applications;
}
