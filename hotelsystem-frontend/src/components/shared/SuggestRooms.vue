<template>
  <div>
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
      <div class="room-container">
        <h1 class="text-left" style="font-family: 'Montserrat', sans-serif; color: #888; letter-spacing: 3px; font-size: 30px" >CHOOSE YOUR ROOMS</h1>
        <div style="margin-top: 15px;"></div>

        <!-- Iterate over the list of room combinations -->
        <div v-for="(combination, index) in roomCombinations" :key="index" class="room-combination-box">
          <!-- Iterate over the keys in each combination object -->
          <div v-for="(count, roomType, roomIndex) in combination" :key="roomType" class="room-info">
            <!-- Add Bootstrap classes for styling -->
            <div class="row">
              <div class="col-md-3">
                <!-- Add an image for each room type -->
                <img :src="getRoomImage(roomType)" alt="Room Image" class="img-fluid room-image" />
              </div>
              <div class="col-md-9">
                <!-- Display room information and count -->
                <h5 style="font-family: 'Montserrat', sans-serif; color: #888; letter-spacing: 2px" >{{ count }} {{ roomType }} room(s)</h5>
                <p style="font-family: 'Georgia', sans-serif">{{ getRoomDescription(roomType) }}</p>
                <p style="font-family: 'Georgia', sans-serif">Price: {{ getTotalPrice(roomType, count) }}</p>
                <p v-if="roomIndex === Object.keys(combination).length - 1" style="font-family: 'Georgia', sans-serif">Total Price: {{ getTotalCombinationPrice(combination) }}</p>
                <button v-if="roomIndex === Object.keys(combination).length - 1" class="btn btn-lg custom-book-button" @click="book(combination)">Book</button>
              </div>
            </div>
          </div>
        </div>
    </div>
  </div>
</template>

<script>
import regularRoomImage from "../../assets/regularRoom.png";
import deluxeRoomImage from "../../assets/deluxeRoom.png";
import luxuryRoomImage from "../../assets/luxuryRoom.png";
import suiteImage from "../../assets/suiteRoom.png";

