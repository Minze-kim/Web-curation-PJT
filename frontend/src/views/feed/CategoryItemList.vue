<template>
  <div id="mainfeed" style="height:100%; margin:10px auto">
    <div style="max-width:360px;" class="mx-auto">
      <p v-if="categoryArticles.length === 0">{{ categoryName }}에 등록된 게시물이 없습니다 :)</p>
      <v-row>
        <CategoryItem
          :article="article"
          v-for="article in categoryArticles"
          :key="`article_${article.articleId}`"
        />
      </v-row>
    </div>
    <v-btn style="position: fixed; bottom: 70px; right: 20px;" color="#F6B87B" fab href="#">
      <v-icon style="color: white;">keyboard_arrow_up</v-icon>
    </v-btn>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import CategoryItem from "@/components/article/CategoryItem.vue";
import swal from "sweetalert";

export default {
  components: {
    CategoryItem,
  },
  data() {
    return {
      categoryName: this.$attrs.categoryName,
    };
  },
  mounted: function () {
    this.$store.dispatch("fetchCategoryArticle", this.categoryName);
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
      this.$store.dispatch("wishModify", articleId);
    },
    articleDetail(id) {
      this.$router.push({ name: "ArticleDetail", params: { id: id } });
    },
    ...mapActions(["userProfile"]),
  },
};
</script>
<style src="../../scss/mainfeed.scss" lang="scss">
