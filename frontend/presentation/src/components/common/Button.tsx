import React from 'react';
import { Button as MuiButton, ButtonProps as MuiButtonProps } from '@mui/material';

type CustomButtonProps = MuiButtonProps & {
  children: React.ReactNode;
};

const Button: React.FC<CustomButtonProps> = ({ children, ...props }) => {
  return (
    <MuiButton {...props} variant="contained" color="primary">
      {children}
    </MuiButton>
  );
};

export default Button;