<template>
  <el-table-column
    align="center" :label="prop.name" :prop="prop.id"
    :sortable="prop.listOrderIdx >= 0 ? 'custom' : false"
    :width="prop.width" show-overflow-tooltip scoped-slot
  >
    <template slot="header">
      <rva-list-config :prop="prop" :requestParams="requestParams"></rva-list-config>
    </template>
    <template slot-scope="scope">
      <a v-if="scope.row[prop.id]" data-v-211f81e0="" href="javascript:void(0)" @click="openDialog(scope.row)"
         class="el-link el-link--default">
          <span class="el-link--inner">
            <span class="el-icon-video-play">
            </span>
          </span>
      </a>

      <el-dialog :title="title" :visible.sync="open" :key="prop.id+new Date().getTime()"
                 width="1000px" v-if="keyPropValue == scope.row.keyPropValue" append-to-body>

        <video-player :src="getFileUrl" :full="true"
        />
      </el-dialog>
    </template>


  </el-table-column>
</template>
<script>

import column from "@/components/rva/list/column/column";
import {RvaUtils} from "@/api/rva/util";
import {getFileUrl} from "@/utils/ruoyi";
import VideoPlayer from '@/components/VideoPlayer'

export default {
  components: {
    VideoPlayer
  },
  mixins: [column],
  data() {
    return {
      open: false,
      title: "",
      url: "",
      keyPropValue: ""
    }
  },
  computed: {
    getFileUrl() {
      let url = this.url;
      console.log('video getFileUrl' + url)
      if (url) {
        return getFileUrl(url)
      }
    },
  },
  methods: {
    openDialog(row) {
      this.url = row[this.prop.id];
      this.getVideoTitle(this.prop, row);
      this.keyPropValue = row.keyPropValue
      this.open = true
    },
    getVideoTitle(p, row) {
      let title = RvaUtils.parseValue(p.data, 'listVideoTitle');
      if (!title) {
        this.title = row['namePropValue'];
        return;
      }
      this.title = RvaUtils.parseJsValue(title, row);
    },


  }
};
</script>
