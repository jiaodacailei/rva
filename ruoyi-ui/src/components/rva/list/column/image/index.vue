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
      <el-image
        style="width: 25px; height: 25px"
        :src="getUrl(scope.row[prop.id])"
        fit="contain"
        :preview-src-list="getListByStr(scope.row[prop.id])">
        <div slot="error" class="image-slot">
          <i class="el-icon-picture-outline"></i>
        </div>
      </el-image>
    </template>

  </el-table-column>
</template>
<script>

import column from "@/components/rva/list/column/column";
import {getFileUrl} from "@/utils/ruoyi";

export default {
  mixins: [column],

  methods: {
    getUrl(str) {
      let array = this.getListByStr(str);
      return array[0];
    },
    getListByStr(str) {
      if (str) {
        let array = str.split(',')
        for (let i = 0; i < array.length; i++) {
          array[i] = getFileUrl(array[i])
        }
        return array;
      }
      return []
    },
  }
};
</script>