import axios from 'axios'
var config = require('../../../config')
var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
var axiosClient = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: "SuggestRooms",
  data(){
    return {
      startDate: '',
      endDate: '',
      guests: 0,
      newGuests: 0,
      rooms: 0,
      regularRoom: ['Regular', 899, 'Queen', 2],
      deluxeRoom: ['Deluxe', 1299, 'Queen', 4],
      luxuryRoom: ['Luxury', 1499, 'King', 2],
      suite: ['Suite', 1999, 'King', 4],
      roomCombinations: []
    }
  },
  mounted(){
    this.startDate = this.$route.params.param1;
    this.endDate = this.$route.params.param2;
    this.guests = this.$route.params.param3;
    this.rooms = this.$route.params.param4;
    this.newGuests = this.$route.params.param5;
    this.roomCombinations = this.findPossibleRoomCombinations(this.guests, this.rooms);
    console.log(this.roomCombinations);
  },
  methods: {
    findPossibleRoomCombinations(guests, totalRooms) {
      guests = parseInt(guests, 10);
      // Step 1: Find basic room combinations
      let basicCombos = [];
      if (guests % 2 === 1) {
        guests = guests + 1; // Adjusting odd number of guests to even
      }
      for (let i = 0; i <= guests / 4; i++) {
        const remainingGuests = guests - i * 4;
        if (remainingGuests % 2 === 0) {
          basicCombos.push({
            roomsOf4: i,
            roomsOf2: remainingGuests / 2
          });
        }
      }

      // Step 2: Generate advanced room combinations
      const advancedCombos = new Set();
      const serializeCombo = (combo) => {
        return Object.keys(combo).sort().map(key => `${key}:${combo[key]}`).join(',');
      };
      const addCombination = (roomsOf4, roomsOf2, combo) => {

        if (roomsOf4 === 0 && roomsOf2 === 0) {
          advancedCombos.add(serializeCombo(combo));
          return;
        }
        if (roomsOf4 > 0) {
          addCombination(roomsOf4 - 1, roomsOf2, { ...combo, Suite: (combo.Suite || 0) + 1 });
          addCombination(roomsOf4 - 1, roomsOf2, { ...combo, Deluxe: (combo.Deluxe || 0) + 1 });
        }
        if (roomsOf2 > 0) {
          addCombination(roomsOf4, roomsOf2 - 1, { ...combo, Regular: (combo.Regular || 0) + 1 });
          addCombination(roomsOf4, roomsOf2 - 1, { ...combo, Luxury: (combo.Luxury || 0) + 1 });
        }
      };
      basicCombos.forEach(({ roomsOf4, roomsOf2 }) => {
        addCombination(roomsOf4, roomsOf2, {});
      });

      console.log("Advanced Combos:", Array.from(advancedCombos)); // Debugging line

      // Deserialize advanced combinations
      let combinedCombos = Array.from(advancedCombos).map(comboStr => {
        const combo = {};
        comboStr.split(',').forEach(pair => {
          const [key, value] = pair.split(':');
          combo[key] = parseInt(value, 10);
        });
        return combo;
      });

      // Step 3: Filter combinations by total rooms
      const result = []; // Initialize an empty array to store the matching objects

      for (let i = 0; i < combinedCombos.length; i++) {
        const roomObj = combinedCombos[i];
        let sum = 0;
        if (roomObj.hasOwnProperty("Regular")){
          sum = sum+roomObj.Regular;
        }
        if (roomObj.hasOwnProperty("Luxury")){
          sum = sum+roomObj.Luxury;
        }
        if (roomObj.hasOwnProperty("Suite")){
          sum = sum+roomObj.Suite;
        }
        if (roomObj.hasOwnProperty("Deluxe")){
          sum = sum+roomObj.Deluxe;
        }
        if (sum === parseInt(totalRooms, 10)) {
          result.push(roomObj); // Add the object to the result array if it matches the target sum
        }
      }
      return(result);
    },
    getRoomImage(roomType) {
      switch (roomType) {
        case "Regular":
          return regularRoomImage;
        case "Deluxe":
          return deluxeRoomImage;
        case "Luxury":
          return luxuryRoomImage;
        case "Suite":
          return suiteImage;
        default:
          return ""; // Add a default image or handle it as needed
      }
    },
    getRoomDescription(roomType) {
      switch (roomType) {
        case "Regular":
          return "Indulge in simplicity and comfort in our Regular Room with one queen bed, offering a peaceful retreat for a restful night's sleep. Thoughtfully designed with modern amenities, this cozy space is perfect for solo travelers or couples seeking a comfortable and convenient stay.";
        case "Deluxe":
          return "Experience elevated comfort in our Deluxe Room featuring two queen beds, providing ample space for a luxurious and rejuvenating stay. With a perfect blend of style and convenience, this room is ideal for families or friends traveling together, offering a relaxing haven after a day of exploration.";
        case "Luxury":
          return "Indulge in opulence in our Luxury Room, where sophistication meets comfort. With a lavish king-sized bed, a cozy ensuite living room, and a private balcony offering scenic views, this refined retreat provides an exquisite experience for those seeking the pinnacle of luxury and relaxation.";
        case "Suite":
          return "Step into unparalleled luxury in our expansive Suite with two bedrooms. This exclusive retreat offers a spacious living room, a fully equipped kitchen, and a private balcony adorned with a hot tub, providing an indulgent escape for those who crave the ultimate in comfort and style.";
        default:
          return ""; // Add a default description or handle it as needed
      }
    },
    getTotalPrice(roomType, count) {
      switch (roomType) {
        case "Regular":
          return count * this.regularRoom[1];
        case "Deluxe":
          return count * this.deluxeRoom[1];
        case "Luxury":
          return count * this.luxuryRoom[1];
        case "Suite":
          return count * this.suite[1];
        default:
          return 0; // Handle other cases as needed
      }
    },
    async Login() {
      await this.$router.push({path: '/Login/'})
    },
    async SignUp() {
      await this.$router.push({path: '/SignUp/'})
    },
    async Home() {
      await this.$router.push({name: 'Home'})
    },
    getTotalCombinationPrice(combination) {
      let totalPrice = 0;

      for (const roomType in combination) {
        if (combination.hasOwnProperty(roomType)) {
          totalPrice += this.getTotalPrice(roomType, combination[roomType]);
        }
      }

      return totalPrice;
    },
    async book(combination) {
      const combinationString = JSON.stringify(combination);
      const encodedCombination = encodeURIComponent(combinationString);
      if(combination.hasOwnProperty("Regular")) {
        axiosClient.get("/specificRoom/available/type/Regular/" + this.startDate + '/' + this.endDate)
          .then((response) => {
            if(response.data.length < combination.Regular){
              alert('Rooms unavailble')
              return;
            }
          })
          .catch((err) => {
            this.errorMsg = `Failure: ${err.response.data}`
            alert(this.errorMsg)
            return;
          })
      }
      if(combination.hasOwnProperty("Luxury")) {
        axiosClient.get("/specificRoom/available/type/Luxury/"+ this.startDate + '/' + this.endDate)
          .then((response) => {
            if(response.data.length < combination.Luxury){
              alert('Rooms unavailble')
              return;
            }
          })
          .catch((err) => {
            this.errorMsg = `Failure: ${err.response.data}`
            alert(this.errorMsg)
            return;
          })
      }
      if(combination.hasOwnProperty("Suite")) {
        axiosClient.get("/specificRoom/available/type/Suite/"+ this.startDate + '/' + this.endDate)
          .then((response) => {
            if(response.data.length < combination.Suite){
              alert('Rooms unavailble')
              return;
            }
          })
          .catch((err) => {
            this.errorMsg = `Failure: ${err.response.data}`
            alert(this.errorMsg)
            return;
          })
      }
      if(combination.hasOwnProperty("Deluxe")) {
        axiosClient.get("/specificRoom/available/type/Deluxe"+ this.startDate + '/' + this.endDate)
          .then((response) => {
            if(response.data.length < combination.Deluxe){
              alert('Rooms unavailble')
              return;
            }
          })
          .catch((err) => {
            this.errorMsg = `Failure: ${err.response.data}`
            alert(this.errorMsg)
            return;
          })
      }
      await this.$router.push({path: '/CreateCustomerPage/' + this.startDate + '/' +  this.endDate + '/' + encodedCombination + '/' + this.newGuests});
    }
  }
}
</script>


<style scoped>
.navbar-brand {
  margin-right: 0; /* Reset the margin for the navbar-brand */
}

.transparent-background {
  background-color: rgba(136, 136, 136, 0.3);
}

.navbar-container {
  z-index: 1;
}

.room-combination-box {
  border: 1px solid #ddd;
  padding: 10px;
  margin-bottom: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.room-container {
  margin: 20px 40px;
  padding: 20px;
  border-radius: 8px;
  border: none;
}

.room-info:not(:last-child) {
  margin-bottom: 20px; /* Add space between room combinations except for the last one */
}

.custom-book-button {
  background-color: transparent;
  color: #888;
  border: 2px solid #888;
  transition: background-color 0.3s, color 0.3s;
}

.custom-book-button:hover {
  background-color: #888;
  color: white;
}

.room-image {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s;
}

.room-image:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2); /* Adjust the shadow on hover as needed */
}

.clickable-text:hover {
  cursor: pointer;
  color: white !important;
}

</style>
