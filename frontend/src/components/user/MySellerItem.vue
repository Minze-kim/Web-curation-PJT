<template>
  <div @click="articleDetail(mySeller.articleId)" style="display-flex">
    <div style="display:flex;">
      <div style="position:relative;">
        <div v-if="expired || mySeller.price === mySeller.immediatePrice">
          <img
            style="filter: brightness(40%);height:130px;width:130px;border-radius:15px;padding:5px"
            :src="mySeller.images[0]"
            alt
          />
          <div style="position:absolute;top:27%;left:27%;z-index:2;text-align: center;">
            <v-icon medium color="white">check_circle_outline</v-icon>
            <p style="color:white;font-size:20px;">거래종료</p>
          </div>
        </div>
        <img
          v-else
          style="height:130px;width:130px;border-radius:15px;padding:5px"
          :src="mySeller.images[0]"
          alt
        />
      </div>
      <div style="display:relative;width:100%">
        <div style="padding:10px;">{{mySeller.title}}</div>
        <div
          style="padding:0px;margin-left:10px;color:#5B5B5B;font-weight:900"
        >&nbsp;{{mySeller.price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}} 원</div>
        <p style="padding-top:48px;text-align:right;font-size:13px;">
          <v-icon style="font-size:14px;color:red">mdi-heart</v-icon>
          {{mySeller.wishCount}}
        </p>
      </div>
    </div>
  </div>
</template>


<script>
import { mapActions } from "vuex";
export default {
  name: "MySellerItem",
  props: {
    mySeller: Object,
    index: Number,
  },
  data() {
    return {
      expired: false,
    };
  },
  methods: {
    ...mapActions(["articleDetail"]),

    checkExpired() {
      var now_date = new Date().getTime();
      var expire_date = new Date(
        this.$store.state.mySellers[this.index].expireDate
      ).getTime();
      if (expire_date - now_date > 0) {
        this.expired = false;
      } else if (
        this.$store.state.mySellers[this.index].price ===
        this.$store.state.mySellers[this.index].immediatePrice
      ) {
        this.expired = true;
      } else {
        this.expired = true;
      }
    },
  },
  created() {
    this.checkExpired();
  },
};
</script>