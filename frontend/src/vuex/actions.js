import userApi from '../api/UserApi';
import articleApi from '../api/ArticleApi';
import wishApi from '../api/WishApi';
import alarmApi from '../api/AlarmApi';
import enterestApi from '../api/EnterestApi';

import axios from 'axios';
import router from '../router';
import bidApi from '../api/BidApi';
import cookies from 'vue-cookies'
import swal from "sweetalert";
import BidApi from '../api/BidApi';
// import VueConfirmDialog from 'vue-confirm-dialog';


export default {
  login({ commit }, loginData) {
    axios.post(userApi.BASE_URL + userApi.ROUTES.login, loginData)
      .then((res) => {
        // console.dir(res)
        swal('로그인 성공', '환영합니다 :)', 'success')
        commit('SET_USER', res)
        router.push({ name: 'Main' })
        router.go()
      })
      .catch((err) => {
        console.log(err)
        swal('실패', '로그인 정보가 잘못 되었습니다.', 'error');
      });
  },
  logout({ commit }) {
    commit('DEL_USER')
    router.push({ name: 'Main' })
    router.go()
  },

  signup({ getters }, signupData) {
    return new Promise((resolve, reject) => {
      axios.post(userApi.BASE_URL + userApi.ROUTES.signup, signupData)
        .then((res) => {
          router.push({ name: 'Login' });
          getters.config;
          res;
          resolve();
        })
        .catch(() => {
          reject();
        });
    })
  },

  createArticle({ getters }, articleData) {
    if (articleData.articleRequest.state[0] == 'S') articleData.articleRequest.state = 1;
    else if (articleData.articleRequest.state[0] == 'A') articleData.articleRequest.state = 2;
    else if (articleData.articleRequest.state[0] == 'B') articleData.articleRequest.state = 3;
    else if (articleData.articleRequest.state[0] == 'C') articleData.articleRequest.state = 4;
    else articleData.articleRequest.state = 5;
    axios.post(articleApi.BASE_URL + articleApi.ROUTES.createarticle, articleData, getters.config)
      .then((res) => {
        // console.log(res.data);
        router.push({ name: "ArticleDetail", params: { id: res.data } })
        // router.push({ name: 'Main' });
      })
      .catch(() => {
        swal('작성 오류', '모든 항목은 필수입니다.', 'error')
      })
  },
  //전체글 조회
  fetchArticle({ commit, getters, state }) {
    if (state.userInfo.userNickname) {
      console.log('로그인유저')
      axios.get(articleApi.BASE_URL + articleApi.ROUTES.fetcharticles, getters.config)
        .then((res) => {
          commit('FETCH_ARTICLES', res.data)
        })
        .catch(() => {
        })
    } else {
      console.log('비로그인유저')

      axios.get(articleApi.BASE_URL + articleApi.ROUTES.fetcharticles)
        .then((res) => {
          commit('FETCH_ARTICLES', res.data)
        })
        .catch(() => {
        })
    }
  },
  //카테고리별 게시글 조회
  fetchCategoryArticle({ state, commit, getters }, categoryName) {

    if (state.userInfo.userNickname) {
      axios.get(articleApi.BASE_URL + articleApi.ROUTES.fetchcategoryarticle + categoryName, getters.config)
        .then((res) => {
          commit('FETCH_CATEGORY_ARTICLES', res.data)
        })
        .catch(() => {
        })
    } else {
      axios.get(articleApi.BASE_URL + articleApi.ROUTES.fetchcategoryarticle + categoryName)
        .then((res) => {
          commit('FETCH_CATEGORY_ARTICLES', res.data)
        })
        .catch(() => {
        })
    }

  },

  // 회원가입시 이메일 인증
  signupEmailConfirm({ state, commit }, emailData) {
    return new Promise((resolve, reject) => {
      axios.get(userApi.BASE_URL + userApi.ROUTES.signupemailconfirm + emailData)
        .then(() => {
          // commit()
          // commit('IS_VALID_EMAIL', state.isValidEmail)
          state.authEmail = false
          commit('AUTH_EMAIL', state.authEmail)
          resolve();
        })
        .catch(() => {
          state.authEmail = true
          commit('AUTH_EMAIL', state.authEmail)
          swal('인증 실패', '이미 가입한 메일입니다.', 'error')
          reject();
          // return state.authEmail
          //중복확인
          // aler
        })
    })
  },

  // //비밀번호 찾기 이메일 인증
  // emailConfirm({ commit, state }, confirmemail) {
  //   axios.get(userApi.BASE_URL + userApi.ROUTES.emailconfirm + confirmemail)
  //     .then(() => {
  //       state.isValidEmail = true
  //       commit('IS_VALID_EMAIL', state.isValidEmail)
  //     })
  //     .catch(() => {
  //       // console.log(err.status)
  //       //중복확인
  //       // aler
  //     })

  // },
  // 이메일 인증번호 확인
  checkConfirmNum({ state, commit }, confirmInfo) {
    return new Promise((resolve, reject) => {
      axios.post(userApi.BASE_URL + userApi.ROUTES.emailconfirmnum + confirmInfo.email, confirmInfo.confirmNum)
        .then(() => {
          state.isValidNum = true
          state.changepwEmail = confirmInfo.email
          state.isValidEmail = true

          commit('VALIDATED_EMAIL', state.changepwEmail)
          commit('IS_VALID_NUM', state.isValidNum)
          commit('IS_VALID_EMAIL', state.isValidEmail)
          if (confirmInfo.state === 2) { //비밀번호찾기
            router.push({ name: 'ChangePw' })
            swal(
              "인증 성공",
              "비밀번호를 변경해주세요.",
              "success"
            )
          } else {
            swal(
              "인증 성공",
              "입력하신 이메일이 성공적으로 인증되었습니다.",
              "success"
            )
          }
          resolve();
          // state.isValidEmail = true
        })
        .catch(() => {
          swal(
            "인증번호가 일치하지 않습니다.",
            "다시 인증해주세요.",
            "error"
          );
          reject();
          // console.log(err)
          // alert('인증번호가 일치하지 않습니다')
        })
    })

  },

  // 비밀번호 변경
  changePw(context, changePwData) {
    console.log(changePwData)
    axios.post(userApi.BASE_URL + userApi.ROUTES.changepw + changePwData.email, changePwData)
      .then(() => {
        swal("성공", "비밀번호가 성공적으로 변경되었습니다", "success")
        router.push({ name: 'Login' })
      })
      .catch(() => {
        swal("실패", "오류가 발생하였습니다.", "error")
      })
  },

  articleDetail({ commit }, articleId) {
    axios.get(articleApi.BASE_URL + articleApi.ROUTES.detailarticle + articleId)
      .then((res) => {
        commit('GET_ARTICLE', res.data);
        router.push({ name: 'ArticleDetail', params: { id: articleId } });
      })
      .catch(() => {
        swal('오류', '알수없는 오류가 발생하였습니다.\n다시 시도해주세요.', 'error');
      })
  },
  bidding({ state, getters }, { bidInfo, article }) {
    return new Promise((resolve, reject) => {
      if (state.userInfo.authToken != null) {
        if (article.sellerNickname != state.userInfo.userNickname) {
          if (bidInfo.bidData.coin >= bidInfo.immediatePrice) {
            swal({
              title: "입찰 확인",
              text: "즉시구매가 이상으로 입찰하셨습니다. \n\n즉시구매가로 입찰 하시겠습니까?",
              icon: "warning",
              buttons: true,
              dangerMode: true,
            })
              .then((willDelete) => {
                if (willDelete) {
                  swal({
                    title: "입찰 확인",
                    text: "경매에 입찰하게 되면 입찰 취소가 불가하니 신중하게 입찰해주세요. \n\n 현재 금액으로 입찰 하시겠습니까?",
                    icon: "warning",
                    buttons: true,
                    dangerMode: true,
                  }).then((willDelete) => {
                    if (willDelete) {
                      bidInfo.bidData.coin = bidInfo.immediatePrice;
                      if (state.userInfo.userCoin < bidInfo.bidData.coin) {
                        swal('코인부족', "보유코인이 부족합니다.", 'error')
                      }
                      else {
                        axios.post(BidApi.BASE_URL + BidApi.ROUTES.bidTryArticles, bidInfo.bidData, getters.config)
                          .then(() => {

                            swal('입찰 성공', "입찰되었습니다!", 'success')
                            // return async (dispatch) => {
                            //   let newCoinInfo = bidInfo.bidData.coin
                            //   dispatch(newCoinInfo)
                            // }
                            resolve()
                          })
                          .catch((err) => {
                            alert(err);
                            reject()
                          })
                      }
                    } else {
                      swal("입찰이 취소되었습니다");

                    }
                  })

                } else {
                  swal("입찰이 취소되었습니다");
                }
              })
          }
          else if (bidInfo.bidData.coin <= bidInfo.price) {
            swal('입찰 실패', "현재가 보다 높은 금액으로 입찰해주세요!", 'error');
          }
          else if ((bidInfo.bidData.coin - bidInfo.price) % bidInfo.minUnit === 0) {
            swal({
              title: "입찰 확인",
              text: "경매에 입찰하게 되면 입찰 취소가 불가하니 신중하게 입찰해주세요. \n\n 현재 금액으로 입찰 하시겠습니까?",
              icon: "warning",
              buttons: true,
              dangerMode: true,
            }).then((willDelete) => {
              if (willDelete) {
                if (state.userInfo.userCoin < bidInfo.bidData.coin) {
                  swal('코인부족', "보유코인이 부족합니다.", 'error')
                }
                else {
                  axios.post(BidApi.BASE_URL + BidApi.ROUTES.bidTryArticles, bidInfo.bidData, getters.config)
                    .then(() => {

                      swal('입찰 성공', "입찰되었습니다!", 'success')
                      // return async (dispatch) => {
                      //   let newCoinInfo = bidInfo.bidData.coin
                      //   dispatch(newCoinInfo)
                      // }
                      resolve()
                    })
                    .catch((err) => {
                      alert(err);
                      reject()
                    })
                }
              } else {
                swal("입찰이 취소되었습니다");

              }
            });

          }

          else {
            swal('오류', "입찰 단위를 맞춰서 입찰해주세요!", 'error');
          }
        }
        else {
          swal('입찰 실패', "본인의 상품에는 입찰할 수 없습니다!", 'error')
        }
      }
      else {
        swal('입찰 실패', "로그인이 필요합니다"), 'error';
        router.push({ name: 'Login' })
      }
    })
  },
  wishList({ commit, getters }) {
    axios.get(wishApi.BASE_URL + wishApi.ROUTES.fechWishs, { headers: getters.getRequestAuth })

      .then((res) => {
        commit('FETCH_WISHS', res.data);
      })
      .catch((err) => {
        console.log(err);
      })
  },
  wishModify({ getters, state, commit }, articleId) {
    axios.put(wishApi.BASE_URL + wishApi.ROUTES.wishModify + articleId, articleId, getters.config)
      .then((res) => {
        //commit('MODIFY_WISH',res.data.articleId)
        if (res.data.message === "해당상품을 찜했습니다") {

          state.isWishList = true //찜한 상태
          commit('IS_IN_WISH', state.isWishList)
        } else {
          state.isWishList = false //찜해제 상태
          commit('IS_IN_WISH', state.isWishList)
        }
      })
      .catch((err) => {
        if (err.response == null) return;
        if (err.response.status == '401') {
          swal('실패', '본인 상품에는 찜할 수 없습니다', 'error');
        } else {
          router.push({ name: 'Main' })
          // alert('알 수 없는 에러입니다');
        }
      })
  },
  bidTryArticles({ commit, getters }) {
    axios.get(bidApi.BASE_URL + bidApi.ROUTES.bidTryArticles, getters.config)
      .then((res) => {
        commit('BID_TRY_ARTICLES', res.data);
        res;
      })
      .catch((err) => {
        console.log(err);
      })
  },
  myArticles({ commit, getters }, nickName) {
    axios.get(articleApi.BASE_URL + articleApi.ROUTES.myarticle + nickName, getters.config)
      .then((res) => {
        commit('MY_ARTICLES', res.data);
        res;
      })
      .catch((err) => {
        console.log(err);
      })
  },
  // userprofile 프로필
  getUserProfile({ commit, getters }, nickName) {
    return axios.get(userApi.BASE_URL + userApi.ROUTES.profile + nickName, getters.config)
      .then((res) => {
        commit("USER_PROFILE", res.data);
      })
      .catch((err) => {
        console.log(err);
      })
  },

  // like({ getters }, userNickname) {
  //   axios.post(userApi.BASE_URL + userApi.ROUTES.follow, userNickname, getters.config)
  //     .then

  // },

  // 팔로우
  // validateLike({ getters }, userNickname) {
  //   axios.post(userApi.BASE_URL + userApi.ROUTES.follow, userNickname, getters.config)

  // },

  onFollow({ getters }, nickName) {
    axios.post(userApi.BASE_URL + userApi.ROUTES.follow, nickName, getters.config)
      .then(() => {

      })
      .catch(err => {
        console.log(err)
      })
  },


  coinRefill({ getters, commit }, coin) {
    axios.post(userApi.BASE_URL + userApi.ROUTES.coinrefill, coin, getters.config)
      .then((res) => {
        console.log(res.data);
        console.log(typeof res.data);
        commit("COIN_REFILL", res.data);
      })
      .catch((res) => {
        console.log(res);
      })

  },
  getNotifications({ commit, getters }, userInfo) {
    if (userInfo) {
      cookies.set('auth-alarm', false);
      // axios.get("http://i3a304.p.ssafy.io/api/", getters.config)
      axios.get(alarmApi.BASE_URL + alarmApi.ROUTES.alarms, getters.config)

        .then((res) => {
          commit('GET_NOTIFICATIONS', res.data);
        })
        .catch((err) => {
          alert(err);
        })
    }
    else {
      console.log("로그인이 필요합니다.")
    }
  },
  addWishFeed({ getters }, feedInfo) {
    axios.post(enterestApi.BASE_URL + enterestApi.ROUTES.wishFeed, feedInfo, getters.config)
      .then(() => {
        swal('성공', '피드등록에성공했습니다', 'success');
      })
      .catch((err) => {
        console.log(err);
        swal('오류', '관심피드등록은 최대 10개만 가능합니다', 'error');
      })
  },
  fetchWishFeed({ commit, getters }) {
    axios.get(enterestApi.BASE_URL + enterestApi.ROUTES.fetchWishFeed, { headers: getters.getRequestAuth })
      .then((res) => {
        commit('FETCH_WISH_FEED', res.data);
      })
      .catch((err) => {
        console.log(err);
      })
  },




}