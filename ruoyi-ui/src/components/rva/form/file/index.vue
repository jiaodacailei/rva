<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <FileUpload
        :value="value2"
        :file-size="parseIntValue('fileSize')"
        :limit="parseIntValue('fileLimit')"
        :file-type="parseArray('fileType')"
        @input="$emit('input', $event)"
        :showList="true"
      ></FileUpload>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
  </div>
</template>

<script>

import formInput from "@/api/rva/form-input";
import {RvaUtils} from "@/api/rva/util";

import FileUpload from "@/components/FileUpload"

export default {
  mixins: [formInput],
  data() {
    return {
      max: 20,
      value2: this.value
    }
  },
  components: {FileUpload},
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
    }
  }
};
</script>
