import Vue from "vue"
import VueRouter from 'vue-router';

import Homepage from '../containers/Homepage.vue';
// import Order from '../containers/Order.vue';
import SelectHotelPage from '../containers/SelectHotelPage.vue';
import OrderPage from '../containers/OrderPage.vue';
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
      component: OrderPage,
    },
    {
      path: '/',
      component: Homepage
    }
  ]
})

export default router