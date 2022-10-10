<template>
  <el-form-item :label="prop.name" :prop="prop.id" :label-width="labelWidth">
    <template slot="label">
      <rva-search-config :prop="prop"></rva-search-config>
    </template>
    <el-radio-group v-bind:value="value" @input="$emit('input', $event)">
      <el-radio
        v-for="dict in dictData"
        :key="dict.dictValue"
        :label="dict.dictValue"
      >{{ dict.dictLabel }}
      </el-radio>
    </el-radio-group>
  </el-form-item>
</template>

<script>

import search from "@/api/rva/search";

export default {
  mixins: [search],
  name: "RvaFormRadio",
}
</script>
