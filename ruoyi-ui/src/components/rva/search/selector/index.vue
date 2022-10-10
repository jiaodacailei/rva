<template>
  <el-form-item :label="prop.name" :prop="prop.id" :rules="rules" :labelWidth="labelWidth">
    <template slot="label">
      <rva-search-config :prop="prop"></rva-search-config>
    </template>
    <el-select
      :value="value"
      :multiple="multiple"
      filterable
      clearable
      remote
      default-first-option
      :allow-create="allowCreate"
      :disabled="disabled || prop.formReadonly == 'Y'"
      reserve-keyword
      :placeholder="'请输入' + prop.name"
      :remote-method="searchSelectOptions"
      @change="change"
      size="small"
      :loading="loading">
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value">
      </el-option>
    </el-select>
  </el-form-item>
</template>

<script>
import formInput from "@/api/rva/form-input";
import selector from "@/components/rva/form/selector/selector";

export default {
  mixins: [formInput, selector],
  name: "RvaFormSelector",
  methods: {
    change(value) {
      // debugger
      let results = this.options.filter(option => {
        if (this.prop.formSelectorSingle == 'N') {
          if (value && value.length && value.slice(-1)[0] == option.value) {
            return true;
          }
        } else {
          return option.value == value
        }
      })
      if (results.length == 0) {
        if (this.prop.formSelectorSingle == 'N') {
          if (value != '') {
            this.options.push({
              value: value.slice(-1)[0],
              label: value.slice(-1)[0]
            });
          }
        } else {
          if (value != '') {
            this.options.push({
              value,
              label: value
            });
          }
        }

      }
      console.log('selector-change', value);
      this.$emit('input', value)
    },
  }
}
</script>
