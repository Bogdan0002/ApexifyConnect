package com.apexifyconnect.DAO.impl;

import com.apexifyconnect.DAO.interfaces.JobPostDAO;
import com.apexifyconnect.Model.JobPost;
import com.apexifyconnect.Model.JobStatus;
import com.apexifyconnect.Repository.JobPostRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JobPostDAOImpl implements JobPostDAO {

    private final JobPostRepository jobPostRepository;

    public JobPostDAOImpl(JobPostRepository jobPostRepository) {
        this.jobPostRepository = jobPostRepository;
    }

    @Override
    public JobPost save(JobPost jobPost) {
        return jobPostRepository.save(jobPost);
    }

    @Override
    public List<JobPost> findByCompanyId(Long companyId) {
        return jobPostRepository.findByCompanyId(companyId);
    }

    @Override
    public List<JobPost> findAll() {
        return jobPostRepository.findAll();
    }

    @Override
    public Optional<JobPost> findById(Long id) {
        // Return the result wrapped in Optional provided by the repository
        return jobPostRepository.findById(id);
    }

    @Override
    public List<JobPost> findByCompanyIdAndStatus(Long companyId, JobStatus status) {
        return jobPostRepository.findByCompanyIdAndStatus(companyId, status);
    }
}