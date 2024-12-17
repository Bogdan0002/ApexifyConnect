package com.apexifyconnect.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@DiscriminatorValue("CONTENT_CREATOR")
@Getter
@Setter
public class ContentCreator extends User {
    private String profilePicture;
    private String bio;
    private String skills; // Skills or expertise (e.g., "videography, editing, social media")
    private String preferredContentType; // Types of content they create (e.g., video, image)
  //  private String tags; // Tags that describe their niche (e.g., "fitness", "tech", "beauty")
    private Double minBudget; // Minimum acceptable budget for a job
    private Double maxBudget; // Maximum budget range for filtering jobs

    @ManyToMany
    @JoinTable(
            name = "creator_tags",
            joinColumns = @JoinColumn(name = "creator_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;


    @OneToMany(mappedBy = "contentCreator", cascade = CascadeType.ALL)
    private List<Application> applications; // List of applications submitted by this creator

}
