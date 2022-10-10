<template>
  <div>
    <el-divider></el-divider>
    <el-tag type="info">
      {{ tip }}
    </el-tag>
  </div>
</template>

<script>

export default {
  props: ['tip']
}
</script>

<style scoped>
/deep/.el-divider {
  margin: 0 0;
}
/deep/.el-divider--horizontal {
  height: 0px;
}
</style>
