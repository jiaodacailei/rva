<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label" >
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <el-input-number
          v-if="prop.numberScale == 0"
          size="small" :value="value" @input="$emit('input', $event)" :disabled="prop.formReadonly == 'Y' || disabled"
          :min="parseInt(prop.formValueMin) == NaN ? 0 : parseInt(prop.formValueMin)"
          :max="parseInt(prop.formValueMax) == NaN ? 100 : parseInt(prop.formValueMax)" :precision="0"
          style="width:100%"
      ></el-input-number>
      <el-input-number
          v-else
          size="small" :value="value" @input="$emit('input', $event)" :disabled="prop.formReadonly == 'Y' || disabled"
          :min="parseFloat(prop.formValueMin) == NaN ? 0 : parseFloat(prop.formValueMin)"
          :max="parseFloat(prop.formValueMax) == NaN ? 100 : parseFloat(prop.formValueMax)" :precision="parseInt(prop.numberScale)"
          style="width:100%"
      ></el-input-number>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
  </div>
</template>

<script>

import formInput from "@/api/rva/form-input";

export default {
  mixins: [formInput],
}
</script>
