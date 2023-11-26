<template>
  <div>
    <div id = "makeRequest">
      <div class="background">
        <div class="navbar-container">
          <nav class="navbar navbar-expand-lg navbar-light transparent-background">
            <a class="navbar-brand" href="#">
              <img src="../../assets/marwaniottNoBG.png" alt="Your Logo" height="60">
            </a>
            <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
              <ul class="navbar-nav">
                <li class="nav-item">
                  <a class="nav-link clickable-text" @click="Home">Home</a>
                </li>
                <li class="nav-item active">
                  <a class="nav-link" href="#">Reservations<span class="sr-only">(current)</span></a>
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

        <div class="reservations-container">
          <div class="luxurious-text" style="font-family: 'Montserrat', sans-serif; color: #888; letter-spacing: 3px">
            <h3>YOUR RESERVATIONS</h3>
            <div class="table-responsive subheading">
              <table class="table table-bordered">
                <thead>
                <tr>
                  <th scope="col" class="text-center subheading">Id</th>
                  <th scope="col" class="text-center subheading">Number of People</th>
                  <th scope="col" class="text-center subheading">Check In date</th>
                  <th scope="col" class="text-center subheading">Check Out date</th>
                  <th scope="col" class="text-center subheading">Total price</th>
                  <th scope="col" class="text-center subheading">Paid ?</th>
                  <th scope="col" class="text-center subheading">Status</th>
                  <th scope="col" class="text-center subheading">Pay</th>
                  <th scope="col" class="text-center subheading">Cancel</th>
                  <th scope="col" class="text-center subheading">Request</th>
                </tr>
                </thead>
                <tbody>
                  <tr v-for="r in reservations">
                    <td class="text-center" style="background: white; min-width: 90px"><input class="form-control text-center" :value="r.reservationId" readonly></td>
                    <td class="text-center" style="background: white"><input class="form-control text-center" :value="r.numPeople" readonly></td>
                    <td class="text-center" style="background: white; min-width: 150px"><input class="form-control text-center" :value="r.checkin" readonly></td>
                    <td class="text-center" style="background: white; min-width: 150px"><input class="form-control text-center" :value="r.checkOut" readonly></td>
                    <td class="text-center" style="background: white ; min-width: 90px"><input class="form-control text-center" :id="'price'.concat(r.reservationId)" :value="r.totalPrice" readonly></td>
                    <td class="text-center" style="background: white; min-width: 90px"><input class="form-control text-center" :id="'paid'.concat(r.reservationId)" :value="r.paid ? 'Yes' : 'No'" readonly></td>
                    <td class="text-center" style="background: white; min-width: 160px"><input class="form-control text-center" :value="r.checkedIn" readonly></td>
                    <td :id="'pay'.concat(r.reservationId)" class="text-center" style="background: white; min-width: 130px">
                      <input :id="'amount'.concat(r.reservationId)" class="form-control text-center" style="min-width:60px; display: inline-block; margin-bottom: 3px" placeholder="$">
                      <button type="button" @click="pay(r.reservationId)" class="btn btn-primary btn-block mb-4 payButton" style="min-width:60px; display: inline-block" >Pay</button>
                    </td>
                    <td :id="'cancel'.concat(r.reservationId)" class="text-center" style="background: white">
                      <button type="button" @click="cancel(r.reservationId)" class="btn btn-primary btn-block mb-4 cancelButton">Cancel</button>
                    </td>
                    <td :id="'cancel'.concat(r.reservationId)" class="text-center" style="background: white">
                      <button type="button" @click="newRequest(r.reservationId)" class="btn btn-primary btn-block mb-4 payButton">New Request</button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
