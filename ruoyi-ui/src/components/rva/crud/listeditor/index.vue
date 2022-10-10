<template>
  <el-card>
    <div slot="header" v-if="showTitle">
      <span>{{ appData.name }}</span>
    </div>
    <rva-search
      :requestParams="getRequestParams()"
      :queryParams="queryParams"
      :viewId="appData.searchId"
      :selector="selector"
      v-if="appData.searchId"
    ></rva-search>
    <el-row v-if="!selector">
      <right-toolbar :showSearch.sync="queryParams.showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-form v-if="formListMeta.relatedCrudViewData" ref="rvaListeditorForm" :model="formData">
      <rva-form-crud :hideLabel="true" :prop="formListMeta" :requestParams="getParams()" :formData="formData"
      ></rva-form-crud>
    </el-form>
    <el-row v-if="!selector && formListMeta.relatedCrudViewData">
      <rva-form-button v-for="btn in formListMeta.relatedCrudViewData.create.viewData.buttons" :key="btn.id"
                       :viewButtonData="btn" :formData="formData" :formRef="$refs.rvaListeditorForm"
                       @rva-button-click="handleButtonClick" :requestParams="requestParams"></rva-form-button>
    </el-row>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </el-card>
</template>

<script>
import RvaFormCrud from "@/components/rva/form/crud";
import RvaFormButton from "@/components/rva/form/button";

import crud from "@/components/rva/crud/crud";
import {loadForms} from "@/api/rva/crud";

export default {
  name: 'RvaListeditor',
  mixins: [crud],
  components: {
    RvaFormCrud,
    RvaFormButton
  },
  data() {
    return {
      // 列表数据 {id, name, relatedCrudViewData: {create: {viewData, formData}, update: {viewData, formData, formDataList}}}
      formListMeta: {},
      // {list: update.formDataList + create.formData}
      formData: {},
      total: 0
    };
  },
  methods: {
    metaLoaded() {
      this.getList()
    },
    getList() {
      loadForms(this.appData.createId, this.appData.updateId, this.getParams()).then(res => {
        // 初始化formData
        let formDataList = res.data.update.formDataList;
        formDataList.push(res.data.create.formData);
        this.formData = {
          [res.data.create.viewData.id] : formDataList
        };

        this.formListMeta = {
          id: res.data.create.viewData.id,
          name: res.data.create.viewData.name,
          relatedCrudViewData: res.data
        }

        console.log('listeditor', this.formListMeta, this.formData)
      });
    },
    getParams() {
      return {...this.requestParams, ...this.queryParams};
    },
    loadList(action) {
      this.getList();
    }
  }
};
</script>
