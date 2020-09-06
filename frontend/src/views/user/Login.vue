<template>
  <v-app id="inspire">
    <v-container class="fill-height" fluid>
      <v-row align="center" justify="center">
        <v-col cols="12" xs="12" sm="8" mg="6" lg="4">
          <v-card class="elevation-12">
            <v-toolbar color="#FF6F61" dark flat>
              <v-toolbar-title>로그인</v-toolbar-title>
              <v-spacer></v-spacer>
              <!-- <v-tooltip bottom>
                <span>Source</span>
              </v-tooltip>-->
            </v-toolbar>
            <v-card-text @keyup.enter="login(loginData)">
              <v-form>
                <v-text-field
                  v-model="loginData.email"
                  label="Email"
                  :rules="emailRules"
                  name="email"
                  prepend-icon="mdi-account"
                  type="text"
                  required
                ></v-text-field>

                <v-text-field
                  v-model="loginData.password"
                  :rules="passwordRules"
                  id="password"
                  label="Password"
                  name="password"
                  prepend-icon="mdi-lock"
                  type="password"
                ></v-text-field>
              </v-form>
            </v-card-text>
            <v-card-actions>
              <!-- <v-spacer></v-spacer> -->
              <v-btn
                style="width:70%;margin:0 auto 20px;color:white;"
                @click="login(loginData)"
                class="submitbtn"
                color="#FF6F61"
              >로그인</v-btn>
              <!-- <v-divider></v-divider> -->
            </v-card-actions>
            <v-divider style="margin:0 auto;width:90%;"></v-divider>

            <v-card-actions>
              <v-spacer></v-spacer>
              <p style="color:#7F7F7F;margin:0 10px;">비밀번호를 잊어버리셨나요?</p>
              <router-link style="text-decoration:none;" :to="{name:'FindPw'}">
                <v-btn
                  style="margin-right:20px;color:white;"
                  class="submitbtn"
                  color="#FFCB94"
                >비밀번호 찾기</v-btn>
              </router-link>
            </v-card-actions>
            <v-card-actions>
              <v-spacer></v-spacer>
              <p style="color:#7F7F7F;margin:0 10px;">회원이 아니십니까?</p>
              <router-link style="text-decoration:none;" :to="{name:'Signup'}">
                <v-btn style="margin-right:20px;color:white;" class="submitbtn" color="#FFCB94">회원가입</v-btn>
              </router-link>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
import { mapActions } from "vuex";

export default {
  name: "Login",
  data() {
    return {
      loginData: {
        password: "",
        email: "",
      },
      inset: false,
      valid: false,
      password: "",
      passwordRules: [
        (v) => !!v || "비밀번호를 입력하세요",
        (v) => v.length >= 8 || "비밀번호는 8자리 이상이어야 합니다.",
      ],
      email: "",
      emailRules: [
        (v) => !!v || "이메일을 입력하세요.",
        (v) =>
          /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) ||
          "유효한 이메일이 아닙니다.",
      ],
    };
  },

  methods: {
    // login(loginData) {
    //   this.$store
    //     .dispatch("login", loginData)
    //     .then(this.$router.push({ name: "Main" }));
    // },
    ...mapActions(["login"]),
  },
  props: {
    source: String,
  },
};
</script>

<style src="../../scss/style.scss" lang="scss">
