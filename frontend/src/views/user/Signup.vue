<template>
  <div id="signup-form" style="max-width:340px;margin-top:50px" class="mx-auto">
    <div style="max-width:340px;margin-top:50px" class="password-changer box-shadow">
      <v-form v-model="isEmail">
        <div class="no-gutter">
          <label for="email">이메일</label>
          <v-text-field
            :disabled="isValidNum"
            class="email-input"
            :rules="emailRules"
            type="email"
            v-model="signupData.email"
          ></v-text-field>
        </div>
        <div class></div>
      </v-form>
      <!-- 이메일 인증 -->
      <v-bottom-sheet style="margin-bottom:300px;width:80%;" v-model="sheet" persistent>
        <!-- <template v-if="!authEmail" v-slot:activator="{ on,attrs }">
          <v-btn
            class="email-confirm"
            color="#F37F74"
            v-on="on"
            style:dark
            v-bind="attrs"
            style="margin:0px auto;"
          >인증하기</v-btn>
        </template>-->
        <template v-if="isEmail && !isVerified" v-slot:activator="{ attrs }">
          <v-btn
            :loading="loading"
            :disabled="loading"
            class="email-confirm"
            color="#8ACEC0"
            style:dark
            v-bind="attrs"
            style="margin:0px auto;"
            @click="loader = 'loading';signupEmailConfirm(signupData.email)"
          >인증하기</v-btn>
        </template>

        <template v-else v-slot:activator="{ attrs }">
          <v-btn
            class="email-confirm"
            color="#CDF2EA"
            style:dark
            v-bind="attrs"
            style="margin:0px auto;"
          >인증하기</v-btn>
        </template>
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
      </v-bottom-sheet>
      <v-form v-model="isNickname">
        <!-- 이메일 인증 end -->

        <label for="nickname">닉네임</label>
        <v-text-field :rules="nicknameRules" type="text" v-model="signupData.nickname"></v-text-field>
        <!-- <br /> -->
      </v-form>

      <label for="password">비밀번호</label>
      <input style="margin-bottom:20px;" :type="passwordType" v-model="signupData.password" />
      <!-- <br /> -->
      <label for="passwordconfirm">비밀번호확인</label>
      <input :type="passwordType" v-model="passwordConfirm" />
      <div class="show-box">
        <button @click="showPassword">Show</button>
      </div>
      <label for="strength">
        패스워드 강도
        <span v-if="!isInitial">:&emsp;</span>
        <span
          v-bind:class="{initial: isInitial, short : isShort, weak: isWeak, fair : isFair, excellent : isExcellent }"
        >{{passwordStregnth}}</span>
      </label>

      <div class="rg-bar">
        <div
          v-bind:class="{ highlight: true, initial: isInitial,  bgShort : isShort, bgWeak: isWeak, bgFair : isFair, bgExcellent : isExcellent }"
        ></div>
      </div>

      <!-- <p class="support-text">Your password must:</p> -->
      <div style="margin-top:8px;" class="password-rules">
        <ul class="support-text">
          <li v-bind:class="{checked: isAtLeast8}">
            <i v-if="!isAtLeast8">&#10008;</i>
            <i v-else>&#10004;</i>
            비밀번호는 8 자리이상이어야 합니다.
          </li>
          <li v-bind:class="{checked: isSpecial}">
            <i v-if="!isSpecial">&#10008;</i>
            <i v-else>&#10004;</i>
            특수문자를 하나 이상 포함해야 합니다.
          </li>
          <li v-bind:class="{checked: isCapital}">
            <i v-if="!isCapital">&#10008;</i>
            <i v-else>&#10004;</i>
            영문 대문자 하나 이상을 포함해야 합니다.
          </li>
          <li v-bind:class="{checked: isSame}">
            <i v-if="!isSame">&#10008;</i>
            <i v-else>&#10004;</i>
            비밀번호와 비밀번호 확인이 일치해야 합니다.
          </li>
        </ul>
      </div>
      <br />
      <button
        @click="onSubmit(signupData)"
        style="margin:0px auto;"
        :class="{disabled: !isValid, btnSubmit: true}"
      >회원가입</button>
    </div>
  </div>
</template>
<script>
import { mapState } from "vuex";
import swal from "sweetalert";

