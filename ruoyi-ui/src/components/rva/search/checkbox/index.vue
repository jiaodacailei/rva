<template>
  <el-form-item :label="prop.name" :prop="prop.id" :label-width="labelWidth">
    <template slot="label">
      <rva-search-config :prop="prop"></rva-search-config>
    </template>
    <el-checkbox-group :value="value" @input="$emit('input', $event)">
      <el-checkbox
        v-for="dict in dictData"
        :key="dict.dictValue"
        :label="dict.dictValue"
      >{{dict.dictLabel}}</el-checkbox>
    </el-checkbox-group>
  </el-form-item>
</template>

<script>
import search from "@/api/rva/search";
export default {
  mixins: [search],
};
</script>
