<template>
  <q-page class="flex flex-center bg-grey-2">
    
    <q-card style="width: 400px" class="shadow-4">
      <q-tabs
        v-model="tab"
        dense
        class="text-grey"
        active-color="primary"
        indicator-color="primary"
        align="justify"
        narrow-indicator
      >
        <q-tab name="login" label="Login" />
        <q-tab name="register" label="Register" />
      </q-tabs>

      <q-separator />

      <q-tab-panels v-model="tab" animated>
        
        <q-tab-panel name="login" class="q-pa-md">
          <q-form @submit.prevent="onLogin">
            <q-input v-model="loginForm.username" label="Username" outlined class="q-mb-md" />
            <q-input v-model="loginForm.password" type="password" label="Password" outlined class="q-mb-md" />
            
            <q-btn type="submit" color="primary" label="Login" class="full-width" :loading="loading" />
          </q-form>
        </q-tab-panel>

        <q-tab-panel name="register" class="q-pa-md">
          <q-form @submit.prevent="onRegister">
            <q-input v-model="registerForm.username" label="Username" outlined class="q-mb-md" />
            <q-input v-model="registerForm.email" type="email" label="Email" outlined class="q-mb-md" />
            <q-input v-model="registerForm.password" type="password" label="Password" outlined class="q-mb-md" />
            <q-input v-model="registerForm.name" label="Full Name" outlined class="q-mb-md" />
            <q-input v-model.number="registerForm.age" type="number" label="Age" outlined class="q-mb-md" />
            
            <q-btn type="submit" color="secondary" label="Register Account" class="full-width" :loading="loading" />
          </q-form>
        </q-tab-panel>

      </q-tab-panels>
    </q-card>
  </q-page>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../stores/auth' 
import { useQuasar } from 'quasar'

const authStore = useAuthStore()
const $q = useQuasar()

const tab = ref('login')
const loading = ref(false)

const loginForm = ref({ username: '', password: '' })
const registerForm = ref({ username: '', email: '', password: '', name: '', age: null })

// Define the signal we will send to App.vue
const emit = defineEmits(['login-success'])

const onLogin = async () => {
  loading.value = true
  try {
    await authStore.login(loginForm.value.username, loginForm.value.password)
    
    // Removed the crashing localStorage line!
    
    $q.notify({ type: 'positive', message: 'Logged in successfully!', position: 'top' })
    
    // Send the signal to App.vue to flip the page!
    emit('login-success')
    
  } catch (error) {
    $q.notify({ type: 'negative', message: 'Login failed. Check your username/password.', position: 'top' })
  } finally {
    loading.value = false
  }
}

const onRegister = async () => {
  loading.value = true
  try {
    await authStore.register(registerForm.value)
    $q.notify({ type: 'positive', message: 'Registration successful! You can now log in.', position: 'top' })
    tab.value = 'login' 
  } catch (error) {
    $q.notify({ type: 'negative', message: 'Registration failed. Username or email might be taken.', position: 'top' })
  } finally {
    loading.value = false
  }
}

// You can also delete the onLogout function from here, 
// since App.vue handles logging out now!
</script>