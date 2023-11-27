<template>
  <div>
    <div class="create-account">
      <div class="background">
        <div class="create-account-container">
          <div class="create-account-box">
            <div class="container">
              <div class="row">
                <div class="col-md-12 mx-auto text-center">
                  <h1 class="text-center" style="font-family: 'Montserrat', serif; color: #888; font-size: 42px" >Reservation Confirmed!</h1>
                  <div style="margin-top: 30px;"></div>
                  <p class="luxurious-text" style="font-weight: normal; color: #888;">We are looking forward to welcoming you at The Marwaniott Hotel</p>
                  <div style="margin-top: 30px;"></div>
                  <button class="btn custom-guest-button" style="margin-right: 10px;" @click="Home">Return to Home</button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="background-image">
          <div class="layout-background-image">
          </div>
        </div>

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
                  <a class="nav-link clickable-text" @click="Home">Home</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link clickable-text" @click="Login">LogIn</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link clickable-text" @click="SignUp">SignUp</a>
                </li>
              </ul>
            </div>
          </nav>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
export default{
  name: "BookedConfirmation",
  methods: {
    async Login() {
      await this.$router.push({path: '/Login/'})
    },
    async SignUp() {
      await this.$router.push({path: '/SignUp/'})
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
  background: white;
  background-size: cover;
}

.create-account {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.create-account-box {
  width: 65%;
}

.background {
  flex: 1;
  display: flex;
}

.background-image {
  flex: 1;
  background-image: url('../../assets/hotelRoomView.png');
  background-size: cover;
  background-position: center;
}

.create-account-container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.navbar-brand {
  margin-right: 0;
}

.luxurious-text {
  font-family: 'Georgia', sans-serif;
  font-weight: bold;
  color: black;
}


.clickable-text:hover {
  cursor: pointer;
  color: white !important;
}

.transparent-background {
  background-color: rgba(136, 136, 136, 0.3);
}

.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
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

</style>
