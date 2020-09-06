import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import router from './router'
import VueCookies from 'vue-cookies'
import vueMoment from 'vue-moment'
import store from './vuex/store'
import Antd from 'ant-design-vue';
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import firebase from 'firebase';

import { library } from '@fortawesome/fontawesome-svg-core'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { far } from '@fortawesome/free-regular-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

library.add(fas)
library.add(far)

Vue.component('font-awesome-icon', FontAwesomeIcon)

Vue.use(VueCookies)
Vue.use(vueMoment)
Vue.config.productionTip = false
Vue.use(Antd);
Vue.use(BootstrapVue)
Vue.use(vuetify, {
  iconfont: 'fa'
})

// var cors = require('cors')
// app.use(cors())
import 'ant-design-vue/dist/antd.css';

var firebaseConfig = {
  apiKey: "AIzaSyAuozC7oQdBwg077TZdzJ0DWrmTU0C5VEQ",
  authDomain: "nuga-5bd51.firebaseapp.com",
  databaseURL: "https://nuga-5bd51.firebaseio.com",
  projectId: "nuga-5bd51",
  storageBucket: "nuga-5bd51.appspot.com",
  messagingSenderId: "1031265018765",
  appId: "1:1031265018765:web:27269705adb0d0dd3fd3c4",
  measurementId: "G-W711TGX5V5"
};
firebase.initializeApp(firebaseConfig);
firebase.analytics();

new Vue({
  vuetify,
  router,
  store,
  render: h => h(App)
}).$mount('#app')
