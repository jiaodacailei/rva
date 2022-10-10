<template>
  <el-dropdown-item :command="command" divided>
    <span>
      <span class="el-icon-lock"></span> 黑名单
    </span>
    <el-dialog :visible.sync="open" width="1200px" append-to-body v-if="open">
      <template slot="title">
        黑名单
        <el-tag>{{ prop.name }}</el-tag>
      </template>
      <rva-form :view-id="viewId" :requestParams="{rvaAppParams: prop}"
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
      viewId: 'c0_none_heimingdan',
    }
  }
}
</script>
