import Vue from 'vue'
import Vuex from 'vuex'

import cookies from 'vue-cookies'

import getters from './getters'
import actions from './actions'
import mutations from './mutations'

Vue.use(Vuex)

const state = {
  // jwt 인증 토큰 활용

  // authToken: cookies.get('auth-token'),
  userInfo: {
    authToken: cookies.get('auth-token'),
    userNickname: cookies.get('auth-nickname'),
    userCoin: cookies.get('auth-coin'),
    userEmail: cookies.get('auth-email'),
    isHaveAlarm: cookies.get('auth-alarm'),
  },
  changepwEmail: null,
  isValidNum: false,
  isValidEmail: false,
  isUser: null,
  categoryArticles: [],
  // loginArticles: [],
  articles: [],
  article: null,
  wishList: [],
  tryArticles: [],
  mySellers: [],
  notifications: [],
  userProfile: null,
  inWishList: false,
  users: [],
  wishFeedList: [],
  isRefill: false,
  authEmail: true, //이미 존재하는 이메일
}

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions,
})
