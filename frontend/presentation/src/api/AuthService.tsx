import axios from 'axios';

export const AuthService = {
  async logout() {
    try {
      await axios.post('/api/users/logout');
      localStorage.removeItem('token');
      localStorage.removeItem('isLoggedIn');
    } catch (error) {
      console.error('Logout failed:', error);
      throw error;
    }
  }
};