<template>
  <div style="max-width:360px;margin:15px auto">
    <v-subheader style="font-size:20px;">유저</v-subheader>
    <div style="margin-left:5px;border-bottom:1.3px solid #5E5E5E">
      <div v-if="users.length != 0">
        <v-list style="height:45px;padding:0px" v-for="user in users" :key="`${user.email}`">
          <div style="display:flex">
            <v-list-item style="font-size:17px;padding:5px" @click="goUserProfile(user.nickName)">
              <v-icon large style="padding:3px">account_circle</v-icon>
              {{user.nickName}}
            </v-list-item>
          </div>
        </v-list>
      </div>
      <div style="margin-left:15px" v-else>검색 결과가 없습니다.</div>
      <br />
    </div>
    <br />

    <v-subheader style="font-size:20px">게시물</v-subheader>
    <div v-if="articles.length != 0">
      <v-list
        style="height:45px;padding:0px"
        v-for="article in articles"
        :key="`${article.articleId}`"
      >
        <router-link
          v-if="article.price !== article.immdiatePrice || !expired"
          :to="{name: 'ArticleDetail', params: {id: article.articleId}}"
        >
          <v-list-item style="font-size:17px;padding:5px">
            <v-icon large style="padding:3px">article</v-icon>
            {{article.title}}
          </v-list-item>
        </router-link>
      </v-list>
    </div>
    <div style="margin-left:15px" v-else>검색 결과가 없습니다.</div>
    <!-- </v-row> -->
    <!-- </v-container> -->
  </div>
</template>

<script>
// import { mapActions } from 'vuex'
import { mapState } from "vuex";
import articleApi from "../../api/ArticleApi.js";
import axios from "axios";
import getters from "../../vuex/getters.js";

export default {
  data: () => ({
    users: [],
    articles: [],
    expired: false,
  }),
  computed: {
    ...mapState(["userInfo"]),
  },
  methods: {
    getSearchResult() {
      axios
        .get(
          articleApi.BASE_URL +
            articleApi.ROUTES.search +
            this.$route.query.keyword,
          getters.config
        )
        .then((res) => {
          this.users = res.data.searchUser;
          this.articles = res.data.searchArticle;
        })
        .catch((err) => {
          alert(err);
        });
    },
    goUserProfile(userNickname) {
      if (this.userInfo.userNickname) {
        this.$router.push({
          name: "UserProfile",
          params: { nickName: userNickname },
        });
      } else {
        this.$router.push({ name: "Login" });
      }
    },
    checkExpired() {
      var now_date = new Date().getTime();
      var expire_date = new Date(this.article.expireDate).getTime();

      if (now_date - expire_date < 0) {
        this.expired = false;
      } else {
        this.expired = true;
      }
    },
  },
  mounted() {
    this.getSearchResult();
  },
};
</script>
<style src="../../scss/mainfeed.scss" lang="scss">
