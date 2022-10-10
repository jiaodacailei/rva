<template>
  <div class="app-container">
    <rva-portal :appId="appId" v-if="appId!=''" :requestParams="requestParams"></rva-portal>
  </div>
</template>

<script>
import RvaPortal from "@/components/rva/portal";
import initApp from "../initApp";

export default {
  name: "RvaAppPortal",
  components: {
    RvaPortal
  },
  mixins: [initApp]
};
</script>
