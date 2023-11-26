import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/shared/Home'
import SignUp from "../components/shared/SignUp"
import Login from "../components/shared/Login"
import CustomerHome from "../components/customer/CustomerHome"
import CustomerAccount from "../components/customer/CustomerAccount"
import OwnerAddShift from "../components/owner/OwnerAddShift";
import ownerManageRooms from "../components/owner/ownerManageRooms";
import CustomerMakeRequest from "../components/customer/CustomerMakeRequest";
import ReservationCustomer from "../components/customer/ReservationCustomer";

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
      path: '/owner',
      name: 'OwnerAddShift',
      component: OwnerAddShift
    },
    {
      path: '/owner/manage_rooms',
      name: 'ownerManageRooms',
      component: ownerManageRooms
    },
    {
      path: '/customer/:param1/reservation',
      name: 'ReservationCustomer',
      component: ReservationCustomer
    },
    {
      path: '/customer/:param1/reservation/:param2/make_request',
      name: 'CustomerMakeRequest',
      component: CustomerMakeRequest
    },
  ]
})
