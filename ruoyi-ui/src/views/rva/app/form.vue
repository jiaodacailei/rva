<template>
  <div class="app-container">
    <rva-form-view :viewId="appId" v-if="appId!=''" :requestParams="requestParams"></rva-form-view>
  </div>
</template>

<script>
import initApp from "../initApp";
import RvaFormView from "@/components/rva/form";

export default {
  components: {
    RvaFormView
  },
  mixins: [initApp]
};
</script>
