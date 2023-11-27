<template>
  <div class="login">
    <div class="background">

      <div class="signup-container">
        <div class="signup-box">
          <div class="card-body">
            <form>
              <h1 class="text-center" style="font-family: 'Montserrat', serif; color: #888; font-size: 42px" >Do you want to confirm your reservation?</h1>
              <div style="margin-top: 30px;"></div>
              <div class="form-group">
                <button @click="CreateCustomer" type="button" class="btn btn-primary btn-block mb-4 signinbutton">Confirm</button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <div class="background-image">
        <div class="layout-background-image">
        </div>
      </div>

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

export default{
  name: "ConfirmReservation",
  data(){
    return {
      fullName: '',
      customerEmail: '',
      startDate: '',
      endDate: '',
      combination: '',
      guests: 0,
      reservationId: 0,
      regularRoom: ['Regular', 899, 'Queen', 2],
      deluxeRoom: ['Deluxe', 1299, 'Queen', 4],
      luxuryRoom: ['Luxury', 1499, 'King', 2],
      suite: ['Suite', 1999, 'King', 4],
    }
  },
  mounted(){
    this.startDate = this.$route.params.param1;
    this.endDate = this.$route.params.param2;
    this.combination = JSON.parse(decodeURIComponent(this.$route.params.param3));
    this.customerEmail = this.$route.params.param5;
    this.guests = this.$route.params.param4;
    console.log(this.combination);
  },
  methods: {
    async Account() {
      await this.$router.push({name: 'CustomerAccount', params: {email: this.customerEmail}})
    },
    async LogOut() {
      await this.$router.push({name: 'Home'})
    },
    async Home() {
      await this.$router.push({path: '/CustomerHome/' + this.customerEmail})
    },
    async Reservations() {
      await this.$router.push({path: '/customer/reservation/' + this.customerEmail})
    },
    async CreateCustomer(){
          const reservation_request = {numPeople: this.guests, checkin: this.startDate, checkOut: this.endDate, customerEmail: this.customerEmail};
          axiosClient.post("/reservation/new", reservation_request)
            .then((response) => {
              this.reservationId = response.data.reservationId;

              if(this.combination.hasOwnProperty("Regular")) {
                axiosClient.get("/specificRoom/available/type/Regular/" + this.startDate + '/' + this.endDate)
                  .then((response) => {
                    for (let i = 0; i < this.combination.Regular && i < response.data.length; i++) {
                      let item = response.data[i];
                      let specificRoomNumber = item.number;
                      const reservedRoom_request = {linkedReservationId: this.reservationId, roomNumber: specificRoomNumber};
                      axiosClient.post("/reservedRoom/new", reservedRoom_request)
                    }
                  })
                  .catch((err) => {
                    this.errorMsg = `Failure: ${err.response.data}`
                    alert(this.errorMsg)
                    return;
                  })
              }
              if(this.combination.hasOwnProperty("Luxury")) {
                axiosClient.get("/specificRoom/available/type/Luxury/"+ this.startDate + '/' + this.endDate)
                  .then((response) => {
                    for (let i = 0; i < this.combination.Luxury && i < response.data.length; i++) {
                      let item = response.data[i];
                      let specificRoomNumber = item.number;
                      const reservedRoom_request = {linkedReservationId: this.reservationId, roomNumber: specificRoomNumber};
                      axiosClient.post("/reservedRoom/new", reservedRoom_request)
                    }
                  })
                  .catch((err) => {
                    this.errorMsg = `Failure: ${err.response.data}`
                    alert(this.errorMsg)
                    return;
                  })
              }
              if(this.combination.hasOwnProperty("Suite")) {
                axiosClient.get("/specificRoom/available/type/Suite/"+ this.startDate + '/' + this.endDate)
                  .then((response) => {
                    for (let i = 0; i < this.combination.Suite && i < response.data.length; i++) {
                      let item = response.data[i];
                      let specificRoomNumber = item.number;
                      const reservedRoom_request = {linkedReservationId: this.reservationId, roomNumber: specificRoomNumber};
                      axiosClient.post("/reservedRoom/new", reservedRoom_request)
                    }
                  })
                  .catch((err) => {
                    this.errorMsg = `Failure: ${err.response.data}`
                    alert(this.errorMsg)
                    return;
                  })
              }
              if(this.combination.hasOwnProperty("Deluxe")) {
                axiosClient.get("/specificRoom/available/type/Deluxe"+ this.startDate + '/' + this.endDate)
                  .then((response) => {
                    for (let i = 0; i < this.combination.Deluxe && i < response.data.length; i++) {
                      let item = response.data[i];
                      let specificRoomNumber = item.number;
                      const reservedRoom_request = {linkedReservationId: this.reservationId, roomNumber: specificRoomNumber};
                      axiosClient.post("/reservedRoom/new", reservedRoom_request)
                    }
                  })
                  .catch((err) => {
                    this.errorMsg = `Failure: ${err.response.data}`
                    alert(this.errorMsg)
                    return;
                  })
              }
            })
            .catch((err) => {
              this.errorMsg = `Failure: ${err.response.data}`
              alert(this.errorMsg)
              return;
            })

      await this.$router.push({path: '/BookedConfirmationCustomer/' + this.customerEmail});
    }
  }
}
</script>

<style scoped>
.background {
  width: 100%;
  height: 100%;
  position: absolute;
  background: white;
  background-size: cover;
}

.signup {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.signup-box {
  width: 65%;
}

.background {
  flex: 1;
  display: flex;
}

.background-image {
  flex: 1;
  background-image: url('../../assets/hotelRoomView.png');
  background-size: cover;
  background-position: center;
}

.signup-container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}

.transparent-background {
  background-color: rgba(136, 136, 136, 0.3);
}

.signinbutton {
  width: 50%;
  margin-top: 5%;
  margin-left: 25%;
  background-color: white;
  border: 2px solid #888888;
  color: #888888;
}

.signinbutton:hover {
  border: #888888;
  background-color: #888888;
  border: 2px solid #888888;
  color: white;
}

.clickable-text:hover {
  cursor: pointer;
  color: white !important;
}

.card-header {
  background: white;
  border-bottom: white;
}
</style>
