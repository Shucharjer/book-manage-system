import {createRouter,createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            path: '/',
            name: 'welcome',
            component: () => import('@/views/WelcomeView.vue'),
            children: [
                {
                    path: '',
                    name: 'welcome-login',
                    component: ()=>import('@/views/welcome/LoginPage.vue')
                }
            ]
        },
        {
            path: '/index',
            name: 'index',
            component: ()=> import('@/views/welcome/IndexView.vue'),
            children: [
                {
                    path: 'user',
                    name: 'user-setting',
                    component: ()=>import('@/views/user/UserSettings.vue')
                },
                {
                    path: 'my-book',
                    name: 'my-book',
                    component: ()=>import('@/views/user/MyBook.vue')
                },
                {
                    path: 'book',
                    name: 'book-setting',
                    component: ()=>import('@/views/book/BookSettings.vue')
                },
                {
                    path: 'management',
                    name: 'management',
                    component: ()=>import('@/views/management/Management.vue')
                }
            ]
        }
    ]
})

export default router