var config = require('../../../config')
var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
var axiosClient = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})
export default {
  name: "ReservationCustomer",
  data() {
    return {
      reservations: [],
      email: '',
      name: '',
    };
  },
  mounted() {
    this.email = this.$route.params.param1
  },
  async created() {
    this.email = this.$route.params.param1

    await axiosClient.get("/customer?email="+this.email)
      .then(response => {
        this.name = response.data.name
      })
      .catch(err => {
        this.errorMsg = `Failure: ${err.response.data}`
        alert(this.errorMsg)
        //console.log(err.response.data)
      })

    await axiosClient.get("/reservation/customer/"+this.email)
      .then(response => {
        this.reservations = response.data
      })
      .catch(err => {
        this.errorMsg = `Failure: ${err.response.data}`
        alert(this.errorMsg)
        //console.log(err.response.data)
      })

    console.log(this.reservations)
  },
  methods : {
    async pay(id) {
      const money = document.getElementById('amount'.concat(id)).value
      axiosClient.put("/reservation/"+id+"?money="+money)
        .then(response => {
          document.getElementById('price'.concat(id)).value = response.data.totalPrice
          document.getElementById('amount'.concat(id)).value = ''

          if(response.data.paid === true) {
            document.getElementById('paid'.concat(id)).value = "Yes"
            alert('Successfully money deposited')
          } else {
            alert('Successfully money deposited')
          }
        })
        .catch(err => {
          this.errorMsg = `Failure: ${err.response.data}`
          alert(this.errorMsg)
          //console.log(err.response.data)
        })
    },
    async cancel(id) {
      await axiosClient.delete('/reservation/cancel/'+id)
        .then(response => {
          //remove it
          // let tmp = null
          // for(const element in this.reservations) {
          //   if(element.reservationId === id) {
          //     tmp = element
          //     break
          //   }
          // }
          //
          // if(tmp !== null) this.reservations.remove(tmp)
        })
        .catch(err => {
          this.errorMsg = `Failure: ${err.response.data}`
          alert(this.errorMsg)
          //console.log(err.response.data)
        })

      //get the new list of reservations without the one that got cancelled
      await axiosClient.get("/reservation/customer/"+this.email)
        .then(response => {
          this.reservations = response.data
        })
        .catch(err => {
          this.errorMsg = `Failure: ${err.response.data}`
          alert(this.errorMsg)
          //console.log(err.response.data)
        })
    },
    async Home(){
      await this.$router.push({path: '/CustomerHome/'+this.email})
    },
    async newRequest(reservationId){
      await this.$router.push({path: 'make_request/'+this.email+'/'+reservationId})
    },
    // async Reservation(){
    //   await this.$router.push({path: 'customer/'+this.email+'/reservation'})
    // },
    async Account() {
      await this.$router.push({path: '/CustomerAccount/' + this.email})
    },
    async LogOut() {
      alert('Successfully logged out.')
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
  background: url('../../assets/hotelView.png') center center no-repeat;
  background-size: cover;
}
.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}

.navbar-brand {
  margin-right: 0; /* Reset the margin for the navbar-brand */
}

.transparent-background {
  background-color: rgba(255, 255, 255, 0.3); /* You can replace this color code with your desired dark color */
}

.nav-link:hover {
  cursor: pointer;
}

.reservations-container {
  background-color: rgba(255, 255, 255, 0.5);
  padding: 2%;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: absolute;
  top: 25%;
  left: 3%;
  right: 3%;
  min-height: 300px;
}

.table-responsive {
  font-family: 'Georgia', sans-serif;
  font-weight: bold;
  color: black;
  width: 100%;
  max-width: 100%;
  overflow-x: auto;
  overflow-y: auto;
  display: block;
  clear: both;
  margin-bottom: 10px;
}

.payButton {
  //width: 100%;
  background-color: white;
  border: 2px solid #0055FF;
  color: #0055FF;
}

.payButton:hover {
  //width: 100%;
  background-color: #0055FF;
  border: 2px solid #0055FF;
  color: white;
}

.cancelButton {
  width: 100%;
  background-color: white;
  border: 2px solid #888888;
  color: #888888;
}

.cancelButton:hover {
//border: #888888;
  background-color: #888888;
  border: 2px solid #888888;
  color: white;
}

.payButtonDisabled {
  width: 100%;
  border: 2px solid #0055FF;
  background-color: #7c1919;
  color: white;
}

.luxurious-text {
  font-family: 'Georgia', sans-serif;
  font-weight: bold;
  color: black;
}

.subheading {
  font-family: 'Montserrat', sans-serif;
  color: #888;
}

.clickable-text:hover {
  cursor: pointer;
  color: white !important;
}

</style>
