<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <el-switch
        v-model="value2"
        @change="change"
        :disabled="disabled || prop.formReadonly == 'Y'"
      >
      </el-switch>
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
      value2:this.value?'Y':'N'

    }
  },
  methods: {
    afterCreated() {
      this.max = RvaUtils.parseInt(this.prop.formValueMax, 20);
    },
    change(v){
    this.$emit('input', v?'Y':'N');
    }
  }
};
</script>
