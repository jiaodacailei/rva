<template>
    <el-button
        size="mini" :loading="loading" plain :type="viewButtonData.cls"
        @click="handleButtonClick" v-hasPermi="getPermissions()"
    >
      <rva-button-config :button="viewButtonData" @rva-button-click="handleButtonConfigClick"></rva-button-config>
      <template v-if="viewButtonData.action == 'dialog'">
        <el-dialog :title="actionTitle" :visible.sync="open" :width="dialogViewData.width + 'px'" append-to-body>
          <rva-form v-if="dialogViewData" :viewData="dialogViewData" :formData="dialogFormData" @rva-button-click="handleFormButtonClick"></rva-form>
        </el-dialog>
      </template>
    </el-button>
</template>

<script>

import button from '@/api/rva/button'

export default {
  mixins: [button]
};
</script>
