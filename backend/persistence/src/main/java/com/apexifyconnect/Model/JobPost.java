package com.apexifyconnect.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_posts")
@Getter
@Setter
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // Job post title
    private String description; // Detailed description of the job
    private Double budget; // Offered budget
    private String contentType; // Type of content (e.g., video, image, blog, etc.)
    private LocalDateTime createdAt;
    private LocalDateTime deadline; // Application deadline

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company; // The company that created the job post

    @ManyToMany
    @JoinTable(
            name = "job_post_tags",
            joinColumns = @JoinColumn(name = "job_post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    private List<Application> applications; // List of applications for this job post

    @Enumerated(EnumType.STRING)
    private JobStatus status; // Status of the job post (e.g., OPEN, CLOSED, COMPLETED)
}