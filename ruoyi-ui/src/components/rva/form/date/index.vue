<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>

      <el-date-picker
        value-format="yyyy-MM-dd"
        v-model="value2"
        align="right"
        type="date"
        placeholder="选择日期"
        :picker-options="pickerOptions"
        @input="$emit('input', $event)"
        :disabled="disabled"
        :readonly="prop.formReadonly == 'Y'"
        style="width: 100%"
      >
      </el-date-picker>
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
      max: 20,
      value2: this.value,
    }
  },
  computed: {
    pickerOptions() {
      if (!RvaUtils.isPC()) {
        return {}
      }
      return {
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      }
    }
  },
  methods: {
    afterCreated() {
      this.max = RvaUtils.parseInt(this.prop.formValueMax, 20);
    }
    ,
  }


}
;
</script>
