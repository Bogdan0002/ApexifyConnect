package com.apexifyconnect.DAO.interfaces;

import com.apexifyconnect.Model.JobPost;
import com.apexifyconnect.Model.JobStatus;

import java.util.List;
import java.util.Optional;

public interface JobPostDAO {
    JobPost save(JobPost jobPost);
    List<JobPost> findByCompanyId(Long companyId);
    List<JobPost> findAll();
    //find by id
    Optional<JobPost> findById(Long jobPostId);// Use Optional to support null-safe handling
    List<JobPost> findByCompanyIdAndStatus(Long companyId, JobStatus status);
}
