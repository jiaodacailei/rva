<template>
  <div v-if="appData.id">
    <div slot="header" v-if="showTitle">
      <span>{{ appData.name }}</span>
    </div>
    <el-container>
      <el-main v-if="isPC">
        <el-row v-for="(row1, row1Index) in rowsPropData" :key="row1Index">
          <el-col v-for="(col1, col1Index) in row1.cols" :span="col1.colSpan" :key="row1Index * 100 + col1Index">
            <el-row v-for="(row2, row2Index) in col1.rows" :key="row1Index * 10000 + col1Index * 100 + row2Index">
              <el-col v-for="(col2, col2Index) in row2.cols" :span="col2.span"
                      :key="row1Index * 1000000 + col1Index * 10000 + row2Index * 100 + col2Index">
                <component :is="'rva-' + col2.prop.relatedAppType" :viewId="col2.prop.relatedAppId"
                           :appId="col2.prop.relatedAppId" :ref="col2.prop.id"
                           :requestParams="{...requestParams, portal: appData}"
                           :showTitle="true" :queryContext="queryContext"
                >
                </component>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-main>

      <el-main v-else>
        <div class="app-item" v-for="app in appData.appItems" @click="showApp(app)"><i class="el-icon-s-order"></i>
          <span class="app-item-name">{{ app.name }}</span>
          <el-divider content-position="left"></el-divider>
        </div>
      </el-main>

    </el-container>
  </div>
</template>

<script>
import handleButtonClick from "@/components/rva/handleButtonClick";
import RvaForm from "@/components/rva/form"
import RvaSearch from "@/components/rva/search"
import RvaList from "@/components/rva/list"
import RvaCrud from "@/components/rva/crud"
import RvaTcrud from "@/components/rva/tcrud"
import RvaChart from "@/components/rva/chart"
import RvaKpi from "@/components/rva/kpi"
import {loadCrud, createRowsPropData} from "@/api/rva/crud";
import {RvaUtils} from "@/api/rva/util";

export default {
  name: "rva-portal",
  mixins: [handleButtonClick],
  props: ['appId', 'requestParams', 'showTitle'],
  components: {
    RvaForm, RvaSearch, RvaCrud, RvaChart, RvaList, RvaTcrud, RvaKpi
  },
  data() {
    return {
      // 应用元数据
      appData: {},
      rowsPropData: [],
      contents: [],
      queryParams: {
        trigger: 0,
        treeCrudId: '',
        treeCrudNodeValue: ''
      },
      currentApp: false,
    };
  },
  created() {
    this.reloadView();
  },
  methods: {
    afterCreated() {
    },
    reloadView(action) {
      loadCrud(this.appId).then(response => {
        this.appData = response.data;
        console.log('this.appData=====', this.appData)
        let columns = RvaUtils.parseValue(this.appData.data, "columns", 1);
        let layout = {
          ...this.appData,
          formColumns: columns,
          properties: this.appData.appItems.map(item => {
            let formRowSpan = RvaUtils.parseValue(item.data, "rowSpan", 1);
            let formColSpan = RvaUtils.parseValue(item.data, "colSpan", 1);
            return {...item, formColSpan, formRowSpan}
          })
        }
        this.rowsPropData = createRowsPropData(layout);
        this.afterCreated();
      });
    },
    showApp(app) {
      this.$nextTick(() => {
        let path = RvaUtils.getMenuPath(app.relatedAppId)
        console.log('showAPP path ===', path)
        if (path) {
          this.$router.push(path)
        }
      })

    }
  },
  computed: {
    queryContext() {
      if (RvaUtils.parseValue(this.appData.data, "shareQueryContext", 'N') == 'Y') {
        return {
          pageNum: 1,
          pageSize: 10,
          // 显示搜索条件
          showSearch: true,
          trigger: 0
        }
      }
    }
  }
};
</script>
<style>
.app-item {
  padding: 0 20px;
}

.app-item-name {
  margin-left: 10px;
}

</style>
