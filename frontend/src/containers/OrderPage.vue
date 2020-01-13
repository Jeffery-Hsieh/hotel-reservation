<template>
    <div class="container" style="display: flex; alignItems: center; justifyContent: center; height: 100%">
        <div class="card hoverCard" style="minWidth: 50%; minHeight: 100%;">
            <div class="card-header"><h1 style="color: #B99363;">Order Detail</h1></div>
            <div class="card-body">
                <Order 
                    :orderInfo="order"
                    :canModify="canModify"
                    :customerName="customerName"
                    :singleRoomAvailNum="order.singleRoomNum"
                    :doubleRoomAvailNum="order.doubleRoomNum"
                    :quadRoomAvailNum="order.quadRoomNum"
                    @confirmBooking="modifyOrConfirmOrder"
                />
                <button type="button" class="btn btn-danger" @click="deleteOrder">Delete</button>
            </div>
        </div>
    </div>
</template>
<script>
import Order from "../components/Order"
import axios from "axios"
import { dateToString } from "../util/dateFormat"

    export default {
        data(){
            return{
                order: this.$route.params,
                customerName: this.$route.params.customerName,
                canModify: false,
                card_info: '',
                email: '',
            }
        },
        components: {
            Order
        },
        methods: {
            modifyOrConfirmOrder(order) {
                if(!this.canModify) {
                    this.canModify = !this.canModify
                    return
                }
                else{
                    this.createNewOrder(order)
                }
            },
            async createNewOrder(order) {                       
                if(new Date(order.check_out_date) - new Date(order.check_in_date) < 0) {
                    alert("離房時間不可小於入住時間")
                    return
                }

                if(order.requestSingleRoomNum + order.requestDoubleRoomNum + order.requestQuadRoomNum < 1){
                    alert('請至少選一間房間')
                    return
                }

                axios.post('http://localhost:8080/hotel_reservation/orders/'+this.order.orderID, {
                    check_in_date: dateToString(this.order.check_in_date),
                    check_out_date:  dateToString(this.order.check_out_date),
                    singleRoomNum: order.requestSingleRoomNum,
                    doubleRoomNum: order.requestDoubleRoomNum,
                    quadRoomNum: order.requestQuadRoomNum
                })
                .then((res) => {
                    this.order.totalPrice = res.data.totalPrice;
                    this.order.orderID = res.data.orderID;
                    this.canModify = false;
                })
            },
            deleteOrder(){
                axios.delete('http://localhost:8080/hotel_reservation/orders/'+this.order.orderID)
                .then(() => {
                    this.$router.push('/')
                })
                
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

                axios.put('http://localhost:8080/hotel_reservation/orders/'+this.order.orderID+'/pay',  {
                    isPaid: 1,
                    email: this.email,
                    card_info: this.card_info
                }).then(() => { this.reservation_info.isPaid = '已付款'})
                
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

        }
    }
</script>
