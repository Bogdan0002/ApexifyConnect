package com.apexifyconnect.Repository;

import com.apexifyconnect.Model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobPostId(Long jobPostId);
    List<Application> findByContentCreatorId(Long creatorId);
}
