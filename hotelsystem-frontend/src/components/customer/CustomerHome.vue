
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
                <a class="nav-link clickable-text" @click="Reservations">Reservations</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Account">Account</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="LogOut">Log Out</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="main-container">
        <div class="container">
          <div class="row">
            <div class="col-md-12 mx-auto text-center">
              <h1 class="text-center" style="font-family: 'Montserrat', serif; color: #888; letter-spacing: 5px; font-size: 45px" >THE MARWANIOTT HOTEL</h1>
              <p class="luxurious-text" style="font-weight: normal; color: #888;">explore. relax. rejuvenate.</p>
              <button class="btn btn-lg mt-3 custom-login-button" @click="BookRoom">Book Your Stay</button>
            </div>
          </div>
        </div>
      </div>
      </div>

    <div id = "intro"></div>
    <div class="custom-box">
      <div class="container">
        <div class="row">
          <div class="col-md-12">
            <h2 class="text-center mb-4" style="font-family: 'Montserrat', serif; color: #888; letter-spacing: 2px">ACCOMMODATIONS</h2>
            <p class="text-center" style="font-family: 'Georgia', serif">Welcome to our enchanted mountain retreat, where nature's embrace meets timeless elegance. Nestled amidst majestic peaks and crisp alpine air, our charming hotel invites you to a world of cozy luxury and breathtaking views. Our mountain haven is not just a getaway; it's a symphony of comfort, charm, and the serenity only found in the heart of nature.</p>
          </div>
        </div>
      </div>
    </div>

    <div id = "rooms"></div>
    <div class="container mt-4">
      <div class="row">
        <!-- Column 1 -->
        <div class="col-md-3 shadow p-3 mb-5 bg-white rounded">
          <img src="../../assets/suiteRoom.png" alt="Image 1" class="img-fluid mb-3">
          <p class="text-center" style="font-family: 'Montserrat', serif; color: #888; letter-spacing: 2px; font-weight: bold">SUITE</p>
          <p class="text-center" style="font-family: 'Georgia', sans-serif">Step into unparalleled luxury in our expansive Suite with two bedrooms. This exclusive retreat offers a spacious living room, a fully equipped kitchen, and a private balcony adorned with a hot tub, providing an indulgent escape for those who crave the ultimate in comfort and style.</p>
        </div>

        <!-- Column 2 -->
        <div class="col-md-3 shadow p-3 mb-5 bg-white rounded">
          <img src="../../assets/luxuryRoom.png" alt="Image 2" class="img-fluid mb-3">
          <p class="text-center" style="font-family: 'Montserrat', serif; color: #888; letter-spacing: 2px; font-weight: bold">LUXURY ROOM</p>
          <p class="text-center" style="font-family: 'Georgia', sans-serif">Indulge in opulence in our Luxury Room, where sophistication meets comfort. With a lavish king-sized bed, a cozy ensuite living room, and a private balcony offering scenic views, this refined retreat provides an exquisite experience for those seeking the pinnacle of luxury and relaxation.</p>
        </div>

        <!-- Column 3 -->
        <div class="col-md-3 shadow p-3 mb-5 bg-white rounded">
          <img src="../../assets/deluxeRoom.png" alt="Image 3" class="img-fluid mb-3">
          <p class="text-center" style="font-family: 'Montserrat', serif; color: #888; letter-spacing: 2px; font-weight: bold">DELUXE ROOM</p>
          <p class="text-center" style="font-family: 'Georgia', sans-serif">Experience elevated comfort in our Deluxe Room featuring two queen beds, providing ample space for a luxurious and rejuvenating stay. With a perfect blend of style and convenience, this room is ideal for families or friends traveling together, offering a relaxing haven after a day of exploration.</p>

        </div>

        <!-- Column 4 -->
        <div class="col-md-3 shadow p-3 mb-5 bg-white rounded">
          <img src="../../assets/regularRoom.png" alt="Image 4" class="img-fluid mb-3">
          <p class="text-center" style="font-family: 'Montserrat', serif; color: #888; letter-spacing: 2px; font-weight: bold">REGULAR ROOM</p>
          <p class="text-center" style="font-family: 'Georgia', sans-serif">Indulge in simplicity and comfort in our Regular Room with one queen bed, offering a peaceful retreat for a restful night's sleep. Thoughtfully designed with modern amenities, this cozy space is perfect for solo travelers or couples seeking a comfortable and convenient stay.</p>
        </div>
      </div>
    </div>

    <div class="footer-container">
      <footer class="footer transparent-background">
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
  name: 'CustomerHome',
  data() {
    return {
      email: "",
    };
  },
  mounted(){
    this.email = this.$route.params.param1
  },
  methods: {
    async Reservations() {
      await this.$router.push({path: '/customer/reservation/' + this.email})
    },
    async Account() {
      await this.$router.push({path: '/CustomerAccount/' + this.email})
    },
    async LogOut() {
      alert('Successfully logged out.')
      await this.$router.push({name: 'Home'})
    },
    async BookRoom(){
      await this.$router.push({path: '/CustomerReservation/' + this.email})
    }
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

.main-container{
  background-color: transparent;
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
  background-color: rgba(255, 255, 255, 0.2);
}

.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}

.clickable-text:hover {
  cursor: pointer;
  color: white !important;
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

</style>
