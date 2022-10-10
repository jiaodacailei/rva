<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" @click="editOption" :label-clickable="optionEditable"></rva-form-config>
      </template>
      <FileUpload
        :read-only="readonly"
        :value="value2"
        :file-size="parseIntValue('fileSize')"
        :limit="parseIntValue('fileLimit')"
        :file-type="parseArray('fileType')"
        :showList="true"
        @input="$emit('input', $event)"
        :list-type="listType"
      ></FileUpload>
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

import FileUpload from "@/components/FileUpload"
import RvaCrudSelector from "@/components/rva/crud/selector";

export default {
  mixins: [formInput],
  data() {
    return {
      max: 20,
      value2: this.value,
      fileType: ["bmp", "gif", "jpg", "jpeg", "png"],
      listType: "picture-card",// picture,picture-card
      appIds: ["crud1_rva_material"],
      open: false
    }
  },
  components: {FileUpload, RvaCrudSelector},
  methods: {
    afterCreated() {
      this.max = RvaUtils.parseInt(this.prop.formValueMax, 20);
    },
    parseIntValue(key) {
      return RvaUtils.parseInt(RvaUtils.parseValue(this.prop.data, key));
    },
    parseArray(key) {
      let v = RvaUtils.parseValue(this.prop.data, key);
      if (!v) {
        return this.fileType
      }
      return v.split(',')
    },
    editOption() {
      this.open = true;
    },
    handleSelection(selection) {

      if (selection.length == 0) {
        return;
      }
      this.changeValue(selection[0]['l0_rva_material_image'])
    },
    changeValue(val) {
      if (val) {
        this.value2 = val;
        console.log('  this.value2 ', this.value2)
        this.$emit('input', this.value2)
      }
    }
  },
  computed: {
    readonly() {
      return this.info || this.viewData.formReadonly == 'Y' || this.prop.formReadonly == 'Y'
    },
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
