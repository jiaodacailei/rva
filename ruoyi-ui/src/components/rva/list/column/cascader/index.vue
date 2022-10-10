<template>
  <el-table-column
    :key="prop.id" align="center" :label="prop.name" :prop="prop.id"
    :sortable="prop.listOrderIdx >= 0 ? 'custom' : false"
    :width="prop.width" show-overflow-tooltip scoped-slot
    :formatter="formatValue2"
  >
    <template slot="header">
      <rva-list-config :prop="prop" :requestParams="requestParams"></rva-list-config>
    </template>

  </el-table-column>
</template>

<script>

import column from "@/components/rva/list/column/column";
import {RvaUtils} from "@/api/rva/util";

export default {
  mixins: [column],
  data() {
    return {
      dictData: []
    }
  },
  computed: {},
  created() {
    RvaUtils.getDicts(this.prop.dictType, this, 'dictData', this.afterDict);
  },
  methods: {
    afterDict(dictType, obj, objProp) {
      let dictData = obj[objProp];
      let results = [];
      for (let i = dictData.length - 1; i >= 0; i--) {
        let dictItem = dictData[i];
        if (!dictItem.children) {
          dictItem.leaf = true;
        }
        dictItem.value = dictItem.dictValue;
        dictItem.label = dictItem.dictLabel;
        let hasParent = false;
        for (let j = i - 1; j >= 0; j--) {
          let dictItem2 = dictData[j];
          if (dictItem.dictValue.indexOf(dictItem2.dictValue) == 0) {
            let children = dictItem2.children;
            if (!children) {
              children = [dictItem];
              dictItem2.children = children;
            } else {
              children.splice(0, 0, dictItem)
            }
            hasParent = true;
            break;
          }
        }
        if (!hasParent) {
          results.splice(0, 0, dictItem);
        }
      }
      obj[objProp] = results;
    },
    formatValue2(row, column, cellValue, index) {
      if (cellValue) {
        let children = this.dictData
        let str = '';
        let valArray = JSON.parse(cellValue);
        for (let i = 0; i < valArray.length; i++) {
          let key = valArray[i];
          let cur = this.find(key, children);
          if(!cur){
            return
          }
          str += cur.label + "/"
          children = cur.children;
        }
        return str.substring(0, str.length - 1);
      }
      return cellValue;
    },
    find(key, array) {
      for (let i = 0; i < array.length; i++) {
        let obj = array[i];
        if (obj.value == key) {
          return obj;
        }
      }
    }
  }
}
</script>
