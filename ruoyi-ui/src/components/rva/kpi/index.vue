<template>
  <el-card v-if="appData">
    <div slot="header" v-if="showTitle">
      <rva-kpi-config :appData="appData" :request-params="requestParams"/>
    </div>
    <rva-search
      v-if="appData.searchId"
      :requestParams="requestParams"
      :queryParams="queryParams"
      :viewId="appData.searchId"
      :selector="selector"
    ></rva-search>
    <el-row v-for="(items, listIndex) in showList" :key="listIndex">
      <el-col v-for="(item, index) in items" :key="index" style="margin-top: 20px;margin-bottom: 20px"
              :span="24/columns * getItemColSpan(item)">
        <el-row :gutter="10">
          <rva-kpi-item :app-data="appData" :item="item" :key="item.id"/>
        </el-row>
      </el-col>
    </el-row>

    <pagination v-if="isPagination&&total > 0"
                :total="total"
                :page.sync="pageNum"
                :limit.sync="pageSize"
                @pagination="getList"
    />
  </el-card>
</template>

<script>
import RvaSearch from "@/components/rva/search";
import RvaForm from "@/components/rva/form";
import RvaKpiConfig from "@/components/rva/kpi/config";
import RvaKpiItem from "@/components/rva/kpi/item";
import {getData} from "@/api/rva/kpi";
import queryContext from "@/components/rva/portal/queryContext";
import {RvaUtils} from "@/api/rva/util";
import handleButtonClick from "@/components/rva/handleButtonClick";

export default {
  components: {
    RvaSearch, RvaKpiConfig, RvaForm, RvaKpiItem
  },
  mixins: [queryContext, handleButtonClick],
  props: ['selector', 'appId', 'requestParams', 'showTitle'],
  created() {
    this.loadData()
  },
  data() {
    return {
      appData: false,
      columns: 3,
      total: 0,
      showList: [],
      pageNum: 1,
      pageSize: 10,
      isPagination: false
    }
  },
  methods: {
    loadData() {
      let params = this.queryParams
      params.pageNum = this.pageNum;
      params.pageSize = this.pageSize;
      getData(this.appId, {...this.requestParams, params}).then(response => {
        this.appData = response.data;
        this.showList = this.appData.itemsList
        this.columns = RvaUtils.parseValue(this.appData.data, 'columns', this.appData.items.length)
        this.setQueryParams(this.appData, true, true);
        this.isPagination = 'Y' == RvaUtils.parseValue(this.appData.data, 'isPagination', 'N')
        if (this.isPagination) {
          this.getList()
        }
      });
    },
    getList() {
      let list = this.appData.itemsList;
      if (list) {
        this.total = list.length
        let pageNum = this.pageNum;
        let pageSize = this.pageSize
        console.log('kpi getList pageNum', pageNum)
        this.showList = this.getPageList(list, pageNum, pageSize)
      }
    },
    getPageList(list, page, pageSize) {
      let array = []
      let startIdx = (page - 1) * pageSize;
      let cpList = RvaUtils.cloneDeep(list)
      array = cpList.splice(startIdx, pageSize)
      return array
    },
    getItemColSpan(item) {
      return RvaUtils.parseValue(item.data, 'colSpan', 1)
    }
  },
  watch: {
    requestParams: {
      handler() {
        this.loadData()
      },
      deep: true
    },
    queryParams: {
      handler(newVal, oldVal) {
        if (JSON.stringify(newVal) != JSON.stringify(oldVal)) {
          this.loadData()
        }
      },
      deep: true
    }
  }
};
</script>
