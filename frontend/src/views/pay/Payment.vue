<template>
  <v-card class="mx-auto" style="margin-top:20px;max-width:360px;padding:20px">
    <div class="mx-auto" style="max-width:360px;text-align:center">
      <h4 style="color:#5E5E5E">아임포트 결제</h4>
      <a-form
        :form="form"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 18 }"
        :colon="false"
        @submit="handleSubmit"
      >
        <a-form-item label="주문번호">
          <a-input
            disabled
            v-decorator="[
            'merchantUid',
            { initialValue: initialMerchantUid },
          ]"
            size="large"
          />
        </a-form-item>
        <a-form-item label="주문명">
          <a-input disabled v-decorator="['name', { initialValue: 'NUGA 포인트충전' }]" size="large" />
        </a-form-item>
        <a-form-item label="결제금액">
          <a-input v-decorator="['amount', { initialValue: '1000' }]" size="large" type="number" />
        </a-form-item>
        <a-form-item label="이메일">
          <a-input
            v-decorator="['buyerEmail', { initialValue: 'example@example.com' }]"
            size="large"
          />
        </a-form-item>
        <a-button style="margin:10px" size="large" @click="handleGoBack">뒤로가기</a-button>
        <a-button
          style="margin:10px;background-color:#fab696;border:none"
          html-type="submit"
          size="large"
        >결제하기</a-button>
      </a-form>
    </div>
  </v-card>
</template>

<script>
import { mapState } from "vuex";
export default {
  computed: {
    ...mapState(["userInfo"]),
  },
  data() {
    return {
      formLayout: "horizontal",
      form: this.$form.createForm(this, { name: "payment" }),
      initialMerchantUid: `mid_${new Date().getTime()}`,
    };
  },
  created: function () {
    this.$store.state.isRefill = true;
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          const { merchantUid, name, amount, buyerEmail } = values;
          const { IMP } = window;
          IMP.init("imp10391932");
          const data = {
            merchant_uid: merchantUid,
            name,
            amount,
            buyer_email: buyerEmail,
          };
          IMP.request_pay(data, this.callback);
        }
      });
    },
    handleGoBack() {
      this.$router.push("/");
    },
    callback(response) {
      // 본인인증 종료 후 result 페이지로 이동
      const query = {
        ...response,
        type: "payment",
      };
      this.$router.push({ path: "/result", query });
    },
  },
};
</script>
