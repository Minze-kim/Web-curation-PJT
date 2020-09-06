<template>
  <div>
    <div class="findpw-form">
      <!-- <v-form v-model="isEmail"> -->
      <v-container style class="d-flex justify-center align-center">
        <v-row style="padding:40px;">
          <v-col cols="12">
            <h2 style="margin:0 auto 50px;color:#717171;">
              회원가입시 등록한
              <br />이메일을 입력해주세요:)
            </h2>
            <br />
          </v-col>

          <v-col style="margin:0 auto;" cols="12">
            <v-form v-model="isEmail">
              <v-text-field
                v-model="confirmInfo.email"
                label="이메일을 입력하세요."
                :rules="emailRules"
                outlined
                clearable
                @keyup.enter="loader = 'loading';emailConfirm(confirmInfo.email)"
              ></v-text-field>
            </v-form>
            <v-btn
              v-if="isEmail"
              class="ma-2"
              :loading="loading"
              :disabled="isValidEmail"
              color="#8ACEC0"
              @click="loader = 'loading';emailConfirm(confirmInfo.email)"
            >이메일 확인</v-btn>
            <!-- 이메일 인증 -->
            <v-bottom-sheet style="margin-bottom:300px;width:80%;" v-model="sheet" persistent>
              <template v-if="isValidEmail" v-slot:activator="{ on, attrs }">
                <v-btn
                  class="ma-2"
                  color="#EF827C"
                  style:dark
                  v-bind="attrs"
                  v-on="on"
                  style="margin:0px auto;"
                >인증하기</v-btn>
              </template>
              <template v-else v-slot:activator="{ attrs }">
                <v-btn
                  color="#EF827C"
                  style:dark
                  disabled
                  v-bind="attrs"
                  style="margin:0px auto;"
                >인증하기</v-btn>
              </template>
              <div>
                <v-sheet class="text-center" height="500px">
                  <v-col style="margin:0px auto;" sm="6" md="3">
                    <v-text-field
                      v-model="confirmInfo.confirmNum"
                      style="margin-top:100px;"
                      oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');"
                      :rules="confirmnumRules"
                      maxlength="6"
                      label="Regular"
                    ></v-text-field>
                    <v-btn class="mt-6" text color="primary" @click="close">닫기</v-btn>
                    <v-btn class="mt-6" text color="primary" @click="onConfirm(confirmInfo)">인증하기</v-btn>
                  </v-col>
                </v-sheet>
              </div>
            </v-bottom-sheet>
            <!-- 이메일 인증 end -->
          </v-col>
        </v-row>
      </v-container>
      <!-- </v-form> -->
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import axios from "axios";
import userApi from "@/api/UserApi";
import swal from "sweetalert";

// import EmailConfirm from "@/components/user/EmailConfirm.vue";

export default {
  components: {
    // EmailConfirm,
  },
  data() {
    return {
      loader: null,
      loading: false,
      sheet: false,
      confirmInfo: {
        confirmNum: "",
        email: "",
        state: 2, //회원가입=1, 비밀번호 찾기=2
      },
      isValidEmail: false,
      emailRules: [
        (v) => !!v || "이메일을 입력하세요.",
        (v) => /.+@.+\..+/.test(v) || "이메일 형식이 아닙니다.",

        // !!this.isValidEmail || "인증되지 않은 이메일입니다.",
      ],
      isEmail: true,
      confirmnumRules: [
        (v) => !!v || "인증번호는 필수입니다",

        (v) => (v && v.length >= 6) || "인증번호는 6자리 입니다.",
        // (v) => /0-9/.test(v) || "숫자만 입력해주세요",
      ],
    };
  },
  methods: {
    close() {
      this.sheet = !this.sheet;
    },
    emailConfirm() {
      axios
        .get(
          userApi.BASE_URL +
            userApi.ROUTES.emailconfirm +
            this.confirmInfo.email
        )
        .then(() => {
          this.isValidEmail = true;
          this[this.loader] = false;
          this.loader = null;
        })
        .catch(() => {
          this.isValidEmail = false;
          this[this.loader] = false;
          this.loader = null;
          swal(
            "등록되지 않은 이메일입니다.",
            "이메일 정보를 다시 입력해주세요 :)",
            "error"
          );
        });
    },
    onConfirm(confirmInfo) {
      if (this.confirmInfo.confirmNum.length != 6) {
        swal("인증번호는 6자리입니다.", "다시 입력해주세요.", "error");
      } else {
        this.$store.dispatch("checkConfirmNum", confirmInfo);
      }
    },
  },
  watch: {
    loader() {
      const l = this.loader;
      this[l] = !this[l];
      setTimeout(() => (this[l] = true));
    },
    isEmail() {
      this.isValidEmail = !this.isEmail;
    },
  },
  computed: {
    ...mapState(["isValidNum"]),
  },
};
</script>

<style src="../../scss/style.scss" lang="scss">
