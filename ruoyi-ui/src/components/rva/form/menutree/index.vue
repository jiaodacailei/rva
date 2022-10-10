<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <el-checkbox v-model="menuExpand" :disabled="disabled || prop.formReadonly == 'Y'"
                   @change="handleCheckedTreeExpand($event, 'menu')">展开/折叠
      </el-checkbox>
      <el-checkbox v-model="menuNodeAll" :disabled="disabled || prop.formReadonly == 'Y'"
                   @change="handleCheckedTreeNodeAll($event, 'menu')">全选/全不选
      </el-checkbox>
      <el-checkbox v-model="menuCheckStrictly" :disabled="disabled || prop.formReadonly == 'Y'"
                   @change="handleCheckedTreeConnect($event, 'menu')">父子联动
      </el-checkbox>
      <el-tree
        class="tree-border"
        :data="menuOptions"
        show-checkbox
        ref="menu"
        node-key="id"
        :check-strictly="!menuCheckStrictly"
        empty-text="加载中，请稍候"
        :props="defaultProps"
        @check-change="check"
        :vaule="value2"
      ></el-tree>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
  </div>
</template>

<script>

import formInput from "@/api/rva/form-input";
import {RvaUtils} from "@/api/rva/util";

import {treeselect as menuTreeselect, roleMenuTreeselect} from "@/api/system/menu";


export default {
  mixins: [formInput],
  data() {
    return {
      max: 20,
      value2: this.value,
      menuExpand: false,
      menuNodeAll: false,
      // 菜单列表
      menuOptions: [],
      menuCheckStrictly: true,
      defaultProps: {
        children: "children",
        label: "label"
      },
      labelWidth: ''
    }
  },
  mounted() {
    console.log("mounted");
    this.getMenuTreeselect();
    this.initChecked();
  },

  methods: {

    afterCreated() {
      this.max = RvaUtils.parseInt(this.prop.formValueMax, 20);
    },
    /** 查询菜单树结构 */
    getMenuTreeselect() {
      menuTreeselect().then(response => {
        this.menuOptions = response.data;
      });
    },
    // 所有菜单节点数据
    getMenuAllCheckedKeys() {
      // 目前被选中的菜单节点
      let checkedKeys = this.$refs.menu.getCheckedKeys();
      // 半选中的菜单节点
      let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },
    check() {
      this.$emit('input', this.getMenuAllCheckedKeys());

    },
    // 树权限（展开/折叠）
    handleCheckedTreeExpand(value, type) {
      if (type == 'menu') {
        let treeList = this.menuOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
        }

      }
    },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value, type) {
      if (type == 'menu') {
        this.$refs.menu.setCheckedNodes(value ? this.menuOptions : []);
      }
    },
    // 树权限（父子联动）
    handleCheckedTreeConnect(value, type) {
      if (type == 'menu') {
        this.menuCheckStrictly = value ? true : false;
      }
    },
    initChecked() {
      if (!this.value) {
        return;
      }
      let checkedKeys = JSON.parse(this.value);
      this.$refs.menu.setCheckedKeys(checkedKeys, this.menuCheckStrictly);
    }

  }
}
;
</script>
<style>

</style>

