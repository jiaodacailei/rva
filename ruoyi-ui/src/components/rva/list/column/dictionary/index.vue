<template>
  <el-table-column
    :key="prop.id" align="center" :label="prop.name" :prop="prop.id"
    :sortable="prop.listOrderIdx >= 0 ? 'custom' : false"
    :width="prop.width" show-overflow-tooltip scoped-slot
  >
    <template slot-scope="scope">
      <dict-tag :options="dicts[prop.dictType]" :value="scope.row[prop.id]"/>
    </template>

    <template slot="header">
      <rva-list-config :prop="prop" :requestParams="requestParams"></rva-list-config>
    </template>

  </el-table-column>
</template>
<script>

import {RvaUtils} from "@/api/rva/util";
import column from "@/components/rva/list/column/column";

export default {
  mixins: [column],
  data() {
    return {
      dicts: {},
    }
  },
  created() {
    this.getDicts();
  },
  methods: {
    getDicts() {
      if (this.prop.dictType) {
        RvaUtils.getDicts(this.prop.dictType, this.dicts, this.prop.dictType, () => {
          this.dicts[this.prop.dictType] = this.dicts[this.prop.dictType].map(function (e) {
            return {
              value: e.dictValue,
              label: e.dictLabel,
              raw: {
                listClass: e.listClass,
                cssClass: e.cssClass
              }

            };
          })
        })
      }
    }
  }
};
</script>
