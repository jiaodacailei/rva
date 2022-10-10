<template>
  <el-card v-if="appData.id">
    <div slot="header" v-if="showTitle">
      <rva-portal-config :appData="appData" :portal-data="requestParams.portal"/>
    </div>
    <el-container v-if="component == 'tree'">
      <el-main :style="isPC ? {} : {padding: '2px'}">
        <template v-if="!isPC">
          <span class="el-icon-caret-bottom"> 导航</span>
          <el-tree :load="loadNode" lazy @node-click="treeClick" :highlight-current="true" node-key="nodeKey" ref="tcrudTree">
          </el-tree>
        </template>
        <template v-for="item in contents">
          <rva-tcrud-content :item="item" :tree-crud-params="treeCrudParams" @rva-button-click="handleButtonClick" :key="item.id" :queryContext="queryParams"></rva-tcrud-content>
		    </template>
      </el-main>
      <el-aside width="200px" style="background-color: white" v-if="isPC">
        <rva-tcrud-config :app="appData" :requestParams="requestParams" @rva-button-click="handleButtonClick"></rva-tcrud-config>
        <el-tree :load="loadNode" lazy @node-click="treeClick" :highlight-current="true" node-key="nodeKey" ref="tcrudTree">
        </el-tree>
      </el-aside>
    </el-container>
    <template v-else>
      <component :is="'rva-tcrud-' + component" :app="appData" :requestParams="requestParams" :appId="appId" @rva-button-click="handleButtonClick" />
    </template>
  </el-card>
</template>

<script>
import tcrud from '@/components/rva/tcrud/tcrud'
import RvaTcrudTabs from "@/components/rva/tcrud/tabs";
import RvaPortalConfig from "@/components/rva/portal/config"

export default {
  name: "rva-tcrud",
  mixins: [tcrud],
  components: {
    RvaTcrudTabs, RvaPortalConfig
  }
};
</script>
