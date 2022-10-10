<template>
  <el-card>
    <div class="page">
      <h3 class="title">{{ detail.title }}</h3>
      <el-divider></el-divider>
      <div class="author-box">
        <img :src="avatar" class="avatar"/>
        <div class="info">
          <div><h4 class="name"> {{ authorInfo.nickName }}</h4></div>
          <div>{{ createTime }}</div>
        </div>
      </div>
      <el-divider></el-divider>
      <div style="display: flex; justify-content: center; align-items: center;padding: 20px 0" v-if="videoUrl">
        <video :src="videoUrl" preload="auto" autoplay controls style="width: 100%;height: 600px"></video>
      </div>
      <el-divider></el-divider>
      <div style="display: flex; justify-content: center; align-items: center;padding: 20px 0; flex-wrap: wrap;"
           v-if="imgList&&imgList.length>0">
        <div class="image" v-for="(imgSrc,index) in imgList" :key="imgSrc">
          <el-image :src="imgSrc" lazy></el-image>
        </div>
      </div>
      <el-divider></el-divider>
      <div class="content" v-html="parseHtml( detail.content)"> 内容</div>
    </div>
  </el-card>
</template>

<script>
import {getFileUrl, parseTime} from "@/utils/ruoyi"
import {listCrud} from "@/api/rva/crud";

import {getUser} from "@/api/system/user";

export default {
  props: {
    id: '',
  },
  data() {
    return {
      detail: {},
      authorInfo: {}
    }
  },
  mounted() {
    this.init()
    // this.getAuthorInfo()
  },
  methods: {
    init() {
      let params = {selection: [{keyPropValue: this.id}]}
      listCrud("/rva/view/u2_rva_uniapp_tablist/load/update", params).then(response => {
        let rvaObj = response.data.formData
        let viewId = "u2_rva_uniapp_tablist"
        let detail = {};
        for (const key in rvaObj) {
          console.log(key + ":" + rvaObj[key])
          let newKey = key.substr(viewId.length + 1, key.length)
          detail [newKey] = rvaObj[key];
        }
        this.detail = detail;
        this.getAuthorInfo();
      });
    },
    getAuthorInfo() {
      let userId = this.detail.create_by;
      console.log("getAuthorInfo,userId,", this.detail)
      getUser(userId).then(res => {
        this.authorInfo = res.data;
        console.log("getUser==", res)
      })
    },
    parseHtml(html) {
      var el = document.createElement('html');
      el.innerHTML = html;

      function each(e) {
        e.style['width'] = '100%'
      }

      Array.from(el.getElementsByTagName('img')).forEach(each);
      return el.getElementsByTagName('body')[0].innerHTML;
      return html
    }
  },
  computed: {
    imgList() {
      let str = this.detail.image;
      if (str) {
        let array = str.split(",");
        let len = array.length;
        for (let i = 0; i < len; i++) {
          array [i] = getFileUrl(array[i])
        }
        return array;
      }
    },
    videoUrl() {
      if (this.detail.video) {
        return getFileUrl(this.detail.video)
      }
    },
    avatar() {
      const user = this.authorInfo
      let sex = user.sex;
      let avatar = user.avatar
      if (!avatar) {
        if (sex == '0') {
          avatar = require("@/assets/images/head-male.jpeg")
        } else if (sex == '1') {
          avatar = require("@/assets/images/head-female.jpeg")
        } else {
          avatar = require("@/assets/images/head.jpeg")
        }
      } else {
        avatar = getFileUrl(avatar);
      }
      return avatar;
    },
    createTime() {
      return parseTime(this.detail.create_time)
    }

  }
};
</script>
<style>
.page {
  display: flex;
  justify-content: center;
  align-content: center;
  flex-direction: row;
  flex-wrap: wrap;
  background: #e5eaef;
  padding: 0 20px;
  margin: 0 40% 0 0;

}

.page div {
  width: 100%;
}

.image {
  margin: 15px 0 0 0;
  width: 100%;
}

.avatar {
  height: 120px;
  width: 120px;
  border-radius: 50%;
}

.author-box {
  display: flex;
  flex-wrap: nowrap;
}

.info {
  display: flex;
  flex-direction: column;
  padding: 10px 40px;

}

.title{
  font-size: 36px;
  line-height: 50px;
  padding-top: 35px;
  padding-bottom: 24px;
  text-align: justify;
  font-weight: 600;
  font-family: Microsoft Yahei,微软雅黑,宋体;
  -webkit-font-smoothing: antialiased;
}
.name{
  font-size: 16px;
  line-height: 18px;
  font-weight: 600;
}

</style>
