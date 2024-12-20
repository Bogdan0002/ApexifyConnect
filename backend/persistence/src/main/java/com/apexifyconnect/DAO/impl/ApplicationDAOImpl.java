package com.apexifyconnect.DAO.impl;

import com.apexifyconnect.DAO.interfaces.ApplicationDAO;
import com.apexifyconnect.DAO.interfaces.JobPostDAO;
import com.apexifyconnect.DAO.interfaces.UserDAO;
import com.apexifyconnect.Model.Application;
import com.apexifyconnect.Model.ApplicationStatus;
import com.apexifyconnect.Model.ContentCreator;
import com.apexifyconnect.Model.JobPost;
import com.apexifyconnect.Repository.ApplicationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ApplicationDAOImpl implements ApplicationDAO {

    private final ApplicationRepository applicationRepository;
    private final JobPostDAO jobPostDAO;
    private final UserDAO userDAO;
    @PersistenceContext
    private EntityManager entityManager;


    public ApplicationDAOImpl(ApplicationRepository applicationRepository, JobPostDAO jobPostDAO, UserDAO userDAO) {
        this.applicationRepository = applicationRepository;
        this.jobPostDAO = jobPostDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Application save(Application application) {
        return applicationRepository.save(application);
    }

    @Override
    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }

    @Override
    public List<Application> findByJobPostId(Long jobPostId) {
        return applicationRepository.findByJobPostId(jobPostId);
    }

    @Override
    public List<Application> findByContentCreatorId(Long contentCreatorId) {
        return applicationRepository.findByContentCreatorId(contentCreatorId);
    }

    @Override
    public boolean existsByJobPostAndContentCreator(Long jobPostId, Long contentCreatorId) {
        JobPost jobPost = jobPostDAO.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("Job post not found"));
        ContentCreator creator = (ContentCreator) userDAO.findById(contentCreatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        return applicationRepository.existsByJobPostAndContentCreator(jobPost, creator);
    }
    @Override
    public List<Application> findByContentCreator(ContentCreator contentCreator) {
        return applicationRepository.findByContentCreator(contentCreator);
    }

    @Override
    public boolean existsByJobPostIdAndContentCreatorId(Long jobPostId, Long creatorId) {
        String jpql = "SELECT COUNT(a) > 0 FROM Application a WHERE a.jobPost.id = :jobPostId AND a.contentCreator.id = :creatorId";
        TypedQuery<Boolean> query = entityManager.createQuery(jpql, Boolean.class);
        query.setParameter("jobPostId", jobPostId);
        query.setParameter("creatorId", creatorId);
        return query.getSingleResult();
    }




}
