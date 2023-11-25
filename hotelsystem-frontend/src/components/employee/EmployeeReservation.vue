<template>
  <div>
    <div class="background">

      <!--TODO: navbar-->

      <div class="table-container">
        <div class="buttons-container">
          <div class="dropdown">
            <button class="prettybutton">{{ reservationsFilterName }}</button>
            <div class="dropdown-content">
              <button class="dropdown-button" @click="SelectNoFilter">None</button>
              <button class="dropdown-button" @click="SelectPaymentFilter">Unpaid</button>
              <button class="dropdown-button" @click="SelectCustomerFilter">Customer</button>
            </div>
          </div>
          <input v-model="filteredCustomerEmail" v-if="reservationsFilter==2" type="text" id="scheduleEmployeeFilter" name="scheduleEmployeeFilter" class="styledinput"
                 @keyup.enter="getReservations" placeholder="Customer Email">
        </div>

        <table>
          <tr>
            <th>Reservation ID</th>
            <th>Customer</th>
            <th>Check in</th>
            <th>Checkout</th>
            <th>People</th>
            <th>Total Price</th>
            <th>Paid</th>
            <th>Status</th>
          </tr>

          <tr v-for="res in reservations" :key="res.reservationId">
            <td>{{res.reservationId}}</td>
            <td>
              <div class="column-container">
                <label>{{ res.customer.name }}</label>
                <label class="prettylabel">{{ res.customer.email }}</label>
              </div>
            </td>
            <td>{{ res.checkin }}</td>
            <td>{{ res.checkOut }}</td>
            <td>{{ res.numPeople }}</td>
            <td> {{res.totalPrice}}$</td>
            <td v-if="res.paid">Paid</td>
            <td v-if="!res.paid">
              <button @click="openConfirmationPopup(0, res.reservationId)" class="prettybutton">Pay</button>
            </td>
            <td>
              <button v-if="res.checkedIn == 'BeforeCheckIn'" @click="openConfirmationPopup(1, res.reservationId)" class="prettybutton">Check In</button>
              <button v-if="res.checkedIn == 'CheckedIn'" @click="openConfirmationPopup(2, res.reservationId)" class="prettybutton">Check Out</button>
              <label v-if="res.checkedIn == 'CheckedOut'">Checked Out</label>
            </td>
          </tr>
        </table>
        <div class="centerbuttoncontainer">
          <label class="prettylabel" v-if="reservations.length == 0">{{errorReservation}}</label>
        </div>
      </div>
    </div>

    <div class="popup-container">
      <!-- Popup overlay -->
      <div class="overlay" v-if="showConfirmationPopup">
        <!-- Popup content -->
        <div class="popup">
          <h2 v-if="confirmationAction == 0" class="prettyheader">Confirm Payment</h2>
          <h2 v-if="confirmationAction == 1" class="prettyheader">Check In</h2>
          <h2 v-if="confirmationAction == 2" class="prettyheader">Check Out</h2>
          <div class="centerbuttoncontainer">
            <label class="prettylabel">Reservation ID: {{currReservationId}}</label>
          </div>
          <div class="centerbuttoncontainer">
            <button @click="confirmAction" class="prettybutton">Confirm</button>
            <button @click="closeConfirmationPopup" class="prettybutton2">Cancel</button>

          </div>
          <div class="centerbuttoncontainer">
            <label>{{ popupErrorShift }}</label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import axios from 'axios'

var config = require('../../../config')
var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
var axiosClient = axios.create({
  baseURL: backendUrl,
  headers: {'Access-Control-Allow-Origin': frontendUrl}
})

function Reservation(reservationId, numPeople, checkin, checkOut, totalPrice, paid, checkedIn, customer){
  this.reservationId = reservationId;
  this.numPeople = numPeople;
  this.checkin = checkin;
  this.checkOut = checkOut;
  this.totalPrice = totalPrice;
  this.paid = paid;
  this.checkedIn = checkedIn;
  this.customer = customer;
}

