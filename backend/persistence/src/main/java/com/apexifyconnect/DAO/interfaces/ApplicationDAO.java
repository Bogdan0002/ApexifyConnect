package com.apexifyconnect.DAO.interfaces;
import com.apexifyconnect.Model.Application;
import com.apexifyconnect.Model.ApplicationStatus;
import com.apexifyconnect.Model.ContentCreator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationDAO {
    Application save(Application application);
    Optional<Application> findById(Long id);
    List<Application> findByJobPostId(Long jobPostId);
    List<Application> findByContentCreatorId(Long contentCreatorId);
    boolean existsByJobPostAndContentCreator(Long jobPostId, Long contentCreatorId);
    List<Application> findByContentCreator(ContentCreator contentCreator);
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Application a WHERE a.jobPost.id = :jobPostId AND a.contentCreator.id = :creatorId")
    boolean existsByJobPostIdAndContentCreatorId(@Param("jobPostId") Long jobPostId, @Param("creatorId") Long creatorId);


}