<template>
  <div v-loading="loading">
    <el-card v-for="row in listData" :key="row.keyPropValue" shadow="always" style="margin-bottom: 5px"
             :class="row.selected ? 'mobile-item-selected' : ''">
      <div class="mobile-item" @click.stop="handleSelectionChange(row)">
        <div class="mobile-item-col1">
          <component :is="`rva-list-mobile-${getPropComponent(propTitle)}`" :row="row" :prop="propTitle"
                     :requestParams="requestParams"/>
          <component :is="`rva-list-mobile-${getPropComponent(propDetail)}`" :row="row" :prop="propDetail"
                     :requestParams="requestParams" style="margin-top: 5px"/>
        </div>
        <div class="mobile-item-col2">
          <component :is="`rva-list-mobile-${getPropComponent(propRight)}`" :row="row" :prop="propRight"
                     :requestParams="requestParams" style="text-align: right"/>

          <component :is="`rva-list-mobile-${getPropComponent(propRight2)}`" :row="row" :prop="propRight2"
                     :requestParams="requestParams" style="text-align: right"/>
        </div>
      </div>
      <el-divider></el-divider>
      <div class="mobile-btn-group" v-if="!selector" @click.stop="handleSelectionChange(row)">
        <rva-list-inner-button
          class="mobile-btn"
          v-for="(btn, btnIndex) in getPropButtons(row)"
          :key="row.keyPropValue + '-' + btn.id"
          :viewButtonData="btn"
          :selection="[row]"
          :requestParams="requestParams"
          @rva-button-click="handleButtonClick"
          :viewData="viewData"
        ></rva-list-inner-button>
        <el-button size="mini" type="text" @click.stop="showDetail(row)"
                   class="el-icon-right" style="margin-top: -3px;color: darkgray"> 详情
        </el-button>
      </div>
    </el-card>
    <el-drawer
      v-if="selection.length > 0"
      append-to-body
      title="列表详情"
      :visible.sync="drawer"
      direction="rtl"
      size="70%">
      <el-descriptions :column="1" border direction="vertical">
        <el-descriptions-item v-for="(prop, index) in viewData.propertiesWithoutHidden" :key="prop.id"
                              v-if="prop.type != 'button'">
          <template slot="label">
            {{ prop.name }}
          </template>
          <component :is="`rva-list-mobile-${getPropComponent(prop)}`" :row="selection[0]" :prop="prop"
                     :requestParams="requestParams"/>
        </el-descriptions-item>
      </el-descriptions>
    </el-drawer>
  </div>
</template>

<script>

import mobile from "@/components/rva/list/mobile/mobile"

export default {
  name: "RvaListMobile",
  mixins: [mobile],
  computed: {
    propTitle() {
      return this.viewData.propertiesWithoutHidden[0];
    },
    propDetail() {
      return this.viewData.propertiesWithoutHidden[1];
    },
    propRight() {
      return this.viewData.propertiesWithoutHidden[2];
    },
    propRight2() {
      return this.viewData.propertiesWithoutHidden[3];
    },
  }
};
</script>
<style scoped>

.mobile-item {
  width: 100%;
  display: flex;
  -webkit-box-pack: justify;
  -webkit-box-align: center;
  flex-direction: row;
  justify-content: space-between;
}

.mobile-item-selected {
  border: 2px solid #00afff;
}

.mobile-item-col1 {
  display: flex;
  flex: 7;
  width: 0;
  flex-direction: column;
  overflow: hidden;
}

.mobile-item-col2 {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  flex: 3;
  width: 0;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  -ms-flex-direction: column;
  flex-direction: column;
  justify-content: space-around;
  overflow: hidden;
}

.mobile-btn-group {
  display: flex;
  flex-direction: row;
  font-size: x-small;
}

.mobile-btn {
  margin-right: 10px;
}

.el-divider--horizontal {
  display: block;
  height: 1px;
  width: 100%;
  margin: 10px 0;
}
</style>
