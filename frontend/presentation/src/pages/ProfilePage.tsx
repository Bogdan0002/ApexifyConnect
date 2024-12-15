import { useEffect, useState } from "react";
import styles from "./Profile.module.css";
import { getProfilePicture, getUserProfile, updateProfilePicture } from "../api/UserService";
import axiosInstance from "../api/axiosInstace";
import Button from "../components/common/Button";

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

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const response = await getUserProfile();
        console.log("User profile data:", response.data); // Debugging log
        setUser(response.data);
        if (response.data.role.toLowerCase() === "content_creator") {
          const profilePictureResponse = await getProfilePicture(response.data.email);
          console.log("Profile picture URL:", profilePictureResponse.data); // Debugging log
          setProfilePicture(profilePictureResponse.data);
        }
      } catch (error) {
        console.error("Error fetching user profile:", error);
      }
    };

    fetchUserProfile();
  }, []);

  const handleProfilePictureChange = async () => {
    if (newProfilePicture && user) {
      const formData = new FormData();
      formData.append("file", newProfilePicture);
      try {
        // Assuming you have an endpoint to upload the file and get the URL
        const uploadResponse = await axiosInstance.post("/uploads/profile-picture", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });
        const profilePictureUrl = uploadResponse.data;
        console.log("Uploaded profile picture URL:", profilePictureUrl); // Debugging log
        await updateProfilePicture(user.email, profilePictureUrl);
        setProfilePicture(profilePictureUrl);
        setMessage("Profile picture updated successfully");
      } catch (error) {
        console.error("Error updating profile picture:", error);
        setMessage("Failed to update profile picture");
      }
    }
  };

  if (!user) {
    return <p>Loading...</p>;
  }

  console.log("User role:", user.role); // Debugging log

  return (
    <div className={styles.profile}>
      <h1>Profile</h1>
      <p>Email: {user.email}</p>
      <p>Role: {user.role}</p>
      {user.role.toLowerCase() === "content_creator" && (
        <div className={styles.contentCreatorProfile}>
          <h2>Content Creator Profile</h2>
          {profilePicture && <img src={profilePicture} alt="Profile" className={styles.profilePicture} />}
          <input type="file" onChange={(e) => setNewProfilePicture(e.target.files?.[0] || null)} />
          <Button onClick={handleProfilePictureChange}>Update Profile Picture</Button>
          {message && <p className={styles.message}>{message}</p>}
        </div>
      )}
    </div>
  );
};

export default Profile;