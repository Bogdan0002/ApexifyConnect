import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Typography, TextField, Button, Paper, Box } from '@mui/material';
import axiosInstance from '../api/axiosInstace';

const ApplicationForm = () => {
    const { jobId } = useParams();
    const navigate = useNavigate();
    const userData = JSON.parse(localStorage.getItem('user') || '{}');
    const creatorId = userData.userResponse.id;

  const [formData, setFormData] = useState({
    coverLetter: '',
    portfolio: '',
    expectedRate: ''
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axiosInstance.post(`/applications/apply?jobPostId=${jobId}&creatorId=${creatorId}&coverLetter=${formData.coverLetter}`);
      navigate('/opportunity-board');
    } catch (error) {
      console.error('Error:', error);
    }
};


  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  return (
    <Container maxWidth="md">
      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        <Typography variant="h4" gutterBottom>
          Submit Your Application
        </Typography>
        
        <form onSubmit={handleSubmit}>
          <Box sx={{ mb: 3 }}>
            <TextField
              fullWidth
              multiline
              rows={6}
              label="Cover Letter"
              name="coverLetter"
              value={formData.coverLetter}
              onChange={handleChange}
              required
              placeholder="Tell us why you're perfect for this position..."
              sx={{ mb: 3 }}
            />

            <TextField
              fullWidth
              label="Portfolio Link"
              name="portfolio"
              value={formData.portfolio}
              onChange={handleChange}
              placeholder="Link to your portfolio or previous work"
              sx={{ mb: 3 }}
            />

            <TextField
              fullWidth
              label="Expected Rate"
              name="expectedRate"
              type="number"
              value={formData.expectedRate}
              onChange={handleChange}
              placeholder="Your expected rate for this project"
              sx={{ mb: 3 }}
            />
          </Box>

          <Button
            type="submit"
            variant="contained"
            color="primary"
            size="large"
            fullWidth
          >
            Submit Application
          </Button>
        </form>
      </Paper>
    </Container>
  );
};

export default ApplicationForm;
