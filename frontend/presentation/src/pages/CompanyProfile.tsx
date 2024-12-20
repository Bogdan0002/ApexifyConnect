import { useEffect, useState } from "react";
import { getCompanyProfile, getCompanyJobPosts, getCompanyApplications, updateApplicationStatus, getCompanyProjects, getCompanyCollaborations } from "../api/UserService";
import { 
  Container, 
  Typography, 
  Box, 
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  CircularProgress,
  Paper,
  Grid,
  Card,
  CardContent,
  List,
  ListItem,
  ListItemText,
  ListItemSecondaryAction,
  IconButton
} from '@mui/material';
import JobPostForm from "../components/common/JobPostForm";
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';

interface CompanyProfileData {
  companyName: string;
  email: string;
  businessLicense: string;
}

interface Application {
  id: string;
  jobTitle: string;
  status: string;
  jobId: string;
}

interface JobPost {
  id: string;
  title: string;
  status: string;
}

const CompanyProfile = () => {
  const [profile, setProfile] = useState<CompanyProfileData | null>(null);
  const [openJobPostModal, setOpenJobPostModal] = useState(false);
  const [jobPosts, setJobPosts] = useState<JobPost[]>([]);
  const [applications, setApplications] = useState<Application[]>([]);
  const [projects, setProjects] = useState([]);
  const [collaborations, setCollaborations] = useState([]);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await getCompanyProfile();
        setProfile(response.data);
      } catch (error) {
        console.error("Error fetching profile:", error);
      }
    };

    const fetchJobPosts = async () => {
      try {
        const response = await getCompanyJobPosts();
        setJobPosts(response.data);
      } catch (error) {
        console.error("Error fetching job posts:", error);
      }
    };

    const fetchApplications = async (selectedJobId: number) => {
      try {
        const response = await getCompanyApplications(selectedJobId);
        setApplications(response.data);
      } catch (error) {
        console.error("Error fetching applications:", error);
      }
    };

    const fetchProjects = async () => {
      try {
        const response = await getCompanyProjects();
        setProjects(response.data);
      } catch (error) {
        console.error("Error fetching projects:", error);
      }
    };

    const fetchCollaborations = async () => {
      try {
        const response = await getCompanyCollaborations();
        setCollaborations(response.data);
      } catch (error) {
        console.error("Error fetching collaborations:", error);
      }
    };

    fetchProfile();
    fetchJobPosts();
    fetchApplications(Number(jobPosts[0]?.id) || 0);
    fetchProjects();
    fetchCollaborations();
  }, []);

  const handleOpenJobPostModal = () => setOpenJobPostModal(true);
  const handleCloseJobPostModal = () => setOpenJobPostModal(false);

  const handleUpdateApplicationStatus = async (applicationId: string, status: string, jobId: string) => {
    try {
      await updateApplicationStatus(applicationId, status);
      setApplications(applications.map(app => app.id === applicationId ? { ...app, status } : app));

      if (status === 'Accepted') {
        setJobPosts(jobPosts.map(job => job.id === jobId ? { ...job, status: 'Closed' } : job));
      }
    } catch (error) {
      console.error("Error updating application status:", error);
    }
  };

  const handleCompleteProject = (jobId: string) => {
    setJobPosts(jobPosts.map(job => job.id === jobId ? { ...job, status: 'Completed' } : job));
  };

  const getDisplayStatus = (status: string) => {
    if (status === 'Closed') {
      return 'Active Collaboration';
    } else if (status === 'Completed') {
      return 'Completed Projects';
    }
    return status;
  };

  if (!profile) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Container maxWidth="lg">
      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        <Grid container spacing={4}>
          <Grid item xs={12} md={8}>
            <Typography variant="h4" gutterBottom>
              Company Profile
            </Typography>
            <Box sx={{ mb: 4 }}>
              <Typography variant="h5" gutterBottom>
                {profile.companyName || "N/A"}
              </Typography>
              <Typography color="textSecondary" gutterBottom>
                {profile.email || "N/A"}
              </Typography>
              <Typography variant="body1">
                Business License: {profile.businessLicense || "N/A"}
              </Typography>
            </Box>

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
                        Jobs in Total
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

      
          </Grid>

          <Grid item xs={12} md={4}>
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
          </Grid>
        </Grid>

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
      </Paper>
    </Container>
  );
};

export default CompanyProfile;