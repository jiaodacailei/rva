<template>
  <el-form-item :label="label">
    <el-select :placeholder="'请选择' + label" :multiple="multiple" v-bind:value="value"
               v-on:input="$emit('input', $event)">
      <el-option v-for="dict in dictData" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"></el-option>
    </el-select>
  </el-form-item>
</template>

<script>
export default {
  name: "AutoSelect",
  data () {
    return {
      dictData: []
    }
  },
  props : ['dictType', 'label', 'value', 'multiple'],
  created() {
    console.log('AutoSelect created');
    if (this.dictType != '') {
      this.getDicts(this.dictType).then(response => {
        console.log('AutoSelect getDicts');
        this.dictData = response.data;
      });
    }
  },
  methods: {

  }
}
</script>
