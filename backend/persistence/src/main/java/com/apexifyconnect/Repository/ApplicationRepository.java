package com.apexifyconnect.Repository;

import com.apexifyconnect.Model.Application;
import com.apexifyconnect.Model.ContentCreator;
import com.apexifyconnect.Model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobPostId(Long jobPostId);
    List<Application> findByContentCreatorId(Long contentCreatorId);
    boolean existsByJobPostAndContentCreator(JobPost jobPost, ContentCreator contentCreator);
}
