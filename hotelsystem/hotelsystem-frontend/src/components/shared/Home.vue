<!-- Save this as home.vue -->
<template>
  <div>

    <div class="hero-section">
      <div class="container">
        <div class="row">
          <!-- Logo in the top left corner -->
          <div class="col-md-6 d-flex align-items-center">
            <a class="navbar-brand" href="#">
              <img src="../../assets/marwaniottNoBG.png" alt="Marwaniott Hotel Logo" class="logo">
            </a>
          </div>
          <!-- Buttons in the top right corner -->
          <div class="col-md-6 text-right">
            <div class="navbar-nav">
              <button v-if="!isLoggedIn" class="btn btn-secondary btn-outline-light custom-login-button" @click="showLoginModal">Login</button>
              <button v-if="!isLoggedIn" class="btn btn-secondary ml-2 btn-outline-light custom-login-button" @click="showSignupModal">Sign Up</button>
            </div>
          </div>
          <!-- Centered content in the middle -->
          <div class="col-md-12 mx-auto text-center">
            <h1 class="luxurious-text">The Marwaniott Hotel</h1>
            <p class="luxurious-text">explore. relax. rejuvenate.</p>
            <button class="btn btn-primary btn-lg mt-3 btn-outline-light custom-login-button">Book Your Stay</button>
          </div>
        </div>
      </div>
    </div>




    <div class="container mt-4">
      <div class="row">
        <div class="col-md-12">
          <h2 class="luxurious-text text-center mb-4">Our Rooms</h2>
          <!-- Add room views or a gallery here -->
          <p class="text-center luxurious-text">Discover the elegance and comfort of our rooms.</p>
          <!-- Add room images or gallery here -->
        </div>
      </div>
    </div>

    <!-- Add your login and signup modals here -->
  </div>
</template>

<script>
export default {
  data() {
    return {
      isLoggedIn: false,
      // Add other data properties as needed
    };
  },
  methods: {
    showLoginModal() {
      // Implement login modal display logic
      console.log('Show login modal');
    },
    showSignupModal() {
      // Implement signup modal display logic
      console.log('Show signup modal');
    },
    logout() {
      // Implement logout logic
      console.log('Logout');
    },
  },
};
</script>

<style scoped>
/* Add your custom styles here */

.navbar-brand {
  margin-right: 0; /* Reset the margin for the navbar-brand */
}

.hero-section {
  background: url('../../assets/hotelView.png') center/cover no-repeat;
  padding: 250px 0;
  text-align: center;
}

.luxurious-text {
  font-family: 'Georgia', sans-serif; /* Add a luxurious font */
  color: #fff; /* Set text color to white or a luxurious color */
}

.logo {
  max-height: 80px;
  margin-left: 0;
}

.custom-login-button {
  background-color: #A11D2B; /* Replace with the desired color code */
}

/* Add more styles as needed */
</style>
