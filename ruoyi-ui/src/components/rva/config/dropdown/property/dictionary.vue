<template>
  <el-dropdown-item :command="command" divided>
    <span>
      <span class="el-icon-notebook-2">
      </span> {{ prop.dictType ? '修改字典' : '新建字典' }}
    </span>
    <el-dialog :visible.sync="open" width="1200px" append-to-body v-if="open">
      <template v-if="prop.dictType">
        <template slot="title">
          修改字典
          <el-tag>{{ prop.dictType }}</el-tag>
        </template>
        <rva-form viewId="u0_sys_dict_type" :requestParams="{dict_type: prop.dictType}"
                  @rva-button-click="handleFormButtonClick"
        ></rva-form>
      </template>
      <template v-else>
        <template slot="title">
          新建字典
        </template>
        <rva-form viewId="c0_sys_dict_type" :requestParams="{rvaAppParams: prop}"
                  @rva-button-click="handleFormButtonClick"
        ></rva-form>
      </template>
    </el-dialog>
  </el-dropdown-item>
</template>
<script>
import dropdown from '../dropdown';

export default {
  mixins: [dropdown]
}
</script>
