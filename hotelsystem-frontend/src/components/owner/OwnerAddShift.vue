<template>
  <div>
    <div id = "addShift"></div>
    <div class="background">
      <div class="navbar-container">
        <nav class="navbar navbar-expand-lg navbar-light transparent-background">
          <a class="navbar-brand" href="#">
            <img src="../../assets/marwaniottNoBG.png" alt="Your Logo" height="60">
          </a>
<!--          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">-->
<!--            <span class="navbar-toggler-icon"></span>-->
<!--          </button>-->
<!--          <div class="collapse navbar-collapse justify-content-end" id="navbarNav">-->
<!--            <ul class="navbar-nav">-->
<!--              <li class="nav-item active">-->
<!--                <a class="nav-link" href="#">Home<span class="sr-only">(current)</span></a>-->
<!--              </li>-->
<!--              <li class="nav-item">-->
<!--                <a class="nav-link" @click="Login">LogIn</a>-->
<!--              </li>-->
<!--              <li class="nav-item">-->
<!--                <a class="nav-link" @click="SignUp">SignUp</a>-->
<!--              </li>-->
<!--            </ul>-->
<!--          </div>-->
        </nav>
      </div>

      <div class="add-shift-container">
        <div class="d-flex justify-content-center h-100">
          <div class="card">
            <div class="card-header">
              <h3 class="text-center" style="font-family: 'Montserrat', serif; color: #888; letter-spacing: 2px">ADD SHIFT</h3>
            </div>
            <div class="card-body">
              <form id="shift_form">
                <div class="input-group form-group">
                  <div class="input-group-prepend">
                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                  </div>
                  <input id="date-shift" v-model="date_shift" type="date" class="form-control" style="font-family: 'Georgia', sans-serif">
                </div>
                <div class="input-group form-group">
                  <div class="input-group-prepend">
                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                  </div>
                  <select name="employees" id="cars" form="shift_form" class="form-control">
                    <option v-for="e in employees">{{ e.email }}</option>
                  </select>
                </div>
              </form>
<!--                <div class="input-group form-group">-->
<!--                  <select name="employees" id="cars" form="shift_form" class="b-dropdown-form">-->
<!--                    <option value="volvo">Volvo</option>-->
<!--                    <option value="saab">Saab</option>-->
<!--                    <option value="opel">Opel</option>-->
<!--                    <option value="audi">Audi</option>-->
<!--                  </select>-->
<!--                </div>-->
            </div>
            <div class="card-footer">
              <div class="d-flex align-items-center links">
                <p></p>
                <a></a>
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
  name: "OwnerAddShift",
  data() {
    return {
      date_shift: '',
      employees: [
        { email: "Alice"},
        { email: "Bob" },
      ],
    };
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

.add-shift-container {
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

.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}
.card {
  width: 100%;
}

.input-group-prepend {
  background-color: transparent;
}

.navbar-brand {
  margin-right: 0;
}

.transparent-background {
  background-color: rgba(255, 255, 255, 0.3);
}
</style>
