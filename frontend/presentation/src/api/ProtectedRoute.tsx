import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

interface ProtectedRouteProps {
    children: React.ReactNode;
    allowedRole: string;
  }
  
  const ProtectedRoute = ({ children, allowedRole }: ProtectedRouteProps) => {
    const { isLoggedIn } = useAuth();
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    const userRole = user.role?.toLowerCase();
  
    if (!isLoggedIn) return <Navigate to="/login" />;
    if (userRole !== allowedRole) return <Navigate to="/" />;
    
    return <>{children}</>;
  };
  