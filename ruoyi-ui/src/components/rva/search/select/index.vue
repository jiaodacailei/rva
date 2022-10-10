<template>
  <el-form-item :label="prop.name" :prop="prop.id" :labelWidth="labelWidth">
    <template slot="label">
      <rva-search-config :prop="prop"></rva-search-config>
    </template>
    <el-select :placeholder="'请选择' + prop.name" :multiple="this.prop.formSelectorSingle == 'N'" :value="value"
               clearable
               @input="$emit('input', $event)" size="small">
      <el-option v-for="dict in dictData" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue">
      </el-option>
    </el-select>
  </el-form-item>
</template>

<script>
import search from "@/api/rva/search";

export default {
  mixins: [search],
  name: "RvaSearchSelect",
}
</script>
