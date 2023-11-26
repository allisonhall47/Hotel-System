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
                <a class="nav-link" href="#">Home<span class="sr-only">(current)</span></a> <!--CURRENT-->
              </li>
              <li class="nav-item">
                <a class="nav-link" @click="Employee">Account</a> <!--employee account-->
              </li>
              <li class="nav-item">
                <a class="nav-link" @click="Repair">Log Repair</a> <!--employee repair-->
              </li>
              <li class="nav-item">
                <a class="nav-link" @click="">View Schedule</a> <!--view employee schedule-->
              </li>
              <li>
                <a class="nav-link" @click="LogOut">Log Out</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <!--start rest of page here-->
      <div class="main-container">
        <div class="container">
          <div class="row">
            <div class="col-md-12 mx-auto text-center">
              <h1 class="text-center" style="font-family: 'Montserrat', serif; color: #ffffff; letter-spacing: 3px; font-size: 45px" >Marwaniott Hotel Employee Portal</h1>
              <p class="luxurious-text" style="font-weight: normal; color: #ffffff; font-size: 20px;">Welcome, {{name}}! </p>
            </div>
          </div>
        </div>
      </div>


    </div>
  </div>
</template>

<script>

import EmployeeRepair from "./EmployeeRepair.vue";

export default {
  name: 'EmployeeHome',

  data() {
    return {
      isLoggedIn: false,
      name: '',
      email: ''
    };
  },

  mounted() {
    this.email = this.$route.params.param1;
    this.name = this.$route.params.param2;
  },

  methods: {
    async Employee() {
      // await this.$router.push({name: "EmployeeAccount", params: {email: this.email, name: this.name}})
      await this.$router.push({path: '/EmployeeAccount/' + this.email + '/' + this.name})
    },
    async LogOut() {
      await this.$router.push({path: '/Home/'})
    },
    async Repair() {
      // await this.$router.push({name: "EmployeeRepair", params: {email: this.email, name: this.name}})
      await this.$router.push({path: '/EmployeeRepair/' + this.email + '/' + this.name})
    }
  }
};
</script>

<style scoped>

.navbar-brand {
  margin-right: 0;
}

.hero-section {
  background: url('../../assets/img_3.png');
  padding: 325px 0;
  text-align: center;
}

.transparent-background {
  background-color: rgba(169, 169, 169, 0.2);
}

.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}

</style>
