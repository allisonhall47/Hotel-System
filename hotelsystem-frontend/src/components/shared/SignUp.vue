<template>
  <div class="signup">
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
                <a class="nav-link" @click="Login">LogIn</a>
              </li>
              <li class="nav-item active">
                <a class="nav-link" href="#">SignUp<span class="sr-only">(current)</span></a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="d-flex justify-content-center h-100">
        <div class="card">
          <div class="card-header">
            <h3 class="text-center" style="font-family: 'Montserrat', sans-serif; color: #888; letter-spacing: 2px">SIGN UP</h3>
          </div>
          <div class="card-body">
            <form>
              <div class="input-group form-group">
                <div class="input-group-prepend">
                  <span class="input-group-text"><i class="fas fa-key"></i></span>
                </div>
                <input id="name" v-model="name" type="text" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="Full Name">
              </div>
              <div class="input-group form-group">
                <div class="input-group-prepend">
                  <span class="input-group-text"><i class="fas fa-user"></i></span>
                </div>
                <input id="email" v-model="email" type="email" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="Email Address">
              </div>
              <div class="input-group form-group">
                <div class="input-group-prepend">
                  <span class="input-group-text"><i class="fas fa-key"></i></span>
                </div>
                <input id="password" v-model="password" type="password" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="Password">
              </div>
              <div class="input-group form-group">
                <div class="input-group-prepend">
                  <span class="input-group-text"><i class="fas fa-key"></i></span>
                </div>
                <input id="passwordConfirm" v-model="password_confirm" type="password" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="Confirm Password">
              </div>
              <div class="input-group form-group">
                <div class="input-group-prepend">
                  <span class="input-group-text"><i class="fas fa-key"></i></span>
                </div>
                <input id="address" v-model="address" type="text" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="Address">
              </div>
              <div class="input-group form-group">
                <div class="input-group-prepend">
                  <span class="input-group-text"><i class="fas fa-key"></i></span>
                </div>
                <input id="dob" v-model="dob" type="date" class="form-control" style="font-family: 'Georgia', sans-serif">
              </div>
              <div class="form-group">
                <button @click="createCustomer()" type="button"
                        class="btn btn-primary btn-block mb-4 signinbutton">Sign Up</button>
              </div>
            </form>
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
  name: 'SignUp',
  data() {
    return {
      email: '',
      password: '',
      password_confirm: '',
      name: '',
      address: '',
      user: '',
      dob: '',
      errorMsg: '',
      new_customer: [],
      new_account: [],
      accountNumber: 0,
    };
  },
  methods: {
    createCustomer(){
      this.email = document.getElementById("email").value;
      this.password = document.getElementById("password").value;
      this.password_confirm = document.getElementById("passwordConfirm").value;
      this.name = document.getElementById("name").value;
      this.address = document.getElementById("address").value;
      this.dob = document.getElementById("dob").value;

      if(this.password === this.password_confirm){
        const account_request = {password: this.password, address: this.address, dob: this.dob};
        axiosClient.post("/account/create", account_request)
          .then((response) => {
            this.accountNumber = response.data.accountNumber
            this.new_account = response

            const customer_request = {name: this.name, email: this.email, accountNumber: this.accountNumber}
            axiosClient.post("/customer/create", customer_request)
              .then((response) => {
                alert('Account successfully created.')
                this.new_customer = response
                this.$router.push({name: 'Login'})
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
      } else {
        alert('Passwords do not match.')
      }

    },
    async Login() {
      await this.$router.push({name: 'Login'})
    },
    async Home(){
      await this.$router.push({name: 'Home'})
    },
  }
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

.card {
  background-color: white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: absolute;
  top: 20%;
  left: 32%;
  right: 32%;
  min-height: 300px;
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
  background-color: rgba(255, 255, 255, 0.3);
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
