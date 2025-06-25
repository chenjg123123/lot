import { createRouter, createWebHistory } from 'vue-router'
import DefaultLayout from '@/layouts/DefaultLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/Login.vue'),
      meta: {
        title: '登录',
        requiresAuth: false,
      },
    },
    {
      path: '/',
      redirect: '/home',
      component: DefaultLayout,
      children: [
        {
          path: 'home',
          name: 'home',
          component: () => import('@/views/Home.vue'),
          meta: {
            title: '首页',
            requiresAuth: true,
          },
        },
        {
          path: 'devices',
          name: 'devices',
          component: () => import('@/views/Devices.vue'),
          meta: {
            title: '设备管理',
            requiresAuth: true,
          },
        },
        {
          path: 'tickets',
          name: 'tickets',
          component: () => import('@/views/Tickets.vue'),
          meta: {
            title: '工单管理',
            requiresAuth: true,
          },
        },
        {
          path: 'alerts',
          name: 'alerts',
          component: () => import('@/views/Alerts.vue'),
          meta: {
            title: '告警中心',
            requiresAuth: true,
          },
        },
      ],
    },
  ],
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = `${to.meta.title} - Smart IoT Ticketing System`

  const token = localStorage.getItem('token')

  // 如果访问登录页面且已登录，重定向到首页
  if (to.path === '/login' && token) {
    next({ name: 'home' })
    return
  }

  // 检查是否需要登录权限
  if (to.meta.requiresAuth && !token) {
    next({ name: 'login' })
    return
  }

  next()
})

export default router
