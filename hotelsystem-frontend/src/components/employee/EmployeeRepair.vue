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
                <a class="nav-link" @click="Login">Account</a> <!--employee account-->
              </li>
              <li class="nav-item">
                <a class="nav-link" @click="SignUp">Log Repair</a> <!--employee repair-->
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

    </div>
  </div>
</template>

<script>
export default {
  name: "EmployeeRepair",
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
}
</script>

<style scoped>

</style>
