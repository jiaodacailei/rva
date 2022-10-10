<template>
  <div class="upload-file" style="height: 250px;">
    <el-upload v-if="!videoUrl"
               :action="uploadFileUrl"
               :before-upload="handleBeforeUpload"
               :file-list="fileList"
               :limit="limit"
               :on-error="handleUploadError"
               :on-success="handleUploadSuccess"
               :show-file-list="false"
               :headers="headers"
               ref="upload"
               :on-remove="handleRemove"
               :on-exceed="handleExceed"
               :listType="listType"
    >
      <!-- 上传按钮 -->

      <!-- 上传提示 -->
      <div class="el-upload__tip" slot="tip" v-if="showTip">
        请上传
        <template v-if="fileSize"> 大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b></template>
        <template v-if="fileType"> 格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b></template>
        的文件
      </div>
      <!-- 上传按钮 -->

      <div class="box-btn">
        <el-button size="mini" type="primary">选取文件</el-button>
      </div>

    </el-upload>

    <div v-if="videoUrl" class="video-box">
      <i class="el-icon-close close" @click="deletetVideo"></i>

      <video-player  :height="200"
        :src="videoUrl"
      />

    </div>


  </div>
</template>

<script>
import {getToken} from "@/utils/auth";
import {getFileUrl, getFileName} from "@/utils/ruoyi";
import VideoPlayer from '@/components/VideoPlayer'


export default {
  components: {
    VideoPlayer
  },
  name: "VideoUpload",
  props: {
    // 值
    value: [String, Object, Array],
    // 数量限制
    limit: {
      type: Number,
      default: 5,
    },
    // 大小限制(MB)
    fileSize: {
      type: Number,
      default: 20,
    },
    // 文件类型, 例如['png', 'jpg', 'jpeg']
    fileType: {
      type: Array,
      default: () => ["mp4", "avi", "rmvb"],
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

  },
  data() {
    return {
      baseUrl: process.env.VUE_APP_BASE_API, uploadFileUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传的图片服务器地址
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      fileList: [],
      tempValue: this.value,

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
          this.fileList = list.map(item => {
            if (typeof item === "string") {
              item = {name: item, url: getFileUrl(item)};
            }
            item.uid = item.uid || new Date().getTime() + temp++;
            return item;
          });
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
    videoUrl() {
      console.log('videoUrl==', getFileUrl(this.tempValue))
      return getFileUrl(this.tempValue);
    }
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
      this.$message.error("上传失败, 请重试");
    },
    // 上传成功回调
    handleUploadSuccess(res, file) {
      console.log(" baseUrl", this.baseUrl)
      console.log(" res", res)
      console.log(" file", file);

      this.$message.success("上传成功");
      this.fileList.push({name: res.fileName, url: getFileUrl(res.fileName)});
      console.log("handleUploadSuccess", JSON.stringify(this.listToString(this.fileList)))
      this.tempValue = res.fileName;
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
    },
    deletetVideo() {
      this.tempValue = '';
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


.close {
  position: relative;
  left: 200px;
  top: 20px;
  //width: 20px;
  //height: 20px;

}

.box-btn {
  border: 1px dashed #c0ccda;
  border-radius: 6px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  width: 148px;
  height: 148px;
  cursor: pointer;
  line-height: 146px;
  vertical-align: top;
}

.video-box {

  width: 200px;
  height: 200px;
}

</style>
