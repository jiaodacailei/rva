<template>
  <div class="app-container">
    <rva-crud :appId="appId" v-if="appId!=''" :requestParams="requestParams"></rva-crud>
  </div>
</template>

<script>
import RvaCrud from "@/components/rva/crud";
import initApp from "../initApp";

export default {
  name: "RvaAppCrud",
  components: {
    "rva-crud": RvaCrud
  },
  mixins: [initApp]
};
</script>
