import axios, { AxiosResponse } from "axios";
import axiosInstance from "./axiosInstace";

const API_BASE_URL = "http://localhost:8080/api/users";

interface ContentCreatorData {
  email: string;
  password: string;
  profilePicture?: string; // Optional
  bio?: string; // Optional
}

interface CompanyData {
  email: string;
  password: string;
  companyName: string;
  businessLicense: string;
}

interface UserResponseDTO {
  email: string;
  role: string;
  token: string;
  profilePicture?: string;
}

// API for registering a Content Creator

export const registerContentCreator = async (
    creatorData: ContentCreatorData
  ): Promise<AxiosResponse<UserResponseDTO>> => {
    return axios.post("http://localhost:8080/api/users/register/content-creator", creatorData);
  };

// API for registering a Company
export const registerCompany = async (
  companyData: CompanyData
): Promise<AxiosResponse<UserResponseDTO>> => {
  return axios.post("http://localhost:8080/api/users/register/company", companyData);
};

// UserService.ts
export const uploadProfilePicture = async (file: File): Promise<string> => {
    const formData = new FormData();
    formData.append("file", file);
  
    try {
      const response = await axios.post(
        "http://localhost:8080/api/uploads/profile-picture",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
      return response.data; // The file URL returned by the backend
    } catch (error: any) {
      console.error("Error uploading profile picture:", error);
      throw new Error(error.response?.data?.message || "File upload failed");
    }
  };

  // API for logging in a user
  export const loginUser = async (
    email: string,
    password: string,
    role: string
  ): Promise<AxiosResponse<UserResponseDTO>> => {
    const endpoint = role === "content-creator" 
      ? "/users/login/content-creator" 
      : "/users/login/company";
    try {
      const response = await axiosInstance.post(endpoint, { email, password });
      localStorage.setItem('user', JSON.stringify(response.data));
      localStorage.setItem('token', response.data.token); // Store the JWT token
      return response;
    } catch (error: any) {
      throw new Error(error.response?.data || "Login failed");
    }
  };

  export const logoutUser = async (): Promise<void> => {
    try {
      await axiosInstance.post('/users/logout');
      localStorage.removeItem('user');
      localStorage.removeItem('token');
      localStorage.removeItem('isLoggedIn');
    } catch (error: any) {
      console.error('Logout error:', error);
      throw new Error(error.response?.data || "Logout failed");
    }
  };

  export const getUserProfile = async (): Promise<AxiosResponse<UserResponseDTO>> => {
    return axiosInstance.get('/users/profile');
  };

  export const getProfilePicture = async (email: string): Promise<AxiosResponse<string>> => {
    return axiosInstance.get(`/users/profile-picture?email=${email}`);
  };
  
  export const updateProfilePicture = async (email: string, profilePictureUrl: string): Promise<AxiosResponse<string>> => {
    return axiosInstance.put(`/users/profile-picture?email=${email}&profilePictureUrl=${profilePictureUrl}`);
  };
