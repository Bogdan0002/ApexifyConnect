import { useState } from "react";
import { loginUser } from "../api/UserService";
import { useNavigate } from "react-router-dom";
import Button from "./common/Button";
import { Container, Typography, Box, Select, MenuItem, FormControl, InputLabel, SelectChangeEvent, TextField } from '@mui/material';
import styles from "./LoginForm.module.css";
import { useAuth } from "../api/AuthContext";

const LoginForm = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    role: "content-creator", // default role
  });
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleTextChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSelectChange = (e: SelectChangeEvent<string>) => {
    const { value } = e.target;
    setFormData(prev => ({
      ...prev,
      role: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    try {
      await loginUser(formData.email, formData.password, formData.role);
      login();
      navigate("/"); // home redirect
    } catch (error: any) {
      setMessage(error.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="sm">
      <Box className={styles.loginForm}>
        <Typography variant="h4" component="h2" gutterBottom>
          Login
        </Typography>
        <form onSubmit={handleSubmit}>
          <TextField
            type="email"
            id="email"
            name="email"
            label="Email"
            value={formData.email}
            onChange={handleTextChange}
            required
            fullWidth
            margin="normal"
          />
          <TextField
            type="password"
            id="password"
            name="password"
            label="Password"
            value={formData.password}
            onChange={handleTextChange}
            required
            fullWidth
            margin="normal"
          />
          <FormControl fullWidth margin="normal">
            <InputLabel id="role-label">Role</InputLabel>
            <Select
              labelId="role-label"
              id="role"
              name="role"
              value={formData.role}
              onChange={handleSelectChange}
              label="Role"
            >
              <MenuItem value="content-creator">Content Creator</MenuItem>
              <MenuItem value="company">Company</MenuItem>
            </Select>
          </FormControl>
          <Button type="submit" disabled={loading}>
            {loading ? "Logging in..." : "Login"}
          </Button>
          {message && <Typography color="error" className={styles.message}>{message}</Typography>}
        </form>
      </Box>
    </Container>
  );
};

export default LoginForm;
