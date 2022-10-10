<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <el-input
          :placeholder="'请输入' + prop.name"
          clearable
          type="textarea"
          size="small"
          :value="value"
          :rows="rows"
          :disabled="disabled"
          @input="$emit('input', $event)"
          :maxlength="parseInt(prop.formValueMax) == NaN ? 20 : parseInt(prop.formValueMax)"
          :readonly="prop.formReadonly == 'Y'"
      />
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
  </div>
</template>

<script>

import {RvaUtils} from "@/api/rva/util";
import formInput from "@/api/rva/form-input";

export default {
  mixins: [formInput],
  data() {
    return {
      rows: 2,
    };
  },
  methods: {
    afterCreated() {
      this.rows = RvaUtils.parseValue(this.prop.data, 'textareaRows', 2);
    },
  }
};
</script>
