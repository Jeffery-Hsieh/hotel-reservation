<template>
    <div class="container" style="display: flex;  justifyContent: center; height: 280%">
        <div class="card hoverCard" style="minWidth: 60%; minHeight: 100%;">
            <div class="card-header"><h1 style="color: #B99363;">Hotel</h1></div>
            <div class="card-body">
                <div v-for="hotel in hotels.slice((page-1)*15, page*15)" :key="hotel.id">
                    <Hotel :info="hotel" @searchRoom="show_detail(hotel)" />
                </div>
                
                <!-- pagination -->
                <ul class="pagination">
                    <li class="page-item"><a class="page-link" href="#" @click="backward"> Previos Page </a></li>
                    <li class="page-item" v-for="i in Math.ceil(hotels.length/15)" :key="i">
                        <a
                        href="#"
                        class="page-link"
                        @click="turn_page(i)"
                        v-if="pages_count(page,i,Math.ceil(hotels.length/15))"
                    > {{i}} </a>
                    </li>
                    <li class="page-item"><a class="page-link" href="#" @click="forward"> Next Page </a></li>
                </ul>
            </div>
        </div>

        <div class="card hoverCard" style="minWidth: 30%; minHeight: 50%;">
            <div class="card-header">
                <h1 style="color: #B99363;">Order</h1>
            </div>
            <div v-if="orderInfo">
                <Order
                    customerName=""
                    :orderInfo="orderInfo"
                    :canModify="canModify"
                    :singleRoomAvailNum="singleRoomAvailNum"
                    :doubleRoomAvailNum="doubleRoomAvailNum"
                    :quadRoomAvailNum="quadRoomAvailNum"
                    @confirmBooking="confirmBooking"
                />
            </div>
            <div v-if="isLoading">
                <rotate-square2></rotate-square2>
            </div>
        </div>
    </div>
</template>

<script>
var axios = require('axios')
import Hotel from "../components/HotelDetail"
import Order from "../components/Order"
import { dateToString } from "../util/dateFormat"
import {RotateSquare2} from 'vue-loading-spinner'
    export default {
        data () {
            return {
                page: 1,
                hotels: this.$route.params.data.hotel_infos,
                check_in_date: this.$route.params.data.check_in_date,
                check_out_date: this.$route.params.data.check_out_date,
                orderInfo: null,
                canModify: true,
                singleRoomAvailNum: 0,
                doubleRoomAvailNum: 0,
                quadRoomAvailNum: 0,
                isLoading: false
            }
        },
        components: {
            Hotel, 
            Order,
            RotateSquare2
        },
        methods:{
            async show_detail(hotel){
                this.isLoading = true
                this.orderInfo = null

                axios.get(`http://localhost:8080/hotel_reservation/hotels/${hotel.id}`, {
                    params: {
                        "check_in_date":dateToString(this.check_in_date),
                        "check_out_date":dateToString(this.check_out_date)
                    }
                })
                .then((res) => {
                    this.singleRoomAvailNum = res.data.singleRoomNum;
                    this.doubleRoomAvailNum = res.data.doubleRoomNum;
                    this.quadRoomAvailNum = res.data.quadRoomNum;
                    this.isLoading = false;
                    this.orderInfo = {
                        "id": hotel.id,
                        "region": hotel.region,
                        "address": hotel.address,
                        "hotelStar": hotel.hotelStar,
                        "check_in_date": this.check_in_date,
                        "check_out_date": this.check_out_date
                    }
                })
            },
            async confirmBooking(order){
                if(order.userName=='') {
                    alert('請輸入姓名！！')
                    return null
                }

                axios.post(`http://localhost:8080/hotel_reservation/hotels/${this.orderInfo.id}`, 
                {
                    "customerName": order.userName,
                    "hotelID": this.orderInfo.id,
                    "check_in_date": dateToString(this.check_in_date),
                    "check_out_date": dateToString(this.check_out_date),
                    "singleRoomNum": parseInt(order.requestSingleRoomNum),
                    "doubleRoomNum": parseInt(order.requestDoubleRoomNum),
                    "quadRoomNum": parseInt(order.requestQuadRoomNum)
                })
                .then((res) => {
                    if(res.data.totalPrice == 0) {
                        alert("Hotel is not available")
                        return
                    }
                                        
                    this.$router.push({name: 'order', 
                        params:{
                            "address":this.orderInfo.address,
                            "region":this.orderInfo.region,
                            ...res.data,
                            "check_in_date": this.check_in_date,
                            "check_out_date": this.check_out_date,
                        }
                    })
                })
                
            },
            turn_page(i){
                this.page = parseInt(i);
            },
            backward(){
                this.page = this.page - 1
            },
            forward(){
                this.page = this.page + 1
            },
            pages_count(page, i, len){
                if(page < 5){
                    return Math.abs(i-page)<8-page
                }else if(page > len-5){
                    return Math.abs(i-page)<8-(len-page)-1
                }else{
                    return Math.abs(i-page)<4
                }
            }
        }
    }
</script>
