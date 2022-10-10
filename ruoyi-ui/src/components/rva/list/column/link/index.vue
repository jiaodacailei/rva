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
      <router-link :to="getLinkUrl(prop, scope.row)" class="link-type">
        <span>{{ scope.row[prop.id] }}</span>
      </router-link>
    </template>
  </el-table-column>
</template>
<script>
import {RvaUtils} from "@/api/rva/util";
import column from "@/components/rva/list/column/column";

export default {
  mixins: [column],
  methods: {
    getLinkUrl(p, row) {
      let url = RvaUtils.parseValue(p.data, 'listLink');
      if (!url) {
        return;
      }
      return RvaUtils.parseJsValue(url, row);
    },
  }
};
</script>
