<template>
  <div>
    <!-- 내 프로필 -->
    <div>
      <v-card
        elevation="1"
        v-if="userInfo.userNickname === userProfile.loginUser.nickName"
        style="padding:20px; min-width:290px;max-width:500px; margin:10px auto"
      >
        <v-row>
          <v-col cols="12" style="margin:0px auto;padding:0px">
            <h5 style="margin:0 auto 20px; auto;text-align:center">{{userInfo.userNickname}}</h5>
          </v-col>
          <v-col style="padding:35px" cols="3">
            <v-btn icon>
              <v-icon style="font-size:100px">account_circle</v-icon>
            </v-btn>
            <!-- <span> -->
            <!-- </span> -->
          </v-col>
          <v-col cols="9">
            <v-simple-table>
              <template v-slot:default>
                <thead>
                  <tr>
                    <th class="text-center">
                      판매
                      <br />마감
                    </th>
                    <th class="text-center">판매중</th>
                    <th class="text-center">
                      <router-link :to="{name:'FollowList', params:{nickName:nickName}}">팔로워</router-link>
                    </th>
                    <th class="text-center">
                      <router-link :to="{name:'FollowList',params:{nickName:nickName}}">팔로잉</router-link>
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td class="text-center">{{userProfile.sellingCount}}</td>
                    <td class="text-center">{{userProfile.totalSellCount-userProfile.sellingCount}}</td>
                    <td style="padding:0" class="text-center">
                      <router-link
                        :to="{name:'FollowList',params:{nickName:nickName}}"
                      >{{userProfile.followerCount}}</router-link>
                    </td>
                    <td class="text-center">
                      <router-link
                        :to="{name:'FollowList',params:{nickName:nickName}}"
                      >{{userProfile.followingCount}}</router-link>
                    </td>
                  </tr>
                </tbody>
              </template>
            </v-simple-table>
          </v-col>
        </v-row>
        <v-row>
          <v-col style="padding:0px">
            <div v-if="userInfo.userNickname" style="text-align:center;">
              <router-link to="/payment">
                <v-btn outlined style="width:150px;margin:3px" color="#609BE5" dark>코인 충전하기</v-btn>
              </router-link>
              <!-- <div> -->
              <router-link to="/addWishFeed">
                <v-btn outlined style="width:150px;margin:3px" color="#609BE5" dark>관심피드등록</v-btn>
              </router-link>
              <!-- </div> -->
            </div>
          </v-col>
        </v-row>
      </v-card>

      <!-- 타 유저 프로필 -->
      <v-card v-else style="padding:20px; min-width:290px;max-width:500px; margin:10px auto">
        <v-row>
          <v-col cols="12" style="margin:0px auto;padding:0px">
            <h5
              style="margin:0 auto 20px; auto;text-align:center"
            >{{userProfile.loginUser.nickName}}</h5>
          </v-col>
          <v-col style="padding:35px" cols="3">
            <v-btn icon>
              <v-icon style="font-size:100px">account_circle</v-icon>
            </v-btn>
            <!-- <span> -->
            <!-- </span> -->
          </v-col>
          <v-col cols="9">
            <v-simple-table>
              <template style v-slot:default>
                <thead>
                  <tr>
                    <th class="text-center">
                      판매
                      <br />마감
                    </th>
                    <th class="text-center">판매중</th>
                    <th class="text-center">
                      <router-link :to="{name:'FollowList', params:{nickName:nickName}}">팔로워</router-link>
                    </th>
                    <th class="text-center">
                      <router-link :to="{name:'FollowList',params:{nickName:nickName}}">팔로잉</router-link>
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td class="text-center">{{userProfile.sellingCount}}</td>
                    <td class="text-center">{{userProfile.totalSellCount-userProfile.sellingCount}}</td>
                    <td style="padding:0" class="text-center">
                      <router-link
                        :to="{name:'FollowList',params:{nickName:nickName}}"
                      >{{userProfile.followerCount}}</router-link>
                    </td>
                    <td class="text-center">
                      <router-link
                        :to="{name:'FollowList',params:{nickName:nickName}}"
                      >{{userProfile.followingCount}}</router-link>
                    </td>
                  </tr>
                </tbody>
              </template>
            </v-simple-table>
          </v-col>
        </v-row>
        <div>
          <div v-if="userInfo.userNickname && !followed" style="text-align:center;">
            <v-btn
              @click="onFollow(nickName);onClickFollow(userProfile.followerCount)"
              style="width:200px"
              color="#80B4F5"
              dark
            >팔로우</v-btn>
          </div>
          <div v-else style="text-align:center;">
            <v-btn
              @click="onFollow(nickName);onClickFollow(userProfile.followerCount)"
              style="width:200px"
              color="#80B4F5"
              dark
            >언팔로우</v-btn>
          </div>
        </div>
      </v-card>
    </div>

    <!-- 마이페이지 컨텐츠 -->
    <div
      v-if="userInfo.userNickname === userProfile.loginUser.nickName"
      style="padding:0px; min-width:290px;max-width:500px; margin:10px auto"
    >
      <v-toolbar flat>
        <!-- <template v-slot:extension> -->
        <v-tabs v-model="myTabs" fixed-tabs>
          <v-tabs-slider></v-tabs-slider>
          <v-tab :key="0" href="#mobile-tabs-5-1" class="primary--text" @click="bidTryArticles">
            <v-icon medium>local_atm</v-icon>

            <small style="color:grey;font-size:16px;margin-left:5px;">입찰중</small>
          </v-tab>

          <v-tab :key="1" href="#mobile-tabs-5-2" class="primary--text" @click="myArticles">
            <v-icon medium>storefront</v-icon>
            <small style="color:grey;font-size:16px;margin-left:5px;">내상품</small>
          </v-tab>
        </v-tabs>
        <!-- </template> -->
      </v-toolbar>

      <try-articles v-if="isLeftTab==true"></try-articles>
      <my-sellers v-if="isLeftTab==false"></my-sellers>
    </div>
    <!-- 타유저 컨텐츠 -->
    <div v-else style="padding:0px; min-width:290px;max-width:500px; margin:10px auto">
      <v-toolbar flat>
        <!-- <template v-slot:extension> -->
        <v-tabs v-model="tabs" fixed-tabs>
          <v-tabs-slider style="padding-left:20px"></v-tabs-slider>
          <!-- <v-tab :key="1" href="#mobile-tabs-5-1" class="primary--text" @click="bidTryArticles">
          <v-icon large>local_atm</v-icon>

          <small style="color:grey;font-size:20px;margin-left:5px;">입찰중</small>
          </v-tab>-->

          <v-tab :key="0" href="#mobile-tabs-5-2" class="primary--text" @click="myArticles">
            <v-icon medium>storefront</v-icon>
            <small style="color:grey;font-size:16px;margin-left:5px;">{{this.nickName}} 님의 상품</small>
          </v-tab>
        </v-tabs>
        <!-- </template> -->
      </v-toolbar>

      <my-sellers></my-sellers>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from "vuex";
