<template>
  <div class="img_main">
    <el-row :gutter="gutter">

      <el-col :span="6" class="img_item" v-for="row in listData"
              :key="row.keyPropertyValue"
      >
        <el-card :body-style="{ padding: '5px' }">
          <el-image :src="getImageUrl(row)" :preview-src-list="getImages(row)" style="height: 300px"></el-image>
          <div class="dec">
              <span class="name">
                 <rva-list-config :prop="viewData.properties[1]" :show-value="row[viewData.properties[1].id]"
                                  :requestParams="requestParams">
                 </rva-list-config>
              </span>
            <span class="state">
                 <rva-list-config :prop="viewData.properties[3]" :show-value="row[viewData.properties[3].id]+''"
                                  :requestParams="requestParams">
                </rva-list-config>
              </span>
          </div>
          <el-divider style="margin: 6px 0;"></el-divider>
          <el-row>
            <el-col :span="8" v-for="btn in getInnerButtons(row)" :key="btn.id">
              <rva-list-inner-button
                :viewButtonData="btn"
                :selection="[row]"
                :requestParams="requestParams"
                @rva-button-click="handleButtonClick"
                :viewData="viewData"
              ></rva-list-inner-button>
            </el-col>
          </el-row>

        </el-card>
      </el-col>

    </el-row>
    <el-dialog :visible.sync="open" width="800px" append-to-body v-if="open && formId">
      <template slot="title">
        表单
      </template>
      <rva-form :viewId="formId" :requestParams="formParams" @rva-button-click="handleButtonClick"></rva-form>
    </el-dialog>
  </div>
</template>

<script>

import RvaListConfig from "@/components/rva/list/config"; // 如果el-column上使用render-header，则RvaListConfig必须在vue中全局注册才能使用

import {handleRvaButtonClick} from "@/api/rva/crud";
import {RvaUtils} from "@/api/rva/util";
import {getFileUrl} from "@/utils/ruoyi"
import RvaListViewInnerButton from "@/components/rva/list/button/inner";

export default {
  name: "RvaListTable",
  props: ['viewData', 'listData', 'requestParams', 'orderByList', 'loading', 'selector', 'selectLimits'],
  components: {
    "rva-list-config": RvaListConfig,
    "rva-list-inner-button": RvaListViewInnerButton,
  },
  data() {
    return {
      gutter: 20,
      // 选中数组
      selection: [],
      orders: ['descending', 'ascending', null],
      formId: false,
      formParams: {},
      open: false
    };
  },
  created() {
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
        let len = this.listData[i][p.id].length;
        if (len > size) {
          size = len;
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
    getLinkUrl(p, row) {
      let url = RvaUtils.parseValue(p.data, 'imageLink');
      if (!url) {
        return '';
      }
      return RvaUtils.parseJsValue(url, row);
    },
    getImages(row) {
      let images = row[this.viewData.properties[2].id];
      if (RvaUtils.isEmpty(images)) {
        return [require("@/assets/images/profile.jpg")];
      }
      return images.split(',').map(img => getFileUrl(img))
    },
    getImageUrl(row) {
      return this.getImages(row)[0]
    },
    getInnerButtons(row) {
      let operationProp = this.viewData.properties.find(p => p.type == 'button');
      if (!operationProp) {
        return []
      }
      let buttons = row[operationProp.id];
      if (!buttons) {
        return []
      }
      return buttons;
    },
  }
};
</script>
<style scoped lang="scss">
.img_main {
  .img_item {
    margin-bottom: 20px;
  }

  .dec {
    display: flex;
    justify-content: space-between;
    font-size: 12px;

    .name {
      color: #1890ff !important;
    }

    .state {
      color: #1890ff !important;
    }
  }
}
</style>

<style scoped>
/deep/ .el-divider--horizontal {
  margin: 6px 0;
}
</style>
