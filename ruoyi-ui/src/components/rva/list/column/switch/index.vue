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
      <el-switch
        :value="getSwitchValue(scope.row[prop.id])"
        @change="handleSwitch(prop, scope.row)"
      >
      </el-switch>
    </template>
  </el-table-column>
</template>
<script>
import {RvaUtils} from "@/api/rva/util";
import request from "@/utils/request";

import column from "@/components/rva/list/column/column";

export default {
  mixins: [column],
  methods: {
    handleSwitch(p, row) {
      let url = RvaUtils.parseValue(p.data, 'switchUrl');
      if (!url) {
        return;
      }
      url = RvaUtils.parseJsValue(url, row);
      request.post(url).then(res => {
        this.$modal.msgSuccess(res.msg);
        if (res.code == '200') {
          this.$emit('reloadView');
        } else {
          this.$modal.msgError(res.msg);
        }
      });
    },
    getSwitchValue(v) {
      return 'Y' == v;
    }
  }
};
</script>
