import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

import { createVuetify } from 'vuetify'

export default createVuetify({
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          background: '#fffbe6',
          accentbackground: '#d6ba88',
          darkprimary: '#29463e',
          primary: '#356859',
          logout: '#ffa0a0'
        },
      },
    },
  },
})
