import { Snackbar, Alert } from '@mui/material';

interface NotificationProps {
  open: boolean;
  message: string;
  onClose: () => void;
  severity?: "success" | "error" | "info" | "warning";
}

const Notification = ({ open, message, onClose, severity = "success" }: NotificationProps) => (
  <Snackbar
    open={open}
    autoHideDuration={3000}
    onClose={onClose}
    anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
    sx={{ mt: 7 }} // Add margin top to position below AppBar
  >
    <Alert
      onClose={onClose}
      severity={severity}
      elevation={6}
      variant="filled"
      sx={{ width: '100%' }}
    >
      {message}
    </Alert>
  </Snackbar>
);

export default Notification;