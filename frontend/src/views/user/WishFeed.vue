<template>
  <div style>
    <div style="max-width:340px;margin-top:50px" class="mx-auto">
      <label for="tags-basic">
        <h4 style="color:#5E5E5E">관심피드를등록해주세요</h4>
      </label>
      <b-form-tags
        style="height:70px; !important;margin-top:20px"
        input-id="tags-basic"
        v-model="value"
        tag-variant
        class="mb-2"
      ></b-form-tags>

      <br />
      <br />
      <div style="text-align:center;">
        <v-btn
          style="width:60%;margin-top;20px"
          depressed
          large
          color="#A1CECF"
          @click="addWishFeed"
        >저장하기</v-btn>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import wishApi from "@/api/WishApi.js";

export default {
  data() {
    return {
      value: "",
    };
  },
  methods: {
    addWishFeed: function () {
      this.$store.dispatch("addWishFeed", this.value);
    },
  },
  created: function () {
    //this.$store.dispatch("fetchWishFeed");
    axios
      .get(wishApi.BASE_URL + wishApi.ROUTES.wishfeed, {
        headers: this.$store.getters.getRequestAuth,
      })
      .then((res) => {
        this.value = res.data;
      })
      .catch(() => {});
  },
  computed: function () {},
};
</script>
<style scoped>
.form-control {
  display: block;
  width: 100%;
  height: calc(1.5em + 0.75rem + 2px);
  padding: 0.375rem 0.75rem;
  font-size: 20px;
  font-weight: 400;
  line-height: 1.5;
  color: #a7cdce;
  background-color: #fff;
  background-clip: padding-box;
  border: 1.3px solid #ced4da;
  border-radius: 0.25rem;
  transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
}
.h-auto {
  height: auto !important;
  padding: 20px;
}
.badge-secondary {
  color: #fff;
  background-color: #6c757d;
}
</style>