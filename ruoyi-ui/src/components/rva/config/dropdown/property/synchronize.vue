<template>
  <el-dropdown-item divided>
    <span @click="open = true">
      <span class="el-icon-lock"></span> 同步配置
    </span>
    <el-dialog :visible.sync="open" width="600px" append-to-body v-if="open">
      <template slot="title">
        同步配置
        <el-tag>{{ prop.name }}</el-tag>(将所选属性的配置，同步到所选的视图中对应的属性上去)
      </template>
      <rva-form view-id="c0_none_tongbupeizhi" :requestParams="{rvaAppParams: prop}"
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
      viewId: 'c0_none_tongbupeizhi',
    }
  }
}
</script>
