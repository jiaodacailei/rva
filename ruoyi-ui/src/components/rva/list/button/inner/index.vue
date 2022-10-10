<template>
  <div v-if="show">
    <template v-if="getConfirmMessage()">
      <el-popconfirm :title="getConfirmMessage()" @confirm="handleButtonClick" icon="el-icon-warning" icon-color="red">
        <el-button  slot="reference" size="mini" type="text"
                    v-hasPermi="getPermissions()" :loading="loading" @rva-button-click="handleFormButtonClick">
          <rva-button-config :button="viewButtonData" @rva-button-click="handleButtonConfigClick"></rva-button-config>
        </el-button>
      </el-popconfirm>
    </template>
    <template v-else>
      <el-button size="mini" type="text" @click="handleButtonClick"
                 v-hasPermi="getPermissions()" :loading="loading" @rva-button-click="handleFormButtonClick">
        <rva-button-config :button="viewButtonData" @rva-button-click="handleButtonConfigClick"></rva-button-config>
      </el-button>
    </template>
    <template v-if="viewButtonData.action == 'dialog'">
      <template v-if="dialogSelector">
        <rva-crud-selector v-if="open && dialogAppParams.trigger > 0" :open.sync="open" :appIds="[viewButtonData.actionDialogAppId]"
                           :singleSelect="dialogSelectorSingle" @rvaConfirmListSelection="handleSelectorSelection"
                           :requestParams="dialogAppParams"
        ></rva-crud-selector>
      </template>
      <template v-else>
        <el-dialog :title="actionTitle" :visible.sync="open" v-if="open" :width="dialogWidth" append-to-body>
          <rva-form v-if="viewButtonData.type == 'form' && dialogFormParams" :viewId="viewButtonData.actionDialogViewId"
                    :requestParams="dialogFormParams" @rva-button-click="handleFormButtonClick"
                    @rva-form-width="setDialogWidth"
          ></rva-form>
          <template v-if="viewButtonData.type == 'tcrud'">
            <rva-tcrud v-if="dialogAppParams.trigger > 0" :appId="viewButtonData.actionDialogAppId"
                       :requestParams="dialogAppParams" @rva-button-click="handleFormButtonClick"></rva-tcrud>
          </template>
          <template v-if="viewButtonData.type == 'crud'">
            <rva-crud v-if="dialogAppParams.trigger > 0" :appId="viewButtonData.actionDialogAppId"
                      :requestParams="dialogAppParams" @rva-button-click="handleFormButtonClick"></rva-crud>
          </template>
        </el-dialog>
      </template>
    </template>
  </div>
</template>

<script>

import button from '@/api/rva/button'

export default {
  name: "RvaListViewInnerButton",
  mixins: [button]
};
</script>
