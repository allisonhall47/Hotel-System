<template>
  <div class="login">
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
              <li class="nav-item active">
                <a class="nav-link" href="#">LogIn<span class="sr-only">(current)</span></a>
              </li>
              <li class="nav-item">
                <a class="nav-link" @click="SignUp">SignUp</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="login-container">
        <div class="d-flex justify-content-center h-100">
          <div class="card">
            <div class="card-header">
              <h3 class="text-center" style="font-family: 'Montserrat', serif; color: #888; letter-spacing: 2px">LOG IN</h3>
            </div>
            <div class="card-body">
              <form>
                <div class="input-group form-group">
                  <div class="input-group-prepend">
                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                  </div>
                  <input id="email" v-model="email" type="email" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="email">
                </div>
                <div class="input-group form-group">
                  <div class="input-group-prepend">
                    <span class="input-group-text"><i class="fas fa-key"></i></span>
                  </div>
                  <input id="password" v-model="password" type="password" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="password">
                </div>

                <div class="form-check form-check-inline">
                  <input v-model="user" class="form-check-input" type="radio" name="user" id="owner" value="Owner">
                  <label class="form-check-label" for="owner">Owner</label>
                </div>

                <div class="form-check form-check-inline">
                  <input v-model="user" class="form-check-input" type="radio" name="user" id="customer" value="Customer">
                  <label class="form-check-label" for="customer">Customer</label>
                </div>
                <div class="form-check form-check-inline">
                  <input v-model="user" class="form-check-input" type="radio" name="user" id="employee" value="Employee">
                  <label class="form-check-label" for="employee">Employee</label>
                </div>
<!--                <div class="form-group">-->
<!--                    <button v-bind:disabled="createUserButtonDisabled" @click="getUser()" type="button" class="btn btn-primary btn-block mb-4 signinbutton">Sign in</button>-->
<!--                </div>-->
                <div class="form-group">
                  <button @click="getUser()" type="button"
                          class="btn btn-primary btn-block mb-4 signinbutton">Sign in</button>
                </div>
              </form>
            </div>
            <div class="card-footer">
              <div class="d-flex align-items-center links">
                <p>Don't have an account?</p>
                <a class="nav-link" @click="SignUp">Sign Up</a>
              </div>
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
  name: 'Login',
  data() {
    return {
      email: '',
      password: '',
      user: '',
      logged_user: [],
      errorMsg: '',
    };
  },
  mounted() {
    this.email = this.$route.params.param1;
    this.name = this.$route.params.param2;
  },

  methods: {
    getUser(){
      if(this.user === "Customer"){
        axiosClient.get("/customer?email=" + this.email)
          .then((response) => {
            if(response.data.accountNumber !== 0){
              this.logged_user = response
              alert("Successfully logged in.")
              this.$router.push({name: 'CustomerHome', params: {email: this.email}})
            } else {
              alert("No account exists with this email.")
            }
          })
          .catch((err) => {
            this.errorMsg = `Failure: ${err.response.data}`
            alert(this.errorMsg)
          })
      }
      else if (this.user === "Employee"){
        axiosClient.get("/employee?email=" + this.email)
          .then((response) => {
            if(response.data.accountNumber !== 0){
              this.logged_user = response
              alert("Successfully logged in.")
              var employeeName = response.data.name;
              // this.$router.push({name: 'EmployeeHome', params: {email: this.email, name: employeeName}})
              this.$router.push({path: '/EmployeeHome/' + this.email + '/' + employeeName})
            } else {
              alert("No account exists with this email.")
            }
          })
          .catch((err) => {
            this.errorMsg = `Failure: ${err.response.data}`
            alert(this.errorMsg)
          })
      }
      else if (this.user === "Owner"){
        axiosClient("/owner/email?email=" + this.email)
          .then((response) => {
            this.logged_user = response
            alert("Successfully logged in.")
            this.$router.push({name: 'OwnerHome', params: {email: this.email}})
          })
          .catch((err) => {
            this.errorMsg = `Failure: ${err.response.data}`
            alert(this.errorMsg)
          })
      }
      else {
        alert("Please select account log in type.")
      }
    },
    async SignUp() {
      await this.$router.push({name: 'SignUp'})
    },
    async Home(){
      await this.$router.push({name: 'Home'})
    },
  },
};
</script>

<style scoped>
.background {
  width: 100%;
  height: 100%;
  position: absolute;
  background: url('../../assets/hotelView.png') center center no-repeat;
  background-size: cover;
}

.login-container {
  background-color: rgba(255, 255, 255, 0.5);
  padding: 2%;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: absolute;
  top: 25%;
  left: 30%;
  right: 30%;
  min-height: 300px;
}

.card {
  width: 100%; /* Use 100% for responsiveness */
}

.input-group-prepend {
  background-color: transparent;
}

.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}

.transparent-background {
  background-color: rgba(255, 255, 255, 0.3); /* You can replace this color code with your desired dark color */
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



</style>
