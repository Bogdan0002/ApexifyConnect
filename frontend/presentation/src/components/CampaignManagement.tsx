import React, { useEffect, useState } from 'react';
import { Container, Typography, Box, Paper, Grid, Chip, Button, Tabs, Tab, Card, CardContent, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import axiosInstance from '../api/axiosInstace';
import JobPostForm from '../components/common/JobPostForm';

interface JobPost {
  id: number;
  title: string;
  status: string;
}

interface ApplicationResponseDTO {
  id: number;
  jobPostId: number;
  jobTitle: string;
  contentCreatorName: string;
  coverLetter: string | null;
  status: string;
  appliedAt: string;
}

const CampaignManagement = () => {
  const [jobPosts, setJobPosts] = useState<JobPost[]>([]);
  const [applications, setApplications] = useState<ApplicationResponseDTO[]>([]);
  const [selectedJobId, setSelectedJobId] = useState<number | null>(null);
  const [projects, setProjects] = useState<any[]>([]);
  const [collaborations, setCollaborations] = useState<any[]>([]);
  const [openJobPostModal, setOpenJobPostModal] = useState(false);

  useEffect(() => {
    const fetchJobPosts = async () => {
      const userData = JSON.parse(localStorage.getItem('user') || '{}');
      const companyId = userData.userResponse?.id;
      const response = await axiosInstance.get(`/job-posts/company/${companyId}`);
      setJobPosts(response.data);
      if (response.data.length > 0) {
        setSelectedJobId(response.data[0].id);
      }
    };
    fetchJobPosts();
  }, []);

  useEffect(() => {
    const fetchApplications = async () => {
      if (selectedJobId) {
        const response = await axiosInstance.get(`/applications/company/jobpost/${selectedJobId}`);
        setApplications(response.data);
      }
    };
    if (selectedJobId) {
      fetchApplications();
    }
  }, [selectedJobId]);

  useEffect(() => {
    const fetchProjects = async () => {
      const response = await axiosInstance.get('/users/profile/company/projects');
      setProjects(response.data);
    };

    const fetchCollaborations = async () => {
      const response = await axiosInstance.get('/users/profile/company/collaborations');
      setCollaborations(response.data);
    };

    fetchProjects();
    fetchCollaborations();
  }, []);

  const handleJobChange = (jobId: number) => {
    setSelectedJobId(jobId);
  };

  const handleStatusUpdate = async (applicationId: number, status: 'ACCEPTED' | 'REJECTED') => {
    try {
      await axiosInstance.patch(`/applications/${applicationId}/status`, null, {
        params: { status }
      });

      if (selectedJobId) {
        const response = await axiosInstance.get(`/applications/company/jobpost/${selectedJobId}`);
        setApplications(response.data);

        if (status === 'ACCEPTED') {
          setJobPosts(jobPosts.map(job => job.id === selectedJobId ? { ...job, status: 'CLOSED' } : job));
          updateStatistics();
        }
      }
    } catch (error) {
      console.error('Error updating status:', error);
    }
  };

  const handleCompleteProject = async (jobId: number) => {
    try {
      await axiosInstance.put(`/users/profile/company/jobs/${jobId}/complete`, null, {
        params: { status: 'COMPLETED' }
      });
      setJobPosts(jobPosts.map(job => job.id === jobId ? { ...job, status: 'COMPLETED' } : job));
      updateStatistics();
    } catch (error) {
      console.error('Error completing project:', error);
    }
  };

  const updateStatistics = () => {
    const completedProjects = jobPosts.filter(job => job.status === 'COMPLETED').length;
    const activeCollaborations = jobPosts.filter(job => job.status === 'CLOSED').length;
    setProjects([...projects, { id: projects.length + 1, status: 'COMPLETED' }]);
    setCollaborations([...collaborations, { id: collaborations.length + 1, status: 'CLOSED' }]);
  };

  const handleOpenJobPostModal = () => setOpenJobPostModal(true);
  const handleCloseJobPostModal = () => setOpenJobPostModal(false);

  return (
    <Container maxWidth="lg">
      <Typography variant="h4" gutterBottom sx={{ mt: 4 }}>
        Campaign Management
      </Typography>

      <Box sx={{ mb: 4 }}>
        <Typography variant="h6" gutterBottom>
          Company Statistics
        </Typography>
        <Grid container spacing={3}>
          <Grid item xs={4}>
            <Card>
              <CardContent>
                <Typography variant="h4" align="center">
                  {jobPosts.length}
                </Typography>
                <Typography variant="subtitle1" align="center">
                  Active Jobs
                </Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={4}>
            <Card>
              <CardContent>
                <Typography variant="h4" align="center">
                  {projects.length}
                </Typography>
                <Typography variant="subtitle1" align="center">
                  Completed Projects
                </Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={4}>
            <Card>
              <CardContent>
                <Typography variant="h4" align="center">
                  {collaborations.length}
                </Typography>
                <Typography variant="subtitle1" align="center">
                  Active Collaborations
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Box>

      <Box sx={{ borderBottom: 1, borderColor: 'divider', mb: 3 }}>
        <Tabs 
          value={selectedJobId} 
          onChange={(_, newValue) => handleJobChange(newValue)}
        >
          {jobPosts.map((job) => (
            <Tab key={job.id} label={job.title} value={job.id} />
          ))}
        </Tabs>
      </Box>

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

      {selectedJobId && (
        <Box sx={{ mt: 4 }}>
          <Button
            variant="contained"
            color="primary"
            onClick={() => handleCompleteProject(selectedJobId)}
            disabled={jobPosts.find(job => job.id === selectedJobId)?.status !== 'CLOSED'}
          >
            Mark Project as Completed
          </Button>
        </Box>
      )}

      <Box sx={{ mt: 4 }}>
        <Button 
          variant="contained" 
          color="primary" 
          onClick={handleOpenJobPostModal}
          fullWidth
          size="large"
        >
          Create Job Post
        </Button>
      </Box>

      <Dialog open={openJobPostModal} onClose={handleCloseJobPostModal} fullWidth maxWidth="sm">
        <DialogTitle>Create a New Job Post</DialogTitle>
        <DialogContent>
          <JobPostForm />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseJobPostModal} color="secondary">
            Cancel
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default CampaignManagement;