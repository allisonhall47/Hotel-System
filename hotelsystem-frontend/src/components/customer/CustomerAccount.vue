<template>
  <div class="customerAccount">
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
              <li class="nav-item active">
                <a class="nav-link" @click="Home">Home</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">Account<span class="sr-only">(current)</span></a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="profile-box">
        <div class="container rounded bg-white mt-5 mb-5">
          <div class="row">
            <div class="col-md-3 border-right">
              <div class="d-flex flex-column align-items-center text-center p-3 py-5">
                <img class="rounded-circle mt-5" width="150px" src="https://st3.depositphotos.com/15648834/17930/v/600/depositphotos_179308454-stock-illustration-unknown-person-silhouette-glasses-profile.jpg">
                <span class="font-weight-bold">Edogaru</span>
                <span class="text-black-50">edogaru@mail.com.my</span>
                <span> </span>
              </div>
            </div>
            <div class="col-md-9"> <!-- Removed the third column (col-md-9 instead of col-md-12) -->
              <div class="p-3 py-5">
                <div class="d-flex justify-content-between align-items-center mb-3">
                  <h4 class="text-right" style="font-family: 'Montserrat', serif; color: #888; letter-spacing: 2px">ACCOUNT</h4>
                </div>
                <div class="row mt-2">
                  <div class="col-md-6"><label class="labels">Name</label><input type="text" class="form-control" placeholder="name" value=""></div>
                </div>
                <div class="row mt-3">
                  <div class="col-md-12"><label class="labels">Address</label><input type="text" class="form-control" placeholder="enter address" value=""></div>
                  <div class="col-md-12"><label class="labels">Email ID</label><input type="text" class="form-control" placeholder="enter email" value=""></div>
                </div>
                <div class="mt-5 text-center"><button class="btn btn-primary profile-button" type="button">Save Profile</button></div>
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
  name: 'CustomerAccount',
  data() {
    return {
    };
  },
  methods: {
    async Home() {
      await this.$router.push({name: 'CustomerHome'})
    },

  }
};

</script>

<style>
.background {
  width: 100%;
  height: 100%;
  position: absolute;
  background: url('../../assets/hotelView.png') center center no-repeat;
  background-size: cover;
}

.customerAccount {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.container {
  background-color: rgba(255, 255, 255, 0.8);
  padding: 20px;
  border-radius: 10px;
  top: 50%; /* Adjust this value to move the profile box down */
}

.form-control:focus {
  box-shadow: none;
  border-color: #BA68C8
}

.profile-button {
  background: rgb(99, 39, 120);
  box-shadow: none;
  border: none
}

.profile-button:hover {
  background: #682773
}

.profile-button:focus {
  background: #682773;
  box-shadow: none
}

.profile-button:active {
  background: #682773;
  box-shadow: none
}

.labels {
  font-size: 11px
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

.profile-box {
  position: absolute;
  top: 15%;
  right: 20%;
  left: 20%;
}
/*.background {*/
/*  width: 100%;*/
/*  height: 100%;*/
/*  position: absolute;*/
/*  background: url('../../assets/hotelView.png') center center no-repeat;*/
/*  background-size: cover;*/
/*}*/

/*.customerAccount {*/
/*  display: flex;*/
/*  justify-content: center;*/
/*  align-items: center;*/
/*  height: 100vh;*/
/*}*/

/*.container {*/
/*  background-color: rgba(255, 255, 255, 0.8);*/
/*  padding: 20px;*/
/*  border-radius: 10px;*/
/*  top: 25%;*/
/*}*/

/*.form-control:focus {*/
/*  box-shadow: none;*/
/*  border-color: #BA68C8*/
/*}*/

/*.profile-button {*/
/*  background: rgb(99, 39, 120);*/
/*  box-shadow: none;*/
/*  border: none*/
/*}*/

/*.profile-button:hover {*/
/*  background: #682773*/
/*}*/

/*.profile-button:focus {*/
/*  background: #682773;*/
/*  box-shadow: none*/
/*}*/

/*.profile-button:active {*/
/*  background: #682773;*/
/*  box-shadow: none*/
/*}*/

/*.labels {*/
/*  font-size: 11px*/
/*}*/


/*.transparent-background {*/
/*  background-color: rgba(255, 255, 255, 0.2); !* You can replace this color code with your desired dark color *!*/
/*}*/

/*.navbar-container {*/
/*  position: absolute;*/
/*  top: 0;*/
/*  left: 0;*/
/*  right: 0;*/
/*}*/


</style>
