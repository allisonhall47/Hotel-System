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
                <a class="nav-link" @click="Home">Home</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" @click="Employee">Account</a> <!--employee account-->
              </li>
              <li class="nav-item active">
                <a class="nav-link" href="#">Log Repair<span class ="sr-only">(current)</span></a>
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

      <div class="container mt-5">
        <div class="row">
          <!-- Repair Request Form -->
          <div class="col-lg-5 mb-5">
            <h2>Submit Repair Request</h2>
            <form @submit.prevent="submitRepair">

              <!-- Description Field -->
              <div class="form-group">
                <label for="description">Description:</label>
                <textarea class="form-control" id="description" v-model="repair.description" required></textarea>
              </div>
              <!-- Submit Button -->
              <button @click="submitRepair()" type="button" class="btn btn-primary">Submit</button>
            </form>
          </div>

          <!-- Repair List -->
          <div class="col-lg-7">
            <h2>Repair List</h2>
            <table class="table">
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
  props: {
    email: {
      type: String,
      required: true
    },
    name: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      repair: {
        status: '',
        description: '',
        employee: '',
        name: ''
      },
      repairs: [],
    };

  },

  created() {
    this.fetchRepairs();
  },

  methods: {
    async Home() {
      console.log(`name: ${this.name}`)
      await this.$router.push({name: "EmployeeHome", params: {email: this.email, name: this.name}})
    },
    async Employee() {
      await this.$router.push({name: "EmployeeAccount", params: {email: this.email, name: this.name}})
    },
    async LogOut() {
      await this.$router.push({name: 'Home'})
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

    // async fetchEmployeeByEmail(email, repairIndex) {
    //   // Fetch the employee details using the email as a query parameter
    //   axiosClient.get("/employee?email=" + encodeURIComponent(email))
    //     .then(response => {
    //       // Assuming response.data has a 'name' field in the EmployeeResponseDto
    //       this.$set(this.repairs[repairIndex], 'employeeName', response.data.name);
    //     })
    //     .catch(error => {
    //       console.error('Error fetching employee details:', error);
    //     });
    // },

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

.repair-list-container {
  padding-top: 20px; /* Adjust this value to move the repair list up */
  height: calc(100vh - 200px); /* Adjust the 200px to increase or decrease the space for the repair list */
  overflow-y: auto; /* Allows scrolling if the list is long */
}
.form-group {
  margin-bottom: 15px;
}
.list-group-item {
  margin-top: 5px;
}
.navbar-brand {
  margin-right: 0;
}

.hero-section {
  background: mintcream;
  padding: 190px 0;
  text-align: center;
}

.transparent-background {
  background-color: rgba(169, 169, 169, 0.2);
}

.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}

</style>
