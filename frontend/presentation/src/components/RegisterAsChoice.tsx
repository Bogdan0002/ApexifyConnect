import { Box, Button, Container, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import styles from './RegisterAsChoice.module.css';

const RegistrationChoice = () => {
  const navigate = useNavigate();

  return (
    <Container maxWidth="sm">
      <Box className={styles.registrationChoice}>
        <Typography variant="h4" component="h2" gutterBottom>
          Choose Registration Type
        </Typography>
        <Box className={styles.buttonGroup}>
          <Button 
            variant="contained" 
            onClick={() => navigate('/register/company')}
            fullWidth
            size="large"
          >
            Register as Company
          </Button>
          <Button 
            variant="contained" 
            onClick={() => navigate('/register/creator')}
            fullWidth
            size="large"
          >
            Register as Content Creator
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default RegistrationChoice;