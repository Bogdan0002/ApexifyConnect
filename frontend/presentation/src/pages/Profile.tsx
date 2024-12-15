import { useEffect, useState } from "react";
import { getUserProfile, getProfilePicture, updateProfilePicture } from "../api/UserService";
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
import axiosInstance from "../api/axiosInstace";

interface UserResponseDTO {
  email: string;
  role: string;
  profilePicture?: string;
}

const Profile = () => {
  const [user, setUser] = useState<UserResponseDTO | null>(null);
  const [profilePicture, setProfilePicture] = useState<string | null>(null);
  const [newProfilePicture, setNewProfilePicture] = useState<File | null>(null);
  const [message, setMessage] = useState<string>("");
  const [isUploading, setIsUploading] = useState(false);
  const [openDialog, setOpenDialog] = useState(false);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const response = await getUserProfile();
        setUser(response.data);
        if (response.data.role.toLowerCase() === "content_creator") {
          const profilePictureResponse = await getProfilePicture(response.data.email);
          setProfilePicture(profilePictureResponse.data);
        }
      } catch (error) {
        console.error("Error fetching user profile:", error);
        setMessage("Failed to load profile");
      }
    };

    fetchUserProfile();
  }, []);

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setNewProfilePicture(file);
      setPreviewUrl(URL.createObjectURL(file));
    }
  };

  const handleProfilePictureChange = async () => {
    if (!newProfilePicture || !user) return;

    setIsUploading(true);
    setMessage("");

    try {
      const formData = new FormData();
      formData.append("file", newProfilePicture);
      
      const uploadResponse = await axiosInstance.post("/uploads/profile-picture", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      
      const profilePictureUrl = uploadResponse.data;
      await updateProfilePicture(user.email, profilePictureUrl);
      
      setProfilePicture(profilePictureUrl);
      setMessage("Profile picture updated successfully");
      setOpenDialog(false);
      setPreviewUrl(null);
    } catch (error) {
      console.error("Error updating profile picture:", error);
      setMessage("Failed to update profile picture");
    } finally {
      setIsUploading(false);
    }
  };

  const handleUpdateClick = () => {
    setOpenDialog(true);
    setMessage("");
  };

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
        {user.role.toLowerCase() === "content_creator" && (
          <Box className={styles.contentCreatorProfile}>
            {profilePicture && <Avatar src={profilePicture} alt="Profile" className={styles.profilePicture} />}
            <Button 
              variant="contained" 
              color="primary" 
              onClick={handleUpdateClick}
              sx={{ mt: 2 }}
            >
              Update Profile Picture
            </Button>
          </Box>
        )}
        <Box className={styles.userInfo}>
          <Typography>Email: {user.email}</Typography>
          <Typography>Role: {user.role}</Typography>
        </Box>

        <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
          <DialogTitle>Update Profile Picture</DialogTitle>
          <DialogContent>
            <Box className={styles.dialogContent}>
              <input
                type="file"
                accept="image/*"
                onChange={handleFileSelect}
                style={{ marginBottom: '20px' }}
              />
              {previewUrl && (
                <Box className={styles.previewImage}>
                  <img src={previewUrl} alt="Preview" />
                </Box>
              )}
            </Box>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
            <Button 
              onClick={handleProfilePictureChange}
              disabled={!newProfilePicture || isUploading}
              variant="contained"
              color="primary"
            >
              {isUploading ? <CircularProgress size={24} /> : "Confirm"}
            </Button>
          </DialogActions>
        </Dialog>

        {message && (
          <Typography 
            color={message.includes("Failed") ? "error" : "success"} 
            className={styles.message}
          >
            {message}
          </Typography>
        )}
      </Box>
    </Container>
  );
};

export default Profile;