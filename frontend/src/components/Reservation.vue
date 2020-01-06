<template>
    <div class="container" style="display: flex;  justifyContent: center; height: 110%">
        <div class="card hoverCard" style="minWidth: 30%; minHeight: 100%;">
            <div class="card-header"><h1 style="color: #B99363;">Order Detail</h1></div>
            <div class="card-body">
                <div>
                    OrderID: {{reservation_other.orderID}}
                    <br>
                    <br>
                    Customer Name: {{reservation_info.customerName}}
                    <br>
                    <br>
                    Hotel: {{reservation_other.hotelID}}
                    <br>
                    <br>
                    單人房數量: {{reservation_info.singleRoomNum}}
                    <br>
                    雙人房數量: {{reservation_info.doubleRoomNum}}
                    <br>
                    四人房數量: {{reservation_info.quadRoomNum}}
                    <br>
                    <br>
                    Check in date: {{reservation_info.check_in_date}}
                    <br>
                    Check out date: {{reservation_info.check_out_date}}
                    <br>
                    <br>
                    Total price: {{reservation_other.totalPrice}} NTD
                    <br>
                    Order Status: {{reservation_other.isPaid}}
                    <br>
                    <br>
                    <label>Card Info</label>
                    <input class="form-control" v-if="show_suc==false" type="text" v-model="card_info">
                    <label v-if="show_suc==true">{{card_info}}</label>
                    <br>
                    <label>E-mail</label>
                    <input class="form-control" v-if="show_suc==false" type="text" v-model="email">
                    <label v-if="show_suc==true">{{email}}</label>
                    <h2 v-if="show_suc==true">pay success</h2>
                    <br>
                    <button class="btn btn-primary" v-if="show_suc==false" @click="pay()">Confirm</button>
                </div>
            </div>
            <div class="card-footer text-muted text-center">Rainforest@2020</div>
        </div>
    </div>
</template>
<script>
var axios = require('axios')
import { parse } from 'querystring';
    export default {
        data(){
            return{
                reservation_info: this.$route.params.data,
                reservation_other: this.$route.params.other,
                email: '',
                card_info: '',
                show_suc: false
            }
        },
        methods: {
            
            pay(){
                if(this.card_info == ''){
                    alert('Please enter card number !')
                    return null
                }
                if(this.email == ''){
                    alert('Please enter email !')
                    return null
                }

                axios_put_res('http://localhost:8080/hotel_reservation/orders/'+this.reservation_other.orderID+'/pay', {
                    isPaid: 1,
                    email: this.email,
                    card_info: this.card_info
                }).then(data => { this.show_suc = true;
                this.reservation_other.isPaid = '已付款'})
                
            }
        }
    }
    function axios_post_res(url, para) {
        return axios.post(url, para)
        .then((res) => { return res.data; })
        .catch((error) => { console.log(error)})
        .finally(() => {return null; })
    };
    function axios_put_res(url, para) {
        return axios.put(url, para)
        .then((res) => { return res.data; })
        .catch((error) => { console.log(error)})
        .finally(() => {return null; })
    };
</script>
