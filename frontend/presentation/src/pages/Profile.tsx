// import { useEffect, useState } from "react";
// import { getUserProfile } from "../api/UserService";
// import { CircularProgress, Box } from '@mui/material';
// import CreatorProfile from './CreatorProfile';
// import CompanyProfile from './CompanyProfile';

// // Router component for handling myPage depending on role. 

// const Profile = () => {
//   const [userRole, setUserRole] = useState<string | null>(null);

//   useEffect(() => {
//     const fetchUserRole = async () => {
//       try {
//         const response = await getUserProfile();
//         setUserRole(response.data.role.toLowerCase());
//       } catch (error) {
//         console.error("Error fetching user role:", error);
//       }
//     };

//     fetchUserRole();
//   }, []);

//   if (!userRole) {
//     return (
//       <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
//         <CircularProgress />
//       </Box>
//     );
//   }

//   return userRole === "content_creator" ? <CreatorProfile /> : <CompanyProfile />;
// };

// export default Profile;