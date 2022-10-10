<template>
  <div class="upload-file">

    <el-upload :disabled="readOnly"
               :action="uploadFileUrl"
               :before-upload="handleBeforeUpload"
               :file-list="fileList"
               :limit="limit"
               :on-error="handleUploadError"
               :on-success="handleUploadSuccess"
               :show-file-list="showList"
               :headers="headers"
               class="upload-file-uploader"
               ref="upload"
               :on-remove="handleRemove"
               :on-exceed="handleExceed"
               :listType="listType"
               v-if="!readOnly"
    >

      <!-- 上传按钮 -->
      <el-button size="mini" type="primary">选取文件</el-button>
      <!-- 上传提示 -->
      <div class="el-upload__tip" slot="tip" v-if="!readOnly && showTip">
        请上传
        <template v-if="fileSize"> 大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b></template>
        <template v-if="fileType"> 格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b></template>
        的文件

      </div>

    </el-upload>

    <div class="demo-image__preview" v-if="listType=='picture-card' && fileList.length > 0">
      <el-image
        style="width: 100px; height: 100px"
        :src="srcList[0]"
        :preview-src-list="srcList">
      </el-image>
    </div>


    <!-- 文件列表 -->
    <!--    <transition-group v-if="showList" class="upload-file-list el-upload-list el-upload-list&#45;&#45;text" name="el-fade-in-linear" tag="ul">-->
    <!--      <li :key="file.uid" class="el-upload-list__item ele-upload-list__item-content" v-for="(file, index) in fileList">-->
    <!--        <el-link :href="`${baseUrl}${file.url}`" :underline="false" target="_blank">-->
    <!--          <span class="el-icon-document"> {{ getFileName(file.name) }} </span>-->
    <!--        </el-link>-->
    <!--        <div class="ele-upload-list__item-content-action">-->
    <!--          <el-link :underline="false" @click="handleDelete(index)" type="danger">删除</el-link>-->
    <!--        </div>-->
    <!--      </li>-->
    <!--    </transition-group>-->
  </div>
</template>

<script>
import {getToken} from "@/utils/auth";
import {getFileUrl, getFileName} from "@/utils/ruoyi";

export default {
  name: "FileUpload",
  props: {
    // 值
    value: [String, Object, Array],
    // 数量限制
    limit: {
      type: Number,
      default: 15,
    },
    // 大小限制(MB)
    fileSize: {
      type: Number,
      default: 15,
    },
    // 文件类型, 例如['png', 'jpg', 'jpeg']
    fileType: {
      type: Array,
      default: () => ["doc", "xls", "ppt", "txt", "pdf", "png", "jpg", "jpeg", "mp4"],
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true
    },
    //是否显示文件列表
    showList: {
      type: Boolean,
      default: true
    },
    listType: {
      type: String,
      default: 'text' // text,picture,picture-card
    },
    readOnly: {
      type: Boolean,
      default: false
    }

  },
  data() {
    return {
      baseUrl: process.env.VUE_APP_BASE_API, uploadFileUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传的图片服务器地址
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      fileList: [],
      srcList: []
    };
  },
  watch: {
    value: {
      handler(val) {
        console.log('value', JSON.stringify(val))
        if (val) {
          let temp = 1;
          // 首先将值转为数组
          const list = Array.isArray(val) ? val : this.value.split(',');

          console.log('list==', JSON.stringify(list))
          // 然后将数组转为对象数组
          let array = [];
          this.fileList = list.map(item => {
            array.push(getFileUrl(item))
            if (typeof item === "string") {
              item = {name: item, url: getFileUrl(item)};
            }
            item.uid = item.uid || new Date().getTime() + temp++;
            return item;
          });

          console.log("array====", array)
          this.srcList = array;

        } else {
          this.fileList = [];
          return [];
        }
      },
      deep: true,
      immediate: true
    },
  },
  computed: {
    // 是否显示提示
    showTip() {
      return this.isShowTip && (this.fileType || this.fileSize);
    },
  },
  methods: {
    // 上传前校检格式和大小
    handleBeforeUpload(file) {
      // 校检文件类型
      if (this.fileType) {
        let fileExtension = "";
        if (file.name.lastIndexOf(".") > -1) {
          fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
        }
        const isTypeOk = this.fileType.some((type) => {
          if (file.type.indexOf(type) > -1) return true;
          if (fileExtension && fileExtension.indexOf(type) > -1) return true;
          return false;
        });
        if (!isTypeOk) {
          this.$message.error(`文件格式不正确, 请上传${this.fileType.join("/")}格式文件!`);
          return false;
        }
      }
      // 校检文件大小
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize;
        if (!isLt) {
          this.$message.error(`上传文件大小不能超过 ${this.fileSize} MB!`);
          return false;
        }
      }
      return true;
    },
    // 文件个数超出
    handleExceed(file, fileList) {
      this.$message.error(`上传文件数量不能超过 ${this.limit} 个!`);
    },
    // 上传失败
    handleUploadError(err) {
      alert(JSON.stringify(err))
      this.$message.error("上传失败, 请重试");
    },
    // 上传成功回调
    handleUploadSuccess(res, file) {
      console.log(" baseUrl", this.baseUrl)
      console.log(" res", res)
      console.log(" file", file)
      let code = res.code
      if ('200' != code) {
        this.handleUploadError(res.msg)
        return
      }
      this.$message.success("上传成功");
      this.srcList.push(getFileUrl(res.fileName))
      this.fileList.push({name: res.fileName, url: getFileUrl(res.fileName)});
      console.log("handleUploadSuccess fileList", JSON.stringify(this.fileList))
      console.log("handleUploadSuccess", JSON.stringify(this.listToString(this.fileList)))
      this.$emit("input", this.listToString(this.fileList));
    },
    // 删除文件
    handleDelete(index) {
      this.fileList.splice(index, 1);
      this.$emit("input", this.listToString(this.fileList));
    },
    // 获取文件名称
    getFileName(name) {
      if (name.lastIndexOf("/") > -1) {
        return name.slice(name.lastIndexOf("/") + 1).toLowerCase();
      } else {
        return "";
      }
    },
    handleRemove(file, fileList) {
      this.fileList = fileList;
      this.$emit("input", this.listToString(fileList));
    },
    // 对象转成指定字符串分隔
    listToString(list, separator) {
      let strs = "";
      separator = separator || ",";
      for (let i in list) {
        let url = list[i].name;
        strs += getFileName(url) + separator;
      }
      return strs != '' ? strs.substr(0, strs.length - 1) : '';
    }
  }
};
</script>

<style scoped lang="scss">
.upload-file-uploader {
  margin-bottom: 5px;
}

.upload-file-list .el-upload-list__item {
  border: 1px solid #e4e7ed;
  line-height: 2;
  margin-bottom: 10px;
  position: relative;
}

.upload-file-list .ele-upload-list__item-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: inherit;
}

.ele-upload-list__item-content-action .el-link {
  margin-right: 10px;
}
</style>
