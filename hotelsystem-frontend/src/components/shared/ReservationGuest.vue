<template>
  <div>
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
              <li class="nav-item">
                <a class="nav-link" @click="SignUp">SignUp</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="container mt-5">
        <div class="row justify-content-center">
          <div class="col-md-6">
            <div class="card">
              <div class="card-body">
                <h5 class="card-title">Hotel Room Reservation</h5>

                <!-- Number of Guests -->
                <div class="form-group">
                  <label for="guests">Number of Guests:</label>
                  <input type="number" class="form-control" v-model="numberOfGuests" id="guests">
                </div>

                <!-- Start Date -->
                <div class="form-group">
                  <label for="startDate">Start Date:</label>
                  <input type="date" class="form-control" v-model="startDate" id="startDate">
                </div>

                <!-- End Date -->
                <div class="form-group">
                  <label for="endDate">End Date:</label>
                  <input type="date" class="form-control" v-model="endDate" id="endDate">
                </div>

                <!-- Reservation Button -->
                <button class="btn btn-primary" @click="">Reserve Room</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ReservationGuest",
  methods: {
    async Login() {
      await this.$router.push({name: 'Login'})
    },
    async SignUp() {
      await this.$router.push({name: 'SignUp'})
    },
    async Home() {
      await this.$router.push({name: 'Home'})
    },
  }
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

.navbar-brand {
  margin-right: 0; /* Reset the margin for the navbar-brand */
}

.hero-section {
  background: url('../../assets/hotelView.png') center/cover no-repeat;
  padding: 300px 0;
  text-align: center;
}

.luxurious-text {
  font-family: 'Georgia', sans-serif;
  font-weight: bold;
  color: black;
}

.custom-login-button {
  background-color: transparent;
  color: #fff;
  border: 2px solid #fff;
  transition: background-color 0.3s, color 0.3s;
}

.custom-login-button:hover {
  background-color: #fff;
  color: #888;
}

.custom-box {
  border: 2px solid lightgray;
  margin: 20px;
  padding: 20px;
}

.transparent-background {
  background-color: rgba(255, 255, 255, 0.2); /* You can replace this color code with your desired dark color */
}

.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}
</style>
