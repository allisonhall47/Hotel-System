import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/shared/Home'
import SignUp from "../components/shared/SignUp"
import Login from "../components/shared/Login";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/',
      name: 'SignUp',
      component: SignUp
    },
    {
      path: '/',
      name: 'Login',
      component: Login
    },
  ]
})
