import { createApp } from 'vue'
import { createPinia } from 'pinia'

// 1. Import Quasar, Notify, AND grab all the UI components
import { Quasar, Notify } from 'quasar'
import * as components from 'quasar'

// Import Quasar icons and CSS
import '@quasar/extras/material-icons/material-icons.css'
import 'quasar/dist/quasar.css'

import App from './App.vue'

const app = createApp(App)

// Initialize Pinia so our Auth Store works
app.use(createPinia())

// 2. Initialize Quasar and register ALL the components
app.use(Quasar, {
  components, // <--- THIS is the magic line that fixes your error!
  plugins: {
    Notify // Required for the popups
  }
})

// Mount the app to the HTML
app.mount('#app')