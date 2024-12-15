import { Box, Container, Typography, Button, Grid, Card, CardContent } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../api/AuthContext';
import styles from './Home.module.css';


const Home = () => {
  const navigate = useNavigate();
  const { isLoggedIn } = useAuth();

  return (
    <Box>
      {/* Hero Section */}
      <Box className={styles.hero}>
        <Container maxWidth="lg">
          <Typography variant="h2" component="h1" gutterBottom>
            Welcome to ApexifyConnect
          </Typography>
          <Typography variant="h5" paragraph>
            Connecting Content Creators with Businesses for Authentic Collaborations
          </Typography>
          {!isLoggedIn && (
            <Box mt={4}>
              <Button 
                variant="contained" 
                color="primary" 
                size="large"
                onClick={() => navigate('/register')}
                sx={{ mr: 2 }}
              >
                Get Started
              </Button>
              <Button 
                variant="outlined" 
                color="primary" 
                size="large"
                onClick={() => navigate('/login')}
              >
                Login
              </Button>
            </Box>
          )}
        </Container>
      </Box>

      {/* Features Section */}
      <Container maxWidth="lg" sx={{ py: 8 }}>
        <Grid container spacing={4}>
          <Grid item xs={12} md={4}>
            <Card className={styles.featureCard}>
              <CardContent>
                <Typography variant="h5" component="h2" gutterBottom>
                  For Content Creators
                </Typography>
                <Typography variant="body1">
                  Showcase your portfolio, find opportunities, and grow your business
                </Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} md={4}>
            <Card className={styles.featureCard}>
              <CardContent>
                <Typography variant="h5" component="h2" gutterBottom>
                  For Businesses
                </Typography>
                <Typography variant="body1">
                  Find the perfect creators for your brand and manage campaigns effectively
                </Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} md={4}>
            <Card className={styles.featureCard}>
              <CardContent>
                <Typography variant="h5" component="h2" gutterBottom>
                  AI-Powered Matching
                </Typography>
                <Typography variant="body1">
                  Our smart algorithm connects the right creators with the right businesses
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Container>
    </Box>
  );
};

export default Home;