<template>
  <div class="app-container">
    <rva-listeditor :appId="appId" v-if="appId!=''" :requestParams="requestParams"></rva-listeditor>
  </div>
</template>

<script>
import RvaListeditor from "@/components/rva/crud/listeditor";
import initApp from "../initApp";

export default {
  name: "RvaAppListeditor",
  components: {
    RvaListeditor
  },
  mixins: [initApp]
};
</script>
