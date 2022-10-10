<template>
  <el-dropdown-item :command="command">
    <span>
      <span class="el-icon-setting"></span> 克隆属性
    </span>
    <el-dialog :visible.sync="open" width="900px" append-to-body v-if="open">
      <template slot="title">
        克隆属性
        <el-tag>{{ prop.name }}</el-tag>
      </template>
      <rva-form :viewId="dialogAppId" :requestParams="{rvaAppParams: prop}" @rva-button-click="handleFormButtonClick"
      ></rva-form>
    </el-dialog>
  </el-dropdown-item>
</template>
<script>
import dropdown from '../dropdown';

export default {
  mixins: [dropdown],
  data() {
    return {
      dialogAppIds: ['c7_rva_viewproperty', 'c8_rva_viewproperty', 'c9_rva_viewproperty']
    }
  }
}
</script>
