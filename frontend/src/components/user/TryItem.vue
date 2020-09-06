<template>
  <div @click="articleDetail(tryArticle.articleId)">
    <div style="display:flex;">
      <div style="position:relative;">
        <div style="position:relative;">
          <div v-if="tryArticle.state===1">
            <img
              style=";height:130px;width:130px;border-radius:15px;padding:5px"
              alt
              :src="tryArticle.images[0]"
            />
          </div>
          <div v-else-if="tryArticle.state===2">
            <img
              :src="tryArticle.images[0]"
              style=";height:130px;width:130px;border-radius:15px;padding:5px"
              alt
            />
            <div style="position:absolute;top:25%;left:33%;z-index:2;text-align: center;"></div>
          </div>
          <div v-else-if="tryArticle.state===3">
            <img
              :src="tryArticle.images[0]"
              style="filter:brightness(40%);height:130px;width:130px;border-radius:15px;padding:5px"
              alt
            />
            <div style="position:absolute;top:27%;left:27%;z-index:2;text-align: center;">
              <v-icon medium color="white">check_circle_outline</v-icon>
              <p style="color:white;font-size:20px;">거래종료</p>
            </div>
          </div>
        </div>
      </div>

      <div style="display:relative;width:100%">
        <div style="padding:10px;">{{tryArticle.title}}</div>
        <div
          style="padding:0px;margin-left:10px;color:#5B5B5B;font-weight:900"
        >&nbsp;{{tryArticle.price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}} 원</div>
        <span v-if="tryArticle.state===1">
          <v-chip style="height:28px" class="ma-1" color="#ffaa71" outlined>
            <v-icon style="font-size:20px" left>shopping_basket</v-icon>입찰중
          </v-chip>
          <v-chip style="height:28px" class="ma-1" color="#1fab89" outlined>
            <v-icon style="font-size:20px" left>people</v-icon>경매 진행중
          </v-chip>
        </span>
        <span v-if="tryArticle.state===2">
          <v-chip style="height:28px" class="ma-1" color="#eb7070" outlined>
            <v-icon style="font-size:20px" left>feedback</v-icon>시도/실패
          </v-chip>
          <v-chip style="height:28px" class="ma-1" color="#1fab89" outlined>
            <v-icon style="font-size:20px" left>people</v-icon>경매 진행중
          </v-chip>
        </span>
        <span v-if="tryArticle.state===3">
          <v-chip
            @click="receiveProduct(tryArticle.articleId)"
            style="color:white;height:28px"
            class="ma-1"
            color="#ff6f61"
          >
            <v-icon style="color:white;font-size:20px" left>emoji_events</v-icon>최종 낙찰/구매확정
          </v-chip>
          <v-chip style="height:28px" class="ma-1" color="#8aae92" outlined>
            <v-icon style="font-size:20px" left>people</v-icon>경매 종료
          </v-chip>
        </span>
        <p style="padding-top:12px;text-align:right;font-size:13px;">
          <v-icon style="font-size:14px;color:red">mdi-heart</v-icon>
          {{tryArticle.wishCount}}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState, mapGetters } from "vuex";
import bidApi from "@/api/BidApi.js";
import swal from "sweetalert";
import axios from "axios";

export default {
  props: {
    tryArticle: Object,
    index: Number,
  },

  data() {
    return {};
  },
  methods: {
    ...mapActions(["articleDetail"]),
    receiveProduct(articleId) {
      swal({
        title: "택배를 받으셨나요?",
        text:
          "상품 수령 후에는 \n 환불처리가 불가하며\n마일리지는 판매자에게 전달됩니다.",
        icon: "warning",
        buttons: true,
        dangerMode: true,
      }).then((willDelete) => {
        if (willDelete) {
          axios
            .get(
              bidApi.BASE_URL + bidApi.ROUTES.receiveProduct + articleId,
              this.config
            )
            .then(() => {
              swal("구매확정 되었습니다.", {
                icon: "success",
              });
            })
            .catch(() => {
              swal("이미 구매확정된 상품입니다.", {
                icon: "error",
              });
            });
        } else {
          swal("3달 이내에 구매확정을 하지 않으면 \n 자동으로 확정됩니다.");
        }
      });
    },
  },
  created: function () {},
  computed: {
    ...mapState(["userInfo"]),
    ...mapGetters(["config"]),
  },
};
</script>

<style>
</style>