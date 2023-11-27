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
                <a class="nav-link clickable-text" @click="Employee">Account</a> <!--employee account-->
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Repair">Log Repair</a> <!--employee repair-->
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="ViewSchedule">View Schedule</a> <!--view employee schedule-->
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Reservations">View Reservations</a>
              </li>
              <li>
                <a class="nav-link clickable-text" @click="LogOut">Log Out</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="container">
        <div class="row">
          <div class="col-md-12 mx-auto text-center">
            <h1 class="text-center" style="font-family: 'Montserrat', serif; color: white; letter-spacing: 5px; font-size: 45px" >MARWANIOTT HOTEL EMPLOYEE PORTAL</h1>
            <p class="luxurious-text" style="font-weight: normal; color: white;">Welcome, {{name}}!</p>
          </div>
        </div>
      </div>
    </div>

    <div id = "intro"></div>
    <div class="custom-box">
      <div class="container">
        <div class="row">
          <div class="col-md-12">
            <h2 class="text-center mb-4" style="font-family: 'Montserrat', serif; color: #888; letter-spacing: 2px">YOUR PORTAL</h2>
            <p class="text-center" style="font-family: 'Georgia', serif">Welcome to your Marwaniott Hotel Employee Portal – your go-to spot for everything! Get ready for a user-friendly experience where you can easily check schedules, view your account, and stay in the loop on the latest buzz at the hotel. Dive in, explore, and let's make every day at the Marwaniott a fantastic one together! </p>
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
      await this.$router.push({name: "Home"})
    },
    async Repair() {
      // await this.$router.push({name: "EmployeeRepair", params: {email: this.email, name: this.name}})
      await this.$router.push({path: '/EmployeeRepair/' + this.email + '/' + this.name})
    },
    async ViewSchedule() {
      await this.$router.push({path: '/EmployeeSchedule/' + this.email + '/' + this.name})
    },

    async Reservations() {
      await this.$router.push({path: '/EmployeeReservation/' + this.email + '/' + this.name})
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

.luxurious-text {
  font-family: 'Georgia', sans-serif;
  font-weight: bold;
  color: black;
}

.custom-box {
  margin: 20px;
  padding: 30px;
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
