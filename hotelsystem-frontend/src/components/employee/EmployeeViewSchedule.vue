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
                <a class="nav-link clickable-text" @click="Home">Home</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Employee">Account</a> <!--employee account-->
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Repairs">Log Repair</a>
              </li>
              <li class="nav-item active">
                <a class="nav-link" href="#">View Schedule<span class="sr-only">(current)</span></a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Reservations">View Reservations</a>
              </li>
              <li>
                <a class="nav-link clickable-text" @click="LogOut">Log Out</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="table-container shadow">
        <div class="buttons-container">
          <h2 class="prettyheader">YOUR SHIFTS</h2>
        </div>

        <table>
          <tr>
            <th>Date</th>
            <th>Start time</th>
            <th>End time</th>
          </tr>

          <tr v-for="shift in shifts" :key="shift.id">
            <td>{{ shift.date }}</td>
            <td>{{ shift.startTime }}</td>
            <td>{{ shift.endTime }}</td>
          </tr>
        </table>
        <div class="centerbuttoncontainer">
          <label class="prettylabel" v-if="shifts.length == 0">{{errorShift}}</label>
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
  name: 'EmployeeViewSchedule',
  data() {
    return {
      shifts: [],
      errorShift: '',
      email: "",
      name: "",
    }
  },
  mounted() {
    this.email = this.$route.params.param1;
    this.name = this.$route.params.param2;
  },
  created: function () {
    this.getShifts()
  },
  methods: {
    getShifts() {
      axios.request({
        method: 'get',
        maxBodyLength: Infinity,
        url: backendUrl+'/shifts/get/'+this.email,
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
    async Home() {
      await this.$router.push({path: '/EmployeeHome/' + this.email + '/' + this.name})
    },
    async Repairs() {
      await this.$router.push({path: '/EmployeeRepair/' + this.email + '/' + this.name})
    },
    async Employee() {
      await this.$router.push({path: '/EmployeeAccount/' + this.email + '/' + this.name})
    },
    async Reservations() {
      await this.$router.push({path: '/EmployeeReservation/' + this.email + '/' + this.name})
    },
    async LogOut() {
      await this.$router.push({name: 'Home'})
    },
  },
}

</script>

<style scoped>
.background {
  width: 100%;
  height: 100%;
  position: absolute;
  background: url('../../assets/hotelLobby.jpeg') center center no-repeat;
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
  background-color: rgba(255, 255, 255, 1);
  padding: 2%;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: absolute;
  top: 25%;
  left: 10%;
  right: 10%;
  min-height: 500px;
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

.centerbuttoncontainer {
  margin-top: 5%;
  text-align: center;
}

.prettylabel {
  width: 25%;
  color: #4e555b;
}

.prettyheader {
  margin-bottom: 3%;
  font-family: 'Montserrat', sans-serif;
  color: #888;
  letter-spacing: 3px;
}

.clickable-text:hover {
  cursor: pointer;
  color: white !important;
}

.transparent-background {
  background-color: rgba(255, 255, 255, 0.6);
}

</style>
