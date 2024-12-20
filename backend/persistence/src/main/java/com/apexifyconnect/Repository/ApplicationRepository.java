package com.apexifyconnect.Repository;

import com.apexifyconnect.Model.Application;
import com.apexifyconnect.Model.ApplicationStatus;
import com.apexifyconnect.Model.ContentCreator;
import com.apexifyconnect.Model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Query("SELECT a FROM Application a LEFT JOIN FETCH a.contentCreator WHERE a.jobPost.id = :jobPostId")
    List<Application> findByJobPostId(@Param("jobPostId") Long jobPostId);

    @Query("SELECT a FROM Application a LEFT JOIN FETCH a.contentCreator WHERE a.contentCreator.id = :contentCreatorId")
    List<Application> findByContentCreatorId(@Param("contentCreatorId") Long contentCreatorId);

    boolean existsByJobPostAndContentCreator(JobPost jobPost, ContentCreator contentCreator);

    @Query("SELECT a FROM Application a LEFT JOIN FETCH a.contentCreator WHERE a.contentCreator = :creator")
    List<Application> findByContentCreator(@Param("creator") ContentCreator creator);

    List<Application> findByJobPostIdAndStatus(Long jobPostId, ApplicationStatus status);
}



