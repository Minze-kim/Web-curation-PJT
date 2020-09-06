<template>
  <container id="wishlist" style="padding:0px;max-width:350px;display:flex;margin:0px auto;">
    <v-row>
      <v-col cols="12">
        <p
          style="margin-bottom:0px;text-align:center;font-size:18px;font-weight:500;color:#797979"
        >{{userInfo.userNickname}} 님의 찜 리스트</p>
      </v-col>

      <div
        class
        v-for="wish in wishList"
        :key="`article_${wish.articleId}`"
        style="cursor:pointer;"
      >
        <v-cols
          style="position:relative"
          cols="4"
          @click="articleDetail(wish.articleId)"
          flat
          tile
          class="grid-item"
        >
          <img :src="wish.images[0]" style="height:117px;width:117px;margin:4px;border-radius:7px;" />
          <p
            style="text-overflow: ellipsis;margin:2px;padding-left:7px;font-size:13px;font-weight:300"
          >{{wish.title.substring(0,9)+'..'}}</p>
          <p
            style="color:#E06A6A;text-overflow: ellipsis;margin:0px 2px;padding-left:7px;font-size:13px;font-weight:300"
          >{{wish.price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}} 원</p>
        </v-cols>
      </div>
    </v-row>
  </container>
</template>

<script>
import { mapState, mapActions } from "vuex";
// import axios from "axios";
// import wishApi from "@/api/WishApi.js";
export default {
  data: () => ({}),
  computed: {
    ...mapState(["userInfo"]),
    ...mapState(["wishList"]),
  },
  methods: {
    ...mapActions(["wishModify"]),
    ...mapActions(["articleDetail"]),
  },
  created: function () {
    this.$store.dispatch("wishList");
  },
};
</script>

<style src="../../scss/wishlist.scss" lang="scss">
