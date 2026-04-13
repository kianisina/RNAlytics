import { defineStore } from 'pinia';
import api from '../api/axios'; // Import the axios instance we just made

export const useAuthStore = defineStore('auth', {
  state: () => ({
    // Initialize the token from local storage if it exists
    token: localStorage.getItem('jwt_token') || null,
  }),
  
  getters: {
    // A quick way to check if someone is logged in
    isAuthenticated: (state) => !!state.token,
  },
  
  actions: {
    // Register a new user
    async register(userData) {
      try {
        // userData will be { username, email, password }
        const response = await api.post('/users', userData);
        return response.data;
      } catch (error) {
        console.error('Registration failed:', error);
        throw error;
      }
    },

    // Log in an existing user
    async login(username, password) {
      try {
        const response = await api.post('/login', { username, password });
        
        // Our Spring Boot filter sends back {"token": "Bearer eyJ..."}
        const token = response.data.token;
        
        // Save it to Pinia state
        this.token = token;
        
        // Save it to the browser so it survives a page refresh
        localStorage.setItem('jwt_token', token);
        
        return true;
      } catch (error) {
        console.error('Login failed:', error);
        throw error;
      }
    },

    // Log the user out
    logout() {
      this.token = null;
      localStorage.removeItem('jwt_token');
    }
  }
});