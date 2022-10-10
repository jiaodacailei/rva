<template>
  <div>
    <el-tag type="info" style="width: auto">
      {{ value }}
    </el-tag>
  </div>
</template>

<script>
import mobileComponent from "@/components/rva/list/mobile/mobileComponent";

export default {
  name: "RvaListMobileTextfield",
  mixins: [mobileComponent]
}
</script>

<style scoped>

</style>
