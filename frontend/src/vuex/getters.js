// import vuex from '../vuex'

export default {
    isLoggedIn: state => !!state.userInfo.authToken,
    isvalid: state => state.isValidEmail,
    config: state => ({
        headers: {
            Authorization: `${state.userInfo.authToken}`
        }
    }),
    getRequestAuth: state => ({
        Authorization: `${state.userInfo.authToken}`
    }),
    getFollowed: state => state.userProfile.followed,
    authEmail: state => state.authEmail
}