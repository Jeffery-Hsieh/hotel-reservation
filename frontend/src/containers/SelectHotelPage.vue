<template>
    <div class="container" style="display: flex;  justifyContent: center; height: 280%">
        <div class="card hoverCard" style="minWidth: 60%; minHeight: 100%;">
            <div class="card-header"><h1 style="color: #B99363;">Hotel</h1></div>
            <div class="card-body">
                <div v-for="h_info in hotel_infos.slice((page-1)*15, page*15)" :key="h_info.id">
                    <Hotel :info="h_info" @searchRoom="show_detail(h_info)" />
                </div>
                
                <!-- pagination -->
                <ul class="pagination">
                    <li class="page-item"><a class="page-link" href="#" @click="backward"> Previos Page </a></li>
                    <li class="page-item" v-for="i in Math.ceil(hotel_infos.length/15)" :key="i">
                        <a
                        href="#"
                        class="page-link"
                        @click="turn_page(i)"
                        v-if="pages_count(page,i,Math.ceil(hotel_infos.length/15))"
                    > {{i}} </a>
                    </li>
                    <li class="page-item"><a class="page-link" href="#" @click="forward"> Next Page </a></li>
                </ul>
            </div>
        </div>

        <div class="card hoverCard" style="minWidth: 30%; minHeight: 50%;">
            <div class="card-header"><h1 style="color: #B99363;">Order</h1></div>
            <div v-if="selectHotel">
                <Order 
                    :show_info="selectHotel" 
                    :info="info"
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
import {RotateSquare2} from 'vue-loading-spinner'
    export default {
        data () {
            return {
                page: 1,
                hotel_infos: this.$route.params.data.hotel_infos,
                info: this.$route.params.data,
                selectHotel: null,
                comments: '',
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
            async show_detail(info){
                this.isLoading = true
                this.selectHotel = null

                axios.get(`http://localhost:8080/hotel_reservation/hotels/${info.id}?check_in_date=${this.info.check_in_date}&check_out_date=${this.info.check_out_date}`)
                .then((res) => {
                    this.singleRoomAvailNum = res.data.singleRoomNum;
                    this.doubleRoomAvailNum = res.data.doubleRoomNum;
                    this.quadRoomAvailNum = res.data.quadRoomNum;
                    this.selectHotel = info;
                    this.isLoading = false;
                })

                // axios_get_res('http://localhost:8080/hotel_reservation/hotels/'+this.show_info.hotelID+'/comments', { params:{
                //     data: true
                // }}).then(data => {this.comments = data;
                //     this.comments.forEach(function(item, index, array){
                //         var s = ''
                //         for(var i = 0; i < parseInt(item.starRate); i++) { s += '讚'}
                //         for(var i = 0; i < 5-parseInt(item.starRate); i++) { s += '幹'}
                //         item.starRate = s
                //     });
                // })
                
            },
            confirmBooking(order){
                if(order.userName=='') {
                    alert('請輸入姓名！！')
                    return null
                }

                axios.post('http://localhost:8080/hotel_reservation/hotels/'+this.selectHotel.id, 
                {
                    "customerName": order.userName,
                    "hotelID": this.selectHotel.id,
                    "check_in_date": this.info.check_in_date,
                    "check_out_date": this.info.check_out_date,
                    "singleRoomNum": parseInt(order.requestSingleRoomNum),
                    "doubleRoomNum": parseInt(order.requestDoubleRoomNum),
                    "quadRoomNum": parseInt(order.requestQuadRoomNum)
                })
                .then((data) => {
                    if(data.totalPrice == 0) {
                        alert("Hotel is not available")
                        return
                    }
                    
                    // totalPrice = data.totalPrice;
                    // orderID = data.orderID;
                    // isPaid = data.isPaid
                    // if(isPaid){
                    //     isPaid = '已付款';
                    // }else{
                    //     isPaid = '未付款';
                    // }
                    // var other = {
                    //     address: this.selectHotel.address, 
                    //     totalPrice: totalPrice, 
                    //     orderID: orderID, 
                    //     isPaid: isPaid, 
                    //     hotelID: this.selectHotel.id
                    // }
                    // this.$router.push({name: 'pay', params:{data: order, other: other}})
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
