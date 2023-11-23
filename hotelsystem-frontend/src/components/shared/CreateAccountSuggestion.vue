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

      <div class="container">
        <div class="row">
          <div class="col-md-12 mx-auto text-center">
            <h1 class="text-center" style="font-family: 'Montserrat', serif; color: #888; font-size: 45px" >Looks like you didn't sign in yet!</h1>
            <p class="luxurious-text" style="font-weight: normal; color: #888;">Choose an option below to continue</p>
            <button class="btn btn-lg custom-signin-button" style="margin-right: 10px;" @click="Login">Log in</button>
            <button class="btn btn-lg custom-guest-button" @click="ReservationGuest">Continue as guest</button>
            <div style="margin-top: 15px;"></div>
            <p class="luxurious-text clickable-text" style="font-weight: normal; color: #888;" @click="SignUp">Don't have an account? Sign up here</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default{
  name: "CreateAccountSuggestion",
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
    async ReservationGuest() {
      await this.$router.push({name: 'ReservationGuest'})
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
  padding: 300px 0;
  text-align: center;
}

.navbar-brand {
  margin-right: 0; /* Reset the margin for the navbar-brand */
}

.luxurious-text {
  font-family: 'Georgia', sans-serif;
  font-weight: bold;
  color: black;
}

.custom-guest-button {
  background-color: transparent;
  color: #888;
  border: 2px solid #888;
  transition: background-color 0.3s, color 0.3s;
}

.custom-guest-button:hover {
  background-color: #888;
  color: white;
}
.custom-signin-button {
  background-color: transparent;
  color: #fff;
  border: 2px solid #fff;
  transition: background-color 0.3s, color 0.3s;
}

.custom-signin-button:hover {
  background-color: #fff;
  color: #888;
}

.clickable-text:hover {
  text-decoration: underline;
  cursor: pointer;
  color: white !important;
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
