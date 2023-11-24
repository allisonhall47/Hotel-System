<template>
  <div>
    <div class="background">
      <div class="navbar-container">
        <nav class="navbar navbar-expand-lg navbar-light transparent-background">
          <a class="navbar-brand" href="#">
            <img src="../../assets/marwaniottNoBG.png" alt="Your Logo" height="60">
          </a>
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                  aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
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
          <button @click="openCreateShiftPopup" class="prettybutton">Create Shift</button>

          <div class="dropdown">
            <button class="prettybutton">Filter</button>
            <div class="dropdown-content">
              <button class="dropdown-button" @click="SelectNoFilter">None</button>
              <button class="dropdown-button" @click="SelectEmployeeFilter">Employee</button>
              <button class="dropdown-button" @click="SelectDateFilter">Date</button>
            </div>
          </div>

          <input v-model="filteredDate" v-if="scheduleFilter==1" type="date" id="scheduleDateFilter" name="scheduleDateFilter"
                 class="styledinput" @input="getShifts">
          <input v-model="filteredEmployeeEmail" v-if="scheduleFilter==2" type="text" id="scheduleEmployeeFilter" name="scheduleEmployeeFilter" class="styledinput"
                 @keyup.enter="getShifts" placeholder="Employee Email">
        </div>

        <table>
          <tr>
            <th>Employee</th>
            <th>Date</th>
            <th>Start time</th>
            <th>End time</th>
            <th>Update</th>
            <th>Delete</th>
          </tr>

          <tr v-for="shift in shifts" :key="shift.id">
            <td>{{shift.employeeEmail}}
            </td>
            <td>{{ shift.date }}</td>
            <td>{{ shift.startTime }}</td>
            <td>{{ shift.endTime }}</td>
            <td>
              <button @click="openUpdateShiftPopup(shift.shiftId)" class="prettybutton2">Update</button>
            </td>
            <td>
              <button @click="deleteShift(shift.shiftId)" class="prettybutton">Delete</button>
            </td>
          </tr>
        </table>
        <div class="centerbuttoncontainer">
          <label class="prettylabel" v-if="shifts.length == 0">{{errorShift}}</label>
        </div>
      </div>
    </div>

    <div class="popup-container">
      <!-- Popup overlay -->
      <div class="overlay" v-if="showCreateShiftPopup">
        <!-- Popup content -->
        <div class="popup">
          <h2 v-if="!isUpdatingShift" class="prettyheader">Create New Shift</h2>
          <h2 v-if="isUpdatingShift" class="prettyheader">Update Shift</h2>
          <div class="inputrow">
            <label class="prettylabel">Employee</label>
            <input v-model="currShift.employeeEmail" type="text" id="newShiftEmployee" name="New Shift Employee" class="styledinput" placeholder="Employee Email">
          </div>
          <div class="inputrow">
            <label class="prettylabel">Date</label>
            <input v-model="currShift.date" type="date" id="newShiftDate" name="New Shift Date" class="styledinput">
          </div>
          <div class="inputrow">
            <label class="prettylabel">Start Time</label>
            <input v-model="currShift.startTime" type="time" id="newShiftStartTime" name="New Shift Start Time"
                   class="styledinput">
          </div>
          <div class="inputrow">
            <label class="prettylabel">End Time</label>
            <input v-model="currShift.endTime" type="time" id="newshiftEndTime" name="New Shift End Time"
                   class="styledinput">
          </div>
          <div class="centerbuttoncontainer">
            <button v-if="!isUpdatingShift" @click="createShift" class="prettybutton">Create</button>
            <button v-if="isUpdatingShift" @click="createShift" class="prettybutton">Update</button>
            <button @click="closeCreateShiftPopup" class="prettybutton2">Close</button>
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

function Shift(shiftId, date, startTime, endTime, employeeEmail) {
  this.shiftId = shiftId
  this.date = date
  this.startTime = startTime
  this.endTime = endTime
  this.employeeEmail = employeeEmail

}

export default {
  name: 'OwnerViewSchedule',
  data() {
    return {
      shifts: [],
      scheduleFilter: 0,
      showCreateShiftPopup: false,
      filteredEmployeeEmail: null,
      filteredDate: null,
      isUpdatingShift: false,
      currShift: null,
      currShiftId: 0,
      errorShift: '',
      popupErrorShift: '',
    }
  },
  created: function () {
    this.getShifts()
  },
  methods: {
    getShifts() {
      var url = '/shifts/'

      switch (this.scheduleFilter) {
        case 1: url = '/shifts/date/get/'+this.filteredDate
          break
        case 2: url = '/shifts/get/'+this.filteredEmployeeEmail
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
          const shifts = response.data
          shifts.forEach(shift => {
            shift.startTime = shift.startTime.substr(0, 5)
            shift.endTime = shift.endTime.substr(0, 5)
          })
          this.shifts = shifts
          this.errorShift = ''
        })
        .catch((error) => {
          const status = error.response.status
          this.shifts = []
          if (status == 404) {
            this.errorShift = "No shifts found!"
          } else {
            this.errorShift = "Error loading shifts!"
          }
        });
    },
    openCreateShiftPopup: function () {
      this.currShift = {
        employeeEmail: '',
        date: new Date().toISOString().split('T')[0],
        startTime: "08:00",
        endTime: "13:00"
      };
      this.isUpdatingShift = false;
      this.showCreateShiftPopup = true;
    },
    openUpdateShiftPopup: function (id) {
      const shift = this.shifts.find(shift => shift.shiftId == id)
      this.currShift = {
        employeeEmail: shift.employeeEmail,
        date: shift.date,
        startTime: shift.startTime,
        endTime: shift.endTime
      }
      this.currShiftId = id
      this.isUpdatingShift = true;
      this.showCreateShiftPopup = true;
      this.popupErrorShift = ''
    },
    closeCreateShiftPopup: function () {
      this.showCreateShiftPopup = false;
    },
    createShift: function () {
      const url = this.isUpdatingShift ? ('/shift/'+this.currShiftId) : '/shift/create'
      const cmd = this.isUpdatingShift ? 'put' : 'post'

      axios.request({
        method: cmd,
        maxBodyLength: Infinity,
        url: backendUrl+url,
        headers: {
          'Content-Type': 'application/json'
        },
        data : {
          employeeEmail: this.currShift.employeeEmail,
          date: this.currShift.date,
          startTime: this.currShift.startTime + ":00",
          endTime: this.currShift.endTime + ":00"
        }
      })
        .then(response => {
          this.errorShift = ''
          this.closeCreateShiftPopup()
          this.getShifts()
        })
        .catch(e => {
          const status = e.response.status
          if (status == 400) {
            this.popupErrorShift = "Check your inputs!"
          } else if (status == 409) {
            this.popupErrorShift = "Employee already has this shift!"
          } else if (status == 404) {
            this.popupErrorShift = "Employee does not exist!"
          } else {
            this.popupErrorShift = "Internal error!"
          }
        })
    },
    deleteShift(id) {
      let config = {
        method: 'delete',
        maxBodyLength: Infinity,
        url: backendUrl + '/shift/delete/' + id,
        headers: { }
      };

      axios.request(config)
        .then((response) => this.getShifts())
        .catch((error) => {
          console.log(error)
        });
    },
    SelectNoFilter() {
      this.scheduleFilter = 0
      this.getShifts()
    },
    SelectEmployeeFilter() {
      this.scheduleFilter = 2
    },
    SelectDateFilter() {
      this.scheduleFilter = 1
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
  width: 350px;
  height: 350px;
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
}

</style>
