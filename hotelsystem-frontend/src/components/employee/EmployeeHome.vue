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
                <a class="nav-link" href="#">Home<span class="sr-only">(current)</span></a>
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

      <!--start rest of page here-->

    </div>
  </div>
</template>

<script>

export default {
  name: 'EmployeeHome',
  props: {
    email: {
      type: String,
      required: true
    }
  },
  data() {
    return {

    };
  },
  methods: {

  }
};
</script>

<style scoped>

.navbar-brand {
  margin-right: 0;
}

.hero-section {
  background: url('../../assets/hotelView.png') center/cover no-repeat;
  padding: 300px 0;
  text-align: center;
}

.transparent-background {
  background-color: rgba(255, 255, 255, 0.2);
}

.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}

</style>
