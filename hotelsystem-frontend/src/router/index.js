import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/shared/Home'
import SignUp from "../components/shared/SignUp"
import Login from "../components/shared/Login"
import CustomerHome from "../components/customer/CustomerHome"
import CustomerAccount from "../components/customer/CustomerAccount"
import ReservationGuest from "../components/shared/ReservationGuest"
import CreateAccountSuggestion from "../components/shared/CreateAccountSuggestion"
import SuggestRooms from "../components/shared/SuggestRooms"
import CreateCustomerPage from "../components/shared/createCustomerPage"

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
      path: '/ReservationGuest',
      name: 'ReservationGuest',
      component: ReservationGuest
    },
    {
      path: '/CreateAccountSuggestion',
      name: 'CreateAccountSuggestion',
      component: CreateAccountSuggestion
    },
    {
      path: '/SuggestRooms/:param1/:param2/:param3/:param4',
      name: 'SuggestRooms',
      component: SuggestRooms
    },
    {
      path: '/CreateCustomerPage/:param1/:param2/:param3',
      name: 'CreateCustomerPage',
      component: CreateCustomerPage
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
  ]
})
