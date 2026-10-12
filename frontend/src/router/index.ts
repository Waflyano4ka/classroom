import { createRouter, createWebHistory } from 'vue-router'
import { getAccessToken } from '@/types/accessToken'
import { initializeAuth } from '@/services/authInitialization'

import PublicLayout from '@/layouts/PublicLayout.vue'
import HomeLayout from '@/layouts/HomeLayout.vue'

import WelcomePage from '@/pages/WelcomePage.vue'
import LoginPage from '@/pages/LoginPage.vue'
import RegistrationPage from '@/pages/RegistrationPage.vue'
import HomePage from '@/pages/HomePage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: PublicLayout,
      meta: {
        guestOnly: true,
      },
      children: [
        {
          path: '',
          name: 'welcome',
          component: WelcomePage,
        },
        {
          path: 'login',
          name: 'login',
          component: LoginPage,
        },
        {
          path: 'register',
          name: 'register',
          component: RegistrationPage,
        },
      ],
    },
    {
      path: '/home',
      component: HomeLayout,
      meta: {
        requiresAuth: true,
      },
      children: [
        {
          path: '',
          name: 'home',
          component: HomePage,
        },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: () => {
        return getAccessToken()
          ? { name: 'home' }
          : { name: 'welcome' }
      },
    },
  ],
})

let authInitialized = false
let authInitializationPromise: Promise<void> | null = null

router.beforeEach(async (to) => {
  if (!authInitialized) {
    authInitializationPromise ??= initializeAuth()

    await authInitializationPromise
    authInitialized = true
  }

  const isAuthenticated = !!getAccessToken()

  if (to.meta.requiresAuth && !isAuthenticated) {
    return { name: 'login' }
  }

  if (to.meta.guestOnly && isAuthenticated) {
    return { name: 'home' }
  }
})

export default router
