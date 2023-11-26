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
import EmployeeHome from "../components/employee/EmployeeHome"
import EmployeeAccount from "../components/employee/EmployeeAccount"
import EmployeeRepair from "../components/employee/EmployeeRepair.vue";
import OwnerHome from "../components/owner/OwnerHome"
import OwnerAccount from "../components/owner/OwnerAccount"
import OwnerManageEmployees from "../components/owner/OwnerManageEmployees"
import OwnerViewSchedule from "../components/owner/OwnerViewSchedule"
import EmployeeViewSchedule from "../components/employee/EmployeeViewSchedule";
import EmployeeReservation from "../components/employee/EmployeeReservation";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/SignUp/',
      component: SignUp
    },
    {
      path: '/Login/',
      component: Login
    },
    {
      path: '/CustomerHome/:param1',
      component: CustomerHome,
    },
    {
      path: '/CustomerAccount/:param1',
      component: CustomerAccount,
    },
    {
      path: '/',
      name: 'EmployeeHome',
      component: EmployeeHome,
      props: true
    },
    {
      path: '/',
      name: 'EmployeeAccount',
      component: EmployeeAccount,
      props: true
    },
    {
      path: '/',
      name: 'EmployeeRepair',
      component: EmployeeRepair,
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
      path: '/customer/reservation/:param1',
      name: 'ReservationCustomer',
      component: ReservationCustomer
    },
    {
      path: '/customer/reservation/make_request/:param1/:param2',
      name: 'CustomerMakeRequest',
      component: CustomerMakeRequest
    },
    {
      path: '/owner-view-schedule',
      name: 'OwnerViewSchedule',
      component: OwnerViewSchedule,
    },
    {
      path: '/employee-view-schedule',
      name: 'EmployeeViewSchedule',
      component: EmployeeViewSchedule,
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
    {
     path: '/owner-manage-employees',
     name: 'OwnerManageEmployees',
     component: OwnerManageEmployees,
     props: true
    },
    {
      path: '/employee-manage-reservations',
      name: 'EmployeeReservation',
      component: EmployeeReservation,
    },
  ]
})
