<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <div>
        <el-row>
          <el-col :xs="24" :md="12" :style="{height: '350px'}">
            <vue-cropper
              ref="cropper"
              :img="options.img"
              :info="true"
              :autoCrop="options.autoCrop"
              :autoCropWidth="options.autoCropWidth"
              :autoCropHeight="options.autoCropHeight"
              :fixedBox="options.fixedBox"
              @realTime="realTime"

            />
          </el-col>
          <el-col :xs="24" :md="12" :style="{height: '350px'}">
            <!--avatar-upload-preview -->
            <div class="upload-preview">
              <img :src="previews.url" :style="previews.img"/>
            </div>
          </el-col>
        </el-row>
        <br/>
        <el-row>
          <el-col :lg="2" :md="2">
            <el-upload action="#" :http-request="requestUpload" :show-file-list="false" :before-upload="beforeUpload">
              <el-button size="small">
                选择
                <i class="el-icon-upload el-icon--right"></i>
              </el-button>
            </el-upload>
          </el-col>
          <el-col :lg="{span: 1, offset: 2}" :md="2">
            <el-button icon="el-icon-plus" size="small" @click="changeScale(1)"></el-button>
          </el-col>
          <el-col :lg="{span: 1, offset: 1}" :md="2">
            <el-button icon="el-icon-minus" size="small" @click="changeScale(-1)"></el-button>
          </el-col>
          <el-col :lg="{span: 1, offset: 1}" :md="2">
            <el-button icon="el-icon-refresh-left" size="small" @click="rotateLeft()"></el-button>
          </el-col>
          <el-col :lg="{span: 1, offset: 1}" :md="2">
            <el-button icon="el-icon-refresh-right" size="small" @click="rotateRight()"></el-button>
          </el-col>
          <el-col :lg="{span: 2, offset: 6}" :md="2">

            <el-button type="primary" size="small" @click="uploadImg()">确认上传</el-button>
          </el-col>
        </el-row>
      </div>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
  </div>
</template>

<script>
import {VueCropper} from "vue-cropper";
import formInput from "@/api/rva/form-input";
import {upload} from "@/api/system/file-upload"
import {RvaUtils} from "@/api/rva/util";
import {getFileUrl} from "@/utils/ruoyi"

export default {
  mixins: [formInput],
  components: {VueCropper},
  data() {
    return {
      // 是否显示弹出层
      open: false,
      // 是否显示cropper
      visible: false,
      // 弹出层标题
      title: "修改头像",
      options: {
        img: '', //裁剪图片的地址
        autoCrop: true, // 是否默认生成截图框
        autoCropWidth: 200, // 默认生成截图框宽度
        autoCropHeight: 200, // 默认生成截图框高度
        fixedBox: false // 固定截图框大小 不允许改变
      },
      previews: {},
    };
  },
  methods: {
    afterCreated() {
      this.max = RvaUtils.parseInt(this.prop.formValueMax, 20);
      this.initOptions()
    },
    initOptions() {
      this.options.img = getFileUrl(this.value)
      let autoCropWidth = RvaUtils.parseValue(this.prop.data, 'autoCropWidth')
      let autoCropHeight = RvaUtils.parseValue(this.prop.data, 'autoCropHeight')
      if (autoCropWidth && autoCropHeight) {
        this.options.autoCropWidth = autoCropWidth;
        this.options.autoCropHeight = autoCropHeight;
        this.options.fixedBox = true;
      }
    },
    // 编辑头像
    editCropper() {
      this.open = true;
    },
    // 打开弹出层结束时的回调
    modalOpened() {
      this.visible = true;
    },
    // 覆盖默认的上传行为
    requestUpload() {
    },
    // 向左旋转
    rotateLeft() {
      this.$refs.cropper.rotateLeft();
    },
    // 向右旋转
    rotateRight() {
      this.$refs.cropper.rotateRight();
    },
    // 图片缩放
    changeScale(num) {
      num = num || 1;
      this.$refs.cropper.changeScale(num);
    },
    // 上传预处理
    beforeUpload(file) {
      if (file.type.indexOf("image/") == -1) {
        this.$modal.msgError("文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。");
      } else {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          this.options.img = reader.result;
        };
      }
    },
    // 上传图片
    uploadImg() {
      this.$refs.cropper.getCropBlob(data => {
        let formData = new FormData();
        formData.append("file", data);
        upload(formData).then(res => {
          this.open = false;
          this.$message.success("上传成功");
          this.options.img = res.url;
          this.$emit("input", res.fileName);
          this.visible = false;
        });
      });
    },
    // 实时预览
    realTime(data) {
      this.previews = data;
    },
    // 关闭窗口
    closeDialog() {
      this.options.img = store.getters.avatar
      this.visible = false;
    },
  }
}
;
</script>
<style scoped lang="scss">


.upload-preview {
  position: absolute;
  top: 50%;
  -webkit-transform: translate(50%, -50%);
  transform: translate(50%, -50%);
  width: 200px;
  height: 200px;
  -webkit-box-shadow: 0 0 4px #ccc;
  box-shadow: 0 0 4px #ccc;
  overflow: hidden
}

</style>
