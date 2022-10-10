<template>
  <el-form-item :label="prop.name" :prop="prop.id" :label-width="labelWidth">
    <template slot="label">
      <rva-search-config :prop="prop"></rva-search-config>
    </template>


    <el-date-picker
      :value="value"
      type="datetimerange"
      :picker-options="pickerOptions"
      @input="change"
      range-separator="至"
      start-placeholder="开始日期"
      end-placeholder="结束日期"
      align="right">
    </el-date-picker>

  </el-form-item>
</template>

<script>

import search from "@/api/rva/search";
import {parseTime} from "@/utils/ruoyi"

export default {
  mixins: [search],
  name: "RvaSearchDatetimeRange",
  data() {
    return {
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);

            picker.$emit('pick', [parseTime(start), parseTime(end)]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [parseTime(start), parseTime(end)]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [parseTime(start), parseTime(end)]);
          }
        }]
      },
    };
  },
  methods: {
    change(value) {
      this.$emit('input', this.parseTimeArray(value));
    },
    parseTimeArray(array){
      if(array){
        return [parseTime(array[0]),parseTime(array[1])];
      }
    }
  },
};
</script>
