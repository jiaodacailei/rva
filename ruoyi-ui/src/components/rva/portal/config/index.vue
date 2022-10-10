<template>
  <component :is="devMode ? 'el-dropdown' : 'rva-span'" v-if="portalData" @command="executeCommand">
    <span @click="$emit('click', $event)">{{ appData.name }}</span>
    <el-dropdown-menu slot="dropdown" v-if="devMode">
      <el-dropdown-item command="configApp"><span class="el-icon-setting"></span> 配置应用</el-dropdown-item>
      <el-dropdown-item command="configAppItem"><span class="el-icon-setting"></span> 配置应用项</el-dropdown-item>
      <el-dropdown-item divided>
        <span class="el-icon-download"/>{{ ' 元数据' }}
      </el-dropdown-item>
      <el-dropdown-item :command="{name : 'exportMeta', exportId: portalData.id, exportType: portalData.type}" :key="portalData.id + '-2'">
        <span class="el-icon-download dropdown-item2"/>{{ ' ' + portalData.id }}
      </el-dropdown-item>
      <el-dropdown-item :command="{name : 'exportMeta', exportId: appData.id, exportType: appData.type}" :key="appData.id + '-1'">
        <span class="el-icon-download dropdown-item2"/>{{ ' ' + appData.id }}
      </el-dropdown-item>
      <el-dropdown-item v-for="item in appData.appItems" :command="{name : 'exportMeta', exportId: item.relatedAppId, exportType: item.relatedAppType}" :key="item.id + '-export'">
        <span class="el-icon-download dropdown-item2"/>{{ ' ' + item.relatedAppId }}
      </el-dropdown-item>
      <el-dropdown-item disabled divided><span class="el-icon-s-grid"/> 门户列数</el-dropdown-item>
      <el-dropdown-item
        v-for="index in 3"
        :command="getPortalAppColumnsData(index)" :key="`columns-${index}`"
      ><span class="el-icon-minus dropdown-item2"/> {{ index }}列
      </el-dropdown-item>
      <el-dropdown-item disabled divided><span class="el-icon-tickets"/> 门户项跨行</el-dropdown-item>
      <el-dropdown-item
        v-for="index in 3" :key="`rowSpan-${index}`"
        :command="getPortalAppItemData(index, 'rowSpan')"
      ><span class="el-icon-minus dropdown-item2"/> {{ index }}行
      </el-dropdown-item>
      <el-dropdown-item disabled><span class="el-icon-c-scale-to-original"/> 门户项跨列</el-dropdown-item>
      <el-dropdown-item
        v-for="index in 3" :key="`colSpan-${index}`"
        :command="getPortalAppItemData(index, 'colSpan')"
      ><span class="el-icon-minus dropdown-item2"/> {{ index }}列
      </el-dropdown-item>
      <el-dialog :visible.sync="openApp" width="600px" append-to-body v-if="openApp">
        <template slot="title">
          配置应用
          <el-tag>{{ portalData.name }}</el-tag>
        </template>
        <rva-form viewId="u0_rva_app" :requestParams="{rvaAppParams: portalData}" @rva-button-click="handleButtonClick"
        ></rva-form>
      </el-dialog>
      <el-dialog :visible.sync="openAppItem" width="900px" append-to-body v-if="openAppItem"
                 @rva-button-click="handleButtonClick">
        <template slot="title">
          配置
          <el-tag>{{ portalData.name }}</el-tag>
          的应用项
        </template>
        <rva-crud appId="crud0_rva_appitem" :requestParams="{rvaAppParams: portalData}"></rva-crud>
      </el-dialog>
      <el-dialog :visible.sync="openExport" width="900px" append-to-body v-if="openExport">
        <template slot="title">
          <el-tag>{{ exportId }}</el-tag>
          元数据
        </template>
        <rva-form :viewId="exportViewId" @rva-button-click="handleButtonClick"
                  :requestParams="{selection: [{keyPropValue: exportId}]}"
        ></rva-form>
      </el-dialog>
    </el-dropdown-menu>
  </component>
  <span v-else @click="$emit('click', $event)">{{ appData.name }}</span>
</template>

<script>
import RvaSpan from "@/components/rva/span";
import RvaForm from "@/components/rva/form";
import handleButtonClick from "@/components/rva/handleButtonClick";
import dropdown from "@/api/rva/dropdown";
import {updateObject} from "@/api/rva/crud";
import exportMetaMixin from "@/components/rva/exportMeta"

export default {
  name: "RvaPortalConfig",
  props: ['appData', 'portalData'],
  mixins: [handleButtonClick, dropdown, exportMetaMixin],
  components: {
    RvaSpan, 'rva-crud': () => import('@/components/rva/crud'), RvaForm
  },
  data() {
    return {
      open: false,
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
    getPortalAppItem() {
      if (!this.portalData) {
        return;
      }
      return this.portalData.appItems.find(item => item.relatedAppId == this.appData.id);
    },
    getPortalAppColumnsData(value) {
      if (!this.portalData) {
        return;
      }
      let data = {};
      if (this.portalData.data) {
        data = JSON.parse(this.portalData.data);
      }
      data.columns = value;
      // this.portalData.data = JSON.stringify(data);
      return {
        name: 'updateApp',
        keyPropValue: this.portalData.id,
        fieldValues: {
          data: JSON.stringify(data)
        }
      }
    },
    updateApp({keyPropValue, fieldValues}) {
      updateObject('rva_app', keyPropValue, fieldValues).then(res => {
        this.$router.go(0);
      })
    },
    getPortalAppItemData(value, propName) {
      let item = this.getPortalAppItem();
      if (!item) {
        return;
      }
      let data = {};
      if (item.data) {
        data = JSON.parse(item.data);
      }
      data[propName] = value;
      // this.portalData.data = JSON.stringify(data);
      return {
        name: 'updateAppItem',
        keyPropValue: item.id,
        fieldValues: {
          data: JSON.stringify(data)
        }
      }
    },
    updateAppItem({keyPropValue, fieldValues}) {
      updateObject('rva_appitem', keyPropValue, fieldValues).then(res => {
        this.$router.go(0);
      })
    },
  },
}
</script>

<style scoped>
.dropdown-item2 {
  margin-left: 20px
}
</style>