export default {
  name: "SignupForm",

  data() {
    return {
      loader: null,
      loading: false,
      sheet: false,
      confirmInfo: {
        confirmNum: "",
        email: "",
        state: 1, //회원가입=1, 비밀번호 찾기=2
      },
      signupData: {
        email: "",
        nickname: "",
        password: "",
      },
      isNickname: true,

      isEmail: true,
      isVerified: false,
      passwordType: "password",
      passwordModelold: "",
      passwordTypeold: "password",
      passwordConfirm: "",
      emailRules: [
        (v) => !!v || "이메일을 입력하세요.",
        (v) => /.+@.+\..+/.test(v) || "이메일 형식이 아닙니다.",
      ],
      nicknameRules: [
        (v) => !!v || "닉네임을 입력하세요.",
        (v) => (v && v.length <= 10) || "닉네임은 10글자 미만이어야 합니다.",
      ],
      confirmnumRules: [
        (v) => !!v || "인증번호는 필수입니다",

        (v) => (v && v.length >= 6) || "인증번호는 6자리 입니다.",
      ],
      isConfirm: null,
    };
  },
  computed: {
    ...mapState(["isValidEmail", "authEmail", "isValidNum"]),

    isInitial() {
      return this.signupData.password.length < 3;
    },
    isShort() {
      return this.signupData.password.length >= 3 && !this.isAtLeast8;
    },
    isWeak() {
      return this.isAtLeast8 && (!this.isSpecial || !this.isCapital);
    },
    isFair() {
      return this.isAtLeast8 && this.isSpecial && this.isCapital;
    },
    isExcellent() {
      return (
        this.signupData.password.length >= 12 &&
        this.isSpecial &&
        this.isCapital
      );
    },
    isValid() {
      return (
        (this.isFair || this.isExcellent) &&
        this.isSame &&
        this.isVerified &&
        this.isNickname
      );
    },
    isCapital() {
      const regex = /[A-Z]/g;
      return this.signupData.password.match(regex);
    },
    isAtLeast8() {
      return this.signupData.password.length >= 8;
    },

    isSpecial() {
      const regex = /[^A-Za-z0-9]/g;
      return this.signupData.password.match(regex);
    },
    passwordStregnth() {
      let msg = "";
      msg = this.isShort ? "Very Weak" : msg;
      msg = this.isWeak ? "Weak" : msg;
      msg = this.isFair ? "Fair" : msg;
      msg = this.isExcellent ? "Strong" : msg;
      return msg;
    },
    isSame() {
      if (
        this.signupData.password !== "" &&
        this.passwordConfirm !== "" &&
        this.signupData.password == this.passwordConfirm
      ) {
        return true;
      } else {
        return false;
      }
    },
  },
  methods: {
    close() {
      this.sheet = !this.sheet;
    },
    onConfirm(confirmInfo) {
      this.confirmInfo.email = this.signupData.email;
      if (this.confirmInfo.confirmNum.length != 6) {
        swal("인증번호는 6자리입니다.", "다시 입력해주세요.", "error");
      } else {
        this.$store
          .dispatch("checkConfirmNum", confirmInfo)
          .then(() => {
            this.sheet = !this.sheet;
            this.isVerified = true;
          })
          .catch(() => {});
      }
    },
    showPassword() {
      this.passwordType =
        this.passwordType === "password" ? "text" : "password";
    },
    showPasswordold() {
      this.passwordTypeold =
        this.passwordTypeold === "password" ? "text" : "password";
    },
    onSubmit(signupData) {
      if (this.isValid) {
        this.$store
          .dispatch("signup", signupData)
          .then(() => {
            swal(
              "가입 성공",
              "회원가입이 성공적으로 완료되었습니다. \n 로그인 해주십시오.",
              "success"
            );
          })
          .catch(() => {});
      } else {
        swal(
          "가입 실패",
          "입력되지 않은 항목이 있거나 \n 이메일인증이 완료되지 않았습니다 :(",
          "error"
        );
      }
    },

    signupEmailConfirm(email) {
      this.$store
        .dispatch("signupEmailConfirm", email)
        .then(() => {
          this.sheet = !this.sheet;
          const l = this.loader;
          this[l] = !this[l];
          this.loader = null;
        })
        .catch(() => {
          const l = this.loader;
          this[l] = !this[l];
          this.loader = null;
        });
    },
  },
  watch: {
    loader() {
      const l = this.loader;
      this[l] = !this[l];
      setTimeout(() => (this[l] = true));
    },
  },
};
</script>
<style src="../../scss/style.scss" lang="scss">
