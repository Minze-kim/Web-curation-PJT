<template>
  <div id="app">
    <v-app id="inspire">
      <v-app-bar app color="#FF6F61" dark>
        <v-toolbar-title>
          <router-link style="text-decoration:none;color:white;" :to="{name:'Main'}">Nuga</router-link>
        </v-toolbar-title>
        <v-spacer></v-spacer>
        <!-- 제목, 태그, 작성자 별로 검색 가능? -->
        <div style="width: 60px"></div>
        <div style="width: 5px"></div>
        <v-text-field
          style="width:150px"
          class="flex-grow-0"
          append-icon="search"
          flat
          hide-details
          label="Search"
          solo-inverted
          v-model="searchWord"
          @keyup.enter="search"
        ></v-text-field>
        <v-menu style="margin:0px 14px" offset-y>
          <template v-slot:activator="{on, attrs}">
            <v-btn
              style="text-decoration:none;max-width: 45px;color:white;"
              @click="getNotifications(userInfo.authToken)"
              icon
              v-bind="attrs"
              v-on="on"
            >
              <div>
                <div v-if="isHaveAlarm === 'true' && !isChecked">
                  <v-icon
                    @click="onClick"
                    color="yellow"
                    :style="{color: isChecked}"
                    large
                  >notifications_active</v-icon>
                </div>
                <div v-else>
                  <v-icon large>notifications</v-icon>
                </div>
              </div>
            </v-btn>
          </template>
          <div
            v-if="userInfo.userNickname"
            style="overflow: auto; height: auto; max-height: 350px; width: 350px"
          >
            <v-list v-for="notification in notifications" :key="`alarm_${notification.articleId}`">
              <v-list-item
                v-if="notification.state == 2"
                @click="articleDetail(notification.articleId)"
              >
                <v-icon style="margin-right:5px">new_releases</v-icon>
                &quot;{{notification.title}}&quot;의 최고금액 입찰자가 변경되었습니다.
              </v-list-item>
              <v-list-item
                v-if="notification.state == 3"
                @click="articleDetail(notification.articleId)"
              >
                <v-icon style="color:#256BAB;margin-right:5px">card_giftcard</v-icon>
                &quot;{{notification.title}}&quot;가 낙찰되었습니다.
              </v-list-item>
              <v-list-item
                v-if="notification.state == 4"
                @click="articleDetail(notification.articleId)"
              >
                <v-icon style="color:#FBD167;margin-right:5px">monetization_on</v-icon>
                &quot;{{notification.title}}&quot;의 경매물품이 판매되었습니다.
              </v-list-item>
              <v-list-item
                v-if="notification.state == 5"
                @click="articleDetail(notification.articleId)"
              >
                <v-icon style="color:#F7665E;margin-right:5px">new_releases</v-icon>
                &quot;{{notification.title}}&quot;가 유찰되었습니다.
              </v-list-item>
              <v-list-item
                v-if="notification.state == 6"
                @click="articleDetail(notification.articleId)"
              >
                <v-icon style="color:#97C6D0;margin-right:5px">stars</v-icon>
                {{userInfo.userNickname}}님이 좋아하실 만한 상품이 등록되었어요~!!
              </v-list-item>
              <v-list-item
                v-if="notification.state == 7"
                @click="articleDetail(notification.articleId)"
              >&quot;{{notification.title}}&quot;의 배송이 완료되었습니다!</v-list-item>
              <hr style="margin: 0px" />
            </v-list>
          </div>
          <v-list v-else>
            <v-list-item>로그인을 해주세요!</v-list-item>
          </v-list>
        </v-menu>

        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      </v-app-bar>
      <v-navigation-drawer v-model="drawer" app right>
        <!-- <template v-slot:prepend> -->
        <div class="row" fixed>
          <v-list-item>
            <div style="margin:10px auto;" v-if="userInfo.userNickname">
              <router-link
                style="margin:10px auto;text-decoration:none;"
                :to="{name:'UserProfile', params: {nickName: `${userInfo.userNickname}`}}"
              >
                <v-icon
                  class="col-12"
                  style="margin-top:15px;font-size:65px;padding-bottom:0px;"
                >account_circle</v-icon>
              </router-link>
            </div>
            <div style="margin:10px auto;" v-else>
              <router-link style="margin:10px auto;text-decoration:none;" :to="{name:'Login'}">
                <v-icon
                  class="col-12"
                  style="margin-top:15px;font-size:65px;padding-bottom:0px;"
                >account_circle</v-icon>
              </router-link>
            </div>
          </v-list-item>
          <v-list-item>
            <v-list-item-content v-if="userInfo.userNickname" class="col-12">
              <v-list-item-title
                style="font-size:25px;text-align:center;"
              >{{ userInfo.userNickname }}</v-list-item-title>
              <router-link class="text-center" to="/payment">
                <v-btn
                  class="text-center lighten-2 rounded-pill d-inline-flex align-center justify-center"
                  height="50"
                  width="200"
                  style="margin:20px auto 0 auto;background-color:#B0D4C5;"
                >{{userInfo.userCoin.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}} 원</v-btn>
              </router-link>
            </v-list-item-content>
            <v-list-item-content style="text-align:center" v-else>
              <router-link style="text-decoration:none;" :to="{name:'Login'}">
                <v-btn
                  class="text-center lighten-2 rounded-pill d-inline-flex align-center justify-center col-12"
                  height="50"
                  style="background-color:#AEDAC7;"
                  width="80%"
                >로그인</v-btn>
              </router-link>
              <!-- </v-btn> -->
            </v-list-item-content>
          </v-list-item>
          <!-- </v-list-item> -->
        </div>
        <!-- </template> -->

        <v-divider></v-divider>

        <!-- 카테고리 리스트 -->

        <div>
          <v-list-item link @click="$router.push({name:'Main'})">
            <v-list-item-action>
              <v-icon large>mdi-home</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>Main</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            link
            @click="$router.push({name:'CategoryItemList',params:{categoryName:categories[0]}})"
          >
            <v-list-item-action>
              <v-icon large>computer</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>디지털</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            link
            @click="$router.push({name:'CategoryItemList',params:{categoryName:categories[1]}})"
          >
            <v-list-item-action>
              <v-icon large>sports_esports</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>게임</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            link
            @click="$router.push({name:'CategoryItemList',params:{categoryName:categories[3]}})"
          >
            <v-list-item-action>
              <v-icon large>weekend</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>가구</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            link
            @click="$router.push({name:'CategoryItemList',params:{categoryName:categories[4]}})"
          >
            <v-list-item-action>
              <v-icon style="font-size:31px">fas fa-gem</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>귀금속</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            link
            @click="$router.push({name:'CategoryItemList',params:{categoryName:categories[5]}})"
          >
            <v-list-item-action>
              <v-icon large>local_offer</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>여성의류</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            link
            @click="$router.push({name:'CategoryItemList',params:{categoryName:categories[6]}})"
          >
            <v-list-item-action>
              <v-icon large>local_offer</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>남성의류</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <v-list-item
            link
            @click="$router.push({name:'CategoryItemList',params:{categoryName:categories[7]}})"
          >
            <v-list-item-action>
              <v-icon large>menu_book</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>도서(문구)</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            link
            @click="$router.push({name:'CategoryItemList',params:{categoryName:categories[8]}})"
          >
            <v-list-item-action>
              <v-icon large>house</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>생활</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            link
            @click="$router.push({name:'CategoryItemList',params:{categoryName:categories[9]}})"
          >
            <v-list-item-action>
              <v-icon large>color_lens</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>뷰티</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            link
            @click="$router.push({name:'CategoryItemList',params:{categoryName:categories[10]}})"
          >
            <v-list-item-action>
              <v-icon large>sports_baseball</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>스포츠</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            link
            @click="$router.push({name:'CategoryItemList',params:{categoryName:categories[11]}})"
          >
            <v-list-item-action>
              <v-icon large>local_fire_department</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>캠핑</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            link
            @click="$router.push({name:'CategoryItemList',params:{categoryName:categories[12]}})"
          >
            <v-list-item-action>
              <v-icon large>card_travel</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>수입명품</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item
            link
            @click="$router.push({name:'CategoryItemList',params:{categoryName:categories[13]}})"
          >
            <v-list-item-action>
              <v-icon large>sentiment_satisfied</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>기타</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <!-- 카테고리 리스트 end -->

          <v-list-item link @click="$router.push({name:'Contact'})">
            <v-list-item-action>
              <v-icon large>perm_device_information</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>Info</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </div>
        <!-- 로그아웃 btn -->
        <!-- <div v-if="userInfo.userNickname" style="text-align:center;">
          <v-btn
            @click="logout"
            class="text-center grey lighten-2 rounded-pill d-inline-flex align-center justify-center col-12"
            height="50"
            width="80%"
          >로그아웃</v-btn>
        </div>-->
        <template v-if="userInfo.userNickname" v-slot:append>
          <div class="pa-5">
            <v-btn
              @click="logout"
              class="text-center grey lighten-2 rounded-pill d-inline-flex align-center justify-center col-12"
              height="50"
              width="80%"
              block
            >로그아웃</v-btn>
          </div>
        </template>
      </v-navigation-drawer>

      <!-- CategoryItemList -->

      <v-main>
        <router-view :key="$route.fullPath" />
      </v-main>
      <!-- Main -->

      <v-footer color="#FF6F61" app style="height:60px">
        <!-- 찜목록 -->
        <v-spacer></v-spacer>
        <div style="margin:0px 14px">
          <v-btn @click="goBack" style="text-decoration:none;max-width: 45px;color:white;" icon>
            <v-icon style="font-size:45px;font-weight:700">navigate_before</v-icon>
          </v-btn>
        </div>
        <div style="margin:0px 14px">
          <router-link style="text-decoration:none;color:white;" :to="{name:'Main'}">
            <v-btn style="text-decoration:none;max-width: 45px;color:white;" icon>
              <v-icon large>home</v-icon>
            </v-btn>
          </router-link>
        </div>
        <div style="margin:0px 14px">
          <div v-if="userInfo.userNickname">
            <router-link style="text-decoration:none;" :to="{name:'CreateArticle'}">
              <v-btn style="text-decoration:none;max-width: 45px;color:white;" icon>
                <v-icon large>edit</v-icon>
              </v-btn>
            </router-link>
          </div>
          <div v-else>
            <router-link style="text-decoration:none;" :to="{name:'Login'}">
              <v-btn style="text-decoration:none;max-width: 45px;color:white;" icon>
                <v-icon large>edit</v-icon>
              </v-btn>
            </router-link>
          </div>
        </div>
        <div style="margin:0px 14px">
          <div v-if="userInfo.userNickname">
            <router-link style="text-decoration:none;" :to="{name:'WishList'}">
              <v-btn style="text-decoration:none;max-width: 45px;color:white;" icon>
                <v-icon large>loyalty</v-icon>
              </v-btn>
            </router-link>
          </div>
          <div v-else>
            <router-link style="text-decoration:none;" :to="{name:'Login'}">
              <v-btn style="text-decoration:none;max-width: 45px;color:white;" icon>
                <v-icon large>loyalty</v-icon>
              </v-btn>
            </router-link>
          </div>
        </div>

        <!-- 알람 -->

        <!-- 마이페이지 -->
        <div style="margin:0px 14px">
          <div v-if="userInfo.userNickname">
            <router-link
              style="text-decoration:none;"
              :to="{name:'UserProfile', params: {nickName: `${userInfo.userNickname}`}}"
            >
              <v-btn style="text-decoration:none;max-width: 45px;color:white;" icon>
                <v-icon large>assignment_ind</v-icon>
              </v-btn>
            </router-link>
          </div>
          <div v-else>
            <router-link style="text-decoration:none;" :to="{name:'Login'}">
              <v-btn style="text-decoration:none;max-width: 45px;color:white;" icon>
                <v-icon large>assignment_ind</v-icon>
              </v-btn>
            </router-link>
          </div>
        </div>
        <!-- 마이페이지 end -->
        <v-spacer></v-spacer>
        <!-- <span class="white--text">&copy; {{ new Date().getFullYear() }}</span> -->
      </v-footer>
    </v-app>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

