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
              <li class="nav-item active">
                <a class="nav-link" @click="Login">LogIn</a>
              </li>
              <li class="nav-item active">
                <a class="nav-link" href="#">SignUp<span class="sr-only">(current)</span></a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="login-container">
        <div class="d-flex justify-content-center h-100">
          <div class="card">
            <div class="card-header">
              <h3 class="text-center" style="font-family: 'Montserrat', serif; color: #888; letter-spacing: 2px">SIGN UP</h3>
            </div>
            <div class="card-body">
              <form>
                <div class="input-group form-group">
                  <div class="input-group-prepend">
                    <span class="input-group-text"><i class="fas fa-key"></i></span>
                  </div>
                  <input id="name" v-model="name" type="text" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="full name">
                </div>
                <div class="input-group form-group">
                  <div class="input-group-prepend">
                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                  </div>
                  <input id="email" v-model="email" type="email" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="email">
                </div>
                <div class="input-group form-group">
                  <div class="input-group-prepend">
                    <span class="input-group-text"><i class="fas fa-key"></i></span>
                  </div>
                  <input id="password" v-model="password" type="password" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="password">
                </div>
                <div class="input-group form-group">
                  <div class="input-group-prepend">
                    <span class="input-group-text"><i class="fas fa-key"></i></span>
                  </div>
                  <input id="address" v-model="address" type="text" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="address">
                </div>
                <div class="input-group form-group">
                  <div class="input-group-prepend">
                    <span class="input-group-text"><i class="fas fa-key"></i></span>
                  </div>
                  <input id="dob" v-model="dob" type="date" class="form-control" style="font-family: 'Georgia', sans-serif">
                </div>

                <!--                <div class="form-group">-->
                <!--                    <button v-bind:disabled="createUserButtonDisabled" @click="getUser()" type="button" class="btn btn-primary btn-block mb-4 signinbutton">Sign in</button>-->
                <!--                </div>-->
                <div class="form-group">
                  <button v-bind:disabled="createCustomerButtonDisabled" @click="createCustomer()" type="button"
                          class="btn btn-primary btn-block mb-4 signinbutton">Sign Up</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'SignUp',
  data() {
    return {
      email: '',
      password: '',
      name: '',
      address: '',
      user: '',
      dob: ''
    };
  },
  methods: {
    createCustomer(){

    },
    async Login() {
      await this.$router.push({name: 'Login'})
    },
    async Home(){
      await this.$router.push({name: 'Home'})
    },
  },
  computed: {
    createCustomerButtonDisabled() {
      return !this.email.trim() || !this.password.trim() || !this.user.trim()
    }
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

.login-container {
  background-color: rgba(255, 255, 255, 0.5);
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: absolute;
  top: 200px;
  left: 600px;
  right: 600px;
  height: 450px;
}

.card {
  width: 350px;
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
  color: white;
}


</style>
