<template>
  <el-dropdown-item :command="command">
    <span>
      <span class="el-icon-setting"></span> 配置属性
    </span>
    <el-dialog :visible.sync="open"  width="1200px" append-to-body v-if="open">
      <template slot="title">
        配置属性<el-tag>{{prop.name}}</el-tag>
      </template>
      <rva-tcrud :appId="dialogAppId" :requestParams="{rvaAppParams: prop}" @rva-button-click="handleFormButtonClick"></rva-tcrud>
    </el-dialog>
  </el-dropdown-item>
</template>
<script>
import dropdown from '../dropdown';

export default {
  mixins: [dropdown],
  data() {
    return {
      dialogAppIds : ['tcrud1_rva_viewproperty', 'tcrud2_rva_viewproperty', 'tcrud3_rva_viewproperty']
    }
  }
}
</script>
