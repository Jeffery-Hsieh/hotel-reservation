<template>
    <div class="form-group">
        <label>飯店: {{orderInfo.id}}</label>
        <label>星等: {{orderInfo.hotelStar}} </label>
        <label>地址 : {{orderInfo.region}} {{orderInfo.address}}</label>
        <h4>房間數量</h4>

        <div>單人房數量 : </div>
        <select 
            v-model="requestSingleRoomNum" 
            class="form-control"
            :disabled="!canModify"
        >
            <option v-for="n in singleRoomAvailNum+1" :key="n">{{n-1}}</option>
        </select>

        <div>雙人房數量 : </div>
        <select 
            v-model="requestDoubleRoomNum" 
            class="form-control"
            :disabled="!canModify"
        >
            <option v-for="n in doubleRoomAvailNum+1" :key="n">{{n-1}}</option>
        </select>

        <div>四人房數量 : </div>
        <select 
            v-model="requestQuadRoomNum" 
            class="form-control"
            :disabled="!canModify" 
        >
            <option v-for="n in quadRoomAvailNum+1" :key="n">{{n-1}}</option>
        </select>

        <div>
            <label>住房日期</label>
            <div class="form-group col-md-6">
                <label for="checkin">Check in date</label>
                <input 
                    type="date"
                    id="checkin"
                    class="form-control"
                    placeholder="Select Check in Date" 
                    v-model="orderInfo.check_in_date"
                    :disabled="!canModify">
            </div>
            <div class="form-group col-md-6">
                <label for="checkin">Check out date</label>
                <input 
                    type="date" 
                    id="checkin" 
                    class="form-control" 
                    placeholder="Select Check in Date" 
                    v-model="orderInfo.check_out_date"
                    :disabled="!canModify">
            </div>
        </div>

        <label>姓名: </label>
        <input v-model="userName" class="form-control" id="userName" :disabled="!canModify">
        <button 
            type="button"
            class="btn btn-success"
            @click="confirmBooking"
        >{{ canModify ? "Confirm" : "Modify" }}</button>

    </div>
</template>
<script src="https://unpkg.com/vuejs-datepicker"></script>

<script>
import Datepicker from 'vuejs-datepicker'
import { dateToYMD } from "../util/dateFormat"

export default {
    data() {
        return {
            userName: this.$props.customerName,
            requestSingleRoomNum: this.$props.singleRoomAvailNum,
            requestDoubleRoomNum: this.$props.doubleRoomAvailNum,
            requestQuadRoomNum: this.$props.quadRoomAvailNum,
        }
    },
    props: {
        orderInfo: Object,
        customerName: String,
        singleRoomAvailNum: Number,
        doubleRoomAvailNum: Number,
        quadRoomAvailNum: Number,
        canModify: Boolean,
    },
    components: {
        Datepicker
    },
    methods: {
        confirmBooking(){
            this.$emit("confirmBooking", this.$data)
        }
    }
}
</script>