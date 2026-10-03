import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

import PublicLayout from '@/layouts/PublicLayout.vue'
import HomeLayout from "@/layouts/HomeLayout.vue";

import WelcomePage from '@/pages/WelcomePage.vue'
import LoginPage from '@/pages/LoginPage.vue'
import RegistrationPage from '@/pages/RegistrationPage.vue'
import HomePage from "@/pages/HomePage.vue";

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
        const authStore = useAuthStore()

        return authStore.token
          ? { name: 'home' }
          : { name: 'welcome' }
      },
    },
  ],
})

router.beforeEach((to) => {
  const authStore = useAuthStore()
  const isAuthenticated = !!authStore.token

  if (to.meta.requiresAuth && !isAuthenticated) {
    return { name: 'login' }
  }

  if (to.meta.guestOnly && isAuthenticated) {
    return { name: 'home' }
  }
})

export default router
