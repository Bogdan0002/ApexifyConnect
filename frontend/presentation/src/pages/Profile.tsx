import { useEffect, useState } from "react";
import { getUserProfile, getProfilePicture } from "../api/UserService";
import { 
  Container, 
  Typography, 
  Box, 
  Avatar, 
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  CircularProgress
} from '@mui/material';
import styles from "./Profile.module.css";
import JobPostForm from "../components/common/JobPostForm";

interface UserResponseDTO {
  email: string;
  role: string;
  profilePicture?: string;
}

const Profile = () => {
  const [user, setUser] = useState<UserResponseDTO | null>(null);
  const [openJobPostModal, setOpenJobPostModal] = useState(false);

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const response = await getUserProfile();
        setUser(response.data);
      } catch (error) {
        console.error("Error fetching user profile:", error);
      }
    };

    fetchUserProfile();
  }, []);

  const handleOpenJobPostModal = () => setOpenJobPostModal(true);
  const handleCloseJobPostModal = () => setOpenJobPostModal(false);

  if (!user) {
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
          Profile
        </Typography>

        {/* Company Section */}
        {user.role.toLowerCase() === "company" && (
          <Box mt={4}>
            <Button 
              variant="contained" 
              color="primary" 
              onClick={handleOpenJobPostModal}
            >
              Create Job Post
            </Button>

            {/* Job Post Form Modal */}
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
        )}

        {/* User Info */}
        <Box className={styles.userInfo}>
          <Typography>Email: {user.email}</Typography>
          <Typography>Role: {user.role}</Typography>
        </Box>
      </Box>
    </Container>
  );
};

export default Profile;
