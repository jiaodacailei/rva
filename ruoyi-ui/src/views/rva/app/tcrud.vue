<template>
  <div class="app-container">
    <rva-tcrud :appId="appId" v-if="appId!=''" :requestParams="requestParams"></rva-tcrud>
  </div>
</template>

<script>
import RvaTreeCrud from "@/components/rva/tcrud";
import initApp from "@/views/rva/initApp";

export default {
  name: "RvaAppTcrud",
  components: {
    "rva-tcrud": RvaTreeCrud
  },
  mixins: [initApp]
};
</script>
