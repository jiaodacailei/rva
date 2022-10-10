<template>
  <el-dialog :width="dialogWidth" :visible.sync="openDialog" append-to-body
             @close="closeDialog">
    <template slot="title">
      对象选择器
      <el-tag v-for="(appName, index) in appNames" :key="index">{{appName}}</el-tag>
    </template>
    <el-alert
      v-show="showInfo"
      title="请点击列表中的多选框进行选择，然后点击右下角的确定按钮"
      type="info" effect="dark" show-icon>
    </el-alert>
    <rva-crud v-if="appIds.length == 1" :appId="appIds[0]" :requestParams="requestParams" @rvaAppData="getAppData" :selector="true"
              :singleSelect="singleSelect" @rvaListSelection="selection[appIds[0]] = $event"></rva-crud>
    <el-tabs v-else type="border-card" style="margin-top: 10px" tab-position="left">
      <el-tab-pane v-for="appId in appIds" :label="getAppName(appId)" :key="appId">
        <rva-crud :appId="appId" :requestParams="requestParams" @rvaAppData="getAppData" :selector="true"
                  :singleSelect="singleSelect" @rvaListSelection="selection[appId] = $event"></rva-crud>
      </el-tab-pane>
    </el-tabs>
    <template slot="footer">
      <el-button plain type="success" class="el-icon-check" size="small" @click="confirmSelection"> 确定</el-button>
      <el-button plain class="el-icon-close" size="small" @click="closeDialog"> 取消</el-button>
    </template>
  </el-dialog>
</template>

<script>

import {RvaUtils} from "@/api/rva/util";

export default {
  name: "RvaCrudSelector",
  components: {
    'rva-crud': () => import('@/components/rva/crud'),
  },
  props: {
    appIds: {
      type: Array,
      default() {
        return [];
      }
    },
    singleSelect: {
      type: Boolean,
      default: true
    },
    requestParams: {
      type: Object,
      default() {
        return {}
      }
    },
    dialogWidth: {
      type: String,
      default: RvaUtils.getDialogWidth(1000) + "px"
    },
    open: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      appNames: {},
      show: false,
      openDialog: true,
      selection: {},
      showInfo: true
    }
  },
  created() {
    this.openDialog = this.open;
    setTimeout(() => {
      this.showInfo = false;
    }, 5000)
  },
  methods: {
    getAppData(appData) {
      this.appNames[appData.id] = appData.name;
      this.appNames = RvaUtils.cloneDeep(this.appNames)
    },
    closeDialog() {
      this.$emit('update:open', false)
    },
    getAppName(appId) {
      return this.appNames[appId]
    },
    confirmSelection() {
      let rows = [];
      for (let i = 0; i < this.appIds.length; i++) {
        let appId = this.appIds[i]
        let r = this.selection[appId]
        if (r && r.length > 0) {
          for (let j = 0; j < r.length; j++) {
            r[j].appId = appId;
            r[j].appName = this.appNames[appId];
            r[j].label = r[j].namePropValue;
            if (this.appIds.length > 1) {
              r[j].value = [appId, r[j].keyPropValue].join(':');
              // r[j].label = [r[j].namePropValue, r[j].appName].join('@');
            } else {
              r[j].value = r[j].keyPropValue;
            }
            rows.push(r[j]);
          }
        }
      }
      this.$emit('rvaConfirmListSelection', rows);
      this.$emit('update:open', false)
    }
  },
};
</script>
<style scoped>
/deep/ .el-dialog__body {
  padding: 5px 10px;
}
</style>
