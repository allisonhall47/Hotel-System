
<template>
  <div class="hero-section">
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
              <a class="nav-link" @click="Home">Home</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" @click="ManageEmployees">Manage Employees</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" @click="Repair">Manage Repairs</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" @click="Rooms">Manage Rooms</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" @click="Account">Account</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" @click="LogOut">Log Out</a>
            </li>
          </ul>
        </div>
      </nav>
    </div>

    <div class="main-container">
      <div class="container">
        <div class="row">
          <div class="text-container">
            <h1 class="portal-text" style="font-family: 'Montserrat', serif; color: white; letter-spacing: 5px; font-size: 45px;">OWNER PORTAL</h1>
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
  }
};
</script>

<style scoped>

.navbar-brand {
  margin-right: 0; /* Reset the margin for the navbar-brand */
}

.hero-section {
  background: url('../../assets/hotelLobby.jpeg') center/cover no-repeat;
  min-height: 100vh; /* Full viewport height */
  position: relative; /* This is important for absolute positioning inside */
}

.text-container {
  position: absolute;
  top: 36.25%; /* Adjust these values to move the text */
  left: 50%; /* Adjust these values to move the text */
  transform: translate(-50%, -50%); /* Center the text */
}

.portal-text {
  font-family: 'Montserrat', serif;
  color: white;
  letter-spacing: normal; /* Keeping this normal or a small pixel value */
  font-size: 3vw; /* Using viewport width for responsive font size */
  margin-top: -2%; /* Adjust this to move the text vertically */
  margin-left: -2%; /* Adjust this to move the text horizontally */
}

.main-container{
  background-color: transparent;
}

.transparent-background {
  background-color: rgba(255, 255, 255, 0.6) !important;
}

.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}
  .nav-link:hover {
    cursor: pointer;
}

</style>
