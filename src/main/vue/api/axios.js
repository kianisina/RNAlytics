import axios from 'axios';

// Create a custom axios instance pointing to your Spring Boot backend
const api = axios.create({
  baseURL: 'http://localhost:8080/api/', 
  headers: {
    'Content-Type': 'application/json'
  }
});

// Interceptor: Before every request is sent, check if we have a token
api.interceptors.request.use(
  (config) => {
    // Grab the token from the browser's local storage
    const token = localStorage.getItem('jwt_token');
    
    if (token) {
      // Attach it to the Authorization header
      config.headers.Authorization = token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default api;