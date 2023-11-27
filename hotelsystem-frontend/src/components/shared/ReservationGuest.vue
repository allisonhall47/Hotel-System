<template>
  <div class="reservation">
    <div class="background">
      <div class="reservation-container">
        <div class="reservation-box">
            <div class="card custom-card-width justify-content-center">
              <div class="card-body">
                <div class="card-header">
                  <h3 class="text-center" style="font-family: 'Montserrat', sans-serif; color: #888; letter-spacing: 2px">ROOM RESERVATION</h3>
                </div>

                <!-- Number of Guests -->
                <div class="form-group custom-form-group">
                  <label class="luxurious-text" for="guests">Number of Guests:</label>
                  <input type="number" class="form-control" id="guests" v-model="guests">
                </div>

                <div class="form-group custom-form-group">
                  <label class="luxurious-text" for="guests">Number of Rooms:</label>
                  <input type="number" class="form-control" id="rooms" v-model="rooms">
                </div>

                <!-- Start Date -->
                <div class="form-group custom-form-group">
                  <label class="luxurious-text" for="startDate">Start Date:</label>
                  <input type="date" class="form-control" id="startDate" v-model="startDate">
                </div>

                <!-- End Date -->
                <div class="form-group custom-form-group">
                  <label class="luxurious-text" for="endDate">End Date:</label>
                  <input type="date" class="form-control" id="endDate" v-model="endDate">
                </div>
                <div style="margin-bottom: 20px;"></div>
                <div class="text-center">
                  <!-- Reservation Button -->
                  <button class="btn btn-primary custom-reservation-button" @click="SuggestRooms">View Rooms</button>
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
</template>

<script>
export default {
  name: "ReservationGuest",
  data() {
    return {
      startDate: '',
      endDate: '',
      guests: 0,
      new_guests: 0,
      rooms: 0,
    }
  },
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
    async SuggestRooms() {
      this.new_guests = this.guests;
      if (parseInt(this.guests) <= 0) {
        // Display an error message or handle the case where guests are less than or equal to 0
        alert('Number of guests should be greater than 0.');
        return;
      }

      if (parseInt(this.guests) <= 0){
        alert('Number of rooms should be greater than 0.');
        return;
      }

      const today = new Date();
      const selectedStartDate = new Date(this.startDate);
      const selectedEndDate = new Date(this.endDate);

      if (this.guests === 0 || this.startDate == null || this.endDate == null || this.rooms === 0){
        alert('Please enter all fields')
        return;
      }

      if (selectedStartDate < today) {
        alert('Start date should be after today.');
        return;
      }

      if (selectedEndDate <= selectedStartDate) {
        alert('End date should be after the start date.');
        return;
      }

      if (this.guests%2 === 1) {
        if (parseInt(this.rooms, 10)*4< parseInt(this.guests, 10)+1) {
          alert('Stop it G');
          return;
        }
        if (parseInt(this.rooms, 10)*2> parseInt(this.guests, 10)+1) {
          this.guests = parseInt(this.rooms,10)*2;
        }
      }
      if (parseInt(this.guests,10)%2 === 0) {
        if ((parseInt(this.rooms,10)*4)<parseInt(this.guests,10)) {
          alert('Stop it G');
          return;
        }
        if (parseInt(this.rooms,10)*2>parseInt(this.guests,10)) {
          this.guests = parseInt(this.rooms,10)*2;
        }
      }

      await this.$router.push({path: '/SuggestRooms/' + this.startDate + '/' +  this.endDate + '/' +this.guests + '/' +this.rooms + '/' + this.new_guests})
    }
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

.reservation {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.reservation-box {
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

.reservation-container {
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
  color: #888;
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


.custom-card-width {
  width: 100%;
  max-width: 800px !important;
  margin: 0 auto;
  top: 30%;
  border: none !important;
}

.custom-form-group {
  width: 100%;
  margin-bottom: 15px;
}

.custom-reservation-button {
  background-color: transparent;
  color: #888;
  border: 2px solid #888;
  transition: background-color 0.3s, color 0.3s;
}

.custom-reservation-button:hover {
  background-color: #888;
  color: white;
}

.card-header {
  background: white;
  border-bottom: white;
}

#startDate::placeholder {
  color:#888;
}

#endDate::placeholder {
  color:#888;
}

.clickable-text:hover {
  cursor: pointer;
  color: white !important;
}

</style>
