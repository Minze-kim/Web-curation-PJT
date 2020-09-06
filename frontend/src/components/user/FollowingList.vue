<template>
  <div>
    <v-list style="padding:0" v-for="user in users" :key="user.nickName">
      <v-list-item style="padding:0">
        <router-link
          style="color:black"
          :to="{name:'UserProfile', params: {nickName: `${user.nickName}`}}"
        >
          <v-icon class="mr-2">person</v-icon>
          {{user.nickName}}
        </router-link>
      </v-list-item>
    </v-list>
  </div>
</template>

<script>
import axios from "axios";
import userApi from "@/api/UserApi";

export default {
  name: "FollowingList",
  data() {
    return {
      users: Array,
    };
  },
  props: {
    userNickname: String,
  },
  methods: {
    getusers() {
      axios
        .get(
          userApi.BASE_URL + userApi.ROUTES.followinglist + this.userNickname
        )
        .then((res) => {
          this.users = res.data;
        });
    },
  },
  mounted() {
    this.getusers();
  },
};
</script>

<style>
.v-list.v-sheet.theme--light:hover {
  background-color: #f1f1f1;
}
</style>