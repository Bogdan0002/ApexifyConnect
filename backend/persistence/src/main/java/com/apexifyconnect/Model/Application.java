package com.apexifyconnect.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Getter
@Setter
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private ContentCreator contentCreator;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // APPLIED, ACCEPTED, REJECTED

    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}