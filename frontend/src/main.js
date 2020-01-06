import Vue from 'vue';
import VueRouter from 'vue-router';
import App from './App.vue';
import { routes } from './routes'
import Vuetify from 'vuetify/lib'

Vue.use(Vuetify)

const opts = {}

export default new Vuetify(opts)

Vue.use(VueRouter);

const router = new VueRouter({
  routes,
  mode: 'history'
});


new Vue({
  el: '#app',
  router,
  render: h => h(App)
});