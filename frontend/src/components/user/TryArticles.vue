<template>
  <div style="margin:5px 10px">
    <TryItem
      v-for="(tryArticle,index) in tryArticles"
      :key="index"
      :index="index"
      :tryArticle="tryArticle"
      class="mx-auto"
      max-width="344"
      style="height:140px;border-bottom:1.2px solid #DBDBDB;"
      outlined
    />
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";
import TryItem from "./TryItem.vue";
export default {
  name: "try-articles",
  props: {},
  components: {
    TryItem,
  },
  data() {
    return {};
  },
  methods: {
    ...mapActions(["articleDetail"]),
  },
  created: function () {},
  computed: {
    ...mapState(["tryArticles"]),
    ...mapState(["userInfo"]),
  },
};
</script>