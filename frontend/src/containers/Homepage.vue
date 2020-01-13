<template>
  <div class="container" style="display: flex; alignItems: center; justifyContent: center; height: 100%">
    <div class="card hoverCard" style="minWidth: 50%; minHeight: 50%;">
      <div class="card-header"><h1 style="color: #B99363;">Searching Page</h1></div>
      <div class="card-body">
        <div class="form-group">
          <label for="region">Region</label>
          <select v-model="region" class="form-control" id="region">
            <option v-for="r in regions" :key="r.id">
              {{r}}
            </option>
          </select>
        </div>
        <div class="form-row">
          <div class="form-group col-md-6">
            <label for="checkin">Check in date : </label>
            <input type="date" id="checkin" class="form-control" placeholder="Select Check in Date" v-model="check_in_date">
            <!-- <datepicker placeholder="Select Check in Date" v-model="check_in_date" id="checkin" class="form-control" /> -->
          </div>
          <div class="form-group col-md-6">
            <label for="checkout">Check out date : </label>
            <input type="date" id="checkout" class="form-control" placeholder="Select Check out Date" v-model="check_out_date">
            <!-- <datepicker placeholder="Select Check out Date" v-model="check_out_date"/> -->
          </div>
        </div>
        <button
          class="btn btn-success"
          style="marginBottom: 30px;"
          @click.prevent="search"
        >
          Search
        </button>
        <div class="form-group">
          <label for="orderId">Please Enter order ID</label>
          <input type="text" v-model="order_id" class="form-control" id="orderId">
        </div>
        <button
          class="btn btn-primary"
          @click.prevent="check_order()"
        >
          Confirm
        </button>
      </div>
      <div class="card-footer text-muted text-center">Rainforest@2020</div>
    </div>
  </div>
</template>
<script src="https://unpkg.com/vue"></script>
<script src="https://unpkg.com/vuejs-datepicker"></script>

<script>
const axios = require('axios');
import Datepicker from 'vuejs-datepicker';
import { dateToString } from "../util/dateFormat"
  export default {

    data () {
      return {
        regions: ['台北','台中','高雄'],
        region: '',
        check_in_date: '',
        check_out_date: '',
        order_id: '',
        single: 0,
        double: 0,
        quad: 0
      }
    },
    methods: {
      search: function() {
        //search2
        if(this.region==''){
          alert('請輸入想訂房地區！！！')
          return
        }

    
        if(!this.check_in_date || !this.check_out_date) {
          alert('請輸入日期！！！')
          return
        }

        if(new Date(this.check_out_date) - new Date(this.check_in_date) < 0) {
          alert('退房時間不可大於入住時間')
          return
        }

        axios.get('http://localhost:8080/hotel_reservation/hotels', { 
          params:{
            region: this.region,
            check_in_date: dateToString(this.check_in_date),
            check_out_date: dateToString(this.check_out_date)
          }
        }).then(res => {
          res.data.forEach(function(item, index, array){
            var s = ''
            for(var i = 0; i < parseInt(item.hotelStar); i++) { s += '★ '}
            item.hotelStar = s          
          })
          
          this.$router.push({name: 'hotel', params: {
            data: {
              region: this.region,
              check_in_date: this.check_in_date,
              check_out_date: this.check_out_date,
              hotel_infos: res.data
              }
            }
          });
        });

      },
      check_order(){
        //search order
        if(this.order_id == ''){
          alert('Please enter order ID')
          return null
        }

        var room_order = ''

        axios.get('http://localhost:8080/hotel_reservation/orders/'+this.order_id)
          .then(res => {
            room_order = res.data;

            if(room_order.isPaid==1){
              room_order.isPaid = '已付款';
            }else{
              room_order.isPaid = '未付款';
            }
            if(room_order.hotelID == ''){
                alert('Can not find your order.')
                return null
            }

            var other = {totalPrice: room_order.totalPrice, orderID: this.order_id}

            this.$router.push({name: 'order', params: {data: room_order, other: other}});
          })

      }
    },
    components: {
      Datepicker
    }
  }
</script>

<style scoped>
.hoverCard {
  box-shadow: 5px 5px 30px rgba(228, 228, 228, 0.5);
  transition: .5s;

}
.hoverCard:hover {
  box-shadow: 10px 10px 50px rgba(228, 228, 228, 0.75);
  transform: translate(-1%, -1%);
}
</style>