export default {
  name: 'EmployeeReservation',
  data() {
    return {
      reservations: [],
      currReservationId: 0,
      reservationsFilter: 0,
      reservationsFilterName: 'No Filter',
      showConfirmationPopup: false,
      filteredCustomerEmail: null,
      confirmationAction: false,
      errorReservation: '',
      popupError: '',
    }
  },
  created: function () {
    this.getReservations()
  },
  methods: {
    getReservations() {
      var url = '/reservation'

      switch (this.reservationsFilter) {
        case 1: url = '/reservation/not-paid'
          break
        case 2: url = '/reservation/customer/'+this.filteredCustomerEmail
          break
      }
      axios.request({
        method: 'get',
        maxBodyLength: Infinity,
        url: backendUrl+url,
        headers: { },
        data : ''
      })
        .then((response) => {
          this.reservations = response.data
          if (this.reservations.length == 0) {
            this.errorReservation = 'No reservations found!'
          }
        })
        .catch((error) => {
          console.log(error)
          this.reservations = []
          if (error.response && error.response.status == 404) {
            this.errorReservation = "Customer does not exist!"
          } else {
            this.errorReservation = "Error loading reservations!"
          }
        });
    },
    openConfirmationPopup: function (action, id) {
      this.popupError = '';
      this.currReservationId = id
      this.confirmationAction = action;
      this.showConfirmationPopup = true;
    },
    closeConfirmationPopup: function () {
      this.showConfirmationPopup = false;
    },
    confirmAction() {
      var url = '/reservation/' + this.currReservationId;
      switch (this.confirmationAction) {
        case 1: // Check In
          url += '/checkIn';
        case 2: // Check Out
          //TODO
      }

      axios.request({
        method: 'put',
        maxBodyLength: Infinity,
        url: backendUrl+url,
        headers: { },
        params : {
          "money": 100000000 // Always enough money. In practice, amount should come from payment service
        }
      })
        .then((response) => {
          this.getReservations()
          this.closeConfirmationPopup()
        })
        .catch((error) => this.popupError = 'Reservation does not exist anymore!');
    },
    SelectNoFilter() {
      this.reservationsFilterName = 'No Filter'
      this.reservationsFilter = 0
      this.getReservations()
    },
    SelectPaymentFilter() {
      this.reservationsFilterName = 'Unpaid'
      this.reservationsFilter = 1
      this.getReservations()
    },
    SelectCustomerFilter() {
      this.reservationsFilterName = 'Customer'
      this.reservationsFilter = 2
    },
    async LogOut() {
      await this.$router.push({name: 'Home'})
    }
  },
}

</script>

<style scoped>
.background {
  width: 100%;
  height: 100%;
  position: absolute;
  background: url('../../assets/pattern.png') center center no-repeat;
  background-size: cover;
}

body {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh; /* 100% of the viewport height */
  margin: 0; /* Remove default margin */
}

.table-container {
  background-color: rgba(255, 255, 255, 0.5);
  padding: 2%;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: absolute;
  top: 25%;
  left: 10%;
  right: 10%;
  min-height: 300px;
}

.buttons-container {
  margin-bottom: 1%;
}

table {
  border-collapse: collapse;
  margin: 0 auto;
  width: 100%; /* Adjust the width as needed */
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f2f2f2;
}

.styledinput {
  border-radius: 5px;
  background-color: white;
  border: 1px solid #888888;
  color: #888888;
}

.prettybutton {
  border-radius: 5px;
  background-color: white;
  border: 1px solid #721c24;
  color: #721c24;
}

.prettybutton2 {
  border-radius: 5px;
  background-color: white;
  border: 1px solid #888888;
  color: #888888;
}

.prettybutton:hover {
  border-radius: 5px;
  border: #721c24;
  background-color: #721c24;
  border: 1px solid #721c24;
  color: white;
}

.prettybutton2:hover {
  border-radius: 5px;
  border: #888888;
  background-color: #888888;
  border: 1px solid #888888;
  color: white;
}

.centerbuttoncontainer {
  margin-top: 5%;
  text-align: center;
}



.column-container {
  display:flex;
  flex-direction: column;
}

.prettylabel {
  color: #4e555b;
}

.prettyheader {
  color: #721c24;
  margin-bottom: 10%;
}

/* Dropdown styles */
.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f9f9f9;
  min-width: 160px;
  box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
  z-index: 1;
  border-radius: 5px;
}

.dropdown-content a {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}

.dropdown-content a:hover {
  background-color: #888888;
  color: white;
}

.dropdown:hover .dropdown-content {
  display: block;
}

/* Button specific styles */
.dropdown-button {
  background-color: white;
  border: none;
  width: 100%;
  text-decoration: none;
  display: inline-block;
  text-align: left;
  cursor: pointer;
  color: #888888;
}

.dropdown-button:hover {
  background-color: #888888;
  color: white;
}

/* Styles for overlay */
.overlay {
  display: flex;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* Semi-transparent black overlay */
  justify-content: center;
  align-items: center;
  z-index: 2;
}

.popup-container {
  display: block;
  position: relative;
  z-index: 3; /* Ensure the popup container is on top of other content */
}

/* Styles for the popup */
.popup {
  width: 30%;
  height: 25%;
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
}

</style>
