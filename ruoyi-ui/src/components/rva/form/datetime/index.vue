<template>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>

      <el-date-picker
        style="width:100%"
        v-model="value2"
        type="datetime"
        placeholder="选择日期时间"
        align="right"
        :picker-options="pickerOptions"
        value-format="yyyy-MM-dd HH:mm:ss"
        @input="change"
        :disabled="disabled"
        :readonly="prop.formReadonly == 'Y'"
      >
      </el-date-picker>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>

</template>

<script>

import formInput from "@/api/rva/form-input";
import {RvaUtils} from "@/api/rva/util";
import {parseTime} from "@/utils/ruoyi"

export default {
  mixins: [formInput],
  data() {
    return {
      max: 20,
      value2: this.value,
    }
  },
  computed :{
    pickerOptions() {
      if (!RvaUtils.isPC()) {
        return {}
      }
      return {
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', parseTime(new Date()));
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', parseTime(date));
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', parseTime(date));
          }
        }]
      }
    }
  },
  methods: {
    afterCreated() {
      this.max = RvaUtils.parseInt(this.prop.formValueMax, 20);
    },
    change(value) {
      this.$emit('input', parseTime(value));
    }
  }
};
</script>
