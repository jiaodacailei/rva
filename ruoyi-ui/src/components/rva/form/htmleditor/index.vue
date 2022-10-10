<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" @click="editOption" :label-clickable="optionEditable" :requestParams="requestParams"></rva-form-config>
      </template>
      <Editor
        :value="value2"
        :min-height="400"
        @input="handleValue"
        :readOnly="disabled || prop.formReadonly == 'Y'"
      ></Editor>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
    <rva-crud-selector v-if="open" :open.sync="open" :appIds="appIds" :singleSelect="prop.formSelectorSingle == 'Y'"
                       :requestParams="requestParams" @rvaConfirmListSelection="handleSelection"
    ></rva-crud-selector>
  </div>
</template>

<script>

import formInput from "@/api/rva/form-input";
import {RvaUtils} from "@/api/rva/util";
import {addUrlPrefix, removePrefix} from "@/utils/ruoyi"
import Editor from "@/components/Editor"
import RvaCrudSelector from "@/components/rva/crud/selector";

export default {
  mixins: [formInput],
  data() {
    return {
      max: 20,
      value2: "",
      appIds: ["crud3_rva_material"],
      open: false
    }
  },
  components: {Editor, RvaCrudSelector},
  methods: {
    afterCreated() {
      this.max = RvaUtils.parseInt(this.prop.formValueMax, 20);
      this.addUrlPrefix();
    },
    addUrlPrefix(val) {
      let html = addUrlPrefix(this.value)
      this.value2 = html;
    },
    handleValue(val) {
      this.$emit('input', removePrefix(val));
    },
    editOption() {
      this.open = true;
    },
    handleSelection(selection) {

      if (selection.length == 0) {
        return;
      }
      this.changeValue(selection[0]['l0_rva_material_content'])
    },
    changeValue(val) {
      if (val) {
        this.value2 = addUrlPrefix(val);
        console.log("addUrlPrefix(val)==", addUrlPrefix(val))
        console.log('  this.value2 ', this.value2)
        this.$emit('input', this.value2)
      }
    }
  },
  computed: {
    optionEditable() {
      return RvaUtils.parseValue(this.prop.data, 'selectorOptionEditable', 'N') == 'Y'
    },
  },
  watch: {
    value2() {

    }
  }
};
</script>
