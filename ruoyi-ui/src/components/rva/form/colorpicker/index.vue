<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <el-color-picker :disabled="disabled || prop.formReadonly == 'Y'" :value="value2"  @input="$emit('input', $event)" ></el-color-picker>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
  </div>
</template>

<script>

import formInput from "@/api/rva/form-input";
import {RvaUtils} from "@/api/rva/util";

import Editor from "@/components/Editor"

export default {
  mixins: [formInput],
  data() {
    return {
      max: 20,
      value2:this.value||'#1890ff'
    }
  },
  components: {Editor},
  methods: {
    afterCreated() {
      this.max = RvaUtils.parseInt(this.prop.formValueMax, 20);
    }
  }
};
</script>
