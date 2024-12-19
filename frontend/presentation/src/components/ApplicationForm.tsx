import { useState } from "react";
import axiosInstance from "../api/axiosInstace";

const ApplicationForm: React.FC<{ jobPostId: number }> = ({ jobPostId }) => {
    const [coverLetter, setCoverLetter] = useState('');
    const [error, setError] = useState('');
    
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        
        const applicationData = {
            jobPostId,
            coverLetter
        };
        
        axiosInstance.post('/applications/apply', applicationData)
            .then(() => {
                alert('Application submitted successfully!');
                setCoverLetter('');
            })
            .catch((error) => {
                setError('Failed to submit application. Please try again.');
                console.error('Error:', error);
            });
    };
    
    return (
        <form onSubmit={handleSubmit}>
            <h3>Apply for this position</h3>
            <textarea
                value={coverLetter}
                onChange={(e) => setCoverLetter(e.target.value)}
                placeholder="Write your cover letter..."
                required
            />
            {error && <p className="error">{error}</p>}
            <button type="submit">Submit Application</button>
        </form>
    );
};
