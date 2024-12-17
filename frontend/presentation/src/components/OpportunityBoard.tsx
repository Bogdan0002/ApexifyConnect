import React from "react";
import JobPostList from "../components/common/JobPostList";
import { Container, Typography, Box } from "@mui/material";

const OpportunityBoard = () => {
  return (
    <Container maxWidth="lg">
      <Box mt={4}>
        <Typography variant="h4" component="h1" gutterBottom>
          Opportunity Board
        </Typography>
        <JobPostList />
      </Box>
    </Container>
  );
};

export default OpportunityBoard;
