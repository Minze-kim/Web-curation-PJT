<template>
  <div id="changepw-form">
    <div class="password-changer box-shadow">
      <label for="password">변경할 비밀번호</label>
      <input
        @keyup.enter="onSubmit(changePwData)"
        style="margin-bottom:20px;"
        :type="passwordType"
        v-model="changePwData.password"
      />
      <label for="passwordconfirm">비밀번호 확인</label>
      <input @keyup.enter="onSubmit(changePwData)" :type="passwordType" v-model="passwordConfirm" />
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
        @click="onSubmit(changePwData)"
        style="margin:0px auto;"
        :class="{disabled: !isValid, btnSubmit: true}"
      >비밀번호 변경</button>
    </div>
  </div>
</template>
<script>
import swal from "sweetalert";
// import { mapState } from "vuex";

export default {
  name: "ChangePw",
  data() {
    return {
      changePwData: {
        email: this.$store.state.changepwEmail,
        password: "",
      },

      passwordType: "password",
      passwordModelold: "",
      passwordTypeold: "password",
      passwordConfirm: "",
    };
  },
  computed: {
    isInitial() {
      return this.changePwData.password.length < 3;
    },
    isShort() {
      return this.changePwData.password.length >= 3 && !this.isAtLeast8;
    },
    isWeak() {
      return this.isAtLeast8 && (!this.isSpecial || !this.isCapital);
    },
    isFair() {
      return this.isAtLeast8 && this.isSpecial && this.isCapital;
    },
    isExcellent() {
      return (
        this.changePwData.password.length >= 12 &&
        this.isSpecial &&
        this.isCapital
      );
    },
    isValid() {
      return (this.isFair || this.isExcellent) && this.isSame;
    },
    isCapital() {
      const regex = /[A-Z]/g;
      return this.changePwData.password.match(regex);
    },
    isAtLeast8() {
      return this.changePwData.password.length >= 8;
    },
    isSpecial() {
      const regex = /[^A-Za-z0-9]/g;
      return this.changePwData.password.match(regex);
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
        this.changePwData.password !== "" &&
        this.passwordConfirm !== "" &&
        this.changePwData.password == this.passwordConfirm
      ) {
        return true;
      } else {
        return false;
      }
    },
    // ...mapState(["changepwEmail"]),
  },
  methods: {
    showPassword() {
      this.passwordType =
        this.passwordType === "password" ? "text" : "password";
    },
    showPasswordold() {
      this.passwordTypeold =
        this.passwordTypeold === "password" ? "text" : "password";
    },
    onSubmit(changePwData) {
      if (this.isValid) {
        this.$store.dispatch("changePw", changePwData);

        // this.$router.push({ name: "Login" });
      } else {
        // const span =
        // span.innerHTML = ''
        swal(
          "오류",
          "모든 항목은 필수이며, \n비밀번호는 양식에 맞게 설정해주세요 :)",
          "error"
        );
      }
    },
  },
};
</script>
<style src="../../scss/style.scss" lang="scss">
