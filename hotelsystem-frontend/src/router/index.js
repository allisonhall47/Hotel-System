import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/shared/Home'
import SignUp from "../components/shared/SignUp"
import Login from "../components/shared/Login"
import CustomerHome from "../components/customer/CustomerHome"
import CustomerAccount from "../components/customer/CustomerAccount"
import OwnerHome from "../components/owner/OwnerHome"
import OwnerAccount from "../components/owner/OwnerAccount"

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
    {
      path: '/',
      name: 'CustomerHome',
      component: CustomerHome,
      props: true
    },
    {
      path: '/',
      name: 'CustomerAccount',
      component: CustomerAccount,
      props: true
    },
    {
     path: '/owner-home',
     name: 'OwnerHome',
     component: OwnerHome,
     props: true
    },
    {
     path: '/owner-account',
     name: 'OwnerAccount',
     component: OwnerAccount,
     props: true
    },
  ]
})
