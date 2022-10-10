<template>
  <el-collapse accordion v-loading="loading" v-model="activeItem">
    <el-collapse-item v-for="row in listData" :name="row.keyPropValue" :columns="1" :key="row.keyPropValue">
      <template slot="title">
        <div class="mobile-item-title">
          <span>{{ getRowTitle(row) }}</span>
          <div class="mobile-item-action">
            <el-button size="mini" type="text" @click.stop="showDetail(row)"
                       class="el-icon-right"> 详情
            </el-button>
            <div @click.stop="">
              <el-checkbox v-model="row.selected" @change="handleSelectionChange(row, $event)">
              </el-checkbox>
            </div>
          </div>
        </div>
      </template>
      <el-card shadow="always" style="margin: 8px">
        <el-descriptions :column="1" border direction="vertical">
          <el-descriptions-item v-for="prop in shortProperties" :key="prop.id">
            <template slot="label">
              {{ prop.name }}
            </template>
            <component :is="`rva-list-mobile-${getPropComponent(prop)}`" :row="row" :prop="prop"
                       :requestParams="requestParams"/>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
      <el-row style="margin-top: 10px" v-if="!selector">
        <el-col v-for="(btn, btnIndex) in getPropButtons(row)"
                :span="getPropButtonColSpan(row)"
                v-if="getPropButtons(row).length <= 4 || btnIndex < 3" :key="row.keyPropValue + '-' + btn.id">
          <rva-list-inner-button
            :viewButtonData="btn"
            :selection="[row]"
            :requestParams="requestParams"
            @rva-button-click="handleButtonClick"
            :viewData="viewData"
          ></rva-list-inner-button>
        </el-col>
        <el-col :span="6" v-if="getPropButtons(row).length > 4">
          <el-dropdown>
            <el-button type="text" size="mini">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="(btn, btnIndex) in getPropButtons(row)"
                                :key="row.keyPropValue + '-' + btn.id"
                                v-if="btnIndex > 2">
                <rva-list-inner-button
                  :viewButtonData="btn"
                  :selection="[row]"
                  :requestParams="requestParams"
                  @rva-button-click="handleButtonClick"
                  :viewData="viewData"
                ></rva-list-inner-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-col>
      </el-row>
    </el-collapse-item>
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
  </el-collapse>
</template>

<script>

import mobile from "@/components/rva/list/mobile/mobile"

export default {
  name: "RvaListMobile",
  mixins: [mobile]
};
</script>
<style>
.mobile-item-title {
  width: 92%;
  display: flex;
  justify-content: space-between;
  flex-direction: row;
}

.mobile-item-action {
  display: flex;
  justify-content: space-between;
  flex-direction: row;
  width: 80px;
  margin-right: 10px;
}
</style>
