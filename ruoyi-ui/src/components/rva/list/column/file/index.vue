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
      <a v-if="scope.row[prop.id]" data-v-211f81e0="" href="javascript:void(0)" @click="open(scope.row[prop.id])"
         class="el-link el-link--default">
        <!----><span class="el-link--inner">
                <span data-v-211f81e0="" class="el-icon-document"> {{ getFileName(scope.row[prop.id]) }}
                </span></span><!----></a>
    </template>
  </el-table-column>
</template>
<script>

import column from "@/components/rva/list/column/column";
import {getFileUrl} from "@/utils/ruoyi";
export default {
  mixins: [column],
  methods: {
    open(url) {
      console.log('', url)

      window.open(getFileUrl(url))
    },
    // 获取文件名称
    getFileName(name) {
      if (name.lastIndexOf("/") > -1) {
        return name.slice(name.lastIndexOf("/") + 1).toLowerCase();
      } else {
        return "";
      }
    },
  }
};
</script>
