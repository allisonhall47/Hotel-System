<template>
  <div class="ownerAccount">
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
                <a class="nav-link" @click="Home">Home</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" @click="ManageEmployees">Manage Employees</a>
              </li>
              <li class="nav-item active">
                <a class="nav-link" href="#">Manage Repairs<span class ="sr-only">(current)</span></a>
              </li>
              <li class="nav-item">
                <a class="nav-link" @click="Account">Manage Account</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" @click="LogOut">Logout</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <!--put rest of page here-->

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
  name: "OwnerRepair",
  props: {
    email: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      isLoggedIn: false,
    };
  },
  created() {

  },
  methods: {
    async Home(){
      await this.$router.push({name: 'OwnerHome', params: {email: this.email}})
    },
    async ManageEmployees(){
      await this.$router.push({name: 'OwnerManageEmployees', params: {email: this.email}})
    },
    async Account(){
      await this.$router.push({name: 'OwnerAccount', params: {email: this.email}})
    },
    async LogOut(){
      await this.$router.push({name: 'Home'})
    },
  }
}
</script>

<style scoped>

</style>
