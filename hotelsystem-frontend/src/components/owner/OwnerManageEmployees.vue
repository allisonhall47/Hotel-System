<template>
  <div class="ownerManageEmployees">
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
                 <a class="nav-link" @click="ManageEmployees">Manage Employees</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" @click="Account">Account</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" @click="LogOut">Log Out</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="hire-container">
              <div class="d-flex justify-content-center h-100">
                <div class="card">
                  <div class="card-header">
                    <h3 class="text-center" style="font-family: 'Montserrat', serif; color: #888; letter-spacing: 2px">HIRE EMPLOYEE</h3>
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
                        <input id="salary" v-model="salary" type="salary" class="form-control" style="font-family: 'Georgia', sans-serif" placeholder="salary">
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
                      <div class="form-group">
                        <button @click="createEmployee()" type="button"
                                class="btn btn-primary btn-block mb-4 hirebutton">Hire</button>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>

      <div class="viewEmployees-container">
        <div class="d-flex justify-content-center h-100">
          <div class="card">
            <div class="card-header">
              <h3 class="text-center" style="font-family: 'Montserrat', serif; color: #888; letter-spacing: 2px">MANAGE CURRENT EMPLOYEES</h3>
            </div>
            <div class="card-body">
              <div v-if="isEditing" class="edit-employee-container">
                <input v-model="editableEmployee.name" type="text" placeholder="Name">
                <input v-model="editableEmployee.salary" type="text" placeholder="Salary">
                <button @click="saveEmployee" type="button" class="btn btn-success save-button">Save</button>
              </div>
              <form>
                <div class="table-scroll">
                <table class="table table-hover">
                  <thead>
                  <tr>
                    <th scope="col">Account #</th>
                    <th scope="col">Name</th>
                    <th scope="col">Email</th>
                    <th scope="col">Salary</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="(employee, index) in employees" :key="index" @click="selectEmployee(employee.email)"
                      :class="{ 'selected-row': selectedEmployeeEmail === employee.email }">
                    <td>{{ employee.accountNumber }}</td>
                    <td>{{ employee.name }}</td>
                    <td>{{ employee.email }}</td>
                    <td>{{ employee.salary }}</td>
                  </tr>
                  </tbody>
                </table>
                </div>
                <div class="button-container">
                <button @click="editEmployee" type="button" class="btn btn-info edit-button">Edit Employee</button>
                  <button @click="fireEmployee" type="button" class="btn btn-danger fire-button">Fire</button>
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

      import axios from 'axios'
      var config = require('../../../config')
      var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
      var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
      var axiosClient = axios.create({
       baseURL: backendUrl,
       headers: { 'Access-Control-Allow-Origin': frontendUrl }
      })

      export default {
        name: 'Hire',
        data() {
          return {
            email: '',
            password: '',
            salary: '',
            name: '',
            address: '',
            user: '',
            dob: '',
            errorMsg: '',
            new_employee: [],
            new_account: [],
            accountNumber: 0,
            employees: [],
            selectedEmployeeEmail: null,

            editableEmployee: {
              email: '',
              name: '',
              salary: '',
            },
            isEditing: false,
          };
        },
        methods: {
          editEmployee() {
            if (!this.selectedEmployeeEmail) {
              alert("Please select an employee to edit");
              return;
            }
            const selectedEmployee = this.employees.find(e => e.email === this.selectedEmployeeEmail);
            if (selectedEmployee) {
              this.editableEmployee = { ...selectedEmployee };
              this.isEditing = true;
            } else {
              alert("Employee not found");
            }
          },
          saveEmployee() {
            if (!this.isEditing) {
              alert("No edits to save");
              return;
            }
            axiosClient.put("/employee/update", this.editableEmployee)
              .then(() => {
                alert("Employee updated successfully");
                this.fetchAllEmployees();
                this.isEditing = false;
              })
              .catch(error => {
                console.error("Error updating employee:", error);
                alert("Failed to update employee");
              });
          },
          selectEmployee(email) {
            this.selectedEmployeeEmail = email;
          },
          createEmployee(){
            this.email = document.getElementById("email").value;
            this.password = document.getElementById("password").value;
            this.salary = document.getElementById("salary").value;
            this.name = document.getElementById("name").value;
            this.address = document.getElementById("address").value;
            this.dob = document.getElementById("dob").value;

              const account_request = {password: this.password, address: this.address, dob: this.dob};
              axiosClient.post("/account/create", account_request)
                .then((response) => {
                  this.accountNumber = response.data.accountNumber
                  this.new_account = response

                  const employee_request = {name: this.name, email: this.email, salary: this.salary, accountNumber: this.accountNumber}
                  axiosClient.post("/employee/create",employee_request)
                    .then((response) => {
                      alert('Employee successfully hired.')
                      this.new_employee = response.data;
                      this.employees.push(this.new_employee);
                    })
                    .catch((err) => {
                      this.errorMsg = `Failure: ${err.response.data}`
                      alert(this.errorMsg)
                    })

                })
                .catch((err) => {
                  this.errorMsg = `Failure: ${err.response.data}`
                  alert(this.errorMsg)
                })

          },
          async Home(){
            await this.$router.push({name: 'OwnerHome', params: {email: this.email}})
          },
          async ManageEmployees(){
            await this.$router.push({name: 'OwnerManageEmployees', params: {email: this.email}})
          },
          async Account(){
            await this.$router.push({name: 'OwnerAccount', params: {email: this.email}})
          },
          async LogOut(){
            await this.$router.push({name: 'Home'})
          },

          fetchAllEmployees() {
            axiosClient.get("/employees")
              .then(response => {
                this.employees = response.data;
              })
              .catch(error => {
                console.error("Error fetching employees:", error);
              });
          },
          fireEmployee() {
            if (!this.selectedEmployeeEmail) {
              alert("Please select an employee to fire");
              return;
            }
            axiosClient.delete(`/employee/delete/${this.selectedEmployeeEmail}`)
              .then(() => {
                alert("Employee fired successfully");
                this.fetchAllEmployees(); // Refresh the employee list
              })
              .catch(error => {
                console.error("Error firing employee:", error);
                alert("Failed to fire employee");
              });
          },

        },
        created() {
          this.fetchAllEmployees();
        },
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

      .hire-container {
        background-color: rgba(255, 255, 255, 0.5);
        padding: 2%; /* Use a percentage for padding */
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        position: absolute;
        top: 23%; /* Use a percentage for top positioning */
        bottom: 18%; /* Use a percentage for top positioning */
        left: 52%; /* Use a percentage for left positioning */
        right: 5%; /* Use a percentage for right positioning */
        min-height: 300px; /* Set a minimum height if needed */
      }

      .viewEmployees-container {
        background-color: rgba(255, 255, 255, 0.5);
        padding: 2%; /* Use a percentage for padding */
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        position: absolute;
        top: 20%; /* Use a percentage for top positioning */
        left: 5%; /* Use a percentage for left positioning */
        right: 52%; /* Use a percentage for right positioning */
        min-height: 300px; /* Set a minimum height if needed */
      }

      .card {
        width: 100%; /* Use 100% for responsiveness */
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

      .hirebutton {
        width: 50%;
        margin-top: 5%;
        margin-left: 25%;
        background-color: white;
        border: 2px solid #888888;
        color: #888888;
      }

      .hirebutton:hover {
        border: #888888;
        background-color: #888888;
        border: 2px solid #888888;
        color: white;
      }

      .nav-link:hover {
        cursor: pointer;
      }

      .table-scroll {
          height: 400px;
          overflow-y: auto;
          overflow-x: hidden;
      }

      .selected-row {
        background-color: #f0f0f0;
      }

      .button-container {
        display: flex;
        justify-content: space-around;
        margin-top: 10px;
      }

      .fire-button, .edit-button {
        flex: 1; /* This makes each button take equal space */
        margin: 0 5px; /* Adds margin between buttons */
      }

      .save-button {
        padding: 6px 12px; /* Adjust padding to match input fields */
        height: calc(1.5em + .75rem + 2px); /* Adjust height to match input fields */
        margin: 0; /* Remove any additional margin */
        flex-shrink: 0; /* Prevent button from shrinking */
        background-color: white;
        border: 2px solid #888888;
        color: #888888;
        transition: background-color 0.3s, color 0.3s, border-color 0.3s; /* Smooth transition for hover */
      }

      .save-button:hover {
        background-color: #888888; /* Hover background color */
        color: white; /* Hover text color */
        border-color: #888888; /* Hover border color */
      }


      .edit-employee-container {
        display: flex;
        align-items: center; /* Align items vertically */
        gap: 10px; /* Spacing between elements */
      }

      .edit-employee-container input {
        flex-grow: 1; /* Input fields take available space */
        margin-bottom: 0; /* Remove any default margin */
      }


      </style>
