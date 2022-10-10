<template>
  <el-table-column
    :key="prop.id" align="center" :label="prop.name" :prop="prop.id"
    :sortable="prop.listOrderIdx >= 0 ? 'custom' : false"
    :width="prop.width" show-overflow-tooltip scoped-slot
  >
    <template slot="header">
      <rva-list-config :prop="prop" :requestParams="requestParams"></rva-list-config>
    </template>

    <template slot-scope="scope">
      <i :class="scope.row[prop.id]" style="height: 32px;width: 16px;"
         class="el-input__icon" v-if="scope.row[prop.id]"> </i>
    </template>
  </el-table-column>
</template>
<script>

import column from "@/components/rva/list/column/column";

export default {
  mixins: [column]
};
</script>
