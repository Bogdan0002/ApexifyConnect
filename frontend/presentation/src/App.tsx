import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import { Box, CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import RegistrationForm from "./components/RegistrationForm";
import LoginForm from "./components/LoginForm";
import Home from "./pages/Home";
import Profile from "./pages/Profile";
import OpportunityBoard from "./components/OpportunityBoard";
import CampaignManagement from "./components/CampaignManagement";
import MessagingSystem from "./components/MessagingSystem";
import Navigation from "./pages/Navigation";
import { AuthProvider, useAuth } from "./api/AuthContext";

// Create theme instance
const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

const ProtectedRoute = ({ children }: { children: JSX.Element }) => {
  const { isLoggedIn } = useAuth();
  return isLoggedIn ? children : <Navigate to="/login" />;
};

export { ProtectedRoute };

function App() {
  return (
    <AuthProvider>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Router>
          <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
            <Navigation />
            <Box component="main" sx={{ flexGrow: 1, pt: 2 }}>
              <Routes>
                <Route path="/register" element={<RegistrationForm />} />
                <Route path="/login" element={<LoginForm />} />
                <Route path="/profile" element={
                  <ProtectedRoute>
                    <Profile />
                  </ProtectedRoute>
                } />
                <Route path="/opportunity-board" element={
                  <ProtectedRoute>
                    <OpportunityBoard />
                  </ProtectedRoute>
                } />
                <Route path="/campaign-management" element={
                  <ProtectedRoute>
                    <CampaignManagement />
                  </ProtectedRoute>
                } />
                <Route path="/messaging" element={
                  <ProtectedRoute>
                    <MessagingSystem />
                  </ProtectedRoute>
                } />
                <Route path="/" element={<Home />} />
              </Routes>
            </Box>
          </Box>
        </Router>
      </ThemeProvider>
    </AuthProvider>
  );
}

export default App;