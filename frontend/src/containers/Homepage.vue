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
import { parse } from 'querystring';
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
        //search

        var y1 = this.check_in_date.toString().split("-")[0]
        var y2 = this.check_out_date.toString().split("-")[0]
        var m1 = this.check_in_date.toString().split("-")[1]
        var m2 = this.check_out_date.toString().split("-")[1]
        var d1 = this.check_in_date.toString().split("-")[2]
        var d2 = this.check_out_date.toString().split("-")[2]

        this.check_in_date = y1 + m1 + d1
        this.check_out_date = y2 + m2 + d2

        if(this.region==''){
          alert('請輸入想訂房地區！！！')
          return null
        }

        // if(this.single+this.double+this.quad < 1){
        //   alert('請輸入房型數量需求！！！')
        //   return null
        // }

        if(y2 == undefined || y1 == undefined){
          alert('請輸入日期！！！')
          return null
        }

        if(parseInt(y1) > parseInt(y2) || parseInt(m1) > parseInt(m2) || parseInt(d1) >= parseInt(d2)){
          alert('日期輸入錯誤！！！')
          return null
        }
        //comments=[{content:'可以',hotelStar:3, name: 'bohwea'},{content:'我覺得不行',hotelStar: 3, name: 'bohwea and me'}]
        //comments=[{content:'我覺得不行',hotelStar: 3, name: 'bohwea and me'},{content:'可以',hotelStar:3, name: 'bohwea'}]
        //自定義
        var hotel_infos = ''
        // [
        //   {hotelID: 'sex till death', address: 'bohwea road intersection', hotelStar: 4},
        //   {hotelID: 'never stop', address: 'bohwea road you know', hotelStar: 3}
        // ]


        // var cid = this.check_in_date.split('-')[0] + one2two(this.check_in_date.split('-')[1]) + this.check_in_date.split('-')[2]
        // var cod = this.check_out_date.split('-')[0] + one2two(this.check_out_date.split('-')[1]) + this.check_out_date.split('-')[2]
        axios_get_res('http://localhost:8080/hotel_reservation/hotels', {
          region: this.region,
          check_in_date: this.check_in_date,
          check_out_date: this.check_out_date
        }).then(data => {
          hotel_infos = data;
          hotel_infos.forEach(function(item, index, array){
            var s = ''
            for(var i = 0; i < parseInt(item.hotelStar); i++) { s += '★ '}
            item.hotelStar = s          
          })
          
          this.$router.push({name: 'hotel', params: {
            data: {
              region: this.region,
              check_in_date: this.check_in_date,
              check_out_date: this.check_out_date,
              hotel_infos: hotel_infos
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
        // {
        //   customerName: 'W.Han',
        //   hotelID: 'sex on the beach',
        //   check_in_date: '2020-1-18',
        //   check_out_date: '2020-1-20',
        //   singleRoomNum: 2,
        //   doubleRoomNum: 2,
        //   quadRoomNum: 2,
        //   isPaid: 0,
        //   address: 'to heaven'
        // };''

        axios_get_res('http://localhost:8080/hotel_reservation/orders/'+this.order_id, {
            orderID: this.order_id
          })
          .then(data => {
            room_order = data;

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
  function axios_get_res(url, para) {
    return axios.get(url, {params: para})
    .then((res) => { return res.data; })
    .catch((error) => { console.log(error)})
    .finally(() => {return null; })
  };
  function one2two(input){
    if(parseInt(input)< 10){
      input = '0' + input
    }
    return input
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