import { AppBar, Toolbar, Button, Box, Container } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../api/AuthContext';
import Notification from '../components/common/NotificationComponent';

const Navigation = () => {
  const { isLoggedIn, logout, notification, setNotification } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logout();
      navigate('/');
    } catch (error) {
      console.error('Logout failed:', error);
      setNotification({
        show: true,
        message: 'Logout failed. Please try again.'
      });
    }
  };

  const handleHomeClick = () => {
    navigate('/');
  };

  const handleNavigation = (path: string) => {
    try {
      navigate(path);
    } catch (error) {
      console.error('Navigation failed:', error);
    }
  };

  return (
    <>
      <AppBar position="static">
        <Container maxWidth="lg">
          <Toolbar disableGutters>
            <Box sx={{ flexGrow: 1 }}>
              <Button 
                color="inherit" 
                onClick={handleHomeClick}
                sx={{ fontSize: '1.2rem' }}
              >
                ApexifyConnect
              </Button>
            </Box>
            <Box sx={{ display: 'flex', gap: 2 }}>
              {isLoggedIn ? (
                <>
                  <Button 
                    color="inherit"
                    onClick={() => handleNavigation('/opportunity-board')}
                  >
                    Opportunities
                  </Button>
                  <Button 
                    color="inherit"
                    onClick={() => handleNavigation('/campaign-management')}
                  >
                    Campaigns
                  </Button>
                  <Button 
                    color="inherit"
                    onClick={() => handleNavigation('/messaging')}
                  >
                    Messages
                  </Button>
                  <Button 
                    color="inherit"
                    onClick={() => handleNavigation('/profile')}
                  >
                    My Profile
                  </Button>
                  <Button 
                    color="inherit"
                    onClick={handleLogout}
                  >
                    Logout
                  </Button>
                </>
              ) : (
                <>
                  <Button 
                    color="inherit"
                    onClick={() => handleNavigation('/login')}
                  >
                    Login
                  </Button>
                  <Button 
                    color="inherit"
                    variant="outlined"
                    onClick={() => handleNavigation('/register')}
                  >
                    Register
                  </Button>
                </>
              )}
            </Box>
          </Toolbar>
        </Container>
      </AppBar>

      {notification && (
        <Notification
          open={notification.show}
          message={notification.message}
          onClose={() => setNotification({ show: false, message: '' })}
        />
      )}
    </>
  );
};

export default Navigation;