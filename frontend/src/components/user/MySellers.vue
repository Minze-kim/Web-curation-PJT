<template>
  <div style="margin:5px 10px">
    <MySellerItem
      :mySeller="mySeller"
      v-for="(mySeller,index) in mySellers"
      :key="index"
      :index="index"
      cols="12"
      class="mx-auto"
      max-width="344"
      style="height:140px;border-bottom:1.2px solid #DBDBDB;"
      outlined
    />
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";
import MySellerItem from "./MySellerItem.vue";
export default {
  name: "my-sellers",
  components: {
    MySellerItem,
  },
  props: {
    parentMessage: {},
  },
  data() {
    return {};
  },
  methods: {
    ...mapActions(["articleDetail"]),
    checkTime() {
      var now_date = new Date().getTime();
      var expire_date = new Date(this.article.expireDate).getTime();
      if (now_date - expire_date < 0) {
        4;
        this.expired = false;
      } else {
        this.expired = true;
      }
    },
  },
  created() {
    this.checkTime();
  },
  computed: {
    ...mapState(["mySellers"]),
  },
};
</script>