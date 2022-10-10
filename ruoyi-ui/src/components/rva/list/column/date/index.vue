<template>
  <el-table-column
    :key="prop.id" align="center" :label="prop.name" :prop="prop.id"
    :sortable="prop.listOrderIdx >= 0 ? 'custom' : false"
    :width="prop.width" show-overflow-tooltip scoped-slot :formatter="formatDateValue"
  >
    <template slot="header">
      <rva-list-config :prop="prop" :requestParams="requestParams"></rva-list-config>
    </template>
  </el-table-column>
</template>
<script>
import column from "@/components/rva/list/column/column";

var moment = require('moment');
export default {
  mixins: [column],
  methods: {
    formatDateValue(row, column, cellValue, index) {
      return cellValue ? moment(cellValue).format('yyyy-MM-DD') : '';
    },
  }
};
</script>
