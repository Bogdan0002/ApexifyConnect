import axios, { AxiosResponse } from "axios";

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
      ? "http://localhost:8080/api/auth/login/content-creator" 
      : "http://localhost:8080/api/auth/login/company";
    return axios.post(endpoint, { email, password });
  };
