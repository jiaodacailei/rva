<template>
  <el-image
    style="width: 25px; height: 25px"
    :src="getUrl(row[prop.id])"
    fit="contain"
    :preview-src-list="getListByStr(row[prop.id])">
    <div slot="error" class="image-slot">
      <i class="el-icon-picture-outline"></i>
    </div>
  </el-image>
</template>
<script>

import mobileComponent from "@/components/rva/list/mobile/mobileComponent";
import {getFileUrl} from "@/utils/ruoyi";

export default {
  mixins: [mobileComponent],
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
