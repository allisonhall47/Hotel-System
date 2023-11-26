<template>
  <div class="login">
    <div class="background">

      <div class="signup-container">
        <div class="signup-box">
          <div class="card-header">
            <h3 class="text-center" style="font-family: 'Montserrat', sans-serif; color: #888; letter-spacing: 2px">GUEST INFO</h3>
          </div>
          <div class="card-body">
            <form>
              <div class="input-group form-group">
                <input id="fullName" v-model="fullName" type="text" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="Full Name">
              </div>
              <div class="input-group form-group">
                <input id="email" v-model="email" type="email" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="Email Address">
              </div>
              <div class="form-group">
                <button @click="CreateCustomer" type="button" class="btn btn-primary btn-block mb-4 signinbutton">Book Room</button>
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
                <a class="nav-link" @click="LogIn">LogIn</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="SignUp">SignUp</a>
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
  name: "CreateCustomerPage",
  data(){
    return {
      fullName: '',
      email: '',
      startDate: '',
      endDate: '',
      combination: '',
      guests: 0,
      reservationId: 0,
      regularRoom: ['Regular', 899, 'Queen', 2],
      deluxeRoom: ['Deluxe', 1299, 'Queen', 4],
      luxuryRoom: ['Luxury', 1499, 'King', 2],
      suite: ['Suite', 1999, 'King', 4],
      new_customer_email: '',
    }
  },
  mounted(){
    this.startDate = this.$route.params.param1;
    this.endDate = this.$route.params.param2;
    this.combination = JSON.parse(decodeURIComponent(this.$route.params.param3));
    this.guests = this.$route.params.param4;
    console.log(this.combination);
  },
  methods: {
    async LogIn() {
      await this.$router.push({name: 'Login'})
    },
    async SignUp() {
      await this.$router.push({name: 'SignUp'})
    },
    async Home() {
      await this.$router.push({name: 'Home'})
    },
    async CreateCustomer(){
      this.email = document.getElementById("email").value;
      this.fullName = document.getElementById("fullName").value;
      const customer_request = {name: this.fullName, email: this.email};
      axiosClient.post("/customer/create", customer_request)
        .then((response) => {
          this.new_customer_email = response.data.email;
          const reservation_request = {numPeople: this.guests, checkin: this.startDate, checkOut: this.endDate, customerEmail: this.new_customer_email};
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
            })
        })
        .catch((err) => {
          this.errorMsg = `Failure: ${err.response.data}`
          alert(this.errorMsg)
        })
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
