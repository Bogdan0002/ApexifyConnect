import React from 'react';
import { TextField } from '@mui/material';

interface InputProps {
  type: string;
  id: string;
  name: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  required?: boolean;
}

const Input: React.FC<InputProps> = ({ type, id, name, value, onChange, required }) => {
  return (
    <TextField
      type={type}
      id={id}
      name={name}
      value={value}
      onChange={onChange}
      required={required}
      fullWidth
      variant="outlined"
      margin="normal"
    />
  );
};

export default Input;