<template>
  <div>
    <div class="background">
      <div class="navbar-container">
        <nav class="navbar navbar-expand-lg navbar-light transparent-background">
          <a class="navbar-brand" href="#">
            <img src="../../assets/marwaniottNoBG.png" alt="Your Logo" height="60">
          </a>
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
            <ul class="navbar-nav">
              <li class="nav-item">
                <a class="nav-link" @click="LogOut">LogOut</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>
      <div class="table-container">
        <div class="buttons-container">
          <button @click="openCreateShiftPopup" class = "prettybutton">Create Shift</button>

          <div class="dropdown">
            <button class="prettybutton">Filter</button>
            <div class="dropdown-content">
              <button class="dropdown-button" @click="SelectNoFilter">None</button>
              <button class="dropdown-button" @click="SelectEmployeeFilter">Employee</button>
              <button class="dropdown-button" @click="SelectDateFilter">Date</button>
            </div>
          </div>

            <input v-if="scheduleFilter==1" type="date" id="scheduleDateFilter" name="scheduleDateFilter" class="styledinput">
            <input v-model="filteredEmployeeName" v-if="scheduleFilter==2" type="text" id="scheduleEmployeeFilter" name="scheduleEmployeeFilter" class="styledinput" @keyup.enter="filterByEmployee(filteredEmployeeName)" placeholder="Employee Name">
        </div>

        <table>
          <tr>
            <th>Employee</th>
            <th>Date</th>
            <th>Start time</th>
            <th>End time</th>
          </tr>

          <tr v-for="shift in shifts" :key="shift.id">
            <td>{{ shift.employeeName }}</td>
            <td>{{ shift.date }}</td>
            <td>{{ shift.startTime }}</td>
            <td>{{ shift.endTime }}</td>
          </tr>
        </table>
      </div>
    </div>

    <div class="popup-container">
      <!-- Popup overlay -->
      <div class="overlay" v-if="showCreateShiftPopup" @click="closeCreateShiftPopup">
        <!-- Popup content -->
        <div class="popup" @click.stop>
          <h2 class="prettyheader">Create New Shift</h2>
          <div class="inputrow">
            <label class="prettylabel">Employee</label>
            <input type="text" id="newShiftEmployee" name="New Shift Employee" class="styledinput" placeholder="Employee Name">
          </div>
          <div class="inputrow">
            <label class="prettylabel">Date</label>
            <input type="date" id="newShiftDate" name="New Shift Date" class="styledinput">
          </div>
          <div class="inputrow">
            <label class="prettylabel">Start Time</label>
            <input type="time" id="newShiftStartTime" name="New Shift Start Time" class="styledinput">
          </div>
          <div class="inputrow">
            <label class="prettylabel">End Time</label>
            <input type="time" id="newshiftEndTime" name="New Shift End Time" class="styledinput">
          </div>
          <div class="centerbuttoncontainer">
            <button class="centerbutton prettybutton">Create</button>
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
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

function Shift(id, date, startTime, endTime, employeeName) {
  this.id = id
  this.date = date
  this.startTime = startTime
  this.endTime = endTime
  this.employeeName = employeeName
}

export default {
  name: 'OwnerViewSchedule',
  data() {
    return {
      shifts: [],
      scheduleFilter: 0,
      showCreateShiftPopup: false,
      filteredEmployeeName: null,
      filteredDate: null,
      newShift: '',
      errorShift: '',
      response: []
    }
  },
  created: function() {
    //Test data
    const shift1 = new Shift(1, Date.now(), "12:00", "15:00", "Tom")
    const shift2 = new Shift(2, Date.now(), "17:00", "22:00", "Tim")
    const shift3 = new Shift(1, Date.now(), "12:00", "15:00", "Tom")
    const shift4 = new Shift(2, Date.now(), "17:00", "22:00", "Tim")
    const shift5 = new Shift(1, Date.now(), "12:00", "15:00", "Tom")
    const shift6 = new Shift(2, Date.now(), "17:00", "22:00", "Tim")
    const shift7 = new Shift(1, Date.now(), "12:00", "15:00", "Tom")
    const shift8 = new Shift(2, Date.now(), "17:00", "22:00", "Tim")

    this.shifts = [shift1, shift2, shift3, shift4, shift5, shift6, shift7, shift8, shift1, shift2, shift3, shift4, shift5, shift6, shift7, shift8]
  },
  methods: {
    openCreateShiftPopup: function() {
      this.showCreateShiftPopup = true;
    },
    closeCreateShiftPopup: function() {
      this.showCreateShiftPopup = false;
    },
    createShift: function () {
      //TODO ...
    },
    SelectNoFilter() {
      this.scheduleFilter = 0
    },
    SelectEmployeeFilter() {
      this.scheduleFilter = 2
    },
    SelectDateFilter() {
      this.scheduleFilter = 1
    },
    filterByEmployee(filteredEmployeeName) {
      //TODO: make http request
    },
    filterByDate(filteredDate) {
      //TODO: make http request
    },
    async LogOut() {
      //TODO: logout
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

.centerbutton {
  margin: 0 auto;
}

.centerbuttoncontainer {
  margin-top: 5%;
  text-align: center;
}

.prettybutton:hover {
  border-radius: 5px;
  border: #888888;
  background-color: #888888;
  border: 1px solid #888888;
  color: white;
}

.inputrow {
  margin-bottom: 1%;
  width: 100%;
}

.prettylabel {
  width: 25%;
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
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
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
  height: 45%;
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
}

</style>
