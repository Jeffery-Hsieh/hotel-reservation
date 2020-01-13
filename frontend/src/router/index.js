import Vue from "vue"
import VueRouter from 'vue-router';

import Homepage from '../containers/Homepage.vue';
// import Order from '../containers/Order.vue';
import SelectHotelPage from '../containers/SelectHotelPage.vue';
import Order_Detail from '../containers/Order_Detail.vue';
// import Pay from '../containers/Pay.vue';

Vue.use(VueRouter)

const router = new VueRouter({
  routes: [
    {
      name: 'hotel',
      path: '/hotel',
      component: SelectHotelPage
    },
    {
      path: '/order',
      name: 'order',
      component: Order_Detail,
    },
    {
      path: '/',
      component: Homepage
    }
  ]
})

export default router