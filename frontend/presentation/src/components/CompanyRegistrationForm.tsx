import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Typography, Box, TextField } from '@mui/material';
import styles from "./RegistrationForm.module.css";
import { registerCompany } from "../api/UserService";
import Button from "./common/Button";

const CompanyRegistrationForm = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    companyName: "",
    businessLicense: "",
  });
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    try {
      await registerCompany(formData);
      setMessage("Registration successful!");
      navigate("/login");
    } catch (error: any) {
      setMessage(error.message || "Registration failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="sm">
      <Box className={styles.registrationForm}>
        <Typography variant="h4" component="h2" gutterBottom>
          Company Registration
        </Typography>
        <form onSubmit={handleSubmit}>
          <TextField
            type="email"
            name="email"
            label="Email"
            value={formData.email}
            onChange={handleChange}
            required
            fullWidth
            margin="normal"
          />
          <TextField
            type="password"
            name="password"
            label="Password"
            value={formData.password}
            onChange={handleChange}
            required
            fullWidth
            margin="normal"
          />
          <TextField
            type="text"
            name="companyName"
            label="Company Name"
            value={formData.companyName}
            onChange={handleChange}
            required
            fullWidth
            margin="normal"
          />
          <TextField
            type="text"
            name="businessLicense"
            label="Business License"
            value={formData.businessLicense}
            onChange={handleChange}
            required
            fullWidth
            margin="normal"
          />
          <Button type="submit" disabled={loading}>
            {loading ? "Registering..." : "Register"}
          </Button>
          {message && <Typography color="error" className={styles.message}>{message}</Typography>}
        </form>
      </Box>
    </Container>
  );
};

export default CompanyRegistrationForm;