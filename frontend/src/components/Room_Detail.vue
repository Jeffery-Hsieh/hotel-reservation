<template >
    <div class="container" style="display: flex;  justifyContent: center; height: 280%">
        <div class="card hoverCard" style="minWidth: 60%; minHeight: 100%;">
            <div class="card-header"><h1 style="color: #B99363;">Hotel</h1></div>
            <div class="card-body">
                <div v-for="h_info in hotel_infos.slice((page-1)*15, page*15)" :key="hotel_infos.slice((page-1)*15, page*15).id">
                    <label >飯店: </label>
                    <a href="" @click.prevent="show_detail(h_info)">{{h_info.id}}</a>
                    <br>
                    <label>星等: {{h_info.hotelStar}} </label>
                    <br>
                    <label>地址 : {{h_info.address}}</label>
                    <br>
                    <br>
                </div>
                <button :disabled="page == 1" class="v-pagination__navigation "  @click="backward()"> < </button>
                <button class="v-pagination__item v-pagination__item--active primary "  v-for="i in Math.ceil(hotel_infos.length/15)" @click="turn_page(i)" v-if="pages_count(page,i,Math.ceil(hotel_infos.length/15))"> {{i}} </button>
                <button :disabled="page==Math.ceil(hotel_infos.length/15)" class="v-pagination__navigation " @click="forward()"> > </button>
            </div>
            <div class="card-footer text-muted text-center">Rainforest@2020</div>
        </div>
        <div v-if="is_show_info==true" class="card hoverCard" style="minWidth: 5%; minHeight: 100%;">
        </div>
        <div v-if="is_show_info==true" class="card hoverCard" style="minWidth: 30%; minHeight: 50%;">
            <div class="card-header"><h1 style="color: #B99363;">Order</h1></div>
            <div class="card-body">
                <div>
                    <label>飯店: {{show_info.id}}</label>
                    <br>
                    <label>星等: {{show_info.hotelStar}} </label>
                    <br>
                    <label>地址 : {{info.region}} {{show_info.address}}</label>
                    <br>
                    <br>
                    <!-- <h4>Comments</h4>
                    <div v-for="comment in comments" :key="comments.id">
                        <br>
                        Star Rate: {{comment.starRate}}
                        <br>
                        {{comment.description}}
                        <br>
                        <br>
                    </div> -->
                    <h4>房間數量</h4>
                    單人房數量 : 
                    <select v-model="singleRoomNum_ans" class="form-control" id="singleRoomNum">
                        <option v-for="n in singleRoomNum+1">{{n-1}}</option>
                    </select>
                    <br>
                    雙人房數量 : 
                    <select v-model="doubleRoomNum_ans" class="form-control" id="doubleRoomNum">
                        <option v-for="n in doubleRoomNum+1">{{n-1}}</option>
                    </select>
                    <br>
                    四人房數量 : 
                    <select v-model="quadRoomNum_ans" class="form-control" id="quadRoomNum">
                        <option v-for="n in quadRoomNum+1">{{n-1}}</option>
                    </select>
                    <br>
                    <h4>住房日期</h4>
                    Check in date : {{info.check_in_date}}
                    <br>
                    Check out date : {{info.check_out_date}}
                    <br>
                    <br>
                    <label>姓名: </label>
                    <input v-model="userName" class="form-control" id="userName">
                    <br>
                    <button class="btn btn-primary" @click="confirm()">Confirm</button>
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
        data () {
            return {
                page: 1,
                hotel_infos: this.$route.params.data.hotel_infos,
                info: this.$route.params.data,
                show_info:'',
                userName: '',
                comments: '',
                singleRoomNum: 0,
                doubleRoomNum: 0,
                quadRoomNum: 0,
                singleRoomNum_ans: 0,
                doubleRoomNum_ans: 0,
                quadRoomNum_ans: 0,
                is_show_info: false
            }
        },
        methods:{
            show_detail(info){
                this.is_show_info = false
                this.show_info = info
                
                axios_get_res('http://localhost:8080/hotel_reservation/hotels/'+this.show_info.id+
                    '?check_in_date='+this.info.check_in_date+'&check_out_date='+this.info.check_out_date,{
                        singleRoomNum: 0,
                        doubleRoomNum: 0,
                        quadRoomNum: 0
                    })
                .then(data => {
                    this.singleRoomNum = data.singleRoomNum;
                    this.doubleRoomNum = data.doubleRoomNum;
                    this.quadRoomNum = data.quadRoomNum;
                    this.is_show_info = true;
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
            confirm(){
                if(this.userName==''){
                    alert('請輸入姓名！！')
                    return null
                }
                var room_order = {
                    //id: this.show_info.id,
                    check_in_date: this.info.check_in_date,
                    check_out_date: this.info.check_out_date,
                    singleRoomNum: parseInt(this.singleRoomNum_ans),
                    doubleRoomNum: parseInt(this.doubleRoomNum_ans),
                    quadRoomNum: parseInt(this.quadRoomNum_ans),
                    customerName: this.userName
                    };
                var totalPrice = ''
                var orderID = 88997
                var isPaid = false

                console.log(room_order)

                axios_post_res('http://localhost:8080/hotel_reservation/hotels/'+this.show_info.id, room_order)
                .then((data) => {
                    if(data.totalPrice == 0) {
                        alert("Hotel is not available")
                        return
                    }
                    
                    totalPrice = data.totalPrice;
                    orderID = data.orderID;
                    isPaid = data.isPaid
                    if(isPaid){
                        isPaid = '已付款';
                    }else{
                        isPaid = '未付款';
                    }
                    var other = {address: this.show_info.address, totalPrice: totalPrice, orderID: orderID, isPaid: isPaid, hotelID: this.show_info.id}
                    this.$router.push({name: 'pay', params:{data: room_order, other: other}})
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

    function axios_get_res(url, para) {
        return axios.get(url, para)
        .then((res) => { return res.data; })
        .catch((error) => { console.log(error)})
        .finally(() => {return null; })
    };

    function axios_post_res(url, para) {
        return axios.post(url, para)
        .then((res) => { return res.data; })
        .catch((error) => { console.log(error)})
        .finally(() => {return null; })
    };
</script>

