import { useEffect, useState } from "react";
import { getCompanyProfile } from "../api/UserService";
import { 
  Container, 
  Typography, 
  Box, 
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  CircularProgress
} from '@mui/material';
import styles from "./Profile.module.css";
import JobPostForm from "../components/common/JobPostForm";

interface CompanyProfileData {
  email: string;
  companyName: string;
  businessLicense: string;
}

const CompanyProfile = () => {
  const [profile, setProfile] = useState<CompanyProfileData | null>(null);
  const [openJobPostModal, setOpenJobPostModal] = useState(false);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await getCompanyProfile();
        setProfile(response.data);
      } catch (error) {
        console.error("Error fetching profile:", error);
      }
    };

    fetchProfile();
  }, []);

  const handleOpenJobPostModal = () => setOpenJobPostModal(true);
  const handleCloseJobPostModal = () => setOpenJobPostModal(false);

  if (!profile) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Container maxWidth="md">
      <Box className={styles.profile}>
        <Typography variant="h4" component="h1" gutterBottom>
          Company Profile
        </Typography>

        <Box className={styles.profileContent}>
          <Box className={styles.userInfo}>
            <Typography variant="h6">{profile.companyName}</Typography>
            <Typography color="textSecondary">{profile.email}</Typography>
          </Box>

          <Box mt={4}>
            <Button 
              variant="contained" 
              color="primary" 
              onClick={handleOpenJobPostModal}
            >
              Create Job Post
            </Button>

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
          </Box>
        </Box>
      </Box>
    </Container>
  );
};

export default CompanyProfile;
