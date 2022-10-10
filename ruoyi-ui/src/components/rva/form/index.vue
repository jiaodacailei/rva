<template>
  <el-form :ref="formRef" :model="formData" label-width="80px" :label-position="labelPosition">

    <template v-if="cruds.length && uiType == 'tabs'">
      <el-tabs style="margin-bottom: 10px" tab-position="top">
        <el-tab-pane label="基本信息">
          <el-row v-for="(row1, row1Index) in rowsPropData" :key="row1Index">
            <el-col v-for="(col1, col1Index) in row1.cols" :span="col1.colSpan" :key="row1Index * 100 + col1Index">
              <el-row v-for="(row2, row2Index) in col1.rows" :key="row1Index * 10000 + col1Index * 100 + row2Index">
                <el-col v-for="(col2, col2Index) in row2.cols" :span="col2.span"
                        :key="row1Index * 1000000 + col1Index * 10000 + row2Index * 100 + col2Index">
                  <component :is="'rva-form-' + col2.prop.type" v-model="formData[col2.prop.id]" :prop="col2.prop"
                             :info="info"
                             :viewData="viewData" @rva-trigger="triggerView"
                             @changeFormRowsPropData="changeFormRowsPropData"
                             :formData="formData" :ref="col2.prop.id" :requestParams="requestParams">
                  </component>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane v-for="prop in cruds" :label="prop.name" :key="prop.id">
          <rva-form-crud v-model="formData[prop.id]" :prop="prop" :info="info" :viewData="viewData"
                         :formData="formData" :ref="prop.id" :requestParams="requestParams" :hideLabel="true"
          ></rva-form-crud>
        </el-tab-pane>
      </el-tabs>
    </template>
    <template v-else>
      <el-row v-for="(row1, row1Index) in rowsPropData" :key="row1Index">
        <el-col v-for="(col1, col1Index) in row1.cols" :span="col1.colSpan" :key="row1Index * 100 + col1Index">
          <el-row v-for="(row2, row2Index) in col1.rows" :key="row1Index * 10000 + col1Index * 100 + row2Index">
            <el-col v-for="(col2, col2Index) in row2.cols" :span="col2.span"
                    :key="row1Index * 1000000 + col1Index * 10000 + row2Index * 100 + col2Index">
              <component :is="'rva-form-' + col2.prop.type" v-model="formData[col2.prop.id]" :prop="col2.prop"
                         :info="info" :viewData="viewData" @rvaReloadView="reloadView"
                         @changeFormRowsPropData="changeFormRowsPropData"
                         :formData="formData" :ref="col2.prop.id" :requestParams="requestParams"
                         @rva-trigger="triggerView">
              </component>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </template>
    <el-row v-if="!info">
      <rva-form-button v-for="btn in viewData.buttons" :key="btn.id" :viewButtonData="btn" :view-data="viewData"
                       :formData="formData" :formRef="getFormRef()"
                       @rva-button-click="handleButtonClick" :requestParams="requestParams"></rva-form-button>
    </el-row>
    <template v-if="viewData.logShow && formData.logs && formData.logs.length">
      <el-divider></el-divider>
      <el-card header="日志">
        <el-timeline>
          <el-timeline-item v-for="log in formData.logs" :timestamp="log.name" placement="top" :key="log.id">
            <el-card>
              <rva-form :view="log.view" :form="log.form" :requestParams="requestParams" :info="true"></rva-form>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </template>

  </el-form>
</template>

<script>
import RvaFormTextfield from "@/components/rva/form/textfield";
import RvaFormNumberfield from "@/components/rva/form/numberfield";
import RvaFormTextarea from "@/components/rva/form/textarea";
import RvaFormTextarray from "@/components/rva/form/textarray";
import RvaFormButton from "@/components/rva/form/button";
import RvaFormSelector from "@/components/rva/form/selector";
import RvaFormObjectselector from "@/components/rva/form/objectselector";
import RvaFormRadio from "@/components/rva/form/radio";
import RvaFormSelect from "@/components/rva/form/select";
import RvaFormCascader from "@/components/rva/form/cascader";
import RvaFormCheckbox from "@/components/rva/form/checkbox";
import RvaFormCrud from "@/components/rva/form/crud";
import RvaFormHtmlEditor from "@/components/rva/form/htmleditor";
import RvaFormFile from "@/components/rva/form/file";
import RvaFormTimePicker from "@/components/rva/form/timepicker";
import RvaFormDate from "@/components/rva/form/date";
import RvaFormDateTime from "@/components/rva/form/datetime";
import RvaFormSwitch from "@/components/rva/form/switch";
import RvaFormSlider from "@/components/rva/form/slider";
import RvaFormRate from "@/components/rva/form/rate";
import RvaFormTransfer from "@/components/rva/form/transfer";
import RvaFormImage from "@/components/rva/form/image";
import RvaFormMenuTree from "@/components/rva/form/menutree";
import RvaFormColorPicker from "@/components/rva/form/colorpicker";
import RvaFormRanges from "@/components/rva/form/ranges";
import RvaFormVideo from "@/components/rva/form/video";
import RvaFormMneuIcon from "@/components/rva/form/menuicon";
import RvaFormIcon from "@/components/rva/form/icon";
import RvaFormImageCropper from "@/components/rva/form/imagecropper"
import RvaFormDeptTree from "@/components/rva/form/depttree"

import {createRowsPropData, handleRvaButtonClick, getRvaViewData} from "@/api/rva/crud";
import {RvaUtils} from "@/api/rva/util";
import {removeDictionaryCache} from "@/api/rva/dict";

