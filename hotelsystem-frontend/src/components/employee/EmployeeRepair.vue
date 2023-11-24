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
          <div class="col-md-6">
            <h2>Submit Repair Request</h2>
            <form @submit.prevent="submitRepair">
              <!-- Status Field -->
              <div class="form-group">
                <label for="status">Status:</label>
                <input type="text" class="form-control" id="status" v-model="repair.status" required>
              </div>
              <!-- Description Field -->
              <div class="form-group">
                <label for="description">Description:</label>
                <textarea class="form-control" id="description" v-model="repair.description" required></textarea>
              </div>
              <!-- Employee Field -->
              <div class="form-group">
                <label for="employee">Employee:</label>
                <input type="text" class="form-control" id="employee" v-model="repair.employee" required>
              </div>
              <!-- Submit Button -->
              <button type="submit" class="btn btn-primary">Submit</button>
            </form>
          </div>

          <!-- Repair List -->
          <div class="col-md-6">
            <h2>Repair List</h2>
            <ul class="list-group">
              <li v-for="repair in repairs" :key="repair.id" class="list-group-item">
                {{ repair.status }} - {{ repair.description }} - {{ repair.employee }}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import EmployeeAccount from "./EmployeeAccount.vue";
import axios from "axios";

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
    }
  },
  data() {
    return {
      repair: {
        status: '',
        description: '',
        employee: ''
      },
      repairs: [],
    };

  },

  created() {
    this.fetchRepairs();
  },

  methods: {
    async Home() {
      await this.$router.push({name: "EmployeeHome", params: {email: this.email}})
    },
    async Employee() {
      await this.$router.push({name: "EmployeeAccount", params: {email: this.email}})
    },
    async LogOut() {
      await this.$router.push({name: 'Home'})
    },

    async submitRepair() {
      axios.post("repair/new", this.repair)
        .then(response => {
          this.repairs.push(response.data);
          this.resetForm();
        })
        .catch(error => {
          console.error('Error submitting repair:', error);
        });
    },

    async fetchRepairs() {
      // Fetch the list of repairs from the backend
      axios.get("/repair")
        .then(response => {
          this.repairs = response.data;
        })
        .catch(error => {
          console.error('Error fetching repairs:', error);
        });
    },

    async resetForm() {
      this.repair.status = '';
      this.repair.description = '';
      this.repair.employee = '';
    },

  }
}
</script>

<style scoped>

.container {
  padding: 20px;
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
  background: white;
  padding: 300px 0;
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
