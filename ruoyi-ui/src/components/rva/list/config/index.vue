<template>
  <el-dropdown v-if="devMode" @command="executeCommand" :show-timeout="showTimeout" @visible-change="visibleChange">
    <template v-if="prop">
      <span>
        <span :style="nameStyle">{{ showValue || prop.name }}</span>
      </span>
      <el-dropdown-menu slot="dropdown">
        <rva-dropdown-configproperty :prop="prop" command="configProperty" :execute-command="command"
                                     @rva-button-click="handleFormButtonClick"/>
        <rva-dropdown-cloneproperty :prop="prop" command="cloneProperty" :execute-command="command"
                                    @rva-button-click="handleFormButtonClick"/>
        <el-dropdown-item command="deleteProperty" style="color: #f56c6c"><span class="el-icon-delete"></span> 删除属性
        </el-dropdown-item>
        <rva-dropdown-dictionary :prop="prop" command="configDict" :execute-command="command"
                                 @rva-button-click="handleFormButtonClick"/>
        <el-dropdown-item v-if="prop.dictType" command="deleteDict" style="color: #F56C6C"><span
          class="el-icon-notebook-2"></span> 删除字典
        </el-dropdown-item>


        <rva-dropdown-blacklistproperty :prop="prop" command="blacklistproperty" :execute-command="command"
                                        @rva-button-click="handleFormButtonClick"/>

        <el-dropdown-item command="deleteBlacklist" style="color: #F56C6C"><span
          class="el-icon-unlock"></span> 解除黑名单
        </el-dropdown-item>
        <rva-dropdown-synchronize :prop="prop" @rva-button-click="handleFormButtonClick"/>

        <rva-dropdown-exportmeta :prop="prop" :command="['exportAppMeta', 'exportViewMeta']"
                                  :execute-command="command" :requestParams="requestParams"
                                  @rva-button-click="handleFormButtonClick"/>
        <rva-dropdown-exportdoc :prop="prop" :requestParams="requestParams" @rva-button-click="handleFormButtonClick"/>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
  <span v-else>{{ prop ? (showValue || prop.name) : '' }}</span>
</template>

<script>

import config from '@/api/rva/config'
import RvaDropdownDictionary from "@/components/rva/config/dropdown/property/dictionary";
import RvaDropdownConfigproperty from "@/components/rva/config/dropdown/property/configproperty";
import RvaDropdownCloneproperty from "@/components/rva/config/dropdown/property/cloneproperty";
import RvaDropdownBlacklistproperty from "@/components/rva/config/dropdown/property/blacklistproperty";
import RvaDropdownExportmeta from "@/components/rva/config/dropdown/property/exportmeta";
import RvaDropdownSynchronize from "@/components/rva/config/dropdown/property/synchronize";
import RvaDropdownExportdoc from "@/components/rva/config/dropdown/property/exportdoc";

export default {
  components: {
    RvaDropdownDictionary,
    RvaDropdownConfigproperty,
    RvaDropdownCloneproperty,
    RvaDropdownBlacklistproperty,
    RvaDropdownExportmeta,
    RvaDropdownSynchronize,
    RvaDropdownExportdoc
  },
  mixins: [config]
};
</script>

