<template>
  <el-card v-if="appData">
    <div slot="header" v-if="showTitle">
      <rva-chart-config :appData="appData" :request-params="requestParams"/>
    </div>
    <rva-search
      v-if="appData.searchId"
      :requestParams="requestParams"
      :queryParams="queryParams"
      :viewId="appData.searchId"
      :selector="selector"
    ></rva-search>
    <echarts v-if="dataset" :option="getChartOption()"></echarts>
  </el-card>
</template>

<script>
import RvaSearch from "@/components/rva/search";
import {getDataset, loadChart} from "@/api/rva/chart";
import RvaChartDataset from "@/components/rva/chart/dataset";
import RvaChartConfig from "@/components/rva/chart/config";
import Echarts from "@/components/Echarts";
import queryContext from "@/components/rva/portal/queryContext";

export default {
  components: {
    RvaSearch, RvaChartDataset, Echarts, RvaChartConfig
  },
  mixins: [queryContext],
  props: ['selector', 'appId', 'requestParams', 'showTitle'],
  created() {
    this.loadChart()
  },
  data() {
    return {
      appData: false,
      dataset: false,
    }
  },
  methods: {
    loadChart() {
      loadChart(this.appId).then(response => {
        this.appData = response.data;
        this.setQueryParams(this.appData);
        this.getDataset();
      });
    },
    getDataset() {
      if (!this.appData) {
        return;
      }
      let dsId = this.appData.datasetList[0].id;
      getDataset(dsId, {...this.requestParams, ...this.queryParams}).then(res => {
        this.dataset = res.data
      })
    },
    getAxis(name) {
      return this.appData.axisList.find(axis => axis.id == this.appData.gridList[0][name])
    },
    getChartOption() {
      return {
        legend: {},
        tooltip: {},
        dataset: {
          source: this.dataset.dsArrayData
        },
        xAxis: {
          type: this.getAxis('axisX0').type,
          name: this.getAxis('axisX0').name
        },
        yAxis: {
          type: this.getAxis('axisY0').type,
          name: this.getAxis('axisY0').name
        },
        series: this.appData.seriesList.map(series => {
          return {
            type: series.type == 'inherit' ? this.appData.type : series.type,
            seriesLayoutBy: series.dataType
          }
        })
      };
    }
  },
  watch: {
    queryParams: {
      handler() {
        this.getDataset()
      },
      deep: true
    },
    requestParams: {
      handler() {
        this.getDataset()
      },
      deep: true
    }
  }
};
</script>
