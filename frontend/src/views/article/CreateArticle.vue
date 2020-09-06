<template>
  <div style="margin:20px auto;max-width:360px" id="createarticle">
    <h4 style="color:#5E5E5E">상품등록하기</h4>
    <br />
    <v-form ref="form" v-model="valid" lazy-validation>
      <v-text-field
        v-model="articleInfo.articleRequest.title"
        :counter="30"
        :rules="nameRules"
        label="글 제목"
        required
        solo
      ></v-text-field>
      <v-text-field
        oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');"
        step="100"
        type="number"
        prefix="￦"
        :rules="startPriceRules"
        v-model.number="articleInfo.articleRequest.price"
        label="경매 시작가"
        required
        solo
        onkeydown="javascript: return event.keyCode == 69 ? false : true"
      ></v-text-field>
      <v-text-field
        solo
        oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');"
        step="100"
        type="number"
        prefix="￦"
        :rules="immediatePriceRules"
        v-model.number="articleInfo.articleRequest.immediatePrice"
        label="즉시 구매가"
        required
        onkeydown="javascript: return event.keyCode == 69 ? false : true"
      ></v-text-field>
      <v-select
        solo
        v-model="articleInfo.articleRequest.minUnit"
        prefix="￦"
        :rules="minUnitPriceRules"
        label="입찰단위"
        required
        v-if="this.articleInfo.articleRequest.immediatePrice < 100000"
        :items="unitPrices1"
      ></v-select>

      <v-select
        solo
        v-model="articleInfo.articleRequest.minUnit"
        prefix="￦"
        :rules="minUnitPriceRules"
        label="입찰단위"
        required
        v-if="this.articleInfo.articleRequest.immediatePrice >= 100000 && this.articleInfo.articleRequest.immediatePrice<1000000"
        :items="unitPrices2"
      ></v-select>
      <v-select
        solo
        v-model="articleInfo.articleRequest.minUnit"
        prefix="￦"
        :rules="minUnitPriceRules"
        label="입찰단위"
        required
        v-if="this.articleInfo.articleRequest.immediatePrice >= 1000000"
        :items="unitPrices3"
      ></v-select>
      <v-row>
        <v-col cols="12" sm="12" md="12">
          <v-menu
            ref="menu"
            v-model="menu"
            :close-on-content-click="false"
            :return-value.sync="date"
            transition="scale-transition"
            offset-y
            width="360px"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="articleInfo.articleRequest.purchaseDate"
                solo
                label="구매일자를 선택하세요."
                readonly
                v-bind="attrs"
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
              width="360px"
              v-model="articleInfo.articleRequest.purchaseDate"
              no-title
              scrollable
            >
              <v-spacer></v-spacer>
              <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
              <v-btn text color="primary" @click="$refs.menu.save(date)">OK</v-btn>
            </v-date-picker>
          </v-menu>
        </v-col>
      </v-row>

      <v-select
        solo
        v-model="articleInfo.categoryName"
        :items="categories"
        :rules="[v => !!v || '카테고리를 선택해주세요!']"
        label="카테고리"
        required
      ></v-select>
      <v-select
        solo
        v-model.number="articleInfo.articleRequest.state"
        :items="conditions"
        :rules="[v => !!v || '제품 상태는 어떠한가요?']"
        label="제품 상태"
        required
      ></v-select>
      <v-select
        solo
        v-model="articleInfo.articleRequest.expireDate"
        :items="expiredateoptions"
        :rules="[v => !!v || '경매를 몇일 후에 마감하시겠습니까?']"
        label="경매 마감일"
        required
      ></v-select>
      <br />
      <v-textarea
        v-model="articleInfo.articleRequest.description"
        outlined
        name="input-7-4"
        label="제품 상세 설명"
      ></v-textarea>
      <form enctype="multipart/form-data">
        <div class="text-center">
          <label for="file-input">
            <v-icon style="font-size:60px">add_a_photo</v-icon>
          </label>
          <input
            id="file-input"
            style="display:none;"
            multiple
            type="file"
            ref="images"
            @change="imageSelection"
          />

          <!-- <v-btn type="button" @click="onClickImageUpload">이미지 업로드</v-btn> -->
          <div class="image-preview-container">
            <div
              v-for="(image, index) in files"
              :index="index"
              :key="index"
              class="image-preview-wrapper"
              style="padding:2px;"
            >
              <v-icon @click="imageDelete" :name="image.number">mdi-close-box</v-icon>
              <img style="padding:2px;height:80px;width:80px" :src="image.preview" />
            </div>
          </div>
        </div>
      </form>

      <div>
        <v-row style>
          <v-text-field label="해시태그를 입력해주세요!" prefix="#" v-model="inputTag" @keyup.enter="addTag()"></v-text-field>
          <v-icon @click="addTag()">mdi-plus-circle</v-icon>
        </v-row>
        <v-chip
          class="mx-1 my-1"
          v-for="tag in this.articleInfo.tagRequests"
          close
          dark
          color="#fec771"
          @click:close="deleteTag(tag)"
          :key="tag"
        ># {{ tag }}</v-chip>
        <div v-if="overTag">해시태그는 최대 5개까지 입력 가능합니다.</div>
        <div
          style="color:#E77979;margin-bottom:20px;"
          v-if="emptyTag"
        >해시태그를 입력하시면 게시물이 더 자주 노출됩니다 :)</div>
      </div>
      <div style="margin-top;20px" class="text-center">
        <v-btn
          style="width:80%;margin-top;20px"
          depressed
          large
          color="#fab696"
          @click="createArticle(articleInfo)"
        >작성완료</v-btn>
      </div>
    </v-form>
  </div>
