import baseApi from './BaseApi'
export default {
  //순서대로 (서버 배포용, 로컬에서 서버 백엔드 연동용, 로컬에서 로컬 연동용)
  //BASE_URL: 'http://localhost/api',
  BASE_URL: baseApi.BASE_URL + '/users',
  // BASE_URL: 'http://localhost:3000/api/users',



  ROUTES: {
    signup: '/signup',
    login: '/login',
    //이메일로 비밀번호 찾기
    emailconfirm: '/findpw/email/', //{email} //get
    //회원가입시 이메일 인증
    signupemailconfirm: '/signup/email/', //{email}
    emailconfirmnum: `/email/confirm/`, // {email} - 바디에 인증번호
    changepw: '/findpw/', //{email}
    // modifyuserinfo: '/',
    follow: '/follow', //팔로우하기
    followerlist: '/follower/', //{nickName} 
    followinglist: '/following/', //{nickName}
    profile: '/profile/', //{nickName}
    coinrefill: '/coin',

  }
}