import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstace';
import { Card, CardContent, Typography, Button, Stack, Box } from '@mui/material';

interface JobPost {
  id: number;
  title: string;
  description: string;
  budget: number;
  tags: string[];
}

const JobPostList: React.FC = () => {
  const [jobPosts, setJobPosts] = useState<JobPost[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    axiosInstance.get('/job-posts/all').then((response) => setJobPosts(response.data));
  }, []);

  const handleApply = (jobId: number) => {
    navigate(`/apply/${jobId}`);
  };

  return (
    <Stack spacing={3}>
      {jobPosts.map((job) => (
        <Card key={job.id}>
          <CardContent>
            <Typography variant="h5" gutterBottom>{job.title}</Typography>
            <Typography paragraph>{job.description}</Typography>
            <Typography color="primary">Budget: ${job.budget}</Typography>
            <Typography>Tags: {job.tags.join(', ')}</Typography>
            <Box mt={2}>
              <Button 
                variant="contained" 
                color="primary"
                onClick={() => handleApply(job.id)}
              >
                Apply Now
              </Button>
            </Box>
          </CardContent>
        </Card>
      ))}
    </Stack>
  );
};

export default JobPostList;
