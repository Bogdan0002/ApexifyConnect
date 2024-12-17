import React, { useState } from 'react';
import TagSelector from './TagSelector';
import axiosInstance from '../../api/axiosInstace';

const JobPostForm: React.FC = () => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [budget, setBudget] = useState('');
  const [contentType, setContentType] = useState('');
  const [deadline, setDeadline] = useState('');
  const [tags, setTags] = useState<string[]>([]);
  const [error, setError] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    // Check if the deadline is a future date
    const selectedDate = new Date(deadline);
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Remove time portion for comparison

    if (!deadline) {
      setError('Deadline is required.');
      return;
    }

    if (selectedDate < today) {
      setError('Deadline must be a future date.');
      return;
    }

    setError(''); // Clear any existing errors

    const jobPostData = {
      title,
      description,
      budget: parseFloat(budget),
      contentType,
      deadline,
      tagNames: tags,
    };

    axiosInstance.post('/job-posts/create', jobPostData).then((response) => {
      alert('Job post created successfully!');
      // Clear form
      setTitle('');
      setDescription('');
      setBudget('');
      setContentType('');
      setDeadline('');
      setTags([]);
    }).catch((error) => {
      console.error('Error creating job post:', error);
      setError('Failed to create job post. Please try again.');
    });
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>Title:</label>
      <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} required />

      <label>Description:</label>
      <textarea value={description} onChange={(e) => setDescription(e.target.value)} required />

      <label>Budget:</label>
      <input type="number" value={budget} onChange={(e) => setBudget(e.target.value)} required />

      <label>Content Type:</label>
      <input type="text" value={contentType} onChange={(e) => setContentType(e.target.value)} />

      <label>Deadline:</label>
      <input
        type="date"
        value={deadline}
        onChange={(e) => setDeadline(e.target.value)}
        min={new Date().toISOString().split('T')[0]} // Minimum is today's date
        required
      />

      <label>Tags:</label>
      <TagSelector onTagsSelected={setTags} />

      {error && <p style={{ color: 'red' }}>{error}</p>}

      <button type="submit">Create Job Post</button>
    </form>
  );
};

export default JobPostForm;
