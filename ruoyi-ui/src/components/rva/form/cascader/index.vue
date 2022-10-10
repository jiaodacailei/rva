<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <el-cascader :placeholder="'请选择' + prop.name" :value="value" clearable :options="dictData"
                   @change="$emit('input', $event)" style="width: 100%" :disabled="disabled || prop.formReadonly == 'Y'">
      </el-cascader>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
  </div>
</template>

<script>
import formInput from "@/api/rva/form-input";

export default {
  mixins: [formInput],
  methods: {
    afterDict(dictType, obj, objProp) {
      let dictData = obj[objProp];
      let results = [];
      for (let i = dictData.length - 1; i >= 0; i--) {
        let dictItem = dictData[i];
        if (!dictItem.children) {
          dictItem.leaf = true;
        }
        dictItem.value = dictItem.dictValue;
        dictItem.label = dictItem.dictLabel;
        let hasParent = false;
        for (let j = i - 1; j >= 0; j--) {
          let dictItem2 = dictData[j];
          if (dictItem.dictValue.indexOf(dictItem2.dictValue) == 0) {
            let children = dictItem2.children;
            if (!children) {
              children = [dictItem];
              dictItem2.children = children;
            } else {
              children.splice(0, 0, dictItem)
            }
            hasParent = true;
            break;
          }
        }
        if (!hasParent) {
          results.splice(0, 0, dictItem);
        }
      }
      obj[objProp] = results;
      console.log('this.dictData', this.dictData)
    },
  }
}
</script>
