<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <el-select :placeholder="'请选择' + prop.name" :multiple="this.prop.formSelectorSingle == 'N'" :value="value"
                 clearable
                 filterable
                 v-bind:allow-create="allowCreate"
                 @input="$emit('input', $event)" style="width: 100%" :disabled="disabled || prop.formReadonly == 'Y'">
        <el-row v-for=" (array,index) in dictDataArrays" :key="index">
          <el-col v-for="dict in array" :span="24/columns" :key="dict.dictValue">
            <el-option :key="dict.dictValue" :label="getLabel(dict)" :value="getValue(dict)"></el-option>
          </el-col>
        </el-row>
      </el-select>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
  </div>
</template>

<script>
import formInput from "@/api/rva/form-input";
import {RvaUtils} from "@/api/rva/util";

export default {
  mixins: [formInput],
  data() {
    return {
      columns: 1,
      dictDataArrays: [],
      allowCreate: false
    }
  },
  methods: {
    afterDict(dictType, obj, objProp) {
      this.initDictDataArrays();
      this.getAllowCreate();
    },
    getLabel: function (dict) {
      let $$ = {...dict};
      $$.keyPropValue = dict.dictValue;
      $$.namePropValue = dict.dictLabel;
      var selectorOptionLabel = RvaUtils.parseValue(this.prop.data, 'selectorOptionLabel');
      if (selectorOptionLabel) {
        return RvaUtils.parseJsValue(selectorOptionLabel, $$);
      }
      return dict.dictLabel;
    },
    getValue: function (dict) {
      let $$ = {...dict};
      $$.keyPropValue = dict.dictValue;
      $$.namePropValue = dict.dictLabel;
      var selectorOptionValue = RvaUtils.parseValue(this.prop.data, 'selectorOptionValue');
      if (selectorOptionValue) {
        return RvaUtils.parseJsValue(selectorOptionValue, $$);
      }
      return dict.dictValue;
    },
    initDictDataArrays() {
      let length = this.dictData ? this.dictData.length : 0;
      let columns = Math.ceil(this.dictData.length / 7);
      columns = columns >= 3 ? 3 : columns
      let rows = Math.ceil(length / columns);
      this.columns = columns;
      let dictDataArray = []
      let j = 0;

      for (let i = 0; i < rows; i++) {
        dictDataArray[i] = [];
        for (let k = 0; k < columns; k++) {
          if (j >= length) {
            break;
          }
          dictDataArray[i].push(this.dictData[j])
          j++
        }
      }
      // console.log('dictDataArray==', dictDataArray)
      this.dictDataArrays = dictDataArray;
    },
    getAllowCreate() {
      this.allowCreate = RvaUtils.parseValue(this.prop.data, 'selectAllowCreate', 'N') == 'Y'
    },
    refreshData() {
      // this.setValue();
      this.searchSelectOptions('#$@$#');
    },

  }

}
</script>
