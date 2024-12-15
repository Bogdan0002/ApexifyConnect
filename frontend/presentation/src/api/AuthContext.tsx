import { createContext, useContext, useState, useEffect } from 'react';

interface NotificationType {
    show: boolean;
    message: string;
  }
  
  interface AuthContextType {
    isLoggedIn: boolean;
    login: () => void;
    logout: () => Promise<void>;
    notification: NotificationType;
    setNotification: (notification: NotificationType) => void;
  }

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(() => {
    return localStorage.getItem('isLoggedIn') === 'true';
  });
  
  const [notification, setNotification] = useState<NotificationType>({
    show: false,
    message: ''
  });

  useEffect(() => {
    // Check token validity on mount
    const token = localStorage.getItem('token');
    if (!token) {
      setIsLoggedIn(false);
      localStorage.removeItem('isLoggedIn');
    }
  }, []);

  const login = async () => {
    try {
      setIsLoggedIn(true);
      localStorage.setItem('isLoggedIn', 'true');
      setNotification({
        show: true,
        message: 'Successfully logged in!'
      });
    } catch (error) {
      setNotification({
        show: true,
        message: 'Login failed'
      });
      throw error;
    }
  };

  const logout = async () => {
    try {
      setIsLoggedIn(false);
      localStorage.removeItem('isLoggedIn');
      localStorage.removeItem('token');
      setNotification({
        show: true,
        message: 'Successfully logged out!'
      });
    } catch (error) {
      setNotification({
        show: true,
        message: 'Logout failed'
      });
      throw error;
    }
  };

  const value: AuthContextType = {
    isLoggedIn,
    login,
    logout,
    notification,
    setNotification
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

export default AuthContext;