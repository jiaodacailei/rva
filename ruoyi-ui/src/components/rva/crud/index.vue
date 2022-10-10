<template>
  <div v-if="appData && appData.template">
    <el-card v-if="component == 'crud'">
      <div slot="header" v-if="showTitle">
        <rva-portal-config :appData="appData" :portal-data="requestParams.portal" @click="click"/>
        <el-dialog :visible.sync="openApp" width="600px" append-to-body v-if="openApp">
          <template slot="title">
            配置应用 <el-tag>{{appData.name}}</el-tag>
          </template>
          <rva-form viewId="u0_rva_app" :requestParams="{rvaAppParams: appData}"></rva-form>
        </el-dialog>
      </div>
      <rva-search
        :requestParams="getRequestParams()"
        :queryParams.sync="queryParams"
        :viewId="appData.searchId"
        :selector="selector"
        v-if="appData.searchId"
      ></rva-search>
      <rva-list
        :requestParams="getRequestParams()"
        :queryParams="queryParams"
        :viewId="appData.listId"
        :selector="selector"
        :selectLimits="singleSelect ? 1 : 0"
        @rvaListSelection="$emit('rvaListSelection', $event)"
        v-if="appData.listId"
      ></rva-list>
    </el-card>
    <component v-else :is="'rva-' + component" :appMeta="appData" :requestParams="requestParams" :showTitle="showTitle"
               @rva-button-click="handleButtonClick"
    ></component>
  </div>

</template>

<script>
import RvaList from "@/components/rva/list";
import RvaForm from "@/components/rva/form";
import RvaListeditor from "@/components/rva/crud/listeditor";
import RvaPortalConfig from "@/components/rva/portal/config"

import crud from "@/components/rva/crud/crud";

export default {
  mixins: [crud],
  components: {
    RvaList, RvaListeditor, RvaPortalConfig, RvaForm
  },
  props:['singleSelect'],
  data() {
    return {
      openApp: false
    }
  },
  methods: {
    click() {
      if (this.devMode) {
        this.openApp = true
      }
    }
  },
  computed: {
    component() {
      return this.appData.template.split("/").pop()
    }
  }
};
</script>
