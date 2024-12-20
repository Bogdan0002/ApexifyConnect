import { useEffect, useState } from "react";
import { getCreatorProfile, uploadProfilePicture, updateProfilePicture } from "../api/UserService";
import axiosInstance from '../api/axiosInstace';
import {
  Container,
  Typography,
  Box,
  Paper,
  Grid,
  Divider,
  Avatar,
  CircularProgress,
  IconButton,
  Chip
} from '@mui/material';
import PhotoCamera from '@mui/icons-material/PhotoCamera';
import styles from "./Profile.module.css";

interface CreatorProfileData {
  email: string;
  firstName: string;
  lastName: string;
  bio: string;
  profilePicture?: string;
}

interface Application {
  id: number;
  jobPostId: number;
  jobTitle: string;
  contentCreatorName: string;
  coverLetter: string | null;
  status: string;
  appliedAt: string;
}


const CreatorProfile = () => {
  const [profile, setProfile] = useState<CreatorProfileData | null>(null);
  const [applications, setApplications] = useState<Application[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await getCreatorProfile();
        setProfile(response.data);
      } catch (error) {
        console.error("Error fetching profile:", error);
      }
    };
    
    const fetchApplications = async () => {
      try {
        const userData = JSON.parse(localStorage.getItem('user') || '{}');
        const creatorId = userData.userResponse?.id;
        const response = await axiosInstance.get(`/applications/creator/${creatorId}/applications`);
        console.log('Applications data:', response.data);
        setApplications(response.data);
      } catch (error) {
        console.error("Error fetching applications:", error);
      }
    };

    fetchProfile();
    fetchApplications();
  }, []);

  const handleImageUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file && profile) {
      setLoading(true);
      try {
        const uploadedUrl = await uploadProfilePicture(file);
        await updateProfilePicture(profile.email, uploadedUrl);
        setProfile(prev => prev ? {...prev, profilePicture: uploadedUrl} : null);
      } catch (error) {
        console.error("Error updating profile picture:", error);
      } finally {
        setLoading(false);
      }
    }
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
      <Paper elevation={3} className={styles.profile}>
        <Grid container spacing={4}>
          <Grid item xs={12} md={4}>
            <Box className={styles.profileImageSection}>
              <Box position="relative" display="inline-block">
                {profile.profilePicture ? (
                  <img
                    src={`http://localhost:8080${profile.profilePicture}`}
                    alt="Profile"
                    className={styles.profilePicture}
                  />
                ) : (
                  <Avatar
                    className={styles.defaultAvatar}
                    alt={`${profile.firstName} ${profile.lastName}`}
                  >
                    {profile.firstName[0]}
                  </Avatar>
                )}
                <input
                  accept="image/*"
                  type="file"
                  id="icon-button-file"
                  onChange={handleImageUpload}
                  style={{ display: 'none' }}
                />
                <label htmlFor="icon-button-file">
                  <IconButton 
                    component="span"
                    className={styles.uploadButton}
                    disabled={loading}
                  >
                    <PhotoCamera />
                  </IconButton>
                </label>
              </Box>
              <Typography variant="h5" className={styles.creatorName}>
                {profile.firstName} {profile.lastName}
              </Typography>
              <Typography color="textSecondary" className={styles.creatorEmail}>
                {profile.email}
              </Typography>
            </Box>
          </Grid>

          <Grid item xs={12} md={8}>
            <Box className={styles.profileContent}>
              <Typography variant="h4" gutterBottom className={styles.sectionTitle}>
                About Me
              </Typography>
              <Divider className={styles.divider} />
              <Typography variant="body1" className={styles.bio}>
                {profile.bio}
              </Typography>

              <Box className={styles.applicationsSection} mt={4}>
                <Typography variant="h4" gutterBottom>
                  My Applications
                </Typography>
                <Grid container spacing={2}>
                {applications.map((application) => (
  <Grid item xs={12} key={application.id}>
    <Paper elevation={2} sx={{ p: 2 }}>
      <Typography variant="h6" gutterBottom>
        {application.jobTitle}
      </Typography>
      <Typography variant="body2" color="textSecondary" paragraph>
        Cover Letter: {application.coverLetter || 'No cover letter provided'}
      </Typography>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Chip 
          label={application.status} 
          color={application.status === 'APPLIED' ? 'info' : 
                application.status === 'ACCEPTED' ? 'success' : 'error'}
        />
        <Typography variant="body2">
          Applied: {new Date(application.appliedAt).toLocaleDateString()}
        </Typography>
      </Box>
    </Paper>
  </Grid>
))}


                </Grid>
              </Box>
            </Box>
          </Grid>
        </Grid>
      </Paper>
    </Container>
  );
};

export default CreatorProfile;
