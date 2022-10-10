<template>
  <div>
    <el-dropdown-item divided>
      <span class="el-icon-download"/>{{ ' 元数据' }}
    </el-dropdown-item>
    <el-dropdown-item :command="{name : 'exportAppMeta', appId}" :key="prop.id + '-exportAppMeta'">
      <span class="el-icon-download dropdown-item2"/>{{ ' 应用元数据' }}
    </el-dropdown-item>
    <el-dropdown-item :command="{name : 'exportViewMeta'}" :key="prop.id + '-exportViewMeta'">
      <span class="el-icon-download dropdown-item2"/>{{ ' 视图元数据' }}
    </el-dropdown-item>
    <el-dialog :visible.sync="open" width="900px" append-to-body v-if="open">
      <template v-if="executeCommand.name == command[0]">
        <template slot="title">
          应用元数据
        </template>
        <rva-form viewId="c0_none_yuanshujujiaoben" @rva-button-click="handleFormButtonClick"
                  :requestParams="{selection: [{keyPropValue: appId}]}"
        ></rva-form>
      </template>
      <template v-else>
        <template slot="title">
          视图元数据
        </template>
        <rva-form viewId="c0_none_shituyuanshujujiaoben" @rva-button-click="handleFormButtonClick"
                  :requestParams="{selection: [{keyPropValue: prop.viewId}]}"
        ></rva-form>
      </template>
    </el-dialog>
  </div>
</template>
<script>
import dropdown from '../dropdown';
import {RvaUtils} from "@/api/rva/util";

export default {
  mixins: [dropdown],
  data() {
    return {
      appId: false
    }
  },
  mounted() {
    this.appId = this.requestParams.appId;
    console.log('this.appId', this.appId, this.prop.id)
  },
}
</script>

<style>
.dropdown-item2 {
  margin-left: 20px
}
</style>
