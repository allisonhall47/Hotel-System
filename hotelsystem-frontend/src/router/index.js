import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/shared/Home'
import SignUp from "../components/shared/SignUp"
import Login from "../components/shared/Login"
import CustomerHome from "../components/customer/CustomerHome"
import CustomerAccount from "../components/customer/CustomerAccount"
import EmployeeHome from "../components/employee/EmployeeHome"
import EmployeeAccount from "../components/employee/EmployeeAccount"
import EmployeeRepair from "../components/employee/EmployeeRepair.vue";
import OwnerHome from "../components/owner/OwnerHome"
import OwnerAccount from "../components/owner/OwnerAccount"
import OwnerManageEmployees from "../components/owner/OwnerManageEmployees"
import OwnerViewSchedule from "../components/owner/OwnerViewSchedule"
import OwnerRepair  from "../components/owner/OwnerRepair.vue";
import EmployeeViewSchedule from "../components/employee/EmployeeViewSchedule";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/Home/',
      component: Home
    },
    {
      path: '/',
      name: 'SignUp',
      component: SignUp
    },
    {
      path: '/Login/',
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
      path: '/EmployeeHome/:param1/:param2',
      component: EmployeeHome,
    },
    {
      path: '/EmployeeAccount/:param1/:param2',
      component: EmployeeAccount,
    },
    {
      path: '/EmployeeRepair/:param1/:param2',
      component: EmployeeRepair,
    },
    {
      path: '/',
      name: 'OwnerViewSchedule',
      component: OwnerViewSchedule,
      props: true
    },
    {
      path: '/',
      name: 'EmployeeViewSchedule',
      component: EmployeeViewSchedule,
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
    {
     path: '/owner-manage-employees',
     name: 'OwnerManageEmployees',
     component: OwnerManageEmployees,
     props: true
    },
    {
      path: '/OwnerRepair/:param1',
      component: OwnerRepair,
    },
  ]
})
