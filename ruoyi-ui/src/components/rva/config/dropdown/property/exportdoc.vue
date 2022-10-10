<template>
  <el-dropdown-item divided>
    <span @click="open = true">
      <span class="el-icon-lock"></span> 用例文档
    </span>
    <el-dialog :visible.sync="open" width="900px" append-to-body v-if="open">
      <template slot="title">
        导出用例文档
        <el-tag>{{ appId }}</el-tag>
      </template>
      <rva-form view-id="c0_none_yuanshujujiaoben" :requestParams="{loadFormUrl: '/rva/meta/app/doc', selection: [{keyPropValue: appId}]}"
                @rva-button-click="handleFormButtonClick"></rva-form>
    </el-dialog>
  </el-dropdown-item>
</template>
<script>
import dropdown from '../dropdown';

export default {
  mixins: [dropdown],
  data() {
    return {
      appId: false
    }
  },
  mounted() {
    this.appId = this.requestParams.appId;
  }
}
</script>
