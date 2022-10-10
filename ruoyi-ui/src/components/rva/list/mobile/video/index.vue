<template>
  <div>
    <a v-if="row[prop.id]" href="javascript:void(0)" @click="openDialog" class="el-link el-link--default">
      <span class="el-link--inner">
        <span class="el-icon-video-play"/>
      </span>
    </a>
    <el-dialog :title="title" :visible.sync="open" :width="width" v-if="open" append-to-body>
      <video autoplay controls style="width: 100%;height: 100%" :src="url"></video>
    </el-dialog>
  </div>
</template>
<script>

import mobileComponent from "@/components/rva/list/mobile/mobileComponent";
import {RvaUtils} from "@/api/rva/util";
import {getFileUrl} from "@/utils/ruoyi";

export default {
  mixins: [mobileComponent],
  data() {
    return {
      open: false
    }
  },
  computed: {
    url() {
      return getFileUrl(this.value)
    },
    title() {
      let title = RvaUtils.parseValue(this.prop.data, 'listVideoTitle');
      if (!title) {
        title = this.row['namePropValue'];
      }
      if (!title) {
        return '视频';
      }
      return RvaUtils.parseJsValue(title, this.row);
    },
    width() {
      return RvaUtils.getDialogWidth(1000) + 'px'
    }
  },
  methods: {
    openDialog() {
      this.open = true;
    }
  }
};
</script>
