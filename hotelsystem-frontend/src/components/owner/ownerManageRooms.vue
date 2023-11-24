<template>
  <div>
    <div id = "addShift"></div>
    <div class="background">
      <div class="navbar-container">
        <nav class="navbar navbar-expand-lg navbar-light transparent-background">
          <a class="navbar-brand" href="#">
            <img src="../../assets/marwaniottNoBG.png" alt="Your Logo" height="60">
          </a>
        </nav>
      </div>

      <div class="manage-room-container">
        <div>
          <div class="table-responsive">
            <table class="table table-bordered">
              <thead>
                <tr>
                  <th scope="col" class="text-center">Number</th>
                  <th scope="col" class="text-center">View</th>
                  <th scope="col" class="text-center">Description</th>
                  <th scope="col" class="text-center">Open For Use</th>
                  <th scope="col" class="text-center">Type</th>
                  <th scope="col" class="text-center">Update</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="r in filteredRooms">
                  <td class="text-center" style="background: white"><input class="form-control text-center" :id="'number'.concat(r.number)" :value="r.number" readonly></td>
                  <td class="text-center" style="background: white"><input class="form-control text-center" :id="'view'.concat(r.number)" :value="r.view" readonly></td>
                  <td class="text-center" style="background: white">
                    <div class="column-content">
                      <input class="form-control text-center" :id="'description'.concat(r.number)" :value="r.description" readonly>
                    </div>
                  </td>
                  <td class="text-center" style="background: white"><input class="form-control text-center" :id="'openForUse'.concat(r.number)" :value="r.openForUse ? 'Yes' : 'No'" readonly></td>
                  <td class="text-center" style="background: white"><input class="form-control text-center" :id="'roomType'.concat(r.number)" :value="r.roomType" readonly></td>
                  <td :id="'edit'.concat(r.number)" class="text-center" style="background: white">
                    <button type="button" @click="editInfo(r.number)" class="btn btn-primary btn-block mb-4 editButton">Edit</button>
                  </td>
                  <td :id="'save'.concat(r.number)" class="text-center" style="background: white" hidden>
                    <button type="button" @click="saveInfo(r.number)" class="btn btn-primary btn-block mb-4 saveButton">Save</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="filters">
            <button @click="filterTrue()" type="button" class="btn btn-primary mb-4 filterButton">
              Open For Use
            </button>
            <button @click="filterFalse()" type="button" class="btn btn-primary mb-4 filterButton">
              Not Open For Use
            </button>
            <button @click="noFilter()" type="button" class="btn btn-primary mb-4 filterButton">
              Show All
            </button>
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
  name: "ownerManageRooms",
  data() {
    return {
      specificRooms: [],
      filteredRooms : [],
      temporary_data_holder: {}, //a dictionary ??
      errorMsg: '',
    };
  },
  async created() {
    // Initializing rooms from backend
    axiosClient.get('/specificRooms')
      .then(response => {
        this.specificRooms = response.data
        this.filteredRooms = response.data
      })
      .catch(err => {
        this.errorMsg = `Failure: ${err.response.data}`
        alert(this.errorMsg)
        //console.log(err.response.data)
      })
  },
  methods: {
    saveInfo(number_clicked) {
      let n = document.getElementById('number'.concat(number_clicked)).value
      let v = document.getElementById('view'.concat(number_clicked)).value
      let d = document.getElementById('description'.concat(number_clicked)).value
      let o = document.getElementById('openForUse'.concat(number_clicked)).value
      let r = document.getElementById('roomType'.concat(number_clicked)).value
      const room_request = {number: n, view: v, description: d, openForUse: o, roomType: r};

      axiosClient.put("/specificRoom/update", room_request)
        .then((response) => {
          //update the specificRooms and filteredRooms lists
          //too complicated, can't implement
          //update the lists again, a little redundant but easier
          axiosClient.get('/specificRooms')
            .then(response => {
              this.specificRooms = response.data
              this.filteredRooms = response.data
            })
            .catch(err => {
              this.errorMsg = `Failure: ${err.response.data}`
              alert(this.errorMsg)
              //console.log(err.response.data)
            })
        })
        .catch((err) => {
          //reset input field to what it was before
          let previousData = this.temporary_data_holder[number_clicked]
          document.getElementById('number'.concat(number_clicked)).value = previousData.number
          document.getElementById('view'.concat(number_clicked)).value = previousData.view
          document.getElementById('description'.concat(number_clicked)).value = previousData.description
          document.getElementById('openForUse'.concat(number_clicked)).value = previousData.openForUse
          document.getElementById('roomType'.concat(number_clicked)).value = previousData.roomType

          this.errorMsg = `Failure: ${err.response.data}`
          alert(this.errorMsg)
        })

      //remove
      // this.temporary_data_holder.remove(number_clicked)

      document.getElementById('view'.concat(number_clicked)).setAttribute('readonly', 'true');
      document.getElementById('description'.concat(number_clicked)).setAttribute('readonly', 'true');
      document.getElementById('openForUse'.concat(number_clicked)).setAttribute('readonly', 'true');
      document.getElementById('roomType'.concat(number_clicked)).setAttribute('readonly', 'true');

      document.getElementById('edit'.concat(number_clicked)).removeAttribute('hidden');
      document.getElementById('save'.concat(number_clicked)).setAttribute('hidden', 'true');
    },
    editInfo(number_clicked) {
      let n = document.getElementById('number'.concat(number_clicked)).value
      let v = document.getElementById('view'.concat(number_clicked)).value
      let d = document.getElementById('description'.concat(number_clicked)).value
      let o = document.getElementById('openForUse'.concat(number_clicked)).value
      let r = document.getElementById('roomType'.concat(number_clicked)).value
      this.temporary_data_holder[number_clicked] = {number: n, view: v, description: d, openForUse: o, roomType: r}

      document.getElementById('view'.concat(number_clicked)).removeAttribute('readonly');
      document.getElementById('description'.concat(number_clicked)).removeAttribute('readonly');
      document.getElementById('openForUse'.concat(number_clicked)).removeAttribute('readonly');
      document.getElementById('roomType'.concat(number_clicked)).removeAttribute('readonly');

      document.getElementById('save'.concat(number_clicked)).removeAttribute('hidden');
      document.getElementById('edit'.concat(number_clicked)).setAttribute('hidden', 'true');
    },
    //some methods to filter by Open for use
    filterTrue() {
      this.filteredRooms = this.specificRooms.filter(room => room.openForUse === true);
    },
    filterFalse() {
      this.filteredRooms = this.specificRooms.filter(room => room.openForUse === false);
    },
    noFilter() {
      this.filteredRooms = this.specificRooms
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

.manage-room-container {
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

th:nth-child(3),
td:nth-child(3) {
  width: 600px;
  max-width: 600px;
  min-width: 600px;
}

th:nth-child(1),
td:nth-child(1),
th:nth-child(4),
td:nth-child(6),
th:nth-child(6),
td:nth-child(4){
  width: 90px;
  max-width: 90px;
  min-width: 90px;
}

.table-responsive {
  width: 100%;
  max-width: 100%;
  overflow-x: auto;
  overflow-y: auto;
  display: block;
  clear: both;
  margin-bottom: 10px;
}

.filters {
  width: 100%;
  display: block;
  clear: both;
}

.column-content {
  max-width: 100%;
  overflow-x: auto;
  overflow-y: auto;
}

.editButton {
  width: 100%;
  background-color: white;
  border: 2px solid #888888;
  color: #888888;
}

.saveButton {
  width: 100%;
  background-color: white;
  border: 2px solid #0055FF;
  color: #0055FF;
}

.saveButton:hover {
  width: 100%;
  background-color: #0055FF;
  border: 2px solid #0055FF;
  color: white;
}

.filterButton {
  width: 200px;
  background-color: white;
  border: 2px solid #888888;
  color: #888888;
  text-align: center;
}

.editButton:hover, .filterButton:hover{
  //border: #888888;
  background-color: #888888;
  border: 2px solid #888888;
  color: white;
}
</style>
<!--<script setup>-->
<!--</script>-->
