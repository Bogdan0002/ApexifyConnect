import React, { useEffect, useState } from 'react';
import axiosInstance from '../api/axiosInstace';


interface Application {
  id: number;
  contentCreatorName: string;
  status: string;
}

const ApplicationsPage: React.FC = () => {
  const [applications, setApplications] = useState<Application[]>([]);

  useEffect(() => {
    axiosInstance.get('/applications/job/1').then((response) => setApplications(response.data));
  }, []);

  const updateStatus = (id: number, status: string) => {
    axiosInstance.patch(`/applications/${id}/status`, null, { params: { status } }).then(() => {
      setApplications((prev) =>
        prev.map((app) => (app.id === id ? { ...app, status } : app))
      );
    });
  };

  return (
    <div>
      <h2>Applications</h2>
      {applications.map((app) => (
        <div key={app.id}>
          <p>Creator: {app.contentCreatorName}</p>
          <p>Status: {app.status}</p>
          <button onClick={() => updateStatus(app.id, 'ACCEPTED')}>Accept</button>
          <button onClick={() => updateStatus(app.id, 'REJECTED')}>Reject</button>
        </div>
      ))}
    </div>
  );
};

export default ApplicationsPage;
