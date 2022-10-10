<template>
  <el-form v-model="queryParams2" ref="queryForm" :inline="inline" v-show="queryParams2.showSearch" label-width="68px"
           v-if="viewData.properties && viewData.properties.length">
    <component
      v-for="p in viewData.properties"
      v-bind:is="'rva-search-' + p.type"
      v-model="queryParams2[p.id]"
      :prop="p" :key="getPropKey(p)" :viewData="viewData" :requestParams="requestParams">
    </component>
    <el-form-item>
      <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import RvaSearchTextfield from "@/components/rva/search/textfield";
import RvaSearchSelect from "@/components/rva/search/select";
import RvaSearchSelector from "@/components/rva/search/selector";
import RvaSearchRadio from "@/components/rva/search/radio";
import RvaSearchCheckbox from "@/components/rva/search/checkbox";
import RvaSearchDatetimeRange from "@/components/rva/search/datetimerange";
import RvaSearchDeptTree from "@/components/rva/search/depttree";

import {getRvaViewData} from "@/api/rva/crud";
import RvaSearchConfig from "@/components/rva/search/config";
import Vue from "vue";
import {RvaUtils} from "@/api/rva/util";

Vue.component('rva-search-config', RvaSearchConfig)

export default {
  name: "RvaSearchView",
  props: ['viewId', 'queryParams', 'requestParams'],
  components: {
    "rva-search-textfield": RvaSearchTextfield,
    "rva-search-select": RvaSearchSelect,
    "rva-search-selector": RvaSearchSelector,
    "rva-search-radio": RvaSearchRadio,
    "rva-search-checkbox": RvaSearchCheckbox,
    "rva-search-datetimerange": RvaSearchDatetimeRange,
    "rva-search-depttree": RvaSearchDeptTree
  },
  data() {
    return {
      viewData: {},
      originalFormData: {},
      queryParams2:this.queryParams
    };
  },
  mounted() {
    // 设置this.viewData
   // this.queryParams2 = RvaUtils.cloneDeep(this.queryParams)
    getRvaViewData(this.viewId, this.requestParams, this, res => {
      this.viewData.properties.forEach(p => {
        if (RvaUtils.isEmpty(p.formInitValue)) {
          return;
        }
        let params = this.queryParams
        params[p.id] = RvaUtils.parseJsValue(p.formInitValue, {...this.requestParams},true);
        this.queryParams2 = {...params}

        console.log('  this.queryParams2  ' , this.queryParams2)
      })
    })
    console.log('search viewdata', this.viewData)
  },
  methods: {
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.trigger++; // 以此使queryParams变化，从而触发loadList
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.$router.go(0);
      // this.viewData.properties.forEach(prop => {
      //   this.queryParams[prop.id] = undefined;
      // })
      // RvaUtils.clone(this.originalFormData, this.queryParams);
      // this.$nextTick(() => {
      //   this.resetForm('queryForm')
      //   this.handleQuery();
      // })
    },
    getPropKey(prop) {
      if (prop.type == 'textfield') {
        return prop.id;
      }
      return prop.id + new Date().getTime()
    }
  },
  watch: {
    requestParams: {
      handler() {
        getRvaViewData(this.viewId, this.requestParams, this, res => {
          RvaUtils.clone(this.formData, this.originalFormData, true);
        })
      },
      deep: true
    },
    queryParams2: {
      handler(newVal,oldVal) {
        this.$emit("update:queryParams",newVal)
      },
      deep: true
    },

  },
  computed: {
    inline() {
      return RvaUtils.isPC();
    }
  }
};
</script>