import RvaFormConfig from "@/components/rva/form/config";
import RvaButtonConfig from "@/components/rva/button/config";
import Vue from "vue";


Vue.component('rva-form-config', RvaFormConfig)
Vue.component('rva-button-config', RvaButtonConfig)

export default {
  name: "rva-form",
  props: ['viewId', 'requestParams', 'info', 'view', 'form'],
  components: {
    "rva-form-button": RvaFormButton,
    "rva-form-textfield": RvaFormTextfield,
    "rva-form-numberfield": RvaFormNumberfield,
    "rva-form-textarea": RvaFormTextarea,
    "rva-form-selector": RvaFormSelector,
    "rva-form-objectselector": RvaFormObjectselector,
    "rva-form-radio": RvaFormRadio,
    "rva-form-select": RvaFormSelect,
    "rva-form-checkbox": RvaFormCheckbox,
    "rva-form-crud": RvaFormCrud,
    "rva-form-htmleditor": RvaFormHtmlEditor,
    "rva-form-file": RvaFormFile,
    "rva-form-timepicker": RvaFormTimePicker,
    "rva-form-date": RvaFormDate,
    "rva-form-datetime": RvaFormDateTime,
    "rva-form-switch": RvaFormSwitch,
    "rva-form-slider": RvaFormSlider,
    "rva-form-rate": RvaFormRate,
    "rva-form-transfer": RvaFormTransfer,
    "rva-form-menuicon": RvaFormMneuIcon,
    "rva-form-icon": RvaFormIcon,
    "rva-form-image": RvaFormImage,
    "rva-form-menutree": RvaFormMenuTree,
    "rva-form-colorpicker": RvaFormColorPicker,
    "rva-form-imagecropper": RvaFormImageCropper,
    "rva-form-depttree": RvaFormDeptTree,
    RvaFormRanges,
    RvaFormVideo,
    RvaFormTextarray, RvaFormCascader
  },
  data() {
    return {
      rowsPropData: [],
      cruds: [],
      formRef: 'form-' + new Date().getTime(),
      originalFormData: {},
      formDataOld: {},
      formData: {},
      viewData: {},
      uiType: 'form',
      componentKey: 0
    };
  },
  created() {
    if (this.view && this.form) {
      this.viewData = this.view;
      this.formData = this.form;
      this.init();
    } else {
      // 设置this.formData/this.viewData
      getRvaViewData(this.viewId, this.requestParams, this, (data, code) => {
        if (code != -1) {
          this.init();
        }
      })
    }
  },
  mounted() {
    console.log('form mounted...', this.formRef);
    // RvaUtils.trigger(this.formData, undefined, this.viewData, this.$refs);
    // this.formDataOld = RvaUtils.cloneDeep(this.formData);
  },
  methods: {
    init() {
      console.log('form index this.viewData', this.viewData);
      if (this.viewData.objId == 'none' && !this.viewData.loadUrl) {// 未关联数据表的视图，需要初始化一下formData
        console.log('-----------', this)
        this.viewData.properties.forEach(p => {
          if (RvaUtils.isEmpty(p.formInitValue)) {
            return;
          }
          this.formData[p.id] = RvaUtils.parseJsValue(p.formInitValue, {...this.requestParams});
          this.formData = {...this.formData}
        })
      }
      RvaUtils.clone(this.formData, this.originalFormData, true);
      this.rowsPropData = createRowsPropData(this.viewData);
      this.uiType = RvaUtils.parseValue(this.viewData.data, 'uiType', 'form');
      this.cruds = this.viewData.properties.filter(p => (p.type == 'crud'));
      //console.log('this.rowsPropData', this.rowsPropData);
      this.$emit('rva-form-width', RvaUtils.getDialogWidth(this.viewData.width));
      this.$emit('rva-meta', this.viewData);
    },
    reloadView() {
      this.rowsPropData.push({})
      this.rowsPropData.pop()
    },
    changeFormRowsPropData(way, prop) {
      for (let i = 0; i < this.viewData.properties.length; i++) {
        let p = this.viewData.properties[i]
        if (p.id == prop.id) {
          if (way == 'hide') {
            p.hide = true
          } else {
            p.hide = false;
          }
        }
      }
      this.rowsPropData = createRowsPropData(this.viewData)
    },
    resetView(action) {// handleRvaButtonClick中会调用
      RvaUtils.clone(this.originalFormData, this.formData);
      this.resetForm(this.formRef);
    },
    goBack(action) {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.go(-1);
    },
    reload(action) {
      this.$router.go(0);
    },
    removeDictionaryCache(action) {
      removeDictionaryCache(action.request.dict_type);
    },
    handleButtonClick(data) {
      handleRvaButtonClick(this, data);
    },
    getFormRef() {
      return this.$refs[this.formRef];
    },
    triggerView(propId) {
      RvaUtils.trigger(this.formData, propId, this.viewData, this.$refs);
    }
  },
  computed: {
    labelPosition() {
      return RvaUtils.isPC() ? 'right' : 'top'
    }
  },
  watch: {
    formData: {
      handler(val, valOld) {
        console.log('RvaUtils.trigger', val == valOld, JSON.stringify(val), JSON.stringify(valOld));
        if (JSON.stringify(val) == JSON.stringify(this.formDataOld)) {
          return;
        }
        this.$nextTick(() => {
          RvaUtils.trigger(val, this.formDataOld, this.viewData, this.$refs);
          RvaUtils.clone(val, this.formDataOld, true);
        })
      },
      deep: true
    },
    rowsPropData: {

      handler(nVal, oldVal) {
        console.log('rowsPropData watch nVal', nVal);
      }
    }
  }
};
</script>

