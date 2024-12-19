// import { useState } from "react";
// import { registerContentCreator, registerCompany, uploadProfilePicture } from "../api/UserService";
// import { useNavigate } from "react-router-dom";
// import Button from "./common/Button";
// import { Container, Typography, Box, Select, MenuItem, FormControl, InputLabel, SelectChangeEvent, TextField } from '@mui/material';
// import styles from "./RegistrationForm.module.css";

// type FormData = {
//   role: "CONTENT_CREATOR" | "COMPANY";
//   email: string;
//   password: string;
//   bio: string;
//   companyName: string;
//   businessLicense: string;
//   profilePicture: File | null;
// };

// const RegistrationForm = () => {
//   const [formData, setFormData] = useState<FormData>({
//     email: "",
//     password: "",
//     role: "CONTENT_CREATOR",
//     bio: "",
//     companyName: "",
//     businessLicense: "",
//     profilePicture: null,
//   });
//   const [message, setMessage] = useState("");
//   const [loading, setLoading] = useState(false);
//   const navigate = useNavigate();

//   const handleTextChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
//     const { name, value } = e.target;
//     setFormData(prev => ({
//       ...prev,
//       [name]: value,
//     }));
//   };

//   const handleSelectChange = (e: SelectChangeEvent<string>) => {
//     const { value } = e.target;
//     setFormData(prev => ({
//       ...prev,
//       role: value as "CONTENT_CREATOR" | "COMPANY",
//     }));
//   };

//   const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
//     const file = e.target.files?.[0] || null;
//     setFormData(prev => ({
//       ...prev,
//       profilePicture: file,
//     }));
//   };

//   const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
//     e.preventDefault();
//     setLoading(true);
//     setMessage("");

//     try {
//       let profilePictureUrl = "";
//       if (formData.profilePicture) {
//         profilePictureUrl = await uploadProfilePicture(formData.profilePicture);
//       }

//       if (formData.role === "CONTENT_CREATOR") {
//         await registerContentCreator({
//           email: formData.email,
//           password: formData.password,
//           bio: formData.bio,
//           profilePicture: profilePictureUrl,
//         });
//       } else {
//         await registerCompany({
//           email: formData.email,
//           password: formData.password,
//           companyName: formData.companyName,
//           businessLicense: formData.businessLicense,
//         });
//       }

//       setMessage("Registration successful!");
//       navigate("/login");
//     } catch (error: any) {
//       setMessage(error.message || "Registration failed");
//     } finally {
//       setLoading(false);
//     }
//   };

//   return (
//     <Container maxWidth="sm">
//       <Box className={styles.registrationForm}>
//         <Typography variant="h4" component="h2" gutterBottom>
//           Register
//         </Typography>
//         <form onSubmit={handleSubmit}>
//           <TextField
//             type="email"
//             id="email"
//             name="email"
//             label="Email"
//             value={formData.email}
//             onChange={handleTextChange}
//             required
//             fullWidth
//             margin="normal"
//           />
//           <TextField
//             type="password"
//             id="password"
//             name="password"
//             label="Password"
//             value={formData.password}
//             onChange={handleTextChange}
//             required
//             fullWidth
//             margin="normal"
//           />
//           <FormControl fullWidth margin="normal">
//             <InputLabel id="role-label">Role</InputLabel>
//             <Select
//               labelId="role-label"
//               id="role"
//               name="role"
//               value={formData.role}
//               onChange={handleSelectChange}
//               label="Role"
//             >
//               <MenuItem value="CONTENT_CREATOR">Content Creator</MenuItem>
//               <MenuItem value="COMPANY">Company</MenuItem>
//             </Select>
//           </FormControl>
//           {formData.role === "CONTENT_CREATOR" && (
//             <TextField
//               type="text"
//               id="bio"
//               name="bio"
//               label="Bio"
//               value={formData.bio}
//               onChange={handleTextChange}
//               required
//               fullWidth
//               margin="normal"
//             />
//           )}
//           {formData.role === "COMPANY" && (
//             <>
//               <TextField
//                 type="text"
//                 id="companyName"
//                 name="companyName"
//                 label="Company Name"
//                 value={formData.companyName}
//                 onChange={handleTextChange}
//                 required
//                 fullWidth
//                 margin="normal"
//               />
//               <TextField
//                 type="text"
//                 id="businessLicense"
//                 name="businessLicense"
//                 label="Business License"
//                 value={formData.businessLicense}
//                 onChange={handleTextChange}
//                 required
//                 fullWidth
//                 margin="normal"
//               />
//             </>
//           )}
//           <TextField
//             type="file"
//             id="profilePicture"
//             name="profilePicture"
//             label="Profile Picture"
//             onChange={handleFileChange}
//             fullWidth
//             margin="normal"
//             InputLabelProps={{ shrink: true }}
//           />
//           <Button type="submit" disabled={loading}>
//             {loading ? "Registering..." : "Register"}
//           </Button>
//           {message && <Typography color="error" className={styles.message}>{message}</Typography>}
//         </form>
//       </Box>
//     </Container>
//   );
// };

// export default RegistrationForm;