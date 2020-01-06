import Homepage from './components/Homepage.vue';
import Header from './components/Header.vue';
import Hotel from './components/Hotel.vue';
import Order from './components/Order.vue';
import Room_Detail from './components/Room_Detail.vue';
import Order_Detail from './components/Order_Detail.vue';
import Pay from './components/Pay.vue';
import Reservation from './components/Reservation.vue'
import Test from './components/test.vue'

export const routes = [
  {
    path: '/',
    component: Homepage
  },
  {
    path: '/hotel',
    name: 'hotel',
    components: {
      default: Hotel,
      nav: Header,
      detail: Room_Detail
    }
  },
  {
    path: '/order',
    name: 'order',
    components: {
      default: Order,
      nav: Header,
      detail: Order_Detail,
    }
  },
  {
    path: '/pay',
    name: 'pay',
    components:{
      default: Pay,
      nav: Header,
      detail: Reservation
    }
  },
  {
    path: '*',
    redirect: '/',
  }
];