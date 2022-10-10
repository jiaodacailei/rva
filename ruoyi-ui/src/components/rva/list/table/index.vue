<template>
  <el-table v-loading="loading" :data="listData" @selection-change="handleSelectionChange"
            @header-click="handleHeaderCLick" :header-cell-class-name="handleHeaderClass"
            :tree-props="{children: 'children', hasChildren: 'hasChildren'}" row-key="keyPropValue"
            @sort-change="handleSortChange"
            :show-summary="showSummary" :summary-method="getSummaries"
            :row-class-name="tableRowClassName" style="width: 100%" ref="rvaList">
    <el-table-column type="selection" width="55" align="center" fixed/>
    <template v-for="p in viewData.properties">
      <template v-if="p.type == 'button'">
        <el-table-column v-if="!selector"
                         :label="p.name" align="center" class-name="small-padding fixed-width"
                         :key="p.id" fixed="right" :width="getPropButtonSize(p) * 75" scoped-slot
        >
          <template slot="header">
            <rva-list-config :prop="p" :requestParams="requestParams"></rva-list-config>
          </template>
          <template slot-scope="scope">
            <el-row>
              <el-col v-for="(btn, btnIndex) in getPropButtons(p, scope.row)"
                      :span="getPropButtonColSpan(p, scope.row)"
                      v-if="getPropButtons(p, scope.row).length <= 4 || btnIndex < 3" :key="p.id + '-' + btn.id">
                <rva-list-inner-button
                  :viewButtonData="btn"
                  :selection="[scope.row]"
                  :requestParams="requestParams"
                  @rva-button-click="handleButtonClick"
                  :viewData="viewData"
                ></rva-list-inner-button>
              </el-col>
              <el-col :span="6" v-if="getPropButtons(p, scope.row).length > 4">
                <el-dropdown>
                  <el-button type="text" size="mini">
                    更多<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-for="(btn, btnIndex) in getPropButtons(p, scope.row)"
                                      :key="p.id + '-' + btn.id"
                                      v-if="btnIndex > 2">
                      <rva-list-inner-button
                        :viewButtonData="btn"
                        :selection="[scope.row]"
                        :requestParams="requestParams"
                        @rva-button-click="handleButtonClick"
                        :viewData="viewData"
                      ></rva-list-inner-button>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
      </template>
      <component v-else  :is="'rva-list-column-' + p.type" :prop="p" @reloadView="loadList" :requestParams="requestParams">
      </component>
    </template>
  </el-table>
</template>

<script>

import RvaListViewInnerButton from "@/components/rva/list/button/inner";
import RvaListConfig from "@/components/rva/list/config"; // 如果el-column上使用render-header，则RvaListConfig必须在vue中全局注册才能使用
import RvaListColumnDate from "@/components/rva/list/column/date";
import RvaListColumnDictionary from "@/components/rva/list/column/dictionary";
import RvaListColumnDateTime from "@/components/rva/list/column/datetime";
import RvaListColumnFile from "@/components/rva/list/column/file";
import RvaListColumnHidden from "@/components/rva/list/column/hidden";
import RvaListColumnMenuIcon from "@/components/rva/list/column/menuicon";
import RvaListColumnIcon from "@/components/rva/list/column/icon";
import RvaListColumnLink from "@/components/rva/list/column/link";
import RvaListColumnSwitch from "@/components/rva/list/column/switch";
import RvaListColumnTextField from "@/components/rva/list/column/textfield";
import RvaListColumnVideo from "@/components/rva/list/column/video";
import RvaListColumnImage from "@/components/rva/list/column/image";
import RvaListColumnCascader from "@/components/rva/list/column/cascader";

import {handleRvaButtonClick} from "@/api/rva/crud";
import {RvaUtils} from "@/api/rva/util";

