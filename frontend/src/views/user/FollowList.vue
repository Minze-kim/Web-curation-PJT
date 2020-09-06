<template>
  <!-- <v-card color="basil"> -->
  <div>
    <v-card :elevation="1" style="margin:0px;position:sticky;">
      <v-card-title style="padding:15px !important;" class="text-center justify-center py-6">
        <h4 class="font-weight-bold basil--text">{{ userNickname }}</h4>
      </v-card-title>

      <v-tabs v-model="tab" background-color="transparent" color="basil" grow>
        <v-tab v-for="item in items" :key="item">{{ item }}</v-tab>
      </v-tabs>
    </v-card>
    <v-tabs-items v-model="tab">
      <v-tab-item v-for="item in items" :key="item">
        <v-card color="basil" flat>
          <FollowingList style="margin:10px" :userNickname="userNickname" v-if="tab==1" />
          <FollowerList style="margin:10px" :userNickname="userNickname" v-if="tab==0" />
          <!-- <v-card-text>{{ text }}</v-card-text> -->
        </v-card>
      </v-tab-item>
    </v-tabs-items>
  </div>
  <!-- </v-card> -->
</template>

<script>
import FollowingList from "@/components/user/FollowingList.vue";
import FollowerList from "@/components/user/FollowerList.vue";

export default {
  components: {
    FollowingList,
    FollowerList,
  },
  data() {
    return {
      userNickname: this.$attrs.nickName,
      tab: null,
      items: ["팔로워", "팔로잉"],
      text:
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
    };
  },
};
</script>

<style>
/* Helper classes */

.basil--text {
  color: #356859 !important;
}
</style>