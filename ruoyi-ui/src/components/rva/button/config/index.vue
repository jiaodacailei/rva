<template>
  <el-dropdown v-if="devMode" @command="executeCommand" :show-timeout="showTimeout">
    <span :style="{color:getColor()}" :class="button.icon">{{' ' + button.name}}</span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item disabled><span class="el-icon-setting"/> 按钮管理</el-dropdown-item>
      <el-dropdown-item command="configButton"><span class="el-icon-setting dropdown-item2"/> 配置按钮</el-dropdown-item>
      <el-dropdown-item command="cloneButton"><span class="el-icon-document-copy dropdown-item2"/> 克隆按钮</el-dropdown-item>
      <el-dropdown-item command="deleteButton" style="color: #F56C6C"><span class="el-icon-delete dropdown-item2"/> 删除按钮</el-dropdown-item>

      <el-dropdown-item disabled><span class="el-icon-circle-plus"/> 模板创建</el-dropdown-item>
      <template v-if="button.viewId.indexOf('l') == 0">
        <el-dropdown-item v-if="button.position == 'top'" command="addListCreateButton"><span class="el-icon-plus dropdown-item2"/> 新建按钮</el-dropdown-item>
        <el-dropdown-item v-if="button.position == 'top'" command="addListDeleteTopButton"><span class="el-icon-delete dropdown-item2"/> 删除按钮</el-dropdown-item>
        <el-dropdown-item v-if="button.position == 'inner'" command="addListDeleteInnerButton"><span class="el-icon-delete dropdown-item2"/> 删除按钮</el-dropdown-item>
        <el-dropdown-item v-if="button.position == 'inner'" command="addListUpdateButton"><span class="el-icon-edit dropdown-item2"/> 修改按钮</el-dropdown-item>
        <el-dropdown-item v-if="button.position == 'inner'" command="addListCloneButton"><span class="el-icon-document-copy dropdown-item2"/> 克隆按钮</el-dropdown-item>
        <el-dropdown-item v-if="button.position == 'top'" command="addListMoveUpButton"><span class="el-icon-top dropdown-item2"/> 上移按钮</el-dropdown-item>
        <el-dropdown-item v-if="button.position == 'top'" command="addListMoveDownButton"><span class="el-icon-bottom dropdown-item2"/> 下移按钮</el-dropdown-item>
        <el-dropdown-item v-if="button.position == 'top'" command="addListImportButton"><span class="el-icon-upload2 dropdown-item2"/> 导入按钮</el-dropdown-item>
        <el-dropdown-item v-if="button.position == 'top'" command="addListExportButton"><span class="el-icon-upload2 dropdown-item2"/> 导出按钮</el-dropdown-item>
      </template>
      <template v-else>
        <el-dropdown-item command="addFormCreateButton"><span class="el-icon-check dropdown-item2"/> 提交新建按钮</el-dropdown-item>
        <el-dropdown-item command="addFormUpdateButton"><span class="el-icon-check dropdown-item2"/> 提交修改按钮</el-dropdown-item>
        <el-dropdown-item command="addFormResetButton"><span class="el-icon-refresh-left dropdown-item2"/> 重置按钮</el-dropdown-item>
        <el-dropdown-item command="addFormCancelButton"><span class="el-icon-close dropdown-item2"/> 取消按钮</el-dropdown-item>
      </template>

      <el-dropdown-item disabled><span class="el-icon-circle-plus-outline"/> 定制创建</el-dropdown-item>
      <template v-if="button.viewId.indexOf('l') == 0">
        <el-dropdown-item command="addListButtonBySelectForm"><span class="el-icon-circle-plus-outline dropdown-item2"/> 选择表单建按钮</el-dropdown-item>
        <el-dropdown-item command="addListButtonByCreateForm"><span class="el-icon-circle-plus-outline dropdown-item2"/> 新建表单建按钮</el-dropdown-item>
        <el-dropdown-item command="addListButtonBySelectCrud"><span class="el-icon-circle-plus-outline dropdown-item2"/> 选择crud建按钮</el-dropdown-item>
        <el-dropdown-item command="addListButtonByCreateTcrud"><span class="el-icon-circle-plus-outline dropdown-item2"/> 新建tcrud建按钮</el-dropdown-item>
        <el-dropdown-item command="addListButtonBySelectTcrud"><span class="el-icon-circle-plus-outline dropdown-item2"/> 选择tcrud建按钮</el-dropdown-item>
        <el-dropdown-item command="addListAjaxButton"><span class="el-icon-circle-plus-outline dropdown-item2"/> 后端请求按钮</el-dropdown-item>
        <el-dropdown-item command="addListDictButtons"><span class="el-icon-circle-plus-outline dropdown-item2"/> 字典按钮</el-dropdown-item>
      </template>
      <template v-else>
        <el-dropdown-item command="addFormAjaxButton"><span class="el-icon-circle-plus-outline dropdown-item2"/> 后端请求按钮</el-dropdown-item>
        <el-dropdown-item command="addFormJsButton"><span class="el-icon-circle-plus-outline dropdown-item2"/> 前端请求按钮</el-dropdown-item>
      </template>
      <el-dropdown-item command="addButtonToMenu" divided><span class="el-icon-circle-plus"/> 加入菜单</el-dropdown-item>
    </el-dropdown-menu>
    <el-dialog :visible.sync="openConfig"  width="1200px" append-to-body v-if="openConfig">
      <template slot="title">
        配置按钮<el-tag>{{button.name}}</el-tag>
      </template>
      <rva-tcrud :appId="button.viewId.indexOf('l') == 0 ? 'tcrud0_rva_viewbutton' : 'tcrud1_rva_viewbutton'"
                 :requestParams="{rvaAppParams: button}" @rva-button-click="handleFormButtonClick"></rva-tcrud>
    </el-dialog>
    <el-dialog :visible.sync="openClone" width="900px" append-to-body v-if="openClone">
      <template slot="title">
        克隆按钮<el-tag>{{button.name}}</el-tag>
      </template>
      <rva-form :viewId="button.viewId.indexOf('l') == 0 ? 'c6_rva_viewbutton' : 'c7_rva_viewbutton'"
                :requestParams="{rvaAppParams: button}" @rva-button-click="handleFormButtonClick"
      ></rva-form>
    </el-dialog>
    <el-dialog :visible.sync="openSelectForm" width="800px" append-to-body v-if="openSelectForm">
      <template slot="title">
        选择表单建按钮<el-tag>{{button.name}}</el-tag>
      </template>
      <rva-form viewId="c0_none_xuanzebiaodanjiananniu" :requestParams="{rvaAppParams: button}"
                @rva-button-click="handleFormButtonClick"></rva-form>
    </el-dialog>
    <el-dialog :visible.sync="openSelectImportForm" width="600px" append-to-body v-if="openSelectImportForm">
      <template slot="title">
        选择新建表单视图创建按钮<el-tag>导入</el-tag>
      </template>
      <rva-form viewId="c0_none_daoruxuanzebiaodan" :requestParams="{rvaAppParams: button}" @rva-button-click="handleFormButtonClick"></rva-form>
    </el-dialog>
    <el-dialog :visible.sync="openCreateForm" width="1200px" append-to-body v-if="openCreateForm">
      <template slot="title">
        新建表单建按钮<el-tag>{{button.name}}</el-tag>
      </template>
      <rva-form :viewId="'c10_rva_view'" :requestParams="{rvaAppParams: button}" @rva-button-click="handleFormButtonClick"
      ></rva-form>
    </el-dialog>
    <el-dialog :visible.sync="openSelectCrud"  width="800px" append-to-body v-if="openSelectCrud">
      <template slot="title">
        选择crud建按钮<el-tag>{{button.name}}</el-tag>
      </template>
      <rva-form viewId="c0_none_xuanzecrudjiananniu" :requestParams="{rvaAppParams: button}" @rva-button-click="handleFormButtonClick"
      ></rva-form>
    </el-dialog>
    <el-dialog :visible.sync="openCreateTcrud"  width="800px" append-to-body v-if="openCreateTcrud">
      <template slot="title">
        新建tcrud建按钮<el-tag>{{button.name}}</el-tag>
      </template>
      <rva-form viewId="cc0_none" :requestParams="{rvaAppParams: button}" @rva-button-click="handleFormButtonClick"
      ></rva-form>
    </el-dialog>
    <el-dialog :visible.sync="openSelectTcrud"  width="800px" append-to-body v-if="openSelectTcrud">
      <template slot="title">
        选择tcrud建按钮<el-tag>{{button.name}}</el-tag>
      </template>
      <rva-form viewId="cc1_none" :requestParams="{rvaAppParams: button}" @rva-button-click="handleFormButtonClick"
      ></rva-form>
    </el-dialog>
    <el-dialog :visible.sync="openListAjax" width="900px" append-to-body v-if="openListAjax">
      <template slot="title">
        后端请求按钮<el-tag>{{button.name}}</el-tag>
      </template>
      <rva-form viewId="cc0_rva_viewbutton" :requestParams="{rvaAppParams: button}" @rva-button-click="handleFormButtonClick"
      ></rva-form>
    </el-dialog>
    <el-dialog :visible.sync="openFormAjax" width="900px" append-to-body v-if="openFormAjax">
      <template slot="title">
        后端请求按钮<el-tag>{{button.name}}</el-tag>
      </template>
      <rva-form viewId="cc1_rva_viewbutton" :requestParams="{rvaAppParams: button}" @rva-button-click="handleFormButtonClick"
      ></rva-form>
    </el-dialog>
    <el-dialog :visible.sync="openFormJs" width="900px" append-to-body v-if="openFormJs">
      <template slot="title">
        前端请求按钮<el-tag>{{button.name}}</el-tag>
      </template>
      <rva-form viewId="cc2_rva_viewbutton" :requestParams="{rvaAppParams: button}" @rva-button-click="handleFormButtonClick"
      ></rva-form>
    </el-dialog>
  </el-dropdown>
  <span v-else :style="{color:getColor()}" :class="button.icon">{{' ' + button.name}}</span>
