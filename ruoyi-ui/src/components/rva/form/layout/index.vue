<template>
  <div>
    <template v-if="rowsPropData.length > 0">
      <el-row v-for="(row1, row1Index) in rowsPropData" :key="row1Index">
        <el-col v-for="(col1, col1Index) in row1.cols" :span="col1.colSpan" :key="row1Index * 100 + col1Index">
          <el-row v-for="(row2, row2Index) in col1.rows" :key="row1Index * 10000 + col1Index * 100 + row2Index">
            <el-col v-for="(col2, col2Index) in row2.cols" :span="col2.span"
                    :key="row1Index * 1000000 + col1Index * 10000 + row2Index * 100 + col2Index">
              <component :is="'rva-form-' + col2.prop.type" v-model="formData[col2.prop.id]" :prop="col2.prop"
                         :info="info" :viewData="viewData" :formData="formData" :requestParams="requestParams"
                         :ref="rowIndex >=0 ? col2.prop.id + '_' + rowIndex : col2.prop.id"
                         :formItemProp="formItemPrefix ? formItemPrefix + col2.prop.id : col2.prop.id"
              >
              </component>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </template>

  </div>
</template>

<script>
import RvaFormTextfield from "@/components/rva/form/textfield";
import RvaFormNumberfield from "@/components/rva/form/numberfield";
import RvaFormTextarea from "@/components/rva/form/textarea";
import RvaFormButton from "@/components/rva/form/button";
import RvaFormSelector from "@/components/rva/form/selector";
import RvaFormObjectselector from "@/components/rva/form/objectselector";
import RvaFormRadio from "@/components/rva/form/radio";
import RvaFormSelect from "@/components/rva/form/select";
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

import {createRowsPropData} from "@/api/rva/crud";

export default {
  name: "RvaLayout",
  props: ['viewData', 'formData', 'formRef', 'requestParams', 'info', 'rowIndex', 'formItemPrefix'],
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
    RvaFormRanges,
    RvaFormVideo
  },
  data() {
    return {
      rowsPropData: []
    }
  },
  created() {
    console.log('rva-layout....', this.viewData, this.rowsPropData)
    this.rowsPropData = createRowsPropData(this.viewData)
  },
}
</script>

<style scoped>

</style>
