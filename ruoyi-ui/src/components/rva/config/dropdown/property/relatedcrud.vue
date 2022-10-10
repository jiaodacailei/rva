<template>
  <div>
    <el-dropdown-item v-if="relatedCruds.length > 0" disabled divided><span class="el-icon-s-tools"/> 关联CRUD
    </el-dropdown-item>
    <template v-for="crud in relatedCruds">
      <el-dropdown-item :command="{name : command[0], crud}"><span
        class="el-icon-setting dropdown-item2"/>{{ ' ' + crud }}
      </el-dropdown-item>
      <el-dropdown-item :command="{name : command[1], crud}"><span
        class="el-icon-info dropdown-item2"/>{{ ' 使用情况' }}
      </el-dropdown-item>
      <el-dropdown-item :command="{name : command[2], crud}"><span
        class="el-icon-download dropdown-item2"/>{{ ' 元数据' }}
      </el-dropdown-item>

    </template>
    <el-dialog :visible.sync="open" width="900px" append-to-body v-if="open">
      <template v-if="executeCommand.name == command[0]">
        <template slot="title">
          <el-tag>{{ prop.name }}</el-tag>
          关联CRUD
        </template>
        <rva-crud :appId="executeCommand.crud" :requestParams="{rvaAppParams: prop}" @rva-button-click="handleFormButtonClick"></rva-crud>
      </template>
      <template v-else-if="executeCommand.name == command[1]">
        <template slot="title">
          <el-tag>{{ prop.name }}</el-tag>
          关联CRUD使用情况
        </template>
        <rva-form viewId="c0_none_shiyongqingkuang"
                  :requestParams="{selection: [{keyPropValue: executeCommand.crud}]}"
                  @rva-button-click="handleFormButtonClick"
        ></rva-form>
      </template>
      <template v-else>
        <template slot="title">
          <el-tag>{{ executeCommand.crud }}</el-tag>
          元数据
        </template>
        <rva-form viewId="c0_none_yuanshujujiaoben"
                  :requestParams="{selection: [{keyPropValue: executeCommand.crud}]}"
                  @rva-button-click="handleFormButtonClick"
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
    return {}
  },
  methods: {},
  computed: {
    relatedCruds() {
      if (RvaUtils.isEmpty(this.prop.formRelatedCrud)) {
        return [];
      }
      return this.prop.formRelatedCrud.split(',');
    }
  }
}
</script>

<style>
.dropdown-item2 {
  margin-left: 20px
}
</style>
