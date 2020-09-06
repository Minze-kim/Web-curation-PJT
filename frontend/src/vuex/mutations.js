// import vuex from '../vuex'
// import { $cookies } from 'vue/types/umd'
import cookies from 'vue-cookies'


export default {
    // SET_TOKEN(state, token) {
    //     state.authToken = token
    //     cookies.set('auth-token', token)
    // },
    IS_VALID_EMAIL(state, isValidEmail) {
        state.isValidEmail = isValidEmail
    },
    IS_VALID_NUM(state, isValidNum) {
        state.isValidNum = isValidNum
    },
    VALIDATED_EMAIL(state, changepwEmail) {
        state.changepwEmail = changepwEmail
    },
    AUTH_EMAIL(state, email) {
        state.authEmail = email
    },
    FETCH_ARTICLES(state, articles) {
        state.articles = articles
    },
    FETCH_CATEGORY_ARTICLES(state, categoryArticles) {
        state.categoryArticles = categoryArticles
    },
    GET_ARTICLE(state, article) {
        state.article = article
    },
    SET_USER(state, res) {
        cookies.set('auth-token', res.headers.authorization)
        cookies.set('auth-nickname', res.data.loginUser.nickName)
        cookies.set('auth-coin', res.data.loginUser.coin)
        cookies.set('auth-email', res.data.loginUser.email)
        cookies.set('auth-alarm', res.data.isHaveAlarm)
        // state.userInfo.authToken = res.headers.authorization
        // state.userInfo.userNickname = res.data.loginUser.nickName
        // state.userInfo.userCoin = res.data.loginUser.coin
        // state.userInfo.userEmail = res.data.loginUser.email
    },
    DEL_USER() {
        cookies.set('auth-token', "")
        cookies.set('auth-nickname', "")
        cookies.set('auth-coin', "")
        cookies.set('auth-email', "")
        cookies.set('auth-alarm', false)
    },
    FETCH_WISHS(state, wishs) {
        state.wishList = wishs;
    },
    BID_TRY_ARTICLES(state, bidTryArticles) {
        //state.bidTryArticles = "";
        state.tryArticles = bidTryArticles;
    },
    MY_ARTICLES(state, myArticles) {
        //state.mySellers="";
        state.mySellers = myArticles;
    },
    USER_PROFILE(state, profile) {
        state.userProfile = profile;
    },
    GET_NOTIFICATIONS(state, notifications) {
        state.notifications = notifications;
    },
    COIN_REFILL(state, coin) {
        console.log(typeof state.userInfo.userCoin);
        console.log(typeof coin);
        var temp = Number(state.userInfo.userCoin) + coin;
        console.log(temp);
        cookies.set('auth-coin', temp);
    },
    IS_IN_WISH(state, inWishList) {
        state.inWishList = inWishList
    },
    FETCH_WISH_FEED(state, fetchWishFeed) {

        state.wishFeedList = fetchWishFeed

    },
    SEARCH_RESULT(state, searchResult) {
        state.articles = searchResult.searchArticle;
        state.users = searchResult.searchUser;
    }

}