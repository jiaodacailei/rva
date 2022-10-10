<template>
  <el-form-item :label="prop.name" :prop="prop.id" :label-width="labelWidth" v-show="showComponent">
    <template slot="label">
      <rva-form-config :hideLabel="hideLabel" :prop="prop" :requestParams="requestParams"></rva-form-config>
    </template>
    <el-button v-if="formDataList.length == 0 && !readonly && !info" size="mini" type="text" @click.stop="addRow()"
               class="el-icon-plus"> 新增
    </el-button>
    <el-card v-else shadow="always">
      <el-collapse v-model="activeCollapseItems">
        <el-collapse-item v-for="(row, rowIndex) in formDataList"
                          :key="prop.id + '_' + rowIndex" :name="rowIndex"
        >
          <template v-if="row.create">
            <template slot="title">
              <el-tag type="danger" style="margin-right: 10px">新</el-tag>
              <el-tag
                v-if="row[prop.relatedCrudViewData.create.viewData.properties[1].id]"
                type="info" style="margin-right: 10px">{{
                  row[prop.relatedCrudViewData.create.viewData.properties[1].id]
                }}
              </el-tag>
              <template v-if="!readonly && !info">
                <el-button size="mini" type="text"
                           @click.stop="addRow(undefined, rowIndex)" class="el-icon-plus">
                  新增
                </el-button>
                <el-button size="mini" type="text" @click.stop="removeRow (rowIndex)" class="el-icon-minus"
                           v-show="showRemove()"> 删除
                </el-button>
              </template>
            </template>
            <el-row v-for="(row1, row1Index) in createRowsPropData(prop.relatedCrudViewData.create.viewData)"
                    :key="rowIndex + '_' + row1Index">
              <el-col v-for="(col1, col1Index) in row1.cols" :span="col1.colSpan"
                      :key="rowIndex + '_' + row1Index + '_' + col1Index">
                <el-row v-for="(row2, row2Index) in col1.rows"
                        :key="rowIndex + '_' + row1Index + '_' + col1Index + '_' + row2Index">
                  <el-col v-for="(col2, col2Index) in row2.cols" :span="col2.span" style="margin-bottom: 20px"
                          :key="rowIndex + '_' + row1Index + '_' + col1Index + '_' + row2Index + '_' + col2Index">
                    <component :is="'rva-form-' + col2.prop.type" v-model="formData[prop.id][rowIndex][col2.prop.id]"
                               :prop="col2.prop" :viewData="prop.relatedCrudViewData.create.viewData"
                               :formData="formData[prop.id][rowIndex]" :ref="col2.prop.id + '_' + rowIndex"
                               :formItemProp="prop.id + '[' + rowIndex + '].' + col2.prop.id"
                               :requestParams="requestParams" :info="getInfo()" @rva-trigger="triggerView(formData[prop.id][rowIndex], $event, rowIndex)">
                    </component>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
          </template>
          <template v-else>
            <template slot="title">
              <el-tag v-if="row.del != 'Y'" type="info" style="margin-right: 10px">
                {{ row[prop.relatedCrudViewData.update.viewData.properties[1].id] }}
              </el-tag>
              <el-badge v-else value="删">
                <el-tag type="info" style="margin-right: 10px">
                  {{ row[prop.relatedCrudViewData.update.viewData.properties[1].id] }}
                </el-tag>
              </el-badge>
              <template v-if="!readonly">
                <el-button size="mini" type="text"
                           @click.stop="addRow(undefined, rowIndex)" class="el-icon-plus">
                  新增
                </el-button>
                <el-button size="mini" type="text" @click.stop="removeRowUpdate(row)" v-show="showRemove()"
                           class="el-icon-minus">
                  {{ row.del == 'Y' ? "取消删除" : "删除" }}
                </el-button>
              </template>
            </template>
            <el-row v-for="(row1, row1Index) in createRowsPropData(prop.relatedCrudViewData.update.viewData)"
                    :key="rowIndex + '_' + row1Index">
              <el-col v-for="(col1, col1Index) in row1.cols" :span="col1.colSpan"
                      :key="rowIndex + '_' + row1Index + '_' + col1Index">
                <el-row v-for="(row2, row2Index) in col1.rows"
                        :key="rowIndex + '_' + row1Index + '_' + col1Index + '_' + row2Index">
                  <el-col v-for="(col2, col2Index) in row2.cols" :span="col2.span" style="margin-bottom: 20px"
                          :key="rowIndex + '_' + row1Index + '_' + col1Index + '_' + row2Index + '_' + col2Index">
                    <component :is="'rva-form-' + col2.prop.type" v-model="formData[prop.id][rowIndex][col2.prop.id]"
                               :prop="col2.prop" :formData="formData[prop.id][rowIndex]"
                               :viewData="prop.relatedCrudViewData.update.viewData" :ref="col2.prop.id + '_' + rowIndex"
                               :formItemProp="prop.id + '[' + rowIndex + '].' + col2.prop.id"
                               :requestParams="requestParams" :info="getInfo()" @rva-trigger="triggerView(formData[prop.id][rowIndex], $event, rowIndex)">
                    </component>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
          </template>
        </el-collapse-item>
      </el-collapse>
    </el-card>
    <template v-if="tip">
      <rva-form-tip :tip="tip"/>
    </template>
  </el-form-item>