export default {
  name: "App",
  props: {
    source: String,
  },
  data: () => ({
    categories: [
      "디지털",
      "게임",
      "도서",
      "가구",
      "귀금속",
      "여성의류",
      "남성의류",
      "도서(문구)",
      "생활",
      "뷰티",
      "스포츠",
      "캠핑",
      "수입명품",
      "기타",
    ],

    show: false,
    isChecked: null,
    drawer: null,
    ifLogin: false,
    isHaveAlarm: false,
    searchWord: "",
    searchOptions: ["태그", "제목", "작성자"],
  }),

  created: function () {
    // console.log(this.$store.state.userInfo.isHaveAlarm);
    this.isHaveAlarm = this.$store.state.userInfo.isHaveAlarm;
  },
  methods: {
    ...mapActions(["logout", "getNotifications", "articleDetail"]),
    onClick() {
      this.isChecked = "#FFFFFF";
    },
    search() {
      this.$router.push({
        name: "SearchResult",
        query: { keyword: this.searchWord },
      });
      this.$router.go();
    },
    goBack() {
      this.$router.go(-1);
    },
  },
  components: {},
  computed: {
    ...mapState(["userInfo", "notifications"]),
  },
};
</script>
<style scoped>
/* #app {
  background-image: url("/static/main.jpg");
} */
.v-text-field__slot {
  width: 70%;
}
@media screen and (max-width: 640px) {
  .v-icon.notranslate.material-icons.theme--dark {
    font-size: 20px;
  }
}
</style>

