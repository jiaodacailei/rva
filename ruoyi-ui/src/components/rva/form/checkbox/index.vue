<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <el-checkbox-group :value="value" @input="$emit('input', $event)">
        <el-checkbox
          v-for="dict in dictData"
          :key="dict.dictValue"
          :label="dict.dictValue"
          :disabled="disabled || prop.formReadonly == 'Y'"
        >{{dict.dictLabel}}</el-checkbox>
      </el-checkbox-group>
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