</template>

<script>
import RvaFormCascader from "@/components/rva/form/cascader";
import RvaFormTextfield from "@/components/rva/form/textfield";
import RvaFormNumberfield from "@/components/rva/form/numberfield";
import RvaFormTextarea from "@/components/rva/form/textarea";
import RvaFormTextarray from "@/components/rva/form/textarray";
import RvaFormButton from "@/components/rva/form/button";
import RvaFormSelector from "@/components/rva/form/selector";
import RvaFormObjectselector from "@/components/rva/form/objectselector";
import RvaFormRadio from "@/components/rva/form/radio";
import RvaFormSelect from "@/components/rva/form/select";
import RvaFormCheckbox from "@/components/rva/form/checkbox";
import RvaFormHtmlEditor from "@/components/rva/form/htmleditor";
import RvaFormFile from "@/components/rva/form/file";
import RvaFormTimePicker from "@/components/rva/form/timepicker";
import RvaFormDate from "@/components/rva/form/date";
import RvaFormDateTime from "@/components/rva/form/datetime";
import RvaFormSwitch from "@/components/rva/form/switch";
import RvaFormSlider from "@/components/rva/form/slider";
import RvaFormRate from "@/components/rva/form/rate";
import RvaFormTransfer from "@/components/rva/form/transfer";
import RvaFormImageUpload from "@/components/rva/form/image";
import RvaFormMenuTree from "@/components/rva/form/menutree";
import RvaFormColorPicker from "@/components/rva/form/colorpicker";
import {createRowsPropData} from "@/api/rva/crud";
import RvaFormIconSelect from "@/components/rva/form/menuicon";
import RvaFormIconSelect2 from "@/components/rva/form/icon";
import RvaFormRanges from "@/components/rva/form/ranges";
import RvaFormImagecropper from "@/components/rva/form/imagecropper"

import formInput from "@/api/rva/form-input";
import {RvaUtils} from "@/api/rva/util";

import RvaFormConfig from "@/components/rva/form/config";
import Vue from "vue";
import RvaFormVideo from "@/components/rva/form/video";
import RvaFormDeptTree from "@/components/rva/form/depttree";
Vue.component('rva-form-config', RvaFormConfig)

