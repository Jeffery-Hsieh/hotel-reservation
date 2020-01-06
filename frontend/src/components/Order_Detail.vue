<template>
    <div class="container" style="display: flex; alignItems: center; justifyContent: center; height: 100%">
        <div class="card hoverCard" style="minWidth: 50%; minHeight: 100%;">
            <div class="card-header"><h1 style="color: #B99363;">Order Detail</h1></div>
            <div class="card-body">
                <div class="form-group">
                    Order ID: {{reservation_other.orderID}}
                    <br>
                    User Name: {{reservation_info.customerName}}
                    <br>
                    Hotel: {{reservation_info.hotelID}}
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
                    Order Status: {{reservation_info.isPaid}}
                    <br>
                    <br>
                    <div v-if="reservation_info.isPaid=='未付款'">
                        <label>Card Info: </label>
                        <input class="form-control" type="text" v-model="card_info">
                        <br>
                        <label>E-mail</label>
                        <input class="form-control" type="text" v-model="email">
                        <br>
                        <button class="btn btn-primary" @click="pay()">Pay</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="card hoverCard" style="minWidth: 5%; minHeight: 100%;">
        </div>
                
            <!-- <div v-if="reservation_info.isPaid=='已付款'">
                <h4>Add Comment</h4>
                <label>Star Rate :</label>
                <button v-if="starRate==0" v-for="i in 5" @click.prevent="rate(i)">{{i}} </button>
                <label v-if="starRate!=0">{{starRate}}</label>
                <br>
                <label>Comment:</label>
                <input v-if="isComment==false" type="text" v-model="description">
                <label v-if="isComment==true">{{description}}</label>
                <button v-if="isComment==false" @click="comment()">Comment</button>
            </div> -->
        <div class="card hoverCard" style="minWidth: 50%; minHeight: 100%;">
            <div class="card-header"><h1 style="color: #B99363;">Modify</h1></div>
            <div class="card-body">
                <div class="form-group">
                    Check in date: 
                    <input type="date" id="checkin" class="form-control" placeholder="Select Check in Date" v-model="modify_checkindate">
                    <!-- <datepicker placeholder="Select Check out Date" v-model="modify_checkindate"/> -->
                    <br>
                    Check out date: 
                    <input type="date" id="checkout" class="form-control" placeholder="Select Check out Date" v-model="modify_checkoutdate">
                    <!-- <datepicker placeholder="Select Check out Date" v-model="modify_checkoutdate"/> -->
                    <br>
                    Single Room:
                    <select class="form-control" v-model="modify_single">
                        <option v-for="n in reservation_info.singleRoomNum + 1">{{reservation_info.singleRoomNum-n+1}}</option>
                    </select>
                    <br>
                    double Room:
                    <select class="form-control" v-model="modify_double">
                        <option v-for="n in reservation_info.doubleRoomNum + 1">{{reservation_info.doubleRoomNum-n+1}}</option>
                    </select>
                    <br>
                    Quad Room:
                    <select class="form-control" v-model="modify_quad">
                        <option v-for="n in reservation_info.quadRoomNum + 1">{{reservation_info.quadRoomNum-n+1}}</option>
                    </select>
                    <br>
                    <br>
                    <button class="btn btn-primary" @click="modify()">Modify</button>
                    <br>
                    <br>
                    <br>
                    <button class="btn btn-danger" @click="del()">Delete Order</button>
                </div>
            </div>
        </div>
    </div>