</template>

<script>

import request from "@/utils/request";
import config from "@/api/rva/config";
import {RvaUtils} from "@/api/rva/util";

export default {
  mixins:[config],
  props: ['button'],
  data() {
    return {
      openConfig : false,
      openClone : false,
      openCreateForm: false,
      openSelectForm: false,
      openSelectCrud: false,
      openCreateTcrud: false,
      openSelectTcrud: false,
      openListAjax: false,
      openFormAjax: false,
      openFormJs: false,
      openSelectImportForm: false
    }
  },
  methods: {
    configButton() {
      this.openConfig = true;
    },
    cloneButton() {
      this.openClone = true;
    },
    getColor () {
      return RvaUtils.getColor(this.button.cls);
    },
    ajax(method, actionSuccess) {
      if (!actionSuccess) {
        actionSuccess = 'reloadView';
      }
      request({
        url: '/rva/metaapp/button/' + this.button.id + '/' + method,
        method: 'post'
      }).then(response => {
        this.$emit('rva-button-click', [{action: 'reloadView'}]);
      });
    },
    closeView () {
      this.openSelectForm = false
      this.openSelectImportForm = false
      this.openCreateForm = false
      this.openConfig = false
      this.openClone = false
      this.openSelectCrud = false
      this.openCreateTcrud = false;
      this.openSelectTcrud = false;
      this.openListAjax = false;
      this.openFormAjax = false;
      this.openFormJs = false;
    },
    deleteButton() {
      if (confirm('确定要删除吗？')) {
        this.ajax('delete');
      }
    },
    addButtonToMenu() {
      this.ajax('addButtonToMenu');
    },
    addListCreateButton() {
      this.ajax('createCrudCreateButton');
    },
    addListDeleteTopButton () {
      this.ajax('createTopDeleteButton');
    },
    addListDeleteInnerButton () {
      this.ajax('createInnerDeleteButton');
    },
    addListUpdateButton () {
      this.ajax('createCrudUpdateButton');
    },
    addListCloneButton () {
      this.ajax('createCrudCloneButton');
    },
    addListMoveUpButton () {
      this.ajax('createMoveUpButton');
    },
    addListMoveDownButton () {
      this.ajax('createMoveDownButton');
    },
    addListImportButton () {
      this.openSelectImportForm = true
    },
    addListExportButton () {
      this.ajax('createExportButton');
    },
    addFormCreateButton () {
      this.ajax('createSubmitCreateButton');
    },
    addFormUpdateButton () {
      this.ajax('createSubmitUpdateButton');
    },
    addFormResetButton () {
      this.ajax('createResetButton');
    },
    addFormCancelButton () {
      this.ajax('createCancelButton');
    },
    addListButtonBySelectForm () {
      this.openSelectForm = true
    },
    addListButtonByCreateForm () {
      this.openCreateForm = true
    },
    addListButtonBySelectCrud () {
      this.openSelectCrud = true
    },
    addListButtonByCreateTcrud() {
      this.openCreateTcrud = true;
    },
    addListButtonBySelectTcrud() {
      this.openSelectTcrud = true;
    },
    addListAjaxButton() {
      this.openListAjax = true;
    },
    addListDictButtons() {
      this.ajax('createListDictButtons');
    },
    addFormAjaxButton() {
      this.openFormAjax = true
    },
    addFormJsButton() {
      this.openFormJs = true
    }
  }
};
</script>
<style>
.dropdown-item2 {
  margin-left: 20px
}

</style>
