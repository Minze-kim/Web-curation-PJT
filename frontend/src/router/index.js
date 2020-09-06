import Vue from 'vue'
import VueRouter from 'vue-router'

//user
import Login from '../views/user/Login.vue'
import Signup from '../views/user/Signup.vue'
import ChangePw from '../views/user/ChangePw.vue'
import UserProfile from '../views/user/UserProfile.vue'
import WishList from '../views/user/WishList.vue'
import FindPw from '../views/user/FindPw.vue'
import WishFeed from '../views/user/WishFeed.vue'

import FollowList from '../views/user/FollowList.vue'


// feed
import Main from '../views/feed/MainFeed.vue'
import SearchResult from '../views/feed/SearchResult.vue'
import CategoryItemList from '../views/feed/CategoryItemList.vue'

//article
import CreateArticle from '../views/article/CreateArticle.vue'
import ArticleDetail from '../views/article/ArticleDetail.vue'

import Test from '../views/article/Test.vue'
// contact
import Contact from '../views/nuga/ContactInfo.vue'
// import { mapGetters } from 'vuex'

Vue.use(VueRouter)

const routes = [

  //user
  {
    path: '/user/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/user/changepw',
    name: 'ChangePw',
    component: ChangePw
  },
  {
    path: '/user/findpw',
    name: 'FindPw',
    component: FindPw
  },

  {
    path: '/user/signup',
    name: 'Signup',
    component: Signup
  },
  {
    path: '/wishlist',
    name: 'WishList',
    component: WishList
  },
  {
    path: '/follow/:nickName',
    name: 'FollowList',
    component: FollowList,
    props: route => ({
      nickName: (route.params.nickName)
    })
  },
  {
    path: '/addwishfeed',
    name: '/WishFeed',
    component: WishFeed,
  },



  //feed
  {
    path: '/',
    name: 'Main',
    component: Main
  },
  {
    path: '/category/:categoryName',
    name: 'CategoryItemList',
    component: CategoryItemList,
    props: route => ({
      categoryName: route.params.categoryName
    })

  },

  //article
  {
    path: '/article/create',
    name: 'CreateArticle',
    component: CreateArticle
  },
  {
    path: '/article/detail/:id',
    name: 'ArticleDetail',
    component: ArticleDetail,
    props: route => ({
      id: Number(route.params.id)
    })
  },

  {
    path: '/search',
    name: 'SearchResult',
    component: SearchResult,
    props: (route) => ({
      query: route.query.keyword
    })
  },
  {
    path: '/test',
    name: 'Test',
    component: Test
  },
  {
    path: '/test',
    name: 'Test',
    component: Test
  },

  //myprofile
  {
    path: '/profile/:nickName',
    name: 'UserProfile',
    component: UserProfile,
    props: route => ({
      nickName: (route.params.nickName)
    })
    //유저 프로필 누를때 아이디값 params에 같이 넘겨주기!
  },
  // {
  //   // path: '/profile/:id',
  //   path: '/profile',
  //   name: 'UserProfile',
  //   component: UserProfile,
  //   //유저 프로필 누를때 아이디값 params에 같이 넘겨주기!
  //   props: route => ({
  //     id: Number(route.params.id)
  //   })

  // },

  //Nuga
  {
    path: '/contact',
    name: 'Contact',
    component: Contact
  },
  //pay
  {
    path: '/payment',
    name: '/Payment',
    component: () => import(/* webpackChunkName: "payment" */ '../views/pay/Payment.vue'),
  },
  {
    path: '/result',
    name: 'Result',
    component: () => import(/* webpackChunkName: "result" */ '../views/pay/Result.vue'),
  },
  {
    path: '/addwishFeed',
    name: '/WishFeed',
    // component: WishFeed,
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 모든 페이지에 접근하기 전에 거치는 검증의 개념!!
// 전역 가드 > 보호하기 이전에 전역 등록 가능
// 사용자 인증관련 페이지..
// router.beforeEach((to, from, next) => {
//   const publicPages = ['Login', 'Signup', 'Main']  // Login 안해도 됨
//   const authPages = ['Login', 'Signup']  // 인증페이지, Login 되어있으면 안됨
//   console.log('TO : ', to)
//   console.log('FROM : ', from)
//   next()
//   const authRequired = !publicPages.includes(to.name)  // 로그인 해야 함.
//   const unauthRequired = authPages.includes(to.name)  // 로그인 해서는 안됨
//   const isUser = !!Vue.$cookies.isKey('auth-token')

//   if (unauthRequired && isUser) {
//     next('/')
//   }
//   authRequired && !isUser ? next({ name: 'Login' }) : next()
// })

export default router
