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
          accentbackground: '#feebc4',
          darkbackground: '#3a382f',
          darkfiltersbackground: '#66645c',
          darkprimary: '#29463e',
          primary: '#356859',
          logout: '#ffa0a0'
        },
      },
    },
  },
})
