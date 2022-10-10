<template>
  <div>
    <template v-if="viewData.id">
      <el-row :gutter="10" class="mb8">
        <template v-if="!selector && !readonly">
          <el-col :span="1.5" v-for="btn in viewData.buttons.filter(b => b.position == 'top')" :key="btn.id">
            <rva-list-top-button
              :viewButtonData="btn" :disabled="isButtonDisabled(btn)" :selection="selection"
              @rva-button-click="handleButtonClick" :requestParams="requestParams" :viewData="viewData"
            ></rva-list-top-button>
          </el-col>
        </template>
        <right-toolbar :showSearch.sync="queryParams.showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
      <component :is="'rva-list-' + listComponent" :loading="loading" :listData="listData" :viewData="viewData"
                 :orderByList="orderByList"  :summariesData="summariesData"
                 :selector="selector || readonly" @rva-button-click="handleButtonClick" @rvaListReload="getList"
                 @rvaListSelection="handleSelectionChange"
                 :selectLimits="selectLimits" :requestParams="requestParams"
      ></component>
      <pagination
        v-show="total > 0"
        :total="total"
        :small="!isPC"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </template>
  </div>
</template>

<script>

import RvaListViewTopButton from "@/components/rva/list/button/top";

import {listCrud, listColumn, handleRvaButtonClick, getRvaViewData} from "@/api/rva/crud";
import {RvaUtils} from "@/api/rva/util";
import RvaListTable from "@/components/rva/list/table";
import RvaListMobile from "@/components/rva/list/mobile";
import RvaListImage from "@/components/rva/list/image";

import RvaButtonConfig from "@/components/rva/button/config";
import Vue from "vue";
import {debounce} from "@/utils";

Vue.component('rva-button-config', RvaButtonConfig)

export default {
  name: "RvaListView",
  props: ['viewId', 'queryParams', 'requestParams', 'selector', 'selectLimits'],
  components: {
    RvaListTable,
    RvaListMobile,
    RvaListImage,
    "rva-list-top-button": RvaListViewTopButton
  },
  data() {
    return {
      dicts: {},
      // 遮罩层
      loading: true,
      // 当列表点击时，列表上面按钮是否禁用
      buttonDisabled: {},
      // 选中数组
      selection: [],
      // 总条数
      total: 0,
      viewData: {},
      // 模块表格数据
      listData: [],
      // 模块表格合计行数据
      summariesData: [],
      orderByList: [],
      listComponent: 'table',
      path: '',
      readonly: false,
      debounceList: null
    };
  },
  created() {
    this.debounceList = debounce(this.list, 1000);
    this.reloadView();
  },
  methods: {
    init() {
      this.listComponent = RvaUtils.parseValue(this.viewData.data, 'listComponent', 'table');
      if (this.listComponent == 'table' && !RvaUtils.isPC()) {
        this.listComponent = 'mobile'
      }
      this.readonly = this.viewData.formReadonly == 'Y';
      this.queryParams.pageSize = this.isPC ? this.viewData.listRows : 5;
      // console.log('viewData', this.viewData);
      var orderProps = this.viewData.properties.filter(p => p.listOrderIdx >= 0).sort(function (p1, p2) {
        return p1.listOrderIdx - p2.listOrderIdx;
      });
      // console.log('orderProps', orderProps);
      for (var i in orderProps) {
        this.orderByList.push({
          prop: orderProps[i].id,
          order: orderProps[i].listOrderType == 'DESC' ? 'descending' : 'ascending'
        });
      }
      this.viewData.properties.forEach(p => {
        if (p.dictType) {
          RvaUtils.getDicts(p.dictType, this.dicts, p.dictType, () => {
            this.dicts[p.dictType] = this.dicts[p.dictType].map(function (e) {
              return {
                value: e.dictValue,
                label: e.dictLabel,
                raw: {
                  listClass: e.listClass,
                  cssClass: e.cssClass
                }

              };
            })
            this.listData.push({})
            this.listData.pop()
          })
        }
      })
    },
    reloadView(action) {
      this.viewData = {};
      this.listData = [];
      getRvaViewData(this.viewId, this.requestParams, this, (data, code) => {
        if (code == 0) {
          this.init();
          this.getList();
        }
      })
    },
    list() {
      this.loading = true;
      this.queryParams.orderByList = this.orderByList;
      let params = RvaUtils.clone(this.queryParams, RvaUtils.clone(this.requestParams, {}, true), true);
      listCrud(this.viewData.loadUrl, params).then(response => {
        this.listData = response.rows;
        this.summariesData = response.summariesData;
        this.total = response.total ? response.total : 0;
        this.viewData.properties.forEach((p) => {
          if (p.formInputorData) {
            response.rows.forEach((r) => {
              if (RvaUtils.isEmpty(r[p.id])) {
                return;
              }
              listColumn(p.formInputorData, r[p.id]).then(res => {
                let names = [];
                res.rows.forEach((r2) => {
                  names.push(r2[r2.namePropId]);
                });
                r[p.id] = names.join(',');
              });
            });
          }
        });
        this.loading = false;
      });
    },
    /** 查询模块列表 */
    getList() {
      this.debounceList()
    },
    handleSelectionChange(selection) {
      this.selection = selection;
      this.$emit('rvaListSelection', this.selection)
    },
    isButtonDisabled(btn) {
      if (!btn.selectType) {
        return false;
      }
      // console.log ('isButtonDisabled', btn, this.selection);
      // selectType: none single singleornone multiple
      if (this.selection.length == 0) {
        if (btn.selectType.indexOf('none') >= 0) {
          return false;
        }
        return true;
      }
      if (this.selection.length == 1) {
        return false;
      }
      if (btn.selectType.indexOf('single') >= 0) {
        return true;
      }
      return false;
    },
    loadList(action) {// handleRvaButtonClick中会调用
      this.getList();
    },
    /** 处理按钮点击事件 */
    handleButtonClick(data) {
      handleRvaButtonClick(this, data);
    }
  },
  watch: {
    queryParams: {
      handler() {
        this.getList();
      },
      deep: true
    },
    requestParams: {
      handler() {
        this.getList();
      },
      deep: true
    },
    // 当前路由显示时，刷新列表数据；当路由第一次加载时，不会触发本方法
    '$route.path': function (val, oldVal) {
      if (this.path == '') {// 第一次触发，是在离开本页面路由时，所以oldVal就是当前页面的路由
        this.path = oldVal;
      }
      if (val == this.path) {
        console.log('$route.path', val, oldVal);
        this.getList()
      }
    }
  }
};
</script>
