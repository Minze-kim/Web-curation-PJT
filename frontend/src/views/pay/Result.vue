<template>
  <v-card class="mx-auto" style="margin-top:20px;max-width:360px;padding:20px">
    <div class="imp-container imp-result">
      <div style="margin:0px auto;text-align:center;">
        <a-icon
          v-if="success"
          type="check-circle"
          :style="{ fontSize: '150px', color: '#52c41a' }"
        />
        <a-icon v-else type="exclamation-circle" :style="{ fontSize: '150px', color: '#f5222d' }" />
      </div>
      <h3
        style="text-align:center;color:#5E5E5E;margin-top:10px"
      >{{ type === 'payment' ? '결제' : '본인인증' }}에 {{ success ? '성공' : '실패'}}하였습니다.</h3>
      <ul style="margin-top:40px">
        <li>
          <span>아임포트 번호</span>
          <span>{{ impUid }}</span>
        </li>
        <li>
          <span>주문번호</span>
          <span>{{ merchantUid }}</span>
        </li>
        <li>
          <span>결제금액</span>
          <span>{{ amount }}</span>
        </li>
        <li>
          <span>영수증(복사해서url에입력하세요)</span>
          <span>{{ receipt}}</span>
        </li>
        <li v-if="!success">
          <span>에러 메시지</span>
          <span>{{ errorMessage }}</span>
        </li>
      </ul>
      <div style="margin:0px auto;text-algin:center">
        <a-button size="large" @click="handleGoBack">뒤로가기</a-button>
      </div>
    </div>
  </v-card>
</template>

<script>
export default {
  data() {
    const { query } = this.$router.history.current;
    const { type } = query;
    return {
      type,
      success: this.getSuccess(query),
      impUid: query.imp_uid,
      merchantUid: query.merchant_uid,
      amount: query.paid_amount,
      receipt: query.receipt_url,
      errorMessage: `[${query.error_code}] ${query.error_msg}`,
    };
  },
  methods: {
    getSuccess(query) {
      const { success } = query;
      const impSuccess = query.imp_success;
      if (impSuccess === undefined) {
        if (typeof success === "string") {
          return success === "true";
        }
        return success;
      }
      if (typeof impSuccess === "string") {
        return impSuccess === "true";
      }
      return impSuccess;
    },
    handleGoBack() {
      this.$router.push(`/${this.type}`);
    },
  },
  created: function () {
    if (this.success === true && this.$store.state.isRefill) {
      this.$store.dispatch("coinRefill", Number(this.amount));
      this.$store.state.isRefill = false;
    }
  },
};
</script>

<style lang="less">
.imp-container.imp-result {
  .anticon {
    margin-bottom: 20px;
  }
  ul {
    padding: 0;
    margin: 0 auto;
    width: 70%;
    li {
      text-align: left;
      list-style: none;
      font-size: 16px;
      display: flex;
      margin: 10px 0;
      span {
        display: inline-block;
        font-size: 13px;
      }
      span:first-child {
        width: 40%;
        color: #888;
      }
      span:last-child {
        width: 60%;
      }
    }
  }
  .ant-btn {
    margin-top: 30px;
  }
}
</style>