export default {
  mixins: [formInput],
  name: "RvaFormCrud",
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
    "rva-form-htmleditor": RvaFormHtmlEditor,
    "rva-form-file": RvaFormFile,
    "rva-form-timepicker": RvaFormTimePicker,
    "rva-form-date": RvaFormDate,
    "rva-form-datetime": RvaFormDateTime,
    "rva-form-switch": RvaFormSwitch,
    "rva-form-slider": RvaFormSlider,
    "rva-form-rate": RvaFormRate,
    "rva-form-transfer": RvaFormTransfer,
    "rva-form-menuicon": RvaFormIconSelect,
    "rva-form-icon": RvaFormIconSelect2,
    "rva-form-image": RvaFormImageUpload,
    "rva-form-menutree": RvaFormMenuTree,
    "rva-form-colorpicker": RvaFormColorPicker,
    "rva-form-depttree": RvaFormDeptTree,
    RvaFormRanges,
    RvaFormVideo,
    RvaFormImagecropper,
    RvaFormTextarray, RvaFormCascader
  },
  data() {
    return {
      formDataList: [],
      formDataListOld: [],
      activeCollapseItems: [0, 1, 2, 3, 4],
      createFormData: false
    };
  },
  mounted() {
    console.log('-------rva-form-crud mounted...');
    this.trigger()
  },
  methods: {
    trigger() {
      for (var i in this.formDataList) {
        let formData = this.formDataList[i];
        if (formData.del == 'Y') {
          continue;
        }
        if (!this.info) {
          RvaUtils.trigger(formData, undefined, this.getViewData(formData), this.$refs, i);
        }
      }
      this.formDataListOld = RvaUtils.cloneDeep(this.formDataList);
    },
    triggerView(formData, propId, rowIndex) {
      if (!this.info) {
        RvaUtils.trigger(formData, propId, this.getViewData(formData), this.$refs, rowIndex);
      }
    },
    afterCreated() {
      console.log('-------rva-form-crud created...');
      this.formDataList = this.formData[this.prop.id];
      // this.prop.formRequired == 'Y'
      if (!this.createFormData) {
        this.createFormData = RvaUtils.cloneDeep(this.formDataList[this.formDataList.length - 1]);
      }
      console.log('this.createFormData', this.createFormData)
      if (!this.info) {
        if (this.prop.formRequired == 'Y') {
          if (this.formDataList.length > 1) {
            this.formDataList.pop();
          }
        } else {
          this.formDataList.pop();
        }
      }
      // 将第一个createForm激活（展开）
      for (var i in this.formDataList) {
        if (this.formDataList[i].create) {
          this.activateCollapseItems(i);
          break;
        }
      }
    },
    activateCollapseItems(index) {
      if (this.activeCollapseItems.indexOf(index) < 0) {
        this.activeCollapseItems.push(index);
      }
    },
    addRow(row, rowIndex) {
      if (!row) {
        row = RvaUtils.cloneDeep(this.createFormData);
      }
      if (!rowIndex) {
        rowIndex = 0;
      }
      row = RvaUtils.clone(row, {create: true}, true);
      // this.formDataList.splice(rowIndex + 1, 0, row);
      // this.activeCollapseItems(rowIndex + 1);
      console.log('this.formDataList', this.formDataList)
      this.formDataList.push(row);
      this.activateCollapseItems(this.formDataList.length - 1);
      // 此处必须使用setTimeout，因为$refs可能没有立刻生成
      // var me = this;
      this.$nextTick(() => {
        // RvaUtils.trigger(row, undefined, me.prop.relatedCrudViewData.create.viewData, me.$refs, rowIndex + 1);
        if (!this.info) {
          RvaUtils.trigger(row, undefined, this.prop.relatedCrudViewData.create.viewData, this.$refs, this.formDataList.length - 1);
          this.formDataListOld = RvaUtils.cloneDeep(this.formDataList);
        }
      })
      console.log('this.formData[this.prop.id]', this.formData[this.prop.id]);
      console.log('this.createFormData', this.createFormData);
    },
    removeRow(rowIndex) {
      this.formDataList.splice(rowIndex, 1);
      this.formDataListOld = RvaUtils.cloneDeep(this.formDataList);
    },
    removeRowUpdate(row) {
      if (row.del == 'Y') {
        delete row.del;
      } else {
        row.del = 'Y';
      }
      this.rerender();
    },
    rerender() {
      // 触发控件重新渲染
      this.formDataList.push([]);
      this.formDataList.pop();
    },
    showRemove() {
      let data = this.formData[this.prop.id];
      return !this.prop.formRequired || (this.prop.formRequired == 'Y' && data.filter(r => r.del != 'Y').length > 1) || this.prop.formRequired == 'N';
    },
    getViewData(formData) {
      console.log('this.prop.relatedCrudViewData', this.prop.relatedCrudViewData);
      return formData.create ? this.prop.relatedCrudViewData.create.viewData : this.prop.relatedCrudViewData.update.viewData;
    },
    createRowsPropData(viewData) {
      return createRowsPropData(viewData);
    },
    getInfo() {
      console.log('this.readonly || this.info || this.disabled', this.readonly || this.info || this.disabled)
      return this.readonly || this.info || this.disabled
    }
  },
  watch: {
    'formDataList': {
      handler(val, valOld) {
        console.log('watch:formDataList-start', val, valOld, this.formDataListOld);
        if (!this.formDataListOld || valOld !== val || val.length != this.formDataListOld.length) {// created初始化时触发
          return;
        }
        if (this.info) {
          return;
        }
        this.$nextTick(() => {
          for (var i in this.formDataList) {
            let row = this.formDataList[i];
            let rowOld = this.formDataListOld[i];
            if (JSON.stringify(row) == JSON.stringify(rowOld)) {
              continue;
            }
            RvaUtils.trigger(row, rowOld, this.getViewData(row), this.$refs, i);
          }
          this.formDataListOld = RvaUtils.cloneDeep(this.formDataList);
          console.log('watch:formDataList-end', val, valOld);
        });
      },
      deep: true
    },
    'prop': {
      handler(val, valOld) {
        this.afterCreated();
        this.trigger();
      }
    },
  }
};
</script>
