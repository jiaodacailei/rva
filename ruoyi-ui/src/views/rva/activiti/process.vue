<template>
  <div class="app-container">
    <rva-activiti :appId="appId" :requestParams="requestParams"></rva-activiti>
  </div>
</template>

<script>
import RvaActiviti from "@/components/rva/activiti";
import initApp from "@/views/rva/initApp";

export default {
  name: "RvaActivitiProcess",
  components: {
    RvaActiviti
  },
  mixins: [initApp]
};
</script>