</template>
<script src="https://unpkg.com/vue"></script>
<script src="https://unpkg.com/vuejs-datepicker"></script>
<script>
import Datepicker from 'vuejs-datepicker';
import { parse } from 'querystring';
var axios = require('axios')
    export default {
        data(){
            return{
                reservation_info: this.$route.params.data,
                reservation_other: this.$route.params.other,
                modify_checkindate: this.$route.params.data.check_in_date,
                modify_checkoutdate: this.$route.params.data.check_out_date,
                modify_single: this.$route.params.data.singleRoomNum,
                modify_double: this.$route.params.data.doubleRoomNum,
                modify_quad: this.$route.params.data.quadRoomNum,
                card_info: '',
                email: '',
                isComment: false,
                description: '',
                starRate: 0
            }
        },
        methods: {
            modify(){
                var month = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec']
                var y1 = this.modify_checkindate.toString().split("-")[0]
                var y2 = this.modify_checkoutdate.toString().split("-")[0]
                var m1 = this.modify_checkindate.toString().split("-")[1]
                var m2 = this.modify_checkoutdate.toString().split("-")[1]
                var d1 = this.modify_checkindate.toString().split("-")[2]
                var d2 = this.modify_checkoutdate.toString().split("-")[2]
                var origin_y1 = this.reservation_info.check_in_date.slice(0,4)
                var origin_y2 = this.reservation_info.check_out_date.slice(0,4)
                var origin_m1 = this.reservation_info.check_in_date.slice(4,6)
                var origin_m2 = this.reservation_info.check_out_date.slice(4,6)
                var origin_d1 = this.reservation_info.check_in_date.slice(6,8)
                var origin_d2 = this.reservation_info.check_out_date.slice(6,8)
               
                if(this.modify_checkindate == this.reservation_info.check_in_date || this.modify_checkoutdate == this.reservation_info.check_out_date){
                    if(this.modify_checkindate != this.reservation_info.check_in_date){
                        this.modify_checkindate = y1+m1+d1
                    }
                    if(this.modify_checkoutdate != this.reservation_info.check_out_date){
                        this.modify_checkoutdate = y2+m2+d2
                    }
                }else{
                    if(parseInt(y1) > parseInt(y2) || parseInt(m1) > parseInt(m2) || parseInt(d1) >= parseInt(d2)){
                        alert('1日期輸入錯誤！！')
                        return null
                    }

                    if(parseInt(origin_y1) > parseInt(origin_y2) || parseInt(origin_m1) > parseInt(origin_m2) || parseInt(origin_d1) >= parseInt(origin_d2)){
                        alert('2日期輸入錯誤！！')
                        return null
                    }

                    if(parseInt(origin_y1)>parseInt(y1) || parseInt(origin_m1)>parseInt(m1) || parseInt(origin_d1)>parseInt(d1) || parseInt(origin_y2)<parseInt(y2) || parseInt(origin_m2)<parseInt(m2) || parseInt(origin_d2)<parseInt(d2)){
                        alert('3日期輸入錯誤！！')
                        return null
                    }
                    this.modify_checkindate = y1+m1+d1
                    this.modify_checkoutdate = y2+m2+d2
                    
                }
                
                if(this.modify_single + this.modify_double+ this.modify_quad < 1){
                    alert('如果都沒有要房間他媽的是不會直接刪訂單嗎？智障？')
                    return null
                }

                axios_post_res('http://localhost:8080/hotel_reservation/orders/'+this.reservation_other.orderID, {
                    check_in_date: this.modify_checkindate,
                    check_out_date:  this.modify_checkoutdate,
                    singleRoomNum: this.modify_single,
                    doubleRoomNum: this.modify_double,
                    quadRoomNum: this.modify_quad
                })
                .then(data => {
                    this.reservation_info.check_in_date = data.check_in_date;
                    this.reservation_info.check_out_date = data.check_out_date;
                    this.reservation_info.singleRoomNum = data.singleRoomNum;
                    this.reservation_info.doubleRoomNum = data.doubleRoomNum;
                    this.reservation_info.quadRoomNum = data.quadRoomNum;
                    this.reservation_other.totalPrice = data.totalPrice;
                    this.reservation_other.orderID = data.orderID;
                })

            },
            del(){
                axios_del_res('http://localhost:8080/hotel_reservation/orders/'+this.reservation_other.orderID).then(data => {this.$router.push('/')})
                
            },
            pay(){
                if(this.card_info == ''){
                    alert('Please enter card number !')
                    return null
                }
                if(this.email == ''){
                    alert('Please enter email !')
                    return null
                }

                axios_put_res('http://localhost:8080/hotel_reservation/orders/'+this.reservation_other.orderID+'/pay',  {
                    isPaid: 1,
                    email: this.email,
                    card_info: this.card_info
                }).then(data => { this.reservation_info.isPaid = '已付款'})
                
            },
            // rate(i){
            //     this.starRate = i;
            // },
            // comment(){
            //     axios.post('hotel_reservation/hotels/'+ this.reservation_info.hotelID +'/comments', {params: {
            //         orderID: this.reservation_other.orderID,
            //         description: this.description,
            //         starRate: this.starRate}})
            //         .then((res) => { })
            //         .catch((error) => { })
            //         .finally(() => { });
            //     this.isComment = true
            // }

        }, 
        components: {
            Datepicker
        }
    }
    function axios_post_res(url, para) {
        return axios.post(url, para)
        .then((res) => { console.log('suc'); return res.data; })
        .catch((error) => { console.log(error)})
        .finally(() => {return null; })
    };
    function axios_put_res(url, para) {
        return axios.put(url, para)
        .then((res) => { console.log('suc'); return res.data; })
        .catch((error) => { console.log(error)})
        .finally(() => {return null; })
    };
    function axios_del_res(url, para) {
        return axios.delete(url, {params: para})
        .then((res) => { console.log('suc'); return res.data; })
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
