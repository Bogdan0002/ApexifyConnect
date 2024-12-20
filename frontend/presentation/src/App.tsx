import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import { Box, CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import CompanyRegistrationForm from "./components/CompanyRegistrationForm";
import CreatorRegistrationForm from "./components/CreatorRegistrationForm";
import LoginForm from "./components/LoginForm";
import Home from "./pages/Home";
import OpportunityBoard from "./components/OpportunityBoard";
import CampaignManagement from "./components/CampaignManagement";
import MessagingSystem from "./components/MessagingSystem";
import Navigation from "./pages/Navigation";
import CompanyProfile from "./pages/CompanyProfile";
import CreatorProfile from "./pages/CreatorProfile";
import { AuthProvider, useAuth } from "./api/AuthContext";
import RegisterAsChoice from "./components/RegisterAsChoice";
import ApplicationForm from "./components/ApplicationForm";
import ApplicationsPage from "./pages/ApplicationsPage";
import CompanyApplication from "./components/CompanyApplications";

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

interface ProtectedRouteProps {
  children: JSX.Element;
  allowedRole?: string;
}

const ProtectedRoute = ({ children, allowedRole }: ProtectedRouteProps) => {
  const { isLoggedIn } = useAuth();
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  const userRole = user.userResponse?.role;  // Access nested role property
  
  console.log('Protected Route Check:', { isLoggedIn, userRole, allowedRole });

  if (!isLoggedIn) return <Navigate to="/login" />;
  if (allowedRole && userRole !== allowedRole) return <Navigate to="/" />;
  
  return children;
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
                <Route path="/" element={<Home />} />
                <Route path="/register" element={<RegisterAsChoice />} />
                <Route path="/login" element={<LoginForm />} />
                <Route path="/register/company" element={<CompanyRegistrationForm />} />
                <Route path="/company/applications" element={
  <ProtectedRoute allowedRole="Company">
    <CompanyApplication/>
  </ProtectedRoute>
} />

                
     
                <Route path="/register/creator" element={<CreatorRegistrationForm />} />
                
                <Route path="/profile/creator" element={
  <ProtectedRoute allowedRole="Content Creator">
    <CreatorProfile />
  </ProtectedRoute>
} />



<Route path="/profile/company" element={
  <ProtectedRoute allowedRole="Company">
    <CompanyProfile />
  </ProtectedRoute>
} />

                <Route path="/opportunity-board" element={
                  <ProtectedRoute>
                    <OpportunityBoard />
                  </ProtectedRoute>
                } />

<Route path="/apply/:jobId" element={
  <ProtectedRoute allowedRole="Content Creator">
    <ApplicationForm />
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
              </Routes>
            </Box>
          </Box>
        </Router>
      </ThemeProvider>
    </AuthProvider>
  );
}
export default App;
