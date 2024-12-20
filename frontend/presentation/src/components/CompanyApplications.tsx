import React, { useEffect, useState } from 'react';
import { Container, Typography, Box, Paper, Grid, Chip, Button } from '@mui/material';
import axiosInstance from '../api/axiosInstace';

interface ApplicationResponseDTO {
  id: number;
  jobPostId: number;
  jobTitle: string;
  contentCreatorName: string;
  coverLetter: string | null;
  status: string;
  appliedAt: string;
}

const CompanyApplications = () => {
  const [applications, setApplications] = useState<ApplicationResponseDTO[]>([]);
  const [selectedJobId, setSelectedJobId] = useState<number | null>(null);

  useEffect(() => {
    const fetchApplications = async () => {
      if (selectedJobId) {
        const response = await axiosInstance.get(`/applications/company/jobpost/${selectedJobId}`);
        setApplications(response.data);
      }
    };
    fetchApplications();
  }, [selectedJobId]);

  const handleStatusUpdate = async (applicationId: number, status: string) => {
    await axiosInstance.patch(`/applications/${applicationId}/status`, null, {
      params: { status }
    });
    
    // Refresh applications after status update
    if (selectedJobId) {
      const response = await axiosInstance.get(`/applications/company/jobpost/${selectedJobId}`);
      setApplications(response.data);
    }
  };

  return (
    <Container maxWidth="lg">
      <Typography variant="h4" gutterBottom sx={{ mt: 4 }}>
        Job Applications Management
      </Typography>
      <Grid container spacing={3}>
        {applications.map((application) => (
          <Grid item xs={12} key={application.id}>
            <Paper elevation={3} sx={{ p: 3 }}>
              <Box display="flex" justifyContent="space-between" alignItems="center">
                <Typography variant="h6">{application.jobTitle}</Typography>
                <Chip 
                  label={application.status}
                  color={
                    application.status === 'ACCEPTED' ? 'success' : 
                    application.status === 'REJECTED' ? 'error' : 
                    'default'
                  }
                />
              </Box>
              <Typography variant="subtitle1" sx={{ mt: 2 }}>
                Applicant: {application.contentCreatorName}
              </Typography>
              <Typography variant="body1" sx={{ mt: 2, mb: 3 }}>
                Cover Letter: {application.coverLetter || 'No cover letter provided'}
              </Typography>
              <Typography variant="body2" color="textSecondary">
                Applied: {new Date(application.appliedAt).toLocaleDateString()}
              </Typography>
              <Box sx={{ mt: 3 }}>
                <Button 
                  variant="contained" 
                  color="success" 
                  onClick={() => handleStatusUpdate(application.id, 'ACCEPTED')}
                  sx={{ mr: 2 }}
                  disabled={application.status === 'ACCEPTED'}
                >
                  Accept
                </Button>
                <Button 
                  variant="contained" 
                  color="error"
                  onClick={() => handleStatusUpdate(application.id, 'REJECTED')}
                  disabled={application.status === 'REJECTED'}
                >
                  Reject
                </Button>
              </Box>
            </Paper>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
};

export default CompanyApplications;
