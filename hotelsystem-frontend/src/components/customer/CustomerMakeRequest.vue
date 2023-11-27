<template>
  <div>
    <div id = "makeRequest"></div>
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
                <a class="nav-link clickable-text" @click="Home">Home</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Reservations">Reservations</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Account">Account</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="LogOut">Log Out</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="make-request-container">
        <div class="d-flex justify-content-center h-100">
          <div class="card">
            <div class="card-header">
              <h3 class="text-center" style="font-family: 'Montserrat', sans-serif; color: #888; letter-spacing: 3px">MAKE A REQUEST</h3>
            </div>
            <div class="card-body">
              <form id="request_form">
                <div class="input-group form-group">
                  <textarea id="request-description" v-model="newRequest.description" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="Enter a description"></textarea>
                </div>
                <div class="form-group">
                  <button @click="createRequest()" type="button" class="btn btn-primary btn-block mb-4 saveButton">Create</button>
                </div>
              </form>
            </div>
          </div>
        </div>

        <div class="d-flex justify-content-center h-100">
          <div class="table-container">
            <div class="table-responsive  luxurious-text" style="font-family: 'Montserrat', sans-serif; color: #888; letter-spacing: 3px">
              <h3>All Requests for reservation {{this.resId}}:</h3>
              <table class="table table-bordered">
                <thead>
                <tr>
                  <th scope="col" class="text-center">Reservation Number</th>
                  <th scope="col" class="text-center">Description</th>
                  <th scope="col" class="text-center">Request Id</th>
                  <th scope="col" class="text-center">Status</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="r in requests">
                  <td class="text-center" style="background: white"><input class="form-control text-center" :id="'number'.concat(r.requestId)" :value="r.reservation.reservationID" readonly></td>
                  <td class="text-center" style="background: white">
                    <div class="column-content">
                      <textarea class="form-control text-center" :id="'description'.concat(r.requestId)" :value="r.description" readonly></textarea>
                    </div>
                  </td>
                  <td class="text-center" style="background: white"><input class="form-control text-center" :id="'request'.concat(r.requestId)" :value="r.requestId" readonly></td>
                  <td class="text-center" style="background: white"><input class="form-control text-center" :id="'status'.concat(r.requestId)" :value="r.status" readonly></td>
                </tr>
                </tbody>
              </table>
            </div>
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
export default {
  name: "CustomerMakeRequest",
  data() {
    return {
      reservationData: {},
      newRequest: {description: '', reservationId: ''},
      requests: [],
      email: '',
      resId: '',
    };
  },
  mounted() {
    this.email = this.$route.params.param1
    this.resId = this.$route.params.param2
  },
  created() {
    this.email = this.$route.params.param1
    this.resId = this.$route.params.param2

    axiosClient.get("/reservation/".concat(this.resId))
      .then(response => {
        this.reservationData = response.data
      })
      .catch(err => {
        this.errorMsg = `Failure: ${err.response.data}`
        alert(this.errorMsg)
        //console.log(err.response.data)
      })

    axiosClient.get("/request")
      .then(response => {
        this.requests = response.data
      })
      .catch(err => {
        this.errorMsg = `Failure: ${err.response.data}`
        alert(this.errorMsg)
        //console.log(err.response.data)
      })
  },
  methods : {
    createRequest() {
      this.newRequest.reservationId = this.resId

      axiosClient.post("request/new", this.newRequest)
        .then(response => {
          //add it to the requests list
          this.requests.push({requestId: response.data.requestId, description: response.data.description, reservation: response.data.reservation, status: response.data.status})
          alert('request created')
        })
        .catch(err => {
          this.errorMsg = `Failure: ${err.response.data}`
          alert(this.errorMsg)
          //console.log(err.response.data)
        })

      //clear the input fields
      document.getElementById("request_form").reset()
      console.log(this.requests)
    },
    async Home(){
      await this.$router.push({path: '/CustomerHome/'+this.email})
    },
    async LogOut() {
      await this.$router.push({name: 'Home'})
    },
    async Reservations() {
      await this.$router.push({path: '/customer/reservation/' + this.email})
    },
    async Account() {
      await this.$router.push({path: '/CustomerAccount/' + this.email})
    },
  },
}
</script>

<style scoped>
.background {
  width: 100%;
  height: 100%;
  position: absolute;
  background: url('../../assets/hotelView.png') center center no-repeat;
  background-size: cover;
}

.big-container {
  background-color: rgba(255, 255, 255, 0.5);
  padding: 2%;
  margin: 2%;
  top: 17%;
  right: 2%;
  left: 2%;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: absolute;
  //overflow: auto;
}

.make-request-container {
  padding: 2%;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: absolute;
  top:20%;
  left: 30%;
  right: 30%;
  min-height: 300px;
  display: block;
  background: white;
  clear: both;
}

.table-container {
  background-color: rgba(255, 255, 255, 1);
  padding: 2%;
  margin-top: 2%;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: absolute;
  width: 100%;
  overflow-x: auto;
  overflow-y: auto;
  display: block;
  clear: both;
}

.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}

.navbar-brand {
  margin-right: 0;
}

.transparent-background {
  background-color: rgba(255, 255, 255, 0.3);
}

.nav-link:hover {
  cursor: pointer;
}
.card {
  width: 100%;
}

.saveButton {
  width: 20%;
  background-color: white;
  border: 2px solid #0055FF;
  color: #0055FF;
}

.saveButton:hover {
  width: 20%;
  background-color: #0055FF;
  border: 2px solid #0055FF;
  color: white;
}

.table-responsive {
  width: 100%;
  max-width: 100%;
  overflow-x: auto;
  overflow-y: auto;
  margin-bottom: 10px;
}

th:nth-child(1),
td:nth-child(1),
th:nth-child(3),
td:nth-child(3),
th:nth-child(4),
td:nth-child(4){
  width: 120px;
  max-width: 120px;
  min-width: 120px;
}

.column-content {
  max-width: 100%;
  overflow-x: auto;
  overflow-y: auto;
}

.luxurious-text {
  font-family: 'Georgia', sans-serif;
  font-weight: bold;
  color: black;
}

.clickable-text:hover {
  cursor: pointer;
  color: white !important;
}
</style>
