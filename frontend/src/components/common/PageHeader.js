import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

const PageHeader = ({ title, buttonText, buttonLink, buttonIcon }) => {
  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        mb: 3,
      }}
    >
      <Typography variant="h4" component="h1" gutterBottom>
        {title}
      </Typography>
      {buttonText && buttonLink && (
        <Button
          variant="contained"
          color="primary"
          component={RouterLink}
          to={buttonLink}
          startIcon={buttonIcon}
        >
          {buttonText}
        </Button>
      )}
    </Box>
  );
};

export default PageHeader;
