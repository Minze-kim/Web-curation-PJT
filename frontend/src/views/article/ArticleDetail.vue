<template>
  <div id="mainfeed" max-width="360px" style="margin-top:10px;" class="mx-auto">
    <v-col>
      <v-card width="100%">
        <v-list-item>
          <div>
            <div v-if="userInfo.userNickname">
              <router-link
                :to="{name:'UserProfile', params: {nickName: `${article.sellerNickname}`}}"
              >
                <v-icon
                  class="user-profile-pic"
                  style="margin:10px;font-size:65px;"
                  x-large
                  @click="userProfile(article.sellerEmail)"
                >account_circle</v-icon>
              </router-link>
            </div>
            <div v-else>
              <router-link :to="{name:'Login'}">
                <v-icon
                  class="user-profile-pic"
                  style="margin:10px;font-size:65px;"
                  x-large
                >account_circle</v-icon>
              </router-link>
            </div>
          </div>
          <v-list-item-content style="margin-left:20px;">
            <v-list-item-title class="seller-name">{{ article.sellerNickname }}</v-list-item-title>

            <span>
              <div v-if="userInfo.userNickname !== article.sellerNickname">
                <v-btn
                  v-if="!followed"
                  @click="onFollow(article.sellerNickname);onClickFollow()"
                  color="#80B4F5"
                  dark
                >팔로우</v-btn>
                <v-btn
                  v-else
                  @click="onFollow(article.sellerNickname);onClickFollow()"
                  color="#80B4F5"
                  dark
                >언팔로우</v-btn>
              </div>
            </span>
          </v-list-item-content>
        </v-list-item>
        <v-carousel cycle height="370" hide-delimiter-background show-arrows-on-hover>
          <v-carousel-item max-width="490" v-for="(item, i) in article.images" :key="i" :src="item"></v-carousel-item>
        </v-carousel>
        <v-card-title style="display:block;font-size:30px;">
          {{this.article.price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}}원
          <br />
          <p style="color:#5E5E5E;font-size:15px">{{this.article.title}}</p>
        </v-card-title>

        <v-card-text style="padding-bottom:0px;">
          <div v-if="this.article.state == 1">
            물품 상태(S/A/B/C/D):
            <v-chip small dark color="warning">S (새상품 / 미개봉상품)</v-chip>
          </div>
          <div v-if="this.article.state == 2">
            물품 상태(S/A/B/C/D):
            <v-chip small dark color="success">A (사용감 거의 없음)</v-chip>
          </div>
          <div v-if="this.article.state == 3">
            물품 상태(S/A/B/C/D):
            <v-chip small dark color="light-blue">B (사용감 적음, 상태 양호)</v-chip>
          </div>
          <div v-if="this.article.state == 4">
            물품 상태(S/A/B/C/D):
            <v-chip small dark color="deep-purple">C (그럭저럭 쓸만해요)</v-chip>
          </div>
          <div v-if="this.article.state == 5">
            물품 상태(S/A/B/C/D):
            <v-chip small dark color="danger">D (사용감 많음)</v-chip>
          </div>
          <br />
          설명: {{this.article.description}}
          <br />
          <br />
          <v-chip
            style="font-size:13px;"
            class="ma-1 pa-3"
            label
            v-for="tag in article.tags"
            color="#B1D7E0"
            dark
            :key="tag"
          >
            <button style="pointer:cursor;" @click="search(tag)"># {{ tag }}</button>
          </v-chip>
          <v-divider></v-divider>
          구매일: {{this.article.purchaseDate}}
          <br />
          경매 만료일: {{this.article.expireDate}}
          <p>현재일 : {{$moment().format('YYYY-MM-DD HH:mm:ss')}}</p>
          <div
            class="price-info"
            style="justify-content:center;display:flex;text-align:center;margin:0px auto;"
          >
            <v-chip
              label
              dark
              id="now-price"
              class="text-center amber lighten-4 ma-1"
              height="40"
              width="130"
              style="font-size:12px; font-weight:900; color:#D25C43;margin:0 auto;"
            >입찰 단위: {{this.article.minUnit.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") }} 원</v-chip>
            <v-chip
              label
              dark
              id="imme-price"
              class="text-center amber lighten-4 ma-1"
              height="40"
              width="130"
              style="font-size:12px; font-weight:900; color:#D25C43;margin:0 auto;"
            >즉시구매가: {{this.bidInfo.immediatePrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}} 원</v-chip>
          </div>
          <v-divider></v-divider>
          <div v-if="article.price != article.immediatePrice && !expired">
            <v-row style="margin-top:10px;">
              <v-col cols="9" style="padding-bottom:0px;">
                <v-text-field
                  solo
                  suffix="원"
                  oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');"
                  type="number"
                  v-model.number="bidInfo.bidData.coin"
                ></v-text-field>
              </v-col>
              <v-col cols="2">
                <v-btn
                  class="d-none d-md-flex"
                  style="background-color:#ED7F67;color:white; width:100px;height:50px;font-size:18px;"
                  @click="bidding({bidInfo, article})"
                >입찰하기</v-btn>
                <v-btn
                  class="d-flex d-md-none"
                  style="background-color:#ED7F67;color:white; width:70px;height:50px;font-size:18px;"
                  @click="bidding({bidInfo, article})"
                >입찰</v-btn>
              </v-col>
            </v-row>
          </div>
          <div v-if="article.price == article.immediatePrice || expired">
            <v-row style="margin-top:0px;">
              <v-col cols="12" style="padding-bottom:0px;">
                <v-alert
                  style="font-size:13px"
                  outlined
                  type="warning"
                  prominent
                  border="left"
                >본 상품은 경매가 종료 된 상품입니다.</v-alert>
              </v-col>
            </v-row>
          </div>
        </v-card-text>
      </v-card>
    </v-col>
  </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from "vuex";
import articleApi from "@/api/ArticleApi.js";
import axios from "axios";
import swal from "sweetalert";

export default {
  data() {
    return {
      expired: false,
      followed: false,
      article: {
        articleId: Number,
        sellerId: Number,
        sellerNickname: "",
        buyerNickname: null,
        price: Number,
        immediatePrice: Number,
        minUnit: Number,
        title: "",
        description: "",
        purchaseDate: "",
        expireDate: "",
        createdDate: "",
        state: Number,
        categoryName: "",
        wishCount: Number,
        wished: false,
        followed: false,
      },
      fav: true,
      bidInfo: {
        bidData: {
          articleId: null,
          coin: Number,
        },
        price: null,
        immediatePrice: null,
        minUnit: null,
      },
    };
  },
  props: {
    id: Number,
  },
  computed: {
    ...mapState(["userInfo"]),
    ...mapGetters(["config"]),
  },
  methods: {
    ...mapActions(["onFollow"]),
    bidding({ bidInfo, article }) {
      this.$store.dispatch("bidding", { bidInfo, article }).then(() => {
        this.article.price = bidInfo.bidData.coin;
        this.bidInfo.bidData.coin = null;
      });
    },

    getarticle() {
      if (this.userInfo.userNickname) {
        return axios
          .get(
            articleApi.BASE_URL + articleApi.ROUTES.detailarticle + this.id,
            this.config
          )
          .then((res) => {
            this.article = res.data;
            this.bidInfo.bidData.articleId = res.data.articleId;
            this.bidInfo.price = res.data.price;
            this.bidInfo.immediatePrice = res.data.immediatePrice;
            this.bidInfo.minUnit = res.data.minUnit;
            this.followed = res.data.followed;
          })
          .catch(() => {
            swal("에러", "알 수 없는 에러입니다\n재로그인해주세요.", "error");
          });
      } else {
        return axios
          .get(articleApi.BASE_URL + articleApi.ROUTES.detailarticle + this.id)
          .then((res) => {
            this.article = res.data;
            this.bidInfo.bidData.articleId = res.data.articleId;
            this.bidInfo.price = res.data.price;
            this.bidInfo.immediatePrice = res.data.immediatePrice;
            this.bidInfo.minUnit = res.data.minUnit;
            this.followed = res.data.followed;
          })
          .catch(() => {});
      }
    },
    onClickFollow() {
      if (this.userInfo.userNickname) {
        this.followed = !this.followed;
      } else {
        swal("오류", "팔로우는 로그인 사용자만 가능합니다 :)", "error");
      }
    },
    checkTime() {
      var now_date = new Date().getTime();
      var expire_date = new Date(this.article.expireDate).getTime();
      if (now_date - expire_date < 0) {
        this.expired = false;
      } else {
        this.expired = true;
      }
    },
    search(tag) {
      this.$router.push({
        name: "SearchResult",
        query: { keyword: tag },
      });
    },
  },
  created() {
    this.getarticle().then(() => {
      this.checkTime();
    });
  },
};
</script>
<style src="../../scss/detailarticle.scss" lang="scss">