import TryArticles from "@/components/user/TryArticles";
import MySellers from "@/components/user/MySellers";
export default {
  components: { TryArticles, MySellers },
  data() {
    return {
      followed: false,
      // unfollow: true,
      tab: null,
      tabs: 2,
      myTabs: 2,
      isLeftTab: true,
      nickName: this.$attrs.nickName,
    };
  },
  computed: {
    ...mapState(["userInfo", "userProfile"]),
    ...mapGetters(["getFollowed"]),
  },
  mounted() {
    this.$store.dispatch("bidTryArticles");
    this.$store.dispatch("myArticles", this.nickName);
    this.$store
      .dispatch(
        "getUserProfile",
        this.nickName
        // this.$store.state.userInfo.userNickname
      )
      .then(() => {
        this.followed = this.userProfile.followed;
      });
  },
  methods: {
    ...mapActions(["articleDetail", "onFollow"]),
    myArticles() {
      this.isLeftTab = false;
    },
    bidTryArticles() {
      this.isLeftTab = true;
    },
    onClickFollow(followNum) {
      // this.onFollow(this.nickName);

      this.followed = !this.followed;
      if (this.followed) {
        this.userProfile.followerCount = followNum + 1;
      } else {
        this.userProfile.followerCount = followNum - 1;
      }
    },
    // onClickUnFollow() {
    //   this.unfollow = !;
    // },
  },
};
</script>
<style scoped>
.tbody:hover {
  background-color: none;
}
</style>