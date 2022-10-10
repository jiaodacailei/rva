<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <el-time-select
        v-model="value2"
        :picker-options="{
          start: '08:30',
          step: '00:15',
          end: '18:30'
        }"
        :disabled="disabled"
        :readonly="prop.formReadonly == 'Y'"
        @input="$emit('input', $event)"
        placeholder="选择时间"
      >
      </el-time-select>
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
      value2: this.value
    }
  },
  methods: {
    afterCreated() {
      this.max = RvaUtils.parseInt(this.prop.formValueMax, 20);
    }
  }
};
</script>
