
<!--<template>-->
<!--  <div class="hero-section">-->
<!--  <div class="hero-section">-->
<!--    <div class="navbar-container">-->
<!--      <nav class="navbar navbar-expand-lg navbar-light transparent-background">-->
<!--        <a class="navbar-brand" href="#">-->
<!--          <img src="../../assets/marwaniottNoBG.png" alt="Your Logo" height="60">-->
<!--        </a>-->
<!--        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">-->
<!--          <span class="navbar-toggler-icon"></span>-->
<!--        </button>-->
<!--        <div class="collapse navbar-collapse justify-content-end" id="navbarNav">-->
<!--          <ul class="navbar-nav">-->
<!--            <li class="nav-item active">-->
<!--              <a class="nav-link" @click="Home">Home</a>-->
<!--            </li>-->
<!--            <li class="nav-item">-->
<!--              <a class="nav-link" @click="ManageEmployees">Manage Employees</a>-->
<!--            </li>-->
<!--            <li class="nav-item">-->
<!--              <a class="nav-link" @click="Repair">Manage Repairs</a>-->
<!--            </li>-->
<!--            <li class="nav-item">-->
<!--              <a class="nav-link" @click="Rooms">Manage Rooms</a>-->
<!--            </li>-->
<!--            <li class="nav-item">-->
<!--              <a class="nav-link" @click="Account">Account</a>-->
<!--            </li>-->
<!--            <li class="nav-item">-->
<!--              <a class="nav-link" @click="LogOut">Log Out</a>-->
<!--            </li>-->
<!--          </ul>-->
<!--        </div>-->
<!--      </nav>-->
<!--    </div>-->

<!--    <div class="main-container">-->
<!--      <div class="container">-->
<!--        <div class="row">-->
<!--          <div class="text-container">-->
<!--            <h1 class="portal-text" style="font-family: 'Montserrat', serif; color: white; letter-spacing: 5px; font-size: 45px;">OWNER PORTAL</h1>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--  </div>-->
<!--</template>-->

<template>
  <div>
    <div id = "main"></div>
    <div class="hero-section">
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
              <li class="nav-item active">
                <a class="nav-link" href="#">Home<span class="sr-only">(current)</span></a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="ManageEmployees">Manage Employees</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Repair">Manage Repairs</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Rooms">Manage Rooms</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Schedule">Schedule</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Account">Account</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Reservations">View Reservations</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="LogOut">Log Out</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="container">
        <div class="row">
          <div class="col-md-12 mx-auto text-center">
            <h1 class="text-center" style="font-family: 'Montserrat', serif; color: white; letter-spacing: 5px; font-size: 45px" >MARWANIOTT HOTEL OWNER PORTAL</h1>
          </div>
        </div>
      </div>
    </div>

    <div class="footer-container">
      <footer class="footer">
        <div class="d-flex align-items-center justify-content-center">
          <img src="../../assets/marwaniottNoBG.png" alt="Your Logo" height="65">
        </div>
        <div class="d-flex align-items-center justify-content-center">
          <p class="text-right" style="font-size: 10px">©The Marwaniott Hotel 2023. All Right Reserved.</p>
        </div>
      </footer>
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
  name: 'OwnerHome',

  data() {
    return {
      email: "",
      isLoggedIn: false,
    };
  },
  mounted(){
    this.email = this.$route.params.param1
  },
  methods: {
    async Login() {
      await this.$router.push({name: 'Login'})
    },
    async Home(){
      await this.$router.push({path: '/OwnerHome/' + this.email})
    },
    async ManageEmployees(){
      await this.$router.push({path: '/OwnerManageEmployees/' + this.email})
    },
    async Repair(){
      await this.$router.push({path: '/OwnerRepair/' + this.email})
    },
    async Rooms(){
      await this.$router.push({path: '/owner/manage_rooms/' + this.email})
    },
    async Account(){
      await this.$router.push({path: '/OwnerAccount/' + this.email})
    },
    async LogOut(){
      await this.$router.push({name: 'Home'})
    },
    async Schedule(){
      await this.$router.push({path: '/owner-view-schedule/' + this.email})
    },
    async Reservations(){
      await this.$router.push({path: '/OwnerReservation/' + this.email + '/' + this.name})
    },
  }
};
</script>

<style scoped>

.navbar-brand {
  margin-right: 0;
}

.hero-section {
  background: url('../../assets/hotelLobby.jpeg') center/cover no-repeat;
  padding: 400px 0;
  text-align: center;
}

.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}

.footer-container {
  position: relative;
  margin-top: 100px;
}

.footer {
  position: absolute;
  bottom: 0;
  width: 100%;
  background-color: rgba(136, 136, 136, 0.2);
  padding-top: 15px;
}

.clickable-text:hover {
  cursor: pointer;
  color: white !important;
}

.transparent-background {
  background-color: rgba(255, 255, 255, 0.6);
}

</style>
