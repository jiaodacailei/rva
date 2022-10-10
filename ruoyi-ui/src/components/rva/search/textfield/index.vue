<template>
  <el-form-item :label="prop.name" :prop="prop.id" :label-width="labelWidth">
    <template slot="label">
      <rva-search-config :prop="prop"></rva-search-config>
    </template>
    <el-input
      :placeholder="'请输入' + prop.name"
      clearable
      size="small"
      :value="value"
      @input="$emit('input', $event)"
    />
  </el-form-item>
</template>

<script>

import search from "@/api/rva/search";
export default {
  mixins: [search],
  name: "RvaSearchTextfield",
  props: ['prop', 'value', 'viewData'],
  methods: {
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.trigger++; // 以此使queryParams变化，从而触发loadList
    },
  }
};
</script>