</template>

<script>
// import { mapActions } from "vuex";
import firebase from "firebase";
import swal from "sweetalert";
export default {
  name: "CreateArticle",
  data() {
    return {
      articleInfo: {
        articleRequest: {
          price: Number,
          title: "",
          // minUnit: null, // Number로하게되면 step옵션으로 최소단위 가능
          purchaseDate: null,
          state: Number, //1~5까지 상태표시
          description: "",
          expireDate: null, //옵션으로 3,5,7일 후만 가능하도록!
          // createdDate: null,
          immediatePrice: null,
          minUnit: null,
          // images: [
          //   {
          //     image: null,
          //   },
          // ],
          // image:null,
        },
        tagRequests: [
          // {
          //   tagName: "태그1",
          // },
        ],
        categoryName: "", //option 태그로 가져와서 사용하기
        images: [
          // {
          //   image: "",
          // }
        ],
        // uploadValue: 0,
      },
      imagePath: [
        {
          path: null,
        },
      ],
      isSelected: 0,
      valid: true,
      name: "",
      nameRules: [
        (v) => !!v || "제목은 필수사항입니다.",
        (v) => (v && v.length <= 30) || "제목은 30자 이내로 작성하세요.",
      ],
      startPriceRules: [
        (v) => (v && v > 0) || "경매 시작가 설정은 필수사항입니다.",
      ],
      immediatePriceRules: [
        (v) => (v && v > 999) || "즉시 구매가는 1000원 이상으로 설정하세요.",
        (v) =>
          (v && v > this.articleInfo.articleRequest.price) ||
          "즉시 구매가는 경매 시작가보다 높은 금액으로 설정하세요.",
        (v) => !!v || "가격설정은 필수입니다.",
      ],

      minUnitPriceRules: [(v) => !!v || "입찰단위 설정은 필수사항입니다."],
      // selectcategory: null,
      // selectexpiredate: null,
      expiredateoptions: ["1일후", "3일후", "5일후", "7일후"],
      categories: [
        "디지털",
        "게임",
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
      conditions: [
        "S ( 새상품/미개봉상품 )",
        "A ( 사용감이 거의 없어요 )",
        "B ( 사용감은 있지만 상태가 매우 양호해요 )",
        "C ( 그럭저럭 쓸만해요 )",
        "D ( 사용한 흔적이 많아요ㅠㅠ )",
      ],
      unitPrices1: [100, 200, 300, 400, 500, 600, 700, 800, 900, 1000],
      unitPrices2: [1000, 2000, 3000, 4000, 5000],
      unitPrices3: [5000, 6000, 7000, 8000, 9000, 10000],

      date: new Date().toISOString().substr(0, 10),
      menu: false,
      inputTag: "",
      emptyTag: true,
      overTag: false,

      files: [],
      uploadImageIndex: 0,
    };
  },
  methods: {
    // ...mapActions(["createArticle"]),
    createArticle(articleInfo) {
      if (
        !!this.articleInfo.articleRequest.title &&
        !!this.articleInfo.articleRequest.price &&
        !!this.articleInfo.articleRequest.immediatePrice &&
        !!this.articleInfo.articleRequest.minUnit &&
        !!this.articleInfo.articleRequest.purchaseDate &&
        !!this.articleInfo.categoryName &&
        !!this.articleInfo.articleRequest.state &&
        !!this.articleInfo.articleRequest.expireDate &&
        !!this.articleInfo.articleRequest.description &&
        !!this.files[0]
      ) {
        this.$store.dispatch("createArticle", articleInfo);
      } else {
        swal("게시물 작성 실패", "모든 항목은 필수입니다", "error");
      }
    },
    onClickImageUpload() {
      for (var i = 0; i < this.files.length; i++) {
        var today = new Date();
        const storageRef = firebase
          .storage()
          .ref(`${today + "_" + this.files[i].image.name}`)
          .put(this.files[i].image);
        storageRef.on(
          "state_changed",
          (snapshot) => {
            this.uploadValue =
              (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
          },
          (error) => {
            console.log(error.message);
          },
          () => {
            this.uploadValue = 100;
            storageRef.snapshot.ref.getDownloadURL().then((url) => {
              this.articleInfo.images.push(url);
            });
          }
        );
      }
    },
    changeStatus() {
      this.isSelected = 0;
    },
    addTag() {
      if (this.inputTag === "") {
        return;
      }
      for (var i = 0; i < this.articleInfo.tagRequests.length; i++) {
        if (this.inputTag === this.articleInfo.tagRequests[i]) {
          this.inputTag = "";
          return;
        }
      }
      if (this.articleInfo.tagRequests.length > 4) {
        return (this.overTag = true), (this.inputTag = "");
      }
      this.articleInfo.tagRequests.push(this.inputTag);
      this.inputTag = "";
      this.emptyTag = false;
    },
    deleteTag(tag) {
      this.articleInfo.tagRequests.splice(
        this.articleInfo.tagRequests.indexOf(tag),
        1
      );
      if (this.articleInfo.tagRequests.length === 0) {
        this.emptyTag = true;
      }
      this.overTag = false;
    },
    imageSelection() {
      let num = -1;
      for (let i = 0; i < this.$refs.images.files.length; i++) {
        this.files = [
          ...this.files,
          {
            image: this.$refs.images.files[i],
            preview: URL.createObjectURL(this.$refs.images.files[i]),
            number: i + this.uploadImageIndex,
          },
        ];
        num = i;
      }
      this.uploadImageIndex =
        this.uploadImageIndex + num + (1).then(this.onClickImageUpload());
    },
    imageDelete(e) {
      const name = e.target.getAttribute("name");
      this.files = this.files.filter((data) => data.number !== Number(name));
    },
  },
  watch: {
    isSelected: function () {
      // this.isSelected = 0;
    },
  },
};
</script>

<style>
.image-preview-wrapper {
  padding: 10px;
  position: relative;
}

.image-preview-wrapper > img {
  position: relative;
  width: 200px;
  height: 200px;
  z-index: 10;
}

.image-preview-container {
  height: 100%;
  display: flex;
  flex-wrap: wrap;
}
</style>