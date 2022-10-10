<template>
  <el-tabs v-if="contents && contents.length">
    <el-tab-pane v-for="item in contents" :key="item.id">
      <template slot="label">
        <rva-tcrud-config :app="appData" :item="item" :requestParams="requestParams" @rva-button-click="handleButtonClick"></rva-tcrud-config>
      </template>
      <rva-tcrud-content :item="item" :tree-crud-params="treeCrudParams"></rva-tcrud-content>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import tcrud from '@/components/rva/tcrud/tcrud'

export default {
  name: "rva-tcrud-tabs",
  mixins: [tcrud],
  props: ['app'],
  created() {
    this.appData = this.app;
    this.loadContent();
  },
  methods: {
  }
};
</script>
