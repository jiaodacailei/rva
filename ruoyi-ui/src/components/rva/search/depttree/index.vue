<template>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop"></rva-form-config>
      </template>
      <treeselect v-model="value2" :options="deptOptions"
                  @input="$emit('input', $event)"
                  :show-count="true" placeholder="请选择归属部门" style="width: 200px"/>
    </el-form-item>
</template>

<script>

import formInput from "@/api/rva/form-input";
import {RvaUtils} from "@/api/rva/util";


import {treeselect as deptTreeselect} from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {treeselect as menuTreeselect} from "@/api/system/menu";


export default {
  mixins: [formInput],
  components: {Treeselect},
  data() {
    return {
      max: 20,
      value2: this.value,
      deptOptions: [],
    }
  },
  mounted() {
    this.getDeptTreeselect();
  },

  methods: {
    afterCreated() {
      this.max = RvaUtils.parseInt(this.prop.formValueMax, 20);
    },
    /** 查询菜单树结构 */
    getDeptTreeselect() {
      deptTreeselect().then(response => {
        this.deptOptions = response.data;
      });
    },

  }
}
;
</script>
<style>


</style>

