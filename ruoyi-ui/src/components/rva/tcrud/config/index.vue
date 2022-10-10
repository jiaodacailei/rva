<template>
  <el-dropdown v-if="devMode" @command="executeCommand" :show-timeout="showTimeout">
    <span :class="getTitleClass()">{{ getTitle() }}</span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="configApp"><span class="el-icon-setting"></span> 配置应用</el-dropdown-item>
      <el-dropdown-item command="configAppItem"><span class="el-icon-setting"></span> 配置应用项</el-dropdown-item>
      <el-dropdown-item divided>
        <span class="el-icon-zoom-in"/>{{ ' 预览导航' }}
      </el-dropdown-item>
      <el-dropdown-item v-for="item in app.appItems.filter(ai => ai.type == 'nav')"
                        :command="{name : 'previewCrud', item}" :key="item.id">
        <span class="el-icon-zoom-in dropdown-item2"/>{{ ' ' + item.relatedAppId }}
      </el-dropdown-item>
      <el-dropdown-item divided>
        <span class="el-icon-download"/>{{ ' 元数据' }}
      </el-dropdown-item>
      <el-dropdown-item :command="{name : 'exportMeta', exportId: app.id, exportType: 'tcrud'}" :key="app.id + '-export'">
        <span class="el-icon-download dropdown-item2"/>{{ ' ' + app.id }}
      </el-dropdown-item>
      <el-dropdown-item v-for="item in app.appItems" :command="{name : 'exportMeta', exportId: item.relatedAppId, exportType: item.relatedAppType}" :key="item.id + '-export'">
        <span class="el-icon-download dropdown-item2"/>{{ ' ' + item.relatedAppId }}
      </el-dropdown-item>
    </el-dropdown-menu>
    <el-dialog :visible.sync="openApp" width="600px" append-to-body v-if="openApp">
      <template slot="title">
        配置应用
        <el-tag>{{ app.name }}</el-tag>
      </template>
      <rva-form viewId="u0_rva_app" :requestParams="{rvaAppParams: app}" @rva-button-click="handleFormButtonClick"
      ></rva-form>
    </el-dialog>
    <el-dialog :visible.sync="openAppItem" width="900px" append-to-body v-if="openAppItem">
      <template slot="title">
        配置
        <el-tag>{{ app.name }}</el-tag>
        的应用项
      </template>
      <rva-crud appId="crud0_rva_appitem" :requestParams="{rvaAppParams: app}"></rva-crud>
    </el-dialog>
    <el-dialog :visible.sync="openExport" width="900px" append-to-body v-if="openExport">
      <template slot="title">
        <el-tag>{{ exportId }}</el-tag>
        元数据
      </template>
      <rva-form :viewId="exportViewId" @rva-button-click="handleFormButtonClick"
                :requestParams="{selection: [{keyPropValue: exportId}]}"
      ></rva-form>
    </el-dialog>
  </el-dropdown>
  <h4 v-else class="el-icon-caret-bottom">导航</h4>
</template>

<script>

import config from '@/api/rva/config'
import exportMetaMixin from '@/components/rva/exportMeta'

export default {
  mixins: [config, exportMetaMixin],
  props: ['app', 'item'],
  data() {
    return {
      openAppItem: false,
      openApp: false,
    }
  },
  methods: {
    configApp() {
      this.openApp = true;
    },
    configAppItem() {
      this.openAppItem = true;
    },
    getTitle() {
      if (this.item) {
        return this.item.name;
      }
      return `导航（${this.app.name}）`;
    },
    getTitleClass() {
      if (this.item) {
        return '';
      }
      return 'el-icon-caret-bottom';
    },
    closeView() {
      this.openApp = false;
      this.openAppItem = false;
    },
    previewCrud(cmd) {
      let path = `/rva/crud/${cmd.item.relatedAppId}`;
      this.$router.push({path});
      sessionStorage.setItem(path + ".title", cmd.item.name);
    }
  }
};
</script>

<style>
.dropdown-item2 {
  margin-left: 20px
}
</style>
