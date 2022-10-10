<template>
  <el-container v-if="chartData">
    <el-main>
      <rva-chart :appId="appId" :requestParams="requestParams" :show-title="true"></rva-chart>
      <el-tabs type="border-card" v-model="tabNameDs">
        <el-tab-pane v-for="(dataset, index) in chartData.datasetList" :label="`预览数据集-${index}`" :name="dataset.id" :key="index">
          <rva-chart-dataset :appId="dataset.id" :requestParams="requestParams"></rva-chart-dataset>
        </el-tab-pane>
      </el-tabs>
    </el-main>
    <el-aside width="680px" style="padding-top: 18px">
      <el-tabs type="border-card" v-model="tabName">
        <template v-for="(dataset, index) in chartData.datasetList">
          <el-tab-pane :label="`数据集-${index}`" :name="dataset.id">
            <rva-form viewId="u0_rva_chart_dataset" :requestParams="{...requestParams, selection: [{'keyPropValue': dataset.id}]}" @rva-button-click="handleButtonClick" :key="dataset.id"></rva-form>
          </el-tab-pane>
        </template>
        <template v-for="(grid, index) in chartData.gridList">
          <el-tab-pane :label="`坐标系-${index}`" :name="grid.id">
            <rva-form viewId="u0_rva_chart_grid" :requestParams="{...requestParams, selection: [{'keyPropValue': grid.id}]}" @rva-button-click="handleButtonClick" :key="grid.id"></rva-form>
          </el-tab-pane>
        </template>
        <template v-for="(series, index) in chartData.seriesList">
          <el-tab-pane :label="`系列-${index}`" :name="series.id">
            <rva-form viewId="u0_rva_chart_series" :requestParams="{...requestParams, selection: [{'keyPropValue': series.id}]}" @rva-button-click="handleButtonClick" :key="series.id"></rva-form>
          </el-tab-pane>
        </template>
        <el-tab-pane label="基本信息">
          <rva-form viewId="u0_rva_chart" :requestParams="requestParams" @rva-button-click="handleButtonClick"></rva-form>
        </el-tab-pane>
      </el-tabs>

    </el-aside>
  </el-container>
</template>

<script>
import {loadChart} from "@/api/rva/chart";
import initApp from "@/views/rva/initApp";
import RvaCrud from "@/components/rva/crud";
import RvaForm from "@/components/rva/form";
import RvaChartDataset from "@/components/rva/chart/dataset";
import RvaChart from "@/components/rva/chart";
import handleButtonClick from "@/components/rva/handleButtonClick";
export default {
  name: "RvaChartSetting",
  mixins: [initApp, handleButtonClick],
  components: {
    RvaCrud,
    RvaForm,
    RvaChartDataset,
    RvaChart
  },
  data() {
    return {
      chartData : false,
      tabName : '',
      tabNameDs: ''
    }
  },
  created() {
    loadChart(this.appId).then(response => {
      this.chartData = response.data;
      this.tabNameDs = this.tabName = this.chartData.datasetList[0].id;
    });
  },
  methods: {
    loadList() {

    }
  }
}
</script>

<style scoped>

</style>
