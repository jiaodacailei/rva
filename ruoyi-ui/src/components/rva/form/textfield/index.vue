<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
      v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <el-input
          :placeholder="(readonly || disabled) ? '' : '请输入' + prop.name"
          clearable
          size="small"
          :value="value"
          @input="$emit('input', $event)"
          :maxlength="max"
          :disabled="disabled"
          :readonly="prop.formReadonly == 'Y'"
      />
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
  </div>
</template>

<script>

import formInput from "@/api/rva/form-input";
import {RvaUtils} from "@/api/rva/util";

export default {
  mixins: [formInput],
  data() {
    return {
      max: 20,
    }
  },
  methods: {
    afterCreated() {
      this.max = RvaUtils.parseInt(this.prop.formValueMax, 20);
    }
  }
};
</script>
