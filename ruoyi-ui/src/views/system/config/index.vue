<template>
  <div class="app-container">
    <rva-crud :appId="appId" v-if="appId!=''" :requestParams="requestParams"></rva-crud>
  </div>
</template>

<script>
import RvaCrud from "@/components/rva/crud";

export default {
  name: "systemConfig",
  data() {
    return {
      appId: "crud0_sys_config",
      requestParams: {trigger: 0}
    }
      ;
  },
  components: {
    "rva-crud": RvaCrud
  },
};
</script>
