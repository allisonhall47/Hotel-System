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
                <a class="nav-link clickable-text" @click="ManageEmployees">Manage Employees</a>
              </li>
              <li class="nav-item active">
                <a class="nav-link" href="#">Manage Repairs<span class ="sr-only">(current)</span></a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Rooms">Manage Rooms</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Schedule">Schedule</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Account">Account</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Reservations">View Reservations</a>
              </li>
              <li class="nav-item">
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
                <!-- Employee Assign Dropdown -->
                <!-- Dropdown to select an employee -->
                <select v-model="repair.selectedEmployeeEmail">
                  <option disabled value="">Assign to:</option>
                  <option v-for="employee in employees" :value="employee.email">{{ employee.name }}</option>
                </select>
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
                  <th>Assigned: </th>
                  <th>Assign To </th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="repair in repairs" :key="repair.id">
                  <td>
                    <select v-model="repair.status" @change="updateStatus(repair.repairId, repair.status)">
                      <option disabled value="">Please select one:</option>
                      <option v-for="status in statuses" :value="status">{{ status }}</option>
                    </select>
                  </td>
                  <td>{{ repair.description }}</td>
                  <td>{{ repair.employeeName || 'Loading...' }}</td> <!-- Assuming the employee's email is the identifier -->
                  <td>
                    <select v-model="repair.employee" @change="assignEmployee(repair.repairId, repair.employee)">
                      <option disabled value="">Please select one:</option>
                      <option v-for="employee in employees" :value="employee.email">{{ employee.name }}</option>
                    </select>
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
  name: "OwnerRepair",

  data() {
    return {
      repair: {
        status: '',
        description: '',
        employee: '',
        email: '',
        name: '',
        selectedEmployeeEmail: '',
        repairId: 0,
      },
      email: '',
      repairs: [],
      employees: [],
      statuses: ['Done','InProgress','Pending'],
      isLoggedIn: false,
    };
  },

  mounted() {
    this.email = this.$route.params.param1;
  },

  created() {
    this.fetchRepairs();
    this.fetchEmployees();
  },
  methods: {
    async Home(){
      // await this.$router.push({name: 'OwnerHome', params: {email: this.email}})
      await this.$router.push({path: '/OwnerHome/' + this.email})
    },
    async ManageEmployees(){
      // await this.$router.push({name: 'OwnerManageEmployees', params: {email: this.email}})
      await this.$router.push({path: '/OwnerManageEmployees/' + this.email})
    },
    async Account(){
      // await this.$router.push({name: 'OwnerAccount', params: {email: this.email}})
      await this.$router.push({path: '/OwnerAccount/' + this.email})
    },
    async LogOut(){
      await this.$router.push({name: "Home"})
    },
    async Rooms(){
      await this.$router.push({path: '/owner/manage_rooms/' + this.email})
    },
    async Schedule(){
      await this.$router.push({path: '/owner-view-schedule/' + this.email})
    },
    async Reservations(){
      await this.$router.push({path: '/OwnerReservation/' + this.email + '/' + this.name})
    },



    async fetchEmployees() {
      axiosClient.get('/employees/') // modify this endpoint to your actual API endpoint for fetching employees
        .then(response => {
          this.employees = response.data;
          this.employees = response.data.map(employee => {
            return {
              ... employee,
              employeeEmail: employee.email,
            }
          })
        })
        .catch(error => {
          console.error('Error fetching employees:', error);
        });
    },
    async assignEmployee(repairId, employeeEmail) {
      if (!repairId) {
        console.error('Repair ID is missing');
        return;
      }
      if (!employeeEmail) {
        console.error('Employee Email is missing');
        return;
      }

      const config = {
        headers: {
          'Content-Type': 'text/plain'
        }
      }
      // Call API to assign employee to repair
      axiosClient.post('repair/employee/' + repairId, employeeEmail,config)
        .then(() => {
          alert('Employee assigned successfully');
          // Optional: Refresh the list of repairs or update the UI accordingly
          this.repair.selectedEmployeeEmail = ''; // resets the box
          // refresh here so the new employee for that shift pops up (once i've adjusted links)
          window.location.reload();
        })
        .catch(error => {
          console.error('Error assigning employee:', error.response.data);
        });
    },
    async fetchRepairs() {
      axiosClient.get("/repair")
        .then(response => {
          this.repairs = response.data.map(repair => {
            return {
              ...repair,
              employeeName: repair.employee.name, // Access the nested name property
              repairId: repair.repairId,
            };
          });
        })
        .catch(error => {
          console.error('Error fetching repairs:', error);
        });
    },

    // async updateStatus(repairId, newStatus) {
    //   // Call API to update the status of the repair
    //   axiosClient.post('/repair/status/', + repairId, newStatus)
    //     .then(() => {
    //       alert('Status updated successfully');
    //       // Optional: Refresh the list of repairs or update the UI accordingly
    //     })
    //     .catch(error => {
    //       console.error('Error updating status:', error);
    //     });
    // },
    async updateStatus(repairId, newStatus) {
      if (!this.statuses.includes(newStatus)) {
        console.error('Invalid status:', newStatus);
        return;
      }
      // Ensure the newStatus is an object that contains the enum value
      const statusPayload = newStatus;

      // Set the Content-Type header to 'application/json'
      const config = {
        headers: {
          'Content-Type': 'application/json'
        }
      };
      // Pass the statusPayload as a JSON string
      axiosClient.post('repair/status/' + repairId, JSON.stringify(statusPayload), config)
        .then(() => {
          // Optional: Refresh the list of repairs or update the UI accordingly
          window.location.reload();
        })
        .catch(error => {
          // Handle errors here
          console.log(JSON.stringify(statusPayload))
          console.error('Error updating status:', error.response ? error.response.data : error);
        });
    },
    async submitRepair() {
      if(!this.repair.selectedEmployeeEmail) {
        alert('Please select an employee.');
        return;
      }

      const repairRequest = {
        description: this.repair.description,
        employeeEmail: this.repair.selectedEmployeeEmail, // Use the selected employee's email
      };

      axiosClient.post("repair/new", repairRequest)
        .then((response) => {
          // Handle the successful submission
          this.repairs.push(response.data);
          alert('Repair request successfully submitted.');
          this.resetForm(); // Reset your form here
          window.location.reload();
        })
        .catch((error) => {
          // Handle any errors from the API call
          console.error('Error submitting repair:', error);
          alert(`Error: ${error.response.data.message}`);
        });
    },

    resetForm() {
      // Reset the repair form to its initial state
      this.repair.status = '';
      this.repair.description = '';
      this.repair.employee = '';
      this.repair.email = '';
      this.repair.name = '';
      this.repair.selectedEmployeeEmail = '';
      this.repair.repairId = 0;
    },

  }
}
</script>

<style scoped>
.container.mt-5 {
  margin-top: 0 !important; /* Adjust the value as needed */
}

.navbar-brand {
  margin-right: 0; /* Reset the margin for the navbar-brand */
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
.nav-link:hover {
  cursor: pointer;
}

.clickable-text:hover {
  cursor: pointer;
  color: white !important;
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


</style>
