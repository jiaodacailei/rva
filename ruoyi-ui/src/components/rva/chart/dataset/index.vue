<template>
    <el-card v-if="dsMeta">
      <div slot="header" v-if="showTitle">
        <span>{{ dsMeta.name }}</span>
      </div>
      <rva-search
        v-if="dsMeta.searchId"
        :requestParams="requestParams"
        :queryParams="queryParams"
        :viewId="dsMeta.searchId"
        :selector="selector"
      ></rva-search>
      <el-table :data="dsData">
        <el-table-column label="行号" type="index" :index="formatIndex"></el-table-column>
        <template v-if="dsMeta.dataSql">
          <template v-if="dsData.length > 0">
            <el-table-column :label="`列${col - 1}`" v-for="col in getColumns()" :prop="'column' + (col - 1)" :key="col"></el-table-column>
          </template>
        </template>
        <template v-else-if="dsMeta.categoryColumns.length == 1">
          <el-table-column :label="`列${index}`" v-for="(col,index) in dsMeta.columns" :prop="col.viewpropertyId" :key="col.id"></el-table-column>
        </template>
        <template v-else></template>

      </el-table>
    </el-card>
</template>

<script>
import RvaSearchView from "@/components/rva/search";
import {getDataset, loadChart} from "@/api/rva/chart";

export default {
  components: {
    "rva-search": RvaSearchView,
  },
  props: ['selector', 'appId', 'requestParams', 'showTitle'],
  created() {
    this.getDataset()
  },
  data() {
    return {
      dsMeta: false,
      dsData: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        // 显示搜索条件
        showSearch: true,
        trigger: 0
      }
    }
  },
  methods: {

    getDataset() {
      getDataset(this.appId, {...this.requestParams, ...this.queryParams}).then(res => {
        this.dsData = res.data.dsData
        this.dsMeta = res.data.dsMeta
      })
    },
    formatIndex(index) {
      return index;
    },
    getColumns () {
      return this.dsData[0].columns
    }
  },
  watch: {
    queryParams: {
      handler() {
        this.getDataset()
      },
      deep: true
    }
  }
};
</script>
