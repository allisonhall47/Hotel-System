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
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Home">Home</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Employee">Account</a> <!--employee account-->
              </li>
              <li class="nav-item active">
                <a class="nav-link" href="#">Log Repair<span class ="sr-only">(current)</span></a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="ViewSchedule">View Schedule</a> <!--view employee schedule-->
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Reservations">View Reservations</a>
              </li>
              <li>
                <a class="nav-link clickable-text" @click="LogOut">Log Out</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="table-container">
        <div class="container mt-5">
          <div class="row">
            <!-- Repair Request Form -->
            <div class="col-lg-5 mb-5">
              <div class="prettyheader">
                <h3>SUBMIT REPAIR</h3>
              </div>
              <form @submit.prevent="submitRepair">

                <!-- Description Field -->
                <div class="form-group">
                  <label for="description">Description:</label>
                  <textarea class="form-control" id="description" v-model="repair.description" required></textarea>
                </div>
                <!-- Submit Button -->
                <button @click="submitRepair()" type="button" class="btn btn-primary submitbutton">Submit</button>
              </form>
            </div>

            <!-- Repair List -->
            <div class="col-lg-7">
              <div class="prettyheader">
                <h3>REPAIR LIST</h3>
              </div>
              <table class="table table-bordered">
                <thead>
                <tr>
                  <th>Status</th>
                  <th>Description</th>
                  <th>Employee: Assigned To</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="repair in repairs" :key="repair.id">
                  <td>{{ repair.status }}</td>
                  <td>{{ repair.description }}</td>
                  <td>{{ repair.employeeName || 'Loading...' }}</td> <!-- Assuming the employee's email is the identifier -->
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
import EmployeeAccount from "./EmployeeAccount.vue";
import axios from "axios";
var config = require('../../../config')
var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
var axiosClient = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})
export default {
  name: "EmployeeRepair",
  computed: {
    EmployeeAccount() {
      return EmployeeAccount
    }
  },

  data() {
    return {
      repair: {
        status: '',
        description: '',
        employee: '',
        name: '',
        email: ''
      },
      repairs: [],
    };

  },

  mounted() {
    this.email = this.$route.params.param1;
    this.name = this.$route.params.param2;
  },

  created() {
    this.fetchRepairs();
  },

  methods: {
    async Home() {
      console.log(`name: ${this.name}`)
      // await this.$router.push({name: "EmployeeHome", params: {email: this.email, name: this.name}})
      await this.$router.push({path: '/EmployeeHome/' + this.email + '/' + this.name})
    },
    async Employee() {
      // await this.$router.push({name: "EmployeeAccount", params: {email: this.email, name: this.name}})
      await this.$router.push({path: '/EmployeeAccount/' + this.email + '/' + this.name})
    },
    async LogOut() {
      await this.$router.push({name: "Home"})
    },

    async ViewSchedule() {
      await this.$router.push({path: '/EmployeeSchedule/' + this.email + '/' + this.name})
    },

    async Reservations() {
      await this.$router.push({path: '/EmployeeReservation/' + this.email + '/' + this.name})
    },

    async submitRepair() {
      this.description = document.getElementById("description").value;

      const repair_request = {
        description: this.description,
        employeeEmail: this.email,
      };

      axiosClient.post("/repair/new", repair_request)
        .then((response) => {

          description: response.data.description;
          employeeEmail: response.data.employeeEmail;
          name: response.data.name;

          this.repairs.push(response.data);
          alert('Repair request successfully submitted.')

          this.resetForm(); // If you have a method to reset the form
          window.location.reload();
        })
        .catch((err) => {
          this.errorMsg = `Failure: ${err.response.data}`;
          alert(this.errorMsg);
        });
    },

    async fetchRepairs() {
      // Fetch the list of repairs from the backend
      // axiosClient.get("/repair")
      //   .then(response => {
      //     this.repairs = response.data;
      //     this.repairs.forEach((repair, index) => {
      //       if (repair.employeeEmail) {
      //         this.fetchEmployeeByEmail(repair.employeeEmail, index);
      //       }
      //     });
      //   })
      //   .catch(error => {
      //     console.error('Error fetching repairs:', error);
      //   });
      axiosClient.get("/repair")
        .then(response => {
          this.repairs = response.data.map(repair => {
            // Add an employeeName field to each repair object
            return {
              ...repair,
              employeeName: repair.employee.name // Access the nested name property
            };
          });
        })
        .catch(error => {
          console.error('Error fetching repairs:', error);
        });
    },

    async resetForm() {
      this.repair.description = '';
    },
  }
}
</script>

<style scoped>

container {
  display: flex;
  justify-content: space-between;
}

.form-group {
  margin-bottom: 15px;
}
.navbar-brand {
  margin-right: 0;
}

.hero-section {
  background: url('../../assets/hotelLobby.jpeg') center/cover no-repeat;
  min-height: 100vh;
  position: relative;
}

.transparent-background {
  background-color: rgba(255, 255, 255, 0.6);
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

.submitbutton {
  width: 40%;
  margin-top: 5%;
  margin-right: 60%;
  background-color: white;
  border: 2px solid #888888;
  color: #888888;
}

.submitbutton:hover {
  background-color: #888888;
  border: 2px solid #888888;
  color: white;
}

.table-container {
  background-color: rgba(255, 255, 255, 1);
  padding: 2%;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: absolute;
  top: 25%;
  left: 10%;
  right: 10%;
  min-height: 500px;
}

.prettyheader {
  font-family: 'Montserrat', sans-serif;
  color: #888;
  letter-spacing: 2px;
}


</style>
