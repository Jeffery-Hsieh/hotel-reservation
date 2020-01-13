<template>
    <div class="card-body">
        <label>飯店: {{show_info.id}}</label>
        <label>星等: {{show_info.hotelStar}} </label>
        <label>地址 : {{info.region}} {{show_info.address}}</label>
        <h4>房間數量</h4>

        <div>單人房數量 : </div>
        <select v-model="requestSingleRoomNum" class="form-control" id="singleRoomNum">
            <option v-for="n in singleRoomAvailNum+1" :key="n">{{n-1}}</option>
        </select>

        <div>雙人房數量 : </div>
        <select v-model="requestDoubleRoomNum" class="form-control" id="doubleRoomNum">
            <option v-for="n in doubleRoomAvailNum+1" :key="n">{{n-1}}</option>
        </select>

        <div>四人房數量 : </div>
        <select v-model="requestQuadRoomNum" class="form-control" id="quadRoomNum">
            <option v-for="n in quadRoomAvailNum+1" :key="n">{{n-1}}</option>
        </select>

        <div>住房日期</div>
        <div> Check in date : {{info.check_in_date}} </div>
        <div> Check out date : {{info.check_out_date}} </div>
        <label>姓名: </label>
        <input v-model="userName" class="form-control" id="userName">
        <button class="btn btn-primary" @click="confirmBooking">Confirm</button>
    </div>
</template>

<script>
export default {
    data() {
        return{
            userName: "",
            requestSingleRoomNum: 0,
            requestDoubleRoomNum: 0,
            requestQuadRoomNum: 0,
        }
    },
    props: {
        show_info: Object,
        info: Object,
        singleRoomAvailNum: Number,
        doubleRoomAvailNum: Number,
        quadRoomAvailNum: Number,
    },
    methods: {
        confirmBooking() {
            this.$emit("confirmBooking", this.$data)
        }
    }
}
</script>