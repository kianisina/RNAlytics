<template>
  <q-layout view="lHh Lpr lFf">
    
    <q-header elevated class="bg-primary text-white" v-if="isLoggedIn">
      <q-toolbar>
        <q-toolbar-title>
          Bioinformatics Dashboard
        </q-toolbar-title>
        
        <q-btn flat icon="logout" label="Logout" @click="handleLogout" />
      </q-toolbar>
    </q-header>

    <q-page-container>
      
      <AuthPage v-if="!isLoggedIn" @login-success="isLoggedIn = true" />
      
      <AnalysisPage v-else />
      
    </q-page-container>
    
  </q-layout>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// Import BOTH of your pages
import AuthPage from './pages/AuthPage.vue'
import AnalysisPage from './pages/AnalysisPage.vue' // <-- Make sure this file exists!

// State variable to track if the user is allowed inside
const isLoggedIn = ref(false)

// Check if they are already logged in when they refresh the page
onMounted(() => {
  const token = localStorage.getItem('token'); // Or however you save your JWT!
  if (token) {
    isLoggedIn.value = true;
  }
})

// Logout logic
const handleLogout = () => {
  localStorage.removeItem('token'); // Delete the JWT token
  isLoggedIn.value = false;         // Kick them back to the login screen
}
</script>