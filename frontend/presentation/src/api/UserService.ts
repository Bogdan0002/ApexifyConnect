import axios, { AxiosResponse } from "axios";
import axiosInstance from "./axiosInstace";

const API_BASE_URL = "http://localhost:8080/api/users";

interface ContentCreatorData {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  profilePicture?: string; 
  bio?: string; 
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
}

interface ContentCreatorResponseDTO {
  email: string;
  firstName: string;
  lastName: string;
  bio: string;
  role: string;
  profilePicture?: string;
}

interface CompanyResponseDTO {
  email: string;
  companyName: string;
  businessLicense: string;
  role: string;
}


interface JobPostRequest {
    title: string;
    description: string;
    budget: number;
    deadline: string; // ISO date format
    tagNames: string[]; // Tags as an array of strings
  }
  

// API for registering a Content Creator

export const registerContentCreator = async (
    creatorData: ContentCreatorData
  ): Promise<AxiosResponse<ContentCreatorResponseDTO>> => {
    return axios.post("http://localhost:8080/api/users/register/content-creator", creatorData);
  };

// API for registering a Company
export const registerCompany = async (
  companyData: CompanyData
): Promise<AxiosResponse<CompanyResponseDTO>> => {
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

  export const getCreatorProfile = async (): Promise<AxiosResponse<ContentCreatorResponseDTO>> => {
    return axiosInstance.get('/users/profile/creator');
  };

  export const getCompanyProfile = async (): Promise<AxiosResponse<CompanyResponseDTO>> => {
    return axiosInstance.get('/users/profile/company');
  };

  export const getProfilePicture = async (email: string): Promise<AxiosResponse<string>> => {
    return axiosInstance.get(`/users/profile-picture?email=${email}`);
  };
  
  export const updateProfilePicture = async (email: string, profilePictureUrl: string): Promise<AxiosResponse<string>> => {
    return axiosInstance.put(`/users/profile-picture?email=${email}&profilePictureUrl=${profilePictureUrl}`);
  };

  export const getCompanyJobPosts = async (): Promise<AxiosResponse<any>> => {
    return axiosInstance.get('/users/profile/company/jobs');
  };

  export const JobPostAPI = {
    createJobPost: (data: JobPostRequest): Promise<AxiosResponse<any>> => 
      axiosInstance.post('/job-posts/create', data),
  
    getAllJobPosts: (): Promise<AxiosResponse<any>> => 
      axiosInstance.get('/job-posts/all'),
  };

  export const getCompanyApplications = async (jobPostId: number): Promise<AxiosResponse<any>> => {
      return axiosInstance.get(`/applications/job/${jobPostId}`);
    };
  
  export const updateApplicationStatus = async (applicationId: string, status: string): Promise<AxiosResponse<any>> => {
    return axiosInstance.put(`/applications/${applicationId}/status`, { status });
  };
  
  export const getCompanyProjects = async (): Promise<AxiosResponse<any>> => {
    return axiosInstance.get('/users/profile/company/projects');
  };
  
  export const getCompanyCollaborations = async (): Promise<AxiosResponse<any>> => {
    return axiosInstance.get('/users/profile/company/collaborations');
  };

  
  
  
  export const ApplicationAPI = {
    getApplicationsForJob: (jobPostId: number) => axiosInstance.get(`/applications/job/${jobPostId}`),
    updateApplicationStatus: (id: number, status: string) =>
      axiosInstance.patch(`/applications/${id}/status`, null, { params: { status } }),
  };
