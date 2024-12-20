import axiosInstance from './axiosInstace';
import { AxiosResponse } from 'axios';

interface ApplicationRequest {
  jobPostId: number;
  coverLetter: string;
}

interface ApplicationResponse {
  id: number;
  jobPostId: number;
  jobTitle: string;
  contentCreatorName: string;
  coverLetter: string;
  status: string;
  appliedAt: string;
  updatedAt: string;
}

export const submitApplication = async (data: ApplicationRequest): Promise<AxiosResponse<ApplicationResponse>> => {
  return axiosInstance.post('/applications/apply', data);
};

export const getApplicationsForJob = async (jobId: number): Promise<AxiosResponse<ApplicationResponse[]>> => {
  return axiosInstance.get(`/applications/job/${jobId}`);
};
