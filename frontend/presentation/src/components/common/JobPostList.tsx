import React, { useEffect, useState } from 'react';
import axiosInstance from '../../api/axiosInstace';


interface JobPost {
  id: number;
  title: string;
  description: string;
  budget: number;
  tags: string[];
}

const JobPostList: React.FC = () => {
  const [jobPosts, setJobPosts] = useState<JobPost[]>([]);

  useEffect(() => {
    axiosInstance.get('/job-posts/all').then((response) => setJobPosts(response.data));
  }, []);

  return (
    <div>
      <h2>Available Job Posts</h2>
      {jobPosts.map((job) => (
        <div key={job.id}>
          <h3>{job.title}</h3>
          <p>{job.description}</p>
          <p>Budget: ${job.budget}</p>
          <p>Tags: {job.tags.join(', ')}</p>
        </div>
      ))}
    </div>
  );
};

export default JobPostList;
