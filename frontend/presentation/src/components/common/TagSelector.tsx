import React, { useEffect, useState } from 'react';
import Select from 'react-select';
import axiosInstance from '../../api/axiosInstace';


interface Option {
  label: string;
  value: string;
}

interface TagSelectorProps {
  onTagsSelected: (tags: string[]) => void; // Callback to pass selected tags
}

const TagSelector: React.FC<TagSelectorProps> = ({ onTagsSelected }) => {
  const [options, setOptions] = useState<Option[]>([]);

  useEffect(() => {
    // Fetch tags from the backend
    axiosInstance.get('/tags/all').then((response) => {
      const tagOptions = response.data.map((tag: { name: string }) => ({
        label: tag.name,
        value: tag.name,
      }));
      setOptions(tagOptions);
    });
  }, []);

  return (
    <Select
      isMulti
      options={options}
      onChange={(selected) => onTagsSelected(selected.map((tag) => tag.value))}
      placeholder="Select tags..."
    />
  );
};

export default TagSelector;
