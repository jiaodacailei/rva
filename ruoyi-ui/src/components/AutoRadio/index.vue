<template>
  <el-form-item :label="label">
    <el-radio-group v-bind:value="value" v-on:input="$emit('input', $event)">
      <el-radio
          v-for="dict in dictData"
          :key="dict.dictValue"
          :label="dict.dictValue"
      >{{dict.dictLabel}}</el-radio>
    </el-radio-group>
  </el-form-item>
</template>

<script>
export default {
  name: "AutoRadio",
  data() {
    return {
      dictData: []
    }
  },
  props : ['dictType', 'label', 'value'],
  created() {
    console.log('AutoRadio created');
    if (this.dictType != '') {
      this.getDicts(this.dictType).then(response => {
        console.log('AutoRadio getDicts');
        this.dictData = response.data;
      });
    }
  }
}
</script>
