<template>
  <div>
    <v-col style="padding:0px" class="col-12">
      <v-card style="margin:15px 0" width="100%">
        <div>
          <v-list-item>
            <div v-if="userInfo.userNickname">
              <router-link
                :to="{name:'UserProfile', params: {nickName: `${article.sellerNickname}`}}"
              >
                <v-icon class="user-profile-pic" style="font-size:65px;" x-large>account_circle</v-icon>
              </router-link>
            </div>
            <div v-else>
              <router-link :to="{name:'Login'}">
                <v-icon class="user-profile-pic" style="font-size:65px;" x-large>account_circle</v-icon>
              </router-link>
            </div>

            <v-list-item-content style="margin-left:20px;">
              <v-list-item-title class="seller-name">{{ article.sellerNickname }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-carousel cycle height="370" hide-delimiter-background show-arrows-on-hover>
            <v-carousel-item
              max-width="490"
              v-for="(item, i) in article.images"
              :key="i"
              :src="item"
            ></v-carousel-item>
          </v-carousel>
          <a
            style="text-decoration:none;"
            :id="article.articleId"
            @click="articleDetail(article.articleId)"
          >
            <v-row>
              <v-col cols="10">
                <v-card-title>{{ article.price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") }} 원</v-card-title>
              </v-col>
              <v-col style="vertical-align:middle" cols="2">
                <p>
                  <v-icon style="font-size:20px;color:red">mdi-heart</v-icon>
                  {{article.wishCount}}
                </p>
              </v-col>
            </v-row>
            <v-card-text>
              <div class="price my-4 subtitle-1">{{ article.title }}</div>
              <div>{{article.description}}</div>
            </v-card-text>
          </a>
        </div>
        <v-chip
          style="font-size:13px;"
          class="ma-1 pa-3"
          label
          v-for="tag in article.tags"
          color="#B1D7E0"
          dark
          :key="tag"
        >
          <!-- <v-icon></v-icon> -->
          <button style="pointer:cursor;" @click="search(tag)"># {{ tag }}</button>
        </v-chip>

        <v-divider class="mx-4"></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <div>
            <!-- 찜한 상품 -->
            <a
              v-if="article.wished"
              style="bottom:5px;right:7px;"
              icon
              @click="onWish(article.articleId);onClickWish(article.articleId)"
              large
            >
              <img style="height:35px" src="../../assets/cart6.png" />
            </a>
            <!-- 찜하지 않은 상품 -->
            <a
              v-else
              style="bottom:5px;right:7px;"
              icon
              @click="onWish(article.articleId);onClickWish(article.articleId)"
            >
              <img style="height:35px" src="../../assets/cart4.png" />
            </a>
          </div>
        </v-card-actions>
      </v-card>
    </v-col>
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";
import swal from "sweetalert";
export default {
  data() {
    return {};
  },

  props: {
    article: Object,
  },

  computed: {
    ...mapState(["userInfo", "categoryArticles", "inWishList"]),
  },
  methods: {
    articleCreate() {
      if (this.$store.state.userInfo.userEmail) {
        this.$router.push({ name: "CreateArticle" });
      } else {
        swal("오류", "글 작성을 위해서는 로그인이 필요합니다.", "error");
        this.$router.push({ name: "Login" });
      }
    },
    onWish(articleId) {
      if (this.userInfo.userNickname) {
        for (var i = 0; i < this.$store.state.categoryArticles.length; i++) {
          if (this.$store.state.categoryArticles[i].articleId === articleId) {
            if (
              this.$store.state.categoryArticles[i].sellerNickname !==
              this.userInfo.userNickname
            ) {
              this.$store.state.categoryArticles[i].wished = !this.$store.state
                .categoryArticles[i].wished;
            }
          }
        }
      } else {
        swal("실패", "찜은 로그인 사용자만 가능합니다 :)", "error");
      }
      this.$store.dispatch("wishModify", articleId);
    },
    onClickWish(articleId) {
      for (var i = 0; i < this.$store.state.categoryArticles.length; i++) {
        if (this.$store.state.categoryArticles[i].articleId === articleId) {
          if (
            this.$store.state.categoryArticles[i].sellerNickname !==
            this.userInfo.userNickname
          ) {
            if (this.$store.state.categoryArticles[i].wished) {
              this.$store.state.categoryArticles[i].wishCount =
                this.$store.state.categoryArticles[i].wishCount + 1;
            } else {
              this.$store.state.categoryArticles[i].wishCount =
                this.$store.state.categoryArticles[i].wishCount - 1;
            }
          }
        }
      }
    },
    articleDetail(id) {
      this.$router.push({ name: "ArticleDetail", params: { id: id } });
    },
    ...mapActions(["userProfile"]),
    search(tag) {
      this.$router.push({
        name: "SearchResult",
        query: { keyword: tag },
      });
    },
  },
};
</script>

<style src="../../scss/mainfeed.scss" lang="scss">