export default {
  name: "RvaListTable",
  props: ['viewData', 'listData', 'requestParams', 'orderByList', 'loading', 'selector', 'selectLimits', 'summariesData'],
  components: {
    "rva-list-inner-button": RvaListViewInnerButton,
    "rva-list-config": RvaListConfig,
    "rva-list-column-date": RvaListColumnDate,
    "rva-list-column-dictionary": RvaListColumnDictionary,
    "rva-list-column-datetime": RvaListColumnDateTime,
    "rva-list-column-file": RvaListColumnFile,
    "rva-list-column-hidden": RvaListColumnHidden,
    "rva-list-column-menuicon": RvaListColumnMenuIcon,
    "rva-list-column-icon": RvaListColumnIcon,
    "rva-list-column-link": RvaListColumnLink,
    "rva-list-column-switch": RvaListColumnSwitch,
    "rva-list-column-textfield": RvaListColumnTextField,
    "rva-list-column-video": RvaListColumnVideo,
    RvaListColumnImage,
    RvaListColumnCascader
  },
  data() {
    return {
      // 选中数组
      selection: [],
      orders: ['descending', 'ascending', null],

    };
  },
  computed: {
    showSummary() {
      return RvaUtils.isNotEmpty(this.summariesData);
    }
  }
  ,
  created() {
    this.summariesData
  },
  methods: {
    // 多选框选中数据
    handleSelectionChange(selection) {
      let limits = this.selectLimits || 99999999;
      // let limits = 2;
      if (selection.length > limits) {
        for (let i = selection.length - limits - 1; i >= 0; i--) {
          this.$refs.rvaList.toggleRowSelection(selection[i], false);
          selection.splice(i, 1);
        }
      }
      this.selection = selection;
      this.$emit('rvaListSelection', this.selection)
    },
    getPropButtonSize(p) {
      let size = 0;
      for (var i in this.listData) {
        let val = this.listData[i][p.id]
        if (val) {
          let len = this.listData[i][p.id].length;
          if (len > size) {
            size = len;
          }
        } else {
          console.log('getPropButtonSize-undefined', p.id);
        }
      }
      if (size > 4) {
        size = 4;
      }
      return size;
    },
    getPropButtonColSpan(p, row) {
      let size = this.getPropButtons(p, row).length;
      if (size > 4) {
        size = 4;
      }
      return 24 / size;
    },
    getPropButtons(p, row) {
      return row[p.id];
    },
    handleHeaderClass({row, column, rowIndex, columnIndex}) {
      if (column.sortable != 'custom') {
        return;
      }
      let result = this.orderByList.find(e => e.prop === column.property)
      if (result) {
        column.order = result.order;
      }
    },
    handleHeaderCLick(column) {
      if (column.sortable != 'custom') {
        return;
      }
      let orderIndex = (this.orders.indexOf(column.order) + 1) % 3;
      let result = this.orderByList.find(e => e.prop === column.property)
      if (result) {
        result.order = this.orders[orderIndex];
      } else {
        this.orderByList.push({prop: column.property, order: this.orders[orderIndex]});
      }
      this.$emit('rvaListReload');
    },
    /** 排序触发事件 */
    handleSortChange(column, prop, order) {
      let result = this.orderByList.find(e => e.prop === column.prop)
      if (result) {
        result.order = column.order;
      } else {
        this.orderByList.push({prop: column.prop, order: column.order});
      }
      this.$emit('rvaListReload');
    },
    loadList(action) {// handleRvaButtonClick中会调用
      this.$emit('rvaListReload');
    },
    /** 处理按钮点击事件 */
    handleButtonClick(data) {
      handleRvaButtonClick(this, data);
    },

    /**
     * 合计行数据
     * @param param
     */
    getSummaries(param) {
      let array = [];
      //选择框
      array.push("");
      let summariesData = this.summariesData;
      this.viewData.properties.forEach((p) => {
        let viewPropertyId = p.id;
        array.push(summariesData[viewPropertyId])
      });
      return array;
    },
    /**
     primary: '#409EFF',
     info: '#909399',
     success: '#67C23A',
     warning: '#E6A23C',
     danger: '#F56C6C'
     *
     * @param row
     * @param rowIndex
     * @returns {string}
     */
    tableRowClassName({row, rowIndex}) {
      let express = RvaUtils.parseValue(this.viewData.data, "alertExpression")
      if (RvaUtils.isNotEmpty(express)) {
        let result = RvaUtils.parseJsValue(express, row);
        if (typeof result == 'boolean' && result) {
          return 'danger-row';
        } else if (typeof result == 'string') {
          return result + '-row';
        }
      }
      return '';
    }
  }
};
</script>
<style>
.el-table .danger-row {
  background: rgb(253, 226, 226);
}

.el-table .success-row {
  background: #f0f9eb;
}

.el-table .primary-row {
  background: #95bae3;
}

.el-table .warning-row {
  background: #e9ddc8;

}

.el-table .info-row {
  background: #ccd1db;

}

</style>
