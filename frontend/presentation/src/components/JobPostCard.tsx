import React from 'react';

interface JobPostCardProps {
  jobPost: {
    id: number;
    title: string;
    description: string;
    budget: number;
    companyName: string;
    createdAt: string;
    deadline: string;
    status: string;
  };
}

const JobPostCard: React.FC<JobPostCardProps> = ({ jobPost }) => {
  const getDisplayStatus = (status: string) => {
    if (status === 'CLOSED') {
      return 'Active Collaboration';
    } else if (status === 'COMPLETED') {
      return 'Completed Projects';
    }
    return status;
  };

  return (
    <div className="job-post-card">
      <h3>{jobPost.title}</h3>
      <p>{jobPost.description}</p>
      <p>Budget: {jobPost.budget}</p>
      <p>Company: {jobPost.companyName}</p>
      <p>Created At: {new Date(jobPost.createdAt).toLocaleDateString()}</p>
      <p>Deadline: {new Date(jobPost.deadline).toLocaleDateString()}</p>
      <p>Status: {getDisplayStatus(jobPost.status)}</p>
    </div>
  );
};

export default JobPostCard;